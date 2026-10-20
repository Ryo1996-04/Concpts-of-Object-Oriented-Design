# CSYE6200 筆記：Inheritance、super 與 protected

> 個人學習筆記，課堂內容以自己的話重新整理。本文不含投影片原文或圖片，標註的頁碼只用來對照課程講義；所有範例程式都是自己寫的。

- 課程：CSYE6200（Fall 2026）
- 單元：Inheritance and Polymorphism（前半：Inheritance）
- 日期：2026-10-20
- 可執行的範例程式：[`../src/`](../src)（Address、Person、Student、ClassA、ShadowingDemo）

## 目錄

1. [Inheritance 是什麼（p.3、p.6）](#1-inheritance-是什麼p3p6)
2. [is-a 與 has-a（p.3–5）](#2-is-a-與-has-ap35)
3. [補充：UML Class Diagram](#3-補充uml-class-diagram)
4. [extends 語法與繼承的方向（p.7–8）](#4-extends-語法與繼承的方向p78)
5. [課堂範例：Address / Person / Student](#5-課堂範例address--person--student)
6. [複習：this 關鍵字](#6-複習this-關鍵字)
7. [存取權限與 protected（p.9–14）](#7-存取權限與-protectedp914)
8. [super(...)：呼叫父類別的 constructor（p.15–18）](#8-super呼叫父類別的-constructorp1518)
9. [super.member：存取父類別的成員（p.19–20）](#9-supermember存取父類別的成員p1920)
10. [Constructor 呼叫順序（p.28）](#10-constructor-呼叫順序p28)
11. [一個檔案一個 public class，main 要放哪](#11-一個檔案一個-public-classmain-要放哪)
12. [預習：Single inheritance、Overriding、Class hierarchy（p.21–27）](#12-預習single-inheritanceoverridingclass-hierarchyp2127)
13. [常見錯誤](#13-常見錯誤)
14. [檢查清單](#14-檢查清單)

---

## 1. Inheritance 是什麼（p.3、p.6）

**Inheritance（繼承）**：以一個已存在的 class 為基礎，衍生出新的 class。

| 名詞 | 意思 |
|---|---|
| parent class / **superclass** | 被繼承的原本那個 class |
| child class / **subclass** | 衍生出來的新 class |

為什麼要繼承：

- **Reuse**：父類別已經寫好、測試過的欄位和方法，子類別直接拿來用，不用重寫
- **Enhancement / Adaptation**：子類別可以**新增**自己的欄位和方法，也可以**修改**繼承來的方法
- 避免 **duplicate code**：好幾個 class 有大量相同的資料和行為時，把共同部分抽到父類別

子類別是父類別「更具體」的版本。例如 Student 是一種比較具體的 Person。

---

## 2. is-a 與 has-a（p.3–5）

Class 之間常見兩種關係，要分清楚：

| 關係 | 說法 | 例子 | Java 寫法 |
|---|---|---|---|
| **is-a**（Generalization / Specialization） | A 是一種 B | Circle 是一種 Shape | `class Circle extends Shape` |
| **has-a**（Whole-part，稱為 **aggregation**） | A 擁有一個 B | Employee 擁有一個生日 Date | 把 B 當成 A 的欄位 |

**繼承只用在 is-a 關係。**

### 繼承樹（is-a）

越往上越一般，越往下越具體：

```
                Shape
              /       \
         Shape2D      Shape3D
        /   |   \      /    \
 Triangle Rectangle Circle  Cube  Sphere
```

Circle 是一種 Shape2D，Shape2D 是一種 Shape，所以 Circle 也是一種 Shape。

> Java 的 class 名稱不能以數字開頭，所以要寫成 `Shape2D`，不能寫 `2D_Shape`。

### 判斷技巧

把兩個 class 念成一句英文：

- 「A **is a** B」說得通 → 用繼承
- 「A **has a** B」說得通 → 把 B 當成 A 的欄位

例：「Student is a Person」✅ 繼承；「Person is an Address」❌；「Person has an Address」✅ 當欄位。

---

## 3. 補充：UML Class Diagram

課堂上口頭提到 UML（**Unified Modeling Language**）。它是一套畫軟體設計圖的標準符號，不是程式語言。最常用的是 **Class Diagram**。

### 一個 class 畫成三格

```
┌──────────────────────┐
│        Person        │  ← class 名稱
├──────────────────────┤
│ # fName : String     │  ← 欄位（attributes）
│ # age : int          │
│ # address : Address  │
├──────────────────────┤
│ + getAge() : int     │  ← 方法（methods）
│ + setAge(int) : void │
└──────────────────────┘
```

### 存取權限符號

| 符號 | 意思 |
|---|---|
| `+` | public |
| `-` | private |
| `#` | protected |
| `~` | default（package） |

### 關係的畫法

| 關係 | 畫法 |
|---|---|
| 繼承（is-a） | 實線 + **空心三角形**，箭頭由子類別**指向父類別** |
| 聚合（has-a） | 實線 + **空心菱形**，菱形畫在「整體」那一端 |
| 組合（較強的 has-a，composition） | 實線 + **實心菱形** |

---

## 4. extends 語法與繼承的方向（p.7–8）

```java
class 子類別名稱 extends 父類別名稱 {
    // 只寫子類別新增或修改的部分
}
```

兩個重點：

1. **建立子類別物件時，不需要先建立父類別物件。** 直接 `new Circle()` 就好，不用先 `new Shape()`。
2. **繼承是單向的。** 子類別看得到父類別的東西，但父類別**看不到**子類別自己新增的欄位和方法。例如 Person 不能使用 Student 才有的 `major`。

---

## 5. 課堂範例：Address / Person / Student

課堂白板的設計：

```
  Address  ◇────────  Person  ◁────────  Student
         (has-a)                (is-a)
```

| Class | 欄位 | 關係 |
|---|---|---|
| Address | streetNo、streetName、zipCode | 被 Person 擁有（has-a） |
| Person | fName、age、address | 父類別 |
| Student | **id、major** | `Student extends Person` |

### 沒有繼承時的問題

如果 Student 不繼承 Person，就得自己再寫一次 fName、age、address 三個欄位，還有它們的 get/set，全部和 Person 重複。

### 用繼承改寫後

Student 只需要寫學生才有的 `id` 和 `major`，其他都從 Person 繼承。

三個檔案放在**同一個 package**：

```
src/
 ├─ Address.java
 ├─ Person.java
 └─ Student.java
```

#### Address.java

```java
public class Address {
    private String streetNo;
    private String streetName;
    private int zipCode;

    public Address(String streetNo, String streetName, int zipCode) {
        this.streetNo = streetNo;
        this.streetName = streetName;
        this.zipCode = zipCode;
    }

    public String getStreetNo() { return this.streetNo; }
    public void setStreetNo(String streetNo) { this.streetNo = streetNo; }

    public String getStreetName() { return this.streetName; }
    public void setStreetName(String streetName) { this.streetName = streetName; }

    public int getZipCode() { return this.zipCode; }
    public void setZipCode(int zipCode) { this.zipCode = zipCode; }
}
```

> zipCode 用 `int` 時，開頭的 0 會不見（`02115` 會變成 `2115`）。實務上郵遞區號通常用 `String`。

#### Person.java

```java
public class Person {
    protected String fName;
    protected int age;
    protected Address address;          // has-a：Person 擁有一個 Address
    private static int counter = 0;     // 記錄總共建立了幾個 Person

    public Person(String fName, int age, Address address) {
        this.fName = fName;
        this.age = age;
        this.address = address;
        counter++;
    }

    public String getFName() { return this.fName; }
    public void setFName(String fName) { this.fName = fName; }

    public int getAge() { return this.age; }
    public void setAge(int age) { this.age = age; }

    public Address getAddress() { return this.address; }
    public void setAddress(Address address) { this.address = address; }

    public static int getCounter() { return counter; }
}
```

#### Student.java

```java
public class Student extends Person {       // is-a：Student 是一種 Person
    protected int id;                       // 只寫學生才有的欄位
    protected String major;

    public Student(String fName, int age, Address address, int id, String major) {
        super(fName, age, address);         // 共同的部分交給 Person 的 constructor
        this.id = id;
        this.major = major;
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }

    public String getMajor() { return this.major; }
    public void setMajor(String major) { this.major = major; }

    public static void main(String[] args) {
        Address addr = new Address("123", "Main St", 12345);
        Student s = new Student("Amy", 20, addr, 1001, "Software Engineering");

        System.out.println("Name: " + s.getFName());                      // 繼承自 Person
        System.out.println("Age: " + s.getAge());                         // 繼承自 Person
        System.out.println("Street: " + s.getAddress().getStreetName());  // method chaining
        System.out.println("ID: " + s.getId());                           // Student 自己的
        System.out.println("Major: " + s.getMajor());                     // Student 自己的
        System.out.println("Person count: " + Person.getCounter());
    }
}
```

#### 執行結果

```
Name: Amy
Age: 20
Street: Main St
ID: 1001
Major: Software Engineering
Person count: 1
```

- Student 裡沒有寫 `getFName()`，但 `s.getFName()` 可以用，因為是繼承來的。
- `Person count` 是 1：建立 Student 時，Person 的 constructor 也會執行（見第 10 節），所以每個 Student 也被算成一個 Person。
- `s.getAddress().getStreetName()` 是 **method chaining**：先取得 Address 物件，再對它呼叫方法。

#### 改寫前後比較

| | 不用繼承 | `extends Person` |
|---|---|---|
| Student 的欄位 | 5 個 | 2 個（id、major） |
| Student 的 get/set | 5 組 | 2 組 |
| 共同欄位要修改時 | 要改兩個 class | 只改 Person |

> 課堂上第一版先把欄位都設成 `public`，讓大家專心理解繼承；後來才改成 `protected`。原因見第 7 節。

---

## 6. 複習：this 關鍵字

**`this` 代表目前正在被操作的那個物件。**

```java
public void setId(int id) {
    this.id = id;    // 左邊 this.id 是物件的欄位，右邊 id 是參數
}
```

**Shadowing 陷阱**：參數和欄位同名時，方法裡寫 `id` 指的是參數，會把同名欄位遮住。

```java
public void setId(int id) {
    id = id;         // ❌ 參數指定給自己，欄位沒有被改到，而且不會報錯
}
```

**`this` 是誰，看點號前面：** 執行 `a.setId(1)` 時 `this` 是 `a`；執行 `b.setId(2)` 時 `this` 是 `b`。

---

## 7. 存取權限與 protected（p.9–14）

### 封裝與繼承的衝突

- **封裝（Encapsulation）單元**：欄位應該設成 `private`，外部只能透過 get/set 存取。
- **繼承**：子類別常常需要用到父類別的欄位。
- **衝突**：父類別的 `private` 成員，子類別的程式碼**不能直接存取**。

```java
public class Person {
    private String fName;
}

public class Student extends Person {
    void test() {
        this.fName = "Amy";   // ❌ 編譯錯誤：The field Person.fName is not visible
    }
}
```

Student 物件裡**仍然有** fName 這份資料（Person 的 constructor 會設定它），只是 Student 的程式碼看不到，不能直接讀寫。

改成 `public` 雖然子類別可以存取，但**任何 class 都能直接改**，破壞了封裝。例如外部程式可以直接寫 `s.age = -5;`，完全跳過 `setAge()` 的檢查。

### protected：折衷的做法

`protected` 讓**子類別可以直接存取**，同時擋住大部分無關的外部 class。它的封裝程度比 public 好，又不妨礙繼承。

### 四種存取權限

| 修飾字 | 同一個 class | 同一個 package | 不同 package 的子類別 | 其他地方 |
|---|---|---|---|---|
| `private` | ✅ | ❌ | ❌ | ❌ |
| （不寫，default） | ✅ | ✅ | ❌ | ❌ |
| `protected` | ✅ | ✅ | ✅ | ❌ |
| `public` | ✅ | ✅ | ✅ | ✅ |

> ⚠️ Java 的 `protected` 除了開放給子類別，**同一個 package 的所有 class 也能存取**。所以三個 class 都放在同一個 package 時，protected 的效果和 default 幾乎一樣；子類別放在不同 package 時，才看得出兩者的差別。

### 三種寫法比較

| 父類別欄位 | 封裝程度 | 子類別能否直接用 `this.欄位` | 子類別要怎麼設定父類別欄位 |
|---|---|---|---|
| `public` | ❌ 最差 | ✅ | 直接寫 |
| `protected` | ⚠️ 折衷 | ✅ | 直接寫，或用 `super(...)` |
| `private` | ✅ 最好 | ❌ | 只能用 `super(...)` 或繼承來的 getter/setter |

所以封裝和繼承**並不互斥**：父類別欄位維持 `private` 也能繼承，只是子類別要透過 `super(...)` 和 getter/setter 間接處理。`protected` 是為了讓子類別可以直接存取的折衷方案。

---

## 8. super(...)：呼叫父類別的 constructor（p.15–18）

**Constructor 不會被繼承**，即使它是 public。Student 不會自動擁有 `Person(String, int, Address)` 這個 constructor。

但子類別物件裡包含了「父類別那部分」的資料，這部分應該交給父類別的 constructor 初始化。所以：

> **子類別的 constructor 要負責呼叫父類別的 constructor**，方法是在第一行寫 `super(...)`。

```java
public Student(String fName, int age, Address address, int id, String major) {
    super(fName, age, address);    // 初始化 Person 的部分
    this.id = id;                  // 初始化 Student 自己的部分
    this.major = major;
}
```

### 規則

1. `super(...)` 只能寫在 constructor 裡，而且**必須是第一行**。
2. 沒有寫的話，Java 會**自動補上 `super();`**（呼叫父類別的無參數 constructor）。
3. 如果父類別**沒有無參數 constructor**，子類別就**一定要**自己寫 `super(...)`，否則編譯錯誤。Eclipse 的錯誤訊息是：`Implicit super constructor Person() is undefined`。
4. 參數的數量、型別、順序要對應到父類別的某一個 constructor。
5. `this(...)`（呼叫自己的另一個 constructor）和 `super(...)` 都必須在第一行，所以同一個 constructor 裡只能擇一。

---

## 9. super.member：存取父類別的成員（p.19–20）

語法是 `super.成員`，成員可以是欄位或方法。

### 用在被遮蔽的欄位

子類別宣告了和父類別同名的欄位時，子類別的欄位會**遮蔽（shadow）**父類別的欄位。這時要用 `super.` 才能取到父類別那一份：

```java
class Base {
    int x = 1;
}

class Derived extends Base {
    int x = 2;                          // 遮蔽了 Base 的 x

    void show() {
        System.out.println(this.x);     // 2：Derived 的 x
        System.out.println(super.x);    // 1：Base 的 x
    }
}
```

> 實務上應該**避免**子類別宣告與父類別同名的欄位，容易混淆。這個例子只是用來理解遮蔽。

### 用在被改寫的方法

`super.方法()` 可以呼叫父類別版本的方法，在 overriding 時很常用（見第 12 節）。

### this 與 super 對照

| | `this` | `super` |
|---|---|---|
| 代表 | 目前這個物件 | 從父類別繼承來的那部分 |
| `xxx(...)` | 呼叫自己的另一個 constructor | 呼叫父類別的 constructor |
| `xxx.欄位` | 自己的欄位 | 父類別的欄位 |
| `xxx.方法()` | 自己的方法 | 父類別版本的方法 |

不能寫 `super.super.xxx`，只能往上一層。

---

## 10. Constructor 呼叫順序（p.28）

也稱為 **constructor chaining**（課堂上說 cascade）。

### 範例

```java
// 檔名：ClassA.java
public class ClassA {
    public ClassA() {
        System.out.println("A constructor");
    }

    public static void main(String[] args) {
        ClassC c = new ClassC();
    }
}

class ClassB extends ClassA {
    public ClassB() {
        // 沒寫 super()，Java 會自動補上
        System.out.println("B constructor");
    }
}

class ClassC extends ClassB {
    public ClassC() {
        System.out.println("C constructor");
    }
}
```

執行結果：

```
A constructor
B constructor
C constructor
```

### 流程（課堂白板圖的意思）

```
繼承關係          呼叫（往上）               執行完成（往下）
ClassA           A()  ←─┐                    ① A() 的內容
  ▲                     │ super()
ClassB           B()  ←─┤                    ② B() 的內容
  ▲                     │ super()
ClassC           C()  ──┘  ← new ClassC()     ③ C() 的內容
```

- **呼叫方向由下往上**：C() → B() → A() → Object()
- **執行方向由上往下**：Object() 先完成，接著 A()、B()，最後 C()
- 只 new 了一個 ClassC，但三個 constructor 都會執行，因為 C 物件裡面包含了 B 和 A 的部分，必須從最上層開始初始化。
- 每一層 constructor 的第一行都會呼叫上一層（自己寫的 `super(...)`，或 Java 自動補的 `super()`）。
- 這條鏈的最頂端一定是 **`Object`**。Java 裡所有 class 最終都繼承自 `Object`，它的 constructor 不做任何事。

---

## 11. 一個檔案一個 public class，main 要放哪

### 規則

- 一個 `.java` 檔**最多一個 public class**，而且名稱**必須和檔名完全一樣**（大小寫也要相同）。
- 同一個檔案裡的其他 class 不能加 `public`，只能寫 `class ClassB ...`（default 存取，同一個 package 內都看得到）。
- 違反規則時的錯誤訊息：`The public type Xxx must be defined in its own file`。
- 編譯後，每個 class 還是會各自產生一個 `.class` 檔（ClassA.class、ClassB.class、ClassC.class）。

### main 放在哪

像第 10 節的例子，檔名是 `ClassA.java`，就把 `main` 放在 `public class ClassA` 裡。在 Eclipse 按 **Run As → Java Application** 時，執行的就是這個 main。

- main 放在 ClassA 裡，**不代表會建立 ClassA 物件**。`main` 是 `static`，屬於 class 本身，不需要物件就能執行。
- main 裡可以 new 同一個 package 的任何 class，例如 `new ClassC()`。
- 寫了 `ClassC c = ...` 但後面沒用到 `c`，Eclipse 會出現黃色 warning 燈泡，不影響執行。

### 實際寫專案時

每個 public class 各自一個檔案（Address.java、Person.java、Student.java），是最常見的做法。把多個 class 寫在同一個檔案，通常只用在像第 10 節這種小型示範。

---

## 12. 預習：Single inheritance、Overriding、Class hierarchy（p.21–27）

> 以下是課堂還沒講到的部分，先簡單整理。

### Single inheritance（p.21）

- Java 的 class **只能有一個父類別**（`extends` 後面只能接一個 class）。
- 有些語言支援 multiple inheritance（同時繼承多個父類別），但兩個父類別有同名成員時會衝突，Java 不支援這種做法。
- 需要類似效果時，Java 使用 **interface**（之後會教）。

### Overriding（p.22–24）

**Overriding（改寫）**：子類別定義一個和父類別方法**簽名（signature）完全相同**的方法，用自己的版本取代父類別的版本。

- **Signature** = 方法名稱 + 參數的數量、型別、順序
- 實際執行哪個版本，由**物件本身的型別**決定
- 在子類別裡可以用 `super.方法()` 呼叫父類別原本的版本

```java
public class Student extends Person {
    // ...（constructor 等省略）

    @Override
    public void setAge(int age) {
        if (age <= 0) {
            System.out.println("Invalid age");
            return;                 // 不合法就不設定
        }
        super.setAge(age);          // 合法才交給 Person 原本的 setAge
    }
}
```

`@Override` 可寫可不寫，但建議加上：如果簽名打錯、其實沒有改寫到父類別的方法，編譯器會報錯提醒。

欄位也有類似的現象，叫 **shadowing variables**（第 9 節），但應該避免。

### Overloading vs. Overriding（p.25）

| | Overloading（多載） | Overriding（改寫） |
|---|---|---|
| 位置 | **同一個 class** 裡 | 父類別和子類別之間 |
| 方法名稱 | 相同 | 相同 |
| 參數 | **不同** | **完全相同** |
| 用途 | 同一種操作，接受不同的資料 | 同一種操作，不同型別的物件有不同做法 |

### Class hierarchy（p.26–27）

- 子類別也可以再當別人的父類別，形成多層的 **class hierarchy**（例如第 2 節的 Shape 繼承樹）。
- 同一個父類別的兩個子類別叫 **siblings**（例如 Circle 和 Triangle），它們彼此**沒有**繼承關係。
- 繼承會一路往下傳：子類別會擁有**所有祖先**的成員（Circle 同時擁有 Shape2D 和 Shape 的成員）。
- 共同的特性應該盡量放在**合理範圍內最高的那一層**。
- 沒有一種繼承結構適用所有情況，要看需求設計。

---

## 13. 常見錯誤

| 錯誤寫法 | 問題 | 正確寫法 |
|---|---|---|
| 參數寫成 `Address adress`，內容寫 `this.address = address;` | 參數拼錯字，右邊的 `address` 變成欄位本身，等於欄位指定給自己，結果永遠是 `null`，**而且不會報錯** | 參數名稱和欄位一致：`Address address` |
| setter 裡只寫 `this.fName;` | 不是完整的敘述（not a statement），編譯錯誤 | `this.fName = fName;` |
| `id = id;` | 參數遮蔽了欄位，欄位沒有被改到 | `this.id = id;` |
| 子類別再宣告一次父類別已有的欄位 | 出現兩份同名欄位，互相遮蔽 | 子類別只寫新增的欄位 |
| `super(...)` 沒放在第一行 | 編譯錯誤 | 移到 constructor 第一行 |
| 父類別只有帶參數的 constructor，子類別沒寫 `super(...)` | `Implicit super constructor ... is undefined` | 明確寫出 `super(...)` |
| 把兩個 `public class` 寫在同一個檔案 | `must be defined in its own file` | 拆成兩個檔案，或拿掉其中一個的 `public` |
| 父類別欄位是 `private`，子類別寫 `this.欄位 = ...` | `The field ... is not visible` | 改用 `super(...)`、setter，或把欄位改成 `protected` |

---

## 14. 檢查清單

- [ ] 每個 public class 的名稱和檔名相同
- [ ] 相關的 class 放在同一個 package
- [ ] 子類別寫了 `extends 父類別`
- [ ] 子類別沒有重複宣告父類別已有的欄位與 get/set
- [ ] 子類別 constructor 的第一行是 `super(...)`，參數對應到父類別的 constructor
- [ ] 每個 setter 都是 `this.欄位 = 參數;`
- [ ] 欄位的存取權限：只給自己用 → `private`；子類別需要直接用 → `protected`；盡量不要用 `public`
- [ ] 執行後確認：建立子類別物件時，父類別的 constructor 也有執行
