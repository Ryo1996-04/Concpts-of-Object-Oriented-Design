# Classes and Objects: pp. 10–28, p. 39, and Integrated Class Notes

**English edition** · [繁體中文](classes-and-objects.zh-TW.md)

Source material: `5_Classes_And_Objects.pdf`, pages 10–28 and page 39. For the instance/static topics on pp. 29–35, see the separate comparison note.

Reading order: pp. 10–18 cover object and reference basics; pp. 19–28 cover constructors and `this`. The Rectangle and Person supplementary examples from class are also kept in this note.

If a code block contains only statements, put it inside a method (for example `main`) to run it. Classes with the same name on different pages are independent examples; do not paste them all into the same file. Content marked "Supplement / Correction" clarifies simplified wording in the slides or outdated APIs.

These notes keep Java technical terms in English. The Chinese word 「引用」 used in earlier explanations (it literally means "citation" or "quote") is the same thing as a **reference**.

Core idea: **a class defines a type, `new` creates an object, and a reference variable holds a reference that points to the object.**

## Class Notes Integration — Class, Object, Instance, and Variables

This integrates the supplementary notes from 2026-10-06. It corresponds to class / object / reference on pp. 10–12 and instance fields / static fields on pp. 29–31. The Student and Car classes below are independent examples; do not merge the same-named Student definitions into one Java file.

### 1. Remember the core concepts first

- **Class**: the blueprint that defines an object's structure and behavior.
- **Object / Instance**: an actual object created from a class.
- **Instance variable**: data that belongs to an individual object; each object has its own copy.
- **Static variable / Class variable**: data that belongs to the class and is shared by the objects of that class.
- **Reference variable**: a variable that holds a reference to an object and is used to access it; it can also be `null`.

> **A class is the blueprint → an instance is the thing built from the blueprint → instance variables are each thing's own data.**

### 2. An Instance Is Not the Same as an Instance Variable

**An instance is the object itself; an instance variable is a variable that belongs to that object.**

Take the `Student` class as an example:

```java
class Student {
    String name; // instance variable
    int age;     // instance variable
}
```

Create two objects (the following statements can go inside the `main` method):

```java
Student s1 = new Student();
Student s2 = new Student();

s1.name = "Claire";
s1.age = 25;

s2.name = "John";
s2.age = 30;
```

There are two different `Student` instances here, and each object has its own `name` and `age`.

| Item | Meaning |
|---|---|
| `Student` | The class; defines the structure of a student object |
| The object created each time `new Student()` runs | An instance of `Student` |
| `s1`, `s2` | Reference variables that hold object references |
| `name`, `age` | Instance variables that each `Student` object has its own copy of |
| `s1.name`, `s2.name` | Access the `name` of each of the two objects |

> **Precise wording: `s1` and `s2` are reference variables; the objects they point to are the instances.**

### 3. Using a Car Example to Understand That Each Object Has Its Own Data

```java
class Car {
    String color; // instance variable
    int speed;    // instance variable
}
```

Create and set up the objects (inside a method):

```java
Car car1 = new Car();
Car car2 = new Car();

car1.color = "red";
car1.speed = 60;

car2.color = "blue";
car2.speed = 80;
```

Conceptual diagram:

```text
Car class (blueprint)
   │
   ├── creates object A ← car1 holds its reference
   │     ├── color = "red"
   │     └── speed = 60
   │
   └── creates object B ← car2 holds its reference
         ├── color = "blue"
         └── speed = 80
```

Changing `car1.speed` does not change `car2.speed`, because the two reference variables here point to different objects, and each object has its own instance variables.

**An instance variable is called an "instance" variable precisely because every instance has its own copy.**

### 4. Instance Variable vs. Static / Class Variable

```java
class Student {
    String name;             // instance variable: each student's own name
    static int studentCount; // class variable: counting data shared by the class
}
```

Usage example (inside a method):

```java
Student s1 = new Student();
Student s2 = new Student();

s1.name = "Claire";
s2.name = "John";

Student.studentCount = 2;

System.out.println(s1.name);              // Claire
System.out.println(s2.name);              // John
System.out.println(Student.studentCount); // 2
```

- `name` has no `static`: each `Student` object has its own name.
- `studentCount` has `static`: it belongs to the `Student` class, and accessing it as `Student.studentCount` makes that clear.
- `studentCount` **does not count objects automatically**; in this example it is set to `2` by hand. To count automatically, you must write the update logic yourself.

### 5. Terminology Comparison Table

| Name | Chinese term | What it is | Does each object have its own copy? |
|---|---|---|---|
| Class | 類別 | The blueprint for creating objects | Not applicable |
| Object / Instance | 物件／實例 | An actual object created from a class | Not applicable |
| Instance variable | 實例變數 | Data owned by an individual object | Yes |
| Static / Class variable | 靜態變數／類別變數 | Shared data that belongs to the class | No |
| Reference variable | 參考變數 | A variable that holds an object reference or `null` | Depends on where it is declared; this name describes the value it holds |

### 6. Breaking Down One Line of Java Code

```java
Student s1 = new Student();
```

| Code fragment | Meaning |
|---|---|
| `Student` | The class type that `s1` is declared with |
| `s1` | A reference variable |
| `new Student()` | Creates a new `Student` object and produces a reference to it |
| `=` | Assigns the object reference on the right to `s1` |

Next:

```java
s1.name = "Claire";
```

This means: use `s1` to find the object it points to, then set that object's instance variable `name` to `"Claire"`.

### 7. Is There a Difference Between an Object and an Instance?

At the beginner stage of Java, **object and instance can usually be treated as two ways of saying the same thing**.

- **Object**: emphasizes "this is an object."
- **Instance**: emphasizes "this object is an instance of some class."

For example, `new Student()` creates an object, which is also an instance of `Student`.

### 8. Key Points to Remember

> **Class = blueprint**  
> **Object / Instance = an object built from the blueprint**  
> **Instance variable = each object's own data**  
> **Static / Class variable = data shared by the class**  
> **Reference variable = a variable used to point to an object**

When you see `Student s1 = new Student();`, remember to distinguish:

```text
s1                        → reference variable
new Student()             → creates the object and produces its reference
the object s1 points to   → an instance of Student
that object's name        → instance variable
```

Supplement: reference type and instance/static/local are different classification dimensions. For example, `String name` can be both an instance field and a reference variable; `int age` is an instance field, but it holds a primitive value. For a detailed comparison, see [Instance vs. Static](instance-vs-static.en.md).

## p.10 — Data Abstraction

Defining a `Circle` class creates your own data type for describing a circle's data and functionality. For example, a circle can have a `radius` field, and it can also have a method that calculates its area.

```java
Circle aCircle;
Circle bCircle;
```

These two lines only declare two reference variables; no Circle objects have been created yet. Think of it as preparing two slots that can record "where the circle is."

Data abstraction lets us use circles through the `Circle` type and the operations it provides, while the concrete data representation and implementation details stay inside the class.

## p.11 — Objects

An object is an **instance** of a class. `Circle` is like a blueprint, and every circle built from it is an object.

- **State**: an object's field values at a given moment, for example `radius` is `5`.
- **Methods**: the operations an object provides, for example calculating the area or changing the radius.
- **Identity**: every object has its own identity; even if two objects have the same state, they can still be different objects.

A Java program can make objects interact through method calls.

### Supplement: Message Passing ("pass the message," as the instructor said)

The "pass the message" the instructor mentioned corresponds most directly to p.11 within pp. 10–15: objects interact through defined methods. This concept is called **message passing**; here, you can understand it as asking an object to perform an operation through a **method call**.

Suppose `Circle` defines a `getArea()` instance method, and `aCircle` points to a valid Circle object:

```java
aCircle.getArea();
```

This is like sending the "calculate your area" message to that object.

| Part | What it means in this example |
|---|---|
| **Receiver** | The object that `aCircle` points to |
| **Method** | `getArea()`, the operation being requested |
| **Arguments** | None in this example; when needed, data can be passed inside the parentheses |

For example, if `Circle` provides a `setRadius(double radius)` method:

```java
aCircle.setRadius(5.0);
```

This time the receiver is still the object that `aCircle` points to, the method is `setRadius`, and the argument is `5.0`; it means asking that object to set its radius to `5.0`. The `radius` in the method declaration is called a **parameter**.

Message passing here means objects interacting through method calls; it does not necessarily involve network messages or asynchronous delivery.

The slides treat a name as the way to identify an object, which is a simplification. More precisely, a variable has a name and an object has an identity; the same object can be pointed to by multiple reference variables (see p.15).

## p.12 — The Two Steps of Creating an Object

```java
Circle c1;          // 1. Declare a reference variable
c1 = new Circle();  // 2. Create an object and assign its reference to c1
```

You can also combine them into one line:

```java
Circle c1 = new Circle();
```

| Code | Meaning |
|---|---|
| `Circle` | The variable's type |
| `c1` | The reference variable's name |
| `new` | The operator that creates a new object |
| `Circle()` | Calls the no-parameter constructor here to initialize the new object |
| `=` | Assigns the reference on the right to the variable on the left |

A **constructor** runs when an object is created and is used to initialize the object. A constructor has the same name as the class and has no return type — not even `void`.

`new` is an operator, not a method. The expression `new Circle()` creates an object and produces a reference to that object.

### Supplement: Default Constructor vs. No-Argument Constructor

The rule the instructor emphasized can be written as:

> If you don't declare any constructors, Java provides a default constructor.
> If you declare a constructor, Java no longer provides the default constructor automatically.

For an ordinary Java class, **the Java compiler automatically provides a default constructor only when no constructor is declared at all**. As soon as you declare any constructor yourself, the compiler no longer adds one automatically.

**Case 1: no constructor is declared at all.**

```java
class Student {
}

// In code that can access Student:
Student s = new Student(); // OK: uses the default constructor provided by the compiler
```

**Case 2: you declare a constructor with a parameter.**

```java
class Student {
    String name;

    Student(String name) {
        this.name = name;
    }
}

// In code that can access Student:
Student s1 = new Student("Alice"); // OK
Student s2 = new Student();        // Compile-time error: there is no no-argument constructor
```

`this.name` is the current object's field, and the `name` on the right is the constructor's parameter.

**Case 3: if you want to support both ways of creating objects, declare two constructors yourself.**

```java
class Student {
    String name;

    Student() {
        this.name = "Unknown";
    }

    Student(String name) {
        this.name = name;
    }
}

// In code that can access Student:
Student s1 = new Student();        // OK: name is "Unknown"
Student s2 = new Student("Alice"); // OK: name is "Alice"
```

Declaring constructors with different parameter lists in the same class is called **constructor overloading**. The three examples above are separate cases; statements that create objects should go somewhere that allows executable statements, such as a method.

| Technical term | Meaning |
|---|---|
| **Default constructor** | Provided automatically by the compiler when a class declares no constructor; it has no parameters |
| **No-argument constructor** | A constructor with no parameters; you can declare one yourself, and the default constructor also belongs to this category |
| **Parameterized constructor** | A constructor that has parameters, for example `Student(String name)` |

**A `Student()` you write yourself should be called a no-argument constructor; "default constructor" refers specifically to the constructor the compiler provides automatically.**

### Class Example: Rectangle, Constructor Overloading, and this

This Rectangle example demonstrates two key points: **constructor overloading**, and using **`this`** to distinguish a field and a parameter that share the same name.

Below is the complete class with the spelling corrected; it can be saved as `Rectangle.java`:

```java
public class Rectangle {
    public float length;
    public float width;

    // Constructor: two parameters
    public Rectangle(float l, float w) {
        length = l;
        width = w;
    }

    // No-argument constructor
    public Rectangle() {
        length = 0.0f;
        width = 0.0f;
    }

    // Constructor: one parameter
    public Rectangle(float width) {
        length = 0.0f;
        this.width = width; // Store the parameter's value in the current object's field
    }
}
```


**Constructor overloading: one class has multiple constructors with different parameter lists.** The compiler chooses the applicable constructor based on the number and types of the arguments.

| Constructor | How to use it | State after initialization |
|---|---|---|
| `Rectangle(float l, float w)` | `new Rectangle(3.0f, 2.0f)` | `length = 3.0f`, `width = 2.0f` |
| `Rectangle()` | `new Rectangle()` | `length = 0.0f`, `width = 0.0f` |
| `Rectangle(float width)` | `new Rectangle(2.0f)` | `length = 0.0f`, `width = 2.0f` |

These object creation expressions can go inside `main` or another method. The `f` after a number marks it as a `float` literal, for example `2.0f`; `2.0` without the `f` is a `double` by default.

**`this` is a reference to the current object.** In the following assignment:

```java
this.width = width;
```

- `this.width`: the current object's field.
- `width` on the right: the parameter this constructor received.
- The whole line means: store the parameter's value in the current object's field.

When a parameter has the same name as a field, the parameter **shadows** the field's name, so you need `this.width` to specify the field explicitly. If you write `width = width;`, it only assigns the parameter's value back to the parameter, and the object's field is not updated.

The original comment 「this 可以呼叫自己」 ("this can call itself") should be changed here to "this refers to the current object." `this.width` is a field access; using `this(...)` inside a constructor is what calls another constructor of the same class, which is called **constructor chaining**.

The `Rectangle()` here is a **no-argument constructor** declared by hand, not the **default constructor** the compiler provides automatically. If you remove it but keep the other constructors, `new Rectangle()` causes a compile-time error.

## p.13 — Null Reference

`null` means the reference does not point to any object.

```java
Circle aCircle = null;
```

At this point there is a reference variable, but this line does not create a Circle object. Accessing an instance field or calling an instance method through a `null` reference causes a `NullPointerException`.

The slides say that when you only declare a reference variable, its initial value is `null`; you need to distinguish the kind of variable:

| Where it is declared | State when not explicitly assigned |
|---|---|
| A reference field in a class | The default value is `null` |
| A local variable inside a method | Has no default value that can be read directly; it must be assigned before use |

```java
class Example {
    Circle fieldCircle; // reference field: defaults to null

    void demo() {
        Circle localCircle; // local variable: not yet initialized
        // System.out.println(localCircle); // compile-time error
        localCircle = new Circle();
        System.out.println(localCircle);    // Assigned, so it can be used
    }
}
```

## p.14 — Creating Objects with new

```java
aCircle = new Circle();
bCircle = new Circle();
```

Every time `new Circle()` runs, it creates a new object, so there are two different objects here:

```text
aCircle ──→ Circle object A
bCircle ──→ Circle object B
```

Even if A and B have the same `radius`, they still have different identities.

Connecting this to allocation, the heap, and the stack:

- **Allocation**: reserving memory space for data.
- **Heap**: in Java's conceptual model, the space for objects and arrays is allocated here.
- **Stack**: each method call has a corresponding stack frame that contains that call's local variables and other data.

If `aCircle` and `bCircle` are local variables inside a method, you can picture it like this:

```text
Stack                         Heap
aCircle ────────────────────→ Circle object A
bCircle ────────────────────→ Circle object B
```

This is a conceptual model for learning; the JVM implementation may apply optimizations. Also, a reference can be stored in an object's field, so not all reference variables live on the stack.

## p.15 — Reference Assignment

Continuing from the previous page:

```java
bCircle = aCircle;
```

This line copies the reference held by `aCircle` into `bCircle`. It does not call `new`, and it does not copy the Circle object.

```text
Before assignment:
aCircle ──→ Circle object A
bCircle ──→ Circle object B

After assignment:
aCircle ──→ Circle object A ←── bCircle
            Circle object B (loses the reference it used to get from bCircle)
```

Now two reference variables point to the same object; this situation is called **aliasing**.

Suppose `Circle` has an accessible `radius` field:

```java
aCircle.radius = 10;
System.out.println(bCircle.radius); // 10
System.out.println(aCircle == bCircle); // true
```

Both variables point to A, so if you modify A's state through one reference, you will see the change through the other as well. Using `==` on reference types compares whether they point to the same object (or whether both are `null`).

If B can no longer be reached through any valid reference path, it becomes eligible for garbage collection. The **Garbage Collector (GC)** does not guarantee that it will reclaim it immediately.

If you later reassign only one of the variables:

```java
bCircle = new Circle();
```

`bCircle` points to a new object C, while `aCircle` still points to A. Reassigning a reference variable and modifying an object's state are different operations.

## p.16 — Accessing Class Members: Dot Operator and Field Access

The slides use the name **dot separator**; in class it is also often called the **dot operator**. Both refer to the period `.`. In these examples, it is used to access members, including fields and methods, through an object reference.

```java
Circle aCircle = new Circle();
aCircle.x = 2.0;
aCircle.y = 2.0;
aCircle.r = 1.0;
```

- `aCircle`: a reference variable that points to a Circle object.
- `x`, `y`: fields that represent the coordinates of the center.
- `r`: the field that represents the radius.
- `aCircle.r`: a **field access**; accesses that object's `r` field.
- `aCircle.r = 1.0;`: an **assignment to a field**; stores `1.0` in that field and changes the object's state.

The syntax on the slide can be summarized more precisely as:

```java
reference.fieldName
reference.methodName(arguments)
```

These accesses must follow the member's access control; for example, other classes cannot freely access a `private` field directly. This example assumes that Circle's members are accessible to the caller.

## p.17 — Accessing Class Members: Method Invocation and Message Passing

```java
Circle aCircle = new Circle();
double area;
aCircle.r = 1.0;
area = aCircle.area();
```

**Method invocation** is the same as a **method call**: calling a method. The last line first calls the object's `area()` method, then assigns the return value to the local variable `area`.

| Part | Role in `area = aCircle.area();` |
|---|---|
| Receiver | The Circle object that `aCircle` points to |
| Method | `area()`, which calculates the area |
| Arguments | None; `()` is an empty argument list |
| Return value | The area the method calculates |
| Assignment target | The local variable `area` on the left |

The `area` on the left is a variable name, and the `area()` on the right is a method invocation; they play different roles.

This page labels the method call directly as "sent 'message' to aCircle," so **p.17 is the direct code example of message passing**; p.11 introduces the concept of objects interacting through methods first. Message passing here means asking the receiver to perform an operation; it does not necessarily involve network or asynchronous messages.

## p.18 — Using Circle Class: Putting the Steps Together

The slide's main flow is kept below, with the line breaks rearranged into readable Java code. This program needs the course material's Circle class, which has accessible `x`, `y`, `r` fields and `area()` and `circumference()` methods.

```java
class MyMain {
    public static void main(String[] args) {
        Circle aCircle;           // 1. Declare a reference variable
        aCircle = new Circle();   // 2. Create an object, store the reference

        aCircle.x = 10;           // 3. Field assignment: set the center and radius
        aCircle.y = 20;
        aCircle.r = 5;

        double area = aCircle.area();              // 4. Method invocation
        double circumf = aCircle.circumference();  //    Store the return values

        System.out.println("Radius=" + aCircle.r + " Area=" + area);
        System.out.println("Radius=" + aCircle.r
                + " Circumference=" + circumf);
    }
}
```

The order of execution is: **declare a reference variable → create an object → set fields → call methods → print the results**.

`aCircle.x`, `aCircle.y`, and `aCircle.r` are all field accesses; adding `= value` makes them assignments. Only `aCircle.area()` and `aCircle.circumference()` are method invocations.

The output shown on the slide is:

```text
Radius=5.0 Area=78.5
Radius=5.0 Circumference =31.400000000000002
```

These values correspond to calculations that approximate π as `3.14`: the area is `3.14 × 5 × 5`, and the circumference is `2 × 3.14 × 5`. The trailing `000000000000002` is a precision effect of floating-point representation; the actual output depends on how the Circle methods are implemented.

### Lecture Terminology Supplement: Invocation and Dot Operator

The "invocation" heard earlier corresponds to **method invocation**; "datodator," based on its pronunciation and the content of these pages, is most likely **dot operator**. Slide p.16 says **dot separator**. This is a judgment based on the course material and the pronunciation, not a word-for-word confirmation of what the instructor said.

| Code | Technical term | Meaning |
|---|---|---|
| `aCircle.r` | Field access | Accesses the radius data |
| `aCircle.r = 5;` | Assignment to a field | Sets the radius to `5` |
| `aCircle.area()` | Method invocation / method call | Calls the method that calculates the area |
| The `.` in the code above | Dot operator / dot separator | Used to access a member |

**In these examples, a field name has no `()` after it; a method invocation has `()`. Both field access and method invocation use `.`.**

## p.19 — Constructing Objects

```java
Person person = new Person();
```

Read it starting from the right: `new Person()` creates a Person instance, runs the matching constructor, and produces a reference; `=` stores the reference in the variable named `person`. The `Person` on the left is the variable's declared type.

A class defines a new reference type, so besides primitive types such as `int`, you can also declare variables of type `Person` or `Circle`. `Person person;` only declares a variable; the actual object creation happens in `new Person()`.

Supplement: this example uses the same type for the variable and the object; later, when you learn inheritance, you can also store a reference to a subclass object in a compatible superclass or interface variable.

## p.20 — Person Example: Fields and Getters / Setters

The course material's Person fields are `name` and `age`; the earlier class example used `fName`, but the concept is the same.

```java
class Person {
    String name;
    int age;

    String getName() { return name; }
    void setName(String n) { name = n; }
    int getAge() { return age; }
    void setAge(int a) { age = a; }
}
```

| Declaration | Return type | What it does |
|---|---|---|
| `getName()` | `String` | Reads the name |
| `setName(String n)` | `void` | Receives parameter `n` and updates the name |
| `getAge()` | `int` | Reads the age |
| `setAge(int a)` | `void` | Receives parameter `a` and updates the age |

`void` means no value is returned; it does not mean the method has no effect. A setter changes the object's state. This page does not declare a constructor, so it uses the default constructor provided by the compiler.

Supplement: members without an access modifier are package-private, not `public`. The course material declares the fields directly to keep the example simple; if you want to control access through methods, you can make the fields `private`, which relates to **encapsulation**. Just adding a getter/setter while the fields are still public does not prevent direct access to the fields.

## p.21 — Constructing Person Objects: Create First, Then Set

```java
Person p1 = new Person(); // At this point name = null, age = 0
p1.setName("John");       // name becomes "John"
p1.setAge(22);            // age becomes 22
```

These three lines consist of one object creation and two method invocations. If you forget to call one of the setters, the corresponding field keeps its original value.

The slide then asks: can the object have the name Smith and the age 32 right when it is created? Yes — with the **parameterized constructor** coming next, the constructor receives the initialization data.

## p.22 — Constructors: How to Recognize Them

A constructor initializes a new object's state. Its name must match the class name exactly, and it has no return type.

```java
Person() { }       // Constructor
void Person() { }  // Ordinary method: same name, but it has the return type void
```

The above is a syntax comparison snippet; the two mean different things. The course material calls a constructor a special method; more precisely, it is a special constructor declaration, not an ordinary instance method that you can call again with `p.Person()`.

The common way to create an object is `new Person(...)`. A constructor can also do constructor chaining through `this(...)` or `super(...)`; therefore, not every constructor invocation is written directly with `new`.

## p.23 — Person Constructor: How Arguments Get into Fields

```java
class Person {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

```java
Person p2 = new Person("Smith", 32);
```

At run time, the argument `"Smith"` maps to the parameter `name`, and the argument `32` maps to the parameter `age`; the assignments then store these two values in the new object's fields. After the constructor finishes, the object that `p2` points to has these initial values.

This version only declares a two-parameter constructor, so `new Person()` causes a compile-time error; you need to add a no-argument constructor yourself to support it.

## p.24 — Default Constructor: When It Is Provided Automatically

The compiler provides a default constructor only when an ordinary class **declares no constructor at all**. Once you declare any constructor yourself, it is no longer added automatically.

| Constructors in the class | `new Person()` | `new Person("Smith", 32)` |
|---|---|---|
| None declared at all | Allowed; uses the default constructor | Not allowed |
| Only `Person(String, int)` | Not allowed | Allowed |
| Both `Person()` and `Person(String, int)` | Allowed | Allowed |

Supplement / Correction: default initialization of fields is part of object creation, not a feature unique to the default constructor. Even if you write a constructor yourself, the fields first get their default values and are then updated by the initializers and the constructor code.

| Field type | Default value |
|---|---|
| `byte`, `short`, `int`, `long` | The number `0` |
| `float`, `double` | `0.0f`, `0.0d` |
| `boolean` | `false` |
| `char` | `'\u0000'`, not the character `'0'` |
| Reference types | `null` |

The default constructor also involves a superclass constructor invocation, so it does not mean there is no initialization behavior at all; if the superclass has no accessible no-argument constructor, the implicit call may fail to compile. Local variables do not follow the field default value rules in the table above.

## p.25 — Multiple Constructors: Constructor Overloading

```java
public class Circle {
    public double x, y, r;

    public Circle() {
        x = 0.0;
        y = 0.0;
        r = 0.0;
    }

    public Circle(double cx, double cy, double cr) {
        x = cx;
        y = cy;
        r = cr;
    }
}
```

`new Circle()` creates an object centered at `(0, 0)` with radius `0`; `new Circle(2, 3, 5)` creates an object centered at `(2, 3)` with radius `5`. The integer arguments in the latter can undergo a widening conversion to `double`.

Overloads are distinguished by the parameter types, their number, and their order. Changing only the parameter name does not work: for example, `Circle(double radius)` and `Circle(double size)` have the same parameter type list and cannot be two overloads.

## p.26 — Using the this Reference: The Current Receiver

Inside an instance method or constructor, `this` points to the object currently being worked on. When no local variable or parameter with the same name shadows it, `age` can usually be treated as an implicit `this.age`.

```java
void setAge(int age) {
    this.age = age;
}
```

The parameter `age` **shadows** the field with the same name; the left side uses `this.age` to specify the field, and the `age` on the right refers to the parameter. If you write `age = age;`, it is just a self-assignment on the parameter, and the field is not updated.

A static method has no implicit receiver, so it cannot use `this` directly.

## p.27 — this Keyword: Each Object Is Set Up Separately

```java
Person p1 = new Person("Alice", 20);
Person p2 = new Person("Bob", 25);
```

When the first constructor runs, `this` points to Alice's new object; when the second runs, `this` points to Bob's new object. The two `this.age = age` executions update the instance fields of different objects.

`this` does not permanently point to a variable named `p1`; even if the same object is pointed to by multiple references, `this` inside an instance method is still the receiver object of that particular call.

## p.28 — Cascading Constructors: Constructor Chaining

```java
class Person {
    String name;
    int age;

    Person() {
        this("", 0); // Call another constructor of the same class
    }

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

When `new Person()` runs: it first enters the no-argument constructor, which delegates through `this("", 0)` to the two-parameter constructor, then returns when that finishes. **This process creates only one object**; calling two constructors does not create two objects.

The purpose is to centralize initialization logic and avoid repeating the same assignments in multiple constructors. Here `""` is the empty String, which is different from `null`.

| Syntax | Purpose |
|---|---|
| `this.age` | Accesses the current object's field |
| `this.getAge()` | Calls the current object's instance method |
| `this("", 0)` | Calls another constructor of the same class |

This course example puts `this(...)` at the beginning of the constructor, which is easy to understand and works across Java versions; constructors must not form direct or indirect recursive invocations.

## p.39 — Garbage Collection and System.gc()

### The Difference Between Garbage Collection and the Garbage Collector

**Garbage collection (GC)** is the process of automatically reclaiming the memory used by objects that are no longer reachable; the **garbage collector** is the mechanism in the JVM that does this work. This lets heap memory be used again by other objects.

The key criterion is **reachability**. If an object can no longer be reached through any valid reference path the program still holds, it may become eligible for garbage collection. You cannot judge this only by whether some variable was set to null, nor only by whether some other object points to it.

### Slide p.39: What Happens After a Reference Assignment?

Suppose Circle has a usable no-argument constructor:

```java
Circle aCircle = new Circle(); // Create object P
Circle bCircle = new Circle(); // Create object Q
bCircle = aCircle;             // Copy the reference; both point to P
```

```text
Before assignment:
aCircle ----> P
bCircle ----> Q

After assignment:
aCircle ----> P <---- bCircle
              Q (if there is no other reachable path, it becomes a candidate for garbage collection)
```

`bCircle = aCircle` changes the reference that bCircle holds. It does not copy P, and it does not delete Q immediately; Q becomes eligible for collection only when no other reachable references to it remain.

This also connects to aliasing on p.15. When two variables point to the same object, setting one of them to null still lets the other keep using that object:

```java
aCircle = null;
// bCircle still holds the reference to P; P can still be used through it later.
```

### System.gc(): Asking the JVM to Consider Collecting

The correct capitalization is **`System.gc()`** — the S in System is uppercase:

```java
System.gc();
```

| Part | Meaning |
|---|---|
| `System` | A class in java.lang |
| `.` | Dot operator / dot separator |
| `gc()` | A static method invocation with no arguments |
| Return type | void; there is no return value you can use to confirm that collection finished |

`System.gc()` suggests that the JVM expend effort to reclaim unused objects; **it is not a command that forces immediate collection**. It does not guarantee which objects are reclaimed, how much memory is freed, or when it finishes. Even after the method returns, you cannot conclude that a particular object has been reclaimed. [Oracle System.gc() API](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/System.html#gc())

Even if a program never calls `System.gc()`, the JVM manages garbage collection as needed; ordinary programs usually do not need to request it manually. This method cannot specify "collect only Q," and it does not make an object that is still needed lose its references automatically.

### Runnable Example: Observing References, Not Claiming Collection Is Done

Save as `GcExample.java`:

```java
public class GcExample {
    static class Circle {
        double r;
    }

    public static void main(String[] args) {
        Circle aCircle = new Circle(); // P
        Circle bCircle = new Circle(); // Q

        aCircle.r = 5;
        bCircle = aCircle; // Q loses the reference this program held
        System.out.println(aCircle == bCircle); // true

        aCircle = null;
        System.gc(); // Makes a GC request; not a notification that collection is complete
        System.out.println(bCircle.r); // 5.0: P can still be used through bCircle

        bCircle = null; // The program no longer holds a reference to P
        System.gc();
        System.out.println("GC requested; completion is not guaranteed.");
    }
}
```

Output:

```text
true
5.0
GC requested; completion is not guaranteed.
```

This output verifies the behavior of reference assignment and field access; **it cannot be used to prove that P or Q has been collected**. The precise run-time object lifetime may also be affected by JVM optimizations; this diagram is meant for understanding source-level references.

### Relationship to a Static Counter, and Common Misconceptions

- `Person.counter++` is counting logic you write yourself. When GC reclaims a Person object, it does not call `counter--` automatically, so the counter represents the cumulative number created, not the number still alive.
- `p = null` is a reference assignment, not a memory deallocation command.
- Even if two objects reference each other, if the whole group has no externally reachable path, they can still be collected by GC; GC does not simply count the number of references.
- GC manages memory; it does not guarantee timely closing of external resources such as files and network connections. Those resources should be closed explicitly, for example with try-with-resources.

## Class Supplement — Person: Default Values, Getters / Setters, and Constructor

Save and run each of the two versions below as its own `Person.java`; you cannot put two `public class Person` declarations in the same file. The HTML whitespace encoding from the original post has been restored, and the formatting markup in `System.***out***.println` has been cleaned up into the correct Java syntax `System.out.println`.

### Version 1: Original Program and Output

```java
public class Person {
    public String fName; // [1] Reference field: default value is null
    public int age;      // [2] int field: default value is 0

    // [3] No constructor is declared, so the compiler provides a default constructor.

    public String getFName() {
        return this.fName; // Getter: just returns the current field value
    }

    public void setFName(String fName) {
        this.fName = fName; // Setter: field = parameter
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static void main(String[] args) {
        Person p1 = new Person(); // [4] Create an object without specifying a name or age

        // [5] Although setters are defined, they are not called here.
        System.out.println("The name of the person is :" + p1.getFName());
        System.out.println("The age of the person is :" + p1.getAge());

        /* Actual output:
        The name of the person is :null
        The age of the person is :0
        */
    }
}
```

**Why `null` and `0`?** `fName` and `age` are instance fields. When an object is created, its fields first get the default value of their type; this program has no field initializers, and no custom constructor or setter calls change them. So the getters return `null` and `0`. In string concatenation, a `null` reference is displayed as the text `null`.

Defining a method does not mean it has been executed. A setter's body runs only when you write a method invocation such as `p1.setFName(...)` or `p1.setAge(...)`. This output is normal behavior, not a compile-time error or an exception.

### Version 2: Adding the Fix Based on the Instructor's "Define a Constructor"

```java
public class Person {
    public String fName;
    public int age;

    // [Fix 1] Declare a parameterized constructor yourself.
    // The constructor has the same name as the class and no return type, not even void.
    public Person(String fName, int age) {
        this.fName = fName; // The left side is the current object's field; the right side is the parameter
        this.age = age;
    }

    // [Fix 2] Explicitly provide a no-argument constructor to keep new Person() working.
    // This is a constructor you write yourself, not the default constructor the compiler provides.
    public Person() {
        this.fName = "Unknown";
        this.age = 18; // 18 is an initial value chosen for this example, not a Java default value
    }

    public String getFName() {
        return this.fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static void main(String[] args) {
        // [Fix 3] The arguments map to the constructor's parameters.
        Person p1 = new Person("Alice", 20);
        System.out.println("The name of the person is :" + p1.getFName());
        System.out.println("The age of the person is :" + p1.getAge());

        // [Comparison] Call the no-argument constructor you declared.
        Person p2 = new Person();
        System.out.println("The name of the person is :" + p2.getFName());
        System.out.println("The age of the person is :" + p2.getAge());

        // [Supplement] After the object is created, you can still modify its state through a setter.
        p1.setAge(21);
        System.out.println("Updated age:" + p1.getAge());

        /* Actual output:
        The name of the person is :Alice
        The age of the person is :20
        The name of the person is :Unknown
        The age of the person is :18
        Updated age:21
        */
    }
}
```

This fixed version shows both kinds of constructors at once, which is **constructor overloading**. If you only need `new Person("Alice", 20)`, you can keep just the parameterized constructor; but then `new Person()` can no longer be used, because the compiler no longer provides a default constructor automatically.

### Change History and Reasons

1. **Observe the original output**: `new Person()` creates an object, and the getters return `null` and `0`.
2. **Confirm the cause**: neither field is given an initial value, and the program does not call the setters, so the default values remain. A getter only reads; it does not generate a name or age automatically.
3. **Understand the direction of the instructor's fix**: "Define a constructor" means declaring a constructor yourself to initialize the fields during object creation.
4. **Add a parameterized constructor**: pass the arguments in with `new Person("Alice", 20)`, then set the object's state through `this.fName = fName` and `this.age = age`.
5. **Add a no-argument constructor for comparison**: if you still want to use `new Person()`, declare it explicitly and choose the initial values yourself. `"Unknown"` and `18` are just example values chosen here.
6. **Keep the getters / setters**: the constructor handles initialization at creation time, the getter reads the current state, and the setter sets or modifies the state when it is called.

| Concept | Role in this example |
|---|---|
| Default field values | The `String` field is `null`, the `int` field is `0` |
| Default constructor | Provided automatically by the compiler when no constructor is declared |
| Parameterized constructor | Receives the name and age when the object is created |
| No-argument constructor | In this fixed version, explicitly sets `"Unknown"` and `18` |
| Getter | Returns the field's current value |
| Setter | Modifies the field's value when called |
| `this` | A reference to the current object, used to specify its fields explicitly |

Fields have default values; local variables inside a method must be assigned before they are read. For example, `Person p1;` only declares a local variable, and you cannot call `p1.getAge()` directly before it has been assigned.

## Key Comparison

| Code | What happens |
|---|---|
| `Circle aCircle;` | Declares a reference variable |
| `aCircle = null;` | Makes the variable point to no object |
| `aCircle = new Circle();` | Creates a new object and stores its reference in the variable |
| `bCircle = aCircle;` | Copies the reference so that both variables point to the same object |
| `aCircle.radius = 10;` | Modifies the state of the object it points to |

**`new Circle()` creates an object; `bCircle = aCircle` copies a reference.**
