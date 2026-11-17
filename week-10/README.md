# Week 10 — Generics

## 筆記

[Generics](notes/generics.zh-TW.md) ([English](notes/generics.en.md))

對應 `10_Generics.pdf` p.1–8（2026-11-17 課堂進度）：用 overloading 印不同型別陣列的問題、generic method `<E>` 的寫法與呼叫、type inference、generics 的兩個好處（一份宣告代表一組方法／型別、compile-time type safety）、type parameter / type variable / actual type argument、只能用 reference type。p.9–12（erasure、`Comparable` 與 upper bound）依課堂白板整理在第 11 節；p.13–20（generic method overloading、generic class、wildcards）尚未整理。

課堂討論（第 11 節）：C 語言的編譯和 Java 的對照（機器碼 vs bytecode、erasure）、static binding 與 late binding 比較表、編譯器與 JVM 的執行順序（原始碼 → 編譯器 → bytecode → run time，含各步驟的 binding 與錯誤種類）；白板練習：找最大值從 `int` 寫到 generic、`Comparable<T>` 與 upper bound（p.10–12）、有 upper bound 時 erasure 換成 `Comparable`、`Person.main()` 與 `p.getAge()` 的 static／instance 呼叫。英文版尚未同步這次新增的第 11 節第 4–6 小節。

## 程式

| 檔案 | 內容 |
|---|---|
| [PrintArrayOverload.java](src/PrintArrayOverload.java) | p.3 的做法：`printArray` 針對 `int[]`、`double[]`、`long[]` 各寫一個 overload |
| [FindMax.java](src/FindMax.java) | 白板練習：找最大值從 `int[]` 版開始，經過 `double[]` overload、`Integer` + `compareTo()`，改寫成 `<T extends Comparable<T>>` 的 generic method |
| [GenericPrint.java](src/GenericPrint.java) | p.5–6 的修正版（加上 `static`）：一個 `<E> printArray` 印 `Integer[]`、`Double[]`、`Character[]`、`String[]`；`<E> E first(E[])` 示範 E 當 return type 與 local variable 型別 |

## 編譯與執行

從 repository 根目錄執行：

```sh
javac -encoding UTF-8 -d week-10/bin week-10/src/*.java
java -cp week-10/bin PrintArrayOverload
java -cp week-10/bin GenericPrint
java -cp week-10/bin FindMax
```
