# CSYE6200 Notes: ArrayList, Abstract Classes, and Interfaces

**English edition** · [繁體中文](arraylist-abstract-interfaces.zh-TW.md)

> Personal study notes: the lecture content is reorganized in my own words. These notes contain no slide text or images; page numbers are given only for cross-referencing the course handout. All example programs are my own, and every error message and output was produced by actually running `javac` / `java`.

- Course: CSYE6200 (Fall 2026)
- Unit: ArrayList; Abstract classes; Interfaces; Enums (`8_Abstract classes And Interfaces.pdf`; these notes cover p.1–24; p.25–31 on Enum are in the [Week 9 notes](../../week-09/notes/enum.en.md))
- Date: class on 2026-11-03 (missed; self-studied from the handout, written 2026-11-10)
- Runnable example programs: [`../src/`](../src) (ArrayListDemo; Shape, Circle, Rectangle, ShapeTest; Speaker, Politician, Priest, Lecturer, Teacher, Person, Professor, SpeakerTest)

## Contents

**Part 1 — ArrayList**

1. [Why ArrayList? (p.3)](#1-why-arraylist-p3)
2. [Creating an ArrayList (p.4)](#2-creating-an-arraylist-p4)
3. [Common Methods (p.5–6)](#3-common-methods-p56)
4. [Example Trace: ArrayListDemo (p.7)](#4-example-trace-arraylistdemo-p7)
5. [Common ArrayList Pitfalls](#5-common-arraylist-pitfalls)

**Part 2 — Abstract Classes**

6. [What Is an Abstract Class? (p.8)](#6-what-is-an-abstract-class-p8)
7. [Syntax (p.9)](#7-syntax-p9)
8. [The Shape Example (p.10–12)](#8-the-shape-example-p1012)
9. [Four Rules (p.13)](#9-four-rules-p13)

**Part 3 — Interfaces**

10. [What Is an Interface? (p.14–15)](#10-what-is-an-interface-p1415)
11. [Defining and Implementing an Interface (p.16–19)](#11-defining-and-implementing-an-interface-p1619)
12. [An Interface Extending an Interface (p.20)](#12-an-interface-extending-an-interface-p20)
13. [Using extends and implements Together (p.21)](#13-using-extends-and-implements-together-p21)
14. [Interfaces and Software Engineering (p.22)](#14-interfaces-and-software-engineering-p22)
15. [Polymorphism via Interfaces (p.23–24)](#15-polymorphism-via-interfaces-p2324)

**Summary**

16. [Abstract Class vs. Interface](#16-abstract-class-vs-interface)
17. [Supplement: Interfaces in Java 8 and Later](#17-supplement-interfaces-in-java-8-and-later)
18. [Corrections to the Lecture Slides](#18-corrections-to-the-lecture-slides)
19. [Checklist](#19-checklist)

---

# Part 1 — ArrayList

## 1. Why ArrayList? (p.3)

The arrays from Week 3 have one limitation: **their length is fixed**. Once created, an array cannot grow or shrink, so you have to know up front how many elements it will hold.

```java
int[] scores = new int[3];   // can only ever hold 3
```

`ArrayList` is a **dynamic array** that grows automatically as needed:

- It is created with an initial size (capacity).
- When you add more than it can hold, it automatically switches to a larger internal array.
- When elements are removed, the internal array may also shrink.

The handout also mentions that `ArrayList` **extends `AbstractList`** and **implements the `List` interface**. This sentence uses exactly the two topics that come later this week: `AbstractList` is an abstract class (Part 2), and `List` is an interface (Part 3).

## 2. Creating an ArrayList (p.4)

`ArrayList` lives in the `java.util` package, so you have to import it first (packages/imports from Week 5):

```java
import java.util.ArrayList;
```

Three constructors (constructor overloading from Week 4):

| Constructor | What it does |
|---|---|
| `ArrayList()` | Creates an empty list |
| `ArrayList(Collection c)` | Creates a list from the elements of another collection |
| `ArrayList(int capacity)` | Sets the **initial capacity** (how big the internal array starts out); it grows automatically when full |

> **Capacity is not the same as size**: capacity is the size of the internal array, while size is **how many elements are actually stored right now**. The size of `new ArrayList<>(100)` is still 0.

### Supplement: Generics

The handout writes `ArrayList al = new ArrayList();` without specifying an element type (this is called a raw type). It compiles, but you get a warning:

```text
Note: ArrayListDemo.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.
```

The modern way is to specify the element type with `<>`:

```java
ArrayList<String> al = new ArrayList<>();    // can only hold String
```

- Adding the wrong type (for example `al.add(5)`) is a **compile error**, so the mistake is caught early.
- What `get()` returns is already a `String`; no cast is needed.
- `<>` only accepts **class types**, not primitives like `int` or `double`. To store numbers, use the wrapper classes from Week 5: `ArrayList<Integer>`, `ArrayList<Double>`; Java converts automatically when you put values in and take them out (autoboxing/unboxing).

## 3. Common Methods (p.5–6)

| Method | What it does | Notes |
|---|---|---|
| `add(element)` | Adds to the **end** | Returns `true` |
| `add(index, element)` | Inserts at the given position; later elements shift back | index may be `0` to `size()`; out of range throws `IndexOutOfBoundsException` |
| `get(index)` | Gets the element at the given position | index must be `0` to `size() - 1` |
| `set(index, element)` | Replaces the element at the given position | Supplement; not listed in the handout |
| `remove(index)` | Removes the element at the given **position**; later elements shift forward | Returns the removed element |
| `remove(object)` | Removes the first element **equal** to it | See the pitfalls in Section 5 |
| `contains(object)` | Whether the element is present | Compares with `equals()` (Week 3: compare String content with `equals`) |
| `indexOf(object)` | Position of the first occurrence | Returns `-1` if not found |
| `size()` | Current number of elements | |
| `clear()` | Removes everything | |
| `toArray()` | Converts to a regular array | |

### Arrays vs. ArrayList

| | Array (Week 3) | ArrayList |
|---|---|---|
| Length | Fixed | Grows/shrinks automatically |
| How to get the length | `arr.length` (a field, no parentheses) | `al.size()` (a method, with parentheses) |
| Read | `arr[i]` | `al.get(i)` |
| Modify | `arr[i] = x` | `al.set(i, x)` |
| Can it hold primitives? | Yes: `int[]` | No, use a wrapper: `ArrayList<Integer>` |
| Printing the contents | `System.out.println(arr)` prints an address; you need a loop | `System.out.println(al)` prints `[C, A, E]` directly |
| Enhanced for | Yes | Yes |

## 4. Example Trace: ArrayListDemo (p.7)

[`ArrayListDemo.java`](../src/ArrayListDemo.java) is the p.7 example rewritten with generics. The list contents after each step:

| Step | Executed | List contents | size |
|---|---|---|---|
| 1 | `new ArrayList<>()` | `[]` | 0 |
| 2 | `add` C, A, E, B, D, F in order | `[C, A, E, B, D, F]` | 6 |
| 3 | `add(1, "A2")`: insert at index 1, later elements shift back | `[C, A2, A, E, B, D, F]` | 7 |
| 4 | `remove("F")`: remove the element whose **value** is F | `[C, A2, A, E, B, D]` | 6 |
| 5 | `remove(2)`: remove **index 2** (which is `A`) | `[C, A2, E, B, D]` | 5 |

Actual output (the first 5 lines match the handout version):

```text
Initial size of al: 0
Size of al after additions: 7
Contents of al: [C, A2, A, E, B, D, F]
Size of al after deletions: 5
Contents of al: [C, A2, E, B, D]
get(0): C
contains("B"): true
indexOf("Z"): -1
after set(0, "Z"): [Z, A2, E, B, D]
Z A2 E B D 
```

The last 5 lines are my additions: `get`, `contains`, `indexOf`, `set`, and an enhanced for.

## 5. Common ArrayList Pitfalls

**1. `remove(int)` and `remove(Object)` are two different methods (overloading, Week 5)**

With `ArrayList<String>` there is no confusion: `remove(2)` is an index, `remove("F")` is a value. But with `ArrayList<Integer>` you have to be careful:

```java
ArrayList<Integer> nums = new ArrayList<>();
nums.add(10); nums.add(20); nums.add(30);

nums.remove(1);                     // removes index 1 → [10, 30]
nums.remove(Integer.valueOf(10));   // removes value 10 → [30]
```

`1` is an `int`, so Java prefers `remove(int index)`. To remove by "value", you have to pass an `Integer` object.

**2. Index out of range**: `get(size())` throws `IndexOutOfBoundsException`. The last valid index is `size() - 1`, just like `length - 1` for arrays (Week 3).

**3. Calling `remove` while iterating with an enhanced for**: this throws `ConcurrentModificationException`. To delete elements, use a regular for loop going from the back to the front, or use `removeIf`.

---

# Part 2 — Abstract Classes

## 6. What Is an Abstract Class? (p.8)

Start from `final`:

- A `final` class: **cannot be inherited**.
- An abstract class is the exact opposite: **it is only useful when it is inherited**.

An abstract class is a **conceptual class**: it describes what a group of classes have in common, but it is too abstract on its own to create objects directly.

> Example: a "shape" in general has no area formula; only a "circle" or a "rectangle" does. So `Shape` is a good fit for an abstract class, with `Circle` and `Rectangle` inheriting from it.

Key points:

- **Cannot be instantiated**: you cannot `new` an abstract class.
- It provides a **common root** for a group of related classes.

## 7. Syntax (p.9)

```java
abstract class ClassName {
    abstract Type methodName1();      // abstract method: header only, no { }, ends with a semicolon

    Type method2() {                  // regular method: has a body
        // method body
    }
}
```

Rules:

- If a class has **one or more** abstract methods, the class **must** be declared `abstract`.
- Abstract methods are implemented by **subclasses**; a subclass that implements all the abstract methods is called a **concrete class**.
- There are **no abstract constructors**, and **no abstract static methods**.
- Supplement: `abstract` and `final` cannot be used together (one must be inherited, the other forbids inheritance):

```text
error: illegal combination of modifiers: abstract and final
```

## 8. The Shape Example (p.10–12)

Inheritance relationship (p.10):

```text
        Shape (abstract)
        /             \
   Circle          Rectangle
```

### Compared with the Week 6 Shape

In Week 6, `Shape` was a regular class, so `area()` had no choice but to return `0`. But "the area of a shape is 0" doesn't really mean anything, and `new Shape()` was also allowed, giving you a strange object with an area of 0.

After making it abstract ([`Shape.java`](../src/Shape.java)):

```java
public abstract class Shape {
    public abstract double area();          // no body: we don't know how to compute it

    public void describe() {                // regular method: subclasses inherit it directly
        System.out.println(getClass().getSimpleName() + " area = " + area());
    }
}
```

- `area()` has no body, which forces every subclass to write its own.
- An abstract class **can also have regular methods**: `describe()` is written in the parent class and shared by all subclasses. The `area()` it calls will be each subclass's own version (Week 6 overriding).

### Is `new Shape()` allowed? (p.11)

```java
Shape s = new Shape();      // ❌
```

```text
error: Shape is abstract; cannot be instantiated
```

But a **variable declared with type Shape** is perfectly fine, as long as it points to a subclass object:

```java
Shape s = new Circle();     // ✅ a parent-type variable points to a subclass object (Week 6 polymorphism)
```

### Subclasses (p.12)

[`Circle.java`](../src/Circle.java) and [`Rectangle.java`](../src/Rectangle.java) both `extends Shape` and implement `area()`. [`ShapeTest.java`](../src/ShapeTest.java):

```java
Shape[] shapes = { new Circle(), new Circle(2.0), new Rectangle(3.0, 4.0) };
for (Shape s : shapes) {
    s.describe();
}
```

```text
Circle area = 3.1415
Circle area = 12.566
Rectangle area = 12.0
```

(`PI` follows the handout and uses `3.1415`, so the area for radius 2 is `12.566`. Real programs should use `Math.PI`.)

## 9. Four Rules (p.13)

| Situation | Can you `new` it? |
|---|---|
| The class has one or more abstract methods | It is automatically abstract, **no** |
| The class is declared `abstract`, even if it has **no abstract methods at all** | **No** |
| The subclass implements **all** of the parent's abstract methods | **Yes** (concrete class) |
| The subclass does **not** implement all the abstract methods | The subclass is itself abstract, **no** |

The last rule: if a subclass doesn't write `area()` and isn't declared abstract either, you get a compile error:

```text
error: Square is not abstract and does not override abstract method area() in Shape
```

Fix it one of two ways: implement `area()`, or declare `Square` as `abstract` too.

---

# Part 3 — Interfaces

## 10. What Is an Interface? (p.14–15)

An interface is a **conceptual construct** similar to an abstract class, but "purer":

- It can only have **constants** (`final` variables) and **abstract methods** (with no implementation). This is the difference from an abstract class.
- It is used when **many classes need the same set of methods**; each class implements them in its own way.

### Why we need it: informal multiple inheritance

Week 6 explained that Java uses **single inheritance**: a class can only `extends` **one** parent class.

But a class **can implements many interfaces**. An interface only specifies "which methods must exist"; the implementation code is written by the class itself, so there is no "both parents have a method with the same name, which one do I use?" problem. That is why interfaces are considered Java's **informal way of achieving multiple inheritance**.

### Modifiers the compiler adds automatically

| Written in an interface | Actually equivalent to |
|---|---|
| `int MAX_MINUTES = 60;` | `public static final int MAX_MINUTES = 60;` (a constant) |
| `void speak();` | `public abstract void speak();` |

So interface variables **cannot be changed** (`Speaker.MAX_MINUTES = 90;` is a compile error), and methods are **always public**.

## 11. Defining and Implementing an Interface (p.16–19)

### Relationship diagram (p.16)

```text
            <<interface>>
               Speaker
               speak()
          /       |       \
   Politician   Priest   Lecturer
    speak()     speak()   speak()
```

The three classes have **no inheritance relationship** with each other, but they can all "speak".

### Definition (p.17)

[`Speaker.java`](../src/Speaker.java):

```java
public interface Speaker {
    int MAX_MINUTES = 60;    // constant
    void speak();            // header only, no body
}
```

### Implementation (p.18–19)

Use `implements`; you can list several interfaces, separated by commas:

```java
class ClassName implements InterfaceName1, InterfaceName2 { ... }
```

[`Politician.java`](../src/Politician.java):

```java
public class Politician implements Speaker {
    @Override
    public void speak() {
        System.out.println("Talk politics");
    }
}
```

**An implementing method must be declared `public`**. Interface methods are already public, so leaving out `public` would narrow the access level, which is a compile error:

```text
error: speak() in Priest cannot implement speak() in Speaker
  attempting to assign weaker access privileges; was public
```

All three classes on p.19 of the handout write `public void speak()`, which is correct.

## 12. An Interface Extending an Interface (p.20)

An interface can also use `extends` to inherit from another interface; the child interface gets all the members of the parent interface.

[`Teacher.java`](../src/Teacher.java):

```java
public interface Teacher extends Speaker {   // inherits speak()
    void grade(String student);              // adds one more
}
```

A class that implements `Teacher` must write both `speak()` **and** `grade()`.

> Supplement: a class can only extend one class, but an interface **can extend multiple interfaces**: `interface C extends A, B { }`.

## 13. Using extends and implements Together (p.21)

A class can **inherit from one parent class** and at the same time **implement one or more interfaces**. The order is fixed: **`extends` first, then `implements`**.

[`Professor.java`](../src/Professor.java):

```java
public class Professor extends Person implements Teacher {
    public Professor(String name) {
        super(name);                              // Week 6: hand off to the parent constructor
    }

    @Override
    public void speak() {                         // from Speaker (via Teacher)
        System.out.println(name + ": Today we cover interfaces.");
    }

    @Override
    public void grade(String student) {           // from Teacher
        System.out.println(name + " grades " + student);
    }
}
```

`Professor` is a `Person`, a `Teacher`, and a `Speaker` all at once. This is what the handout means by "it looks like multiple inheritance".

## 14. Interfaces and Software Engineering (p.22)

- Like an abstract class, an interface provides a **template for behavior**: it specifies which methods other classes **must** have.
- It separates **design** (which methods must exist) from **implementation** (how the methods are written): the designer defines the interface first, different programmers write their own classes following it, and everyone's method names and parameters end up consistent.
- What gets passed around is the **description of the methods**, not the implementation.
- Java allows only a single parent class; interfaces let different classes "mix and match" multiple abilities.

> You can think of an interface as a **contract**: "as long as you implements me, you are guaranteed to have these methods." Users only need to know the contract, not which class is behind it. This is the same idea as "separating interface from implementation" in Week 5 Encapsulation.

## 15. Polymorphism via Interfaces (p.23–24)

**An interface name can be used as a variable type**:

```java
Speaker current;
```

`current` can point to an object of **any** class that implements `Speaker`. When you call `current.speak()`, which version runs **depends on the object it currently points to**, not on the variable's type.

[`SpeakerTest.java`](../src/SpeakerTest.java):

```java
Speaker guest = new Lecturer();
guest.speak();                  // Lecturer's version
guest = new Politician();
guest.speak();                  // Politician's version

Speaker[] speakers = { new Politician(), new Priest(), new Lecturer(), new Professor("Dr. Lee") };
for (Speaker current : speakers) {
    current.speak();
}
```

Actual output:

```text
Talks Object Oriented Design and Programming!
Talk politics
Talk politics
Preaches
Talks Object Oriented Design and Programming!
Dr. Lee: Today we cover interfaces.
Dr. Lee grades Claire
Dr. Lee is a Speaker: true
Speaker.MAX_MINUTES = 60
```

- The same line `current.speak()` prints four different results over four iterations.
- `Politician`, `Priest`, `Lecturer`, and `Professor` share no common parent class (`Professor`'s parent is `Person`), but they can all go into the same `Speaker[]` because they all implements `Speaker`.
- `p instanceof Speaker` is `true`: `Professor` implements `Speaker` indirectly through `Teacher`.
- A variable of type `Speaker` **can only call methods that `Speaker` has**: `guest.grade(...)` is a compile error; you need a variable of type `Teacher` for that.

---

# Summary

## 16. Abstract Class vs. Interface

| | Abstract class | Interface |
|---|---|---|
| Keyword | `abstract class` | `interface` |
| How subclasses use it | `extends`, **only one** | `implements`, **can be several** |
| Can you `new` it? | No | No |
| Methods | Both abstract methods **and** regular methods | Only abstract methods (for Java 8 and later, see Section 17) |
| Variables | Any kind: instance, static, private… | Constants only (automatically `public static final`) |
| Constructor | **Allowed**; subclasses call it with `super(...)` | **None** |
| Method access level | Any | Automatically `public` |
| Best for | A group of classes that are **fundamentally the same kind** and share some code: `Circle` and `Rectangle` **are a kind of** `Shape` (is-a) | A group of **unrelated** classes that share the same **ability**: a politician, a priest, and a lecturer **can all** speak (can-do) |
| This week's examples | `Shape`, the JDK's `AbstractList` | `Speaker`, `Teacher`, the JDK's `List` |

> Memory aid: **abstract class = a half-finished parent class** (some methods are written, some are left for subclasses); **interface = a contract that is only a list of methods**.

## 17. Supplement: Interfaces in Java 8 and Later

The handout says an interface "can only have constants and abstract methods". That is the rule up to Java 7. Since Java 8, an interface can also have:

- **`default` methods**: they have a body; implementing classes can use them directly or override them.
- **`static` methods**: called using the interface name.
- Since Java 9, `private` methods as well (shared by default methods).

```java
public interface Speaker {
    void speak();

    default void greet() {               // Java 8+
        System.out.println("Hello!");
        speak();
    }
}
```

For the exam, follow the handout: interface = constants + abstract methods.

## 18. Corrections to the Lecture Slides

| Page | Handout | Correct |
|---|---|---|
| Throughout | Quotes in the code are curly quotes `“ ”` | Pasting them as-is causes compile errors; use straight quotes `" "` |
| p.5–7 | Method parameters are written as `Object`, and the example uses `ArrayList al = new ArrayList();` (raw type) | Compiles but gives an unchecked warning; today we use `ArrayList<String>` (Section 2) |
| p.6 | `toArray()` throws `NullPointerException` "if the specified array is null" | That sentence is about the other version, `toArray(T[] a)`; the no-argument `toArray()` does not do this |
| p.7 | `al.add(1, "A2"); S` | There is an extra `S` at the end of the line; it is a typo |
| p.12 | `public Circle extends Shape {`, `public Rectangle extends Shape {` | Missing `class`; it should be `public class Circle extends Shape {`; copying it as-is gives the error `class, interface, annotation type, enum, record, method or field expected` |
| p.15 | Interface methods "can only be abstract classes" | Should be **abstract methods** |
| p.17 | The method declaration "has only a method body" | It is the exact opposite: it has only a **header** (signature) and **no** body |
| p.24 | `new lecturer()`, `new politician()` | Class name capitalization must match: `new Lecturer()`, `new Politician()` |

## 19. Checklist

**ArrayList**

- [ ] `import java.util.ArrayList;`
- [ ] Use `ArrayList<Type>`; store numbers with `Integer`, `Double`
- [ ] Use `size()` for the count, not `length`
- [ ] For `get(i)` / `set(i, x)`, i is between `0` and `size() - 1`
- [ ] To remove a value from an `ArrayList<Integer>`, write `remove(Integer.valueOf(x))`

**Abstract class**

- [ ] Has an abstract method → the class needs `abstract`
- [ ] An abstract method has no `{ }` and ends with a semicolon
- [ ] You cannot `new` an abstract class, but you can use it as a variable type: `Shape s = new Circle();`
- [ ] A subclass must implement all abstract methods, otherwise it must be declared abstract itself

**Interface**

- [ ] `implements` can list several, separated by commas; `extends` goes before `implements`
- [ ] Implementing methods must be `public`
- [ ] Interface variables are constants and cannot be changed
- [ ] A variable of an interface type can only call methods that are in the interface
