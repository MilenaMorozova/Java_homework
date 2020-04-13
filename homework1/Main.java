public class Main {
    public static void main(String[] args) {
        ComplexNumber Igor = new ComplexNumber(1, -2);
        ComplexNumber Sasha = new ComplexNumber(3, 4);
        ComplexNumber result;
        //summa
        result = Igor.sum(Sasha);
        System.out.println("Summa\n" + Igor + " + " + Sasha + " = " + result);
        //difference
        result = Igor.sub(Sasha);
        System.out.println("\nDifference\n" + Igor + " - " + Sasha + " = " + result);
        //multiply
        result = Igor.mul(Sasha);
        System.out.println("\nMultiplication\n" + Igor + " * " + Sasha + " = " + result);
        //divide
        result = Igor.div(Sasha);
        System.out.println("\nDivider\n" + Igor + " / " + Sasha + " = " + result);
    }
}
