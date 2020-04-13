import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.println("Введите ФИО и дату рождения(dd.MM.yyyy):");
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