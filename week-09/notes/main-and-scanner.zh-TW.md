# CSYE6200 筆記：main 方法、args 與 Scanner（課堂問答）

> 個人學習筆記，課堂內容以自己的話重新整理。本文不含投影片原文或圖片；所有範例程式都是自己寫的。

- 課程：CSYE6200（Fall 2026）
- 日期：2026-11-10（Enum 課堂中的提問）
- 相關週次：Week 3 Scanner（`3_Arrays_String_InputOutput.pdf` p.25–27）、Week 5 Command-Line Arguments（`6_CLA_Packages_JavaDoc.pdf`）
- 範例：[`../src/ReadName.java`](../src/ReadName.java)、[`../src/DigitSum.java`](../src/DigitSum.java)

## 目錄

1. [main 是程式的入口](#1-main-是程式的入口)
2. [每個字的意思](#2-每個字的意思)
3. [怎麼 run](#3-怎麼-run)
4. [平常什麼時候寫 main](#4-平常什麼時候寫-main)
5. [為什麼用 Scanner 時一定會看到 main](#5-為什麼用-scanner-時一定會看到-main)
6. [args 與 Scanner 的差別](#6-args-與-scanner-的差別)
7. [期中考重點](#7-期中考重點)
8. [期中 cheat sheet 最後一題：DigitSum](#8-期中-cheat-sheet-最後一題digitsum)

---

## 1. main 是程式的入口

執行 `java Move` 時，JVM 會到 `Move` 這個 class 裡找一個**名稱與寫法完全符合**的方法，從它的第一行開始執行：

```java
public static void main(String[] args)
```

找不到就無法執行，會出現 `Error: Main method not found in class Move`。

## 2. 每個字的意思

| 部分 | 為什麼要這樣寫 |
|---|---|
| `public` | JVM 在 class 外面，要能呼叫到它 |
| `static` | 程式剛開始時還沒有任何物件；`static` 屬於 class 本身，不用 `new` 就能呼叫（同 Week 5「為什麼 `sum()` 要加 `static`」） |
| `void` | 不回傳值給 JVM |
| `main` | 固定名稱，JVM 只認這個名字 |
| `String[] args` | 命令列參數（Week 5）。`java Move 10 20` → `args[0]` 是 `"10"`、`args[1]` 是 `"20"`；沒給參數時是長度 0 的空陣列 |

## 3. 怎麼 run

**命令列**（從 repo 根目錄）：

```sh
javac -encoding UTF-8 -d week-09/bin week-09/src/*.java
java -cp week-09/bin Move
```

1. `javac` 把 `.java` 編譯成 `.class`。
2. `java Move` 啟動 JVM，載入 `Move` class，呼叫 `Move.main(...)`。
3. `main` 裡的程式由上往下執行，跑完最後一行程式就結束。

**Eclipse**：在檔案上按右鍵，選 Run As → Java Application，Eclipse 會替你做上面兩步。選單裡沒有這個選項，通常表示那個 class 沒有 `main`。

## 4. 平常什麼時候寫 main

- **一支程式需要一個入口**：`main` 放在負責啟動程式的 class，不是每個 class 都要寫。Week 6 的 `Shape`、`Circle`、`Rectangle` 都沒有 `main`，只有 `ShapeTest` 有。
- **測試或示範自己寫的 class**：寫完 `Circle` 之後，另外寫 `CircleTest`（或 `Demo`），在它的 `main` 裡 `new` 物件、呼叫方法、印出結果。課堂例子和作業大多是這種用法。
- **放在哪裡**：`Move.java` 把 `main` 直接寫在 enum 裡，是為了一個檔案就能跑；enum 和一般 class 一樣可以有 `main`。比較正式的寫法是另寫一個 `MoveDemo` 放 `main`，讓 `Move` 只負責「方向」。
- **JUnit 測試**：測試方法加 `@Test`，由 JUnit 執行，不需要自己寫 `main`。

> 補充：JDK 25 可以省略成 `void main()`，但課堂與考試用完整寫法 `public static void main(String[] args)`。

## 5. 為什麼用 Scanner 時一定會看到 main

Scanner 的程式碼一定要寫在某個 method 裡才能執行。「打開程式 → 請使用者輸入 → 印出結果」這種小程式，唯一會被 JVM 自動執行的地方就是 `main`，所以 Scanner 範例幾乎都長這樣（Week 3 p.25–27）：

```java
import java.util.Scanner;              // Scanner 在 java.util，要 import

public class ReadName {
    public static void main(String[] args) {     // 程式入口
        Scanner sc = new Scanner(System.in);     // System.in = 鍵盤輸入
        System.out.print("Name: ");
        String name = sc.nextLine();             // 程式停在這裡，等使用者輸入
        System.out.println("Hello, " + name);
        sc.close();
    }
}
```

`main` 和 Scanner 是兩件事：

- `main` 決定**程式從哪裡開始**。
- Scanner 決定**資料從哪裡讀進來**。

## 6. args 與 Scanner 的差別

`main` 括號裡的 `String[] args` 也是一種輸入，但和 Scanner 完全不同：

| | `String[] args`（Week 5） | `Scanner`（Week 3） |
|---|---|---|
| 什麼時候給資料 | **啟動程式時**寫在指令後面：`java Sum 10 9` | 程式**執行到一半**停下來，等使用者打字 |
| 資料型別 | 一律是 `String`；數字要 `Integer.parseInt(args[0])` | 可直接讀成數字：`nextInt()`、`nextDouble()` |
| 要不要 import | 不用 | 要 `import java.util.Scanner;` |
| 沒給資料時 | `args.length` 是 0；直接讀 `args[0]` 會丟 `ArrayIndexOutOfBoundsException` | 程式一直等輸入 |

用 Scanner 的程式通常完全不用 `args`，但 `main` 的寫法是固定的，所以 `String[] args` 還是要寫。

## 7. 期中考重點

**變數分類**（期中複習時錯過的題型：漏列 `main` 的 `args`）

- `args` 是 **local variable**：它是 `main` 的參數，宣告在 method 括號裡，只在 `main` 裡有效。
- `Scanner sc = new Scanner(System.in);` 寫在 `main` 裡，`sc` 也是 local；它指向的 Scanner 物件在 heap 上（Week 3 reference）。
- 規則：看變數**在哪裡宣告**，不看在哪裡被使用。

**Scanner 常見陷阱**（Week 3 p.27）

- `nextInt()` 後面直接接 `nextLine()`：`nextInt()` 只讀走數字，換行留在後面，接著的 `nextLine()` 一讀到換行就結束，拿到空字串。解法：中間多呼叫一次 `sc.nextLine();` 把換行吃掉。
- 忘了 import：出現 `cannot find symbol` … `class Scanner`。

實際測試：輸入 `5`、換行、`Claire`，第一次 `nextLine()` 拿到的是 `[]`（空字串），`Claire` 留在下一行沒讀。

## 8. 期中 cheat sheet 最後一題：DigitSum

期中考的寫程式題：讀入一個整數，印出各位數字的和。cheat sheet 第 16 節最後的模板（[`../src/DigitSum.java`](../src/DigitSum.java)）：

```java
import java.util.Scanner;

public class DigitSum {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Math.abs(sc.nextInt());    // -984 → 984
        int sum = 0;
        while (n > 0) {
            sum += n % 10;                 // 取個位
            n /= 10;                       // 去個位
        }
        System.out.println(sum);           // 567→18, -984→21
    }
}
```

### 為什麼這題會出現 main

題目要「讀入」一個數字，也就是程式執行時由使用者輸入，所以用 Scanner（第 6 節）。而這是一支完整、可以直接執行的程式，必須有入口，所以要寫 `public static void main(String[] args)`（第 1 節）。`args` 在這題完全沒用到，但 `main` 的寫法固定，不能省略。

### 逐行拆解

| 程式 | 作用 |
|---|---|
| `import java.util.Scanner;` | 寫在 class 外面、檔案最上方；沒有它，`Scanner` 會 `cannot find symbol` |
| `Scanner sc = new Scanner(System.in);` | 建立從鍵盤讀資料的 Scanner；`sc` 是 `main` 的 local variable |
| `sc.nextInt()` | 程式停下來等使用者輸入，讀成 `int` |
| `Math.abs(...)` | 取絕對值，負數也能處理（Week 5 Math 類別的 static 方法，不用 `new Math()`） |
| `n % 10` | 取個位數：`567 % 10` → `7` |
| `n /= 10` | 整數除法去掉個位：`567 / 10` → `56` |
| `while (n > 0)` | 每輪拿掉一位，`n` 變成 0 時停止 |

### 追蹤：輸入 567

| 輪次 | `n % 10` | `sum` | `n /= 10` 後 |
|---|---|---|---|
| 開始 | — | 0 | 567 |
| 1 | 7 | 7 | 56 |
| 2 | 6 | 13 | 5 |
| 3 | 5 | 18 | 0 → 停止 |

輸出 `18`。實際執行驗證：`567` → `18`、`-984` → `21`、`0` → `0`（迴圈一次都不跑）、`7` → `7`。
