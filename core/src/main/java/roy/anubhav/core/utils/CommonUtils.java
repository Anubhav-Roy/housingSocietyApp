package roy.anubhav.core.utils;

import android.content.Context;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class CommonUtils {

    public static String formatDate( Context context,long time) {

        DateTime dateTime = new DateTime(time);
        StringBuilder formattedDateBuilder = new StringBuilder();
        DateTime now = DateTime.now().withTimeAtStartOfDay();
        DateTime yesterday = now.minusDays(1);
        LocalDate dayOfDate = dateTime.withTimeAtStartOfDay().toLocalDate();

        if (dayOfDate.equals(now.toLocalDate())) {
            DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(context);
            formattedDateBuilder.append(dateFormat.format(dateTime.toDate().getTime()));
        } else if (dayOfDate.equals(yesterday.toLocalDate())) {
            formattedDateBuilder.append("Yesterday");
        } else {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            formattedDateBuilder.append(dateFormat.format(dateTime.toDate()));
        }


        return formattedDateBuilder.toString();
    }

}
