# CSYE6200 OOP — Week 3: Java Array Notes

**English edition** · [繁體中文](array-key-points.zh-TW.md)

## 1. Class key point: Row Order Processing

> When the class said "Java is row order processing", it can be understood as: a two-dimensional array is usually processed row by row (row-major traversal) — you finish one row before moving on to the next row.

These notes use **rows** and **columns** (row = 列, column = 欄 in the Chinese edition).

```java
int[][] numbers = {
    {10, 20, 30},  // row 0
    {40, 50, 60}   // row 1
};
```

The order of a row-by-row traversal:

```text
10 → 20 → 30 → 40 → 50 → 60
```

**Precise note: this is a common way of traversing, not an execution order that Java enforces.** A program can be written to traverse in other orders. A Java two-dimensional array is actually "an array of arrays", so you cannot conclude from this that all rows are necessarily stored contiguously in a single block of memory.

## 2. Array Variable, Reference and Heap

```java
int[] numbers = new int[3];
```

This line does two things:

1. Declares `numbers`: a variable that can store an `int[]` reference.
2. Creates an array object of length 3 and stores its reference in `numbers`.

```text
the reference in numbers ──→ array object [0, 0, 0]
```

- `int` is a primitive type.
- `int[]` is a reference type; the array itself is an object.
- A reference can be understood as "a value used to find the object"; it is not the array's contents.
- Java does not let ordinary programs treat a reference as a physical memory address that can be freely manipulated.

In the JVM's conceptual model:

| Item | Where it lives / what it belongs to |
|---|---|
| Local variables and parameters in a method | The stack frame of that method call |
| Array objects and their elements | Heap |
| An object's instance fields | Belong to that object; conceptually they sit in the heap together with the object |

**Don't memorize it as "primitives are always on the stack, references are always on the heap".** You need to look at whether the variable is a local variable, an object field, or an array element. A real JVM may also perform optimizations; the above is a model for understanding how programs behave.

### Declaring is not the same as creating an array

```java
int[] a;          // only declares a local variable; not yet assigned, cannot be read directly
int[] b = null;   // assigned, but does not point to any array
int[] c = new int[3]; // creates an array; all elements default to 0
int[] d = new int[0]; // a legal empty array; length is 0
```

The elements of a new array have default values: `0` for integers, `0.0` for floating-point numbers, `false` for `boolean`, `'\u0000'` for `char`, and `null` for references. Local variables, on the other hand, must definitely be assigned before they are used.

## 3. Accessing Array Elements

Syntax:

```java
arrayName[index]
```

**An index is a position number that starts at 0; it is not an object reference, and it is not the value you are searching for.**

```java
int[] numbers = {10, 20, 30};
```

| Index | Element | How to access |
|---|---:|---|
| 0 | 10 | `numbers[0]` |
| 1 | 20 | `numbers[1]` |
| 2 | 30 | `numbers[2]` |

### Reading and writing

```java
int x = numbers[1]; // read: copies the integer value 20 into x
x = 99;             // only changes x; numbers[1] is still 20

numbers[1] = 99;     // write: directly modifies element 1 of the array
```

`numbers[1]` is an expression; it does not print anything automatically. To display the contents you need something like:

```java
System.out.println(numbers[1]);
```

### Length and the last index

```java
numbers.length      // number of elements: 3
numbers.length - 1  // index of the last element: 2
```

Arrays use `.length`, with no parentheses. The last element of a non-empty array is `numbers[numbers.length - 1]`.

## 4. Out of Bounds

A legal index must satisfy:

```java
0 <= index && index < numbers.length
```

For an array of length 3, the legal indexes are `0, 1, 2`.

```java
int[] numbers = {10, 20, 30};

System.out.println(numbers[2]); // legal: 30
// System.out.println(numbers[3]);  // out of bounds at run time
// System.out.println(numbers[-1]); // out of bounds at run time
```

When an access is out of bounds, Java stops that access and throws an `ArrayIndexOutOfBoundsException`; it never lets the program read data outside the array. Both reads and writes have this restriction, and an out-of-bounds write does not automatically enlarge the array either.

### How "Restricted" and "Fail" relate

| Term | What level it describes |
|---|---|
| Restricted | The rule: only indexes in the legal range can be accessed |
| Fail | The result: this out-of-bounds access did not succeed |
| Exception | Java's mechanism for telling the program that this operation failed |

**It is not either/or: because there is a bounds restriction, an out-of-bounds access fails.**

### Why is it usually checked at run time?

```java
static int getElement(int[] array, int index) {
    return array[index];
}
```

At compile time the types of `array` and `index` are known, but not necessarily how long an array the caller will pass in or how large the index will be, so the check has to happen at run time. Even a constant index that is obviously out of bounds is not necessarily a Java compile error; development tools may issue a separate warning.

### For loops use `<`, not `<=`

```java
for (int i = 0; i < numbers.length; i++) {
    System.out.println(numbers[i]);
}
```

If you write `i <= numbers.length`, the loop may reach `numbers[numbers.length]`, which does not exist.

## 5. Two-dimensional arrays: what is the Number of Rows?

```java
int[][] numbers = {
    {10, 20, 30},
    {40, 50, 60}
};
```

| | column 0 | column 1 | column 2 |
|---|---:|---:|---:|
| row 0 | 10 | 20 | 30 |
| row 1 | 40 | 50 | 60 |

Access syntax:

```java
numbers[row][column]

numbers[1][2] // row 1, column 2; the value is 60
```

| Expression | Meaning | Value in this example |
|---|---|---:|
| `numbers.length` | Number of rows: how many elements the outer array has, i.e. how many rows | 2 |
| `numbers[row].length` | How many elements the current row has | 3 |

**`numbers.length` is not the total number of integer elements.** This example has 6 integers in total, but the length of the outer array is 2.

A Java two-dimensional array is an array of arrays:

```text
numbers → outer array
             [0] → int[] [10, 20, 30]
             [1] → int[] [40, 50, 60]
```

`numbers[1][2]` can be broken down as: first use `numbers[1]` to find the array for row 1, then take element 2 of that array.

## 6. Accessing row by row with a nested for loop

```java
for (int row = 0; row < numbers.length; row++) {
    for (int col = 0; col < numbers[row].length; col++) {
        System.out.print(numbers[row][col] + " ");
    }
    System.out.println(); // move to a new line after the current row is printed
}
```

- **The outer loop picks the row**: the number of rows decides how many times it runs.
- **The inner loop picks the column**: it visits every element of the current row.
- Each time we move to the next row, the inner `col` starts again from `0`.

Execution order:

```text
row = 0: numbers[0][0] → numbers[0][1] → numbers[0][2]
row = 1: numbers[1][0] → numbers[1][1] → numbers[1][2]
```

Output:

```text
10 20 30
40 50 60
```

### Rows can have different lengths: Jagged Array

```java
int[][] numbers = {
    {10, 20, 30},
    {40, 50}
};

// numbers.length    == 2
// numbers[0].length == 3
// numbers[1].length == 2
```

So the inner loop should use **`numbers[row].length`**; don't just assume every row is as long as row 0.

The loop above assumes that neither the outer array nor any row is `null`. For example, `new int[2][]` only creates the outer array, and both rows start out as `null`; you must create each row before you can access its `.length` or elements.

## 7. Copying a reference vs copying an array

```java
int[] a = {10, 20};
int[] b = a;

b[0] = 99;
System.out.println(a[0]); // 99
```

`b = a` copies the reference; it does not create a new array:

```text
a ──┐
    ├──→ the same array [99, 20]
b ──┘
```

Reassignment is different, though:

```java
b = new int[]{7, 8};
// b now points to a new array; a still points to [99, 20]
```

| Operation | What does it change? |
|---|---|
| `b[0] = 99` | An element inside the array object |
| `b = new int[]{7, 8}` | The reference stored in variable b |

An array's length is fixed once it is created; a variable can be changed to point to a different array with a different length.

### Clone and shallow copy

```java
int[] a = {1, 2};
int[] b = a.clone();
b[0] = 99;
System.out.println(a[0]); // 1
```

`clone()` creates a new array and copies the elements. But the elements of an object array are references, so after copying they may still share objects; calling `clone()` directly on a two-dimensional array also copies only the outer array, and the rows are still shared.

If "heap saving" means saving memory, the point is that sharing the same object avoids creating a copy; **being stored in the heap does not by itself save memory**, and sharing mutable data also brings the problem of changes affecting each other.

## 8. Interview extra: Pass-by-Value and Final

### Java is always Pass-by-Value

A method parameter receives a copy of the value. When an array is passed in, what gets copied is the reference value.

```java
static void change(int n, int[] arr) {
    n = 99;                 // only changes parameter n
    arr[0] = 88;            // modifies the shared array
    arr = new int[]{7, 8};  // only changes the reference in parameter arr
}
```

Caller:

```java
int x = 10;
int[] a = {1, 2};
change(x, a);

System.out.println(x);    // 10
System.out.println(a[0]); // 88
```

Being able to modify a shared object does not mean it is pass-by-reference.

### Final does not mean the array elements cannot change

```java
final int[] a = {1, 2};
a[0] = 99;         // legal: modifies an element
// a = new int[3]; // compile error: cannot reassign a
```

## Quick review

- One-dimensional array: `array[index]`.
- Two-dimensional array: `array[row][column]`.
- Indexes start at `0` and must be less than the `length` of the corresponding array.
- `array.length` is the number of rows of a two-dimensional array; `array[row].length` is the number of elements in the current row.
- Row order processing: usually finish one row before moving to the next; the order is decided by the program.
- Out-of-bounds access is blocked by Java, and the failed operation is reported with an exception.
- An array variable stores a reference; `b = a` does not copy the whole array.
- When analyzing a program, first ask: **does this line change the reference, or the contents of the object?**

## References

- Course slides: *Arrays, Strings, and I/O*, pages 4–15.
- [Java Language Specification: Arrays](https://docs.oracle.com/javase/specs/jls/se25/html/jls-10.html)
- [Java Language Specification: Variables](https://docs.oracle.com/javase/specs/jls/se25/html/jls-4.html#jls-4.12)
- [Java Language Specification: Formal Parameters](https://docs.oracle.com/javase/specs/jls/se25/html/jls-8.html#jls-8.4.1)
- [JVM Specification: Run-Time Data Areas](https://docs.oracle.com/javase/specs/jvms/se25/html/jvms-2.html#jvms-2.5)
