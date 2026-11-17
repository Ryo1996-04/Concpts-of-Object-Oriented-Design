# Instance vs. Static: Slide Companion Notes

**English edition** · [繁體中文](instance-vs-static.zh-TW.md)

Corresponds to `5_Classes_And_Objects.pdf`: the variables and Person counter on p.29–31, and the methods on p.32–35. This note is a topic supplement that follows p.10–28 of the main notes.

## Terminology First

- **Instance**: an object created from a class, for example the object created by `new Person()`.
- **Reference variable**: a variable that holds an object reference, for example `p1` in `Person p1`.
- **Instance variable / instance field**: a field that each object has its own copy of, for example Person's `name` and `age`.
- **Class variable / static field**: a field marked `static`, with a single copy shared by all objects of the same class, for example `counter`.

So "instance" by itself should not be translated directly as "object variable" (物件變數). If `p1` is declared in main, it is a local reference variable; the `age` accessed through `p1.age` is the instance field.

## p.29–30: Instance Variable vs. Class Variable

| Comparison | Instance variable | Class variable / static field |
|---|---|---|
| Declaration | Inside the class, outside methods, without static | Inside the class, outside methods, with static |
| Belongs to | A specific object | The class |
| Number of copies | One per object | One shared by the whole class |
| How to access | `p1.age` | Preferably `Person.counter` |
| Needs an object first? | An object is needed for that instance's field to exist | The class field can be accessed without an instance |
| Suitable data | Each person's name and age | Shared data such as a running creation count |
| Has a default value? | Yes | Yes |

```java
class Person {
    String name;            // Instance field: each person's own data
    int age;                // Instance field
    static int counter = 0; // Class variable: shared count
}
```

This only illustrates the fields and does not include the constructors from the counter example below. Both instance and static fields have default values determined by their type; a local variable, by contrast, must be assigned before it can be read.

## p.31: Full Person Counter Example

Corresponds to [Person.java](../src/Person.java) in the repository:

```java
public class Person {
    String name;
    int age;
    static int counter = 0;

    public Person() {
        this("", 0); // Constructor chaining within the same object
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        counter++; // Count once each time an object is created
    }

    public static void main(String[] args) {
        Person p1 = new Person();
        Person p2 = new Person("Smith", 32);

        System.out.println(p1.counter);     // 2
        System.out.println(p2.counter);     // 2
        System.out.println(Person.counter); // 2, the recommended way to write it
    }
}
```

Flow: counter is 0 when the class is initialized; it becomes 1 after p1 is created and 2 after p2 is created. All three println calls run after both objects have been created, so they read the same value, 2.

```text
Person class: counter = 2 (shared)

p1 -> object A: name = "", age = 0
p2 -> object B: name = "Smith", age = 32
```

`this("", 0)` only delegates the initialization work; it does not create another object. `counter++` lives only in the parameterized constructor, so in this example each object creation adds exactly one.

### Different Results from Changing an Instance Field vs. a Static Field

Add the following at the end of the main above:

```java
p1.age = 99;
System.out.println(p1.age); // 99
System.out.println(p2.age); // 32: the other object's age did not change

Person.counter = 10;
System.out.println(p1.counter); // 10
System.out.println(p2.counter); // 10: both read the same static field
```

Setting counter directly here is only to show the sharing effect; real counting code usually does not let outside code change counter freely.

If you change `static int counter = 0;` to `int counter = 0;`, each object starts from 0 on its own, and after each runs `counter++` once, p1.counter and p2.counter are both 1. In that case `Person.counter` is a compile-time error, because you must specify an object.

Counter represents the running total of objects created in this example, not the number of objects currently alive; garbage collection does not automatically decrement it. It is also not a thread-safe counter; this example is meant to be understood in a single-threaded setting first.

## p.32–34: Instance Method vs. Static Method

| Comparison | Instance method | Static method |
|---|---|---|
| Declaration | No static | Marked static |
| Typical call | `p1.getName()` | `Person.getCount()` |
| Has a current receiver? | Yes, can use `this` | No implicit receiver, cannot use `this` |
| Using instance fields directly | Can access its own visible fields | Must first obtain an explicit object reference |
| Using static fields | Yes | Yes |
| Typical use | Reading or operating on an object's state | Operations shared by the class, or utility calculations |

The following is a standalone, compilable comparison example, saved as `MemberDemo.java`:

```java
public class MemberDemo {
    int age = 20;
    static int count = 1;

    int getAge() {
        return this.age; // Instance method, has a receiver
    }

    static int getCount() {
        return count; // Static method accessing a static field
    }

    static int readAge(MemberDemo person) {
        return person.age; // Legal: through an explicit object reference
    }

    public static void main(String[] args) {
        MemberDemo p = new MemberDemo();
        System.out.println(p.getAge());          // 20
        System.out.println(MemberDemo.getCount()); // 1
        System.out.println(MemberDemo.readAge(p)); // 20

        // System.out.println(age);      // Compile-time error: no object specified
        // System.out.println(this.age); // Compile-time error: there is no this in a static context
    }
}
```

The statement on slides p.33–34 that "a static method cannot access instance variables" needs a clarification: **it cannot access them directly without a receiver, but it can use an explicit object reference.** The `person.age` in the example above is legal, though it must still follow access control; if person is null, a NullPointerException occurs.

## p.35: Why Is MyMath a Good Fit for Static?

```java
public static double square(double x) {
    return x * x;
}
```

This method declaration goes inside the MyMath class. `MyMath.square(5)` needs only the argument 5 to produce 25.0; it does not need the state of any MyMath object, so static is a good fit.

By contrast, if `Rectangle.area()` computes the current object's length × width, it is better as an instance method, because different rectangles have different dimensions.

## Common Confusions and How to Choose

- **Static does not mean constant.** `static int counter` can be modified; the keyword that prevents reassignment is `final`.
- **Main being static does not mean the variables inside it are static.** `Person p1` inside main is still a local variable.
- **Do not change every field to static just to get rid of a compile error.** If everyone's age were static, all objects would share a single age, which usually does not fit the data model.
- **Ask whether the data belongs to a specific object.** Name, age, and rectangle width usually differ per object; a running creation count can be shared by the class.

## Slide Index

| Page | What this note covers |
|---|---|
| p.29 | Distinguishing instance, class, and local variables |
| p.30 | The shared nature of static fields |
| p.31 | Person.counter and the three outputs of 2 |
| p.32 | Instance methods and the object receiver |
| p.33–34 | Static methods and the limits on accessing instance members |
| p.35 | MyMath's static utility method |

## Further Reading

[Today's combined notes](classes-and-objects.en.md) add a full terminology comparison of Student, Car, and reference variables, plus the p.39 explanation of garbage collection / System.gc().
