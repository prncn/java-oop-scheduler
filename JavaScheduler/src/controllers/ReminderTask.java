package controllers;

import java.text.DateFormat;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimerTask;
import java.util.Timer;
import java.util.concurrent.*;

import models.*;

import javax.swing.*;

class ReminderTask extends TimerTask {
    private Event event;
    int reminderTime = 0;

    public ReminderTask(Event event) {
        this.event = event;
    }

    @Override
    public void run() {
        EmailHandler mail = new EmailHandler();
        mail.setupServerProperties();
        mail.draftReminderMail(event);
        mail.sendMail();
    }

    public static LocalDateTime getReminderTime(Event event)
    {
        LocalDateTime eventTime = event.getDate().atTime(event.getTime());
        LocalDateTime reminderTime = eventTime.minusMinutes(event.getReminder().getMinutes());
        return reminderTime;
    }

    public static boolean checkReminderTime(Event event){
        return  LocalDateTime.now().isAfter(getReminderTime(event));
    }

}



/**
 *
 *         Timer timer =new Timer();
 *         timer.schedule(ReminderTask,3000 );
 *
 *         try{
 *             Thread.sleep(3000);
 *         }catch (InterruptedException e){}
 *         timer.cancel();
 *
 *     }
 */
