# Java Notes: The Difference Between Class, Object, Instance and Variables

**English edition** · [繁體中文](class-object-instance-draft.zh-TW.md)

## 1. Remember the core concepts first

- **Class**: the blueprint that defines an object's structure and behavior.
- **Object / Instance**: an actual object created from a class.
- **Instance variable**: data that belongs to an individual object; every object has its own copy.
- **Static variable / Class variable**: data that belongs to the class and is shared by that class's objects.
- **Reference variable**: a variable that holds a reference to an object and is used to access the object; it can also be `null`.

> **A class is the blueprint → an instance is the thing built from the blueprint → instance variables are each thing's own data.**

## 2. An instance and an instance variable are not the same

**An instance is the object itself; an instance variable is a variable that belongs to that object.**

Take the `Student` class as an example:

```java
class Student {
    String name; // instance variable
    int age;     // instance variable
}
```

Create two objects (the following statements can go in the `main` method):

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
| `Student` | The class; it defines the structure of a student object |
| The object created each time `new Student()` runs | An instance of `Student` |
| `s1`, `s2` | Reference variables that hold references to the objects |
| `name`, `age` | Instance variables that each `Student` object has its own copy of |
| `s1.name`, `s2.name` | Access the `name` of each of the two objects |

> **Precisely: `s1` and `s2` are reference variables; the objects they point to are the instances.**

## 3. Using a Car example to understand each object's own data

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

## 4. Instance variables vs Static / Class variables

```java
class Student {
    String name;             // instance variable: each student's own name
    static int studentCount; // class variable: count data shared by the class
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

- `name` has no `static`: every `Student` object has its own name.
- `studentCount` has `static`: it belongs to the `Student` class, and accessing it as `Student.studentCount` makes that clear.
- `studentCount` **does not count objects automatically**; in this example it is set to `2` by hand. To count automatically, you need to write the update logic yourself.

## 5. Terminology comparison table

| Term | Chinese term | What it is | Does every object have its own copy? |
|---|---|---|---|
| Class | 類別 | The blueprint for creating objects | Not applicable |
| Object / Instance | 物件／實例 | An actual object created from a class | Not applicable |
| Instance variable | 實例變數 | Data owned by an individual object | Yes |
| Static / Class variable | 靜態變數／類別變數 | Shared data that belongs to the class | No |
| Reference variable | 參考變數 | A variable that holds an object reference or `null` | Depends on where it is declared; this name describes the value it holds |

## 6. Breaking down one line of Java code

```java
Student s1 = new Student();
```

| Code piece | Meaning |
|---|---|
| `Student` | The class type that `s1` is declared with |
| `s1` | A reference variable |
| `new Student()` | Creates a new `Student` object and produces a reference to it |
| `=` | Assigns the object reference on the right to `s1` |

Then:

```java
s1.name = "Claire";
```

This means: use `s1` to find the object it points to, then set that object's instance variable `name` to `"Claire"`.

## 7. Is there a difference between Object and Instance?

At the beginner stage of Java, **object and instance can usually be treated as two ways of saying the same thing**.

- **Object**: emphasizes "this is an object".
- **Instance**: emphasizes "this object is an instance of some class".

For example, `new Student()` creates an object, which is also an instance of `Student`.

## 8. Key points to remember

> **Class = blueprint**  
> **Object / Instance = an object built from the blueprint**  
> **Instance variable = each object's own data**  
> **Static / Class variable = data shared by the class**  
> **Reference variable = a variable used to point to an object**

When you see `Student s1 = new Student();`, remember to tell these apart:

```text
s1                       → reference variable
new Student()            → creates an object and produces its reference
the object s1 points to  → an instance of Student
that object's name       → instance variable
```
