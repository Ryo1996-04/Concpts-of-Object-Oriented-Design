/**
 * Slide p.15 Stack<E>, corrected and completed.
 * "new E[size]" is not allowed (generic array creation), so the array is
 * created as Object[] and cast to E[]. The array stays private, and only
 * push(E) can put elements into it, so it only ever holds E values.
 */
public class GenericStack<E> {
    private final int size;    // capacity of the stack
    private int top;           // index of the top element
    private E[] elements;      // array that stores stack elements

    @SuppressWarnings("unchecked")   // the cast is safe: elements stays private
    public GenericStack(int s) {
        size = s > 0 ? s : 10;       // the slide wrote "int size = ...", which shadows the final field
        top = -1;                    // stack is initially empty
        elements = (E[]) new Object[size];
    }

    public void push(E value) {
        if (top == size - 1) {
            throw new IllegalStateException("Stack is full");
        }
        elements[++top] = value;
    }

    public E pop() {
        if (top == -1) {
            throw new IllegalStateException("Stack is empty");
        }
        return elements[top--];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    /** Shows the real runtime type of the array. */
    public String arrayType() {
        return elements.getClass().getSimpleName();
    }

    public static void main(String[] args) {
        GenericStack<Double> doubleStack = new GenericStack<>(5);
        GenericStack<Integer> integerStack = new GenericStack<>(3);

        doubleStack.push(1.1);
        doubleStack.push(2.2);
        integerStack.push(10);
        integerStack.push(20);
        // integerStack.push("thirty");   // compile error: String is not Integer

        System.out.println("pop doubleStack:  " + doubleStack.pop());
        System.out.println("pop integerStack: " + integerStack.pop());
        System.out.println("runtime array type: " + integerStack.arrayType());

        Object[] arr = new String[1];
        try {
            arr[0] = 10;                     // arrays check element types at runtime
        } catch (ArrayStoreException e) {
            System.out.println("ArrayStoreException: " + e.getMessage());
        }
    }
}
