# CSYE6200 OOP — Week 3：Java Array 筆記

## 1. 課堂重點：Row Order Processing

> 課堂提到的「Java 是 row order processing」，可以理解為：二維陣列通常採用逐列處理（row-major traversal），先處理完一個 row，再處理下一個 row。

本筆記使用：**row = 列，column = 欄**。

```java
int[][] numbers = {
    {10, 20, 30},  // row 0
    {40, 50, 60}   // row 1
};
```

逐列走訪的順序：

```text
10 → 20 → 30 → 40 → 50 → 60
```

**精確補充：這是常見的走訪方式，不是 Java 強制的執行順序。** 程式可以寫成其他走訪順序。Java 的二維陣列實際上是「陣列的陣列」，不能因此認為所有列一定連續儲存在同一塊記憶體中。

## 2. Array Variable、Reference 與 Heap

```java
int[] numbers = new int[3];
```

這行程式包含兩件事：

1. 宣告 `numbers`：一個可以儲存 `int[]` reference 的變數。
2. 建立長度為 3 的陣列物件，並把它的 reference 存入 `numbers`。

```text
numbers 裡的 reference ──→ 陣列物件 [0, 0, 0]
```

- `int` 是基本型別（primitive type）。
- `int[]` 是參考型別（reference type），陣列本身是物件。
- reference 可以理解為「用來找到物件的參考值」，不是陣列內容本身。
- Java 不讓一般程式把 reference 當成可自由運算的實體記憶體位址。

在 JVM 的概念模型中：

| 項目 | 所在位置／歸屬 |
|---|---|
| 方法內的區域變數與參數 | 該次方法呼叫的 stack frame |
| 陣列物件與其元素 | Heap |
| 物件的 instance fields | 屬於該物件，概念上隨物件位於 heap |

**不要背成「primitive 都在 stack，reference 都在 heap」。** 要看變數是區域變數、物件欄位，還是陣列元素。實際 JVM 也可能進行最佳化，以上是理解程式行為的模型。

### 宣告不等於建立陣列

```java
int[] a;          // 只宣告區域變數，尚未賦值，不能直接讀取
int[] b = null;   // 已賦值，但沒有指向任何陣列
int[] c = new int[3]; // 建立陣列，元素預設都是 0
int[] d = new int[0]; // 合法的空陣列，length 是 0
```

新陣列的元素有預設值：整數為 `0`、浮點數為 `0.0`、`boolean` 為 `false`、`char` 為 `'\u0000'`、reference 為 `null`。區域變數使用前則必須確定已經賦值。

## 3. Accessing Array Elements：存取陣列元素

語法：

```java
arrayName[index]
```

**Index 是位置編號，從 0 開始；它不是 object reference，也不是要搜尋的值。**

```java
int[] numbers = {10, 20, 30};
```

| Index | 元素 | 存取方式 |
|---|---:|---|
| 0 | 10 | `numbers[0]` |
| 1 | 20 | `numbers[1]` |
| 2 | 30 | `numbers[2]` |

### 讀取與寫入

```java
int x = numbers[1]; // 讀取：把整數值 20 複製給 x
x = 99;             // 只改 x，numbers[1] 仍然是 20

numbers[1] = 99;     // 寫入：直接修改陣列第 1 號元素
```

`numbers[1]` 是運算式，不會自動印出資料。要顯示內容，需要例如：

```java
System.out.println(numbers[1]);
```

### Length 與最後一個索引

```java
numbers.length      // 元素數量：3
numbers.length - 1  // 最後一個元素的索引：2
```

陣列用 `.length`，沒有括號。非空陣列的最後一個元素是 `numbers[numbers.length - 1]`。

## 4. Out of Bounds：超出索引範圍

合法索引必須滿足：

```java
0 <= index && index < numbers.length
```

對長度為 3 的陣列，合法索引是 `0、1、2`。

```java
int[] numbers = {10, 20, 30};

System.out.println(numbers[2]); // 合法：30
// System.out.println(numbers[3]);  // 執行時越界
// System.out.println(numbers[-1]); // 執行時越界
```

越界時，Java 會阻止這次存取，並拋出 `ArrayIndexOutOfBoundsException`，不會讓程式讀到陣列外的資料。讀取和寫入都有這項限制，越界寫入也不會自動擴大陣列。

### Restricted 和 Fail 的關係

| 用詞 | 描述的層次 |
|---|---|
| Restricted（受到限制） | 規則：只能存取合法索引範圍 |
| Fail（失敗） | 結果：這次越界的存取沒有成功 |
| Exception（例外） | Java 通知程式這次操作失敗的機制 |

**不是二選一：因為有邊界限制，越界的存取操作會失敗。**

### 為什麼通常是執行時檢查？

```java
static int getElement(int[] array, int index) {
    return array[index];
}
```

編譯時知道 `array` 和 `index` 的型別，但不一定知道呼叫者會傳入多長的陣列、多大的索引，所以需要在執行時檢查。即使是明顯越界的常數索引，也不一定是 Java 的編譯錯誤；開發工具可能另外提出警告。

### For loop 要用 `<`，不是 `<=`

```java
for (int i = 0; i < numbers.length; i++) {
    System.out.println(numbers[i]);
}
```

若寫成 `i <= numbers.length`，就可能執行到不存在的 `numbers[numbers.length]`。

## 5. 二維陣列：Number of Rows 是什麼？

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

存取語法：

```java
numbers[row][column]

numbers[1][2] // 第 1 列、第 2 欄，值是 60
```

| 寫法 | 意義 | 本例的值 |
|---|---|---:|
| `numbers.length` | Number of rows：外層陣列有幾個元素，也就是幾列 | 2 |
| `numbers[row].length` | 目前這一列有幾個元素 | 3 |

**`numbers.length` 不是所有整數元素的總數。** 本例共有 6 個整數，但外層陣列長度是 2。

Java 的二維陣列是 array of arrays：

```text
numbers → 外層陣列
             [0] → int[] [10, 20, 30]
             [1] → int[] [40, 50, 60]
```

`numbers[1][2]` 可以拆成：先用 `numbers[1]` 找到第 1 列的陣列，再取出那個陣列的第 2 號元素。

## 6. 用雙層 For Loop 逐列存取

```java
for (int row = 0; row < numbers.length; row++) {
    for (int col = 0; col < numbers[row].length; col++) {
        System.out.print(numbers[row][col] + " ");
    }
    System.out.println(); // 當前這一列印完後換行
}
```

- **外層迴圈選 row**：根據 number of rows 決定跑幾次。
- **內層迴圈選 column**：走訪目前這一列的每個元素。
- 每換到下一列，內層的 `col` 都重新從 `0` 開始。

執行順序：

```text
row = 0：numbers[0][0] → numbers[0][1] → numbers[0][2]
row = 1：numbers[1][0] → numbers[1][1] → numbers[1][2]
```

輸出：

```text
10 20 30
40 50 60
```

### 每列可以有不同長度：Jagged Array

```java
int[][] numbers = {
    {10, 20, 30},
    {40, 50}
};

// numbers.length    == 2
// numbers[0].length == 3
// numbers[1].length == 2
```

因此內層應使用 **`numbers[row].length`**，不要直接假設每列都和第 0 列一樣長。

上述迴圈假設外層陣列與各列都不是 `null`。例如 `new int[2][]` 只建立外層陣列，兩列一開始都是 `null`，要先建立各列才能存取其 `.length` 或元素。

## 7. 複製 Reference 與複製陣列

```java
int[] a = {10, 20};
int[] b = a;

b[0] = 99;
System.out.println(a[0]); // 99
```

`b = a` 複製 reference，沒有建立新陣列：

```text
a ──┐
    ├──→ 同一個陣列 [99, 20]
b ──┘
```

但重新賦值不同：

```java
b = new int[]{7, 8};
// b 改指向新陣列，a 仍指向 [99, 20]
```

| 操作 | 修改什麼？ |
|---|---|
| `b[0] = 99` | 陣列物件裡的元素 |
| `b = new int[]{7, 8}` | b 變數裡保存的 reference |

陣列建立後長度固定；變數可以改指向另一個不同長度的陣列。

### Clone 與淺拷貝

```java
int[] a = {1, 2};
int[] b = a.clone();
b[0] = 99;
System.out.println(a[0]); // 1
```

`clone()` 建立新陣列並複製元素。但物件陣列的元素是 reference，所以複製後仍可能共用物件；二維陣列直接 `clone()` 也只複製外層，各列仍共用。

所謂「heap saving」若是指節省記憶體，重點在於共用同一物件可以避免建立副本；**儲存在 heap 本身不等於省記憶體**，而共用可變資料也會帶來互相影響的問題。

## 8. 面試補充：Pass-by-Value 與 Final

### Java 永遠是 Pass-by-Value

方法參數收到的是值的副本。傳入陣列時，複製的是 reference 值。

```java
static void change(int n, int[] arr) {
    n = 99;                 // 只改參數 n
    arr[0] = 88;            // 修改共用陣列
    arr = new int[]{7, 8};  // 只改參數 arr 的 reference
}
```

呼叫端：

```java
int x = 10;
int[] a = {1, 2};
change(x, a);

System.out.println(x);    // 10
System.out.println(a[0]); // 88
```

能修改共用物件，不代表是 pass-by-reference。

### Final 不代表陣列元素不能改

```java
final int[] a = {1, 2};
a[0] = 99;         // 合法：修改元素
// a = new int[3]; // 編譯錯誤：不能重新賦值給 a
```

## 快速複習

- 一維陣列：`array[index]`。
- 二維陣列：`array[row][column]`。
- 索引從 `0` 開始，必須小於對應陣列的 `length`。
- `array.length` 是二維陣列的列數；`array[row].length` 是當前列的元素數。
- Row order processing：通常先走完一列，再走下一列；順序由程式決定。
- 越界存取受 Java 阻止，並以例外表示操作失敗。
- 陣列變數儲存 reference；`b = a` 不會複製整個陣列。
- 分析程式時，先問：**這行改的是 reference，還是物件的內容？**

## 參考

- 課堂講義：*Arrays, Strings, and I/O*，第 4–15 頁。
- [Java 語言規範：Arrays](https://docs.oracle.com/javase/specs/jls/se25/html/jls-10.html)
- [Java 語言規範：Variables](https://docs.oracle.com/javase/specs/jls/se25/html/jls-4.html#jls-4.12)
- [Java 語言規範：Formal Parameters](https://docs.oracle.com/javase/specs/jls/se25/html/jls-8.html#jls-8.4.1)
- [JVM 規範：Run-Time Data Areas](https://docs.oracle.com/javase/specs/jvms/se25/html/jvms-2.html#jvms-2.5)
