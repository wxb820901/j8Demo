package com.bill.demo.j8;

import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.YEARS;

public class DateTest {
    @Test
    public void testDate(){


        LocalDate today = LocalDate.now();
        System.out.println("Today's Local date : " + today);
        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();
        System.out.printf("Year : %d Month : %d day : %d \t %n", year, month, day);


        LocalDate dateOfBirth = LocalDate.of(2010, 01, 14);
        System.out.println("Your Date of birth is : " + dateOfBirth);


        LocalDate date1 = LocalDate.of(2014, 01, 14);
        if(date1.equals(today)){
            System.out.printf("Today %s and date1 %s are same date %n", today, date1);
        }


        dateOfBirth = LocalDate.of(2010, 01, 14);
        MonthDay birthday = MonthDay.of(dateOfBirth.getMonth(), dateOfBirth.getDayOfMonth());
        MonthDay currentMonthDay = MonthDay.from(today);
        if(currentMonthDay.equals(birthday)){//equals
            System.out.println("Many Many happy returns of the day !!");
        }else{
            System.out.println("Sorry, today is not your birthday");
        }


        LocalTime time = LocalTime.now();
        System.out.println("local time now : " + time);
        LocalTime newTime = time.plusHours(2); //plusHours
        System.out.println("Time after 2 hours : " + newTime);


        LocalDate nextWeek = today.plus(1, ChronoUnit.WEEKS);//plus
        System.out.println("Today is : " + today);
        System.out.println("Date after 1 week : " + nextWeek);


        LocalDate previousYear = today.minus(1, YEARS);//minus
        System.out.println("Date before 1 year : " + previousYear);
        LocalDate nextYear = today.plus(1, YEARS);//plus
        System.out.println("Date after 1 year : " + nextYear);


        // Returns the current time based on your system clock and set to UTC.
        Clock clock = Clock.systemUTC();
        System.out.println("Clock : " + clock);
        // Returns time based on system clock zone Clock defaultClock =
        Clock.systemDefaultZone();
        System.out.println("Clock : " + clock);


        LocalDate tomorrow = LocalDate.of(2014, 1, 15);
        if(tomorrow.isAfter(today)){//isAfter
            System.out.println("Tomorrow comes after today");
        }
        LocalDate yesterday = today.minus(1, DAYS);
        if(yesterday.isBefore(today)){//isBefore
            System.out.println("Yesterday is day before today");
        }


        // Date and time with timezone in Java 8
        ZoneId america = ZoneId.of("America/New_York");//ZoneId
        LocalDateTime localDateAndTime = LocalDateTime.now();//LocalDateTime
        System.out.println("Current date and time in a default timezone : " + localDateAndTime);
        ZonedDateTime dateAndTimeInNewYork = ZonedDateTime.of(localDateAndTime, america );//ZonedDateTime
        System.out.println("Current date and time in a particular timezone : " + dateAndTimeInNewYork);


        YearMonth currentYearMonth = YearMonth.now(); //YearMonth
        System.out.printf("Days in month year %s: %d%n", currentYearMonth, currentYearMonth.lengthOfMonth());
        YearMonth creditCardExpiry = YearMonth.of(2018, Month.FEBRUARY);
        System.out.printf("Your credit card expires on %s %n", creditCardExpiry);


        if(today.isLeapYear()){//isLeapYear
            System.out.println("This year is Leap year");
        }else {
            System.out.println("2014 is not a Leap year");
        }


        LocalDate java8Release = LocalDate.of(2014, Month.JULY, 14);
        Period periodToNextJavaRelease = Period.between(java8Release, today);//Period
        System.out.println("Months left between today and Java 8 release : " + periodToNextJavaRelease.getMonths() );


        LocalDateTime datetime = LocalDateTime.of(2014, Month.JANUARY, 14, 19, 30);
        ZoneOffset offset = ZoneOffset.of("+05:30");//ZoneOffset
        OffsetDateTime date = OffsetDateTime.of(datetime, offset);
        System.out.println("Date and Time with timezone offset in Java : " + date);


        Instant timestamp = Instant.now();//Instant
        System.out.println("What is value of this instant " + timestamp);


        String dayAfterTommorrow = "20140116";
        LocalDate formatted = LocalDate.parse(dayAfterTommorrow,//parse
                DateTimeFormatter.BASIC_ISO_DATE);
        System.out.printf("Date generated from String %s is %s %n", dayAfterTommorrow, formatted);


        String goodFriday = "Apr 18 2014";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");//DateTimeFormatter
            LocalDate holiday = LocalDate.parse(goodFriday, formatter);//parse
            System.out.printf("Successfully parsed String %s, date is %s%n", goodFriday, holiday);
        } catch (DateTimeParseException ex) {
            System.out.printf("%s is not parsable!%n", goodFriday);
            ex.printStackTrace();
        }



        LocalDateTime arrivalDate = LocalDateTime.now();
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mm a");
            String landing = arrivalDate.format(format);//format
            System.out.printf("Arriving at : %s %n", landing);
        } catch (DateTimeException ex) {
            System.out.printf("%s can't be formatted!%n", arrivalDate);
            ex.printStackTrace();
        }



    }
}
