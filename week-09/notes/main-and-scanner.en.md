# CSYE6200 Notes: The main Method, args and Scanner (Class Q&A)

**English edition** · [繁體中文](main-and-scanner.zh-TW.md)

> Personal study notes: the class content is reorganized in my own words. These notes contain no slide text or images; all example programs are my own.

- Course: CSYE6200 (Fall 2026)
- Date: 2026-11-10 (a question asked during the Enum class)
- Related weeks: Week 3 Scanner (`3_Arrays_String_InputOutput.pdf` p.25–27), Week 5 Command-Line Arguments (`6_CLA_Packages_JavaDoc.pdf`)
- Examples: [`../src/ReadName.java`](../src/ReadName.java), [`../src/DigitSum.java`](../src/DigitSum.java)

## Contents

1. [main is the program's entry point](#1-main-is-the-programs-entry-point)
2. [What each word means](#2-what-each-word-means)
3. [How to run it](#3-how-to-run-it)
4. [When to write main in everyday code](#4-when-to-write-main-in-everyday-code)
5. [Why you always see main when using Scanner](#5-why-you-always-see-main-when-using-scanner)
6. [args vs. Scanner](#6-args-vs-scanner)
7. [Midterm key points](#7-midterm-key-points)
8. [Last problem on the midterm cheat sheet: DigitSum](#8-last-problem-on-the-midterm-cheat-sheet-digitsum)

---

## 1. main is the program's entry point

When you run `java Move`, the JVM looks in the `Move` class for a method whose **name and signature match exactly**, and starts executing from its first line:

```java
public static void main(String[] args)
```

If it cannot find one, the program cannot run, and you get `Error: Main method not found in class Move`.

## 2. What each word means

| Part | Why it is written this way |
|---|---|
| `public` | The JVM is outside the class and must be able to call it |
| `static` | When the program first starts there are no objects yet; `static` belongs to the class itself and can be called without `new` (same as Week 5's "why does `sum()` need `static`") |
| `void` | It returns no value to the JVM |
| `main` | A fixed name; the JVM only recognizes this name |
| `String[] args` | Command-line arguments (Week 5). `java Move 10 20` → `args[0]` is `"10"` and `args[1]` is `"20"`; with no arguments it is an empty array of length 0 |

## 3. How to run it

**Command line** (from the repo root):

```sh
javac -encoding UTF-8 -d week-09/bin week-09/src/*.java
java -cp week-09/bin Move
```

1. `javac` compiles the `.java` files into `.class` files.
2. `java Move` starts the JVM, loads the `Move` class, and calls `Move.main(...)`.
3. The code in `main` runs from top to bottom, and the program ends after its last line runs.

**Eclipse**: right-click the file and choose Run As → Java Application; Eclipse does the two steps above for you. If that option is missing from the menu, it usually means the class has no `main`.

## 4. When to write main in everyday code

- **A program needs one entry point**: `main` goes in the class responsible for starting the program; not every class needs one. In Week 6, `Shape`, `Circle` and `Rectangle` have no `main`; only `ShapeTest` has one.
- **Testing or demonstrating a class you wrote**: after writing `Circle`, write a separate `CircleTest` (or `Demo`), and in its `main` create objects with `new`, call methods and print the results. Most class examples and assignments use it this way.
- **Where to put it**: `Move.java` writes `main` directly inside the enum so that a single file can run; an enum can have a `main` just like an ordinary class. A more formal approach is to write a separate `MoveDemo` to hold `main`, so that `Move` is only responsible for "directions".
- **JUnit tests**: test methods are marked with `@Test` and run by JUnit, so you do not need to write your own `main`.

> Extra: in JDK 25 this can be shortened to `void main()`, but class and exams use the full form `public static void main(String[] args)`.

## 5. Why you always see main when using Scanner

Scanner code has to be written inside some method in order to run. For a small program of the "open the program → ask the user for input → print the result" kind, the only place the JVM runs automatically is `main`, so Scanner examples almost always look like this (Week 3 p.25–27):

```java
import java.util.Scanner;              // Scanner is in java.util, so import it

public class ReadName {
    public static void main(String[] args) {     // program entry point
        Scanner sc = new Scanner(System.in);     // System.in = keyboard input
        System.out.print("Name: ");
        String name = sc.nextLine();             // the program stops here and waits for user input
        System.out.println("Hello, " + name);
        sc.close();
    }
}
```

`main` and Scanner are two separate things:

- `main` decides **where the program starts**.
- Scanner decides **where the data is read from**.

## 6. args vs. Scanner

The `String[] args` in `main`'s parentheses is also a kind of input, but it is completely different from Scanner:

| | `String[] args` (Week 5) | `Scanner` (Week 3) |
|---|---|---|
| When the data is given | **When the program starts**, written after the command: `java Sum 10 9` | The program **stops partway through** and waits for the user to type |
| Data type | Always `String`; numbers need `Integer.parseInt(args[0])` | Can be read directly as numbers: `nextInt()`, `nextDouble()` |
| Needs an import? | No | Yes: `import java.util.Scanner;` |
| When no data is given | `args.length` is 0; reading `args[0]` directly throws `ArrayIndexOutOfBoundsException` | The program keeps waiting for input |

Programs that use Scanner usually do not use `args` at all, but the form of `main` is fixed, so `String[] args` still has to be written.

## 7. Midterm key points

**Classifying variables** (a question type I got wrong during midterm review: I left out `main`'s `args`)

- `args` is a **local variable**: it is a parameter of `main`, declared inside the method's parentheses, and is valid only inside `main`.
- `Scanner sc = new Scanner(System.in);` is written inside `main`, so `sc` is also local; the Scanner object it refers to is on the heap (Week 3 references).
- Rule: look at **where the variable is declared**, not where it is used.

**Common Scanner pitfalls** (Week 3 p.27)

- `nextLine()` right after `nextInt()`: `nextInt()` reads only the number and leaves the newline behind, so the following `nextLine()` ends as soon as it reads that newline and gets an empty string. Fix: call `sc.nextLine();` once more in between to consume the newline.
- Forgetting the import: you get `cannot find symbol` … `class Scanner`.

Actual test: entering `5`, newline, `Claire`, the first `nextLine()` gets `[]` (an empty string), and `Claire` is left unread on the next line.

## 8. Last problem on the midterm cheat sheet: DigitSum

The midterm coding problem: read an integer and print the sum of its digits. The template at the end of section 16 of the cheat sheet ([`../src/DigitSum.java`](../src/DigitSum.java)):

```java
import java.util.Scanner;

public class DigitSum {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Math.abs(sc.nextInt());    // -984 → 984
        int sum = 0;
        while (n > 0) {
            sum += n % 10;                 // take the ones digit
            n /= 10;                       // drop the ones digit
        }
        System.out.println(sum);           // 567→18, -984→21
    }
}
```

### Why main appears in this problem

The problem asks you to "read in" a number, meaning the user enters it while the program runs, so Scanner is used (section 6). And this is a complete program that can be run directly, so it needs an entry point, which means writing `public static void main(String[] args)` (section 1). `args` is not used at all in this problem, but the form of `main` is fixed and cannot be left out.

### Line-by-line breakdown

| Code | What it does |
|---|---|
| `import java.util.Scanner;` | Written outside the class, at the very top of the file; without it, `Scanner` gives `cannot find symbol` |
| `Scanner sc = new Scanner(System.in);` | Creates a Scanner that reads from the keyboard; `sc` is a local variable of `main` |
| `sc.nextInt()` | The program stops and waits for user input, read as an `int` |
| `Math.abs(...)` | Takes the absolute value so negative numbers work too (a static method of the Week 5 Math class; no `new Math()` needed) |
| `n % 10` | Gets the ones digit: `567 % 10` → `7` |
| `n /= 10` | Integer division drops the ones digit: `567 / 10` → `56` |
| `while (n > 0)` | Removes one digit per round and stops when `n` becomes 0 |

### Trace: input 567

| Round | `n % 10` | `sum` | After `n /= 10` |
|---|---|---|---|
| Start | — | 0 | 567 |
| 1 | 7 | 7 | 56 |
| 2 | 6 | 13 | 5 |
| 3 | 5 | 18 | 0 → stop |

Output `18`. Verified by actually running it: `567` → `18`, `-984` → `21`, `0` → `0` (the loop does not run even once), `7` → `7`.
