# CSYE6200 OOP — Week 3：Arrays、Strings 與 I/O

**繁體中文版** · [English](notes.en.md) · [導覽](../README.md)

依 `Arrays_String_InputOutput.pdf` 的實際 PDF 頁序（1–28）整理。保留原投影片標題，加入學習重點、程式範例與課堂討論；補充與更正會另外標明。本筆記是學習整理，並非老師逐字講稿。

本筆記使用 **row = 列、column = 欄**。除完整 class 範例外，Java 片段通常放在 `main` 方法內；同名變數的不同範例應分開執行。

<a id="slide-01"></a>

## 第 01 頁｜Arrays, Strings, and I/O

### 本週主題與學習目標

- 原講義作者：Tessema Mengistu, Ph.D.
- Arrays：用有索引、固定長度的物件儲存多個值。
- Strings：理解文字、reference、內容比較與不可變性。
- Input/Output：讀取輸入、處理資料，再輸出結果。
- 學習主線：**變數保存什麼 → 如何存取資料 → 如何走訪 → 如何處理文字與輸入輸出。**

<a id="slide-02"></a>

## 第 02 頁｜Outline

### 依投影片順序複習

| 範圍 | 主題 | 要能做到的事 |
|---|---|---|
| 3–16 頁 | Arrays | 宣告、初始化、讀寫、走訪一維與不規則二維陣列 |
| 17–22 頁 | Strings | 分辨 reference 與內容、使用方法與跳脫字元 |
| 23–27 頁 | Input/Output | 使用標準串流、格式化輸出與 Scanner |
| 28 頁 | References | 找到講義來源與延伸閱讀 |

<a id="slide-03"></a>

## 第 03 頁｜What are Arrays?

### 陣列是有索引的一組元素

- 每個陣列都有固定的元素型別與建立後不變的長度。
- `int[]` 的元素是 `int`；`char[]` 的元素是 `char`。
- 陣列本身是物件，因此 `int[]` 是參考型別，即使其元素 `int` 是基本型別。
- 參考型別陣列可以保存相容型別物件的 reference，或 `null`；不是所有元素都必須有同一個執行期類別。

### 範例

```java
int[] scores = {80, 90, 100};
char[] vowels = {'a', 'e', 'i', 'o', 'u'};
String[] names = {"Lu", "Qian"};
```

**補充：** `String[]` 的元素保存 String reference，不是把整個 String 物件直接塞進每個元素格子。

<a id="slide-04"></a>

## 第 04 頁｜Java Arrays

### 宣告變數與建立物件是兩件事

```java
int[] numbers;          // 宣告參考變數
numbers = new int[10];  // 建立陣列，將 reference 指派給 numbers

int[] scores = new int[3]; // 也可以合寫
```

- `int[] numbers` 和 `int numbers[]` 都合法；通常採用前者，型別較清楚。
- 只宣告區域變數不會建立陣列，也不能在確定賦值前讀取它。
- `new int[10]` 建立 10 個 `int` 元素的陣列。

### 課堂補充：Reference、Stack 與 Heap

```text
方法內的 numbers 變數 → reference → heap 中的 int[] 陣列
```

JVM 的概念模型中，方法的區域變數與參數位於對應的 stack frame，物件與陣列配置在 heap。若 reference 是物件欄位，它也隸屬於該物件。因此不能背成「primitive 都在 stack、reference 都在 heap」。實際 JVM 可以進行最佳化，示意圖不是實體記憶體布局保證。

### 課堂補充：Heap saving 與共用物件

```java
int[] a = new int[1000];
int[] b = a;         // 複製 reference，仍只有一個陣列
int[] c = a.clone(); // 建立另一個陣列並複製元素
```

共用 reference 可以避免複製資料，但共用可變物件會互相影響。「存放在 heap」本身不等於節省記憶體。把一個 reference 設成 `null` 也不會立即刪除物件；當物件不再可達時，才可能被垃圾回收。

<a id="slide-05"></a>

## 第 05 頁｜Java Arrays

### 明確初始化與預設值

```java
int[] nums = {30, 50, -23, 16};
int[] zeros = new int[3]; // [0, 0, 0]
```

| 元素型別 | 新陣列元素的預設值 |
|---|---|
| `byte`、`short`、`int`、`long` | `0` |
| `float`、`double` | `0.0` |
| `boolean` | `false` |
| `char` | `'\u0000'`，不是字母 `'0'` |
| 參考型別，例如 `String` | `null`，不是空字串 `""` |

### 區域變數和陣列元素不能混淆

```java
int[] a;              // 尚未賦值的區域變數
// System.out.println(a); // 編譯錯誤

String[] names = new String[2];
System.out.println(names[0]); // null
```

欄位與新陣列元素有預設值；區域變數使用前必須確定已賦值。建立物件陣列也不會自動建立元素所指向的物件。

<a id="slide-06"></a>

## 第 06 頁｜Array Indexes

### 索引從 0 開始

長度為 `n` 的陣列，其合法索引滿足 `0 <= index && index < n`。非空陣列最後一個索引是 `n - 1`。

```java
int[] a = {10, 20, 30};
// 合法索引：0、1、2
// a[3] 和 a[-1] 都越界
```

### 用詞更正：Index 不等於 Object Reference

投影片以「integer reference」描述位置。這裡應理解為 **index（索引）**，不要和 object reference 混用：reference 找到陣列物件，index 選出物件中的元素。

<a id="slide-07"></a>

## 第 07 頁｜Array Visualization

### 拆解陣列宣告

```java
int[] primes = new int[10];
```

| 部分 | 意義 |
|---|---|
| `int[]` | 變數的型別：int 陣列 reference |
| `primes` | 變數名稱 |
| `new int[10]` | 建立有 10 個元素的陣列 |
| `primes[0]` 到 `primes[9]` | 10 個可存取的元素 |

```text
primes → [0][0][0][0][0][0][0][0][0][0]
index     0  1  2  3  4  5  6  7  8  9
```

**更正：** 陣列物件的型別是 `int[]`，元素型別才是 `int`。變數名稱 `primes` 不會自動讓元素成為質數。

<a id="slide-08"></a>

## 第 08 頁｜Accessing Array Elements

### 用 `array[index]` 取得元素

```java
String[] names = {"Lu", "Qian", "John"};
String firstName = names[0];
System.out.println(firstName); // Lu

int i = 2;
System.out.println(names[i]); // John
```

`names[0]` 是運算式：取得索引 0 的元素值，不會自動輸出。對 `String[]` 而言，取得的是 String reference；對 `int[]` 而言，取得的是整數值。

### 課堂補充：Out of Bounds、Restricted 與 Fail

- Java 限制合法存取範圍，這是 **restriction（限制）**。
- 索引越界時，該次存取 **fails（失敗）**，並拋出 `ArrayIndexOutOfBoundsException`。
- Java 阻止非法讀寫，不會先讀到陣列外的資料再通知失敗。
- 長度與索引可能在執行時才能確定，所以必須做執行時邊界檢查。明顯越界的常數索引也不一定造成編譯錯誤。
- `null` 陣列的存取則是 `NullPointerException`，與索引越界不同。

```java
int[] a = {10, 20, 30};
// int x = a[3]; // 執行時拋出 ArrayIndexOutOfBoundsException

for (int i = 0; i < a.length; i++) {
    System.out.println(a[i]);
}
```

迴圈條件使用 `< a.length`，不是 `<= a.length`。

<a id="slide-09"></a>

## 第 09 頁｜Assigning values to Array Elements

### 指定索引並寫入相容的值

```java
double[] prices = new double[3];
prices[0] = 6.75;
prices[1] = 80.43;
prices[2] = 10.02;

String[] names = new String[2];
names[0] = "Lu";
names[1] = "Qian";
```

| 寫法 | 動作 |
|---|---|
| `double x = prices[0];` | 讀取元素值，存入 x |
| `prices[0] = 9.99;` | 修改指定元素 |

寫入同樣會檢查邊界，也不會自動擴大陣列。

<a id="slide-10"></a>

## 第 10 頁｜Length of array

### 長度固定，使用 `.length` 欄位

```java
String[] names = {"Lu", "Qian", "Emina", "Jamal", "John"};
System.out.println(names.length); // 5
System.out.println(names[names.length - 1]); // John
```

- 陣列使用 `array.length`，沒有括號；String 使用 `text.length()`。
- 長度為 0 的陣列合法，但沒有可存取的元素。
- 陣列物件長度不能改；陣列變數可以改指向另一個陣列。

```java
int[] a = new int[3];
a = new int[10]; // 建立新陣列，不是把原陣列擴大
```

<a id="slide-11"></a>

## 第 11 頁｜Modifying Array Elements

### 修改內容與重新賦值 Reference

```java
String[] names = {"David", "Qian"};
names[0] = "Beki";
System.out.println(names[0]); // Beki
```

修改 `names[0]` 是替換該元素的 reference，不是把原本的 String 物件 `"David"` 改掉。投影片此頁使用 `David`，和前頁範例的初始姓名不同；這裡以獨立範例呈現。

### 課堂補充：Aliasing、Clone 與 Final

```java
int[] a = {1, 2};
int[] b = a;
b[0] = 99;
System.out.println(a[0]); // 99：共用同一陣列

int[] c = a.clone();
c[0] = 7;
System.out.println(a[0]); // 99：c 是另一個陣列

final int[] fixed = {1, 2};
fixed[0] = 8;         // 合法：改元素
// fixed = new int[2]; // 不合法：重新賦值 final 變數
```

`clone()` 對物件陣列與二維陣列是淺拷貝：元素 reference 被複製，所指物件仍可能共用。`==` 比較陣列身分；內容比較可用 `java.util.Arrays.equals`，巢狀內容可用 `deepEquals`。

### 面試補充：Java 是 Pass-by-Value

方法收到引數值的副本；陣列參數收到的是 reference 值的副本。因此 `arr[0] = 88` 可以修改共用陣列，但 `arr = new int[3]` 只重新賦值方法內的參數，不會改變呼叫端變數的 reference。

<a id="slide-12"></a>

## 第 12 頁｜Multi-Dimensional Arrays

### 二維陣列是「陣列的陣列」

```java
int[][] grid = {{8, 4}, {9, 7}, {3, 6}};
System.out.println(grid[2][0]); // 3
```

```text
grid → 外層陣列
         [0] → int[] [8, 4]
         [1] → int[] [9, 7]
         [2] → int[] [3, 6]
```

`grid[row][column]` 先取得某列的陣列，再取得那一列中的元素。

### Number of Rows 與每列長度

| 表達式 | 意義 | 本例 |
|---|---|---:|
| `grid.length` | 外層長度，即 number of rows | 3 |
| `grid[row].length` | 當前列的元素數 | 2 |

外層長度不是所有整數的總數。本例有 6 個整數，但 `grid.length` 是 3。

<a id="slide-13"></a>

## 第 13 頁｜Multi-Dimensional Array

### 建立矩形陣列並使用兩個索引

```java
double[][] heights = new double[20][55];
heights[11][23] = 12.5;
```

這個陣列有 20 列，每列 55 個元素。列索引為 0–19，欄索引為 0–54。以上建立一個外層陣列和 20 個列陣列。

### 課堂補充：Row Order Processing

**通常以逐列方式（row-major traversal）走訪：先走完當前列，再走下一列。** 這是迴圈設計，不是 Java 強制的執行順序，也不保證所有列在實體記憶體中連續排列。

```java
int[][] table = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
for (int i = 0; i < table.length; i++) {
    for (int j = 0; j < table[i].length; j++) {
        System.out.print(table[i][j] + " ");
    }
    System.out.println();
}
```

外層選列，內層選當前列的元素。這叫 basic/traditional for loop；使用兩層則稱 nested for loops（巢狀迴圈）。

<a id="slide-14"></a>

## 第 14 頁｜Enhanced for Loop

### 直接依序取得元素

```java
char[] vowels = {'a', 'e', 'i', 'o', 'u'};
for (char item : vowels) {
    System.out.println(item);
}
```

`for (char item : vowels)` 可讀成「依序取得 vowels 的每個 char 元素」。Enhanced for loop 也稱 for-each，不需要自己更新索引。

### 課堂 Exercise：把原本雙層 For 改寫

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

外層元素型別是 `int[]`，內層元素型別才是 `int`。輸出：

```text
1 2 3
4 5 6
7 8 9
10 11 12
```

### 迴圈變數是元素值的副本

對 `int[]` 使用 `for (int item : array)`，再寫 `item = 99`，不會修改陣列元素。物件 reference 的副本可以用來修改共用物件，但重新賦值迴圈變數不會替換原陣列元素。需要索引或直接替換元素時，基本 for 迴圈較適合。

<a id="slide-15"></a>

## 第 15 頁｜Jagged Arrays

### 每列長度不同的不規則陣列

```java
int[][] table = {{1, 2, 3}, {4}, {5, 6, 7, 8}};
// 各列長度：3、1、4
```

### Omit the Column 的意思

省略第二組方括號內的大小，保留括號，稍後分別建立每列：

```java
int[][] table = new int[3][];
table[0] = new int[3];
table[1] = new int[1];
table[2] = new int[4];
```

剛執行 `new int[3][]` 時，各列都是 `null`。完成三列配置後，整數元素都是 0。

走訪時，內層用 `table[i].length`；或使用雙層 enhanced for。這些寫法假設各列不是 `null`。

### 投影片語法更正

投影片最後一行把配置大小與初始化串在一起，不是合法 Java。可改成：

```java
int[][] arr = {{1, 2, 3}, {4, 5}};
```

<a id="slide-16"></a>

## 第 16 頁｜Exercise

### 按照每列長度輸出 Jagged Array

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

輸出（忽略每行最後的空格）：

```text
1 2 3
4 5
6 7 8 9
10
11 12 13 14 15
```

### 練習檢查點

- 外層長度為 5，而每列長度依序為 3、2、4、1、5。
- 換行放在內層迴圈結束之後、外層迴圈之內。
- 若改寫為基本 for，內層上限應為 `num[i].length`。

<a id="slide-17"></a>

## 第 17 頁｜Strings

### String Literal 與 New String

`String` 是參考型別；字串 literal 使用雙引號，單一 `char` literal 使用單引號。

```java
String fName = "John";
String name = "John";
String lName = new String("John");
String mName = new String("John");
```

### 白板補充：String Pool 與物件身分

依課堂照片重建：相同 literal 可以共用字串池中的物件；每次 `new String(...)` 則建立另一個 String 物件。

```text
fName ─┐
       ├→ 字串池中的 "John"
name ──┘
lName ──→ 另一個 "John" 物件
mName ──→ 又一個 "John" 物件
```

```java
System.out.println(fName == name);       // true
System.out.println(lName == mName);      // false
System.out.println(fName == lName);      // false
System.out.println(lName.equals(mName)); // true
```

**`==` 比較物件身分；String 的 `.equals()` 比較文字內容。** 池中的 String 也是物件，不應把字串池想成不屬於 heap 的另一種字串。

<a id="slide-18"></a>

## 第 18 頁｜Strings

### Immutable：內容不可變，Reference 可以重新賦值

```java
String fullName = "John";
String original = fullName;
fullName = fullName + " Smith";

System.out.println(fullName); // John Smith
System.out.println(original); // John
```

`fullName` 改指向串接結果，原本的 `"John"` 物件沒有被改寫。第一張白板照片的箭頭與 `immutable` 可用這個例子理解；照片中空字串／空白字串的細節不影響此原理。

### `+` 的加法與字串串接

```java
System.out.println(1 + 2 + " apples");   // 3 apples
System.out.println("apples: " + 1 + 2);  // apples: 12
System.out.println("apples: " + (1 + 2)); // apples: 3
```

`+` 依操作數型別做數值相加或字串串接，括號可以改變計算分組。不要認為每個看得到的 `+` 都必然產生獨立物件；編譯器可以最佳化常數與串接。

### 方法結果需要接住

```java
String name = "John";
name.toUpperCase();
System.out.println(name); // John
name = name.toUpperCase();
System.out.println(name); // JOHN
```

<a id="slide-19"></a>

## 第 19 頁｜String Class

### 讀取與轉換文字

`String` 位於 `java.lang`，一般 Java 程式不需要另外 import。

| 方法 | 用途 | 例子 |
|---|---|---|
| `charAt(index)` | 取得指定位置的 char | `"Java".charAt(0)` → `'J'` |
| `length()` | UTF-16 code units 的數量 | `"Java".length()` → `4` |
| `toUpperCase()` | 回傳大寫結果 | `"Java".toUpperCase()` → `"JAVA"` |
| `toLowerCase()` | 回傳小寫結果 | `"Java".toLowerCase()` → `"java"` |
| `trim()` | 去掉兩端值不大於 U+0020 的字元 | `"  Java  ".trim()` → `"Java"` |
| `substring(beginIndex)` | 取得從指定索引到結尾的結果 | `"Java".substring(2)` → `"va"` |

### 常見誤解

- `charAt` 用括號，不是 `text[index]`。
- `charAt` 合法索引小於 `length()`；`substring(length())` 則合法，回傳空字串。
- Emoji 等字元可能占兩個 UTF-16 code units，`length()` 不一定等於肉眼看到的字元數。
- 這些轉換不會修改原本 String；大小寫轉換也可能受 locale 影響。

官方補充：[String API](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/String.html)。

<a id="slide-20"></a>

## 第 20 頁｜String Class

### 搜尋、比較與拆分

| 方法 | 重點 |
|---|---|
| `indexOf(...)` | 第一個符合位置，找不到回傳 `-1` |
| `lastIndexOf(...)` | 最後一個符合位置，找不到回傳 `-1` |
| `concat(...)` | 回傳串接結果，不修改原字串 |
| `equals(...)` | 比較內容 |
| `equalsIgnoreCase(...)` | 忽略大小寫比較內容 |
| `split(regex)` | 依正規表示式拆成 String 陣列 |
| `replace(...)` | 回傳替換後結果；此處的 replace 不是 replaceAll |
| `compareTo(...)` | 字典順序比較，回傳負值、0 或正值 |
| `compareToIgnoreCase(...)` | 忽略大小寫的順序比較 |

```java
String text = "banana";
System.out.println(text.indexOf('a'));     // 1
System.out.println(text.lastIndexOf('a')); // 5
System.out.println(text.indexOf('z'));     // -1
System.out.println(text.replace('a', 'o')); // bonono
System.out.println(text); // banana
```

**更正：** 投影片中的 `equalgnoreCase` 應為 `equalsIgnoreCase`。`compareTo` 只需判斷正負或 0，不保證回傳恰好 `-1` 或 `1`。找不到時的 `-1` 不能直接拿去當索引。

<a id="slide-21"></a>

## 第 21 頁｜Escape Sequences

### 使用反斜線表達特殊字元

| 寫法 | 意義 |
|---|---|
| `\b` | Backspace 控制字元 |
| `\t` | Tab 定位字元 |
| `\n` | Newline 換行字元 |
| `\r` | Carriage return 回車字元 |
| `\"` | 雙引號 |
| `\'` | 單引號 |
| `\\` | 一個反斜線 |

### 範例

```java
System.out.println("She said \"Hello\".");
System.out.println("C:\\notes\\week3");
```

輸出：

```text
She said "Hello".
C:\notes\week3
```

<a id="slide-22"></a>

## 第 22 頁｜Escape Sequence: Example

### 多行輸出、縮排與引號

投影片把 `\n`、`\t`、`\"` 和多段字串串接結合。用較短的自編範例觀察相同概念：

```java
System.out.println("Name:\n\t\"John\"\nCourse:\n\tJava");
```

示意輸出（Tab 寬度依顯示環境而異）：

```text
Name:
    "John"
Course:
    Java
```

### 補充：Backspace 不是修改 String 的方法

投影片也使用 `\b`。它是控制字元，不會從 Java String 物件中刪除前一個字元；終端機可能移動游標，IDE console 則可能有不同顯示行為。不要依賴 `\b` 來修正文句。

<a id="slide-23"></a>

## 第 23 頁｜Input/Output

### 三個標準串流

| 名稱 | 角色 | 常見用途 |
|---|---|---|
| `System.in` | 標準輸入 | 常見來源是鍵盤，也可重新導向 |
| `System.out` | 標準輸出 | 正常結果與提示 |
| `System.err` | 標準錯誤輸出 | 診斷或錯誤訊息 |

Stream 可理解為依序讀取或寫入的資料流。

### 投影片概念補充

Java I/O 有位元組串流，也有字元導向的 Reader/Writer，不能概括為所有 I/O 串流都是位元組 API。使用串流也不保證一定更快，效能與緩衝、裝置及使用方式有關。這裡的 I/O stream 與集合操作的 `java.util.stream.Stream` 不同。

<a id="slide-24"></a>

## 第 24 頁｜Printing

### Print、Println 與 Printf

| 方法 | 用途 |
|---|---|
| `print(...)` | 輸出，不自動換行 |
| `println(...)` | 輸出後換行；無參數時只換行 |
| `printf(format, args...)` | 依格式輸出，不自動換行 |

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

`%s` 表示文字、`%d` 表示整數、`%.2f` 顯示小數兩位、`%n` 使用平台換行。例子指定 US locale，使小數點格式固定。格式與引數型別不相容可能造成格式化例外。

官方補充：[PrintStream API](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/io/PrintStream.html)。

<a id="slide-25"></a>

## 第 25 頁｜Reading Input

### Scanner 將輸入解析成可用資料

- Scanner 可以從標準輸入、字串或檔案等來源讀取資料。
- 預設以空白字元分隔 token，例如空格、Tab 與換行。
- 從 console 讀取時，Scanner 可能等待使用者提供資料。

```java
java.util.Scanner scan = new java.util.Scanner("10 20");
int first = scan.nextInt();
int second = scan.nextInt();
System.out.println(first + second); // 30
scan.close();
```

這個例子從字串讀取，方便先理解 Scanner，而不需要鍵盤輸入。

<a id="slide-26"></a>

## 第 26 頁｜Reading Input

### 建立 Scanner 並讀取一整行

完整程式：

```java
import java.util.Scanner;

public class ReadName {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your full name: ");
        String answer = scan.nextLine();
        System.out.println("Hello, " + answer);
        // 共用 System.in 的程式應統一管理其生命週期。
    }
}
```

`nextLine()` 讀取目前位置到該行結尾的內容，不包含行分隔符號。因此 `John Smith` 中間的空格會被保留。

### 資源生命週期補充

關閉包住 `System.in` 的 Scanner 也會關閉底層輸入。需要繼續使用標準輸入時，不要由某個局部讀取方法提早關閉它；可由程式統一管理。檔案等由程式擁有的資源則應妥善關閉。

<a id="slide-27"></a>

## 第 27 頁｜Reading Input

### 匯入與常用方法

```java
import java.util.Scanner;
```

| 方法 | 回傳／讀取內容 |
|---|---|
| `next()` | 下一個 token |
| `nextLine()` | 目前位置到該行結尾的文字 |
| `nextInt()` | 下一個整數 token |
| `nextFloat()` | 下一個 float token |
| `nextDouble()` | 下一個 double token |

### 常見陷阱：NextInt 後面接 NextLine

```java
java.util.Scanner scan = new java.util.Scanner("20\nJohn Smith\n");
int age = scan.nextInt();
scan.nextLine(); // 消耗年齡那一行剩餘的內容與行分隔符號
String name = scan.nextLine();
System.out.println(age + ": " + name); // 20: John Smith
scan.close();
```

`nextInt()` 讀取數字 token 後，該行剩餘內容與換行可能仍待處理。上述情境若省略中間的 `nextLine()`，下一次讀行會先得到空字串。這個做法假設下一筆資料位於下一行。

輸入型別不符可能拋出 `InputMismatchException`；可以用 `hasNextInt()` 等方法先檢查，也要考慮資料已讀完的情況。

官方補充：[Scanner API](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Scanner.html)。

<a id="slide-28"></a>

## 第 28 頁｜References

### 投影片列出的參考書

Herbert Schildt、Danny Coward，*Java: The Complete Reference*，第 13 版，McGraw-Hill，2024。講義列出第 3、13 章。此書目依投影片記載，本筆記未另外讀取該書。

### 官方延伸閱讀

- [JLS Chapter 10：Arrays](https://docs.oracle.com/javase/specs/jls/se25/html/jls-10.html)
- [JLS 4.12：Variables](https://docs.oracle.com/javase/specs/jls/se25/html/jls-4.html#jls-4.12)
- [JLS 8.4.1：Formal Parameters](https://docs.oracle.com/javase/specs/jls/se25/html/jls-8.html#jls-8.4.1)
- [JLS 14.14.2：Enhanced for](https://docs.oracle.com/javase/specs/jls/se25/html/jls-14.html#jls-14.14.2)
- [JVMS 2.5：Run-Time Data Areas](https://docs.oracle.com/javase/specs/jvms/se25/html/jvms-2.html#jvms-2.5)

### 複習自我檢查

1. `a.length` 和 `a.length - 1` 分別代表什麼？
2. 為什麼二維陣列內層用 `table[i].length`？
3. `int[] row` 和 `int item` 為什麼型別不同？
4. `b = a`、`b[0] = 99` 和 `b = a.clone()` 有什麼差別？
5. 為什麼相同文字的 String，`==` 可能是 false？
6. 為什麼 `name.toUpperCase()` 不會直接修改 name 指向的字串？
7. `nextInt()` 後接 `nextLine()` 時，哪些內容還留在輸入中？
