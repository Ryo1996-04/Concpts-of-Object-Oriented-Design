# CSYE6200 Notes: Inheritance, super, protected, and Overriding

**English edition** · [繁體中文](inheritance-and-super.zh-TW.md)

> Personal study notes: the lecture content is reorganized in my own words. These notes contain no original slide text or images; the page numbers are only for cross-referencing the course handout, and all example programs are my own.

- Course: CSYE6200 (Fall 2026)
- Unit: Inheritance and Polymorphism (p.3–28: Inheritance and Overriding)
- Date: 2026-10-20 (the lecture reached p.28)
- Runnable example programs: [`../src/`](../src) (Address, Person, Student, ClassA, ShadowingDemo, Shape, Circle, Rectangle, ShapeTest)

## Contents

1. [What Is Inheritance? (p.3, p.6)](#1-what-is-inheritance-p3-p6)
2. [is-a vs. has-a (p.3–5)](#2-is-a-vs-has-a-p35)
3. [Supplement: UML Class Diagram](#3-supplement-uml-class-diagram)
4. [extends Syntax and the Direction of Inheritance (p.7–8)](#4-extends-syntax-and-the-direction-of-inheritance-p78)
5. [Class Example: Address / Person / Student](#5-class-example-address--person--student)
6. [Review: the this Keyword](#6-review-the-this-keyword)
7. [Access Modifiers and protected (p.9–14)](#7-access-modifiers-and-protected-p914)
8. [super(...): Calling the Parent Class Constructor (p.15–18)](#8-super-calling-the-parent-class-constructor-p1518)
9. [super.member: Accessing Parent Class Members (p.19–20)](#9-supermember-accessing-parent-class-members-p1920)
10. [Single Inheritance (p.21)](#10-single-inheritance-p21)
11. [Overriding (p.22–24)](#11-overriding-p2224)
12. [Overloading vs. Overriding (p.25)](#12-overloading-vs-overriding-p25)
13. [Class Hierarchy (p.26–27)](#13-class-hierarchy-p2627)
14. [Constructor Call Order (p.28)](#14-constructor-call-order-p28)
15. [One Public Class per File, and Where to Put main](#15-one-public-class-per-file-and-where-to-put-main)
16. [Common Mistakes](#16-common-mistakes)
17. [Checklist](#17-checklist)

---

## 1. What Is Inheritance? (p.3, p.6)

**Inheritance**: deriving a new class from an existing class.

| Term | Meaning |
|---|---|
| parent class / **superclass** | The original class being inherited from |
| child class / **subclass** | The new class derived from it |

Why use inheritance:

- **Reuse**: the subclass directly uses fields and methods the parent class has already written and tested, without rewriting them
- **Enhancement / Adaptation**: the subclass can **add** its own fields and methods, and can also **modify** inherited methods
- Avoid **duplicate code**: when several classes share a lot of the same data and behavior, move the common part into a parent class

A subclass is a "more specific" version of its parent class. For example, a Student is a more specific kind of Person.

---

## 2. is-a vs. has-a (p.3–5)

There are two common relationships between classes, and you need to tell them apart:

| Relationship | How to say it | Example | How to write it in Java |
|---|---|---|---|
| **is-a** (Generalization / Specialization) | A is a kind of B | A Circle is a kind of Shape | `class Circle extends Shape` |
| **has-a** (Whole-part, called **aggregation**) | A has a B | An Employee has a birthday Date | Make B a field of A |

**Inheritance is only for is-a relationships.**

### Inheritance tree (is-a)

The higher you go, the more general; the lower you go, the more specific:

```
                Shape
              /       \
         Shape2D      Shape3D
        /   |   \      /    \
 Triangle Rectangle Circle  Cube  Sphere
```

A Circle is a kind of Shape2D, and a Shape2D is a kind of Shape, so a Circle is also a kind of Shape.

> Java class names cannot start with a digit, so you have to write `Shape2D`, not `2D_Shape`.

### A quick test

Read the two classes as an English sentence:

- "A **is a** B" makes sense → use inheritance
- "A **has a** B" makes sense → make B a field of A

Examples: "Student is a Person" ✅ inheritance; "Person is an Address" ❌; "Person has an Address" ✅ make it a field.

---

## 3. Supplement: UML Class Diagram

UML (**Unified Modeling Language**) was mentioned verbally in class. It is a standard set of symbols for drawing software designs, not a programming language. The most commonly used diagram is the **Class Diagram**.

### A class is drawn as three compartments

```
┌──────────────────────┐
│        Person        │  ← class name
├──────────────────────┤
│ # fName : String     │  ← fields (attributes)
│ # age : int          │
│ # address : Address  │
├──────────────────────┤
│ + getAge() : int     │  ← methods
│ + setAge(int) : void │
└──────────────────────┘
```

### Access modifier symbols

| Symbol | Meaning |
|---|---|
| `+` | public |
| `-` | private |
| `#` | protected |
| `~` | default (package) |

### How relationships are drawn

| Relationship | Notation |
|---|---|
| Inheritance (is-a) | Solid line + **hollow triangle**, with the arrow pointing from the subclass **to the parent class** |
| Aggregation (has-a) | Solid line + **hollow diamond**, with the diamond at the "whole" end |
| Composition (a stronger has-a) | Solid line + **filled diamond** |

---

## 4. extends Syntax and the Direction of Inheritance (p.7–8)

```java
class SubclassName extends SuperclassName {
    // Only write what the subclass adds or changes
}
```

Two key points:

1. **You do not need to create a parent class object before creating a subclass object.** Just write `new Circle()`; there is no need to `new Shape()` first.
2. **Inheritance is one-way.** A subclass can see what is in its parent class, but the parent class **cannot see** the fields and methods the subclass adds. For example, Person cannot use `major`, which only Student has.

---

## 5. Class Example: Address / Person / Student

The design from the classroom whiteboard:

```
  Address  ◇────────  Person  ◁────────  Student
         (has-a)                (is-a)
```

| Class | Fields | Relationship |
|---|---|---|
| Address | streetNo, streetName, zipCode | Owned by Person (has-a) |
| Person | fName, age, address | Parent class |
| Student | **id, major** | `Student extends Person` |

### The problem without inheritance

If Student does not inherit from Person, it has to write the three fields fName, age, and address again, plus their get/set methods, all duplicating Person.

### After rewriting with inheritance

Student only needs to write `id` and `major`, which only students have; everything else is inherited from Person.

The three files are placed in **the same package**:

```
src/
 ├─ Address.java
 ├─ Person.java
 └─ Student.java
```

#### Address.java

```java
public class Address {
    private String streetNo;
    private String streetName;
    private int zipCode;

    public Address(String streetNo, String streetName, int zipCode) {
        this.streetNo = streetNo;
        this.streetName = streetName;
        this.zipCode = zipCode;
    }

    public String getStreetNo() { return this.streetNo; }
    public void setStreetNo(String streetNo) { this.streetNo = streetNo; }

    public String getStreetName() { return this.streetName; }
    public void setStreetName(String streetName) { this.streetName = streetName; }

    public int getZipCode() { return this.zipCode; }
    public void setZipCode(int zipCode) { this.zipCode = zipCode; }
}
```

> When zipCode is an `int`, leading zeros are lost (`02115` becomes `2115`). In practice, ZIP codes are usually stored as a `String`.

#### Person.java

```java
public class Person {
    protected String fName;
    protected int age;
    protected Address address;          // has-a: a Person has an Address
    private static int counter = 0;     // Counts how many Person objects have been created in total

    public Person(String fName, int age, Address address) {
        this.fName = fName;
        this.age = age;
        this.address = address;
        counter++;
    }

    public String getFName() { return this.fName; }
    public void setFName(String fName) { this.fName = fName; }

    public int getAge() { return this.age; }
    public void setAge(int age) { this.age = age; }

    public Address getAddress() { return this.address; }
    public void setAddress(Address address) { this.address = address; }

    public static int getCounter() { return counter; }
}
```

#### Student.java

```java
public class Student extends Person {       // is-a: a Student is a kind of Person
    protected int id;                       // Only the fields that students have
    protected String major;

    public Student(String fName, int age, Address address, int id, String major) {
        super(fName, age, address);         // Hand the shared part to Person's constructor
        this.id = id;
        this.major = major;
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }

    public String getMajor() { return this.major; }
    public void setMajor(String major) { this.major = major; }

    public static void main(String[] args) {
        Address addr = new Address("123", "Main St", 12345);
        Student s = new Student("Amy", 20, addr, 1001, "Software Engineering");

        System.out.println("Name: " + s.getFName());                      // Inherited from Person
        System.out.println("Age: " + s.getAge());                         // Inherited from Person
        System.out.println("Street: " + s.getAddress().getStreetName());  // method chaining
        System.out.println("ID: " + s.getId());                           // Student's own
        System.out.println("Major: " + s.getMajor());                     // Student's own
        System.out.println("Person count: " + Person.getCounter());
    }
}
```

#### Output

```
Name: Amy
Age: 20
Street: Main St
ID: 1001
Major: Software Engineering
Person count: 1
```

- Student does not define `getFName()`, but `s.getFName()` works because it is inherited.
- `Person count` is 1: when a Student is created, Person's constructor also runs (see Section 14), so every Student is also counted as a Person.
- `s.getAddress().getStreetName()` is **method chaining**: first get the Address object, then call a method on it.

#### Before vs. after

| | Without inheritance | `extends Person` |
|---|---|---|
| Student's fields | 5 | 2 (id, major) |
| Student's get/set pairs | 5 | 2 |
| When a shared field needs to change | Change two classes | Change only Person |

> In class, the first version made all fields `public` so everyone could focus on understanding inheritance; they were changed to `protected` later. See Section 7 for why.

---

## 6. Review: the this Keyword

**`this` refers to the object currently being operated on.**

```java
public void setId(int id) {
    this.id = id;    // this.id on the left is the object's field; id on the right is the parameter
}
```

**Shadowing trap**: when a parameter has the same name as a field, `id` inside the method refers to the parameter and hides the field with the same name.

```java
public void setId(int id) {
    id = id;         // ❌ Assigns the parameter to itself; the field is not changed, and there is no error
}
```

**To know what `this` is, look before the dot:** when `a.setId(1)` runs, `this` is `a`; when `b.setId(2)` runs, `this` is `b`.

---

## 7. Access Modifiers and protected (p.9–14)

### The conflict between encapsulation and inheritance

- **Encapsulation unit**: fields should be `private`, and outside code may only access them through get/set.
- **Inheritance**: subclasses often need to use the parent class's fields.
- **Conflict**: a subclass's code **cannot directly access** the parent class's `private` members.

```java
public class Person {
    private String fName;
}

public class Student extends Person {
    void test() {
        this.fName = "Amy";   // ❌ Compile error: The field Person.fName is not visible
    }
}
```

A Student object **still contains** the fName data (Person's constructor sets it); Student's code just cannot see it, so it cannot read or write it directly.

Making it `public` does let the subclass access it, but then **any class can change it directly**, which breaks encapsulation. For example, outside code could write `s.age = -5;` directly and skip the check in `setAge()` entirely.

### protected: the compromise

`protected` lets **subclasses access the member directly** while blocking most unrelated outside classes. It gives better encapsulation than public without getting in the way of inheritance.

### The four access levels

| Modifier | Same class | Same package | Subclass in a different package | Everywhere else |
|---|---|---|---|---|
| `private` | ✅ | ❌ | ❌ | ❌ |
| (none, default) | ✅ | ✅ | ❌ | ❌ |
| `protected` | ✅ | ✅ | ✅ | ❌ |
| `public` | ✅ | ✅ | ✅ | ✅ |

> ⚠️ Besides being open to subclasses, Java's `protected` is **also accessible to every class in the same package**. So when all three classes are in the same package, protected behaves almost the same as default; the difference only shows up when the subclass is in a different package.

### Comparing the three approaches

| Parent class field | Encapsulation | Can the subclass use `this.field` directly? | How does the subclass set the parent's field? |
|---|---|---|---|
| `public` | ❌ Worst | ✅ | Write it directly |
| `protected` | ⚠️ Compromise | ✅ | Write it directly, or use `super(...)` |
| `private` | ✅ Best | ❌ | Only through `super(...)` or the inherited getters/setters |

So encapsulation and inheritance are **not mutually exclusive**: parent class fields can stay `private` and still be inherited; the subclass just handles them indirectly through `super(...)` and getters/setters. `protected` is a compromise that lets subclasses access them directly.

---

## 8. super(...): Calling the Parent Class Constructor (p.15–18)

**Constructors are not inherited**, even if they are public. Student does not automatically get the `Person(String, int, Address)` constructor.

But a subclass object contains the data of "the parent class part," and that part should be initialized by the parent class's constructor. So:

> **The subclass constructor is responsible for calling the parent class constructor**, by writing `super(...)` on the first line.

```java
public Student(String fName, int age, Address address, int id, String major) {
    super(fName, age, address);    // Initialize the Person part
    this.id = id;                  // Initialize Student's own part
    this.major = major;
}
```

### Rules

1. `super(...)` can only be written inside a constructor, and it **must be the first line**.
2. If you do not write it, Java **automatically inserts `super();`** (a call to the parent class's no-argument constructor).
3. If the parent class **has no no-argument constructor**, the subclass **must** write `super(...)` itself, or there is a compile error. Eclipse's error message is: `Implicit super constructor Person() is undefined`.
4. The number, types, and order of the arguments must match one of the parent class's constructors.
5. `this(...)` (calling another constructor of the same class) and `super(...)` must both be on the first line, so a single constructor can only use one of them.

---

## 9. super.member: Accessing Parent Class Members (p.19–20)

The syntax is `super.member`, where the member can be a field or a method.

### For a shadowed field

When a subclass declares a field with the same name as one in the parent class, the subclass's field **shadows** the parent's field. You then need `super.` to get the parent's copy:

```java
class Base {
    int x = 1;
}

class Derived extends Base {
    int x = 2;                          // Shadows Base's x

    void show() {
        System.out.println(this.x);     // 2: Derived's x
        System.out.println(super.x);    // 1: Base's x
    }
}
```

> In practice, you should **avoid** declaring a subclass field with the same name as a parent class field; it is easy to get confused. This example is only for understanding shadowing.

### For an overridden method

`super.method()` calls the parent class's version of a method. It is very common when overriding (see Section 11).

### this vs. super

| | `this` | `super` |
|---|---|---|
| Refers to | The current object | The part inherited from the parent class |
| `xxx(...)` | Calls another constructor of the same class | Calls the parent class's constructor |
| `xxx.field` | Its own field | The parent class's field |
| `xxx.method()` | Its own method | The parent class's version of the method |

You cannot write `super.super.xxx`; you can only go up one level.

---

## 10. Single Inheritance (p.21)

- A Java class **can have only one direct parent class**: only one class can follow `extends`.
- Some languages allow **multiple inheritance** (inheriting from several parent classes at once), but if two parent classes have a field or method with the same name, the subclass does not know which one to use, and this conflict has to be resolved separately.
- Java chose not to support multiple inheritance of classes. When you need a similar effect, use an **interface** instead (covered in a later unit).

```java
class Circle extends Shape { }              // ✅ One parent class
class Circle extends Shape, Drawable { }    // ❌ Compile error
```

"Only one direct parent class" does not mean only one level. Circle extending Shape2D, and Shape2D in turn extending Shape, is fine (see Section 13).

---

## 11. Overriding (p.22–24)

### Definition

**Overriding**: a subclass writes a method with **exactly the same signature** as a parent class method, replacing the parent's version with its own.

- **Signature** = method name + the number, types, and order of the parameters
- The signature must be the same, but **the method body can be completely different**
- What gets overridden is a **method**. Fields (attributes) cannot be overridden; declaring a field with the same name in a subclass only causes shadowing (Section 9) and should be avoided

### Class example: Shape / Circle / Rectangle

Circle and Rectangle both inherit from Shape. Shape has an `area()` method, and each subclass overrides it with its own formula.

```
              ┌──────────────┐
              │    Shape     │
              ├──────────────┤
              │ + area()     │
              └──────────────┘
                 ▲        ▲
                 │        │
   ┌─────────────┴─┐  ┌───┴──────────────┐
   │    Circle     │  │    Rectangle     │
   ├───────────────┤  ├──────────────────┤
   │ - radius      │  │ - width, height  │
   ├───────────────┤  ├──────────────────┤
   │ + area()      │  │ + area()         │  ← area() listed again in the subclass means override
   └───────────────┘  └──────────────────┘
```

```java
public class Shape {
    public double area() {
        return 0;     // A general "shape" has no fixed area formula, so return 0 for now
    }
}

public class Circle extends Shape {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double area() {                    // Same name, parameters, and return type as Shape's area()
        return Math.PI * radius * radius;     // Body replaced with the circle formula
    }
}

public class Rectangle extends Shape {
    private double width;
    private double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;                // Body replaced with the rectangle formula
    }
}
```

### The object decides which version runs

```java
Shape s1 = new Circle(2);
Shape s2 = new Rectangle(3, 4);
Shape s3 = new Shape();

System.out.println(s1.area());   // 12.566370614359172: Circle's area()
System.out.println(s2.area());   // 12.0: Rectangle's area()
System.out.println(s3.area());   // 0.0: Shape's own area()
```

All three variables have type `Shape`, but at run time Java looks at **which class the object actually is** and then decides which version of `area()` to call.

The benefit: when handling a bunch of different shapes, you do not have to check "is it a circle or a rectangle?" one by one. Just call `area()` on each object, and each uses its own formula. This is the foundation of the next topic, **Polymorphism**.

### Rules for overriding

| Rule | Explanation |
|---|---|
| Same name | Both are called `area` |
| Exactly the same parameters | Same number, types, and order; different parameters make it an overload, not an override |
| Same return type | Both are `double` |
| Access cannot be more restrictive | If the parent's is `public`, the subclass cannot change it to `protected` or `private` |
| Only inherited methods can be overridden | A subclass cannot see `private` methods, so they cannot be overridden; `static` and `final` methods cannot be either |

### Add `@Override`

`@Override` is not required, but it is recommended every time. It tells the compiler "I intend to override a parent class method":

```java
@Override
public double aera() { ... }   // ❌ Compile error: the parent class has no aera(), so the typo is caught immediately
```

Without `@Override`, the misspelled `aera()` is treated as a new method, and there is no error. Then calling `area()` still runs the parent class's version, and this kind of bug is hard to find.

### Use `super.method()` to call the parent's original version

After overriding, the subclass can still call the parent class's version with `super.method()`. A common pattern is "add your own handling first, then hand off to the parent class":

```java
public class Student extends Person {
    // ... (constructor etc. omitted)

    @Override
    public void setAge(int age) {
        if (age <= 0) {
            System.out.println("Invalid age");
            return;                 // Do not set it if invalid
        }
        super.setAge(age);          // Only hand off to Person's original setAge if valid
    }
}
```

> When writing this kind of check, remember to `return` (or use `else`) in the invalid case. Otherwise, after printing the warning, the program keeps going and stores the invalid value anyway.

---

## 12. Overloading vs. Overriding (p.25)

The two terms look alike, but they are completely different things:

| | Overloading | Overriding |
|---|---|---|
| Where | Within **the same class** | Between a parent class and a subclass |
| Method name | Same | Same |
| Parameters | **Different** | **Exactly the same** |
| What decides which one is called | The arguments passed in | The object's actual type |
| Purpose | The same operation accepting different data | The same operation done differently by objects of different types |

```java
public class Circle extends Shape {
    public Circle() { this(1); }                          // overload: in the same class,
    public Circle(double radius) { this.radius = radius; } // two constructors with different parameters

    @Override
    public double area() { ... }                          // override: replaces Shape's area()
}
```

---

## 13. Class Hierarchy (p.26–27)

- A subclass can in turn be the parent of another class, forming a multi-level **class hierarchy**:

```
                Shape
              /       \
         Shape2D      Shape3D
        /   |   \      /    \
   Circle Rectangle Triangle Cube Sphere
```

- Subclasses of the same parent class are called **siblings** (for example, Circle and Rectangle). They have **no** inheritance relationship with each other: Circle cannot use Rectangle's `width`.
- Inheritance passes all the way down: a subclass has the members of **all its ancestors**. Circle has what Shape2D and Shape both have.
- **Put shared features at the highest level where they reasonably belong.** For example, every shape has an area, so `area()` goes in Shape; only flat shapes have a perimeter, so `perimeter()` fits in Shape2D.
- No single inheritance structure fits every situation; design it according to what the program needs.

---

## 14. Constructor Call Order (p.28)

Also called **constructor chaining** (in class it was called a cascade).

### Example

```java
// File name: ClassA.java
public class ClassA {
    public ClassA() {
        System.out.println("A constructor");
    }

    public static void main(String[] args) {
        ClassC c = new ClassC();
    }
}

class ClassB extends ClassA {
    public ClassB() {
        // super() is not written, so Java inserts it automatically
        System.out.println("B constructor");
    }
}

class ClassC extends ClassB {
    public ClassC() {
        System.out.println("C constructor");
    }
}
```

Output:

```
A constructor
B constructor
C constructor
```

### The flow (what the whiteboard diagram in class means)

```
Inheritance       Calls (going up)           Finishes running (going down)
ClassA           A()  ←─┐                    ① body of A()
  ▲                     │ super()
ClassB           B()  ←─┤                    ② body of B()
  ▲                     │ super()
ClassC           C()  ──┘  ← new ClassC()     ③ body of C()
```

- **Calls go from bottom to top**: C() → B() → A() → Object()
- **Execution finishes from top to bottom**: Object() finishes first, then A(), B(), and finally C()
- Only one ClassC is created with new, but all three constructors run, because a C object contains the B and A parts, which must be initialized starting from the top.
- The first line of each constructor calls the level above (either a `super(...)` you wrote, or the `super()` Java inserts automatically).
- The top of this chain is always **`Object`**. Every class in Java ultimately inherits from `Object`, whose constructor does nothing.

---

## 15. One Public Class per File, and Where to Put main

### Rules

- A `.java` file can have **at most one public class**, and its name **must exactly match the file name** (including upper and lower case).
- Other classes in the same file cannot be `public`; you can only write `class ClassB ...` (default access, visible anywhere in the same package).
- The error message when you break this rule: `The public type Xxx must be defined in its own file`.
- After compiling, each class still produces its own `.class` file (ClassA.class, ClassB.class, ClassC.class).

### Where to put main

As in the Section 14 example, if the file name is `ClassA.java`, put `main` inside `public class ClassA`. When you click **Run As → Java Application** in Eclipse, this is the main that runs.

- Putting main in ClassA **does not mean a ClassA object is created**. `main` is `static`, so it belongs to the class itself and runs without any object.
- main can create (new) any class in the same package, for example `new ClassC()`.
- If you write `ClassC c = ...` but never use `c` afterward, Eclipse shows a yellow warning light bulb; it does not affect running the program.

### In real projects

Giving each public class its own file (Address.java, Person.java, Student.java) is the most common practice. Putting several classes in one file is usually only for small demos like the one in Section 14.

---

## 16. Common Mistakes

| Wrong code | Problem | Correct code |
|---|---|---|
| The parameter is written as `Address adress`, and the body says `this.address = address;` | The parameter name is misspelled, so `address` on the right becomes the field itself; the field is assigned to itself and is always `null`, **and there is no error** | Make the parameter name match the field: `Address address` |
| The setter only says `this.fName;` | Not a complete statement (not a statement); compile error | `this.fName = fName;` |
| `id = id;` | The parameter shadows the field, so the field is not changed | `this.id = id;` |
| The subclass declares again a field the parent class already has | Two fields with the same name exist and shadow each other | The subclass only writes the fields it adds |
| `super(...)` is not on the first line | Compile error | Move it to the first line of the constructor |
| The parent class only has constructors with parameters, and the subclass does not write `super(...)` | `Implicit super constructor ... is undefined` | Write `super(...)` explicitly |
| Two `public class`es are written in the same file | `must be defined in its own file` | Split them into two files, or remove `public` from one of them |
| The parent class field is `private`, and the subclass writes `this.field = ...` | `The field ... is not visible` | Use `super(...)` or a setter instead, or change the field to `protected` |
| Trying to override but misspelling the method name (`aera()`), without `@Override` | It becomes a new method; calling `area()` still runs the parent's version, and there is no error | Add `@Override` to every override |
| Trying to override but changing the parameters (`area(int x)`) | It becomes an overload, and the parent's `area()` is not replaced | The parameters must be exactly the same as the parent's |
| Changing `public` to `protected` or `private` when overriding | Compile error: access cannot be more restrictive than in the parent class | Keep the same or wider access |

---

## 17. Checklist

- [ ] Each public class has the same name as its file
- [ ] Related classes are in the same package
- [ ] The subclass says `extends ParentClass`
- [ ] The subclass does not redeclare fields and get/set methods the parent class already has
- [ ] The first line of the subclass constructor is `super(...)`, with arguments matching a parent class constructor
- [ ] Every setter is `this.field = parameter;`
- [ ] Field access: used only by the class itself → `private`; subclasses need direct access → `protected`; avoid `public` as much as possible
- [ ] Methods the subclass overrides have the same name, parameters, and return type as the parent's, and have `@Override`
- [ ] After running, confirm: when a subclass object is created, the parent class constructor also runs
- [ ] After running, confirm: when a parent-type variable points to a subclass object, the subclass's overridden version is called
