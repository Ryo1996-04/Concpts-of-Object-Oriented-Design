# CSYE6200 筆記：Exception Handling

> 個人學習筆記，課堂內容以自己的話重新整理。本文不含投影片原文或圖片，標註的頁碼只用來對照課程講義；所有範例程式都是自己寫的，錯誤訊息都是實際用 `javac` / `java` 跑出來的。

- 課程：CSYE6200（Fall 2026）
- 單元：Exceptions; Unit Testing in Java（`9_Exception Handling in Java.pdf`，本篇是 p.3–30 的 Exception Handling；p.31–40 Unit Testing 另寫）
- 日期：2026-11-10
- 可執行的範例程式：[`../src/`](../src)（DivideDemo、FileCheck、InvalidAgeException、AgeDemo）

## 目錄

1. [課堂開場：三種錯誤](#1-課堂開場三種錯誤)
2. [兩種處理錯誤的方式（p.3–4）](#2-兩種處理錯誤的方式p34)
3. [Exception 是什麼（p.5）](#3-exception-是什麼p5)
4. [Throwable 繼承架構（p.6–7）](#4-throwable-繼承架構p67)
5. [new 與 heap：和記憶體有關的錯誤（p.4、p.5、p.7）](#5-new-與-heap和記憶體有關的錯誤p4p5p7)
6. [Checked 與 Unchecked（p.8–10）](#6-checked-與-uncheckedp810)
7. [沒有處理時會怎樣：讀懂 Stack Trace（p.11–13）](#7-沒有處理時會怎樣讀懂-stack-tracep1113)
8. [為什麼需要 Exception Handling（p.13–14）](#8-為什麼需要-exception-handlingp1314)
9. [try…catch（p.15–16）](#9-trycatchp1516)
10. [執行流程與 Termination Model（p.17、p.27）](#10-執行流程與-termination-modelp17p27)
11. [throw 與 throws（p.18–21）](#11-throw-與-throwsp1821)
12. [Multiple catch blocks（p.22–23）](#12-multiple-catch-blocksp2223)
13. [finally（p.24–25）](#13-finallyp2425)
14. [Rethrowing（p.26）](#14-rethrowingp26)
15. [User-Defined Exception（p.28–30）](#15-user-defined-exceptionp2830)
16. [補充：exception 物件的常用方法](#16-補充exception-物件的常用方法)
17. [講義語法更正](#17-講義語法更正)
18. [檢查清單](#18-檢查清單)

---

## 1. 課堂開場：三種錯誤

老師先把程式錯誤分成三種（講義沒有這幾頁，是課堂講解）。差別在於**什麼時候發現**、**誰發現**。

| 種類 | 什麼時候發現 | 誰發現 | 程式能跑嗎 | 怎麼處理 |
|---|---|---|---|---|
| Syntax error（compile-time error） | 編譯時 | `javac` | 不能，連 `.class` 都產生不出來 | 照錯誤訊息改程式 |
| Runtime error | 執行到某一行時 | JVM，丟出 exception | 能編譯，跑到一半中止 | **Exception handling**（本篇） |
| Logic error | 看結果才發現 | 人或測試 | 能跑完，不會當掉 | 測試（講義後半的 Unit Testing）、debug |

### Syntax error：寫法不符合 Java 規則

```java
int x = 10          // 少了分號
System.out.println(x);
```

```text
Syntax.java:3: error: ';' expected
        int x = 10
                  ^
1 error
```

拼錯方法名稱也是編譯時就抓到。講義 p.11 剛好有這個錯：`input.netxInt()`。

```text
Typo.java:5: error: cannot find symbol
        int num1 = input.netxInt();
                        ^
  symbol:   method netxInt()
  location: variable input of type Scanner
```

### Runtime error：語法正確，執行時出事

```java
public static int div(int a, int b) { return a / b; }
// main 裡呼叫 div(10, 0)
```

可以編譯，執行到除以 0 才中止：

```text
Exception in thread "main" java.lang.ArithmeticException: / by zero
	at Runtime.div(Runtime.java:2)
	at Runtime.main(Runtime.java:4)
```

Week 3 的 `ArrayIndexOutOfBoundsException`、Week 9 Scanner 筆記的 `args[0]` 沒給值，也都是 runtime error。

### Logic error：程式跑完了，但答案錯

```java
int a = 2, b = 3;
double avg = (a + b) / 2;     // 想算平均 2.5
System.out.println(avg);      // 實際印出 2.0
```

`(a + b) / 2` 兩邊都是 `int`，先做整數除法得到 `2`，存進 `double` 才變成 `2.0`。改成 `(a + b) / 2.0` 就會印出 `2.5`。編譯器和 JVM 都不會報錯，只能靠測試發現。

> **重點**：exception handling 只處理 **runtime error**。syntax error 程式根本跑不起來；logic error 不會丟 exception。

---

## 2. 兩種處理錯誤的方式（p.3–4）

**方式一：在每個可能出錯的地方直接檢查**（p.3）

```text
做事情 A
如果 A 失敗 → 處理錯誤
做事情 B
如果 B 失敗 → 處理錯誤
```

- 優點：一眼就看得出有沒有檢查。
- 缺點：正常邏輯和錯誤處理混在一起，程式一大就很難讀、改、維護、debug。

**方式二：Exception handling**（p.4）

- 把錯誤處理從「主線」程式碼裡拿出來，集中放到 `catch`。
- 程式更清楚、更 robust（遇到錯誤不會直接崩潰），也更 fault-tolerant（出錯後還能繼續或安全結束）。

講義列出的常見失敗：記憶體配置不到、陣列索引超出範圍、除以 0、傳入不合法的參數。

## 3. Exception 是什麼（p.5）

Exception 是程式執行時**不正常、預期之外**的狀況。講義分三種來源：

| 來源 | 例子 |
|---|---|
| 程式設計師的錯 | 除以 0 |
| 使用者的錯 | 年齡輸入負數（第 15 節用自訂 exception 處理這個） |
| 實體資源不足 | 記憶體用完 |

## 4. Throwable 繼承架構（p.6–7）

Java 用**類別的繼承**來表示各種錯誤（Week 6 的繼承概念）。所有錯誤和 exception 的最上層是 `Throwable`：

```text
Throwable
├── Error                          嚴重錯誤，一般程式不處理
│   ├── OutOfMemoryError
│   ├── ThreadDeath
│   └── AWTError
└── Exception                      一般程式可能遇到、可以處理
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
| 意思 | 系統層級的嚴重問題 | 程式執行中可能發生的問題 |
| 要不要處理 | 通常不 catch，處理了也救不回來 | 應該處理 |
| 例子 | `OutOfMemoryError` | `ArithmeticException`、`IOException` |

因為是繼承關係，`catch (Exception e)` 可以接住 `Exception` **和它所有子類別**（polymorphism：父類別型別可以指向子類別物件）。

## 5. new 與 heap：和記憶體有關的錯誤（p.4、p.5、p.7）

課堂上老師提到 `new` 會 **allocate to the heap**，意思是：`new` 在 heap 上分配一塊記憶體，用來存放新物件。

### 1. Stack 與 Heap（複習 Week 3）

| | Stack | Heap |
|---|---|---|
| 放什麼 | 方法裡的 local variables、參數、reference | 用 `new` 建立的**物件**和**陣列** |
| 什麼時候清掉 | 方法結束就自動清掉 | 沒有 reference 指向它之後，由 garbage collector 回收（Week 4 p.39） |
| 大小 | 小 | 大，但有上限 |

```java
int[] scores = new int[5];
```

1. `new int[5]` 向 heap 要一塊能放 5 個 int 的空間（**allocate**）。
2. 把元素初始化成 0。
3. 把這塊空間的位址（reference）存進 stack 上的變數 `scores`。

### 2. new 失敗：OutOfMemoryError

p.4 列的第一個常見失敗，就是 **`new` 沒有分配到記憶體**：heap 滿了，`new` 要不到空間。

實際測試：把 heap 上限設成 64 MB（`java -Xmx64m Oom`），再 `new` 一個約 400 MB 的陣列：

```java
int[] big = new int[100_000_000];   // 約 400 MB
```

```text
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at Oom.main(Oom.java:3)
```

用預設大小的 heap 跑同一支程式，就能成功分配。所以會不會出錯，取決於 heap 有多大。

- 它在第 4 節架構圖的位置是 **`Throwable → Error → OutOfMemoryError`**。它是 `Error`，不是 `Exception`：記憶體都用完了，catch 住通常也救不回來，所以一般程式不處理它。
- 對應 p.5 的第三種來源：**實體資源不足**。
- 依第 1 節的分類屬於 runtime error：編譯沒問題，執行到 `new` 那行才出錯。

### 3. 同一組概念的另外兩個錯誤

| 錯誤 | 跟記憶體的關係 | 類別 |
|---|---|---|
| `StackOverflowError` | **stack** 用完了，通常是遞迴停不下來，每呼叫一次方法就在 stack 上多疊一層。實際測試：方法自己呼叫自己，疊到大約第 22,081 層就丟出（層數會因電腦和設定不同） | `Error` |
| `NullPointerException` | reference 是 `null`，**沒有指向 heap 上的任何物件**，卻去使用它 | `Exception`（unchecked），屬於程式的 bug |

```java
int[] a = null;
System.out.println(a.length);
```

```text
Exception in thread "main" java.lang.NullPointerException: Cannot read the array length because "<local1>" is null
```

（訊息裡的 `<local1>` 是變數 `a`。編譯時加 `-g`，或是在 Eclipse 裡執行，就會直接顯示變數名稱。）

### 4. Exception 本身也在 heap 上

`throw new ArithmeticException(...)` 也用了 `new`。exception 本身就是物件，所以同樣 allocate 在 heap 上。它不會隨著 `div()` 結束而消失，所以能一路傳到 `main` 的 catch，被 `e` 這個 reference 接住。

## 6. Checked 與 Unchecked（p.8–10）

### 判斷規則

| | Checked exception | Unchecked exception |
|---|---|---|
| 哪些類別 | `Exception` 的子類別，但**不是** `RuntimeException` 底下的 | `RuntimeException` 和它的子類別（`Error` 也不檢查） |
| 編譯器檢查嗎 | **檢查**：沒處理就編譯失敗 | 不檢查，執行時才發生 |
| 通常原因 | 程式外部的狀況：檔案不存在、網路斷線 | 程式本身的 bug：除以 0、null、索引錯 |
| 例子 | `IOException`、`FileNotFoundException`、自訂的 `extends Exception` | `ArithmeticException`、`NullPointerException` |

### Checked 例子（p.9）

開檔案可能失敗（檔案不存在），所以 `new FileReader(...)` 會丟 checked 的 `FileNotFoundException`。什麼都不處理就編譯：

```text
error: unreported exception FileNotFoundException; must be caught or declared to be thrown
```

錯誤訊息直接給了**兩種解法**：

1. **must be caught**：用 `try…catch` 接住（[`FileCheck.java`](../src/FileCheck.java)）。
2. **declared to be thrown**：在方法標頭加 `throws FileNotFoundException`，交給呼叫者處理（第 11 節）。

`FileCheck` 執行時，目錄裡沒有 `logs.txt`，會印出：

```text
not found: logs.txt (系統找不到指定的檔案。)
```

括號裡是作業系統的訊息，會跟著 Windows 語言改變。

### 課堂範例：CheckedException（檔案不存在、損壞……）

老師在課堂上寫的版本：

```java
import java.io.File;
public class CheckedException {
    public static void main(String[] args) {
        String fileName = "C:/example.txt";
        File input = new File(fileName);
        FileReader fr = new FileReader(input);
}
```

**照原樣編譯，會依序遇到三層錯誤**（每修好一層再編譯一次）：

| 層 | 錯誤訊息 | 原因 | 種類 |
|---|---|---|---|
| 1 | `reached end of file while parsing` | 少了 class 結尾的 `}`；javac 先檢查語法，這層沒過，後面的錯誤都不會出現 | syntax error |
| 2 | `cannot find symbol` … `class FileReader` | 只 import 了 `File`；`FileReader` 也在 `java.io`，要 import 或改成 `import java.io.*;` | compile-time error |
| 3 | `unreported exception FileNotFoundException; must be caught or declared to be thrown` | 老師真正要示範的：checked exception 沒有處理（同講義 p.9） | compile-time error |

**為什麼檔案相關的 exception 是 checked：程式無法控制**

`new FileReader(input)` 的語法完全正確。會不會成功，取決於**程式外面的世界**：

- 檔案在不在
- 路徑是不是其實指向一個資料夾
- 有沒有讀取權限
- 讀到一半，隨身碟會不會被拔掉、網路磁碟會不會斷線
- 內容有沒有損壞

這些都不是寫程式的人能控制或事先改對的，所以 Java 強迫你在**編譯時**就寫好「萬一發生了要怎麼辦」。對照之下，除以 0 只要先檢查 `b != 0` 就能避免，屬於程式本身的 bug，所以是 unchecked。

**修正版：[`../src/CheckedException.java`](../src/CheckedException.java)**

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
            fr = new FileReader(input);              // 打開：可能 FileNotFoundException
            int ch;
            while ((ch = fr.read()) != -1) {         // 讀取：可能 IOException
                System.out.print((char) ch);
            }
            System.out.println();
        } catch (FileNotFoundException e) {          // 子類別在前
            System.out.println("cannot open: " + e.getMessage());
        } catch (IOException e) {                    // 父類別在後
            System.out.println("read failed: " + e.getMessage());
        } finally {
            if (fr != null) {
                try {
                    fr.close();                      // close() 也可能 IOException
                } catch (IOException e) {
                    System.out.println("close failed: " + e.getMessage());
                }
            }
            System.out.println("finally: done");
        }
    }
}
```

| 寫法 | 為什麼 |
|---|---|
| `fr` 宣告在 `try` 外面，先設成 `null` | `try` 裡宣告的變數，在 `finally` 看不到（第 10 節 termination model） |
| `finally` 裡先檢查 `fr != null` | 檔案根本沒打開時 `fr` 還是 `null`，直接 `close()` 會丟出 `NullPointerException` |
| `close()` 外面再包一層 try | `close()` 本身也會丟 checked 的 `IOException` |
| 檔名可以從 `args[0]` 傳入 | 接上 main 與 args 的課堂問答：`java CheckedException 路徑` |

**各種狀況的實際測試結果**

| 狀況 | 在哪一步出錯 | 結果 |
|---|---|---|
| 檔案不存在 | `new FileReader(...)` | `FileNotFoundException`：`(系統找不到指定的檔案。)` |
| 路徑其實是資料夾 | `new FileReader(...)` | 也是 `FileNotFoundException`，訊息是 `(存取被拒。)` |
| 沒有讀取權限 | `new FileReader(...)` | `FileNotFoundException`（存取被拒）；沒有另外實測 |
| 正常檔案 | 不會出錯 | 印出內容，最後執行 finally |
| 讀到一半裝置斷線 | `fr.read()` | `IOException`，由第二個 catch 接住；無法在這裡模擬，沒有實測 |
| **內容損壞** | **不會丟 exception** | 印出 `abc��def`，壞掉的位元組被換成 `�` |

每一種狀況最後都會印出 `finally: done`。

**注意：「檔案損壞」不一定會丟 exception**

我寫了一個含有不合法 UTF-8 位元組的檔案，用 `FileReader` 讀時**完全沒有 exception**：壞掉的位元組被默默換成 `�`，程式照常跑完。這就變成第 1 節講的 **logic error**：程式沒當掉，但資料是錯的。

要讓損壞的內容丟出 exception，就改用嚴格模式讀取：

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

`MalformedInputException` 是 `IOException` 的子類別，所以 `catch (IOException e)` 接得住。（需要 import `java.io.BufferedReader`、`java.nio.charset.StandardCharsets`、`java.nio.file.Files`、`java.nio.file.Path`；寫法是第 13 節補充的 try-with-resources。）

### Unchecked 例子（p.10）

| Exception | 什麼時候發生 |
|---|---|
| `NullPointerException` | 對 `null` 呼叫方法或存取欄位，例如 `String s = null; s.length();` |
| `IndexOutOfBoundsException` | 索引超出範圍；陣列是子類別 `ArrayIndexOutOfBoundsException` |
| `ArithmeticException` | 整數除以 0 |
| `ClassCastException` | 物件強制轉型成它實際上不是的型別 |
| `IllegalArgumentException` | 方法收到不合理的參數（通常是自己 `throw`） |
| `IOError` | I/O 嚴重失敗。注意：它其實是 `Error` 的子類別，不是 exception |

`ClassCastException` 要在執行時才發生，變數的型別必須是 `Object` 這類父類別：

```java
Object o = 3.14;
String s = (String) o;   // 編譯通過，執行時 ClassCastException
```

如果寫 `Double d = 3.14; String s = (String) d;`，編譯器就知道 `Double` 不可能是 `String`，會直接編譯錯誤 `incompatible types`。

## 7. 沒有處理時會怎樣：讀懂 Stack Trace（p.11–13）

p.11 的程式讀兩個數字然後相除。第二個數字輸入 `0`：

```text
Exception in thread "main" java.lang.ArithmeticException: / by zero
	at Example.div(Example.java:4)
	at Example.main(Example.java:12)
```

| 部分 | 意思 |
|---|---|
| `in thread "main"` | 在 `main` 這條執行緒發生 |
| `java.lang.ArithmeticException` | exception 的類別（完整名稱含 package） |
| `/ by zero` | 錯誤訊息，就是 `getMessage()` 的內容 |
| `at Example.div(Example.java:4)` | **最上面一行 = 真正出錯的位置**：`div` 方法，第 4 行 |
| `at Example.main(Example.java:12)` | 呼叫路徑：第 12 行的 `main` 呼叫了 `div` |

由上往下讀，就是「在哪裡出錯 ← 是誰呼叫的」。exception 沒被接住，會一路往呼叫者傳，傳到 `main` 外面還沒人處理，程式就**異常終止**（p.13），後面的程式都不會執行。

## 8. 為什麼需要 Exception Handling（p.13–14）

- Exception 發生時，正常流程被打斷；沒處理就異常終止。
- Exception handling 的工作是決定：**讓程式回到正常狀態繼續跑**，或是**安全地結束**。
- 用詞：exception 被 **throw**（丟出），由 exception handler **catch**（接住）。

p.14 的適用範圍：

| 適用 | 不適用 |
|---|---|
| **同步（synchronous）錯誤**：執行某一行時直接造成的，例如除以 0 | **非同步（asynchronous）事件**：和目前執行到哪一行無關，例如磁碟 I/O 完成、滑鼠點擊；這些用其他機制處理（interrupt、event listener） |
| 系統可以從錯誤中恢復 | |
| 錯誤在 A 處發生，卻要在 B 處處理 | |
| 救不回來，但要**乾淨地關閉**（關檔案、存資料） | |

## 9. try…catch（p.15–16）

```java
try {
    // 正常的程式碼，可能丟出 exception
} catch (ExceptionType e) {
    // exception 發生時才執行的處理
}
```

- `try`：把**可能出錯**的程式碼包起來。
- `catch (型別 名稱)`：括號像方法參數，`e` 就是被丟出來的那個 exception 物件。
- p.16 把讀數字、相除、印結果整段包進 `try`，用 `catch (Exception e)` 接住所有 exception，再 `System.err.println(e)` 印出。
- `System.err` 是標準錯誤串流（Week 3 p.23 的三個標準串流之一），Eclipse 會用紅字顯示。它和 `System.out` 是兩條不同的串流，同時使用時，印出的順序可能和程式順序不一樣。所以本篇的範例統一用 `System.out`。

### 1. 課堂圖解：business logic 與 error handling

老師畫了兩張表：

- **business logic 表 = `try` block**：放程式**正常要做的事**（讀數字、計算、印結果）。
- **error object**：business logic 中途出錯時，Java 會**產生一個 exception 物件**丟出來，就是表裡的那個 error object。
- **error handling 表 = `catch` block**：依照 error object 的**型別**，找到**對應的**處理方式（corresponding error handling）。

```text
┌──────────── try：business logic ────────────┐
│ 讀第一個數字                                  │
│ 讀第二個數字  ── 輸入 abc ──→ [InputMismatchException 物件] ──┐
│ div(num1, num2) ── 除數 0 ──→ [ArithmeticException 物件] ──┐  │
│ 印出結果                                      │              │  │
└───────────────────────────────────────────────┘              │  │
                                                               │  │
┌──────────── catch：error handling ──────────┐              │  │
│ catch (ArithmeticException e)    { 處理除以 0 } ←────────────┘  │
│ catch (InputMismatchException e) { 處理輸入錯 } ←───────────────┘
└───────────────────────────────────────────────┘
```

每一種 error object 都有自己對應的 catch。

**error object 是什麼**

- 它是一個**真的物件**：exception class 的 instance，和 `new` 出來的其他物件一樣放在 heap 上（第 5 節）。
- 物件裡帶著三樣資訊：
  - **型別**：哪一種錯，例如 `ArithmeticException`。
  - **訊息**：`e.getMessage()`，例如 `/ by zero`。
  - **發生位置**：stack trace，用 `e.printStackTrace()` 印出。
- `catch (ArithmeticException e)` 裡的 **`e` 是指向這個物件的 reference**，寫法跟方法參數一樣。

**怎麼對應到正確的 catch**：Java 拿 error object 的型別**由上往下**比對 catch，執行**第一個型別符合的**，其他跳過，再從最後一個 catch 後面繼續。「符合」包括子類別，見下一小節。

### 2. 對照 p.7 繼承圖：為什麼一個 `catch (Exception e)` 全部接得住

老師接著把 p.7 的繼承圖和 p.16 的程式碼連在一起。p.16 只寫了**一個** `catch (Exception e)`，卻能接住 try 裡所有可能的錯誤，原因是：**catch 會接住指定型別，以及它底下所有的子類別。**

p.16 的 try 裡可能出現兩種 error object，在圖上往上找：

```text
InputMismatchException ─┐
                        ├─→ RuntimeException ─→ Exception ─→ Throwable
ArithmeticException ────┘                          ↑
                                          catch (Exception e) 在這一層
```

兩個都在 `Exception` 底下，所以都接得住。這是 Week 6 的 polymorphism：父類別型別的變數 `e`，可以指向子類別的物件。

**實際執行 p.16**（補上 p.11 的 `div`）：

| 輸入 | 印出 | 說明 |
|---|---|---|
| `10 2` | `5` | 沒有 exception，跳過 catch |
| `10 0` | `java.lang.ArithmeticException: / by zero` | 類別名稱 + 訊息 |
| `10 abc` | `java.util.InputMismatchException` | 這個 exception **沒有訊息**，所以只印出類別名稱 |

`System.err.println(e)` 會自動呼叫 `e.toString()`，格式是「完整類別名稱: 訊息」，所以看得出是哪一種 error object。

**用繼承圖判斷 catch 能接住什麼**

| catch 寫法 | 在圖上的位置 | 能接住 |
|---|---|---|
| `catch (ArithmeticException e)` | 最下層 | 只有除以 0 |
| `catch (RuntimeException e)` | 第三層 | 底下全部：除以 0、null、索引、轉型、輸入格式 |
| `catch (Exception e)` | 第二層左邊 | `RuntimeException` 和 `IOException` 兩邊都接得住 |
| `catch (Throwable e)` | 最上層 | 連 `Error` 都接，但**不建議** |

- **右邊的 `Error` 接不住**：`OutOfMemoryError` 不在 `Exception` 底下，`catch (Exception e)` 接不到它。這也符合設計：Error 一般程式本來就不處理。
- **不能 catch 不可能發生的 checked exception**：try 裡只有 `10 / 0`，卻寫 `catch (IOException e)`，會編譯錯誤：

```text
error: exception IOException is never thrown in body of corresponding try statement
```

`IOException` 是 checked，編譯器知道 try 裡沒有程式碼會丟出它。unchecked 的不會被這樣檢查。

### 3. p.16（一個大 catch）與 p.23（分開 catch）

| | p.16：`catch (Exception e)` | p.23：一種錯一個 catch |
|---|---|---|
| 在圖上 | 接在**上層** | 接在**最下層** |
| 優點 | 寫法短，什麼錯都接得住 | 每種錯有對應的處理（上面的課堂圖解） |
| 缺點 | 所有錯都同樣處理，分不出是哪一種 | 要一個一個寫 |

兩種混用時，順序要**由下往上**：圖上越下層（子類別）的 catch 寫在越前面，`catch (Exception e)` 放最後（第 12 節）。

### 4. 課堂實驗：把 p.16 的 try…catch 註解掉

老師用 p.16 的程式碼，把 try…catch 註解掉，比較兩者的差別。下面兩個版本的 `main` 最後都多加了一行 `System.out.println("end of main");`，用來看程式有沒有跑完（這行是自己加的，講義沒有）。

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

**註解掉之後照樣編譯通過**：這裡可能發生的 `ArithmeticException`、`InputMismatchException` 都是 **unchecked**，編譯器不強迫處理。如果 try 裡是 `FileReader` 這種 checked exception，註解掉就會編譯失敗（第 6 節）。

**三種輸入的實際結果**

| 輸入 | 有 try…catch | 沒有 try…catch |
|---|---|---|
| `10 2` | `5` → `end of main` | **完全一樣**：`5` → `end of main` |
| `10 0` | `java.lang.ArithmeticException: / by zero` → `end of main` | 印出完整 stack trace，**沒有** `end of main` |
| `10 abc` | `java.util.InputMismatchException` → `end of main` | 印出完整 stack trace，**沒有** `end of main` |
| exit code | 三種都是 `0`（正常結束） | 出錯時是 `1`（異常終止） |

沒出錯時兩個版本沒有任何差別，try…catch 只在 exception 發生時才有作用。

**沒有 try…catch，輸入 `10 0`**

```text
Exception in thread "main" java.lang.ArithmeticException: / by zero
	at Test.div(Test.java:4)
	at Test.main(Test.java:13)
```

1. `div()` 裡的 `a / b` 產生 `ArithmeticException` 物件。
2. `div()` 沒有 catch，exception 傳回呼叫它的 `main`（第 13 行）。
3. `main` 也沒有 catch，exception 傳出 `main`。
4. 沒有人接住，JVM 印出 stack trace，**程式異常終止**。`System.out.println(result)` 和 `end of main` 都沒有執行（p.13）。

**沒有 try…catch，輸入 `10 abc`**

```text
Exception in thread "main" java.util.InputMismatchException
	at java.base/java.util.Scanner.throwFor(Scanner.java:977)
	at java.base/java.util.Scanner.next(Scanner.java:1632)
	at java.base/java.util.Scanner.nextInt(Scanner.java:2297)
	at java.base/java.util.Scanner.nextInt(Scanner.java:2251)
	at Test.main(Test.java:12)
```

exception 是在 **Java 內建的 Scanner 裡面**產生的，所以 stack trace 比較長。

> **看 stack trace 的技巧**：開頭是 `java.base/` 的行是 JDK 內部的程式碼，可以跳過。往下找**第一行是自己的 class**，這裡是 `Test.main(Test.java:12)`，也就是 `int num2 = input.nextInt();`，問題就出在這一行。

**有 try…catch，同樣輸入 `10 0`**

```text
java.lang.ArithmeticException: / by zero
end of main
```

1. exception 一樣在 `div()` 產生，傳回 `main`。
2. `catch (Exception e)` 的型別符合，接住了。
3. catch 裡 `System.err.println(e)` 只印出一行「類別名稱: 訊息」，不是整段 stack trace。
4. 從 catch 後面繼續往下，印出 `end of main`，**程式正常結束**。

try 裡的 `System.out.println(result)` 還是被跳過（termination model，第 10 節），但 try…catch **外面**的程式碼可以繼續執行。

**整理**

| | 有 try…catch | 沒有 try…catch |
|---|---|---|
| 能不能編譯 | 能 | 能（因為是 unchecked） |
| 沒出錯時 | 正常 | 正常，兩版一樣 |
| 出錯時誰處理 | 自己寫的 catch | 沒人處理，交給 JVM |
| 印出什麼 | catch 裡決定印什麼 | JVM 印出完整 stack trace |
| 出錯之後 | 繼續執行 catch 後面的程式 | **立刻終止**，後面都不執行 |
| 結束方式 | 正常結束（exit code 0） | 異常終止（exit code 1） |

**重點**：exception 一樣會發生，差別只在**發生之後有沒有人接住**。有接住，程式可以決定怎麼處理再繼續（p.13 的「回到正常」）；沒接住，程式就直接崩潰。

## 10. 執行流程與 Termination Model（p.17、p.27）

| 情況 | 流程 |
|---|---|
| `try` 裡**沒有** exception | 跑完 `try` → **跳過所有 catch** → 執行 `finally`（若有）→ 繼續後面的程式 |
| `try` 裡**有** exception，且有 catch 型別符合 | `try` **立刻中止**（剩下的行不執行）→ 跳到**第一個型別符合的** catch → `finally` → 繼續後面的程式 |
| 有 exception，但**沒有** catch 符合 | 執行 `finally` → exception 繼續往呼叫者傳；一直沒人接，程式就異常終止 |

**Termination model**（p.27）：Java 處理完 exception 後，**不會回到出錯的那一行（throw point）繼續**，因為那個 `try` block 已經結束了。`try` 裡宣告的 local variables 也跟著消失，在 catch 和 try 後面都用不到。

用 [`DivideDemo.java`](../src/DivideDemo.java) 驗證，三種輸入的結果：

| 輸入 | 輸出 | 說明 |
|---|---|---|
| `10 2` | `result = 5` → `finally: scanner closed` → `program continues` | 沒有 exception，跳過 catch |
| `10 0` | `ArithmeticException: second number must not be 0` → `finally…` → `program continues` | `result = …` 那行被跳過（termination model） |
| `10 abc` | `InputMismatchException: please enter integers` → `finally…` → `program continues` | `nextInt()` 讀到不是整數 |

三種情況最後都有印出 `program continues`，代表 exception 處理完之後，程式**沒有中止**。

## 11. throw 與 throws（p.18–21）

### throw：自己丟出 exception

方法發現不合理的狀況時，可以主動丟出 exception 物件：

```java
public static int div(int a, int b) {
    if (b == 0) {
        throw new ArithmeticException("second number must not be 0");
    }
    return a / b;
}
```

- `throw` 後面接**一個 exception 物件**，通常當場 `new`。
- 執行到 `throw`，方法立刻結束（後面的 `return` 不會跑），exception 交給**最近的**、型別符合的 catch。
- 括號裡的字串就是之後 `e.getMessage()` 會拿到的訊息。

### throws：在方法標頭宣告「我可能丟出這個」

講義 p.21 寫 `throw new Exception()`，但沒有處理。`Exception` 是 **checked**，所以照講義寫會編譯失敗：

```text
error: unreported exception Exception; must be caught or declared to be thrown
            throw new Exception();
            ^
```

改法：方法標頭加 `throws`，呼叫者就必須用 try…catch 處理：

```java
public static int div(int a, int b) throws Exception {
    if (b == 0)
        throw new Exception("divide by zero");
    else
        return a / b;
}
```

或是像本篇範例改丟 unchecked 的 `ArithmeticException`，就不必加 `throws`。

### 兩個字的比較

| | `throw` | `throws` |
|---|---|---|
| 寫在哪裡 | 方法**裡面**，是一個敘述 | 方法**標頭**，參數括號後面 |
| 後面接 | 一個 exception **物件**：`throw new X(...)` | exception **類別名稱**，可以列多個：`throws X, Y` |
| 作用 | 現在就丟出 | 告訴呼叫者「可能會丟，你要處理」 |

## 12. Multiple catch blocks（p.22–23）

一個 `try` 後面可以接好幾個 catch，各自處理不同型別：

```java
try {
    ...
} catch (ArithmeticException e) {       // 除以 0
    ...
} catch (InputMismatchException e) {    // 輸入不是整數（java.util）
    ...
}
```

- 由上往下比對，**只會執行第一個型別符合的** catch。
- **子類別要放在父類別前面**。如果先寫 `catch (Exception e)`，它會先把所有 exception 接走，後面的 catch 永遠不會執行，編譯器會直接報錯：

```text
error: exception ArithmeticException has already been caught
        } catch (ArithmeticException e) {
          ^
```

`FileCheck.java` 也是這個順序：`FileNotFoundException`（子類別）在前，`IOException`（父類別）在後。

> 補充（Java 7 起）：處理方式相同時可以合併：`catch (ArithmeticException | InputMismatchException e)`。

## 13. finally（p.24–25）

問題：`try` 裡打開了檔案、資料庫連線、網路連線，中途發生 exception，`close()` 那行就被跳過，資源一直沒有釋放。

`finally` 解決這個問題：

- 可有可無，放在**最後一個 catch 後面**。
- **不管有沒有 exception，都會執行**。
- 用途：放釋放資源的程式碼。

```java
try {
    // 取得資源、使用資源
} catch (...) {
    ...
} finally {
    // 釋放資源：close()
}
```

`DivideDemo` 把 `input.close()` 放在 `finally`，三種輸入都印出了 `finally: scanner closed`。

> 補充：Java 7 起有 try-with-resources，`try (Scanner sc = new Scanner(System.in)) { ... }` 結束時會自動 close，不用自己寫 finally。

## 14. Rethrowing（p.26）

catch 接到 exception 後，如果這一層**沒辦法完整處理**，可以先做一部分（例如記錄 log），再用 `throw e;` 丟給外層：

```java
static void update(AgeDemo p, int age) throws InvalidAgeException {
    try {
        p.setAge(age);
    } catch (InvalidAgeException e) {
        System.out.println("update(): logging -> " + e.getMessage());
        throw e;                       // 交給外層的 try 處理
    }
}
```

重新丟出的 exception 會由**外層**的 try…catch 接住（[`AgeDemo.java`](../src/AgeDemo.java) 的 `main`）。

## 15. User-Defined Exception（p.28–30）

Java 內建的 exception 不夠用時，可以自己定義：

- exception 也是**物件**，所以自訂 exception 就是寫一個 class。
- 一般繼承 `Exception`，成為 **checked** exception。繼承 `RuntimeException` 的話，就是 unchecked。
- 加一個收 `String` 的 constructor，用 `super(message)` 把訊息交給父類別（Week 6 的 `super(...)`），之後 `getMessage()` 就拿得到。

[`InvalidAgeException.java`](../src/InvalidAgeException.java)，對應 p.5「使用者輸入負的年齡」：

```java
public class InvalidAgeException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidAgeException(String message) {
        super(message);
    }
}
```

使用（[`AgeDemo.java`](../src/AgeDemo.java)）：

```java
public void setAge(int age) throws InvalidAgeException {   // checked → 要 throws
    if (age < 0) {
        throw new InvalidAgeException("age cannot be negative: " + age);
    }
    this.age = age;
}
```

執行結果（先設 27，再設 -5）：

```text
age set to 27
update(): logging -> age cannot be negative: -5
main(): caught -> age cannot be negative: -5
```

第二行是 `update()` 記錄後重新丟出，第三行是 `main` 接住。

> `serialVersionUID`：`Exception` 實作了 `Serializable`，沒寫這行時 Eclipse 會跳黃色警告。程式照樣能跑，加上這行就不會再有警告。課堂範例沒寫也沒關係。

## 16. 補充：exception 物件的常用方法

| 方法 | 回傳 | 以 `10 / 0` 為例 |
|---|---|---|
| `e.getMessage()` | 只有訊息 | `/ by zero` |
| `e.toString()`（`println(e)` 會自動呼叫） | 類別名稱 + 訊息 | `java.lang.ArithmeticException: / by zero` |
| `e.printStackTrace()` | 無，直接把完整 stack trace 印到 `System.err` | 同第 7 節的格式 |

## 17. 講義語法更正

講義有幾處是 C++ 的說法，或是程式碼照抄會編譯失敗：

| 頁 | 講義 | Java 實際情況 |
|---|---|---|
| 全份 | 程式碼裡的引號是彎引號 `“ ”` | 照貼會編譯錯誤（illegal character），要用直引號 `" "` |
| p.11 | `input.netxInt()` | 拼錯，應為 `nextInt()`；這本身就是第 1 節的 compile-time error |
| p.11–12 | class 叫 `Test`，stack trace 卻是 `Example.div` | 同一個例子的兩個版本，看懂格式就好 |
| p.16、p.23 | 呼叫 `div(...)` | 這兩頁的程式碼沒有定義 `div`，要沿用 p.11 的方法 |
| p.17–18 | 可以丟出字串、任何型別或條件運算式 | 這是 C++。Java **只能丟 `Throwable` 的物件**；`throw "error";` 會編譯錯誤：`String cannot be converted to Throwable` |
| p.18–20 | `DivideByZeroException`、`NotConnectedException` | JDK 沒有這兩個類別，要像第 15 節那樣自己定義；除以 0 的內建 exception 是 `ArithmeticException` |
| p.19、p.26 | 沒接住會呼叫 terminate → abort | C++ 說法。Java 是把 exception 往呼叫者傳，傳出 `main` 還沒人接，就印出 stack trace 並結束 |
| p.21 | `throw new Exception()` | 少分號；`Exception` 是 checked，`div` 必須加 `throws Exception`（第 11 節） |
| p.29–30 | `if (...) throw x;` 直接寫在 class 裡 | 敘述必須寫在方法裡；`UserErr` 是 checked，那個方法也要 `throws UserErr` |
| p.10 | 把 `IOError` 列在 unchecked exceptions | 它屬於 `Error`，不是 `Exception`（但確實不受編譯器檢查） |
| p.10 | `ClassCastException` 例子是把 Double 轉成 String | 變數型別是 `Double` 時會直接編譯錯誤；要用 `Object` 型別的變數才會在執行時發生（第 6 節） |
| p.14 | 「在錯誤發生前抓到」 | 實際上是 exception **丟出後**才接住，讓程式不至於直接崩潰 |

## 18. 檢查清單

- [ ] 先分辨錯誤種類：編譯不過 → syntax；跑到一半中止 → runtime（exception）；跑完但答案錯 → logic
- [ ] 看懂 stack trace：最上面的 `at` 是出錯位置
- [ ] 呼叫會丟 **checked** exception 的方法 → try…catch，或在方法標頭 `throws`
- [ ] 多個 catch：**子類別在前、父類別在後**
- [ ] `try` 裡出錯那行後面的程式不會執行，處理完也不會回去
- [ ] 要釋放的資源（Scanner、檔案）放在 `finally` 或用 try-with-resources
- [ ] `throw` 在方法裡丟物件；`throws` 在標頭宣告類別
- [ ] 自訂 exception：`extends Exception`，constructor 寫 `super(message)`
