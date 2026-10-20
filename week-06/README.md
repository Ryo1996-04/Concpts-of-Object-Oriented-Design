# Week 6 — Inheritance, super, protected, and Overriding

## 筆記

[Inheritance、super、protected 與 Overriding](notes/inheritance-and-super.zh-TW.md)

對應講義 p.3–28：is-a / has-a、UML class diagram、`extends`、存取權限與 `protected`、`super(...)` 與 `super.member`、single inheritance、overriding（Shape / Circle / Rectangle 的 `area()`）、overloading 與 overriding 的比較、class hierarchy，以及 constructor 呼叫順序。

## 程式

| 檔案 | 內容 |
|---|---|
| [Address.java](src/Address.java) | 三個 private fields 與 get/set，被 Person 擁有（has-a） |
| [Person.java](src/Person.java) | protected fields、三參數 constructor、static counter |
| [Student.java](src/Student.java) | `extends Person`，用 `super(...)` 初始化共同欄位；main 輸出 6 行，最後一行 `Person count: 1` |
| [ClassA.java](src/ClassA.java) | ClassA / ClassB / ClassC 三層繼承，`new ClassC()` 依序輸出 `A constructor`、`B constructor`、`C constructor` |
| [ShadowingDemo.java](src/ShadowingDemo.java) | 子類別欄位遮蔽父類別欄位，`this.x` 輸出 `2`，`super.x` 輸出 `1` |
| [Shape.java](src/Shape.java) | 父類別，`area()` 回傳 `0` |
| [Circle.java](src/Circle.java) | `extends Shape`，override `area()` 為 π × r²；另有無參數 constructor 示範 overloading |
| [Rectangle.java](src/Rectangle.java) | `extends Shape`，override `area()` 為 width × height |
| [ShapeTest.java](src/ShapeTest.java) | 四個 `Shape` 型別的變數指向不同物件，依序輸出 `12.566370614359172`、`12.0`、`0.0`、`3.141592653589793` |

## 編譯與執行

從 repository 根目錄執行：

```sh
javac -encoding UTF-8 -d week-06/bin week-06/src/*.java
java -cp week-06/bin Student
java -cp week-06/bin ClassA
java -cp week-06/bin ShadowingDemo
java -cp week-06/bin ShapeTest
```
