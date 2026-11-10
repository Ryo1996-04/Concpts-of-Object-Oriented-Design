# Week 9 — Enum Types、Exception Handling

## 筆記

[Enum Types](notes/enum.zh-TW.md) · [main、args 與 Scanner（課堂問答）](notes/main-and-scanner.zh-TW.md) · [Exception Handling](notes/exception-handling.zh-TW.md)

對應 `8_Abstract classes And Interfaces.pdf` p.25–31：固定常數集合、`enum` 宣告語法、enum 是繼承 `java.lang.Enum` 的 class、private constructor、`values()`、enum 搭配 `switch`。課堂以紅綠燈（開／關、顏色、方向）和座標系上下左右（Move）為例。

Exception Handling 對應 `9_Exception Handling in Java.pdf` p.3–30：syntax／runtime／logic error、Throwable 架構、checked 與 unchecked、stack trace、try／catch／finally、throw 與 throws、multiple catch、rethrow、user-defined exception。p.31–40 Unit Testing 尚未整理。

## 程式

| 檔案 | 內容 |
|---|---|
| [Day.java](src/Day.java) | 七天的 enum；用 `values()` 印出 `ordinal` 與名稱，再用 `switch` 判斷平日／週末 |
| [TrafficLight.java](src/TrafficLight.java) | `Power`、`LightColor`（含秒數 field、constructor、`next()`）、`Direction` 三個 enum；燈關著時不換色，開燈後 GREEN → YELLOW → RED 循環 |
| [Move.java](src/Move.java) | 課堂座標系例子的修正版：Left／Right／Up／Down 各帶 (x, y) 變化量，從 (0, 0) 走一段路徑並印出每步座標 |
| [ReadName.java](src/ReadName.java) | Scanner 最小範例：讀一行姓名並打招呼 |
| [DigitSum.java](src/DigitSum.java) | 期中 cheat sheet 最後一題：Scanner 讀整數，`% 10` / `/= 10` 算各位數和（567 → 18） |
| [DivideDemo.java](src/DivideDemo.java) | `div()` 遇到 0 時 `throw ArithmeticException`；`ArithmeticException`、`InputMismatchException` 兩個 catch 加上 `finally`。輸入 `10 2`、`10 0`、`10 abc` 三種結果都會印出 `program continues` |
| [FileCheck.java](src/FileCheck.java) | 講義 p.9 的 checked exception 修正版：`FileNotFoundException` 的 catch 放在 `IOException` 前面 |
| [CheckedException.java](src/CheckedException.java) | 課堂 FileReader 例子的修正版：打開、讀取、`finally` 關閉都處理 checked exception；檔名可從 `args[0]` 傳入，測試過檔案不存在、資料夾、正常檔案、損壞內容 |
| [InvalidAgeException.java](src/InvalidAgeException.java) | 自訂 checked exception，`super(message)` |
| [AgeDemo.java](src/AgeDemo.java) | `setAge()` 用 `throws` 宣告、`update()` 記錄後重新丟出，由 `main` 接住 |

## 編譯與執行

從 repository 根目錄執行：

```sh
javac -encoding UTF-8 -d week-09/bin week-09/src/*.java
java -cp week-09/bin Day
java -cp week-09/bin TrafficLight
java -cp week-09/bin Move
echo Claire | java -cp week-09/bin ReadName
echo 567 | java -cp week-09/bin DigitSum
echo "10 0" | java -cp week-09/bin DivideDemo
java -cp week-09/bin FileCheck
java -cp week-09/bin CheckedException week-09/src/Day.java
java -cp week-09/bin AgeDemo
```
