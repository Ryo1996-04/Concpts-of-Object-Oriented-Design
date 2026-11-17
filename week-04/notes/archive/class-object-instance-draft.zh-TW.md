# Java 筆記：Class、Object、Instance 與變數的差別

## 1. 先記住核心概念

- **Class（類別）**：定義物件結構與行為的設計圖。
- **Object（物件）／Instance（實例）**：依照類別建立出來的實際物件。
- **Instance variable（實例變數）**：屬於個別物件的資料，每個物件都有自己的一份。
- **Static variable／Class variable（靜態變數／類別變數）**：屬於類別、由該類別的物件共用的資料。
- **Reference variable（參考變數）**：保存物件參考的變數，用來存取物件；也可以是 `null`。

> **Class 是設計圖 → Instance 是照設計圖做出來的東西 → Instance variables 是每個東西自己的資料。**

## 2. Instance 和 Instance Variable 不一樣

**Instance 是物件本身；instance variable 是屬於這個物件的變數。**

以 `Student` 類別為例：

```java
class Student {
    String name; // instance variable
    int age;     // instance variable
}
```

建立兩個物件（以下操作可放在 `main` 方法中）：

```java
Student s1 = new Student();
Student s2 = new Student();

s1.name = "Claire";
s1.age = 25;

s2.name = "John";
s2.age = 30;
```

這裡有兩個不同的 `Student` instance，每個物件都有自己的 `name` 和 `age`。

| 項目 | 意義 |
|---|---|
| `Student` | 類別，定義學生物件的結構 |
| 每次執行 `new Student()` 所建立的物件 | `Student` 的 instance |
| `s1`、`s2` | 保存物件參考的 reference variables |
| `name`、`age` | 每個 `Student` 物件各自擁有的 instance variables |
| `s1.name`、`s2.name` | 分別存取兩個物件的 `name` |

> **精確說法：`s1` 和 `s2` 是參考變數；它們所指向的物件才是 instances。**

## 3. 用 Car 範例理解物件各自擁有的資料

```java
class Car {
    String color; // instance variable
    int speed;    // instance variable
}
```

建立與設定物件（放在方法中）：

```java
Car car1 = new Car();
Car car2 = new Car();

car1.color = "red";
car1.speed = 60;

car2.color = "blue";
car2.speed = 80;
```

概念示意圖：

```text
Car class（設計圖）
   │
   ├── 建立物件 A ← car1 保存它的參考
   │     ├── color = "red"
   │     └── speed = 60
   │
   └── 建立物件 B ← car2 保存它的參考
         ├── color = "blue"
         └── speed = 80
```

修改 `car1.speed` 不會改變 `car2.speed`，因為這裡的兩個參考變數指向不同物件，各自擁有自己的實例變數。

**Instance variable 之所以叫「實例變數」，就是因為每個 instance 都各自擁有一份。**

## 4. Instance Variable 與 Static／Class Variable

```java
class Student {
    String name;             // instance variable：每個學生自己的姓名
    static int studentCount; // class variable：類別共用的計數資料
}
```

使用範例（放在方法中）：

```java
Student s1 = new Student();
Student s2 = new Student();

s1.name = "Claire";
s2.name = "John";

Student.studentCount = 2;

System.out.println(s1.name);              // Claire
System.out.println(s2.name);              // John
System.out.println(Student.studentCount); // 2
```

- `name` 沒有 `static`：每個 `Student` 物件都有自己的姓名。
- `studentCount` 有 `static`：屬於 `Student` 類別，使用 `Student.studentCount` 存取能清楚表達這點。
- `studentCount` **不會自動計算物件數量**；此例是手動設定為 `2`。若要自動計數，需要自行撰寫更新邏輯。

## 5. 名詞比較表

| 名稱 | 中文 | 是什麼 | 每個物件都有自己的一份嗎？ |
|---|---|---|---|
| Class | 類別 | 建立物件的設計圖 | 不適用 |
| Object／Instance | 物件／實例 | 依照類別建立的實際物件 | 不適用 |
| Instance variable | 實例變數 | 個別物件擁有的資料 | 是 |
| Static／Class variable | 靜態變數／類別變數 | 屬於類別的共用資料 | 否 |
| Reference variable | 參考變數 | 保存物件參考或 `null` 的變數 | 取決於宣告位置；此名稱描述的是它保存的值 |

## 6. 拆解一行 Java 程式碼

```java
Student s1 = new Student();
```

| 程式碼片段 | 意義 |
|---|---|
| `Student` | `s1` 宣告使用的類別型別 |
| `s1` | 參考變數 |
| `new Student()` | 建立一個新的 `Student` 物件，並產生指向它的參考 |
| `=` | 將右側的物件參考指派給 `s1` |

接著：

```java
s1.name = "Claire";
```

意思是：透過 `s1` 找到它指向的物件，再將該物件的 instance variable `name` 設為 `"Claire"`。

## 7. Object 和 Instance 有差別嗎？

在 Java 初學階段，**object 和 instance 通常可以視為同一個東西的兩種說法**。

- **Object**：強調「這是一個物件」。
- **Instance**：強調「這個物件是某個類別的實例」。

例如，`new Student()` 建立的是一個 object，也是一個 `Student` 的 instance。

## 8. 記憶重點

> **Class = 設計圖**  
> **Object／Instance = 依照設計圖建立的物件**  
> **Instance variable = 每個物件自己的資料**  
> **Static／Class variable = 類別共用的資料**  
> **Reference variable = 用來指向物件的變數**

看到 `Student s1 = new Student();` 時，記得區分：

```text
s1              → 參考變數
new Student()   → 建立物件並產生其參考
s1 指向的物件    → Student 的 instance
該物件的 name   → instance variable
```
