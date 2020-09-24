package turkuvaz.sdk.global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ahmet MUŞLUOĞLU on 1/14/2020.
 */
public class DateUtils {
    private static Locale localeTR = new Locale("tr");

    public static String format1(String currDate) {
        String formmattedDate = " ";
        if (currDate == null)
            return formmattedDate;

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", localeTR);
            Date date = format.parse(currDate);
            format = new SimpleDateFormat("dd MMMM, yyyy HH:mm", localeTR);
            formmattedDate = format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formmattedDate;
    }

    public static String format2(String currDate) {
        String formattedDate = " ";
        if (currDate == null)
            return formattedDate;

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", localeTR);
            Date date = format.parse(currDate);
            format = new SimpleDateFormat("yyyy-MM-dd", localeTR);
            formattedDate = format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    public static int get_HH(String currDate) {//get-hour
        int formattedDate = 0;
        if (currDate == null)
            return formattedDate;

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", localeTR);
            Date date = format.parse(currDate);
            format = new SimpleDateFormat("HH", localeTR);
            formattedDate = Integer.parseInt(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    public static int get_mm(String currDate) {//get-minutes
        int formattedDate = 0;
        if (currDate == null)
            return formattedDate;

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", localeTR);
            Date date = format.parse(currDate);
            format = new SimpleDateFormat("mm", localeTR);
            formattedDate = Integer.parseInt(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    public static int getPartOfDate(String currDate, String part) {//get-minutes
        int formattedDate = 0;
        if (currDate == null)
            return formattedDate;

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", localeTR);
            Date date = format.parse(currDate);
            format = new SimpleDateFormat(part, localeTR);
            formattedDate = Integer.parseInt(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }
}
