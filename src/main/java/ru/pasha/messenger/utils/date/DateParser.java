package ru.pasha.messenger.utils.date;

import org.springframework.stereotype.Component;
import ru.pasha.messenger.exception.ValidationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateParser {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public Date parse(String date) {
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            throw new ValidationException("Date format is wrong");
        }
    }
}
