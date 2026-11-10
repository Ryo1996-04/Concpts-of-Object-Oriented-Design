public enum Move {
    Left(-10, 0), Right(10, 0), Up(0, 10), Down(0, -10);

    private final int x, y;          // 每一步在 x、y 方向的變化量

    private Move(int x, int y) {     // Left(-10, 0) 會呼叫這個 constructor
        this.x = x;
        this.y = y;
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }

    public static void main(String[] args) {
        int px = 0, py = 0;           // 起點 (0, 0)
        Move[] path = { Move.Up, Move.Up, Move.Right, Move.Down, Move.Left, Move.Left };
        for (Move m : path) {
            px += m.getX();
            py += m.getY();
            System.out.println(m + " -> (" + px + ", " + py + ")");
        }
    }
}
