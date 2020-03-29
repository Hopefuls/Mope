package me.hope.franxxmin.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;

public class TimestampResolver {
    private Timestamp timestamp;

public TimestampResolver(Timestamp timestamp) {

    this.timestamp = timestamp;
}

public String resolve() {


    String day = new SimpleDateFormat("dd").format(timestamp);

    if (day.startsWith("0")) {
        day = day.substring(1);
    }
    String dayform;
    if (day.endsWith("1")) {
        dayform = day+"st";
    } else if (day.endsWith("2")) {
        dayform = day+"nd";
    } else if (day.endsWith("3")) {
        dayform = day+"rd";
    } else {
        dayform = day+"th";
    }
    String year = new SimpleDateFormat("yyyy").format(timestamp);
    int month = Integer.parseInt(new SimpleDateFormat("MM").format(timestamp));
    String[] tempmonthname = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    String monthname = tempmonthname[month-1];



    return dayform+" "+monthname+" "+year;
}

}
