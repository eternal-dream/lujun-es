package com.cqvip.innocence.tests;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName TimeTest
 * @Description
 * @Author Innocence
 * @Date 2020/8/2
 * @Version 1.0
 */
public class TimeTest {
    public static void main(String[] args) {

        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        String format = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:ss:mm"));
        System.out.println("now = " + format);
    }
}
