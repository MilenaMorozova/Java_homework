import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConvertString {
    private String fio;
    private String birthday;

    public ConvertString() {
        fio = "";
        birthday = "";
    }

    public ConvertString(String data) throws IncorrectInputException{
        String[] result = split(data.trim());
        if(result != null && checkString(result[0], result[1])){
            fio = capitalize(result[0]);
            birthday = result[1];
        }
        else{
            throw new IncorrectInputException();
        }
    }

    private String[] split(String data){
        int index = data.lastIndexOf(' ');
        if(index != -1){
            String[] result = new String[2];
            result[0] = data.substring(0, index);
            result[1] = data.substring(index + 1);
            return result;
        }
        return null;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getFio() {
        return fio;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    private boolean checkFIO(String fio){
        if(fio.equals("")){
            return false;
        }
        String[] result = fio.split("\\s+");
        if(result.length != 3){
            return false;
        }
        for(String word: result){
            if(word.matches("[А-Яа-яёЁ\\-]+") == false){
                return false;
            }
        }
        return true;
    }

    private boolean checkBirthday(String birthday){
        if(birthday.equals("")){
            return false;
        }
        if(birthday.matches("[^0-9.]+")){
            return false;
        }
        int[] daysOfYear = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int[] daysOfLeapYear = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int[] dateInt = dateToInteger(birthday);
        if(dateInt == null){
            return false;
        }
        if (dateInt[2] < 1900){  // year
            return false;
        }
        boolean isLeapYear = ((dateInt[2] % 4 == 0) && (dateInt[2] % 100 != 0) || (dateInt[2] % 400 == 0));
        if (dateInt[1] < 1 || dateInt[1] > 12){  //month
            return false;
        }
        if(dateInt[0] < 1){  //day
            return false;
        }
        if(isLeapYear && dateInt[0] > daysOfLeapYear[dateInt[1]-1]){
            return false;
        }
        if(isLeapYear == false && dateInt[0] > daysOfYear[dateInt[1]-1]){
            return false;
        }
        LocalDateTime time = LocalDateTime.of(dateInt[2], dateInt[1], dateInt[0], 0, 0, 0);
        LocalDateTime now = LocalDateTime.now();
        if(Duration.between(time, now).toDays() < 0){
            return false;
        }
        return true;
    }

    private int[] dateToInteger(String time){
        String[] date = time.split("\\.");
        if(date.length != 3){
            return null;
        }
        int[] dateInt = new int [3];
        for(int i = 0; i < 3; i++){
            dateInt[i] = Integer.parseInt(date[i].trim());
        }
        return dateInt;
    }

    private LocalDateTime convertBirthday(){
        int[] dateInt = dateToInteger(birthday);
        return LocalDateTime.of(dateInt[2], dateInt[1]-1, dateInt[0], 0, 0, 0);
    }

    //FIO and birthday
    private boolean checkString(String fio, String birthday){
        return checkFIO(fio) && checkBirthday(birthday);
    }

    private int age() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateBirthday = convertBirthday();
        return (int) (Duration.between(dateBirthday, now).toDays() / 365);
    }

    private String sex(){
        if(fio.endsWith("на")){
            return "женский";
        }
        else if(fio.endsWith("ич")){
            return "мужской";
        }
        else{
            return "неопознан";
        }
    }

    private String capitalize(String str){
        if(str.equals("")){
            return str;
        }
        str = str.substring(0,1).toUpperCase()+ str.substring(1).toLowerCase();
        Pattern pattern = Pattern.compile("[\\s|\\-][а-яё]");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            str = str.substring(0,start+1)+str.substring(start+1, end).toUpperCase()+str.substring(end);
        }
        return str;
    }

    private String convertFIO(){
        if(fio.equals("")){
            return "";
        }
        String result = "";
        int space = fio.indexOf(' ');
        result += fio.substring(0,space);
        Pattern pattern = Pattern.compile("[\\s|\\-][А-ЯЁ]");
        Matcher matcher = pattern.matcher(fio);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            result += fio.substring(start, end)+'.';
        }
        return result;
    }

    @Override
    public String toString() {
        return convertFIO()+' '+sex()+' '+age();
    }
}
