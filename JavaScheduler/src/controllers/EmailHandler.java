
package controllers;

import javax.mail.*;
import javax.mail.internet.*;
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
     * @param event the event of which was created, updation, deleted
     * @param status of the event decides, which Mail layout will be drafted(creation, updation, deletion email)
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
     * @param user
     */
    public static void reminderMail(User user) {
        System.out.println("remindermail");
        for(Event event : user.getEvents()){
            if(user.getId() == event.getHostId()) {
                if (checkReminderTime(event)) {
                    System.out.println("ifcheckremindertimeevent");
                    EmailHandler mail = new EmailHandler();
                    mail.setupServerProperties();
                    mail.draftReminderMail(event);
                    mail.sendMail();
                }
            }
        }
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
     * @param event
     * @param status
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
     * @param event
     */
    public void draftReminderMail(Event event) {
        System.out.println("draftremindermail");
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
     * @param event
     * @return Boolean if Current time is after reminder time
     */
    public static boolean checkReminderTime(Event event){
        System.out.println("checkremindertime");
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




/**public static void main(String args[]) throws AddressException, MessagingException {
 EmailHandler mail = new EmailHandler();
 mail.setupServerProperties();
 mail.draftMail();
 mail.sendMail();
 }**/

/**String[] recipients;
 ArrayList<User> participants = event.getParticipants(); // Event.participants(ArrayList<User>) werden in participents gespeichert)
 for (int i = 0; i > participants.size(); i++) {
 User user;
 user = participants.get(i);
 recipients[i] = user.getEmail();
 }




 // getParticipants um User Liste zu kriegen, dann User.email;
 // iterieren der Liste und in neue String Liste
 **/



/**
 -HTML Layout umändern/ free Temp finden; selbst finden
 -Reminder
 -Edit
 -Delete, Unterscheidung ob nur 1 user gelöscht, oder ob ganze Event gelöscht
 **/

/**
 30.01.2021
 private void draftUpdatedMail (Event event){
 mimeMessage = new MimeMessage(newSession);

 for (User participant : event.getParticipants()) {
 try {
 mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(participant.getEmail()));
 } catch (MessagingException e) {
 e.printStackTrace();
 }
 }

 try {
 mimeMessage.setSubject("Updated Event: " + event.getName() + " " + event.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " + event.getTime() + " " + event.getLocation().getName());
 mimeMessage.setText(EmailHandlerHTML.setupText(event, "Your Event has been edited!"),null, "html");
 } catch (MessagingException e) {
 e.printStackTrace();
 }
 }



 private void draftDeletedMail (Event event){
 mimeMessage = new MimeMessage(newSession);

 for (User participant : event.getParticipants()) {
 try {
 mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(participant.getEmail()));
 } catch (MessagingException e) {
 e.printStackTrace();
 }
 }

 try {
 mimeMessage.setSubject("Deleted Event: " + event.getName() + " " + event.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " + event.getTime() + " " + event.getLocation().getName());
 mimeMessage.setText(EmailHandlerHTML.setupText(event, "Your Event has been removed!"),null, "html");
 } catch (MessagingException e) {
 e.printStackTrace();
 }
 }
**/