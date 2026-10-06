
public class ArrayExample {

    public static void main(String[] args) {
        int table [][] = {{1,2,3},{4,5,6},{7,8,9},{10,11,12}};
        for(int i = 0; i<table.length;i++)
        {
            for (int j = 0; j<table[i].length;j++)
            {
                System.out.print(table[i][j] + " ");
                
            }
            System.out.println();
        }
        
        for(int [] row: table)
        {
            for (int element: row)
            {
                System.out.print(element + " ");
                
            }
            System.out.println();
        }

    }

}
