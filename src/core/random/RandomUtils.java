package core.random;

import org.apache.commons.lang3.text.WordUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by serdyuk on 6/12/16.
 */
public class RandomUtils {

    Random randomGenerator = new Random();

    /**
     *
     * @return YYYY-MM-DD_HHMMSS String
     */
    public String getRandomDateForRegUser() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

    public String getDateAndTime() {
        return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public String getSeconds(){
        return new SimpleDateFormat("mmssS").format(Calendar.getInstance().getTime());
    }

    /**
     * @param max int
     * @return int value from 0 to "int max"
     */
    public int getUniqueNumWithMaxParam(int max) {
        return randomGenerator.nextInt(max);
    }

    /**
     * @param from int
     * @param to   int
     * @return range int (from - to)
     */
    public int randomNumber(int from, int to) {
        return randomGenerator.nextInt((to - from) + 1) + from;
    }

    /**
     * @return YYYY-MM-DD String
     */
    public String getDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }


    public String randomScreenName(){
        String alphabet = "abcdefghijklmnopqrstyvwxyz";

        String result = "";

        for(int i = 0; i < 7; i++){
            result += alphabet.charAt(randomNumber(0,25));
        }

        return WordUtils.capitalize(result);
    }

    public String randomEmail(String email) {

        String[] split = email.split("@");

        String first = split[0] + "+" + getSeconds() + randomNumber(100, 999);

        return  first+"@"+split[1];
    }
}
