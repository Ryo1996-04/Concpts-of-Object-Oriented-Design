# Week 5 — Encapsulation、Overloading、Wrapper Classes、Command-Line Arguments、Package、JavaDoc

## 筆記

[Encapsulation / Overloading / Wrapper / CLA / Package / JavaDoc](notes/encapsulation-cla-packages-javadoc.zh-TW.md)（2026-10-13 課堂）

- `5_Encapsulation.pdf` p.1–13：封裝、visibility modifiers、getter／setter、封裝前後對照、課堂 Circle 例子
- `4_Classes_And_Objects.pdf` p.41、p.44–57：Method Overloading、`static` 問答、Math、Wrapper Classes（p.42–43 尚未整理）
- `6_CLA_Packages_JavaDoc.pdf` p.3–19：Command-Line Arguments、編譯與執行、Package 與 import、JavaDoc 標籤

## 程式

| 檔案 | 內容 |
|---|---|
| [Hello.java](src/Hello.java) | 從 `args` 讀名和姓並打招呼；引數不是兩個時提示並結束 |
| [Sum.java](src/Sum.java) | 命令列輸入兩個整數，`Integer.parseInt` 轉換後相加 |
| [StaticCalculator.java](src/StaticCalculator.java) | `static` 的 overloaded `sum`，用 class 名稱呼叫 |
| [InstanceCalculator.java](src/InstanceCalculator.java) | 同樣的 overloaded `sum` 但沒有 `static`，要先建立物件 |

## 編譯與執行

從 repository 根目錄執行：

```sh
javac -encoding UTF-8 -d week-05/bin week-05/src/*.java
java -cp week-05/bin Hello Ada Lovelace
java -cp week-05/bin Sum 10 9
java -cp week-05/bin StaticCalculator
java -cp week-05/bin InstanceCalculator
```
