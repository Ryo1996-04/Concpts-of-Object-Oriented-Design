# Week 8 — ArrayList, Abstract Classes, and Interfaces

## 筆記

[ArrayList、Abstract Classes 與 Interfaces](notes/arraylist-abstract-interfaces.zh-TW.md) ([English](notes/arraylist-abstract-interfaces.en.md))

對應 `8_Abstract classes And Interfaces.pdf` p.1–24：ArrayList（動態陣列、constructors、常用方法、generics、`remove` 的 overloading 陷阱）、abstract class（不能 instantiate、abstract method、concrete class、四條規則）、interface（常數與 abstract method、`implements`、interface 繼承、同時 extends 與 implements、polymorphism via interfaces），最後是 abstract class 與 interface 的比較。同一份講義 p.25–31 的 Enum 在 [Week 9](../week-09/README.md)。

2026-11-03 缺課，依講義自學整理。

## 程式

| 檔案 | 內容 |
|---|---|
| [ArrayListDemo.java](src/ArrayListDemo.java) | p.7 改成 `ArrayList<String>`；add、插入、依值與依 index 移除，再示範 get、contains、indexOf、set |
| [Shape.java](src/Shape.java) | abstract class：abstract `area()` 加上一般方法 `describe()` |
| [Circle.java](src/Circle.java) | `extends Shape`，實作 `area()` 為 PI × r²（PI 照講義 3.1415） |
| [Rectangle.java](src/Rectangle.java) | `extends Shape`，實作 `area()` 為 w × h |
| [ShapeTest.java](src/ShapeTest.java) | `Shape[]` 放不同子類別，`describe()` 依序輸出 `3.1415`、`12.566`、`12.0` |
| [Speaker.java](src/Speaker.java) | interface：常數 `MAX_MINUTES` 與 `speak()` |
| [Politician.java](src/Politician.java) · [Priest.java](src/Priest.java) · [Lecturer.java](src/Lecturer.java) | 三個不相關的 class 各自 `implements Speaker` |
| [Teacher.java](src/Teacher.java) | `interface Teacher extends Speaker`，多一個 `grade()` |
| [Person.java](src/Person.java) | 一般父類別 |
| [Professor.java](src/Professor.java) | `extends Person implements Teacher` |
| [SpeakerTest.java](src/SpeakerTest.java) | `Speaker` 型別的變數輪流指向不同物件，同一行 `speak()` 印出不同結果 |

## 編譯與執行

從 repository 根目錄執行：

```sh
javac -encoding UTF-8 -d week-08/bin week-08/src/*.java
java -cp week-08/bin ArrayListDemo
java -cp week-08/bin ShapeTest
java -cp week-08/bin SpeakerTest
```
