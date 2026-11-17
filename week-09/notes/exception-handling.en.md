# CSYE6200 Notes: Exception Handling

**English edition** · [繁體中文](exception-handling.zh-TW.md)

> Personal study notes: the class content is reorganized in my own words. These notes contain no slide text or images; the page numbers are only there to cross-reference the course handout. All example programs are my own, and every error message was produced by actually running `javac` / `java`.

- Course: CSYE6200 (Fall 2026)
- Unit: Exceptions; Unit Testing in Java (`9_Exception Handling in Java.pdf`; these notes cover Exception Handling, p.3–30; Unit Testing, p.31–40, is written up separately)
- Date: 2026-11-10
- Runnable example programs: [`../src/`](../src) (DivideDemo, FileCheck, InvalidAgeException, AgeDemo)

## Contents

1. [Class opener: three kinds of errors](#1-class-opener-three-kinds-of-errors)
2. [Two ways to handle errors (p.3–4)](#2-two-ways-to-handle-errors-p34)
3. [What is an exception? (p.5)](#3-what-is-an-exception-p5)
4. [The Throwable hierarchy (p.6–7)](#4-the-throwable-hierarchy-p67)
5. [new and the heap: memory-related errors (p.4, p.5, p.7)](#5-new-and-the-heap-memory-related-errors-p4-p5-p7)
6. [Checked vs. unchecked (p.8–10)](#6-checked-vs-unchecked-p810)
7. [What happens without handling: reading a stack trace (p.11–13)](#7-what-happens-without-handling-reading-a-stack-trace-p1113)
8. [Why we need exception handling (p.13–14)](#8-why-we-need-exception-handling-p1314)
9. [try…catch (p.15–16)](#9-trycatch-p1516)
10. [Execution flow and the termination model (p.17, p.27)](#10-execution-flow-and-the-termination-model-p17-p27)
11. [throw and throws (p.18–21)](#11-throw-and-throws-p1821)
12. [Multiple catch blocks (p.22–23)](#12-multiple-catch-blocks-p2223)
13. [finally (p.24–25)](#13-finally-p2425)
14. [Rethrowing (p.26)](#14-rethrowing-p26)
15. [User-defined exceptions (p.28–30)](#15-user-defined-exceptions-p2830)
16. [Extra: common methods of an exception object](#16-extra-common-methods-of-an-exception-object)
17. [Syntax corrections to the handout](#17-syntax-corrections-to-the-handout)
18. [Checklist](#18-checklist)

---

## 1. Class opener: three kinds of errors

The professor started by splitting program errors into three kinds (these pages are not in the handout; this was explained in class). The difference is **when the error is found** and **who finds it**.

| Kind | When it is found | Who finds it | Can the program run? | How to deal with it |
|---|---|---|---|---|
| Syntax error (compile-time error) | At compile time | `javac` | No; not even a `.class` file is produced | Fix the code according to the error message |
| Runtime error | When execution reaches a certain line | The JVM, by throwing an exception | It compiles, but stops partway through | **Exception handling** (these notes) |
| Logic error | Only when you look at the result | A person or a test | It runs to the end and does not crash | Testing (Unit Testing in the second half of the handout), debugging |

### Syntax error: the code breaks Java's rules

```java
int x = 10          // missing semicolon
System.out.println(x);
```

```text
Syntax.java:3: error: ';' expected
        int x = 10
                  ^
1 error
```

A misspelled method name is also caught at compile time. Handout p.11 happens to contain exactly this mistake: `input.netxInt()`.

```text
Typo.java:5: error: cannot find symbol
        int num1 = input.netxInt();
                        ^
  symbol:   method netxInt()
  location: variable input of type Scanner
```

### Runtime error: the syntax is correct, but something goes wrong while running

```java
public static int div(int a, int b) { return a / b; }
// call div(10, 0) in main
```

It compiles, and stops only when it reaches the division by 0:

```text
Exception in thread "main" java.lang.ArithmeticException: / by zero
	at Runtime.div(Runtime.java:2)
	at Runtime.main(Runtime.java:4)
```

The `ArrayIndexOutOfBoundsException` from Week 3, and the missing value for `args[0]` in the Week 9 Scanner notes, are also runtime errors.

### Logic error: the program finishes, but the answer is wrong

```java
int a = 2, b = 3;
double avg = (a + b) / 2;     // want the average 2.5
System.out.println(avg);      // actually prints 2.0
```

Both sides of `(a + b) / 2` are `int`, so integer division happens first and gives `2`; it only becomes `2.0` when stored in the `double`. Changing it to `(a + b) / 2.0` prints `2.5`. Neither the compiler nor the JVM reports an error, so only testing can find it.

> **Key point**: exception handling deals only with **runtime errors**. With a syntax error the program cannot even start; a logic error never throws an exception.

---

## 2. Two ways to handle errors (p.3–4)

**Approach 1: check directly at every place that might fail** (p.3)

```text
Do task A
If A fails → handle the error
Do task B
If B fails → handle the error
```

- Pro: you can see at a glance whether something is checked.
- Con: the normal logic and the error handling are mixed together; once the program gets large it is hard to read, change, maintain and debug.

**Approach 2: exception handling** (p.4)

- Takes the error handling out of the "main line" code and gathers it in `catch`.
- The program becomes clearer, more robust (it does not simply crash when an error occurs) and more fault-tolerant (after an error it can still continue or end safely).

Common failures listed in the handout: memory cannot be allocated, an array index is out of range, division by 0, an invalid argument is passed in.

## 3. What is an exception? (p.5)

An exception is an **abnormal, unexpected** situation while a program is running. The handout lists three sources:

| Source | Example |
|---|---|
| Programmer error | Division by 0 |
| User error | Entering a negative age (section 15 handles this with a custom exception) |
| Insufficient physical resources | Running out of memory |

## 4. The Throwable hierarchy (p.6–7)

Java represents the different kinds of errors with **class inheritance** (the inheritance concept from Week 6). The top of all errors and exceptions is `Throwable`:

```text
Throwable
├── Error                          serious errors; normal programs don't handle them
│   ├── OutOfMemoryError
│   ├── ThreadDeath
│   └── AWTError
└── Exception                      problems normal programs may hit and can handle
    ├── IOException                ← checked
    └── RuntimeException           ← unchecked
        ├── ArithmeticException
        ├── ArrayIndexOutOfBoundsException
        ├── NullPointerException
        ├── ClassCastException
        └── InputMismatchException
```

| | Error | Exception |
|---|---|---|
| Meaning | A serious, system-level problem | A problem that may happen while the program runs |
| Should it be handled? | Usually not caught; even if handled, the program cannot recover | It should be handled |
| Example | `OutOfMemoryError` | `ArithmeticException`, `IOException` |

Because this is an inheritance relationship, `catch (Exception e)` can catch `Exception` **and all of its subclasses** (polymorphism: a superclass-type variable can refer to a subclass object).

## 5. new and the heap: memory-related errors (p.4, p.5, p.7)

In class the professor mentioned that `new` will **allocate to the heap**, meaning: `new` allocates a block of memory on the heap to hold the new object.

### 1. Stack and heap (Week 3 review)

| | Stack | Heap |
|---|---|---|
| What goes there | A method's local variables, parameters, references | **Objects** and **arrays** created with `new` |
| When it is cleared | Automatically, when the method ends | Reclaimed by the garbage collector once no reference points to it (Week 4 p.39) |
| Size | Small | Large, but with a limit |

```java
int[] scores = new int[5];
```

1. `new int[5]` asks the heap for a block of space that can hold 5 ints (**allocate**).
2. The elements are initialized to 0.
3. The address of this block (the reference) is stored in the variable `scores` on the stack.

### 2. When new fails: OutOfMemoryError

The first common failure on p.4 is exactly **`new` failing to get memory**: the heap is full, and `new` cannot get any space.

Actual test: set the heap limit to 64 MB (`java -Xmx64m Oom`), then `new` an array of about 400 MB:

```java
int[] big = new int[100_000_000];   // about 400 MB
```

```text
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at Oom.main(Oom.java:3)
```

Running the same program with the default heap size, the allocation succeeds. So whether it fails depends on how big the heap is.

- Its position in the hierarchy diagram from section 4 is **`Throwable → Error → OutOfMemoryError`**. It is an `Error`, not an `Exception`: once memory is used up, catching it usually cannot save the program, so normal programs do not handle it.
- It matches the third source on p.5: **insufficient physical resources**.
- By the classification in section 1 it is a runtime error: it compiles fine and fails only when execution reaches the `new` line.

### 3. Two other errors from the same set of concepts

| Error | Relation to memory | Class |
|---|---|---|
| `StackOverflowError` | The **stack** is used up, usually by recursion that never stops: each method call stacks one more frame on the stack. Actual test: a method that calls itself threw it at roughly the 22,081st level (the depth varies by computer and settings) | `Error` |
| `NullPointerException` | The reference is `null`, **pointing to no object on the heap**, yet you try to use it | `Exception` (unchecked); it is a bug in the program |

```java
int[] a = null;
System.out.println(a.length);
```

```text
Exception in thread "main" java.lang.NullPointerException: Cannot read the array length because "<local1>" is null
```

(The `<local1>` in the message is the variable `a`. If you compile with `-g`, or run it in Eclipse, the variable name is shown directly.)

### 4. The exception itself is also on the heap

`throw new ArithmeticException(...)` also uses `new`. An exception is itself an object, so it is likewise allocated on the heap. It does not disappear when `div()` ends, which is why it can travel all the way to the catch in `main` and be caught by the reference `e`.

## 6. Checked vs. unchecked (p.8–10)

### The rule

| | Checked exception | Unchecked exception |
|---|---|---|
| Which classes | Subclasses of `Exception` that are **not** under `RuntimeException` | `RuntimeException` and its subclasses (`Error` is not checked either) |
| Does the compiler check it? | **Yes**: if not handled, compilation fails | No; it only happens at run time |
| Usual cause | Conditions outside the program: a file does not exist, the network drops | Bugs in the program itself: division by 0, null, a bad index |
| Examples | `IOException`, `FileNotFoundException`, your own `extends Exception` | `ArithmeticException`, `NullPointerException` |

### Checked example (p.9)

Opening a file can fail (the file does not exist), so `new FileReader(...)` throws the checked `FileNotFoundException`. Compiling without handling it at all gives:

```text
error: unreported exception FileNotFoundException; must be caught or declared to be thrown
```

The error message directly gives **two solutions**:

1. **must be caught**: catch it with `try…catch` ([`FileCheck.java`](../src/FileCheck.java)).
2. **declared to be thrown**: add `throws FileNotFoundException` to the method header and let the caller handle it (section 11).

When `FileCheck` runs and there is no `logs.txt` in the directory, it prints:

```text
not found: logs.txt (系統找不到指定的檔案。)
```

The part in parentheses is the operating system's message, and it changes with the Windows display language. (Here it is the Traditional Chinese Windows message for "The system cannot find the file specified.")

### Class example: CheckedException (file missing, corrupted…)

The version the professor wrote in class:

```java
import java.io.File;
public class CheckedException {
    public static void main(String[] args) {
        String fileName = "C:/example.txt";
        File input = new File(fileName);
        FileReader fr = new FileReader(input);
}
```

**Compiled as is, it hits three layers of errors in turn** (fix one layer, then compile again):

| Layer | Error message | Cause | Kind |
|---|---|---|---|
| 1 | `reached end of file while parsing` | The class's closing `}` is missing; javac checks syntax first, and until this layer passes none of the later errors appear | syntax error |
| 2 | `cannot find symbol` … `class FileReader` | Only `File` is imported; `FileReader` is also in `java.io`, so import it or change to `import java.io.*;` | compile-time error |
| 3 | `unreported exception FileNotFoundException; must be caught or declared to be thrown` | What the professor really wanted to show: a checked exception that is not handled (same as handout p.9) | compile-time error |

**Why file-related exceptions are checked: the program cannot control them**

The syntax of `new FileReader(input)` is completely correct. Whether it succeeds depends on **the world outside the program**:

- Whether the file exists
- Whether the path actually points to a folder
- Whether you have read permission
- Whether, halfway through reading, the USB drive gets unplugged or the network drive disconnects
- Whether the content is corrupted

None of these are things the programmer can control or fix in advance, so Java forces you to write, **at compile time**, "what to do if it happens". By contrast, division by 0 can be avoided simply by checking `b != 0` first; it is a bug in the program itself, so it is unchecked.

**Fixed version: [`../src/CheckedException.java`](../src/CheckedException.java)**

```java
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CheckedException {
    public static void main(String[] args) {
        String fileName = (args.length > 0) ? args[0] : "C:/example.txt";
        File input = new File(fileName);
        FileReader fr = null;
        try {
            fr = new FileReader(input);              // open: may throw FileNotFoundException
            int ch;
            while ((ch = fr.read()) != -1) {         // read: may throw IOException
                System.out.print((char) ch);
            }
            System.out.println();
        } catch (FileNotFoundException e) {          // subclass first
            System.out.println("cannot open: " + e.getMessage());
        } catch (IOException e) {                    // superclass after
            System.out.println("read failed: " + e.getMessage());
        } finally {
            if (fr != null) {
                try {
                    fr.close();                      // close() may also throw IOException
                } catch (IOException e) {
                    System.out.println("close failed: " + e.getMessage());
                }
            }
            System.out.println("finally: done");
        }
    }
}
```

| What the code does | Why |
|---|---|
| `fr` is declared outside `try` and set to `null` first | A variable declared inside `try` cannot be seen in `finally` (section 10, termination model) |
| `finally` checks `fr != null` first | If the file never opened, `fr` is still `null`, and calling `close()` directly would throw a `NullPointerException` |
| `close()` is wrapped in another try | `close()` itself also throws the checked `IOException` |
| The file name can be passed in through `args[0]` | Ties in with the class Q&A on main and args: `java CheckedException <path>` |

**Actual test results for each situation**

| Situation | Which step fails | Result |
|---|---|---|
| File does not exist | `new FileReader(...)` | `FileNotFoundException`: `(系統找不到指定的檔案。)` ("The system cannot find the file specified.") |
| The path is actually a folder | `new FileReader(...)` | Also `FileNotFoundException`, with the message `(存取被拒。)` ("Access is denied.") |
| No read permission | `new FileReader(...)` | `FileNotFoundException` (access denied); not tested separately |
| Normal file | No error | Prints the content, then runs finally at the end |
| Device disconnects halfway through reading | `fr.read()` | `IOException`, caught by the second catch; cannot be simulated here, not tested |
| **Corrupted content** | **No exception is thrown** | Prints `abc��def`; the broken bytes are replaced with `�` |

Every situation ends by printing `finally: done`.

**Note: a "corrupted file" does not necessarily throw an exception**

I wrote a file containing invalid UTF-8 bytes, and reading it with `FileReader` threw **no exception at all**: the broken bytes were silently replaced with `�`, and the program ran to the end as usual. This turns into the **logic error** from section 1: the program does not crash, but the data is wrong.

To make corrupted content throw an exception, read it in strict mode instead:

```java
try (BufferedReader r = Files.newBufferedReader(Path.of(fileName), StandardCharsets.UTF_8)) {
    System.out.println(r.readLine());
} catch (IOException e) {
    System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
}
```

```text
MalformedInputException: Input length = 1
```

`MalformedInputException` is a subclass of `IOException`, so `catch (IOException e)` can catch it. (You need to import `java.io.BufferedReader`, `java.nio.charset.StandardCharsets`, `java.nio.file.Files` and `java.nio.file.Path`; the syntax is the try-with-resources covered in the extra note in section 13.)

### Unchecked examples (p.10)

| Exception | When it happens |
|---|---|
| `NullPointerException` | Calling a method or accessing a field on `null`, e.g. `String s = null; s.length();` |
| `IndexOutOfBoundsException` | An index is out of range; for arrays it is the subclass `ArrayIndexOutOfBoundsException` |
| `ArithmeticException` | Integer division by 0 |
| `ClassCastException` | Casting an object to a type it is not actually |
| `IllegalArgumentException` | A method receives an unreasonable argument (usually you `throw` it yourself) |
| `IOError` | A serious I/O failure. Note: it is actually a subclass of `Error`, not an exception |

For a `ClassCastException` to happen only at run time, the variable's type must be a superclass such as `Object`:

```java
Object o = 3.14;
String s = (String) o;   // compiles; ClassCastException at run time
```

If you write `Double d = 3.14; String s = (String) d;`, the compiler knows a `Double` can never be a `String` and gives a compile error right away: `incompatible types`.

## 7. What happens without handling: reading a stack trace (p.11–13)

The program on p.11 reads two numbers and divides them. Enter `0` as the second number:

```text
Exception in thread "main" java.lang.ArithmeticException: / by zero
	at Example.div(Example.java:4)
	at Example.main(Example.java:12)
```

| Part | Meaning |
|---|---|
| `in thread "main"` | It happened in the `main` thread |
| `java.lang.ArithmeticException` | The exception's class (full name including the package) |
| `/ by zero` | The error message, i.e. what `getMessage()` returns |
| `at Example.div(Example.java:4)` | **Top line = where the error actually happened**: the `div` method, line 4 |
| `at Example.main(Example.java:12)` | The call path: `main` on line 12 called `div` |

Reading from top to bottom gives "where it failed ← who called it". If an exception is not caught, it keeps passing up to the caller; if it gets out of `main` and still nobody has handled it, the program **terminates abnormally** (p.13), and none of the remaining code runs.

## 8. Why we need exception handling (p.13–14)

- When an exception happens, the normal flow is interrupted; if it is not handled, the program terminates abnormally.
- The job of exception handling is to decide whether to **bring the program back to a normal state and keep running**, or to **end it safely**.
- Terminology: an exception is **thrown**, and an exception handler **catches** it.

Scope of use on p.14:

| Suitable for | Not suitable for |
|---|---|
| **Synchronous errors**: caused directly by executing a particular line, e.g. division by 0 | **Asynchronous events**: unrelated to which line is currently running, e.g. disk I/O completion, mouse clicks; these are handled by other mechanisms (interrupts, event listeners) |
| The system can recover from the error | |
| The error happens at place A but must be handled at place B | |
| It cannot be recovered, but must be **shut down cleanly** (close files, save data) | |

## 9. try…catch (p.15–16)

```java
try {
    // normal code that might throw an exception
} catch (ExceptionType e) {
    // handling that runs only when an exception happens
}
```

- `try`: wraps the code that **might fail**.
- `catch (Type name)`: the parentheses look like a method parameter; `e` is the exception object that was thrown.
- p.16 wraps the whole block (read the numbers, divide, print the result) in `try`, catches every exception with `catch (Exception e)`, and prints it with `System.err.println(e)`.
- `System.err` is the standard error stream (one of the three standard streams from Week 3 p.23), and Eclipse shows it in red. It and `System.out` are two different streams; when both are used, the printed order may differ from the program order. That is why the examples in these notes all use `System.out`.

### 1. Class diagram: business logic and error handling

The professor drew two tables:

- **business logic table = `try` block**: holds what the program **normally needs to do** (read numbers, calculate, print the result).
- **error object**: when the business logic fails partway through, Java **creates an exception object** and throws it; that is the error object in the diagram.
- **error handling table = `catch` block**: based on the error object's **type**, it finds the **corresponding** way to handle it (corresponding error handling).

```text
┌──────────── try: business logic ────────────┐
│ read first number                            │
│ read second number ── input abc ──→ [InputMismatchException object] ──┐
│ div(num1, num2) ── divisor 0 ──→ [ArithmeticException object] ──┐     │
│ print result                                 │                  │     │
└──────────────────────────────────────────────┘                  │     │
                                                                  │     │
┌──────────── catch: error handling ──────────┐                  │     │
│ catch (ArithmeticException e)    { handle divide by 0 } ←───────┘     │
│ catch (InputMismatchException e) { handle bad input }   ←─────────────┘
└──────────────────────────────────────────────┘
```

Each kind of error object has its own corresponding catch.

**What is the error object?**

- It is a **real object**: an instance of an exception class, stored on the heap just like any other object created with `new` (section 5).
- The object carries three pieces of information:
  - **Type**: which kind of error, e.g. `ArithmeticException`.
  - **Message**: `e.getMessage()`, e.g. `/ by zero`.
  - **Where it happened**: the stack trace, printed with `e.printStackTrace()`.
- The **`e` in `catch (ArithmeticException e)` is a reference to this object**, written the same way as a method parameter.

**How it is matched to the right catch**: Java compares the error object's type against the catches **from top to bottom**, runs the **first one whose type matches**, skips the rest, and then continues after the last catch. "Matches" includes subclasses; see the next subsection.

### 2. Compared with the p.7 inheritance diagram: why one `catch (Exception e)` catches everything

The professor then connected the p.7 inheritance diagram to the code on p.16. p.16 has only **one** `catch (Exception e)`, yet it catches every error that might happen in the try. The reason: **a catch catches the specified type and all of its subclasses.**

Two kinds of error objects might appear in the try on p.16. Follow them upward in the diagram:

```text
InputMismatchException ─┐
                        ├─→ RuntimeException ─→ Exception ─→ Throwable
ArithmeticException ────┘                          ↑
                                          catch (Exception e) is at this level
```

Both are under `Exception`, so both are caught. This is the polymorphism from Week 6: a variable `e` of the superclass type can refer to a subclass object.

**Actually running p.16** (with `div` from p.11 added):

| Input | Printed | Explanation |
|---|---|---|
| `10 2` | `5` | No exception; the catch is skipped |
| `10 0` | `java.lang.ArithmeticException: / by zero` | Class name + message |
| `10 abc` | `java.util.InputMismatchException` | This exception **has no message**, so only the class name is printed |

`System.err.println(e)` automatically calls `e.toString()`, whose format is "full class name: message", so you can tell which kind of error object it is.

**Using the inheritance diagram to decide what a catch can catch**

| How the catch is written | Position in the diagram | Catches |
|---|---|---|
| `catch (ArithmeticException e)` | Bottom level | Only division by 0 |
| `catch (RuntimeException e)` | Third level | Everything under it: division by 0, null, index, cast, input format |
| `catch (Exception e)` | Second level, left side | Both the `RuntimeException` side and the `IOException` side |
| `catch (Throwable e)` | Top level | Even `Error`, but this is **not recommended** |

- **The `Error` on the right is not caught**: `OutOfMemoryError` is not under `Exception`, so `catch (Exception e)` cannot catch it. This also fits the design: normal programs are not supposed to handle Errors anyway.
- **You cannot catch a checked exception that cannot possibly happen**: if the try contains only `10 / 0` but you write `catch (IOException e)`, you get a compile error:

```text
error: exception IOException is never thrown in body of corresponding try statement
```

`IOException` is checked, and the compiler knows that no code in the try throws it. Unchecked exceptions are not checked this way.

### 3. p.16 (one big catch) vs. p.23 (separate catches)

| | p.16: `catch (Exception e)` | p.23: one catch per kind of error |
|---|---|---|
| In the diagram | Catches at an **upper** level | Catches at the **bottom** level |
| Pro | Short to write; catches any error | Each kind of error gets its own handling (the class diagram above) |
| Con | Every error is handled the same way; you cannot tell which kind it was | You have to write them one by one |

When you mix the two, the order must go **from bottom to top**: the lower a class is in the diagram (subclasses), the earlier its catch is written, with `catch (Exception e)` last (section 12).

### 4. Class experiment: commenting out the try…catch on p.16

The professor took the p.16 code, commented out the try…catch, and compared the two versions. Both versions of `main` below have an extra line at the end, `System.out.println("end of main");`, to see whether the program ran to completion (I added this line myself; it is not in the handout).

```java
// try {
    System.out.println("enter the first number");
    int num1 = input.nextInt();
    System.out.println("enter the second number");
    int num2 = input.nextInt();
    int result = div(num1, num2);
    System.out.println(result);
// }
// catch (Exception e) {
//     System.err.println(e);
// }
System.out.println("end of main");
```

**It still compiles after commenting it out**: the `ArithmeticException` and `InputMismatchException` that might happen here are both **unchecked**, so the compiler does not force you to handle them. If the try contained a checked exception such as `FileReader`'s, commenting it out would make compilation fail (section 6).

**Actual results for three inputs**

| Input | With try…catch | Without try…catch |
|---|---|---|
| `10 2` | `5` → `end of main` | **Exactly the same**: `5` → `end of main` |
| `10 0` | `java.lang.ArithmeticException: / by zero` → `end of main` | Prints the full stack trace, **no** `end of main` |
| `10 abc` | `java.util.InputMismatchException` → `end of main` | Prints the full stack trace, **no** `end of main` |
| exit code | `0` in all three cases (normal end) | `1` when an error occurs (abnormal termination) |

When nothing goes wrong, the two versions behave identically; try…catch only has an effect when an exception happens.

**Without try…catch, input `10 0`**

```text
Exception in thread "main" java.lang.ArithmeticException: / by zero
	at Test.div(Test.java:4)
	at Test.main(Test.java:13)
```

1. `a / b` inside `div()` creates an `ArithmeticException` object.
2. `div()` has no catch, so the exception passes back to `main`, which called it (line 13).
3. `main` has no catch either, so the exception passes out of `main`.
4. Nobody catches it, so the JVM prints the stack trace and **the program terminates abnormally**. Neither `System.out.println(result)` nor `end of main` runs (p.13).

**Without try…catch, input `10 abc`**

```text
Exception in thread "main" java.util.InputMismatchException
	at java.base/java.util.Scanner.throwFor(Scanner.java:977)
	at java.base/java.util.Scanner.next(Scanner.java:1632)
	at java.base/java.util.Scanner.nextInt(Scanner.java:2297)
	at java.base/java.util.Scanner.nextInt(Scanner.java:2251)
	at Test.main(Test.java:12)
```

The exception is created **inside Java's built-in Scanner**, so the stack trace is longer.

> **Tip for reading a stack trace**: lines starting with `java.base/` are JDK internal code and can be skipped. Look down for the **first line that is your own class**; here it is `Test.main(Test.java:12)`, which is `int num2 = input.nextInt();`. That is the line where the problem is.

**With try…catch, same input `10 0`**

```text
java.lang.ArithmeticException: / by zero
end of main
```

1. The exception is still created in `div()` and passed back to `main`.
2. The type of `catch (Exception e)` matches, so it is caught.
3. `System.err.println(e)` inside the catch prints only one line, "class name: message", not the whole stack trace.
4. Execution continues after the catch and prints `end of main`; **the program ends normally**.

`System.out.println(result)` inside the try is still skipped (termination model, section 10), but the code **outside** the try…catch can keep running.

**Summary**

| | With try…catch | Without try…catch |
|---|---|---|
| Does it compile? | Yes | Yes (because the exceptions are unchecked) |
| When nothing goes wrong | Normal | Normal; both versions are the same |
| Who handles an error | The catch you wrote | Nobody; it is left to the JVM |
| What gets printed | Whatever the catch decides to print | The JVM prints the full stack trace |
| After the error | Continues with the code after the catch | **Terminates immediately**; nothing after it runs |
| How it ends | Normal end (exit code 0) | Abnormal termination (exit code 1) |

**Key point**: the exception happens either way; the only difference is **whether someone catches it afterward**. If it is caught, the program can decide how to handle it and then continue (the "return to normal" from p.13); if it is not caught, the program simply crashes.

## 10. Execution flow and the termination model (p.17, p.27)

| Situation | Flow |
|---|---|
| **No** exception in `try` | Finish `try` → **skip all catches** → run `finally` (if any) → continue with the code after it |
| An exception **is** thrown in `try`, and a catch type matches | `try` **stops immediately** (the remaining lines do not run) → jump to the **first catch whose type matches** → `finally` → continue with the code after it |
| An exception is thrown, but **no** catch matches | Run `finally` → the exception keeps passing to the caller; if nobody ever catches it, the program terminates abnormally |

**Termination model** (p.27): after Java handles an exception, it **does not go back to the line that failed (the throw point) and continue**, because that `try` block has already ended. The local variables declared inside `try` disappear with it and cannot be used in the catch or after the try.

Verified with [`DivideDemo.java`](../src/DivideDemo.java); results for three inputs:

| Input | Output | Explanation |
|---|---|---|
| `10 2` | `result = 5` → `finally: scanner closed` → `program continues` | No exception; the catch is skipped |
| `10 0` | `ArithmeticException: second number must not be 0` → `finally…` → `program continues` | The `result = …` line is skipped (termination model) |
| `10 abc` | `InputMismatchException: please enter integers` → `finally…` → `program continues` | `nextInt()` read something that is not an integer |

All three cases end by printing `program continues`, which shows that after the exception is handled, the program **does not stop**.

## 11. throw and throws (p.18–21)

### throw: throwing an exception yourself

When a method detects an unreasonable situation, it can actively throw an exception object:

```java
public static int div(int a, int b) {
    if (b == 0) {
        throw new ArithmeticException("second number must not be 0");
    }
    return a / b;
}
```

- `throw` is followed by **one exception object**, usually created with `new` right there.
- When execution reaches `throw`, the method ends immediately (the `return` after it does not run), and the exception is handed to the **nearest** catch whose type matches.
- The string in the parentheses is the message that `e.getMessage()` will return later.

### throws: declaring "I might throw this" in the method header

Handout p.21 writes `throw new Exception()` but does not handle it. `Exception` is **checked**, so writing it the way the handout does fails to compile:

```text
error: unreported exception Exception; must be caught or declared to be thrown
            throw new Exception();
            ^
```

The fix: add `throws` to the method header, and then the caller must handle it with try…catch:

```java
public static int div(int a, int b) throws Exception {
    if (b == 0)
        throw new Exception("divide by zero");
    else
        return a / b;
}
```

Or, like the example in these notes, throw the unchecked `ArithmeticException` instead, and then `throws` is not needed.

### Comparing the two words

| | `throw` | `throws` |
|---|---|---|
| Where it goes | **Inside** the method; it is a statement | In the method **header**, after the parameter parentheses |
| What follows it | One exception **object**: `throw new X(...)` | Exception **class names**, possibly several: `throws X, Y` |
| What it does | Throws right now | Tells the caller "this might throw; you must handle it" |

## 12. Multiple catch blocks (p.22–23)

One `try` can be followed by several catches, each handling a different type:

```java
try {
    ...
} catch (ArithmeticException e) {       // divide by 0
    ...
} catch (InputMismatchException e) {    // input is not an integer (java.util)
    ...
}
```

- They are compared from top to bottom, and **only the first catch whose type matches runs**.
- **Subclasses must come before superclasses**. If you write `catch (Exception e)` first, it grabs every exception first, the later catches can never run, and the compiler reports an error right away:

```text
error: exception ArithmeticException has already been caught
        } catch (ArithmeticException e) {
          ^
```

`FileCheck.java` uses this order too: `FileNotFoundException` (subclass) first, `IOException` (superclass) after.

> Extra (since Java 7): when the handling is the same, you can combine them: `catch (ArithmeticException | InputMismatchException e)`.

## 13. finally (p.24–25)

The problem: `try` opens a file, a database connection or a network connection, an exception happens partway through, the `close()` line is skipped, and the resource is never released.

`finally` solves this problem:

- It is optional, and goes **after the last catch**.
- **It runs whether or not an exception happens**.
- Use: holds the code that releases resources.

```java
try {
    // acquire the resource, use the resource
} catch (...) {
    ...
} finally {
    // release the resource: close()
}
```

`DivideDemo` puts `input.close()` in `finally`, and all three inputs printed `finally: scanner closed`.

> Extra: since Java 7 there is try-with-resources: `try (Scanner sc = new Scanner(System.in)) { ... }` closes automatically when it ends, so you do not need to write finally yourself.

## 14. Rethrowing (p.26)

After a catch receives an exception, if this layer **cannot fully handle it**, it can do part of the work first (for example, write a log) and then pass it to the outer layer with `throw e;`:

```java
static void update(AgeDemo p, int age) throws InvalidAgeException {
    try {
        p.setAge(age);
    } catch (InvalidAgeException e) {
        System.out.println("update(): logging -> " + e.getMessage());
        throw e;                       // let the outer try handle it
    }
}
```

The rethrown exception is caught by the **outer** try…catch (`main` in [`AgeDemo.java`](../src/AgeDemo.java)).

## 15. User-defined exceptions (p.28–30)

When Java's built-in exceptions are not enough, you can define your own:

- An exception is also an **object**, so a custom exception is just a class you write.
- Usually it extends `Exception`, which makes it a **checked** exception. If it extends `RuntimeException`, it is unchecked.
- Add a constructor that takes a `String` and pass the message to the superclass with `super(message)` (the `super(...)` from Week 6); after that, `getMessage()` can return it.

[`InvalidAgeException.java`](../src/InvalidAgeException.java), matching "the user enters a negative age" from p.5:

```java
public class InvalidAgeException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidAgeException(String message) {
        super(message);
    }
}
```

Using it ([`AgeDemo.java`](../src/AgeDemo.java)):

```java
public void setAge(int age) throws InvalidAgeException {   // checked → needs throws
    if (age < 0) {
        throw new InvalidAgeException("age cannot be negative: " + age);
    }
    this.age = age;
}
```

Output (set 27 first, then -5):

```text
age set to 27
update(): logging -> age cannot be negative: -5
main(): caught -> age cannot be negative: -5
```

The second line is `update()` logging and then rethrowing; the third line is `main` catching it.

> `serialVersionUID`: `Exception` implements `Serializable`, so without this line Eclipse shows a yellow warning. The program runs either way; adding this line makes the warning go away. It is fine that the class example does not have it.

## 16. Extra: common methods of an exception object

| Method | Returns | Example with `10 / 0` |
|---|---|---|
| `e.getMessage()` | Just the message | `/ by zero` |
| `e.toString()` (called automatically by `println(e)`) | Class name + message | `java.lang.ArithmeticException: / by zero` |
| `e.printStackTrace()` | Nothing; prints the full stack trace directly to `System.err` | Same format as in section 7 |

## 17. Syntax corrections to the handout

In several places the handout uses C++ terminology, or its code fails to compile if copied as is:

| Page | Handout | What actually happens in Java |
|---|---|---|
| Throughout | The quotation marks in the code are curly quotes `“ ”` | Pasting them causes a compile error (illegal character); use straight quotes `" "` |
| p.11 | `input.netxInt()` | Misspelled; it should be `nextInt()`. This is itself the compile-time error from section 1 |
| p.11–12 | The class is called `Test`, but the stack trace says `Example.div` | Two versions of the same example; just understand the format |
| p.16, p.23 | Call `div(...)` | The code on these two pages does not define `div`; reuse the method from p.11 |
| p.17–18 | You can throw a string, any type, or a conditional expression | This is C++. Java **can only throw `Throwable` objects**; `throw "error";` is a compile error: `String cannot be converted to Throwable` |
| p.18–20 | `DivideByZeroException`, `NotConnectedException` | The JDK has neither of these classes; you must define them yourself as in section 15. The built-in exception for division by 0 is `ArithmeticException` |
| p.19, p.26 | If not caught, terminate is called → abort | C++ terminology. Java passes the exception up to the caller; if it gets out of `main` and nobody has caught it, the stack trace is printed and the program ends |
| p.21 | `throw new Exception()` | Missing semicolon; `Exception` is checked, so `div` must add `throws Exception` (section 11) |
| p.29–30 | `if (...) throw x;` written directly in the class | Statements must be inside a method; `UserErr` is checked, so that method also needs `throws UserErr` |
| p.10 | Lists `IOError` among the unchecked exceptions | It belongs to `Error`, not `Exception` (though it is indeed not checked by the compiler) |
| p.10 | The `ClassCastException` example casts a Double to a String | If the variable's type is `Double`, it is a compile error right away; it only happens at run time with a variable of type `Object` (section 6) |
| p.14 | "Catch it before the error happens" | In reality the exception is caught **after it is thrown**, so that the program does not simply crash |

## 18. Checklist

- [ ] First identify the kind of error: does not compile → syntax; stops partway → runtime (exception); finishes but the answer is wrong → logic
- [ ] Read the stack trace: the top `at` line is where the error happened
- [ ] Calling a method that throws a **checked** exception → try…catch, or `throws` in the method header
- [ ] Multiple catches: **subclass first, superclass after**
- [ ] In `try`, the code after the failing line does not run, and execution does not go back after handling
- [ ] Put resources that must be released (Scanner, files) in `finally`, or use try-with-resources
- [ ] `throw` throws an object inside a method; `throws` declares classes in the header
- [ ] Custom exception: `extends Exception`, and the constructor calls `super(message)`
