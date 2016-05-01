package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import java.util.Calendar;

/**
 * Created by ludovic on 1/05/16.
 */
public class DateObject {

    private int hour;
    private int minute;
    private int dayOfMonth;
    private int month;
    private int year;

    public DateObject(int hour, int minute, int dayOfMonth, int month, int year){
        this.hour = hour;
        this.minute = minute;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.year = year;
    }

    public DateObject(Calendar c){
        this.hour = c.get(Calendar.HOUR_OF_DAY);
        this.minute = c.get(Calendar.MINUTE);
        this.dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        this.month = c.get(Calendar.MONTH);
        this.year = c.get(Calendar.YEAR);
    }

    public DateObject(String dateFormat){
        String[] arrayString = dateFormat.split("-");
        String[] hourString = arrayString[0].split(":");
        String[] dateString = arrayString[1].split("/");
        this.hour = Integer.parseInt(hourString[0]);
        this.minute = Integer.parseInt(hourString[1]);
        this.dayOfMonth = Integer.parseInt(dateString[0]);
        this.month = Integer.parseInt(dateString[1]);
        this.year = Integer.parseInt(dateString[2]);
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public boolean isPrevious(DateObject date){
        if (this.year <= date.getYear()){
            if (this.month <= date.getMonth()){
                if (this.dayOfMonth <= date.getDayOfMonth()){
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }



}
