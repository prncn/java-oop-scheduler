/**package controllers;

import java.text.ParseException;

import java.time.LocalDateTime;
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

  /**  public int reminderTimeCalc(Event event) {
        int time;
        switch (event.getReminder()) {
            case NONE:
                time = 0;
                break;
            case TEN_MIN:
                new Timer().schedule(new ReminderTask(event), 10);
                break;
            case ONE_HOUR:
                break;
            case THREE_DAYS:
                break;
            case ONE_WEEK:
                break;
            return;
        }
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
