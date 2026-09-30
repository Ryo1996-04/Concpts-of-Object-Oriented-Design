# Week 3 — Arrays, Strings, and I/O

CSYE6200 OOP 學習筆記 / Study notes

## 選擇語言 / Choose a language

- [繁體中文版：逐頁重點、範例與課堂補充](notes.zh-TW.md)
- [English: slide-by-slide notes, examples, and classroom supplements](notes.en.md)

## 內容與來源 / Scope and source

依 Tessema Mengistu, Ph.D. 的 `Arrays_String_InputOutput.pdf`，按實際 PDF 頁序 1–28 整理；加入課堂討論與白板照片的 String 範例。中英文版本有相同頁碼與標題，保留講義的學習順序，並另外標示補充及語法更正。

Based on `Arrays_String_InputOutput.pdf` by Tessema Mengistu, Ph.D., in physical PDF page order 1–28. Both editions include corresponding sections, examples, classroom discussion, and String concepts reconstructed from whiteboard photographs. Supplements and corrections are identified separately. These are study notes, not an official or verbatim lecture transcript.

涵蓋 / Includes:

- Array references, heap concepts, initialization, access, and bounds checks.
- Row-major traversal, basic/enhanced for loops, and jagged arrays.
- String pooling, identity versus content equality, and immutability.
- String methods, escape sequences, standard streams, and Scanner.

## 逐頁索引 / Slide index

| PDF 頁 / Page | 投影片標題 / Slide title | 中文 | English |
|---|---|---|---|
| 01 | Arrays, Strings, and I/O | [閱讀](notes.zh-TW.md#slide-01) | [Read](notes.en.md#slide-01) |
| 02 | Outline | [閱讀](notes.zh-TW.md#slide-02) | [Read](notes.en.md#slide-02) |
| 03 | What are Arrays? | [閱讀](notes.zh-TW.md#slide-03) | [Read](notes.en.md#slide-03) |
| 04 | Java Arrays | [閱讀](notes.zh-TW.md#slide-04) | [Read](notes.en.md#slide-04) |
| 05 | Java Arrays | [閱讀](notes.zh-TW.md#slide-05) | [Read](notes.en.md#slide-05) |
| 06 | Array Indexes | [閱讀](notes.zh-TW.md#slide-06) | [Read](notes.en.md#slide-06) |
| 07 | Array Visualization | [閱讀](notes.zh-TW.md#slide-07) | [Read](notes.en.md#slide-07) |
| 08 | Accessing Array Elements | [閱讀](notes.zh-TW.md#slide-08) | [Read](notes.en.md#slide-08) |
| 09 | Assigning values to Array Elements | [閱讀](notes.zh-TW.md#slide-09) | [Read](notes.en.md#slide-09) |
| 10 | Length of array | [閱讀](notes.zh-TW.md#slide-10) | [Read](notes.en.md#slide-10) |
| 11 | Modifying Array Elements | [閱讀](notes.zh-TW.md#slide-11) | [Read](notes.en.md#slide-11) |
| 12 | Multi-Dimensional Arrays | [閱讀](notes.zh-TW.md#slide-12) | [Read](notes.en.md#slide-12) |
| 13 | Multi-Dimensional Array | [閱讀](notes.zh-TW.md#slide-13) | [Read](notes.en.md#slide-13) |
| 14 | Enhanced for Loop | [閱讀](notes.zh-TW.md#slide-14) | [Read](notes.en.md#slide-14) |
| 15 | Jagged Arrays | [閱讀](notes.zh-TW.md#slide-15) | [Read](notes.en.md#slide-15) |
| 16 | Exercise | [閱讀](notes.zh-TW.md#slide-16) | [Read](notes.en.md#slide-16) |
| 17 | Strings | [閱讀](notes.zh-TW.md#slide-17) | [Read](notes.en.md#slide-17) |
| 18 | Strings | [閱讀](notes.zh-TW.md#slide-18) | [Read](notes.en.md#slide-18) |
| 19 | String Class | [閱讀](notes.zh-TW.md#slide-19) | [Read](notes.en.md#slide-19) |
| 20 | String Class | [閱讀](notes.zh-TW.md#slide-20) | [Read](notes.en.md#slide-20) |
| 21 | Escape Sequences | [閱讀](notes.zh-TW.md#slide-21) | [Read](notes.en.md#slide-21) |
| 22 | Escape Sequence: Example | [閱讀](notes.zh-TW.md#slide-22) | [Read](notes.en.md#slide-22) |
| 23 | Input/Output | [閱讀](notes.zh-TW.md#slide-23) | [Read](notes.en.md#slide-23) |
| 24 | Printing | [閱讀](notes.zh-TW.md#slide-24) | [Read](notes.en.md#slide-24) |
| 25 | Reading Input | [閱讀](notes.zh-TW.md#slide-25) | [Read](notes.en.md#slide-25) |
| 26 | Reading Input | [閱讀](notes.zh-TW.md#slide-26) | [Read](notes.en.md#slide-26) |
| 27 | Reading Input | [閱讀](notes.zh-TW.md#slide-27) | [Read](notes.en.md#slide-27) |
| 28 | References | [閱讀](notes.zh-TW.md#slide-28) | [Read](notes.en.md#slide-28) |

## 使用方式 / Usage

一般程式片段放在 `main` 內；完整 class 請以相符檔名另存，例如 `Array.java` 或 `ReadName.java`。重複變數名稱的獨立範例不要直接拼接。註解中的錯誤寫法是教學用途。

Place ordinary snippets inside `main`. Save complete classes under matching filenames such as `Array.java` or `ReadName.java`. Do not concatenate independent examples that reuse variable names. Commented-out invalid code is included for explanation.

## 重要澄清 / Important clarifications

- Row order processing 是常見走訪方式，由迴圈決定；不是 Java 強制的順序或跨列記憶體連續性保證。
- Row-major traversal comes from the loop structure, not a Java requirement or a guarantee of contiguous row storage.
- 越界限制與操作失敗不矛盾：Java 阻止非法存取並拋出例外。
- Bounds restrictions and failed access describe different aspects of the same runtime check.
- String 物件不可變，但一般 String 變數仍可重新賦值。
- String objects are immutable; ordinary String variables can still be reassigned.
