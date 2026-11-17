# CSYE6200 Notes: Generics

**English edition** · [繁體中文](generics.zh-TW.md)

> Personal study notes: the class content is reorganized in my own words. These notes contain no slide text or images, and the page numbers are only there to match the course handout. All example programs are my own, and every error message and output was actually produced with `javac` / `java` (JDK 25).

- Course: CSYE6200 (Fall 2026)
- Unit: Generics (`10_Generics.pdf`, 21 pages; these notes currently cover up to **p.8**, p.9 onward to be continued)
- Date: 2026-11-17 (class reached p.8; there was also a class discussion on C compilation, static/late binding, and the order of the compiler and the JVM, see Section 11)
- Runnable example programs: [`../src/`](../src) (PrintArrayOverload, GenericPrint)
- Prerequisites: Week 5 Method Overloading, Wrapper Classes; Week 8 [Generics supplement in the ArrayList notes](../../week-08/notes/arraylist-abstract-interfaces.en.md#supplement-generics)

## Table of contents

1. [The problem: same task, different types (p.2)](#1-the-problem-same-task-different-types-p2)
2. [Solution 1: Overloading (p.3)](#2-solution-1-overloading-p3)
3. [Can we write just one method? (p.4)](#3-can-we-write-just-one-method-p4)
4. [How to write a generic method (p.5)](#4-how-to-write-a-generic-method-p5)
5. [Calling a generic method (p.6)](#5-calling-a-generic-method-p6)
6. [Two benefits of generics (p.7)](#6-two-benefits-of-generics-p7)
7. [Generic method rules and terms (p.8)](#7-generic-method-rules-and-terms-p8)
8. [Slide syntax corrections](#8-slide-syntax-corrections)
9. [Common mistakes](#9-common-mistakes)
10. [Checklist](#10-checklist)
11. [Class discussion: C compilation, binding, the compiler and the JVM](#11-class-discussion-c-compilation-binding-the-compiler-and-the-jvm)
12. [Class notes (to be added)](#12-class-notes-to-be-added)

---

## 1. The problem: same task, different types (p.2)

The requirement: there is an `int` array, a `double` array and a `long` array (maybe a `float` one too), and for each of them we want to "print the elements one by one".

The work is exactly the same; only the **element type** differs. Java is a strongly typed language, and an `int[]` parameter cannot accept a `double[]`, so the first solution that comes to mind is **method overloading** from Week 5.

## 2. Solution 1: Overloading (p.3)

Use the same method name `printArray` and write three versions, with parameter types `int[]`, `double[]` and `long[]`. The three methods have different signatures (different parameter types), so they can coexist; at the call site the compiler picks a version based on the argument type (review from Week 5: signature = name + number and types of parameters, not including the return type).

Example: [`PrintArrayOverload.java`](../src/PrintArrayOverload.java)

```java
public static void printArray(int[] inputArray) {
    for (int element : inputArray) {
        System.out.printf("%s ", element);
    }
    System.out.println();
}
// double[] version, long[] version: only the types in two places differ; everything else is identical
```

Output:

```text
1 2 3 
1.5 2.5 
10000000000 20000000000 
```

**Problems with this solution**

| Problem | Explanation |
|---|---|
| Duplicate code | Apart from the type, every line of the three methods is the same |
| Types you did not write cannot be used | To print a `float[]` or `String[]` you have to write another version; if you don't, it is a compile error |
| Hard to maintain | To change the separator from a space to a comma, you must edit every version; miss one and they become inconsistent |

> Note: `printf("%s ", element)` works for any type. `%s` converts the value to a string (objects call `toString()`), so `int`, `double`, `Character` and `String` can all be printed. This is also why the generic version on p.5 can reuse the same line.

## 3. Can we write just one method? (p.4)

The slide asks: can we write just **one** method that accepts an array of "any type" and prints its elements? The answer is yes, with **generics**.

Core idea: make the "type" a parameter too. Ordinary parameters pass **values**; generics add another kind of parameter that passes **types**.

## 4. How to write a generic method (p.5)

Example: [`GenericPrint.java`](../src/GenericPrint.java)

```java
public static <E> void printArray(E[] inputArray) {
    for (E element : inputArray) {
        System.out.printf("%s ", element);
    }
    System.out.println();
}
```

Breaking down the method header piece by piece:

| Piece | Meaning |
|---|---|
| `public static` | Same modifiers as an ordinary method (the slide leaves out `static`, see [Section 8](#8-slide-syntax-corrections)) |
| `<E>` | **Type parameter section**: declares "this method has one type parameter, called E". It is always written **before the return type** |
| `void` | Return type |
| `printArray` | Method name |
| `E[] inputArray` | Parameter: "an array of type E" |
| `for (E element : ...)` | E is also used inside the method to declare a local variable |

Compared with the overloading version: replace every `int` / `double` / `long` with `E`, then add `<E>` in front to declare it. Three methods become one.

> Memory tip: `<E>` is the "declaration", and the later `E`s are "uses". Just as a variable must be declared before it is used, a type parameter must first be declared inside `< >`.

## 5. Calling a generic method (p.6)

```java
Integer[] intArray = {1, 2, 3, 4, 5};
Double[] doubleArray = {1.1, 2.2, 3.3, 4.4};
Character[] charArray = {'H', 'E', 'L', 'L', 'O'};

printArray(intArray);     // E = Integer
printArray(doubleArray);  // E = Double
printArray(charArray);    // E = Character
```

Output:

```text
Array integerArray contains:
1 2 3 4 5 

Array doubleArray contains:
1.1 2.2 3.3 4.4 

Array characterArray contains:
H E L L O 
```

Key points:

1. **You don't write the type when calling**: the compiler sees that the argument is an `Integer[]` and infers `E = Integer` (type inference). You can write it explicitly too: `GenericPrint.<Double>printArray(doubleArray);`, but this is rarely needed.
2. **The array is `Integer[]`, not `int[]`**: E can only stand for a reference type (rule on p.8). So you use the Week 5 wrapper classes: `Integer`, `Double`, `Character`.
3. **`{1, 2, 3}` can go into an `Integer[]`**: this is autoboxing; each `int` is automatically wrapped into an `Integer`.
4. **A new type needs no new method**: the example also adds a `String[]`, and the same method prints it directly:

```text
Array stringArray contains:
Java Generics 
```

## 6. Two benefits of generics (p.7)

### Benefit 1: One declaration stands for a whole family

- One **generic method** is equivalent to a whole family of related methods (for example, the single method on p.5 replaces the three on p.3).
- One **generic class** is equivalent to a whole family of related types (for example, Week 8's `ArrayList<String>` and `ArrayList<Integer>` both come from the same `ArrayList<E>` class; from p.14 the slides cover writing your own generic class).
- The slide's example: write one generic sorting method, and you can use it to sort `Integer[]`, `Double[]` and `String[]`.

### Benefit 2: Compile-time type safety

A wrong type is caught **at compile time**, instead of crashing only when the program runs. It is clearest when compared with the version that "doesn't use generics and uses `Object` instead":

Without generics: the parameter and return value are both `Object`, and you have to cast yourself when taking the value out.

```java
public static Object first(Object[] a) { return a[0]; }

String[] words = {"a", "b"};
Integer n = (Integer) first(words);   // compiles, fails only at run time
```

```text
Exception in thread "main" java.lang.ClassCastException: class java.lang.String cannot be cast to class java.lang.Integer ...
	at NoGeneric.main(NoGeneric.java:5)
```

With generics:

```java
public static <E> E first(E[] a) { return a[0]; }

String[] words = {"a", "b"};
Integer n = first(words);             // fails at compile time
```

```text
error: incompatible types: inference variable E has incompatible bounds
        Integer n = first(words);
                         ^
    upper bounds: Integer,Object
    lower bounds: String
```

| | Using `Object` | Using generics |
|---|---|---|
| Do you need to cast when taking the value out? | Yes, `(Integer)` | No, the compiler knows what E is |
| When is a wrong type discovered? | **At run time**: `ClassCastException` (runtime error) | **At compile time**: error (compile-time error) |

> Link back to Week 9: a runtime error only shows up when the program reaches that line; a compile-time error is blocked while you are writing the code. Generics turn some runtime errors into compile-time errors ahead of time.

## 7. Generic method rules and terms (p.8)

### Terminology

| Term | Meaning | In `public static <E> void printArray(E[] a)` |
|---|---|---|
| **Type parameter section** | The block wrapped in angle brackets `< >`, written before the return type | `<E>` |
| **Type parameter** (also called **formal type parameter** or **type variable**) | A name that stands for "some type" | `E` |
| **Actual type argument** | The type actually substituted in at the call | `Integer`, `Double`, `Character` |

Analogy with ordinary parameters:

| | Ordinary parameter | Type parameter |
|---|---|---|
| Declaration | `x` in `(int x)` | `E` in `<E>` |
| Substituted at the call | A value, e.g. `5` | A type, e.g. `Integer` |
| Names | formal parameter / actual argument | formal type parameter / actual type argument |

### Rule 1: You can have multiple type parameters, separated by commas

```java
public static <K, V> void printPair(K key, V value) { ... }
```

### Rule 2: Type parameters can be used in three places

The return type, parameter types and local variable types. The `first` method in [`GenericPrint.java`](../src/GenericPrint.java) uses all three:

```java
public static <E> E first(E[] array) {   // E as the return type and as the parameter type
    E result = array[0];                 // E as a local variable type
    return result;
}

Integer x = first(intArray);   // no cast needed
System.out.println(x + 1);     // 2
```

### Rule 3: Only reference types, not primitives

None of the 8 primitives such as `int`, `double` and `char` are allowed; use wrapper classes.

Passing an `int[]` to `E[]`:

```text
error: method printArray in class Prim cannot be applied to given types;
        printArray(ints);
        ^
  required: E[]
  found:    int[]
  reason: inference variable E has incompatible bounds
    equality constraints: int
    upper bounds: Object
```

We already saw the same rule with `ArrayList` in Week 8:

```text
error: unexpected type
        ArrayList<int> list = new ArrayList<>();
                  ^
  required: reference
  found:    int
```

> Why only reference types? Erasure on p.9 explains it: after compilation E is replaced by `Object`, and an `Object` can only point to an object; it cannot hold a primitive.

### Rule 4: The names used in the method must match the ones declared in `< >`

Declaring `<E>` but writing `T[]` in the parameter:

```java
public static <E> void printArray(T[] a) { }
```

```text
error: cannot find symbol
    public static <E> void printArray(T[] a) { }
                                      ^
  symbol:   class T
  location: class Name
```

The compiler treats `T` as a class named T and looks for it, but cannot find it.

### Extra: naming conventions

Type parameters are conventionally **a single uppercase letter**, to avoid confusion with real class names:

| Letter | Common use |
|---|---|
| `E` | Element (elements of a collection or array); the slide on p.5 uses this one |
| `T` | Type (a general type); the slides use this from p.11 on |
| `K`, `V` | Key, Value (e.g. `HashMap<K, V>`) |
| `N` | Number |

## 8. Slide syntax corrections

- **p.5 is missing `static`**: the slide's `printArray` has no `static`, but the `main` on p.6 is `static`, so calling it directly is a compile error (from Week 5: a static method cannot directly call an instance method):

  ```text
  error: non-static method <E>printArray(E[]) cannot be referenced from a static context
          printArray(intArray);
          ^
  ```

  To run the two pages together, write it as `public static <E> void printArray(E[] inputArray)`.

- **`String args[]` on p.6**: this is a C-style array declaration. Java allows it, and it is exactly the same as `String[] args`. The usual convention is `String[] args`.

## 9. Common mistakes

| Wrong code | Result | Correct code |
|---|---|---|
| `public static void <E> printArray(...)` | Compile error: `<E>` is in the wrong position | Put `<E>` **before** the return type: `public static <E> void` |
| `int[] a = {1,2}; printArray(a);` | Compile error: E cannot be `int` | `Integer[] a = {1,2};` |
| Declaring `<E>` but using `T` in the body | `cannot find symbol: class T` | Keep the names consistent |
| Calling a non-static generic method directly from `main` | `cannot be referenced from a static context` | Add `static`, or create an object with `new` first and call it on that |
| Thinking generics let `printArray` accept `int[]` | It doesn't; primitive arrays still need overloads | Use wrapper arrays |

## 10. Checklist

- [ ] Same logic that differs only in type → consider a generic method instead of writing several overloads
- [ ] `<E>`, written before the return type, is the "declaration"; the later `E`s are "uses"
- [ ] You usually don't write the type when calling; the compiler infers it from the arguments
- [ ] Type parameters can only be reference types; for numbers use wrappers such as `Integer` and `Double`
- [ ] E can be used as the return type, a parameter type, and a local variable type
- [ ] Can explain the difference between type parameter / type variable / actual type argument
- [ ] Can name the two benefits of generics: one declaration stands for a family of methods or types; type errors are caught at compile time

## 11. Class discussion: C compilation, binding, the compiler and the JVM

While covering generics, the professor also talked about how C is compiled, and about the difference between static binding and late binding. Both topics make the same point: **which things are decided at compile time and which are only decided at run time**. The design of generics is divided along exactly this line.

> The "why this was mentioned" explanations below are my own, put together from the slides (Generics p.9–13, Lecture 7 slides p.35–50); the professor's actual wording and examples go in [Section 12](#12-class-notes-to-be-added).

### 1. Why C compilation came up

It is used as a contrast with **erasure** on p.9.

| | C | Java |
|---|---|---|
| Compilation result | Compiled directly into machine code for a specific CPU/OS | Compiled into bytecode, which the JVM runs |
| Can you overload? | **No**. Function names cannot repeat, so you can only write `printIntArray`, `printDoubleArray`, ... | Yes (p.3) |
| Writing a "general-purpose" function | Use `void*` (a generic pointer with no type checking), or use a macro to copy the code once for each type | Use generics |

So the problem on p.2–3 is even worse in C: C doesn't even have overloading.

Java's approach (p.9): the compiler first uses `<E>` to **check types**, then **erases `E` and replaces it with `Object`**. The bytecode contains only one `printArray(Object[])`; it does not generate a separate copy for Integer, Double and so on. From this we can see:

- **Type safety only works at compile time**: at run time the JVM no longer knows what E is.
- **E cannot be `int`**: after erasure it becomes `Object`, and an `Object` can only point to an object; it cannot hold a primitive ([Section 7, Rule 3](#rule-3-only-reference-types-not-primitives)).

> Note: C++ templates work the opposite way from Java: they generate a separate copy of the code for each type.

### 2. Static binding and late binding

Binding means: when the program says `obj.doIt()`, **deciding which method body actually runs**. The difference is when that decision is made.

| | Static binding (early binding) | Late binding (dynamic binding) |
|---|---|---|
| When it is decided | Compile time | Run time |
| Who decides | The compiler, looking at the **declared type** and the argument types | The JVM, looking at the **actual type of the object** |
| Typical cases | **Overloading**; `static`, `private`, `final` methods | **Overriding** |
| Slides | Generics p.3, p.13 (generic methods can also be overloaded; the compiler picks the most specific one) | Lecture 7 slides p.35, p.40 |

How this relates to generics:

1. **The overloads on p.3 are static binding**: `printArray(ints)` calling the `int[]` version is chosen by the compiler at compile time.
2. **A generic method prints the right result thanks to late binding**: after erasure, `element` inside `printArray` is just an `Object` as far as the compiler can see.

   ```java
   System.out.printf("%s ", element);   // internally calls element.toString()
   ```

   The compiler only knows "call `Object`'s `toString()`". When the program actually runs, the JVM looks at the object's actual type and runs the overridden `toString()` of `Integer`, `Character` or `String`, so it prints `1 2 3` and `H E L L O` instead of memory addresses.
3. `compareTo` on p.10–11 works the same way: `T extends Comparable<T>` lets the compiler be sure that T always has `compareTo()`; at run time, late binding then calls the version belonging to `Integer` or `String`.

> Exam reminder: Lecture 7 slides p.50 classify both overloading and overriding as polymorphism. The former is often called compile-time polymorphism, and the latter run-time polymorphism.

### 3. The order of the compiler and the JVM

The compiler runs first, then the JVM; they are two separate steps.

```text
Hello.java ──javac──► Hello.class ──java──► program output
source code  compiler    bytecode    JVM (run time)
```

| Step | File / command | Who does it | What happens | Binding | Errors caught at this point |
|---|---|---|---|---|---|
| ① Source code | `Hello.java` | You | Write classes, methods, `<E>`, and calls such as `obj.doIt()` | **Not decided yet**: you have only written the call; which method body runs is not decided yet | Not checked yet (Eclipse's red underlines actually come from it running the compiler in the background, which belongs to ②) |
| ② Compiler | `javac Hello.java` | The compiler | Checks syntax and types; for generics it first checks `<E>`, then **erases** it to `Object`; produces the `.class` file | **Static binding is completed here**: which overload is chosen, and `static` / `private` / `final` methods, are decided by the compiler from the declared types | **Compile-time errors**: syntax errors, type mismatches, generic type errors, `int` used as E |
| ③ Bytecode | `Hello.class` | (the output) | Platform-independent bytecode that runs on any platform with a JVM; E has already become `Object` | The results of static binding are **already fixed** in the bytecode; an overriding call is only recorded as "call `Object.toString()`" and is **left for the JVM to decide** | None (this is just a file). If you edit the `.java` file but don't run `javac` again, the old `.class` is still what runs (the John example from Week 5) |
| ④ Run time | `java Hello` | The JVM | Loads the `.class` file and turns the bytecode into machine code to run it | **Late binding happens here**: overriding runs according to the object's actual type, e.g. `element.toString()` runs the version belonging to `Integer` or `Character` | **Runtime errors**: `ArithmeticException`, `ClassCastException`, array index out of bounds |

The difference from C: compiling C produces machine code directly in a single step, and you must recompile for every platform; Java's `.class` file runs on any platform that has a JVM (write once, run anywhere).

**In one sentence:** ② the compiler handles checking, erasure and static binding; ④ the JVM handles execution and late binding; ③ the bytecode is the handoff file between the two.

### Checklist for this section

- [ ] Can explain the difference between what C and Java compile into (machine code vs bytecode)
- [ ] Can explain at which step erasure happens, and why E cannot be a primitive
- [ ] Overloading → static binding (compile time); overriding → late binding (run time)
- [ ] Can explain that a generic method can print different types because late binding calls each type's own `toString()`
- [ ] Can walk through source code → compiler → bytecode → JVM in order, and which kind of error each step catches

## 12. Class notes (to be added)

> Extra examples, Q&A and whiteboard content from the professor's lectures go here:

-

---

Next time, continue from **p.9 Erasure**, followed by p.10–13 `Comparable` and bounded type parameters (`<T extends Comparable<T>>`), p.14–17 generic classes (`Stack<E>`), and p.18–20 wildcards.
