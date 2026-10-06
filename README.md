# CSYE6200 — Object-Oriented Design

Java 課堂程式與學習筆記，依週次整理。筆記以英文技術名詞搭配中文解釋；Week 3 另有英文版本。

## 課程導覽

| 週次 | 主題 | 程式 | 筆記 |
|---|---|---|---|
| [Week 3](week-03/README.md) | Arrays, Strings, and I/O | [src](week-03/src) | [繁體中文](week-03/notes/notes.zh-TW.md) · [English](week-03/notes/notes.en.md) |
| [Week 4](week-04/README.md) | Classes, Objects, Constructors, Static Fields | [src](week-04/src) | [p.10–28、p.39 筆記](week-04/notes/classes-and-objects.zh-TW.md) · [Instance / Static](week-04/notes/instance-vs-static.zh-TW.md) |

## Directory structure

```text
.
├── README.md
├── .gitignore
├── week-03/
│   ├── README.md
│   ├── src/
│   └── notes/
└── week-04/
    ├── README.md
    ├── src/
    ├── notes/
    ├── .project
    ├── .classpath
    └── .settings/
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
