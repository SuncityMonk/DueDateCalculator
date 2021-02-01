package com.company;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static java.lang.String.format;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.LocalTime.of;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import static java.time.temporal.ChronoUnit.HOURS;

public class DueDateUtil {

    private static final LocalTime END_OF_WORKDAY = of(17, 0);
    private static final LocalTime START_OF_WORKDAY = of(9, 0);
    private static final long PARAM_WORK_DAY_HOURS = 8;

    /**
     * Used to calculate the due date for a task.
     * Note, that tasks cannot have a submit date outside working hours (between 9AM and 5PM) or on a weekend.
     *
     * @param submitDateTime  the time of submission
     * @param turnAroundHours the amount of hours required to complete the task
     * @return the desired time of completion of the task
     * @throws IllegalArgumentException if the time of submission is outside working hours or on a weekend.
     */
    public static LocalDateTime calcDueDateTime(LocalDateTime submitDateTime, long turnAroundHours) {
        validate(submitDateTime);

        final LocalTime submitTime = submitDateTime.toLocalTime();
        final long plusHours = turnAroundHours % PARAM_WORK_DAY_HOURS;
        final LocalTime withPlusHours = submitTime.plusHours(plusHours);


        long taskDays = turnAroundHours / PARAM_WORK_DAY_HOURS;
        final LocalTime resultTime;

        if (withPlusHours.isAfter(END_OF_WORKDAY)) {
            taskDays++;
            final long overhangHours = HOURS.between(END_OF_WORKDAY, withPlusHours);
            resultTime = START_OF_WORKDAY
                    .plusHours(overhangHours)
                    .withMinute(submitTime.getMinute());
        } else {
            resultTime = withPlusHours;
        }

        LocalDate resultDate = submitDateTime.toLocalDate();
        while (taskDays > 0L) {
            resultDate = resultDate.plusDays(1);
            final DayOfWeek dayOfWeek = resultDate.getDayOfWeek();
            if (SATURDAY.equals(dayOfWeek) || SUNDAY.equals(dayOfWeek)) {
                continue;
            }
            taskDays--;
        }

        return LocalDateTime.of(resultDate, resultTime);
    }

    private static void validate(LocalDateTime submitDateTime) {
        final LocalTime submitTime = submitDateTime.toLocalTime();
        final LocalDate submitDate = submitDateTime.toLocalDate();

        final StringBuilder errors = new StringBuilder();
        if (END_OF_WORKDAY.isBefore(submitTime) || START_OF_WORKDAY.isAfter(submitTime)) {
            errors.append(
                    format(
                            "Submit time must be between 9:00 and 17:00, Failed argument: %s; ",
                            submitDateTime.format(ISO_LOCAL_TIME)
                    )
            );
        }
        final DayOfWeek dayOfWeek = submitDate.getDayOfWeek();
        if (SATURDAY.equals(dayOfWeek) || SUNDAY.equals(dayOfWeek)) {
            errors.append(
                    format(
                            "Submit date must not be a weekend, Failed argument: %s; ",
                            dayOfWeek
                    )
            );
        }
        final String errorString = errors.toString();
        if (0 != errorString.length()) {
            throw new IllegalArgumentException(errorString);
        }
    }
}