# CSYE6200 — Object-Oriented Design

Java 課堂程式與學習筆記，依週次整理。筆記以英文技術名詞搭配中文解釋；每份筆記都有繁體中文與英文（`.en.md`）兩個版本。

## 課程導覽

| 週次 | 主題 | 程式 | 筆記 |
|---|---|---|---|
| [Week 3](week-03/README.md) | Arrays, Strings, and I/O | [src](week-03/src) | [繁體中文](week-03/notes/notes.zh-TW.md) · [English](week-03/notes/notes.en.md) · [Array 精簡版](week-03/notes/array-key-points.zh-TW.md) ([English](week-03/notes/array-key-points.en.md)) |
| [Week 4](week-04/README.md) | Classes, Objects, Constructors, Static Fields | [src](week-04/src) | [p.10–28、p.39 筆記](week-04/notes/classes-and-objects.zh-TW.md) ([English](week-04/notes/classes-and-objects.en.md)) · [Instance / Static](week-04/notes/instance-vs-static.zh-TW.md) ([English](week-04/notes/instance-vs-static.en.md)) |
| [Week 5](week-05/README.md) | Encapsulation, Overloading, Wrapper Classes, Command-Line Arguments, Package, JavaDoc | [src](week-05/src) | [Encapsulation / CLA / JavaDoc](week-05/notes/encapsulation-cla-packages-javadoc.zh-TW.md) ([English](week-05/notes/encapsulation-cla-packages-javadoc.en.md)) |
| [Week 6](week-06/README.md) | Inheritance, super, protected, Overriding | [src](week-06/src) | [Inheritance / super / Overriding](week-06/notes/inheritance-and-super.zh-TW.md) ([English](week-06/notes/inheritance-and-super.en.md)) |
| [Week 8](week-08/README.md) | ArrayList, Abstract Classes, Interfaces | [src](week-08/src) | [ArrayList / Abstract / Interface](week-08/notes/arraylist-abstract-interfaces.zh-TW.md) ([English](week-08/notes/arraylist-abstract-interfaces.en.md)) |
| [Week 9](week-09/README.md) | Enum Types；main、args 與 Scanner；Exception Handling | [src](week-09/src) | [Enum](week-09/notes/enum.zh-TW.md) ([English](week-09/notes/enum.en.md)) · [main / Scanner](week-09/notes/main-and-scanner.zh-TW.md) ([English](week-09/notes/main-and-scanner.en.md)) · [Exception Handling](week-09/notes/exception-handling.zh-TW.md) ([English](week-09/notes/exception-handling.en.md)) |
| [Week 10](week-10/README.md) | Generics | [src](week-10/src) | [Generics（p.1–12）](week-10/notes/generics.zh-TW.md) ([English](week-10/notes/generics.en.md)) |

## 講義與筆記對照

資料夾以上課週次命名，和講義檔名的編號不一定相同。

| 講義 | 筆記位置 | 已整理 | 尚未整理 |
|---|---|---|---|
| `1_OverView+of+OOP+using+Java.pdf`（48 頁） | — | — | 全部 |
| `2_Program Flow Control.pdf`（17 頁） | — | — | 全部 |
| `3_Arrays_String_InputOutput.pdf`（28 頁） | Week 3 | p.1–28 | — |
| `4_Classes_And_Objects.pdf`（57 頁） | Week 4、Week 5 | p.10–35、39、41、44–57 | p.1–9、36–38、40、42–43 |
| `5_Encapsulation.pdf`（14 頁） | Week 5 | p.1–13 | — |
| `6_CLA_Packages_JavaDoc.pdf`（19 頁） | Week 5 | p.3–19 | — |
| `7_Inheritance and Polymorphism.pdf`（51 頁） | Week 6 | p.3–28 | p.29–50（Object class、final、binding、polymorphism、casting、instanceof） |
| `8_Abstract classes And Interfaces.pdf`（32 頁） | Week 8、Week 9 | p.1–31 | — |
| `9_Exception Handling in Java.pdf`（41 頁） | Week 9 | p.3–30 | p.31–40（Unit Testing、JUnit） |
| `10_Generics.pdf`（21 頁） | Week 10 | p.1–12（p.9–12 依課堂白板整理） | p.13–20 |

## Directory structure

```text
.
├── README.md
├── .gitignore
├── week-03/
│   ├── README.md
│   ├── src/
│   └── notes/
├── week-04/
│   ├── README.md
│   ├── src/
│   ├── notes/
│   ├── .project
│   ├── .classpath
│   └── .settings/
├── week-05/
│   ├── README.md
│   ├── src/
│   └── notes/
├── week-06/
│   ├── README.md
│   ├── src/
│   └── notes/
├── week-08/
│   ├── README.md
│   ├── src/
│   └── notes/
├── week-09/
│   ├── README.md
│   ├── src/
│   └── notes/
└── week-10/
    ├── README.md
    ├── src/
    └── notes/
```

每週的 Java 原始碼放在 `src/`，Markdown 筆記放在 `notes/`。週次使用小寫與兩位數編號；Java 檔名維持與 public class name 相同的 PascalCase。編譯輸出放在各週 `bin/`，由 Git 忽略。

## 執行範例

需安裝 JDK，並能在終端機使用 `javac` 與 `java`；Week 4 的 Eclipse 設定使用 JavaSE-25。

在 repository 根目錄執行：

```sh
javac -encoding UTF-8 -d week-04/bin week-04/src/*.java
java -cp week-04/bin Person
```

Person 的預期輸出為三行 `2`。各週 README 列出更多使用方式。每週獨立編譯，避免不同週的同名 classes 混在一起。

## 路徑調整

| 原位置 | 新位置 |
|---|---|
| `week3/*.java` | `week-03/src/` |
| `week3/notes.*.md` | `week-03/notes/` |
| `Week-4-Example/` | `week-04/` |
| `notes/Classes_And_Objects_p10-15.md` | `week-04/notes/classes-and-objects.zh-TW.md` |

舊的檔案網址需要改用以上新路徑。筆記檔名不再包含頁數，方便後續延伸；目前涵蓋範圍以文件標題為準。
