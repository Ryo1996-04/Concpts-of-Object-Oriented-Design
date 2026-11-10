# CSYE6200 筆記：Enum Types

> 個人學習筆記，課堂內容以自己的話重新整理。本文不含投影片原文或圖片，標註的頁碼只用來對照課程講義；所有範例程式都是自己寫的。

- 課程：CSYE6200（Fall 2026）
- 單元：ArrayList; Abstract classes; Interfaces; Enums（`8_Abstract classes And Interfaces.pdf`，本篇是 p.25–31 的 Enum）
- 日期：2026-11-10（本週從 Enum 開始；同一份講義的 p.1–24 是上週內容，見 [Week 8 筆記](../../week-08/notes/arraylist-abstract-interfaces.zh-TW.md)）
- 可執行的範例程式：[`../src/`](../src)（Day、TrafficLight）

## 目錄

1. [課堂開場：紅綠燈](#1-課堂開場紅綠燈)
2. [什麼時候用 Enum（p.25）](#2-什麼時候用-enump25)
3. [宣告語法（p.26–27）](#3-宣告語法p2627)
4. [Enum 其實是一個 class（p.28、p.30）](#4-enum-其實是一個-classp28p30)
5. [課堂範例：Move 與座標系（p.25–27 延伸）](#5-課堂範例move-與座標系p2527-延伸)
6. [values() 走訪所有常數（p.29）](#6-values-走訪所有常數p29)
7. [Enum 搭配 switch（p.31）](#7-enum-搭配-switchp31)
8. [補充：其他內建方法](#8-補充其他內建方法)
9. [補充：完整紅綠燈範例](#9-補充完整紅綠燈範例)
10. [講義語法更正](#10-講義語法更正)
11. [檢查清單](#11-檢查清單)

---

## 1. 課堂開場：紅綠燈

老師用紅綠燈舉例：一個紅綠燈有好幾種「只能從固定選項中挑一個」的狀態。

| 屬性 | 可能的值 | 對應的 enum |
|---|---|---|
| 開／關 | ON、OFF | `Power` |
| 顏色 | RED、YELLOW、GREEN | `LightColor` |
| 方向 | 例如 NORTH_SOUTH、EAST_WEST | `Direction` |

重點：這些值在**寫程式的時候就全部知道**，不會在執行時多出新的顏色。用 `int`（0、1、2）或 `String`（"red"）也能表示，但：

- `int color = 7;` 編譯器不會阻止，7 不是任何顏色。
- `String color = "gren";` 拼錯也能編譯。
- `LightColor color = LightColor.GREN;` 會**編譯錯誤**，錯誤提早被抓到。

> 課堂記錄（待補）：老師實際寫的程式、方向的值、還有哪些屬性：

---

## 2. 什麼時候用 Enum（p.25）

當一個變數只會是**一組固定常數中的某一個**，就適合用 enum。講義的例子：一週七天、一年十二個月。共通點是所有可能值在 compile time 就確定。

## 3. 宣告語法（p.26–27）

用 `enum` 關鍵字宣告，大括號內列出常數，常數之間用逗號分開，慣例全部大寫。

```java
public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}
```

使用時寫 `型別名.常數`：

```java
Day today = Day.TUESDAY;
```

## 4. Enum 其實是一個 class（p.28、p.30）

- `enum` 宣告出來的是一種特殊的 class（enum type），每個常數都是這個 class 的一個物件，而且**只有這幾個**。
- 所有 enum 都自動繼承 `java.lang.Enum`。因為 Java 是 single inheritance（Week 6），enum **不能再 `extends` 別的 class**，但可以 `implements` interface。
- Enum 的 body 可以有 fields、methods、constructor。
- Constructor 不能是 `public` 或 `protected`。講義寫「package-private 或 private」；實際上不寫修飾詞時，Java 規格會把 enum constructor 當成 `private`。**不能自己 `new`**，Java 會在一開始替每個常數各呼叫一次 constructor。

```java
enum LightColor {
    RED(30), YELLOW(5), GREEN(25);   // 每個常數各呼叫一次 constructor

    private final int seconds;

    LightColor(int seconds) {        // 預設就是 private
        this.seconds = seconds;
    }

    int getSeconds() {
        return seconds;
    }
}

// LightColor c = new LightColor(10);  // 編譯錯誤：enum 不能 new
```

常數後面接其他成員時，最後一個常數要用 **分號** 結束（上例 `GREEN(25);`）。

### 補充：private constructor（私有建構子）是什麼？

`private constructor` 表示**只有這個類別內部**可以呼叫這個建構子，外部不能直接用它 `new` 物件。

一般的 `public constructor` 允許外部建立物件：

```java
public class Student {
    public Student() {}
}

// 其他類別中
Student s = new Student(); // ✅ 可以
```

改成 `private`：

```java
public class Student {
    private Student() {}
}

// 其他類別中
Student s = new Student(); // ❌ 編譯錯誤：建構子是 private
```

為什麼要這樣設計？常見有兩種用途：

**1. 禁止建立物件：工具類別**

如果類別只提供 `static` 方法，就不需要建立物件。

```java
public class MathUtil {
    private MathUtil() {}

    public static int square(int n) {
        return n * n;
    }
}
```

使用時直接用類別名稱呼叫：

```java
int result = MathUtil.square(5); // ✅ 25
// new MathUtil();               // ❌ 不允許
```

Week 5 的 `Math` 類別也是這樣：`Math.sqrt()`、`Math.PI` 都直接用類別名稱，不需要 `new Math()`。

**2. 由類別控制物件的建立**

外部不能直接 `new`，但可以透過類別提供的 `static` 方法取得物件：

```java
public class Student {
    private String name;

    private Student(String name) {
        this.name = name;
    }

    public static Student create(String name) {
        return new Student(name); // ✅ 類別內部可以呼叫
    }
}
```

外部使用：

```java
Student s = Student.create("Claire"); // ✅
```

這樣可以在 `create()` 裡統一做資料檢查，或決定是否重複使用既有物件。**Singleton（單例模式）**也常用私有建構子來限制物件的建立。

注意：`private` 限制的是**建構子的存取**，不是物件能不能被外部使用。拿到物件後，外部照樣可以呼叫它的 `public` 方法。

**連回 Enum**：enum 就是第 2 種用途的極端版。Constructor 一定是 private，而且連 `create()` 這種方法都不需要，所有物件就是大括號裡列出的那幾個常數，由 Java 自動建立。所以 `LightColor` 永遠只有 RED、YELLOW、GREEN 三個物件，這也是可以用 `==` 比較的原因。

## 5. 課堂範例：Move 與座標系（p.25–27 延伸）

老師接著用座標系解釋：上下左右四個方向是固定的一組值（p.25 的條件），而且每個方向都對應 x 或 y 的增加／減少。p.27 的 `Days` 只有名字；`Move` 再往前一步，讓**每個常數帶自己的資料**（p.28：enum body 可以有 fields 與 methods）。

### 1. 方向與 (x, y) 變化量

| 常數 | (x, y) 變化 | 意思 |
|---|---|---|
| `Left(-10, 0)` | x 減 10 | 往左 |
| `Right(10, 0)` | x 加 10 | 往右 |
| `Up(0, 10)` | y 加 10 | 往上 |
| `Down(0, -10)` | y 減 10 | 往下 |

x、y 是「每一步的變化量」，不是位置。目前座標加上變化量，就是走一步後的新位置。

> 這是數學座標系（y 往上變大）。螢幕或遊戲的座標通常 y 往下變大，那時 Up 要改成 `(0, -10)`。

### 2. 課堂上的版本（老師可能還沒寫完）

```java
public enum Move {
    Left(-10,0), Right(10,0), Up(0,10), Down(0,-10);
    public int x, y;
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    private Move () { this.x = 0; this.y = 0; }
    public void moveUp() { }
}
```

實際編譯會出現 4 個錯誤，四個常數各一個：

```text
constructor Move in enum Move cannot be applied to given types;
  required: no arguments
  found:    int,int
```

| 問題 | 說明 |
|---|---|
| 缺 `(int, int)` constructor（編譯錯誤） | `Left(-10, 0)` 就是「用 -10、0 呼叫 constructor」，但只有不收參數的 `Move()`；而且它把 x、y 都設成 0，傳進來的值會被丟掉 |
| `public int x, y;` | 每個常數全程式只有一個物件，任何人寫 `Move.Left.x = 99;` 就會影響所有用到 Left 的地方。改成 `private final` + getter（Week 5 Encapsulation） |
| `moveUp()` 是空的 | 已經有 `Up` 常數，通常不需要；可能是老師還沒寫完，之後看老師怎麼補 |
| 命名 | 慣例是全大寫 `LEFT`；`Left` 也能編譯，只是不符合慣例 |

### 3. 修正版：[`Move.java`](../src/Move.java)

```java
public enum Move {
    Left(-10, 0), Right(10, 0), Up(0, 10), Down(0, -10);

    private final int x, y;          // 每一步在 x、y 方向的變化量

    private Move(int x, int y) {     // Left(-10, 0) 會呼叫這個 constructor
        this.x = x;
        this.y = y;
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }

    public static void main(String[] args) {
        int px = 0, py = 0;           // 起點 (0, 0)
        Move[] path = { Move.Up, Move.Up, Move.Right, Move.Down, Move.Left, Move.Left };
        for (Move m : path) {
            px += m.getX();
            py += m.getY();
            System.out.println(m + " -> (" + px + ", " + py + ")");
        }
    }
}
```

從 (0, 0) 出發的執行結果：

```text
Up -> (0, 10)
Up -> (0, 20)
Right -> (10, 20)
Down -> (10, 10)
Left -> (0, 10)
Left -> (-10, 10)
```

### 4. 延伸：保留無參數 constructor

想留下 `Move()` 也可以，兩個 constructor 並存就是 **overloading**（Week 5）。但必須有一個不帶參數的常數去用它，例如加一個 `Stay`，呼叫 `Move()` 並把 x、y 設成 0。

> 課堂記錄（待補）：老師最後怎麼完成 `moveUp()`、有沒有搭配 Point／位置類別：

---

## 6. values() 走訪所有常數（p.29）

編譯器會自動加一個 `static` 方法 `values()`，回傳**依宣告順序**排列的陣列，可以直接用 enhanced for loop（Week 3）走訪：

```java
for (Day d : Day.values()) {
    System.out.println(d);
}
```

輸出 MONDAY 到 SUNDAY 共七行。`println` 印出的是常數名稱。

## 7. Enum 搭配 switch（p.31）

`switch` 可以直接用 enum 變數；`case` 後面只寫常數名稱，**不用**加 `Day.`。

```java
switch (day) {
    case MONDAY:
    case TUESDAY:
    case WEDNESDAY:
    case THURSDAY:
    case FRIDAY:
        System.out.println(day + " is a weekday");
        break;
    case SATURDAY:
    case SUNDAY:
        System.out.println(day + " is the weekend");
        break;
}
```

多個 `case` 疊在一起沒有 `break`，會一路往下執行（fall-through），所以星期一到五共用同一段輸出。

## 8. 補充：其他內建方法

講義只提到 `values()`，以下是常一起用到的：

| 方法 | 作用 | 範例結果 |
|---|---|---|
| `values()` | 所有常數的陣列 | `LightColor.values().length` → `3` |
| `name()` / `toString()` | 常數名稱字串 | `"YELLOW"` |
| `ordinal()` | 宣告順序，從 0 開始 | `YELLOW.ordinal()` → `1` |
| `valueOf(String)` | 由名稱找常數；找不到丟 `IllegalArgumentException` | `LightColor.valueOf("YELLOW")` |

比較 enum 可以直接用 `==`，因為每個常數只有一個物件（對照 Week 3：String 內容比較要用 `equals`）。

## 9. 補充：完整紅綠燈範例

[`TrafficLight.java`](../src/TrafficLight.java) 把第 1 節的三個屬性寫成三個 enum：

- `Power`：ON／OFF；燈關著時 `change()` 不會換色。
- `LightColor`：每個顏色存秒數，`next()` 依 GREEN → YELLOW → RED → GREEN 循環。
- `Direction`：NORTH_SOUTH／EAST_WEST，建立物件時決定。

執行結果：

```text
NORTH_SOUTH OFF RED (30s)
NORTH_SOUTH OFF RED (30s)
NORTH_SOUTH ON GREEN (25s)
NORTH_SOUTH ON YELLOW (5s)
NORTH_SOUTH ON RED (30s)
YELLOW ordinal=1
true
3
```

第二行仍是 RED：燈還沒開，`change()` 沒有作用。

## 10. 講義語法更正

- p.27、p.29、p.31 的 `WEDENSDAY` 拼錯，應為 `WEDNESDAY`。照講義拼法寫也能編譯，但之後用 `valueOf("WEDNESDAY")` 會找不到。
- p.31 的 switch 片段結尾多了一個 `}`，是從較長的程式截出來的，單獨使用時要刪掉。

## 11. 檢查清單

- [ ] 值是固定、事先知道的一組選項 → 用 enum，不用 `int` 或 `String` 代號
- [ ] 常數全部大寫，用逗號分開
- [ ] 常數後面還有 fields／methods 時，最後一個常數加分號
- [ ] Constructor 不寫 `public`，也不在外面 `new`
- [ ] 走訪用 `values()`；switch 的 `case` 只寫常數名
- [ ] 比較 enum 用 `==`
