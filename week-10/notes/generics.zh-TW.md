# CSYE6200 筆記：Generics（泛型）

> 個人學習筆記，課堂內容以自己的話重新整理。本文不含投影片原文或圖片，標註的頁碼只用來對照課程講義；所有範例程式都是自己寫的，錯誤訊息與輸出都是實際用 `javac` / `java`（JDK 25）跑出來的。

- 課程：CSYE6200（Fall 2026）
- 單元：Generics（`10_Generics.pdf`，共 21 頁；本篇目前整理到 **p.8**，p.9 起待續）
- 日期：2026-11-17（課堂進度到 p.8；另有課堂討論：C 語言編譯、static／late binding、編譯器與 JVM 的順序，見第 11 節）
- 可執行的範例程式：[`../src/`](../src)（PrintArrayOverload、GenericPrint）
- 先備：Week 5 Method Overloading、Wrapper Classes；Week 8 [ArrayList 的 Generics 補充](../../week-08/notes/arraylist-abstract-interfaces.zh-TW.md#補充generics泛型)

## 目錄

1. [問題：同一件事，不同型別（p.2）](#1-問題同一件事不同型別p2)
2. [解法一：Overloading（p.3）](#2-解法一overloadingp3)
3. [能不能只寫一個方法（p.4）](#3-能不能只寫一個方法p4)
4. [Generic method 的寫法（p.5）](#4-generic-method-的寫法p5)
5. [呼叫 Generic method（p.6）](#5-呼叫-generic-methodp6)
6. [Generics 的兩個好處（p.7）](#6-generics-的兩個好處p7)
7. [Generic method 的規則與名詞（p.8）](#7-generic-method-的規則與名詞p8)
8. [講義語法更正](#8-講義語法更正)
9. [常見錯誤](#9-常見錯誤)
10. [檢查清單](#10-檢查清單)
11. [課堂討論：C 語言編譯、Binding、編譯器與 JVM](#11-課堂討論c-語言編譯binding編譯器與-jvm)
12. [課堂補充（待補）](#12-課堂補充待補)

---

## 1. 問題：同一件事，不同型別（p.2）

需求：有一個 `int` 陣列、一個 `double` 陣列、一個 `long` 陣列（也可能還有 `float`），每個都要「把元素一個個印出來」。

做的事情完全一樣，只有**元素型別**不同。Java 是強型別語言，`int[]` 的參數不能收 `double[]`，所以第一個想到的解法是 Week 5 學過的 **method overloading**。

## 2. 解法一：Overloading（p.3）

同一個方法名稱 `printArray`，寫三個版本，參數型別分別是 `int[]`、`double[]`、`long[]`。三個方法的 signature 不同（參數型別不同），所以可以共存；呼叫時編譯器依引數型別選版本（複習 Week 5：signature = 名稱 + 參數的數量與型別，不含 return type）。

範例：[`PrintArrayOverload.java`](../src/PrintArrayOverload.java)

```java
public static void printArray(int[] inputArray) {
    for (int element : inputArray) {
        System.out.printf("%s ", element);
    }
    System.out.println();
}
// double[] 版、long[] 版：只有兩個地方的型別不同，其餘一模一樣
```

輸出：

```text
1 2 3 
1.5 2.5 
10000000000 20000000000 
```

**這個解法的問題**

| 問題 | 說明 |
|---|---|
| 重複程式碼 | 三個方法除了型別以外每一行都相同 |
| 沒寫到的型別不能用 | 想印 `float[]`、`String[]` 就要再寫一個；沒寫就編譯錯誤 |
| 難維護 | 想把分隔的空白改成逗號，要改每一個版本，漏改一個就不一致 |

> 補充：`printf("%s ", element)` 對任何型別都能用。`%s` 會把值轉成字串（物件呼叫 `toString()`），所以 `int`、`double`、`Character`、`String` 都印得出來。這也是 p.5 的 generic 版本能沿用同一行的原因。

## 3. 能不能只寫一個方法（p.4）

講義提問：能不能只寫**一個**方法，接受「任何型別」的陣列並印出元素？答案是可以，用 **generics**。

核心想法：把「型別」也變成一個參數。平常的參數傳的是**值**；generics 多了一種參數，傳的是**型別**。

## 4. Generic method 的寫法（p.5）

範例：[`GenericPrint.java`](../src/GenericPrint.java)

```java
public static <E> void printArray(E[] inputArray) {
    for (E element : inputArray) {
        System.out.printf("%s ", element);
    }
    System.out.println();
}
```

逐段拆解方法標頭：

| 片段 | 意思 |
|---|---|
| `public static` | 和一般方法一樣的修飾詞（講義漏了 `static`，見[第 8 節](#8-講義語法更正)） |
| `<E>` | **type parameter section**：宣告「這個方法有一個型別參數，叫 E」。一定寫在 **return type 前面** |
| `void` | return type |
| `printArray` | 方法名稱 |
| `E[] inputArray` | 參數：一個「E 型別的陣列」 |
| `for (E element : ...)` | 方法內也用 E 宣告 local variable |

和 overloading 版對照：把 `int` / `double` / `long` 全部換成 `E`，再在前面加 `<E>` 宣告它。三個方法變成一個。

> 記法：`<E>` 是「先宣告」，後面的 `E` 是「使用」。就像變數要先宣告才能用，型別參數也要先在 `< >` 裡宣告。

## 5. 呼叫 Generic method（p.6）

```java
Integer[] intArray = {1, 2, 3, 4, 5};
Double[] doubleArray = {1.1, 2.2, 3.3, 4.4};
Character[] charArray = {'H', 'E', 'L', 'L', 'O'};

printArray(intArray);     // E = Integer
printArray(doubleArray);  // E = Double
printArray(charArray);    // E = Character
```

輸出：

```text
Array integerArray contains:
1 2 3 4 5 

Array doubleArray contains:
1.1 2.2 3.3 4.4 

Array characterArray contains:
H E L L O 
```

重點：

1. **呼叫時不用寫型別**：編譯器看引數是 `Integer[]`，就推斷 `E = Integer`（type inference）。要明寫也可以：`GenericPrint.<Double>printArray(doubleArray);`，但很少需要。
2. **陣列是 `Integer[]`，不是 `int[]`**：E 只能代表 reference type（p.8 規則）。所以要用 Week 5 的 wrapper class：`Integer`、`Double`、`Character`。
3. **`{1, 2, 3}` 可以放進 `Integer[]`**：這是 autoboxing，`int` 自動包成 `Integer`。
4. **新型別不用新方法**：範例多加了 `String[]`，同一個方法直接能印：

```text
Array stringArray contains:
Java Generics 
```

## 6. Generics 的兩個好處（p.7）

### 好處一：一份宣告，代表一整組

- 一個 **generic method** 等於一整組相關的方法（例如 p.5 一個方法就代替 p.3 的三個）。
- 一個 **generic class** 等於一整組相關的型別（例如 Week 8 的 `ArrayList<String>`、`ArrayList<Integer>` 都來自同一個 `ArrayList<E>` class；講義 p.14 起會講自己寫 generic class）。
- 講義的例子：寫一個 generic 的排序方法，就能拿來排 `Integer[]`、`Double[]`、`String[]`。

### 好處二：Compile-time type safety（編譯時期型別安全）

錯誤型別在**編譯時**就被抓出來，不必等到執行才當掉。對照「不用 generics、改用 `Object`」的寫法最清楚：

不用 generics：參數和回傳都是 `Object`，取出來要自己轉型。

```java
public static Object first(Object[] a) { return a[0]; }

String[] words = {"a", "b"};
Integer n = (Integer) first(words);   // 能編譯，執行才出錯
```

```text
Exception in thread "main" java.lang.ClassCastException: class java.lang.String cannot be cast to class java.lang.Integer ...
	at NoGeneric.main(NoGeneric.java:5)
```

用 generics：

```java
public static <E> E first(E[] a) { return a[0]; }

String[] words = {"a", "b"};
Integer n = first(words);             // 編譯就失敗
```

```text
error: incompatible types: inference variable E has incompatible bounds
        Integer n = first(words);
                         ^
    upper bounds: Integer,Object
    lower bounds: String
```

| | 用 `Object` | 用 generics |
|---|---|---|
| 取出時要不要轉型 | 要 `(Integer)` | 不用，編譯器知道 E 是什麼 |
| 型別錯了何時發現 | **執行時** `ClassCastException`（runtime error） | **編譯時** error（compile-time error） |

> 連回 Week 9：runtime error 要等程式跑到那一行才知道；compile-time error 在寫程式時就被擋下來。Generics 把一部分 runtime error 提前變成 compile-time error。

## 7. Generic method 的規則與名詞（p.8）

### 名詞對照

| 名詞 | 意思 | 在 `public static <E> void printArray(E[] a)` 裡 |
|---|---|---|
| **Type parameter section** | 用角括號 `< >` 包起來的區塊，寫在 return type 前 | `<E>` |
| **Type parameter**（也叫 **formal type parameter**、**type variable**） | 代表「某個型別」的名稱 | `E` |
| **Actual type argument** | 呼叫時實際代入的型別 | `Integer`、`Double`、`Character` |

和一般參數類比：

| | 一般參數 | 型別參數 |
|---|---|---|
| 宣告 | `(int x)` 裡的 `x` | `<E>` 裡的 `E` |
| 呼叫時代入 | 值，例如 `5` | 型別，例如 `Integer` |
| 名稱 | formal parameter / actual argument | formal type parameter / actual type argument |

### 規則一：可以有多個型別參數，用逗號分開

```java
public static <K, V> void printPair(K key, V value) { ... }
```

### 規則二：型別參數可以用在三個地方

return type、參數型別、local variable 型別。[`GenericPrint.java`](../src/GenericPrint.java) 的 `first` 三種都用到：

```java
public static <E> E first(E[] array) {   // E 當 return type、當參數型別
    E result = array[0];                 // E 當 local variable 型別
    return result;
}

Integer x = first(intArray);   // 不用轉型
System.out.println(x + 1);     // 2
```

### 規則三：只能代表 reference type，不能是 primitive

`int`、`double`、`char` 等 8 種 primitive 都不行，要用 wrapper class。

把 `int[]` 傳給 `E[]`：

```text
error: method printArray in class Prim cannot be applied to given types;
        printArray(ints);
        ^
  required: E[]
  found:    int[]
  reason: inference variable E has incompatible bounds
    equality constraints: int
    upper bounds: Object
```

同樣的規則在 Week 8 的 `ArrayList` 也看過：

```text
error: unexpected type
        ArrayList<int> list = new ArrayList<>();
                  ^
  required: reference
  found:    int
```

> 為什麼只能 reference type？p.9 的 erasure 會解釋：編譯後 E 會被換成 `Object`，而 `Object` 只能指向物件，不能裝 primitive。

### 規則四：方法內用的名稱要和 `< >` 裡宣告的一致

宣告 `<E>`，卻在參數寫 `T[]`：

```java
public static <E> void printArray(T[] a) { }
```

```text
error: cannot find symbol
    public static <E> void printArray(T[] a) { }
                                      ^
  symbol:   class T
  location: class Name
```

編譯器把 `T` 當成一個叫 T 的 class 去找，找不到。

### 補充：命名慣例

型別參數習慣用**一個大寫字母**，避免和真正的 class 名稱搞混：

| 字母 | 常見用途 |
|---|---|
| `E` | Element（集合、陣列的元素），講義 p.5 用這個 |
| `T` | Type（一般型別），講義 p.11 起用這個 |
| `K`、`V` | Key、Value（例如 `HashMap<K, V>`） |
| `N` | Number |

## 8. 講義語法更正

- **p.5 少了 `static`**：講義的 `printArray` 沒有 `static`，但 p.6 的 `main` 是 `static`，直接呼叫會編譯錯誤（Week 5 學過：static 方法不能直接呼叫 instance 方法）：

  ```text
  error: non-static method <E>printArray(E[]) cannot be referenced from a static context
          printArray(intArray);
          ^
  ```

  兩頁合在一起要執行時，要寫成 `public static <E> void printArray(E[] inputArray)`。

- **p.6 的 `String args[]`**：這是 C 風格的陣列宣告，Java 允許，和 `String[] args` 完全一樣。一般慣例寫 `String[] args`。

## 9. 常見錯誤

| 錯誤寫法 | 結果 | 正確寫法 |
|---|---|---|
| `public static void <E> printArray(...)` | 編譯錯誤：`<E>` 位置錯 | `<E>` 放在 return type **前面**：`public static <E> void` |
| `int[] a = {1,2}; printArray(a);` | 編譯錯誤：E 不能是 `int` | `Integer[] a = {1,2};` |
| 宣告 `<E>`，內文用 `T` | `cannot find symbol: class T` | 名稱一致 |
| 非 static 的 generic method 從 `main` 直接呼叫 | `cannot be referenced from a static context` | 加 `static`，或先 `new` 物件再呼叫 |
| 以為 generics 讓 `printArray` 能收 `int[]` | 不行，primitive 陣列仍要 overload | 用 wrapper 陣列 |

## 10. 檢查清單

- [ ] 同樣的邏輯只差型別 → 考慮 generic method，而不是寫好幾個 overload
- [ ] `<E>` 寫在 return type 前，是「宣告」；後面的 `E` 是「使用」
- [ ] 呼叫時通常不用寫型別，編譯器會從引數推斷
- [ ] 型別參數只能是 reference type；數字要用 `Integer`、`Double` 等 wrapper
- [ ] E 可以當 return type、參數型別、local variable 型別
- [ ] 能說出 type parameter / type variable / actual type argument 的差別
- [ ] 能說出 generics 的兩個好處：一份宣告代表一組方法或型別；編譯時抓到型別錯誤

## 11. 課堂討論：C 語言編譯、Binding、編譯器與 JVM

老師在講 generics 時，順帶講了 C 語言怎麼編譯，以及 static binding 和 late binding 的差別。兩個話題要說的是同一件事：**哪些事在 compile time 決定，哪些事在 run time 才決定**，generics 的設計就是依照這條線劃分的。

> 下面「為什麼講這個」的部分，是依講義（Generics p.9–13、講義 7 p.35–50）整理出來的解釋；老師實際的說法和例子，記在[第 12 節](#12-課堂補充待補)。

### 1. 為什麼提 C 語言的編譯

用來對照 p.9 的 **erasure**。

| | C | Java |
|---|---|---|
| 編譯結果 | 直接編成特定 CPU／OS 的機器碼 | 編成 bytecode，交給 JVM 執行 |
| 能不能 overload | **不能**。函式名稱不能重複，只能寫 `printIntArray`、`printDoubleArray`…… | 可以（p.3） |
| 想寫「通用」的函式 | 用 `void*`（萬用指標，沒有型別檢查），或用 macro 替每個型別複製一份程式 | 用 generics |

所以 p.2–3 的問題在 C 裡更嚴重，C 連 overloading 都沒有。

Java 的做法（p.9）：編譯器先用 `<E>` **檢查型別**，再把 `E` **擦掉、換成 `Object`**。bytecode 裡只有一個 `printArray(Object[])`，不會替 Integer、Double 各生一份。由此可知：

- **型別安全只在 compile time 有效**：執行時 JVM 已經不知道 E 是什麼。
- **E 不能是 `int`**：擦掉後變成 `Object`，而 `Object` 只能指向物件、裝不了 primitive（[第 7 節規則三](#規則三只能代表-reference-type不能是-primitive)）。

> 補充：C++ 的 template 做法和 Java 相反，會替每個型別各產生一份程式碼。

### 2. Static binding 與 Late binding

Binding 的意思是：程式裡寫了 `obj.doIt()`，**決定實際執行哪一個方法本體**。差別在於什麼時候決定。

| | Static binding（early binding） | Late binding（dynamic binding） |
|---|---|---|
| 何時決定 | Compile time | Run time |
| 誰決定 | 編譯器，看**宣告的型別**和引數型別 | JVM，看**物件實際的型別** |
| 代表情況 | **Overloading**；`static`、`private`、`final` 方法 | **Overriding** |
| 講義 | Generics p.3、p.13（generic method 也能 overload，編譯器挑最精確的那個） | 講義 7 p.35、p.40 |

和 generics 的關係：

1. **p.3 的 overload 是 static binding**：`printArray(ints)` 呼叫 `int[]` 版，是編譯器在 compile time 就選好的。
2. **Generic method 能印出正確結果，靠的是 late binding**：擦除之後，`printArray` 裡的 `element` 在編譯器眼中只是 `Object`。

   ```java
   System.out.printf("%s ", element);   // 內部會呼叫 element.toString()
   ```

   編譯器只知道「呼叫 `Object` 的 `toString()`」。真正執行時，JVM 看物件實際的型別，跑 `Integer`、`Character`、`String` 各自改寫過的 `toString()`，所以印出 `1 2 3`、`H E L L O`，而不是記憶體位址。
3. p.10–11 的 `compareTo` 也一樣：`T extends Comparable<T>` 讓編譯器確定 T 一定有 `compareTo()`；執行時再由 late binding 呼叫 `Integer` 或 `String` 自己的版本。

> 考試提醒：講義 7 p.50 把 overloading 和 overriding 都歸為 polymorphism。前者常被稱為 compile-time polymorphism，後者稱為 run-time polymorphism。

### 3. 編譯器與 JVM 的執行順序

先編譯器，後 JVM，兩個是分開的步驟。

```text
Hello.java ──javac──► Hello.class ──java──► 執行結果
  原始碼      編譯器      bytecode     JVM（run time）
```

| 步驟 | 檔案／指令 | 誰做 | 做什麼 | Binding | 這時抓到的錯 |
|---|---|---|---|---|---|
| ① 原始碼 | `Hello.java` | 自己 | 寫 class、method、`<E>`，以及像 `obj.doIt()` 這樣的呼叫 | **還沒決定**：只寫了呼叫，還沒決定執行哪個方法本體 | 還沒檢查（Eclipse 的紅線其實是它在背景跑編譯器，屬於 ②） |
| ② 編譯器 | `javac Hello.java` | 編譯器 | 檢查語法和型別；generics 先檢查 `<E>`，再 **erasure** 成 `Object`；產生 `.class` | **Static binding 在這裡完成**：overloading 選哪一版、`static`／`private`／`final` 方法，由編譯器看宣告的型別決定 | **Compile-time error**：語法錯、型別不符、generics 型別錯、`int` 當 E |
| ③ Bytecode | `Hello.class` | （產物） | 和平台無關的位元組碼，任何有 JVM 的平台都能跑；E 已經變成 `Object` | Static binding 的結果**已經寫死**在 bytecode 裡；overriding 的呼叫只記下「呼叫 `Object.toString()`」，**留給 JVM 決定** | 無（這只是檔案）。改了 `.java` 卻沒重新 `javac`，執行的還是舊的 `.class`（Week 5 的 John 例子） |
| ④ Run time | `java Hello` | JVM | 載入 `.class`，把 bytecode 轉成機器碼執行 | **Late binding 在這裡發生**：overriding 依物件實際的型別執行，例如 `element.toString()` 跑的是 `Integer` 或 `Character` 自己的版本 | **Runtime error**：`ArithmeticException`、`ClassCastException`、陣列越界 |

和 C 的差別：C 編譯後直接產生機器碼，只有一步，而且換平台要重新編譯；Java 的 `.class` 換到任何有 JVM 的平台都能跑（write once, run anywhere）。

**一句話：**② 編譯器負責檢查、erasure 和 static binding；④ JVM 負責執行和 late binding；③ bytecode 是兩者之間的交接檔。

### 本節檢查清單

- [ ] 說得出 C 和 Java 編譯結果的差別（機器碼 vs bytecode）
- [ ] 說得出 erasure 在哪一步發生、為什麼 E 不能是 primitive
- [ ] Overloading → static binding（compile time）；overriding → late binding（run time）
- [ ] 說得出 generic method 能印出不同型別，是因為 late binding 呼叫各自的 `toString()`
- [ ] 能依序講出 原始碼 → 編譯器 → bytecode → JVM，以及每一步抓到哪一種錯

## 12. 課堂補充（待補）

> 老師上課額外講的例子、問答、白板內容記在這裡：

-

---

下次從 **p.9 Erasure** 繼續，接著是 p.10–13 `Comparable` 與 bounded type parameter（`<T extends Comparable<T>>`）、p.14–17 generic class（`Stack<E>`）、p.18–20 wildcards。
