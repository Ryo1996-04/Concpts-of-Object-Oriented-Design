# Week 10 — Generics

## 筆記

[Generics](notes/generics.zh-TW.md)

對應 `10_Generics.pdf` p.1–8（2026-11-17 課堂進度）：用 overloading 印不同型別陣列的問題、generic method `<E>` 的寫法與呼叫、type inference、generics 的兩個好處（一份宣告代表一組方法／型別、compile-time type safety）、type parameter / type variable / actual type argument、只能用 reference type。p.9–20（erasure、`Comparable` 與 bounded type、generic class、wildcards）尚未整理。

課堂討論（第 11 節）：C 語言的編譯和 Java 的對照（機器碼 vs bytecode、erasure）、static binding 與 late binding 比較表、編譯器與 JVM 的執行順序（原始碼 → 編譯器 → bytecode → run time，含各步驟的 binding 與錯誤種類）。

## 程式

| 檔案 | 內容 |
|---|---|
| [PrintArrayOverload.java](src/PrintArrayOverload.java) | p.3 的做法：`printArray` 針對 `int[]`、`double[]`、`long[]` 各寫一個 overload |
| [GenericPrint.java](src/GenericPrint.java) | p.5–6 的修正版（加上 `static`）：一個 `<E> printArray` 印 `Integer[]`、`Double[]`、`Character[]`、`String[]`；`<E> E first(E[])` 示範 E 當 return type 與 local variable 型別 |

## 編譯與執行

從 repository 根目錄執行：

```sh
javac -encoding UTF-8 -d week-10/bin week-10/src/*.java
java -cp week-10/bin PrintArrayOverload
java -cp week-10/bin GenericPrint
```
