# CSYE6200 筆記：ArrayList、Abstract Classes 與 Interfaces

> 個人學習筆記，課堂內容以自己的話重新整理。本文不含投影片原文或圖片，標註的頁碼只用來對照課程講義；所有範例程式都是自己寫的，錯誤訊息與輸出都是實際用 `javac` / `java` 跑出來的。

- 課程：CSYE6200（Fall 2026）
- 單元：ArrayList; Abstract classes; Interfaces; Enums（`8_Abstract classes And Interfaces.pdf`，本篇是 p.1–24；p.25–31 Enum 在 [Week 9 筆記](../../week-09/notes/enum.zh-TW.md)）
- 日期：2026-11-03 的課（缺課，依講義自學整理，2026-11-10 寫）
- 可執行的範例程式：[`../src/`](../src)（ArrayListDemo；Shape、Circle、Rectangle、ShapeTest；Speaker、Politician、Priest、Lecturer、Teacher、Person、Professor、SpeakerTest）

## 目錄

**Part 1 — ArrayList**

1. [為什麼需要 ArrayList（p.3）](#1-為什麼需要-arraylistp3)
2. [建立 ArrayList（p.4）](#2-建立-arraylistp4)
3. [常用方法（p.5–6）](#3-常用方法p56)
4. [範例追蹤：ArrayListDemo（p.7）](#4-範例追蹤arraylistdemop7)
5. [ArrayList 常見陷阱](#5-arraylist-常見陷阱)

**Part 2 — Abstract Classes**

6. [Abstract class 是什麼（p.8）](#6-abstract-class-是什麼p8)
7. [語法（p.9）](#7-語法p9)
8. [Shape 範例（p.10–12）](#8-shape-範例p1012)
9. [四條規則（p.13）](#9-四條規則p13)

**Part 3 — Interfaces**

10. [Interface 是什麼（p.14–15）](#10-interface-是什麼p1415)
11. [定義與實作 Interface（p.16–19）](#11-定義與實作-interfacep1619)
12. [Interface 繼承 Interface（p.20）](#12-interface-繼承-interfacep20)
13. [同時 extends 與 implements（p.21）](#13-同時-extends-與-implementsp21)
14. [Interfaces 與軟體工程（p.22）](#14-interfaces-與軟體工程p22)
15. [Polymorphism via Interfaces（p.23–24）](#15-polymorphism-via-interfacesp2324)

**整理**

16. [Abstract class 與 Interface 比較](#16-abstract-class-與-interface-比較)
17. [補充：Java 8 之後的 Interface](#17-補充java-8-之後的-interface)
18. [講義語法更正](#18-講義語法更正)
19. [檢查清單](#19-檢查清單)

---

# Part 1 — ArrayList

## 1. 為什麼需要 ArrayList（p.3）

Week 3 的陣列有一個限制：**長度固定**。建立之後不能變長也不能變短，所以一開始就要知道要放幾個元素。

```java
int[] scores = new int[3];   // 永遠只能放 3 個
```

`ArrayList` 是**動態陣列**，可以依需要自動變大：

- 建立時有一個初始大小（capacity）。
- 放超過時，自動換一個更大的內部陣列。
- 移除元素時，內部陣列也可能縮小。

講義也提到：`ArrayList` **extends `AbstractList`** 並且 **implements `List` interface**。這句話正好用到本週後面兩個主題：`AbstractList` 是 abstract class（Part 2），`List` 是 interface（Part 3）。

## 2. 建立 ArrayList（p.4）

`ArrayList` 在 `java.util` package，要先 import（Week 5 的 package／import）：

```java
import java.util.ArrayList;
```

三種 constructor（Week 4 的 constructor overloading）：

| Constructor | 作用 |
|---|---|
| `ArrayList()` | 建立空的 list |
| `ArrayList(Collection c)` | 用另一個集合的元素建立 |
| `ArrayList(int capacity)` | 指定**初始容量**（內部陣列一開始有多大）；放滿會自動變大 |

> **capacity 與 size 不同**：capacity 是內部陣列的大小，size 是**目前實際放了幾個元素**。`new ArrayList<>(100)` 的 size 還是 0。

### 補充：Generics（泛型）

講義的寫法是 `ArrayList al = new ArrayList();`，沒有指定元素型別（叫 raw type），可以編譯，但會出現提醒：

```text
Note: ArrayListDemo.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.
```

現在的寫法是用 `<>` 指定元素型別：

```java
ArrayList<String> al = new ArrayList<>();    // 只能放 String
```

- 放錯型別（例如 `al.add(5)`）會**編譯錯誤**，錯誤提早被抓到。
- `get()` 拿出來就是 `String`，不用轉型。
- `<>` 只能放 **class 型別**，不能放 `int`、`double` 這些 primitive。要存數字時用 Week 5 的 wrapper class：`ArrayList<Integer>`、`ArrayList<Double>`；放入和取出時 Java 會自動轉換（autoboxing／unboxing）。

## 3. 常用方法（p.5–6）

| 方法 | 作用 | 注意 |
|---|---|---|
| `add(element)` | 加到**最後面** | 回傳 `true` |
| `add(index, element)` | 插到指定位置，後面的元素往後移 | index 可以是 `0` 到 `size()`；超出丟 `IndexOutOfBoundsException` |
| `get(index)` | 取得指定位置的元素 | index 必須是 `0` 到 `size() - 1` |
| `set(index, element)` | 把指定位置換成新元素 | 補充，講義沒列 |
| `remove(index)` | 移除指定**位置**，後面的元素往前移 | 回傳被移除的元素 |
| `remove(object)` | 移除第一個**等於**它的元素 | 見第 5 節陷阱 |
| `contains(object)` | 有沒有這個元素 | 用 `equals()` 比較（Week 3：String 比內容要用 `equals`） |
| `indexOf(object)` | 第一次出現的位置 | 找不到回傳 `-1` |
| `size()` | 目前元素個數 | |
| `clear()` | 全部清空 | |
| `toArray()` | 轉成一般陣列 | |

### 陣列與 ArrayList 對照

| | 陣列（Week 3） | ArrayList |
|---|---|---|
| 長度 | 固定 | 自動變大／變小 |
| 長度怎麼取 | `arr.length`（欄位，沒有括號） | `al.size()`（方法，有括號） |
| 讀取 | `arr[i]` | `al.get(i)` |
| 修改 | `arr[i] = x` | `al.set(i, x)` |
| 能放 primitive 嗎 | 可以：`int[]` | 不行，要用 wrapper：`ArrayList<Integer>` |
| 印出內容 | `System.out.println(arr)` 印出位址，要用迴圈 | `System.out.println(al)` 直接印出 `[C, A, E]` |
| enhanced for | 可以 | 可以 |

## 4. 範例追蹤：ArrayListDemo（p.7）

[`ArrayListDemo.java`](../src/ArrayListDemo.java) 是 p.7 改成 generics 的版本。每一步之後 list 的內容：

| 步驟 | 執行 | list 內容 | size |
|---|---|---|---|
| 1 | `new ArrayList<>()` | `[]` | 0 |
| 2 | 依序 `add` C、A、E、B、D、F | `[C, A, E, B, D, F]` | 6 |
| 3 | `add(1, "A2")`：插到 index 1，後面往後移 | `[C, A2, A, E, B, D, F]` | 7 |
| 4 | `remove("F")`：移除**值**是 F 的元素 | `[C, A2, A, E, B, D]` | 6 |
| 5 | `remove(2)`：移除 **index 2**（是 `A`） | `[C, A2, E, B, D]` | 5 |

實際輸出（前 5 行和講義版相同）：

```text
Initial size of al: 0
Size of al after additions: 7
Contents of al: [C, A2, A, E, B, D, F]
Size of al after deletions: 5
Contents of al: [C, A2, E, B, D]
get(0): C
contains("B"): true
indexOf("Z"): -1
after set(0, "Z"): [Z, A2, E, B, D]
Z A2 E B D 
```

後 5 行是我補的 `get`、`contains`、`indexOf`、`set`、enhanced for。

## 5. ArrayList 常見陷阱

**1. `remove(int)` 和 `remove(Object)` 是兩個不同的方法（overloading，Week 5）**

`ArrayList<String>` 不會混淆：`remove(2)` 是 index，`remove("F")` 是值。但 `ArrayList<Integer>` 就要小心：

```java
ArrayList<Integer> nums = new ArrayList<>();
nums.add(10); nums.add(20); nums.add(30);

nums.remove(1);                     // 移除 index 1 → [10, 30]
nums.remove(Integer.valueOf(10));   // 移除值 10   → [30]
```

`1` 是 `int`，Java 會優先選 `remove(int index)`。想移除「值」就要傳 `Integer` 物件。

**2. index 超出範圍**：`get(size())` 會丟 `IndexOutOfBoundsException`。合法的最後一個 index 是 `size() - 1`，和陣列的 `length - 1` 一樣（Week 3）。

**3. 一邊用 enhanced for 走訪、一邊 `remove`**：會丟 `ConcurrentModificationException`。要刪除時改用一般 for 迴圈從後往前，或用 `removeIf`。

---

# Part 2 — Abstract Classes

## 6. Abstract class 是什麼（p.8）

從 `final` 講起：

- `final` class：**不能被繼承**。
- abstract class 剛好相反：**一定要被繼承才有用**。

abstract class 是一個**概念上的類別**（conceptual class），描述一群類別的共同點，但本身太抽象，不能直接建立物件。

> 例子：「形狀」本身沒有面積公式，「圓形」、「長方形」才有。所以 `Shape` 適合寫成 abstract class，讓 `Circle`、`Rectangle` 繼承。

重點：

- **不能 instantiate**：不能 `new` abstract class。
- 它提供一群相關類別的**共同根（common root）**。

## 7. 語法（p.9）

```java
abstract class ClassName {
    abstract Type methodName1();      // abstract method：只有標頭，沒有 { }，以分號結尾

    Type method2() {                  // 一般方法：有內容
        // method body
    }
}
```

規則：

- class 裡**有一個以上** abstract method，這個 class 就**必須**宣告成 `abstract`。
- abstract method 要由**子類別**實作，實作了全部 abstract method 的子類別叫 **concrete class**。
- **不能有 abstract constructor**，也**不能有 abstract static method**。
- 補充：`abstract` 和 `final` 不能一起用（一個要被繼承、一個禁止繼承）：

```text
error: illegal combination of modifiers: abstract and final
```

## 8. Shape 範例（p.10–12）

繼承關係（p.10）：

```text
        Shape（abstract）
        /             \
   Circle          Rectangle
```

### 對照 Week 6 的 Shape

Week 6 的 `Shape` 是一般 class，`area()` 只好回傳 `0`。但「形狀的面積是 0」其實沒有意義，而且 `new Shape()` 也能建立，得到一個面積 0 的怪物件。

改成 abstract 之後（[`Shape.java`](../src/Shape.java)）：

```java
public abstract class Shape {
    public abstract double area();          // 沒有 body：不知道怎麼算

    public void describe() {                // 一般方法：子類別直接繼承
        System.out.println(getClass().getSimpleName() + " area = " + area());
    }
}
```

- `area()` 沒有 body，強迫每個子類別自己寫。
- abstract class **也可以有一般方法**：`describe()` 寫在父類別，所有子類別共用。它呼叫的 `area()` 會是各子類別自己的版本（Week 6 overriding）。

### `new Shape()` 可以嗎？（p.11）

```java
Shape s = new Shape();      // ❌
```

```text
error: Shape is abstract; cannot be instantiated
```

但**宣告成 Shape 型別的變數**完全可以，只要指向子類別物件：

```java
Shape s = new Circle();     // ✅ 父類別型別的變數指向子類別物件（Week 6 polymorphism）
```

### 子類別（p.12）

[`Circle.java`](../src/Circle.java)、[`Rectangle.java`](../src/Rectangle.java) 都 `extends Shape`，並實作 `area()`。[`ShapeTest.java`](../src/ShapeTest.java)：

```java
Shape[] shapes = { new Circle(), new Circle(2.0), new Rectangle(3.0, 4.0) };
for (Shape s : shapes) {
    s.describe();
}
```

```text
Circle area = 3.1415
Circle area = 12.566
Rectangle area = 12.0
```

（`PI` 照講義用 `3.1415`，所以半徑 2 的面積是 `12.566`。正式程式應該用 `Math.PI`。）

## 9. 四條規則（p.13）

| 情況 | 能不能 `new` |
|---|---|
| class 有一個以上 abstract method | 自動是 abstract，**不能** |
| class 宣告成 `abstract`，即使**一個 abstract method 都沒有** | **不能** |
| 子類別實作了父類別**全部** abstract method | **可以**（concrete class） |
| 子類別**沒有**實作全部 abstract method | 子類別自己也是 abstract，**不能** |

最後一條：如果子類別沒寫 `area()`，又沒宣告成 abstract，會編譯錯誤：

```text
error: Square is not abstract and does not override abstract method area() in Shape
```

改法二選一：實作 `area()`，或把 `Square` 也宣告成 `abstract`。

---

# Part 3 — Interfaces

## 10. Interface 是什麼（p.14–15）

interface 是和 abstract class 類似的**概念上的東西**，但更「純」：

- 只能有**常數**（`final` 變數）和 **abstract method**（沒有實作）。這是和 abstract class 的差別。
- 用在**很多類別要有同樣的一組方法**時；每個類別各自實作。

### 為什麼需要：非正式的 multiple inheritance

Week 6 講過 Java 是 **single inheritance**：一個 class 只能 `extends` **一個**父類別。

但一個 class **可以 implements 很多個 interface**。interface 只規定「要有哪些方法」，實作程式碼由 class 自己寫，所以不會有「兩個父類別都有同名方法，要用哪一個」的問題。因此 interface 被視為 Java **非正式實現 multiple inheritance** 的方式。

### 編譯器自動加上的修飾詞

| 寫在 interface 裡 | 實際上等於 |
|---|---|
| `int MAX_MINUTES = 60;` | `public static final int MAX_MINUTES = 60;`（常數） |
| `void speak();` | `public abstract void speak();` |

所以 interface 的變數**不能改**（`Speaker.MAX_MINUTES = 90;` 會編譯錯誤），方法**一定是 public**。

## 11. 定義與實作 Interface（p.16–19）

### 關係圖（p.16）

```text
            <<interface>>
               Speaker
               speak()
          /       |       \
   Politician   Priest   Lecturer
    speak()     speak()   speak()
```

三個 class 彼此**沒有繼承關係**，但都「會 speak」。

### 定義（p.17）

[`Speaker.java`](../src/Speaker.java)：

```java
public interface Speaker {
    int MAX_MINUTES = 60;    // 常數
    void speak();            // 只有標頭，沒有 body
}
```

### 實作（p.18–19）

用 `implements`，可以列多個 interface，用逗號分開：

```java
class ClassName implements InterfaceName1, InterfaceName2 { ... }
```

[`Politician.java`](../src/Politician.java)：

```java
public class Politician implements Speaker {
    @Override
    public void speak() {
        System.out.println("Talk politics");
    }
}
```

**實作的方法一定要寫 `public`**。interface 的方法本來就是 public，少寫 `public` 等於縮小存取權限，會編譯錯誤：

```text
error: speak() in Priest cannot implement speak() in Speaker
  attempting to assign weaker access privileges; was public
```

講義 p.19 的三個 class 都有寫 `public void speak()`，是正確的。

## 12. Interface 繼承 Interface（p.20）

interface 也可以用 `extends` 繼承另一個 interface，子 interface 會得到父 interface 的所有成員。

[`Teacher.java`](../src/Teacher.java)：

```java
public interface Teacher extends Speaker {   // 繼承 speak()
    void grade(String student);              // 再加一個
}
```

實作 `Teacher` 的 class 要寫 `speak()` **和** `grade()` 兩個方法。

> 補充：class 只能 extends 一個 class，但 interface **可以 extends 多個 interface**：`interface C extends A, B { }`。

## 13. 同時 extends 與 implements（p.21）

一個 class 可以**繼承一個父類別**，同時**實作一個以上的 interface**。順序固定：**先 `extends`，後 `implements`**。

[`Professor.java`](../src/Professor.java)：

```java
public class Professor extends Person implements Teacher {
    public Professor(String name) {
        super(name);                              // Week 6：交給父類別 constructor
    }

    @Override
    public void speak() {                         // 來自 Speaker（透過 Teacher）
        System.out.println(name + ": Today we cover interfaces.");
    }

    @Override
    public void grade(String student) {           // 來自 Teacher
        System.out.println(name + " grades " + student);
    }
}
```

`Professor` 同時是 `Person`、`Teacher`、`Speaker`，這就是講義說的「看起來像 multiple inheritance」。

## 14. Interfaces 與軟體工程（p.22）

- interface 和 abstract class 一樣，提供**行為的範本**：規定其他 class **必須**有哪些方法。
- 把**設計**（要有哪些方法）和**實作**（方法怎麼寫）分開：設計者先定好 interface，不同的程式設計師照著它寫各自的 class，大家的方法名稱和參數就會一致。
- 傳遞的是**方法的描述**，不是實作。
- Java 只允許單一父類別，interface 讓不同的 class 可以「混搭」多種能力。

> 可以把 interface 想成一份**合約**：「只要你 implements 我，就保證有這些方法」。使用的人只要知道合約，不必知道是哪個 class。這和 Week 5 Encapsulation 的「interface 與 implementation 分開」是同一個想法。

## 15. Polymorphism via Interfaces（p.23–24）

**interface 名稱可以當成變數的型別**：

```java
Speaker current;
```

`current` 可以指向**任何**實作了 `Speaker` 的 class 的物件。呼叫 `current.speak()` 時，執行哪一個版本，**取決於它當下指向的物件**，不是變數的型別。

[`SpeakerTest.java`](../src/SpeakerTest.java)：

```java
Speaker guest = new Lecturer();
guest.speak();                  // Lecturer 的版本
guest = new Politician();
guest.speak();                  // Politician 的版本

Speaker[] speakers = { new Politician(), new Priest(), new Lecturer(), new Professor("Dr. Lee") };
for (Speaker current : speakers) {
    current.speak();
}
```

實際輸出：

```text
Talks Object Oriented Design and Programming!
Talk politics
Talk politics
Preaches
Talks Object Oriented Design and Programming!
Dr. Lee: Today we cover interfaces.
Dr. Lee grades Claire
Dr. Lee is a Speaker: true
Speaker.MAX_MINUTES = 60
```

- 同一行 `current.speak()`，四次印出四種結果。
- `Politician`、`Priest`、`Lecturer`、`Professor` 彼此沒有共同的父類別（`Professor` 的父類別是 `Person`），但都能放進同一個 `Speaker[]`，因為它們都 implements `Speaker`。
- `p instanceof Speaker` 是 `true`：`Professor` 透過 `Teacher` 間接實作了 `Speaker`。
- 用 `Speaker` 型別的變數**只能呼叫 `Speaker` 有的方法**：`guest.grade(...)` 會編譯錯誤，要用 `Teacher` 型別的變數才行。

---

# 整理

## 16. Abstract class 與 Interface 比較

| | Abstract class | Interface |
|---|---|---|
| 關鍵字 | `abstract class` | `interface` |
| 子類別怎麼用 | `extends`，**只能一個** | `implements`，**可以多個** |
| 能不能 `new` | 不能 | 不能 |
| 方法 | abstract method **和**一般方法都可以 | 只有 abstract method（Java 8 之後見第 17 節） |
| 變數 | 任何種類：instance、static、private… | 只有常數（自動 `public static final`） |
| constructor | **可以有**，子類別用 `super(...)` 呼叫 | **沒有** |
| 方法的存取權限 | 任意 | 自動 `public` |
| 適合 | 一群**本質相同**的類別，共用部分程式碼：`Circle`、`Rectangle` **是一種** `Shape`（is-a） | 一群**不相關**的類別，有同樣的**能力**：政治家、神父、講師都**會** speak（can-do） |
| 本週例子 | `Shape`、JDK 的 `AbstractList` | `Speaker`、`Teacher`、JDK 的 `List` |

> 記法：**abstract class = 半成品的父類別**（有些方法寫好了、有些留給子類別）；**interface = 一份只有方法清單的合約**。

## 17. 補充：Java 8 之後的 Interface

講義寫 interface「只能有常數和 abstract method」，這是 Java 7 以前的規則。Java 8 之後 interface 也可以有：

- **`default` method**：有 body，實作的 class 可以直接用，也可以 override。
- **`static` method**：用 interface 名稱呼叫。
- Java 9 起還可以有 `private` method（給 default method 共用）。

```java
public interface Speaker {
    void speak();

    default void greet() {               // Java 8+
        System.out.println("Hello!");
        speak();
    }
}
```

考試以講義為準：interface = 常數 + abstract method。

## 18. 講義語法更正

| 頁 | 講義 | 正確 |
|---|---|---|
| 全份 | 程式碼裡的引號是彎引號 `“ ”` | 照貼會編譯錯誤，要用直引號 `" "` |
| p.5–7 | 方法參數寫 `Object`，範例用 `ArrayList al = new ArrayList();`（raw type） | 能編譯但會出現 unchecked 提醒；現在用 `ArrayList<String>`（第 2 節） |
| p.6 | `toArray()` 會在「指定的陣列是 null」時丟 `NullPointerException` | 這句是講另一個版本 `toArray(T[] a)`；沒有參數的 `toArray()` 不會這樣 |
| p.7 | `al.add(1, "A2"); S` | 行尾多了一個 `S`，是打字錯誤 |
| p.12 | `public Circle extends Shape {`、`public Rectangle extends Shape {` | 少了 `class`，應為 `public class Circle extends Shape {`；照抄的錯誤是 `class, interface, annotation type, enum, record, method or field expected` |
| p.15 | interface 的方法「只能是 abstract classes」 | 應為 **abstract methods** |
| p.17 | 方法宣告「只有 method body」 | 剛好相反：只有**標頭**（signature），**沒有** body |
| p.24 | `new lecturer()`、`new politician()` | class 名稱大小寫要一致：`new Lecturer()`、`new Politician()` |

## 19. 檢查清單

**ArrayList**

- [ ] `import java.util.ArrayList;`
- [ ] 用 `ArrayList<型別>`；存數字用 `Integer`、`Double`
- [ ] 個數用 `size()`，不是 `length`
- [ ] `get(i)` / `set(i, x)` 的 i 在 `0` 到 `size() - 1`
- [ ] `ArrayList<Integer>` 移除值要寫 `remove(Integer.valueOf(x))`

**Abstract class**

- [ ] 有 abstract method → class 要加 `abstract`
- [ ] abstract method 沒有 `{ }`，以分號結尾
- [ ] 不能 `new` abstract class，但可以用它當變數型別：`Shape s = new Circle();`
- [ ] 子類別要實作全部 abstract method，否則自己也要宣告 abstract

**Interface**

- [ ] `implements`，可以多個，用逗號分開；`extends` 寫在 `implements` 前面
- [ ] 實作的方法要寫 `public`
- [ ] interface 的變數是常數，不能改
- [ ] interface 型別的變數只能呼叫 interface 裡有的方法
