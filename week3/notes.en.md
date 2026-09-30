# CSYE6200 OOP — Week 3: Arrays, Strings, and I/O

**English edition** · [繁體中文](notes.zh-TW.md) · [Guide](README.md)

These notes follow the physical PDF page order (1–28) of `Arrays_String_InputOutput.pdf`. Original slide titles are retained. Examples, classroom discussion, and corrections are added as study material; this is not a verbatim lecture transcript.

Unless a complete class is shown, Java snippets normally belong inside `main`. Run independent examples with repeated variable names separately.

<a id="slide-01"></a>

## Slide 01 | Arrays, Strings, and I/O

### Topics and learning goals

- Original slides by Tessema Mengistu, Ph.D.
- Arrays: store multiple values in indexed objects with a fixed length.
- Strings: understand text, references, content comparison, and immutability.
- Input/output: read data, process it, and display results.
- Learning sequence: **what variables hold → how to access values → how to iterate → how to handle text and I/O.**

<a id="slide-02"></a>

## Slide 02 | Outline

### Study in slide order

| Pages | Topic | Learning outcome |
|---|---|---|
| 3–16 | Arrays | Declare, initialize, access, and traverse one-dimensional and jagged arrays |
| 17–22 | Strings | Distinguish references from content; use methods and escape sequences |
| 23–27 | Input/output | Use standard streams, formatted output, and Scanner |
| 28 | References | Locate the slide bibliography and further reading |

<a id="slide-03"></a>

## Slide 03 | What are Arrays?

### An indexed collection of elements

- Each array has a component type and a length that stays fixed after creation.
- Elements of `int[]` are `int` values; elements of `char[]` are `char` values.
- An array itself is an object: `int[]` is a reference type even though `int` is primitive.
- Reference arrays may hold references to compatible object types or `null`. Their elements do not all need the same runtime class.

### Example

```java
int[] scores = {80, 90, 100};
char[] vowels = {'a', 'e', 'i', 'o', 'u'};
String[] names = {"Lu", "Qian"};
```

**Clarification:** A `String[]` stores String references in its elements, rather than embedding complete String objects in the slots.

<a id="slide-04"></a>

## Slide 04 | Java Arrays

### Declaration and object creation are separate operations

```java
int[] numbers;          // Declare a reference variable.
numbers = new int[10];  // Create an array and assign its reference.

int[] scores = new int[3]; // Combined form.
```

- Both `int[] numbers` and `int numbers[]` are valid; the first form keeps the type together.
- Declaring a local variable does not create an array. It cannot be read before it is definitely assigned.
- `new int[10]` creates an array with ten `int` elements.

### Classroom supplement: references, stack, and heap

```text
Local variable numbers → reference → int[] array on the heap
```

In the JVM conceptual model, local variables and parameters belong to a method's stack frame; objects and arrays are allocated from the heap. A reference stored in an instance field belongs to that object. Do not memorize “all primitives are on the stack” or “all references are on the heap.” Implementations may optimize execution; this diagram is not a physical layout guarantee.

### Classroom supplement: sharing and memory use

```java
int[] a = new int[1000];
int[] b = a;         // Copy a reference; still one array.
int[] c = a.clone(); // Create another array and copy elements.
```

Sharing can avoid data copies, but changes to shared mutable objects affect other users. Being stored on the heap does not inherently save memory. Setting one reference to `null` does not immediately delete an object; unreachable objects may later be garbage-collected.

<a id="slide-05"></a>

## Slide 05 | Java Arrays

### Explicit initialization and default values

```java
int[] nums = {30, 50, -23, 16};
int[] zeros = new int[3]; // [0, 0, 0]
```

| Element type | Default value in a new array |
|---|---|
| `byte`, `short`, `int`, `long` | `0` |
| `float`, `double` | `0.0` |
| `boolean` | `false` |
| `char` | `'\u0000'`, not the character `'0'` |
| Reference types, including `String` | `null`, not the empty string `""` |

### Local variables are different from array elements

```java
int[] a;
// System.out.println(a); // Compile-time error: not initialized.

String[] names = new String[2];
System.out.println(names[0]); // null
```

Fields and new array elements have default values. Local variables must be definitely assigned before use. Creating an object-reference array does not create the objects its elements could reference.

<a id="slide-06"></a>

## Slide 06 | Array Indexes

### Indexes start at zero

For an array of length `n`, a valid index satisfies `0 <= index && index < n`. The last index of a nonempty array is `n - 1`.

```java
int[] a = {10, 20, 30};
// Valid indexes: 0, 1, 2.
// Both a[3] and a[-1] are out of bounds.
```

### Terminology correction: index versus object reference

The slide calls a position an “integer reference.” Read this as **index**, not object reference. An object reference identifies the array object; an index selects an element within that array.

<a id="slide-07"></a>

## Slide 07 | Array Visualization

### Read each part of an array declaration

```java
int[] primes = new int[10];
```

| Part | Meaning |
|---|---|
| `int[]` | Variable type: reference to an int array |
| `primes` | Variable name |
| `new int[10]` | Create an array with ten elements |
| `primes[0]` through `primes[9]` | The ten accessible elements |

```text
primes → [0][0][0][0][0][0][0][0][0][0]
index     0  1  2  3  4  5  6  7  8  9
```

**Correction:** The array object's type is `int[]`; its element type is `int`. Naming the variable `primes` does not automatically populate it with prime numbers.

<a id="slide-08"></a>

## Slide 08 | Accessing Array Elements

### Retrieve an element with `array[index]`

```java
String[] names = {"Lu", "Qian", "John"};
String firstName = names[0];
System.out.println(firstName); // Lu

int i = 2;
System.out.println(names[i]); // John
```

`names[0]` is an expression that retrieves the element at index zero. It does not print by itself. Reading a `String[]` element retrieves a reference; reading an `int[]` element retrieves an integer value.

### Classroom supplement: out of bounds, restricted, and failed

- Java restricts array access to valid indexes.
- An out-of-bounds access fails and throws `ArrayIndexOutOfBoundsException`.
- Java prevents the invalid read or write; it does not first return data from outside the array.
- Lengths and indexes may only become known at runtime, so bounds are checked during execution. Even an obviously invalid constant index is not necessarily a compile-time error.
- Accessing a `null` array causes `NullPointerException`, a different problem.

```java
int[] a = {10, 20, 30};
// int x = a[3]; // Throws ArrayIndexOutOfBoundsException at runtime.

for (int i = 0; i < a.length; i++) {
    System.out.println(a[i]);
}
```

Use `< a.length`, not `<= a.length`. “Restricted” describes the rule; “failed” describes the outcome of violating it.

<a id="slide-09"></a>

## Slide 09 | Assigning values to Array Elements

### Write a compatible value to a selected element

```java
double[] prices = new double[3];
prices[0] = 6.75;
prices[1] = 80.43;
prices[2] = 10.02;

String[] names = new String[2];
names[0] = "Lu";
names[1] = "Qian";
```

| Statement | Operation |
|---|---|
| `double x = prices[0];` | Read an element value into x |
| `prices[0] = 9.99;` | Change the selected element |

Writes also require a valid index. They do not automatically enlarge the array.

<a id="slide-10"></a>

## Slide 10 | Length of array

### Fixed length and the `.length` field

```java
String[] names = {"Lu", "Qian", "Emina", "Jamal", "John"};
System.out.println(names.length); // 5
System.out.println(names[names.length - 1]); // John
```

- Arrays use `array.length` without parentheses; String uses `text.length()`.
- A zero-length array is valid but has no accessible elements.
- An array object's length is fixed, but a variable may reference a different array.

```java
int[] a = new int[3];
a = new int[10]; // A new array, not an enlarged original array.
```

<a id="slide-11"></a>

## Slide 11 | Modifying Array Elements

### Mutating an element versus reassigning a reference

```java
String[] names = {"David", "Qian"};
names[0] = "Beki";
System.out.println(names[0]); // Beki
```

This replaces the reference in `names[0]`; it does not modify the original String `"David"`. The slide uses David although the preceding slide starts with another name; the example above is self-contained.

### Classroom supplement: aliasing, clone, and final

```java
int[] a = {1, 2};
int[] b = a;
b[0] = 99;
System.out.println(a[0]); // 99: one shared array.

int[] c = a.clone();
c[0] = 7;
System.out.println(a[0]); // 99: c is a different array.

final int[] fixed = {1, 2};
fixed[0] = 8;         // Allowed: modify an element.
// fixed = new int[2]; // Not allowed: reassign a final variable.
```

Cloning reference arrays and multidimensional arrays is shallow: referenced objects or row arrays may remain shared. `==` compares array identity. Use `java.util.Arrays.equals` for element comparison or `deepEquals` for nested contents.

### Interview supplement: Java is pass-by-value

A parameter receives a copy of the argument's value. An array parameter receives a copy of a reference value. Thus `arr[0] = 88` may change the shared array, but `arr = new int[3]` only reassigns the parameter, not the caller's variable.

<a id="slide-12"></a>

## Slide 12 | Multi-Dimensional Arrays

### A two-dimensional array is an array of arrays

```java
int[][] grid = {{8, 4}, {9, 7}, {3, 6}};
System.out.println(grid[2][0]); // 3
```

```text
grid → outer array
         [0] → int[] [8, 4]
         [1] → int[] [9, 7]
         [2] → int[] [3, 6]
```

`grid[row][column]` first selects a row array, then selects an element within it.

### Number of rows versus elements in a row

| Expression | Meaning | Value here |
|---|---|---:|
| `grid.length` | Outer-array length: number of rows | 3 |
| `grid[row].length` | Elements in the current row | 2 |

The outer length is not the total number of integers. This example has six integers but an outer length of three.

<a id="slide-13"></a>

## Slide 13 | Multi-Dimensional Array

### Create a rectangular array and use two indexes

```java
double[][] heights = new double[20][55];
heights[11][23] = 12.5;
```

There are twenty rows with fifty-five elements each. Row indexes are 0–19 and column indexes are 0–54. This creates one outer array and twenty row arrays.

### Classroom supplement: row order processing

**Row-major traversal usually means completing one row before moving to the next.** This order comes from the loop structure. Java does not mandate it or guarantee that all row arrays occupy one contiguous physical memory block.

```java
int[][] table = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
for (int i = 0; i < table.length; i++) {
    for (int j = 0; j < table[i].length; j++) {
        System.out.print(table[i][j] + " ");
    }
    System.out.println();
}
```

The outer loop selects a row; the inner loop selects its elements. Each is a basic/traditional for loop. Together they are nested for loops.

<a id="slide-14"></a>

## Slide 14 | Enhanced for Loop

### Retrieve elements directly in order

```java
char[] vowels = {'a', 'e', 'i', 'o', 'u'};
for (char item : vowels) {
    System.out.println(item);
}
```

Read this as “for each char element in vowels.” An enhanced for loop, also called for-each, removes the need to update an index yourself.

### Classroom exercise: rewrite the nested loops

```java
public class Array {
    public static void main(String[] args) {
        int[][] table = {
            {1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}
        };
        for (int[] row : table) {
            for (int item : row) {
                System.out.print(item + " ");
            }
            System.out.println();
        }
    }
}
```

The outer element type is `int[]`; the inner element type is `int`. Output:

```text
1 2 3
4 5 6
7 8 9
10 11 12
```

### The loop variable receives a copy of the element value

Assigning `item = 99` inside `for (int item : array)` does not modify array elements. A copied object reference can be used to mutate the shared object, but reassigning the loop variable does not replace the array element. Use a basic for loop when you need indexes or direct element replacement.

<a id="slide-15"></a>

## Slide 15 | Jagged Arrays

### Rows may have different lengths

```java
int[][] table = {{1, 2, 3}, {4}, {5, 6, 7, 8}};
// Row lengths: 3, 1, 4.
```

### What “omit the column” means

Leave the second dimension's size unspecified, retain its brackets, and create the rows separately:

```java
int[][] table = new int[3][];
table[0] = new int[3];
table[1] = new int[1];
table[2] = new int[4];
```

Immediately after `new int[3][]`, all row references are `null`. Once the rows above have been allocated, their integer elements contain zero.

Traverse with `table[i].length` in the inner basic loop, or use nested enhanced for loops. These forms assume that the rows are non-null.

### Slide syntax correction

The slide's final statement incorrectly combines explicit size allocation with an initializer. A valid form is:

```java
int[][] arr = {{1, 2, 3}, {4, 5}};
```

<a id="slide-16"></a>

## Slide 16 | Exercise

### Print a jagged array using each row's own length

```java
int[][] num = {
    {1, 2, 3}, {4, 5}, {6, 7, 8, 9}, {10}, {11, 12, 13, 14, 15}
};

for (int[] row : num) {
    for (int value : row) {
        System.out.print(value + " ");
    }
    System.out.println();
}
```

Output, ignoring a trailing space on each line:

```text
1 2 3
4 5
6 7 8 9
10
11 12 13 14 15
```

### Exercise checkpoints

- The outer length is five; row lengths are 3, 2, 4, 1, and 5.
- Place the newline after the inner loop and inside the outer loop.
- A basic-loop solution must use `num[i].length` as its inner limit.

<a id="slide-17"></a>

## Slide 17 | Strings

### String literals and new String objects

`String` is a reference type. String literals use double quotes; a single `char` literal uses single quotes.

```java
String fName = "John";
String name = "John";
String lName = new String("John");
String mName = new String("John");
```

### Whiteboard supplement: the string pool and identity

Reconstructed from the classroom photographs: identical literals can share a pooled object, whereas each `new String(...)` creates a separate String object.

```text
fName ─┐
       ├→ pooled "John"
name ──┘
lName ──→ another "John" object
mName ──→ another "John" object
```

```java
System.out.println(fName == name);       // true
System.out.println(lName == mName);      // false
System.out.println(fName == lName);      // false
System.out.println(lName.equals(mName)); // true
```

**`==` compares identity; String's `.equals()` compares text content.** Pooled strings are still objects; do not interpret the string pool as strings existing outside the heap.

<a id="slide-18"></a>

## Slide 18 | Strings

### Immutable content, reassignable references

```java
String fullName = "John";
String original = fullName;
fullName = fullName + " Smith";

System.out.println(fullName); // John Smith
System.out.println(original); // John
```

The variable `fullName` references the concatenation result. The original `"John"` object has not changed. This illustrates the arrows and “immutable” label in the first whiteboard photograph; an empty string versus a space in that photo does not change the principle.

### Numeric addition versus string concatenation

```java
System.out.println(1 + 2 + " apples");   // 3 apples
System.out.println("apples: " + 1 + 2);  // apples: 12
System.out.println("apples: " + (1 + 2)); // apples: 3
```

Operand types determine whether `+` performs numeric addition or string concatenation. Parentheses change grouping. Do not infer that every written `+` must create a separate object; compilers can optimize constants and concatenation.

### Keep the returned result

```java
String name = "John";
name.toUpperCase();
System.out.println(name); // John
name = name.toUpperCase();
System.out.println(name); // JOHN
```

<a id="slide-19"></a>

## Slide 19 | String Class

### Read and transform text

`String` belongs to `java.lang`, which does not need an explicit import in ordinary Java source.

| Method | Purpose | Example |
|---|---|---|
| `charAt(index)` | Read a char at an index | `"Java".charAt(0)` → `'J'` |
| `length()` | Count UTF-16 code units | `"Java".length()` → `4` |
| `toUpperCase()` | Return uppercase text | `"Java".toUpperCase()` → `"JAVA"` |
| `toLowerCase()` | Return lowercase text | `"Java".toLowerCase()` → `"java"` |
| `trim()` | Remove end characters at or below U+0020 | `"  Java  ".trim()` → `"Java"` |
| `substring(beginIndex)` | Return the suffix from an index | `"Java".substring(2)` → `"va"` |

### Common misunderstandings

- Use `charAt(...)`, not `text[index]`.
- A valid `charAt` index is less than `length()`, but `substring(length())` is valid and returns an empty string.
- Some characters, including many emoji, occupy two UTF-16 code units. Length is not necessarily the visible character count.
- Transformations do not mutate the original String; case conversion can also depend on locale.

Official reference: [String API](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/String.html).

<a id="slide-20"></a>

## Slide 20 | String Class

### Search, compare, and split

| Method | Key idea |
|---|---|
| `indexOf(...)` | First matching index, or `-1` if absent |
| `lastIndexOf(...)` | Last matching index, or `-1` if absent |
| `concat(...)` | Return concatenated content without mutating the original |
| `equals(...)` | Compare content |
| `equalsIgnoreCase(...)` | Compare content ignoring case |
| `split(regex)` | Split into a String array using a regular expression |
| `replace(...)` | Return replacement results; distinguish from replaceAll |
| `compareTo(...)` | Lexicographic comparison: negative, zero, or positive |
| `compareToIgnoreCase(...)` | Order comparison ignoring case |

```java
String text = "banana";
System.out.println(text.indexOf('a'));     // 1
System.out.println(text.lastIndexOf('a')); // 5
System.out.println(text.indexOf('z'));     // -1
System.out.println(text.replace('a', 'o')); // bonono
System.out.println(text); // banana
```

**Correction:** The slide's `equalgnoreCase` should be `equalsIgnoreCase`. Check the sign of `compareTo` results; they need not be exactly `-1` or `1`. Do not use a “not found” result of `-1` directly as an index.

<a id="slide-21"></a>

## Slide 21 | Escape Sequences

### Represent special characters with a backslash

| Sequence | Meaning |
|---|---|
| `\b` | Backspace control character |
| `\t` | Tab |
| `\n` | Newline |
| `\r` | Carriage return |
| `\"` | Double quote |
| `\'` | Single quote |
| `\\` | One backslash |

### Example

```java
System.out.println("She said \"Hello\".");
System.out.println("C:\\notes\\week3");
```

Output:

```text
She said "Hello".
C:\notes\week3
```

<a id="slide-22"></a>

## Slide 22 | Escape Sequence: Example

### Combine lines, indentation, and quotation marks

The slide combines `\n`, `\t`, `\"`, and concatenation. This shorter original example demonstrates the same ideas:

```java
System.out.println("Name:\n\t\"John\"\nCourse:\n\tJava");
```

Illustrative output; tab width depends on the display environment:

```text
Name:
    "John"
Course:
    Java
```

### Clarification: backspace does not edit a String

The slide also uses `\b`. It is a control character, not an instruction to remove a preceding character from a Java String. A terminal may move the cursor; an IDE console may behave differently. Do not rely on it to correct text.

<a id="slide-23"></a>

## Slide 23 | Input/Output

### Three standard streams

| Name | Role | Typical use |
|---|---|---|
| `System.in` | Standard input | Often keyboard input; can be redirected |
| `System.out` | Standard output | Normal results and prompts |
| `System.err` | Standard error | Diagnostic and error messages |

A stream provides a sequential flow of input or output data.

### Slide clarification

Java provides byte streams and character-oriented Reader/Writer APIs; not every I/O API should be described as byte-only. Streams are not automatically fast: performance depends on buffering, devices, and usage. These I/O streams are also different from `java.util.stream.Stream` used in data processing.

<a id="slide-24"></a>

## Slide 24 | Printing

### Print, println, and printf

| Method | Behavior |
|---|---|
| `print(...)` | Output without adding a newline |
| `println(...)` | Output followed by a newline; no argument prints only a newline |
| `printf(format, args...)` | Formatted output; no automatic newline |

```java
System.out.print("Java ");
System.out.println("OOP");
System.out.printf(java.util.Locale.US, "Name: %s, score: %d, price: %.2f%n",
        "John", 95, 12.5);
```

```text
Java OOP
Name: John, score: 95, price: 12.50
```

`%s` formats text, `%d` an integer, `%.2f` two decimal places, and `%n` a platform line separator. The example specifies US locale to make decimal formatting predictable. Incompatible formats and argument types can cause formatting exceptions.

Official reference: [PrintStream API](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/io/PrintStream.html).

<a id="slide-25"></a>

## Slide 25 | Reading Input

### Scanner parses input into usable values

- Scanner can read standard input, strings, files, and other sources.
- Its default token delimiters are whitespace, including spaces, tabs, and newlines.
- Console reads may wait for the user to supply data.

```java
java.util.Scanner scan = new java.util.Scanner("10 20");
int first = scan.nextInt();
int second = scan.nextInt();
System.out.println(first + second); // 30
scan.close();
```

This string-based example demonstrates Scanner without requiring keyboard input.

<a id="slide-26"></a>

## Slide 26 | Reading Input

### Create a Scanner and read a complete line

Complete program:

```java
import java.util.Scanner;

public class ReadName {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your full name: ");
        String answer = scan.nextLine();
        System.out.println("Hello, " + answer);
        // Manage the shared System.in lifecycle at application level.
    }
}
```

`nextLine()` returns the remaining text on the current line without its line separator. It preserves the space in an input such as `John Smith`.

### Resource lifecycle supplement

Closing a Scanner that wraps `System.in` also closes that underlying input. A helper should not close it prematurely if other code needs it; manage ownership centrally. Resources owned by the program, such as opened files, should be closed appropriately.

<a id="slide-27"></a>

## Slide 27 | Reading Input

### Import and common methods

```java
import java.util.Scanner;
```

| Method | Reads or returns |
|---|---|
| `next()` | The next token |
| `nextLine()` | The remaining text on the current line |
| `nextInt()` | The next integer token |
| `nextFloat()` | The next float token |
| `nextDouble()` | The next double token |

### Common trap: nextLine after nextInt

```java
java.util.Scanner scan = new java.util.Scanner("20\nJohn Smith\n");
int age = scan.nextInt();
scan.nextLine(); // Consume the remainder of the age line and its separator.
String name = scan.nextLine();
System.out.println(age + ": " + name); // 20: John Smith
scan.close();
```

After `nextInt()` consumes a numeric token, the remaining line content and separator may still be unread. Omitting the intermediate `nextLine()` above makes the following line read return an empty string. This approach assumes the next field starts on the next line.

Invalid numeric input can cause `InputMismatchException`. Methods such as `hasNextInt()` can help validate input; also account for exhausted input.

Official reference: [Scanner API](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Scanner.html).

<a id="slide-28"></a>

## Slide 28 | References

### Bibliography listed on the slide

Herbert Schildt and Danny Coward, *Java: The Complete Reference*, 13th edition, McGraw-Hill, 2024. The slide lists Chapters 3 and 13. This bibliography is reproduced from the slide; the book itself was not separately consulted for these notes.

### Official further reading

- [JLS Chapter 10: Arrays](https://docs.oracle.com/javase/specs/jls/se25/html/jls-10.html)
- [JLS 4.12: Variables](https://docs.oracle.com/javase/specs/jls/se25/html/jls-4.html#jls-4.12)
- [JLS 8.4.1: Formal Parameters](https://docs.oracle.com/javase/specs/jls/se25/html/jls-8.html#jls-8.4.1)
- [JLS 14.14.2: Enhanced for](https://docs.oracle.com/javase/specs/jls/se25/html/jls-14.html#jls-14.14.2)
- [JVMS 2.5: Run-Time Data Areas](https://docs.oracle.com/javase/specs/jvms/se25/html/jvms-2.html#jvms-2.5)

### Review questions

1. What do `a.length` and `a.length - 1` mean?
2. Why should the inner loop use `table[i].length`?
3. Why do `int[] row` and `int item` have different types?
4. How do `b = a`, `b[0] = 99`, and `b = a.clone()` differ?
5. Why can `==` return false for Strings with identical content?
6. Why does `name.toUpperCase()` not mutate the referenced String?
7. What input may remain after `nextInt()` before `nextLine()`?
