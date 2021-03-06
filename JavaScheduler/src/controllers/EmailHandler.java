
package controllers;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import models.*;

/**
 * The Class EmailHandler is used to send emails to the participants of an event
 * @author ZuHyunLee97
 */

public class EmailHandler {

    /**creating new session object to hold host data */
    Session newSession = null;

    /**creating new message object to hold email content */
    MimeMessage mimeMessage = null;

    /**
     * Sets up email server, creates a email draft and sends the email to all participants
     *
     * @param event the event of subject
     * @param status of the event, which Mail layout will be drafted(creation, update, deletion email)
     */
    public static void sendEventMail(Event event, Status status) {
        EmailHandler mail = new EmailHandler();
        mail.setupServerProperties();
        mail.draftMail(event, status);
        mail.sendMail();
    }

    /**
     * Sets up email server, creates a reminder email draft and sends the mail to all participants
     * checks if current time is the reminder time
     *
     * @param user logged in user
     */
    public static void reminderMail(User user) {
        for(Event event : user.getEvents()){
            if(user.getId() == event.getHostId()) {
                if (checkReminderTime(event)) {
                    EmailHandler mail = new EmailHandler();
                    mail.setupServerProperties();
                    mail.draftReminderMail(event);
                    mail.sendMail();



                }
            }
        }
    }

    /**
     * Creates a reminder thread timer to periodically update reminder time every minute
     * @param user logged in user
     *
     * @see javax.swing.Timer
     */
    public static void runreminderMail(User user) {

        Timer timer = new Timer(60000, e -> {
            reminderMail(user);
        });
        timer.start();
    }
    /**
     * Sets up Google's SMTP server for email session
     */
    void setupServerProperties() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        newSession = Session.getDefaultInstance(properties, null);
    }

    /**
     * Drafts the email depending on event status which decides from different mail layouts
     *
     * @param event event of subject
     * @param status status of event
     */
    private void draftMail(Event event, Status status) {
        mimeMessage = new MimeMessage(newSession);

        for (User participant : event.getParticipants()) {
            try {
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(participant.getEmail()));
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        try {
            String subject = "";
            String text = "";
            switch (status){
                case CREATED:
                    subject = "New Event: ";
                    text = "You have a newly added Event!";
                    break;
                case EDITED:
                    subject = "Updated Event: ";
                    text = "Your Event has been edited!";
                    break;
                case DELETED:
                    subject = "Deleted Event: ";
                    text = "Your Event has been removed!";
                    break;
            }
            mimeMessage.setSubject(subject + event.getName() + " " + event.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " + event.getTime() + " " + event.getLocation().getName());
            mimeMessage.setText(EmailHandlerHTML.setupText(event, text),null, "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



    /**
     * Drafts the reminder mail layout
     * @param  event event of subject
     */
    public void draftReminderMail(Event event) {
        mimeMessage = new MimeMessage(newSession);

        for (User participant : event.getParticipants()) {
            try {
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(participant.getEmail()));
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        try {
            mimeMessage.setSubject("Reminder: " + event.getName() + " " + event.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " + event.getTime() + " " + event.getLocation().getName());
            mimeMessage.setText(EmailHandlerHTML.setupText(event, "Your Reminder for:"), null, "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        event.setReminder(Reminder.NONE);
        DatabaseAPI.editEvent(event);
    }

    /**
     * Checks if the current time after reminder time
     *
     * @param event event of subject
     * @return Boolean if current time is after reminder time
     */
    public static boolean checkReminderTime(Event event){
        if(event.getReminder().equals(Reminder.NONE)){
            return false;
        }
        LocalDateTime eventTime = event.getDate().atTime(event.getTime());
        LocalDateTime reminderTime = eventTime.minusMinutes(event.getReminder().getMinutes());
        if(LocalDateTime.now().isAfter(eventTime)){
            return false;
        }
        return  LocalDateTime.now().isAfter(reminderTime);
    }

    /**
     * Logs into Gmail account and sends email to all participants
     */
    public  void sendMail() {
        String fromUser = "javaschedulerlablundaws2021@gmail.com";
        String fromUserPassword = "Javascheduler2021";
        String mailHost = "smtp.gmail.com";
        Transport transport = null;
        try {
            transport = newSession.getTransport("smtp");
            transport.connect(mailHost, fromUser, fromUserPassword);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
            System.out.println("Mail successfully sent");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}