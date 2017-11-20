package elasticsearch.service.utils;

/**
 * Created by Ghazi Ennacer on 20/11/2017.
 */
public class SpecialCharacterTransient {

    public static String appendBackSlash(String text) {
        String[] specialCharacters = new String[]{"\\+", "-", "\\*", "/", "\\.", "\\(", "\\)", "%", "\"", ":", ","};
        for (int i = 0; i < specialCharacters.length; i++) {
            text = text.replaceAll(specialCharacters[i], "\\\\" + String.valueOf(specialCharacters[i]));
            System.out.println(text);
        }
        return text;
    }

    public static void main(String[] args) {
        String text = "kjs-+*/:,%().jhs";
        appendBackSlash(text);
    }
}
