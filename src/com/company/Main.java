package com.company;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

public class Main {


    public static void main(String[] args) {
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        junit.run(DueDateUtilTest.class);
//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            try {
//                System.out.println("Please input the time of submit...");
//                final LocalDateTime submitDateTime = LocalDateTime.parse(scanner.nextLine());
//
//
//                System.out.println("Please input length of task in hours...");
//                final long turnAroundHours = Long.parseLong(scanner.nextLine());
//
//                System.out.println(format("end of task: %s", calcDueDateTime(submitDateTime, turnAroundHours).format(ISO_LOCAL_DATE_TIME)));
//
//            } catch (NumberFormatException | DateTimeParseException ex){
//                System.out.println("invalid input");
//            }
//        }
    }

}
