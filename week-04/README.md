# Week 4 — Classes and Objects

## 筆記

[Classes and Objects：p.10–28、p.39 與課堂整合](notes/classes-and-objects.zh-TW.md)

涵蓋 reference、object creation、field access、method invocation、constructors、overloading 和 `this`，並保留課堂 Rectangle 與 Person 的說明。

[Instance 與 Static 比較：p.29–35](notes/instance-vs-static.zh-TW.md)

## 程式

| 檔案 | 內容 |
|---|---|
| [Person.java](src/Person.java) | p.31 static counter 範例、constructor chaining；輸出三行 `2` |
| [Rectangle.java](src/Rectangle.java) | 三個 constructors，以及 `area()`、`perm()` methods |
| [RectangleTest.java](src/RectangleTest.java) | 建立三個 Rectangle objects；目前沒有 println，因此沒有終端輸出 |

## 編譯與執行

從 repository 根目錄執行：

```sh
javac -encoding UTF-8 -d week-04/bin week-04/src/*.java
java -cp week-04/bin Person
java -cp week-04/bin RectangleTest
```

## Eclipse

使用 File → Import → General → Existing Projects into Workspace，選取此 `week-04` 資料夾。Project name 為 `week-04`，source folder 是 `src`，output folder 是 `bin`。既有設定使用 JavaSE-25，需在 Eclipse 中配置對應 JDK。

## 2026-10-06 補充

主筆記整合了 Class、Object、Instance、instance variable、static variable、reference variable 的 Student / Car 範例，並新增 p.39 的垃圾回收圖解與 System.gc() request 說明。
