public class Main {
    public static void main(String[] args) {
        Frequency frequency = new Frequency("input.txt", "frequency.txt");
        frequency.readFile();
        frequency.writeToFile();
    }
}