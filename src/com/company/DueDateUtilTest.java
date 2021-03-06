package com.company;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDateTime;

import static com.company.DueDateUtil.calculateDueDate;
import static org.junit.Assert.assertEquals;

public class DueDateUtilTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testFailOnWeekend() {
        exception.expect(IllegalArgumentException.class);

        final LocalDateTime aSaturday = LocalDateTime.of(2021, 1, 30, 9, 0, 0);
        calculateDueDate(aSaturday, 8L);
    }


    @Test
    public void testFailOnAfterWorkHours() {
        exception.expect(IllegalArgumentException.class);

        final LocalDateTime sixPM = LocalDateTime.of(2021, 2, 1, 18, 0, 0);
        calculateDueDate(sixPM, 8L);
    }


    @Test
    public void testFailOnBeforeWorkHours() {
        exception.expect(IllegalArgumentException.class);

        final LocalDateTime sixAM = LocalDateTime.of(2021, 2, 1, 6, 0, 0);
        calculateDueDate(sixAM, 8L);
    }

    @Test
    public void testSuccessBasicOneDayTask() {
        final LocalDateTime aThursday = LocalDateTime.of(2021, 1, 28, 10, 30, 0);

        final LocalDateTime endOfTask = calculateDueDate(aThursday, 8L);
        final LocalDateTime friday = LocalDateTime.of(2021, 1, 29, 10, 30, 0);

        assertEquals(endOfTask, friday);
    }

    @Test
    public void testSuccessNotCountingWeekend() {
        final LocalDateTime aThursday = LocalDateTime.of(2021, 1, 28, 10, 30, 0);

        final LocalDateTime endOfTask = calculateDueDate(aThursday, 24L);
        final LocalDateTime nextTuesday = LocalDateTime.of(2021, 2, 2, 10, 30, 0);

        assertEquals(endOfTask, nextTuesday);
    }

    @Test
    public void testSuccessOvertimeToNextDay() {
        final LocalDateTime aThursday = LocalDateTime.of(2021, 1, 28, 16, 11, 0);

        final LocalDateTime endOfTask = calculateDueDate(aThursday, 2L);
        final LocalDateTime nextDay = LocalDateTime.of(2021, 1, 29, 10, 11, 0);

        assertEquals(endOfTask, nextDay);
    }

    @Test
    public void testSuccessMidnight() {
        final LocalDateTime aThursday = LocalDateTime.of(2021, 1, 28, 17, 0, 0);

        final LocalDateTime endOfTask = calculateDueDate(aThursday, 7L);
        final LocalDateTime nextDay = LocalDateTime.of(2021, 1, 29, 16, 0, 0);

        assertEquals(endOfTask, nextDay);
    }

}