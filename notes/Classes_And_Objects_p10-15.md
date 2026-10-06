# Classes and Objects：p.10–15 學習筆記

教材：`5_Classes_And_Objects.pdf`，第 10–15 頁。

本文以英文保留 Java 技術名詞，搭配中文解釋。先前說明中的「引用」就是 **reference**。

核心概念：**class 定義型別，`new` 建立 object，reference variable 保存指向 object 的 reference。**

## p.10 — Data Abstraction

定義 `Circle` class，就是建立一種自己的 data type，用來描述圓的資料與功能。例如，圓可以有 `radius` field，也可以有計算面積的 method。

```java
Circle aCircle;
Circle bCircle;
```

這兩行只宣告兩個 reference variables，尚未建立 Circle objects。可以想成先準備兩個能記錄「圓在哪裡」的欄位。

Data abstraction 讓我們透過 `Circle` 這個型別及其提供的操作來使用圓，將具體的資料表示與實作細節留在 class 內。

## p.11 — Objects

Object 是 class 的 **instance**。`Circle` 如同設計圖，依照它建立的每一個圓都是一個 object。

- **State**：object 在某個時間點的 field values，例如 `radius` 是 `5`。
- **Methods**：object 提供的操作，例如計算面積或改變半徑。
- **Identity**：每個 object 都有自己的身分；即使兩個 objects 的 state 相同，也可以是不同的 objects。

Java 程式可以透過 method calls 讓 objects 互動。

### 補充：Message Passing（老師提到的「pass the message」）

老師提到的「pass the message」，在 p.10–15 中最直接對應 p.11：objects 透過已定義的 methods 互動。這個概念稱為 **message passing**；在這裡，可以理解成透過 **method call** 請某個 object 執行操作。

假設 `Circle` 定義了 `getArea()` instance method，而且 `aCircle` 指向一個有效的 Circle object：

```java
aCircle.getArea();
```

這就像向該 object 傳送「計算面積」的 message。

| 組成 | 這個例子的意思 |
|---|---|
| **Receiver** | `aCircle` 指向的 object |
| **Method** | `getArea()`，要求執行的操作 |
| **Arguments** | 此例沒有；需要時可在括號內傳入資料 |

例如，若 `Circle` 提供 `setRadius(double radius)` method：

```java
aCircle.setRadius(5.0);
```

這次的 receiver 仍是 `aCircle` 指向的 object，method 是 `setRadius`，argument 是 `5.0`，意思是請該 object 將半徑設為 `5.0`。Method 宣告中的 `radius` 則稱為 **parameter**。

此處的 message passing 指 object 之間透過 method calls 互動，不代表一定涉及網路訊息或非同步傳送。

投影片將 name 作為識別 object 的方式，這是簡化說法。更精確地說，variable 有 name，object 有 identity；同一個 object 可以被多個 reference variables 指向，見 p.15。

## p.12 — 建立 Object 的兩個步驟

```java
Circle c1;          // 1. 宣告 reference variable
c1 = new Circle();  // 2. 建立 object，將其 reference 指派給 c1
```

也可以合併成一行：

```java
Circle c1 = new Circle();
```

| 程式碼 | 意義 |
|---|---|
| `Circle` | variable 的 type |
| `c1` | reference variable 的 name |
| `new` | 建立新 object 的 operator |
| `Circle()` | 此處呼叫無參數 constructor，初始化新 object |
| `=` | 將右側的 reference 指派給左側 variable |

**Constructor** 在建立 object 時執行，用來初始化 object。Constructor 的名稱與 class 相同，而且沒有 return type，連 `void` 都不寫。

`new` 是 operator，不是 method。`new Circle()` 這個 expression 建立 object，並產生該 object 的 reference。

### 補充：Default Constructor 與 No-Argument Constructor

老師強調的規則可以寫成：

> If you don't declare any constructors, Java provides a default constructor.
> If you declare a constructor, Java no longer provides the default constructor automatically.

對一般 Java class 而言，**只有在完全沒有宣告任何 constructor 時，Java compiler 才會自動提供 default constructor**。只要自行宣告任何 constructor，compiler 就不會再自動補上一個。

**情況一：完全沒有宣告 constructor。**

```java
class Student {
}

// 在可存取 Student 的程式碼中：
Student s = new Student(); // OK：使用 compiler 提供的 default constructor
```

**情況二：自行宣告帶 parameter 的 constructor。**

```java
class Student {
    String name;

    Student(String name) {
        this.name = name;
    }
}

// 在可存取 Student 的程式碼中：
Student s1 = new Student("Alice"); // OK
Student s2 = new Student();        // Compile-time error：沒有 no-argument constructor
```

`this.name` 是目前 object 的 field，右側的 `name` 是 constructor 的 parameter。

**情況三：想同時支援兩種建立方式，就自行宣告兩個 constructors。**

```java
class Student {
    String name;

    Student() {
        this.name = "Unknown";
    }

    Student(String name) {
        this.name = name;
    }
}

// 在可存取 Student 的程式碼中：
Student s1 = new Student();        // OK：name 是 "Unknown"
Student s2 = new Student("Alice"); // OK：name 是 "Alice"
```

同一個 class 宣告不同 parameter lists 的 constructors，稱為 **constructor overloading**。以上三個範例是各自獨立的情況；建立 objects 的 statements 應放在 method 等允許執行 statements 的位置。

| 技術名詞 | 意義 |
|---|---|
| **Default constructor** | Class 未宣告任何 constructor 時，由 compiler 自動提供，沒有 parameters |
| **No-argument constructor** | 沒有 parameters 的 constructor；可以自行宣告，default constructor 也屬於這一類 |
| **Parameterized constructor** | 帶有 parameters 的 constructor，例如 `Student(String name)` |

**自己寫的 `Student()` 應稱為 no-argument constructor；default constructor 特指 compiler 自動提供的 constructor。**

### 課堂範例：Rectangle、Constructor Overloading 與 this

這個 Rectangle 範例示範兩個重點：**constructor overloading**，以及使用 **`this`** 區分同名的 field 和 parameter。

以下是修正拼字後的完整 class，可存成 `Rectangle.java`：

```java
public class Rectangle {
    public float length;
    public float width;

    // Constructor：兩個 parameters
    public Rectangle(float l, float w) {
        length = l;
        width = w;
    }

    // No-argument constructor
    public Rectangle() {
        length = 0.0f;
        width = 0.0f;
    }

    // Constructor：一個 parameter
    public Rectangle(float width) {
        length = 0.0f;
        this.width = width; // 將 parameter 的值存入目前 object 的 field
    }
}
```

原始筆記有兩個需要修正的地方：`float 1` 應為 `float l`（小寫 L），並將對應的 assignment 改成 `length = l;`；`lenght` 應為 `length`。數字 `1` 不能作為 parameter name。

**Constructor overloading：同一個 class 有多個 constructors，其 parameter lists 不同。** Compiler 根據 arguments 的數量及型別，選擇適用的 constructor。

| Constructor | 使用方式 | 初始化後的 state |
|---|---|---|
| `Rectangle(float l, float w)` | `new Rectangle(3.0f, 2.0f)` | `length = 3.0f`、`width = 2.0f` |
| `Rectangle()` | `new Rectangle()` | `length = 0.0f`、`width = 0.0f` |
| `Rectangle(float width)` | `new Rectangle(2.0f)` | `length = 0.0f`、`width = 2.0f` |

這些 object creation expressions 可以放在 `main` 或其他 method 內。數字後面的 `f` 表示這是 `float` literal，例如 `2.0f`；不加 `f` 的 `2.0` 預設是 `double`。

**`this` 是目前 object 的 reference。** 在以下 assignment 中：

```java
this.width = width;
```

- `this.width`：目前 object 的 field。
- 右側的 `width`：目前 constructor 收到的 parameter。
- 整行意思：把 parameter 的值存入目前 object 的 field。

當 parameter 與 field 同名時，parameter 會 **shadow** field 的名稱，因此需要 `this.width` 明確指定 field。如果寫成 `width = width;`，只會把 parameter 的值指派回 parameter，object 的 field 不會因此更新。

原註解「this 可以呼叫自己」在這裡應改為「this 指向目前的 object」。`this.width` 是 field access；在 constructor 中使用 `this(...)`，才是呼叫同一個 class 的另一個 constructor，稱為 **constructor chaining**。

這裡的 `Rectangle()` 是自行宣告的 **no-argument constructor**，並非 compiler 自動提供的 **default constructor**。若移除它但保留其他 constructors，`new Rectangle()` 就會造成 compile-time error。

## p.13 — Null Reference

`null` 表示 reference 沒有指向任何 object。

```java
Circle aCircle = null;
```

此時有一個 reference variable，但沒有因為這行程式而建立 Circle object。若透過 `null` reference 存取 instance field 或呼叫 instance method，會發生 `NullPointerException`。

投影片說只宣告 reference variable 時，初始值是 `null`；這需要區分 variable 的種類：

| 宣告位置 | 未明確賦值時的狀態 |
|---|---|
| Class 中的 reference field | 預設值為 `null` |
| Method 內的 local variable | 沒有可直接讀取的預設值，使用前必須先賦值 |

```java
class Example {
    Circle fieldCircle; // reference field：預設為 null

    void demo() {
        Circle localCircle; // local variable：尚未初始化
        // System.out.println(localCircle); // compile-time error
        localCircle = new Circle();
        System.out.println(localCircle);    // 已賦值，可以使用
    }
}
```

## p.14 — 使用 new 建立 Objects

```java
aCircle = new Circle();
bCircle = new Circle();
```

每次執行 `new Circle()` 都會建立新的 object，因此這裡有兩個不同的 objects：

```text
aCircle ──→ Circle object A
bCircle ──→ Circle object B
```

即使 A 與 B 的 `radius` 相同，它們仍然有不同的 identity。

連結到 allocation、heap 和 stack：

- **Allocation**：為資料配置 memory space。
- **Heap**：以 Java 的概念模型來說，objects 與 arrays 的空間配置在這裡。
- **Stack**：每次 method call 都有對應的 stack frame，包含該次呼叫的 local variables 等資料。

如果 `aCircle`、`bCircle` 是 method 內的 local variables，可以用下圖理解：

```text
Stack                         Heap
aCircle ────────────────────→ Circle object A
bCircle ────────────────────→ Circle object B
```

這是學習用的概念模型，JVM 實作可能進行最佳化。另外，reference 也可以存在 object 的 field 中，並不是所有 reference variables 都在 stack。

## p.15 — Reference Assignment

接續上一頁執行：

```java
bCircle = aCircle;
```

這行將 `aCircle` 保存的 reference 複製給 `bCircle`。它沒有呼叫 `new`，也沒有複製 Circle object。

```text
Before assignment:
aCircle ──→ Circle object A
bCircle ──→ Circle object B

After assignment:
aCircle ──→ Circle object A ←── bCircle
            Circle object B（失去原本來自 bCircle 的 reference）
```

現在兩個 reference variables 指向同一個 object，這種情況稱為 **aliasing**。

假設 `Circle` 有可存取的 `radius` field：

```java
aCircle.radius = 10;
System.out.println(bCircle.radius); // 10
System.out.println(aCircle == bCircle); // true
```

兩個 variables 都指向 A，所以透過其中一個 reference 修改 A 的 state，透過另一個也會看到改變。對 reference types 使用 `==`，是在比較它們是否指向同一個 object（或是否都為 `null`）。

如果 B 已無法透過任何有效的 reference 路徑存取，它就符合 garbage collection 的條件。**Garbage Collector（GC）**不保證立即回收它。

若之後只重新指派其中一個 variable：

```java
bCircle = new Circle();
```

`bCircle` 會指向新的 object C，`aCircle` 仍指向 A。重新指派 reference variable，與修改 object 的 state，是不同的操作。

## 重點比較

| 程式碼 | 發生什麼事 |
|---|---|
| `Circle aCircle;` | 宣告 reference variable |
| `aCircle = null;` | 讓 variable 不指向任何 object |
| `aCircle = new Circle();` | 建立新 object，將 reference 存入 variable |
| `bCircle = aCircle;` | 複製 reference，讓兩個 variables 指向同一個 object |
| `aCircle.radius = 10;` | 修改所指向 object 的 state |

**`new Circle()` 建立 object；`bCircle = aCircle` 複製 reference。**
