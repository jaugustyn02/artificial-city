package org.example;


public class Clock {
    private int hours, minutes, seconds;
    private final int initHours, initMinutes, initSeconds;

    public Clock(int h, int min, int sec){
        hours = h;
        minutes = min;
        seconds = sec;
        initHours = h;
        initMinutes = min;
        initSeconds = sec;
    }

    public void reset(){
        hours = initHours;
        minutes = initMinutes;
        seconds = initSeconds;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void iterate(){
        addSecond();
    }

    public void addSecond(){
        seconds++;
        if (seconds == 60){
            seconds = 0;
            addMinute();
        }
    }

    public void addMinute(){
        minutes++;
        if (minutes == 60){
            minutes = 0;
            addHour();
        }
    }

    public void addHour(){
        hours++;
        if (hours == 24){
            hours = 0;
        }
    }

    public String getTime(){
        String strH = (hours < 10 ? "0" : "") + hours;
        String strMin = (minutes < 10 ? "0" : "") + minutes;
        String strSec = (seconds < 10 ? "0" : "") + seconds;
        return strH + ":" + strMin + ":" + strSec;
    }
}
