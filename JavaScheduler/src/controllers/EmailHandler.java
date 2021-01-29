
package controllers;

import javax.mail.*;
import javax.mail.internet.*;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import controllers.*;
import models.*;

public class EmailHandler {

    Session newSession = null;
    MimeMessage mimeMessage = null;


    public static void createdMail(Event event) {
        EmailHandler mail = new EmailHandler();
        mail.setupServerProperties();
        mail.draftCreatedMail(event);
        mail.sendMail();
    }


    public static void updatedMail(Event event) {
        EmailHandler mail = new EmailHandler();
        mail.setupServerProperties();
        mail.draftUpdatedMail(event);
        mail.sendMail();
    }


    public static void deletedMail(Event event) {
        EmailHandler mail = new EmailHandler();
        mail.setupServerProperties();
        mail.draftDeletedMail(event);
        mail.sendMail();
    }

    public static void reminderMail(User user) {
        for(Event event : user.getAcceptedEvents()){
            if(ReminderTask.checkReminderTime(event)) {
                EmailHandler mail = new EmailHandler();
                mail.setupServerProperties();
                mail.draftReminderMail(event);
                mail.sendMail();
            }
        }
    }


    void setupServerProperties() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        newSession = Session.getDefaultInstance(properties, null);
    }


    private void draftCreatedMail(Event event) {
        mimeMessage = new MimeMessage(newSession);

        for (User participant : event.getParticipants()) {
            try {
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(participant.getEmail()));
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        try {
            mimeMessage.setSubject("New Event: " + event.getName() + " " + event.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " + event.getTime() + " " + event.getLocation().getName());
            mimeMessage.setText(EmailHandlerHTML.setupText(event, "You have a newly added Event!"),null, "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


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

    void draftReminderMail(Event event) {
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
    }

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