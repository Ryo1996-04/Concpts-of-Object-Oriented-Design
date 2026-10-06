# Instance 與 Static：投影片對照筆記

對應 `5_Classes_And_Objects.pdf`：p.29–31 的 variables 與 Person counter，以及 p.32–35 的 methods。本篇是主筆記 p.10–28 之後的主題補充。

## 先釐清名詞

- **Instance**：由 class 建立出來的 object，例如 `new Person()` 建立的 object。
- **Reference variable**：保存 object reference 的 variable，例如 `Person p1` 中的 `p1`。
- **Instance variable / instance field**：每個 object 各自擁有的 field，例如 Person 的 `name`、`age`。
- **Class variable / static field**：帶有 `static` 的 field，同一個 class 的 objects 共用一份，例如 `counter`。

所以 instance 本身不能直接翻譯成「物件變數」。`p1` 若宣告在 main 裡，是 local reference variable；`p1.age` 存取的 `age` 才是 instance field。

## p.29–30：Instance Variable 與 Class Variable

| 比較 | Instance variable | Class variable / static field |
|---|---|---|
| 宣告 | Class 內、method 外，不加 static | Class 內、method 外，加 static |
| 歸屬 | 特定 object | Class |
| 資料份數 | 每個 object 各一份 | 同一個 class 共用一份 |
| 存取方式 | `p1.age` | 建議 `Person.counter` |
| 是否需要先建立 object | 需要 object 才有該 instance 的 field | 不需要 instance 即可存取 class field |
| 適用資料 | 各人的姓名、年齡 | 累計建立次數等共用資料 |
| 是否有 default value | 有 | 有 |

```java
class Person {
    String name;            // Instance field：每個人各自的資料
    int age;                // Instance field
    static int counter = 0; // Class variable：共用計數
}
```

這是 fields 的示意，不含下方 counter 範例的 constructors。Instance 與 static fields 都有依 type 決定的 default values；local variable 則必須先賦值才能讀取。

## p.31：Person Counter 完整範例

對應 repository 的 [Person.java](../src/Person.java)：

```java
public class Person {
    String name;
    int age;
    static int counter = 0;

    public Person() {
        this("", 0); // 同一個 object 的 constructor chaining
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        counter++; // 每次建立 object 累計一次
    }

    public static void main(String[] args) {
        Person p1 = new Person();
        Person p2 = new Person("Smith", 32);

        System.out.println(p1.counter);     // 2
        System.out.println(p2.counter);     // 2
        System.out.println(Person.counter); // 2，建議使用此寫法
    }
}
```

流程：class 初始化時 counter 為 0；建立 p1 後為 1；建立 p2 後為 2。三次 println 都在兩個 objects 建立後執行，因此讀取同一個值 2。

```text
Person class：counter = 2（共享）

p1 -> object A：name = ""，age = 0
p2 -> object B：name = "Smith"，age = 32
```

`this("", 0)` 只委派初始化工作，沒有額外建立另一個 object。`counter++` 集中在 parameterized constructor，所以此範例每次 object creation 只加一次。

### 改 Instance Field 與 Static Field 的不同結果

在上述 main 的最後加入：

```java
p1.age = 99;
System.out.println(p1.age); // 99
System.out.println(p2.age); // 32：另一個 object 的 age 沒有改變

Person.counter = 10;
System.out.println(p1.counter); // 10
System.out.println(p2.counter); // 10：讀的是同一份 static field
```

上述直接設定 counter 只是為了展示共享效果；實際計數程式通常不讓外部任意改 counter。

若把 `static int counter = 0;` 改成 `int counter = 0;`，每個 object 都各自從 0 開始，各執行一次 `counter++` 後，p1.counter 和 p2.counter 都是 1。此時 `Person.counter` 會 compile-time error，因為必須指定 object。

Counter 表示本範例累計建立次數，不是目前存活 objects 的數量；garbage collection 不會自動減一。它也不是多執行緒安全的計數器，此範例先以單執行緒理解。

## p.32–34：Instance Method 與 Static Method

| 比較 | Instance method | Static method |
|---|---|---|
| 宣告 | 沒有 static | 加上 static |
| 常見呼叫 | `p1.getName()` | `Person.getCount()` |
| 是否有目前的 receiver | 有，能使用 `this` | 沒有隱含 receiver，不能使用 `this` |
| 直接使用 instance fields | 可以存取自身可見 fields | 必須先取得明確的 object reference |
| 使用 static fields | 可以 | 可以 |
| 常見用途 | 讀取或操作某個 object 的 state | Class 共用操作或工具計算 |

以下是獨立、可編譯的對照範例，存為 `MemberDemo.java`：

```java
public class MemberDemo {
    int age = 20;
    static int count = 1;

    int getAge() {
        return this.age; // Instance method，有 receiver
    }

    static int getCount() {
        return count; // Static method 存取 static field
    }

    static int readAge(MemberDemo person) {
        return person.age; // 合法：透過明確的 object reference
    }

    public static void main(String[] args) {
        MemberDemo p = new MemberDemo();
        System.out.println(p.getAge());          // 20
        System.out.println(MemberDemo.getCount()); // 1
        System.out.println(MemberDemo.readAge(p)); // 20

        // System.out.println(age);      // Compile-time error：未指定 object
        // System.out.println(this.age); // Compile-time error：static context 沒有 this
    }
}
```

投影片 p.33–34 的「static method 不能存取 instance variables」需要補充：**不能在沒有 receiver 的情況下直接存取，但可以使用明確的 object reference。** 上例的 `person.age` 就是合法寫法，仍須符合 access control；person 為 null 時會出現 NullPointerException。

## p.35：MyMath 為什麼適合 Static？

```java
public static double square(double x) {
    return x * x;
}
```

這個 method declaration 放在 MyMath class 裡。`MyMath.square(5)` 只需 argument 5，就能得到 25.0，不需要某個 MyMath object 的 state，因此適合 static。

相較之下，`Rectangle.area()` 若計算目前 object 的 length × width，就適合 instance method，因為不同 rectangles 有不同尺寸。

## 常見混淆與選擇方式

- **Static 不代表 constant。** `static int counter` 可以修改；限制重新 assignment 的 keyword 是 `final`。
- **Main 是 static，不代表裡面的 variables 都是 static。** `Person p1` 在 main 內仍是 local variable。
- **不要只為修掉編譯錯誤就把 fields 全改 static。** 若把所有人的 age 改成 static，所有 objects 會共用同一個 age，通常不符合資料模型。
- **判斷資料是否屬於特定 object。** Name、age、rectangle width 通常各自不同；累計建立次數可由 class 共用。

## 投影片索引

| 頁碼 | 本篇對應內容 |
|---|---|
| p.29 | Instance、class、local variables 的區分 |
| p.30 | Static field 的共用特性 |
| p.31 | Person.counter 與三次輸出 2 |
| p.32 | Instance methods 與 object receiver |
| p.33–34 | Static methods 與 instance member access 的限制 |
| p.35 | MyMath 的 static 工具方法 |
