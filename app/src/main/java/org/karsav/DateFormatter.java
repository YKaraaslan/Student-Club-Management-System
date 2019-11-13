package org.karsav;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class DateFormatter {

    public String format(String date) {

        String language = Locale.getDefault().getLanguage();
        String convTime = null;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date past = inputFormat.parse(date);
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
            if (language.equalsIgnoreCase("tr")) {
                if (seconds < 60) {
                    convTime = seconds + " Saniye";
                } else if (minutes < 60) {
                    convTime = minutes + " Dakika";
                } else if (hours < 24) {
                    convTime = hours + " Saat";
                } else if (days >= 7) {
                    if (days > 30) {
                        convTime = (days / 30) + " Ay";
                    } else {
                        convTime = (days / 7) + " Hafta";
                    }
                } else {
                    convTime = days + " Gün";
                }
            } else {
                if (seconds < 60) {
                    if (seconds == 1)
                        convTime = seconds + " Second";
                    else
                        convTime = seconds + " Seconds";
                } else if (minutes < 60) {
                    if (minutes == 1)
                        convTime = minutes + " Minute";
                    else
                        convTime = minutes + " Minutes";
                } else if (hours < 24) {
                    if (hours == 1)
                        convTime = hours + " Hour";
                    else
                        convTime = hours + " Hours";
                } else if (days >= 7) {
                    if (days > 30) {
                        if (days / 30 == 1)
                            convTime = (days / 30) + " Month";
                        else
                            convTime = (days / 30) + " Months";
                    } else {
                        if (days / 7 < 2)
                            convTime = (days / 7) + " Week";
                        else
                            convTime = (days / 7) + " Weeks";
                    }
                } else {
                    if (days == 1)
                        convTime = days + " Day";
                    else
                        convTime = days + " Days";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convTime;
    }
}
