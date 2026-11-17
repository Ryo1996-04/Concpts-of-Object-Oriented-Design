# CSYE6200 Notes: Enum Types

**English edition** · [繁體中文](enum.zh-TW.md)

> Personal study notes: the lecture content is reorganized in my own words. These notes contain no slide text or images; page numbers are given only for cross-referencing the course handout. All example programs are my own.

- Course: CSYE6200 (Fall 2026)
- Unit: ArrayList; Abstract classes; Interfaces; Enums (`8_Abstract classes And Interfaces.pdf`; these notes cover Enum on p.25–31)
- Date: 2026-11-10 (this week starts from Enum; p.1–24 of the same handout were last week's content, see the [Week 8 notes](../../week-08/notes/arraylist-abstract-interfaces.en.md))
- Runnable example programs: [`../src/`](../src) (Day, TrafficLight)

## Contents

1. [Class Opener: Traffic Lights](#1-class-opener-traffic-lights)
2. [When to Use an Enum (p.25)](#2-when-to-use-an-enum-p25)
3. [Declaration Syntax (p.26–27)](#3-declaration-syntax-p2627)
4. [An Enum Is Actually a Class (p.28, p.30)](#4-an-enum-is-actually-a-class-p28-p30)
5. [Class Example: Move and the Coordinate System (extending p.25–27)](#5-class-example-move-and-the-coordinate-system-extending-p2527)
6. [Iterating Over All Constants with values() (p.29)](#6-iterating-over-all-constants-with-values-p29)
7. [Enums with switch (p.31)](#7-enums-with-switch-p31)
8. [Supplement: Other Built-in Methods](#8-supplement-other-built-in-methods)
9. [Supplement: Complete Traffic Light Example](#9-supplement-complete-traffic-light-example)
10. [Corrections to the Lecture Slides](#10-corrections-to-the-lecture-slides)
11. [Checklist](#11-checklist)

---

## 1. Class Opener: Traffic Lights

The instructor used a traffic light as the example: a traffic light has several states where each one "can only be one choice from a fixed set of options".

| Property | Possible values | Corresponding enum |
|---|---|---|
| On/off | ON, OFF | `Power` |
| Color | RED, YELLOW, GREEN | `LightColor` |
| Direction | e.g. NORTH_SOUTH, EAST_WEST | `Direction` |

Key point: these values are **all known when you write the program**; no new colors appear at run time. You could represent them with an `int` (0, 1, 2) or a `String` ("red"), but:

- `int color = 7;` the compiler won't stop you, and 7 is not any color.
- `String color = "gren";` a typo still compiles.
- `LightColor color = LightColor.GREN;` is a **compile error**, so the mistake is caught early.

> Class notes (to be filled in): the code the instructor actually wrote, the values for direction, and what other properties there are:

---

## 2. When to Use an Enum (p.25)

When a variable can only be **one of a fixed set of constants**, an enum is a good fit. The handout's examples: the seven days of the week and the twelve months of the year. What they have in common is that all possible values are settled at compile time.

## 3. Declaration Syntax (p.26–27)

Declare it with the `enum` keyword and list the constants inside the braces, separated by commas; by convention they are all uppercase.

```java
public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}
```

To use one, write `TypeName.CONSTANT`:

```java
Day today = Day.TUESDAY;
```

## 4. An Enum Is Actually a Class (p.28, p.30)

- What an `enum` declaration creates is a special kind of class (an enum type). Each constant is an object of this class, and **these are the only ones**.
- Every enum automatically inherits from `java.lang.Enum`. Because Java uses single inheritance (Week 6), an enum **cannot `extends` any other class**, but it can `implements` interfaces.
- An enum body can have fields, methods, and a constructor.
- The constructor cannot be `public` or `protected`. The handout says "package-private or private"; in fact, when you write no modifier, the Java specification treats the enum constructor as `private`. **You cannot `new` it yourself**; Java calls the constructor once for each constant at the start.

```java
enum LightColor {
    RED(30), YELLOW(5), GREEN(25);   // the constructor is called once for each constant

    private final int seconds;

    LightColor(int seconds) {        // private by default
        this.seconds = seconds;
    }

    int getSeconds() {
        return seconds;
    }
}

// LightColor c = new LightColor(10);  // compile error: an enum cannot be new-ed
```

When other members follow the constants, the last constant must end with a **semicolon** (`GREEN(25);` in the example above).

### Supplement: What is a private constructor?

A `private constructor` means **only code inside this class** can call the constructor; outside code cannot use it to `new` an object directly.

A normal `public constructor` lets outside code create objects:

```java
public class Student {
    public Student() {}
}

// in another class
Student s = new Student(); // ✅ allowed
```

Change it to `private`:

```java
public class Student {
    private Student() {}
}

// in another class
Student s = new Student(); // ❌ compile error: the constructor is private
```

Why design it this way? There are two common uses:

**1. Preventing object creation: utility classes**

If a class only provides `static` methods, there is no need to create objects of it.

```java
public class MathUtil {
    private MathUtil() {}

    public static int square(int n) {
        return n * n;
    }
}
```

To use it, call through the class name directly:

```java
int result = MathUtil.square(5); // ✅ 25
// new MathUtil();               // ❌ not allowed
```

The `Math` class from Week 5 works the same way: `Math.sqrt()` and `Math.PI` are used through the class name directly, with no need for `new Math()`.

**2. Letting the class control how objects are created**

Outside code cannot `new` directly, but it can get an object through a `static` method that the class provides:

```java
public class Student {
    private String name;

    private Student(String name) {
        this.name = name;
    }

    public static Student create(String name) {
        return new Student(name); // ✅ can be called from inside the class
    }
}
```

Using it from outside:

```java
Student s = Student.create("Claire"); // ✅
```

This lets `create()` do all the data validation in one place, or decide whether to reuse an existing object. The **Singleton pattern** also commonly uses a private constructor to restrict object creation.

Note: `private` restricts **access to the constructor**, not whether outside code can use the object. Once outside code has the object, it can still call its `public` methods as usual.

**Back to Enum**: an enum is the extreme version of use #2. The constructor is always private, and you don't even need a method like `create()`: the objects are exactly the constants listed inside the braces, created automatically by Java. That is why `LightColor` always has exactly three objects, RED, YELLOW, and GREEN, and it is also why you can compare them with `==`.

## 5. Class Example: Move and the Coordinate System (extending p.25–27)

The instructor then explained using a coordinate system: the four directions up, down, left, and right are a fixed set of values (the condition from p.25), and each direction corresponds to an increase or decrease in x or y. The `Days` enum on p.27 only has names; `Move` goes one step further and lets **each constant carry its own data** (p.28: an enum body can have fields and methods).

### 1. Directions and (x, y) changes

| Constant | (x, y) change | Meaning |
|---|---|---|
| `Left(-10, 0)` | x decreases by 10 | Move left |
| `Right(10, 0)` | x increases by 10 | Move right |
| `Up(0, 10)` | y increases by 10 | Move up |
| `Down(0, -10)` | y decreases by 10 | Move down |

x and y are "the change per step", not a position. The current coordinates plus the change give the new position after one step.

> This is the mathematical coordinate system (y gets larger going up). In screen or game coordinates, y usually gets larger going down; in that case Up should become `(0, -10)`.

### 2. The version from class (the instructor may not have finished it)

```java
public enum Move {
    Left(-10,0), Right(10,0), Up(0,10), Down(0,-10);
    public int x, y;
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    private Move () { this.x = 0; this.y = 0; }
    public void moveUp() { }
}
```

Actually compiling it gives 4 errors, one for each of the four constants:

```text
constructor Move in enum Move cannot be applied to given types;
  required: no arguments
  found:    int,int
```

| Problem | Explanation |
|---|---|
| Missing `(int, int)` constructor (compile error) | `Left(-10, 0)` means "call the constructor with -10 and 0", but there is only the no-argument `Move()`; and it sets both x and y to 0, so the values passed in would be thrown away |
| `public int x, y;` | Each constant is a single object for the whole program, so if anyone writes `Move.Left.x = 99;`, it affects every place that uses Left. Change it to `private final` + getters (Week 5 Encapsulation) |
| `moveUp()` is empty | There is already an `Up` constant, so it usually isn't needed; the instructor may not have finished it yet, so see how the instructor fills it in later |
| Naming | The convention is all uppercase, `LEFT`; `Left` also compiles, it just doesn't follow the convention |

### 3. Fixed version: [`Move.java`](../src/Move.java)

```java
public enum Move {
    Left(-10, 0), Right(10, 0), Up(0, 10), Down(0, -10);

    private final int x, y;          // change per step in the x and y directions

    private Move(int x, int y) {     // Left(-10, 0) calls this constructor
        this.x = x;
        this.y = y;
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }

    public static void main(String[] args) {
        int px = 0, py = 0;           // starting point (0, 0)
        Move[] path = { Move.Up, Move.Up, Move.Right, Move.Down, Move.Left, Move.Left };
        for (Move m : path) {
            px += m.getX();
            py += m.getY();
            System.out.println(m + " -> (" + px + ", " + py + ")");
        }
    }
}
```

Output when starting from (0, 0):

```text
Up -> (0, 10)
Up -> (0, 20)
Right -> (10, 20)
Down -> (10, 10)
Left -> (0, 10)
Left -> (-10, 10)
```

### 4. Extension: keeping the no-argument constructor

If you want to keep `Move()`, you can: having both constructors side by side is **overloading** (Week 5). But there must be a constant without arguments that uses it, for example adding a `Stay` that calls `Move()` and sets x and y to 0.

> Class notes (to be filled in): how the instructor finally completed `moveUp()`, and whether it was paired with a Point/position class:

---

## 6. Iterating Over All Constants with values() (p.29)

The compiler automatically adds a `static` method `values()` that returns an array ordered **in declaration order**, which you can iterate over directly with an enhanced for loop (Week 3):

```java
for (Day d : Day.values()) {
    System.out.println(d);
}
```

This prints seven lines, MONDAY through SUNDAY. `println` prints the constant's name.

## 7. Enums with switch (p.31)

`switch` can use an enum variable directly; after `case` you write only the constant name, **without** adding `Day.`.

```java
switch (day) {
    case MONDAY:
    case TUESDAY:
    case WEDNESDAY:
    case THURSDAY:
    case FRIDAY:
        System.out.println(day + " is a weekday");
        break;
    case SATURDAY:
    case SUNDAY:
        System.out.println(day + " is the weekend");
        break;
}
```

When several `case` labels are stacked without a `break`, execution keeps running downward (fall-through), so Monday through Friday share the same output.

## 8. Supplement: Other Built-in Methods

The handout only mentions `values()`; here are the ones often used alongside it:

| Method | What it does | Example result |
|---|---|---|
| `values()` | Array of all constants | `LightColor.values().length` → `3` |
| `name()` / `toString()` | The constant's name as a string | `"YELLOW"` |
| `ordinal()` | Declaration position, starting at 0 | `YELLOW.ordinal()` → `1` |
| `valueOf(String)` | Finds a constant by name; throws `IllegalArgumentException` if not found | `LightColor.valueOf("YELLOW")` |

You can compare enums directly with `==`, because each constant is only one object (compare with Week 3: String content comparison needs `equals`).

## 9. Supplement: Complete Traffic Light Example

[`TrafficLight.java`](../src/TrafficLight.java) writes the three properties from Section 1 as three enums:

- `Power`: ON/OFF; when the light is off, `change()` does not change the color.
- `LightColor`: each color stores a number of seconds; `next()` cycles GREEN → YELLOW → RED → GREEN.
- `Direction`: NORTH_SOUTH/EAST_WEST, decided when the object is created.

Output:

```text
NORTH_SOUTH OFF RED (30s)
NORTH_SOUTH OFF RED (30s)
NORTH_SOUTH ON GREEN (25s)
NORTH_SOUTH ON YELLOW (5s)
NORTH_SOUTH ON RED (30s)
YELLOW ordinal=1
true
3
```

The second line is still RED: the light hasn't been turned on yet, so `change()` has no effect.

## 10. Corrections to the Lecture Slides

- `WEDENSDAY` on p.27, p.29, and p.31 is misspelled; it should be `WEDNESDAY`. Writing it with the handout's spelling still compiles, but a later `valueOf("WEDNESDAY")` will not find it.
- The switch snippet on p.31 has an extra `}` at the end; it was cut from a longer program, so delete it when using the snippet on its own.

## 11. Checklist

- [ ] The values are a fixed set of options known in advance → use an enum, not `int` or `String` codes
- [ ] Constants are all uppercase, separated by commas
- [ ] When fields/methods follow the constants, put a semicolon after the last constant
- [ ] Don't write `public` on the constructor, and don't `new` it from outside
- [ ] Iterate with `values()`; in a switch, `case` uses only the constant name
- [ ] Compare enums with `==`
