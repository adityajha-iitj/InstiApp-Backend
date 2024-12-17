package in.ac.iitj.instiapp.Tests.Utilities;


import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


public class Conversions {

    public static Date convertLocalDateToDate(LocalDate localDate) {
       return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
