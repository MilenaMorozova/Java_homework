import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.println("Input FIO and birthday:");
            String s = in.nextLine();
            try {
                ConvertString example = new ConvertString(s);
                System.out.println(example);
            } catch (IncorrectInputException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}