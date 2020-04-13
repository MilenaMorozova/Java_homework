import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConvertString {
    final static String dateFormat = "dd.MM.yyyy";
    private String fio;
    private String birthday;

    public ConvertString(String data) throws IncorrectInputException {
        String[] result = extractFIOBirthdayFromString(data.trim());
        if(result != null){
            setFio(result[0]);
            setBirthday(result[1]);
        }
        else{
            throw new IncorrectInputException("String without separators");
        }
    }

    private String[] extractFIOBirthdayFromString(String data){
        int index = data.lastIndexOf(" ");
        if(index != -1){
            String[] result = new String[2];
            result[0] = data.substring(0, index);
            result[1] = data.substring(index + 1);
            return result;
        }
        return null;
    }

    public void setFio(String fio) throws IncorrectInputException {
        if(fio.equals("") == false) {
            this.fio = fio;
        }
        else {
            throw new IncorrectInputException("FIO");
        }
    }

    public String getFio() {
        return fio;
    }

    public void setBirthday(String birthday) throws IncorrectInputException{
        if(isDate(birthday)) {
            this.birthday = birthday;
        }
        else{
            throw new IncorrectInputException("BIRTHDAY");
        }
    }

    public String getBirthday() {
        return birthday;
    }

    private boolean isDate(String birthday){
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setLenient(false);
        return formatter.parse(birthday, new ParsePosition(0)) != null;
    }

    private int[] parseDateToInteger(){
        String[] date = birthday.split("\\.");
        int[] dateInt = new int [3];
        for(int i = 0; i < 3; i++){
            dateInt[i] = Integer.parseInt(date[i].trim());
        }
        return dateInt;
    }

    private int age() {
        int[] parsedBirthday = parseDateToInteger();
        LocalDate dateOfBirth = LocalDate.of(parsedBirthday[2], parsedBirthday[1], parsedBirthday[0]);
        LocalDate now = LocalDate.now();
        return Period.between(dateOfBirth, now).getYears();
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

    private String FIO(){
        String[] temp = fio.split("\\s+", 2);
        String result = temp[0];
        Pattern pattern = Pattern.compile("[\\s|\\-][а-яА-ЯёЁa-zA-Z]");
        Matcher matcher = pattern.matcher(fio);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            result += fio.substring(start, end).toUpperCase()+'.';
        }
        return result;
    }

    @Override
    public String toString() {
        return FIO()+' '+sex()+' '+age();
    }
}
