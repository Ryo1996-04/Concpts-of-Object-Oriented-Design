# Week 6 — Inheritance, super, and protected

## 筆記

[Inheritance、super 與 protected](notes/inheritance-and-super.zh-TW.md)

涵蓋 is-a / has-a、UML class diagram、`extends`、存取權限與 `protected`、`super(...)` 與 `super.member`、constructor 呼叫順序，以及 overriding 與 class hierarchy 的預習。

## 程式

| 檔案 | 內容 |
|---|---|
| [Address.java](src/Address.java) | 三個 private fields 與 get/set，被 Person 擁有（has-a） |
| [Person.java](src/Person.java) | protected fields、三參數 constructor、static counter |
| [Student.java](src/Student.java) | `extends Person`，用 `super(...)` 初始化共同欄位；main 輸出 6 行，最後一行 `Person count: 1` |
| [ClassA.java](src/ClassA.java) | ClassA / ClassB / ClassC 三層繼承，`new ClassC()` 依序輸出 `A constructor`、`B constructor`、`C constructor` |
| [ShadowingDemo.java](src/ShadowingDemo.java) | 子類別欄位遮蔽父類別欄位，`this.x` 輸出 `2`，`super.x` 輸出 `1` |

## 編譯與執行

從 repository 根目錄執行：

```sh
javac -encoding UTF-8 -d week-06/bin week-06/src/*.java
java -cp week-06/bin Student
java -cp week-06/bin ClassA
java -cp week-06/bin ShadowingDemo
```
