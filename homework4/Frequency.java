import java.io.*;
import java.util.HashMap;

public class Frequency {
    private String inputFileName;
    private String outputFileName;
    private HashMap<Character, Integer> dictionary;

    public Frequency(String inputFileName, String outputFileName){
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        dictionary = new HashMap<>();
    }

    public Frequency(String inputFileName){
        this.inputFileName = inputFileName;
        outputFileName = "frequency.txt";
        dictionary = new HashMap<>();
    }

    public void readFile(){
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            int character;
            while ((character = reader.read()) != -1) {
                if ((65 <= character && character <= 90) || (97 <= character && character <= 122)){
                    Character letter = Character.toLowerCase((char)character);
                    if (dictionary.containsKey(letter) == false) {
                        dictionary.put(letter, 1);
                    } else {
                        dictionary.put(letter, dictionary.get(letter) + 1);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }
    }

    public void writeToFile(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            writer.write("Result:\n");
            if(dictionary.isEmpty()){
                writer.write("No English letters\n");
            }
            else{
                for (Character key : dictionary.keySet()) {
                    writer.write(key + " - " + dictionary.get(key) + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }
    }
}
