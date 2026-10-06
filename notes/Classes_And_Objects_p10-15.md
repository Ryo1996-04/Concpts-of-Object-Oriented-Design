# Classes and Objects：p.10–28 學習筆記

教材：`5_Classes_And_Objects.pdf`，第 10–28 頁。

閱讀順序：p.10–18 為 object 與 reference 基礎；p.19–28 為 constructors 與 this。課堂 Rectangle 與 Person 補充範例另保留在本文。

程式區塊若只有 statements，應放入 method（例如 `main`）內執行；不同頁的同名 classes 是獨立範例，不能全部貼進同一個檔案。標示「補充／修正」的內容用來釐清投影片的簡化說法或舊式 API。

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

## p.16 — Accessing Class Members：Dot Operator 與 Field Access

投影片使用 **dot separator** 這個名稱，課堂也常稱為 **dot operator**，指的就是句點 `.`。在這些例子中，它用來透過 object reference 存取 members，包括 fields 和 methods。

```java
Circle aCircle = new Circle();
aCircle.x = 2.0;
aCircle.y = 2.0;
aCircle.r = 1.0;
```

- `aCircle`：reference variable，指向一個 Circle object。
- `x`、`y`：表示圓心座標的 fields。
- `r`：表示 radius 的 field。
- `aCircle.r`：**field access**，存取該 object 的 `r` field。
- `aCircle.r = 1.0;`：**assignment to a field**，把 `1.0` 存入該 field，改變 object 的 state。

投影片的語法可以更精確地整理為：

```java
reference.fieldName
reference.methodName(arguments)
```

這些存取必須符合 member 的 access control；例如，其他 class 不能任意直接存取 `private` field。此例假設 Circle 的 members 可由呼叫端存取。

## p.17 — Accessing Class Members：Method Invocation 與 Message Passing

```java
Circle aCircle = new Circle();
double area;
aCircle.r = 1.0;
area = aCircle.area();
```

**Method invocation** 就是 **method call**，表示呼叫 method。最後一行會先呼叫 object 的 `area()` method，再把 return value 指派給 local variable `area`。

| 組成 | 在 `area = aCircle.area();` 中的角色 |
|---|---|
| Receiver | `aCircle` 指向的 Circle object |
| Method | `area()`，計算面積 |
| Arguments | 沒有；`()` 是空的 argument list |
| Return value | Method 算出的面積 |
| Assignment target | 左側的 local variable `area` |

左邊的 `area` 是 variable name，右邊的 `area()` 是 method invocation，兩者角色不同。

這頁直接以「sent 'message' to aCircle」標註 method call，因此 **p.17 是 message passing 的直接程式範例**；p.11 則先介紹 objects 透過 methods 互動的概念。此處 message passing 表示請 receiver 執行操作，不代表一定涉及網路或非同步訊息。

## p.18 — Using Circle Class：把步驟串起來

以下保留投影片的主要流程，將換行整理成可讀的 Java 程式碼。此程式需要另外提供教材的 Circle class，包含可存取的 `x`、`y`、`r` fields，以及 `area()`、`circumference()` methods。

```java
class MyMain {
    public static void main(String[] args) {
        Circle aCircle;           // 1. 宣告 reference variable
        aCircle = new Circle();   // 2. 建立 object，儲存 reference

        aCircle.x = 10;           // 3. Field assignment：設定圓心及半徑
        aCircle.y = 20;
        aCircle.r = 5;

        double area = aCircle.area();              // 4. Method invocation
        double circumf = aCircle.circumference();  //    儲存 return values

        System.out.println("Radius=" + aCircle.r + " Area=" + area);
        System.out.println("Radius=" + aCircle.r
                + " Circumference=" + circumf);
    }
}
```

執行順序是：**宣告 reference variable → 建立 object → 設定 fields → 呼叫 methods → 印出結果**。

`aCircle.x`、`aCircle.y`、`aCircle.r` 都是 field access；加上 `= 值` 就是在做 assignment。`aCircle.area()` 與 `aCircle.circumference()` 才是 method invocation。

投影片顯示的輸出為：

```text
Radius=5.0 Area=78.5
Radius=5.0 Circumference =31.400000000000002
```

這些數值對應以 `3.14` 近似 π 的計算：area 為 `3.14 × 5 × 5`，circumference 為 `2 × 3.14 × 5`。尾端的 `000000000000002` 是 floating-point representation 的精度現象；實際輸出取決於 Circle methods 的實作。

### 聽課名詞補充：Invocation 與 Dot Operator

先前聽到的「invocation」對應 **method invocation**；「datodator」依發音及這幾頁的內容推測，很可能是 **dot operator**。投影片 p.16 寫的是 **dot separator**。這是依教材與發音做的判斷，並非老師原話的逐字確認。

| 寫法 | 技術名詞 | 意思 |
|---|---|---|
| `aCircle.r` | Field access | 存取 radius 資料 |
| `aCircle.r = 5;` | Assignment to a field | 把 radius 設為 `5` |
| `aCircle.area()` | Method invocation / method call | 呼叫計算面積的 method |
| 上述寫法中的 `.` | Dot operator / dot separator | 用來存取 member |

**在這些例子中，field name 後沒有 `()`；method invocation 有 `()`。Field access 與 method invocation 都會使用 `.`。**

## p.19 — Constructing Objects

```java
Person person = new Person();
```

從右邊開始理解：`new Person()` 建立 Person instance，執行相應 constructor，產生 reference；`=` 把 reference 存入名為 `person` 的 variable。左側 `Person` 是 variable 的 declared type。

Class 定義新的 reference type，因此除了 `int` 等 primitive types，也可以宣告 `Person`、`Circle` 型別的 variables。`Person person;` 只宣告 variable；真正的 object creation 在 `new Person()`。

補充：此處示範 variable 與 object 使用相同 type；未來學到 inheritance 時，也可以把 subclass object 的 reference 存入相容的 superclass 或 interface variable。

## p.20 — Person Example：Fields 與 Getters / Setters

教材 Person 的 fields 是 `name`、`age`；先前課堂範例用的是 `fName`，概念相同。

```java
class Person {
    String name;
    int age;

    String getName() { return name; }
    void setName(String n) { name = n; }
    int getAge() { return age; }
    void setAge(int a) { age = a; }
}
```

| 宣告 | Return type | 功能 |
|---|---|---|
| `getName()` | `String` | 讀取姓名 |
| `setName(String n)` | `void` | 接收 parameter `n`，更新姓名 |
| `getAge()` | `int` | 讀取年齡 |
| `setAge(int a)` | `void` | 接收 parameter `a`，更新年齡 |

`void` 表示不回傳 value，不代表沒有執行效果。Setter 會改變 object 的 state。此頁未宣告 constructor，因此使用 compiler 提供的 default constructor。

補充：沒有 access modifier 的 members 是 package-private，不是 `public`。教材為簡化範例直接宣告 fields；若要透過 methods 控制存取，可將 fields 設為 `private`，這與 **encapsulation** 有關。單純加入 getter/setter，而 fields 仍為 public，並沒有阻止直接存取 fields。

## p.21 — Constructing Person Objects：先建立，再設定

```java
Person p1 = new Person(); // 此時 name = null，age = 0
p1.setName("John");       // name 改為 "John"
p1.setAge(22);            // age 改為 22
```

這三行分成 object creation 和兩次 method invocations。如果忘記呼叫其中一個 setter，對應 field 就保留原本值。

投影片接著問：能不能建立時就有姓名 Smith、年齡 32？可以，透過接下來的 **parameterized constructor**，讓初始化資料由 constructor 接收。

## p.22 — Constructors：辨認規則

Constructor 用來初始化新 object 的 state，其名稱必須與 class name 完全一致，而且沒有 return type。

```java
Person() { }       // Constructor
void Person() { }  // 普通 method：名稱雖相同，但有 return type void
```

上面是語法比較片段，兩者意義不同。教材稱 constructor 為 special method；更精確地說，它是特殊的 constructor declaration，並不是一般可以用 `p.Person()` 重新呼叫的 instance method。

常見 object creation 是 `new Person(...)`。Constructor 也可以透過 `this(...)` 或 `super(...)` 進行 constructor chaining；因此不是所有 constructor invocation 都直接寫 `new`。

## p.23 — Person Constructor：Arguments 如何進入 Fields

```java
class Person {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

```java
Person p2 = new Person("Smith", 32);
```

執行時，argument `"Smith"` 對應 parameter `name`，argument `32` 對應 parameter `age`；接著 assignments 把這兩個值存入新 object 的 fields。Constructor 完成後，`p2` 指向的 object 就具有這些初始值。

此版本只宣告兩個 parameters 的 constructor，因此 `new Person()` 會出現 compile-time error；需要自行加上 no-argument constructor 才能支援它。

## p.24 — Default Constructor：自動提供的條件

一般 class **完全沒有宣告 constructor** 時，compiler 才會提供 default constructor。自行宣告任何 constructor 後，就不再自動補上。

| Class 中的 constructors | `new Person()` | `new Person("Smith", 32)` |
|---|---|---|
| 完全沒宣告 | 可以，使用 default constructor | 不可以 |
| 只有 `Person(String, int)` | 不可以 | 可以 |
| 同時有 `Person()` 與 `Person(String, int)` | 可以 | 可以 |

補充／修正：fields 的 default initialization 是 object creation 的一部分，不是 default constructor 特有的功能。即使自行寫 constructor，fields 也會先取得 default values，再依 initialization 與 constructor 的程式更新。

| Field type | Default value |
|---|---|
| `byte`、`short`、`int`、`long` | 數值 `0` |
| `float`、`double` | `0.0f`、`0.0d` |
| `boolean` | `false` |
| `char` | `'\u0000'`，不是字元 `'0'` |
| Reference types | `null` |

Default constructor 也涉及 superclass constructor invocation，並不代表完全沒有初始化行為；如果 superclass 沒有可存取的 no-argument constructor，隱含呼叫也可能無法編譯。Local variables 不適用上表的 field default values 規則。

## p.25 — Multiple Constructors：Constructor Overloading

```java
public class Circle {
    public double x, y, r;

    public Circle() {
        x = 0.0;
        y = 0.0;
        r = 0.0;
    }

    public Circle(double cx, double cy, double cr) {
        x = cx;
        y = cy;
        r = cr;
    }
}
```

`new Circle()` 建立中心 `(0, 0)`、radius `0` 的 object；`new Circle(2, 3, 5)` 建立中心 `(2, 3)`、radius `5` 的 object。後者的 integer arguments 可以 widening conversion 成 `double`。

Overloading 依 parameter types、數量及排列區分。只改 parameter name 沒用，例如 `Circle(double radius)` 和 `Circle(double size)` 是相同 parameter type list，不能當兩個 overloads。

## p.26 — Using the this Reference：目前的 Receiver

在 instance method 或 constructor 裡，`this` 指向目前正在處理的 object。在沒有同名 local variable 或 parameter 遮蔽時，`age` 常可視為隱含的 `this.age`。

```java
void setAge(int age) {
    this.age = age;
}
```

Parameter `age` **shadows** 同名 field；左邊使用 `this.age` 指定 field，右邊 `age` 指 parameter。如果寫成 `age = age;`，只是在 parameter 上做 self-assignment，field 沒有更新。

Static method 沒有隱含的 receiver，所以不能直接使用 `this`。

## p.27 — this Keyword：每個 Object 各自設定

```java
Person p1 = new Person("Alice", 20);
Person p2 = new Person("Bob", 25);
```

執行第一個 constructor 時，`this` 指向 Alice 的新 object；執行第二個時，`this` 指向 Bob 的新 object。兩次 `this.age = age` 分別更新不同 objects 的 instance fields。

`this` 不是固定指向某個名叫 `p1` 的 variable；即使同一個 object 被多個 references 指向，instance method 裡的 `this` 仍是當次 receiver object。

## p.28 — Cascading Constructors：Constructor Chaining

```java
class Person {
    String name;
    int age;

    Person() {
        this("", 0); // 呼叫同 class 的另一個 constructor
    }

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

執行 `new Person()` 時：先進入 no-argument constructor，再由 `this("", 0)` 委派給兩個 parameters 的 constructor，完成後返回。**這個過程只建立一個 object**，沒有因為呼叫兩個 constructors 就建立兩個 objects。

目的在於集中初始化邏輯，避免多個 constructors 重複寫相同 assignments。這裡 `""` 是 empty String，與 `null` 不同。

| 語法 | 用途 |
|---|---|
| `this.age` | 存取目前 object 的 field |
| `this.getAge()` | 呼叫目前 object 的 instance method |
| `this("", 0)` | 呼叫同 class 的另一個 constructor |

本教材範例把 `this(...)` 放在 constructor 開頭，這是容易理解且可跨版本使用的寫法；不能讓 constructors 形成直接或間接的 recursive invocation。

## 課堂補充 — Person：Default Values、Getters / Setters 與 Constructor

以下兩個版本各自存為 `Person.java` 執行，不能把兩個 `public class Person` 放進同一個檔案。原始貼文的 HTML 空白編碼已還原，`System.***out***.println` 的格式標記也已整理為 Java 正確語法 `System.out.println`。

### 版本一：原始程式與輸出

```java
public class Person {
    public String fName; // [1] Reference field：default value 是 null
    public int age;      // [2] int field：default value 是 0

    // [3] 沒有宣告任何 constructor，compiler 會提供 default constructor。

    public String getFName() {
        return this.fName; // Getter：只回傳目前的 field value
    }

    public void setFName(String fName) {
        this.fName = fName; // Setter：field = parameter
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static void main(String[] args) {
        Person p1 = new Person(); // [4] 建立 object，但未指定姓名、年齡

        // [5] 雖然定義了 setters，這裡並沒有呼叫它們。
        System.out.println("The name of the person is :" + p1.getFName());
        System.out.println("The age of the person is :" + p1.getAge());

        /* 實際輸出：
        The name of the person is :null
        The age of the person is :0
        */
    }
}
```

**為什麼是 `null` 和 `0`？** `fName` 與 `age` 是 instance fields。Object 建立時，fields 先取得各自 type 的 default value；這個程式沒有 field initializers，也沒有自訂 constructor 或 setter calls 去改變它們。因此 getters 回傳 `null` 和 `0`。字串串接時，`null` reference 會被顯示為文字 `null`。

定義 method 不代表已經執行它。只有寫出 `p1.setFName(...)` 或 `p1.setAge(...)` 等 method invocation，setter 的內容才會執行。這個輸出是正常行為，不是 compile-time error 或 exception。

### 版本二：依老師的「Define a Constructor」加入修正

```java
public class Person {
    public String fName;
    public int age;

    // [修正 1] 自行宣告 parameterized constructor。
    // Constructor 名稱與 class 相同，沒有 return type，連 void 都不寫。
    public Person(String fName, int age) {
        this.fName = fName; // 左側是目前 object 的 field，右側是 parameter
        this.age = age;
    }

    // [修正 2] 明確提供 no-argument constructor，保留 new Person() 的用法。
    // 這是自己寫的 constructor，不是 compiler 提供的 default constructor。
    public Person() {
        this.fName = "Unknown";
        this.age = 18; // 18 是這個範例自訂的初始值，不是 Java default value
    }

    public String getFName() {
        return this.fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static void main(String[] args) {
        // [修正 3] Arguments 對應 constructor 的 parameters。
        Person p1 = new Person("Alice", 20);
        System.out.println("The name of the person is :" + p1.getFName());
        System.out.println("The age of the person is :" + p1.getAge());

        // [比較] 呼叫自己宣告的 no-argument constructor。
        Person p2 = new Person();
        System.out.println("The name of the person is :" + p2.getFName());
        System.out.println("The age of the person is :" + p2.getAge());

        // [補充] Object 建立後，仍可以透過 setter 修改 state。
        p1.setAge(21);
        System.out.println("Updated age:" + p1.getAge());

        /* 實際輸出：
        The name of the person is :Alice
        The age of the person is :20
        The name of the person is :Unknown
        The age of the person is :18
        Updated age:21
        */
    }
}
```

此修正版同時展示兩種 constructors，屬於 **constructor overloading**。若只需要 `new Person("Alice", 20)`，可以只保留 parameterized constructor；但此時 `new Person()` 便不能使用，因為 compiler 不會再自動提供 default constructor。

### 修改歷程與原因

1. **觀察原始輸出**：`new Person()` 建立 object，getters 回傳 `null` 與 `0`。
2. **確認原因**：兩個 fields 沒有指定初始值，程式也沒有呼叫 setters，因此保留 default values。Getter 負責讀取，不會自動產生姓名或年齡。
3. **理解老師的修正方向**：「Define a constructor」是自行宣告 constructor，在 object creation 過程中初始化 fields。
4. **加入 parameterized constructor**：用 `new Person("Alice", 20)` 將 arguments 傳入，再透過 `this.fName = fName`、`this.age = age` 設定 object 的 state。
5. **補上 no-argument constructor 作比較**：若仍希望使用 `new Person()`，就明確宣告它，並自行決定初始值。`"Unknown"` 與 `18` 只是此處選用的範例值。
6. **保留 getters / setters**：constructor 處理建立時的初始化，getter 讀取目前的 state，setter 在被呼叫時設定或修改 state。

| 概念 | 本例的角色 |
|---|---|
| Default field values | `String` field 為 `null`，`int` field 為 `0` |
| Default constructor | 未宣告任何 constructor 時，由 compiler 自動提供 |
| Parameterized constructor | 建立 object 時接收姓名和年齡 |
| No-argument constructor | 在此修正版中，明確設定 `"Unknown"` 和 `18` |
| Getter | 回傳 field 的目前值 |
| Setter | 被呼叫時修改 field 的值 |
| `this` | 目前 object 的 reference，用來明確指定其 fields |

Fields 有 default values；method 內的 local variables 必須在讀取前完成賦值。例如 `Person p1;` 只宣告 local variable，不能在未賦值時直接呼叫 `p1.getAge()`。

## 重點比較

| 程式碼 | 發生什麼事 |
|---|---|
| `Circle aCircle;` | 宣告 reference variable |
| `aCircle = null;` | 讓 variable 不指向任何 object |
| `aCircle = new Circle();` | 建立新 object，將 reference 存入 variable |
| `bCircle = aCircle;` | 複製 reference，讓兩個 variables 指向同一個 object |
| `aCircle.radius = 10;` | 修改所指向 object 的 state |

**`new Circle()` 建立 object；`bCircle = aCircle` 複製 reference。**
