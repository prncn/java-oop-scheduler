package controllers;

import models.*;

import java.time.format.DateTimeFormatter;

/**
 * The Class EmailHanlderHTML is used for creating the mail Layout
 * @author ZuHyunLee97
 */
public class EmailHandlerHTML {

    /**
     *
     *
     * @param event
     * @return nothing or all participants of event if participants > 1
     */
    public static String  Participants(Event event) {
        String p = "";
        if (event.getParticipants().size() > 1) {
            p = "Participants: " + event.participantsToString();
        }
        return p;
    }

    /**
     *
     * @param event
     * @return
     */
    public static String Description(Event event) {
        String d ="";
        if (event.getDescription().equals(d)) {

        } else {
            d = "Description: " + event.getDescription();
        }
        return d;
    }

    /**
     *
     * @param event
     * @param subject
     * @return
     */
    public static String setupText(Event event, String subject) {
        return
                "<body>\n" +
                "    <div class=\"es-wrapper-color\">\n" +
                "        <!--[if gte mso 9]>\n" +
                "\t\t\t<v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\">\n" +
                "\t\t\t\t<v:fill type=\"tile\" color=\"#eeeeee\"></v:fill>\n" +
                "\t\t\t</v:background>\n" +
                "\t\t<![endif]-->\n" +
                "        <table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "            <tbody>\n" +
                "                <tr>\n" +
                "                    <td class=\"esd-email-paddings\" valign=\"top\">\n" +
                "                        <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content esd-header-popover\" align=\"center\">\n" +
                "                            <tbody>\n" +
                "                                <tr>\n" +
                "                                </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                        <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-header\" align=\"center\">\n" +
                "                            <tbody>\n" +
                "                                <tr>\n" +
                "                                    <td class=\"esd-stripe\" esd-custom-block-id=\"7681\" align=\"center\">\n" +
                "                                        <table class=\"es-header-body\" style=\"background-color: #74cfb7;\" width=\"550\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#74cfb7\" align=\"center\">\n" +
                "                                            <tbody>\n" +
                "                                                <tr>\n" +
                "                                                    <td class=\"esd-structure es-p15t es-p15b es-p15r es-p15l\" align=\"left\">\n" +
                "                                                        <!--[if mso]><table width=\"520\" cellpadding=\"0\" cellspacing=\"0\"><tr><td width=\"328\" valign=\"top\"><![endif]-->\n" +
                "                                                        <table class=\"es-left\" cellspacing=\"0\" cellpadding=\"0\" align=\"left\">\n" +
                "                                                            <tbody>\n" +
                "                                                                <tr>\n" +
                "                                                                    <td class=\"es-m-p0r es-m-p20b esd-container-frame\" width=\"328\" valign=\"top\" align=\"center\">\n" +
                "                                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                                            <tbody>\n" +
                "                                                                                <tr>\n" +
                "                                                                                    <td class=\"esd-block-text es-m-txt-c es-p5\" align=\"left\">\n" +
                "                                                                                        <h1 style=\"color: #ffffff; line-height: 100%;\">  " + subject + "<br></h1>\n" +
                "                                                                                    </td>\n" +
                "                                                                                </tr>\n" +
                "                                                                            </tbody>\n" +
                "                                                                        </table>\n" +
                "                                                                    </td>\n" +
                "                                                                </tr>\n" +
                "                                                            </tbody>\n" +
                "                                                        </table>\n" +
                "                                                        <!--[if mso]></td><td width=\"20\"></td><td width=\"172\" valign=\"top\"><![endif]-->\n" +
                "                                                        <table cellspacing=\"0\" cellpadding=\"0\" align=\"right\">\n" +
                "                                                            <tbody>\n" +
                "                                                                <tr class=\"es-hidden\">\n" +
                "                                                                    <td class=\"es-m-p20b esd-container-frame\" esd-custom-block-id=\"7704\" width=\"172\" align=\"left\">\n" +
                "                                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                                            <tbody>\n" +
                "                                                                                <tr>\n" +
                "                                                                                    <td>\n" +
                "                                                                                        <table cellspacing=\"0\" cellpadding=\"0\" align=\"right\">\n" +
                "                                                                                            <tbody>\n" +
                "                                                                                                <tr>\n" +
                "                                                                                                    <td align=\"center\" class=\"esd-empty-container\" style=\"display: none;\"></td>\n" +
                "                                                                                                </tr>\n" +
                "                                                                                            </tbody>\n" +
                "                                                                                        </table>\n" +
                "                                                                                    </td>\n" +
                "                                                                                </tr>\n" +
                "                                                                            </tbody>\n" +
                "                                                                        </table>\n" +
                "                                                                    </td>\n" +
                "                                                                </tr>\n" +
                "                                                            </tbody>\n" +
                "                                                        </table>\n" +
                "                                                        <!--[if mso]></td></tr></table><![endif]-->\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </tbody>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                        <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n" +
                "                            <tbody>\n" +
                "                                <tr>\n" +
                "                                    <td class=\"esd-stripe\" align=\"center\">\n" +
                "                                        <table class=\"es-content-body\" width=\"550\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\">\n" +
                "                                            <tbody>\n" +
                "                                                <tr>\n" +
                "                                                    <td class=\"esd-structure es-p20t es-p35r es-p35l\" esd-custom-block-id=\"7685\" align=\"left\">\n" +
                "                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                            <tbody>\n" +
                "                                                                <tr>\n" +
                "                                                                    <td class=\"esd-container-frame\" width=\"480\" valign=\"top\" align=\"center\">\n" +
                "                                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                                            <tbody>\n" +
                "                                                                                <tr>\n" +
                "                                                                                    <td class=\"esd-block-image es-p20t es-p25b es-p35r es-p35l\" align=\"center\" style=\"font-size: 0px;\"><a target=\"_blank\" href=\"<ahref=&quot;https://icon-library.net/icon/add-reminder-icon-2.html&quot;title=&quot;AddReminderIcon#348522&quot;><imgsrc=&quot;https://icon-library.net//images/add-reminder-icon/add-reminder-icon-2.jpg&quot;width=&quot;350&quot;/></a>\"><img src=\"\"https://cdn.discordapp.com/attachments/764039131604451340/803230862853406740/kisspng-bell-computer-icons-clip-art-notification-bell-5b27cdf9abf9f9.2971416115293352897044.png\"\" alt style=\"display: block;\" width=\"120\"></a></td>\n" +
                "                                                                                </tr>\n" +
                "                                                                                <tr>\n" +
                "                                                                                    <td class=\"esd-block-text\" align=\"center\">\n" +
                "                                                                                        <h2>" + event.getName() + "</h2>\n" +
                "                                                                                    </td>\n" +
                "                                                                                </tr>\n" +
                "                                                                                <tr>\n" +
                "                                                                                    <td class=\"esd-block-text es-p15t es-p10b es-p40l\" align=\"left\">\n" +
                "                                                                                        <p><strong>Priority: " + event.getPriority() + "</strong></p>\n" +
                "                                                                                        <p><strong><br>Date: " + event.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +"</strong></p>\n" +
                "                                                                                        <p><strong>Time: " + event.getTime() + "</strong></p>\n" +
                "                                                                                        <p><strong>Duration: " + event.getDurationMinutes() + " minutes" + "</strong></p>\n" +
                "                                                                                        <p><strong>Location: " + event.getLocation().getName() + "</strong></p>\n" +
                "                                                                                        <p><strong>" + Participants(event) + "</strong></p>\n" +
                "                                                                                        <p><strong>" + Description(event) +  "</strong></p>\n" +
                "                                                                                    </td>\n" +
                "                                                                                </tr>\n" +
                "                                                                                <tr>\n" +
                "                                                                                    <td class=\"esd-block-spacer es-p20t es-p15b\" align=\"center\" style=\"font-size:0\">\n" +
                "                                                                                        <table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\n" +
                "                                                                                            <tbody>\n" +
                "                                                                                                <tr>\n" +
                "                                                                                                    <td style=\"border-bottom: 3px solid #eeeeee; background: rgba(0, 0, 0, 0) none repeat scroll 0% 0%; height: 1px; width: 100%; margin: 0px;\"></td>\n" +
                "                                                                                                </tr>\n" +
                "                                                                                            </tbody>\n" +
                "                                                                                        </table>\n" +
                "                                                                                    </td>\n" +
                "                                                                                </tr>\n" +
                "                                                                            </tbody>\n" +
                "                                                                        </table>\n" +
                "                                                                    </td>\n" +
                "                                                                </tr>\n" +
                "                                                            </tbody>\n" +
                "                                                        </table>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                                <tr>\n" +
                "                                                    <td class=\"esd-structure es-p20t es-p40b es-p35r es-p35l\" esd-custom-block-id=\"7685\" align=\"left\">\n" +
                "                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                            <tbody>\n" +
                "                                                                <tr>\n" +
                "                                                                    <td class=\"esd-container-frame\" width=\"480\" valign=\"top\" align=\"center\">\n" +
                "                                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                                            <tbody>\n" +
                "                                                                                <tr>\n" +
                "                                                                                    <td class=\"esd-block-text es-p20t\" align=\"right\">\n" +
                "                                                                                        <p>This mail was created automatically by Java-OOP-Scheduler<br>Please do not reply<br><br>*for a proper view, please disable darkmode</p>\n" +
                "                                                                                    </td>\n" +
                "                                                                                </tr>\n" +
                "                                                                            </tbody>\n" +
                "                                                                        </table>\n" +
                "                                                                    </td>\n" +
                "                                                                </tr>\n" +
                "                                                            </tbody>\n" +
                "                                                        </table>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </tbody>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                        <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n" +
                "                            <tbody>\n" +
                "                                <tr>\n" +
                "                                </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                        <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\">\n" +
                "                            <tbody>\n" +
                "                                <tr>\n" +
                "                                    <td class=\"esd-stripe\" esd-custom-block-id=\"7766\" align=\"center\">\n" +
                "                                        <table class=\"es-content-body\" style=\"border-bottom:10px solid #6600ff;background-color: #1b9ba3;\" width=\"550\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#6600ff\" align=\"center\">\n" +
                "                                            <tbody>\n" +
                "                                                <tr>\n" +
                "                                                    <td class=\"esd-structure\" align=\"left\">\n" +
                "                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                            <tbody>\n" +
                "                                                                <tr>\n" +
                "                                                                                </tr>\n" +
                "                                                                            </tbody>\n" +
                "                                                                        </table>\n" +
                "                                                                    </td>\n" +
                "                                                                </tr>\n" +
                "                                                            </tbody>\n" +
                "                                                        </table>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </tbody>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                        <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-footer\" align=\"center\">\n" +
                "                            <tbody>\n" +
                "                                <tr>\n" +
                "                                </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                        <table class=\"esd-footer-popover es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n" +
                "                            <tbody>\n" +
                "                                <tr>\n" +
                "                                </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </tbody>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </tbody>\n" +
                "        </table>\n" +
                "    </div>\n" +
                "</body>\n" +
                "\n" +
                "</html>";
    }
}

/**
 "<head>\n" +
 "    <meta charset=\"UTF-8\">\n" +
 "    <meta content=\"width=device-width, initial-scale=1\" name=\"viewport\">\n" +
 "    <meta name=\"x-apple-disable-message-reformatting\">\n" +
 "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
 "    <meta content=\"telephone=no\" name=\"format-detection\">\n" +
 "    <title></title>\n" +
 "    <!--[if (mso 16)]>\n" +
 "    <style type=\"text/css\">\n" +
 "    a {text-decoration: none;}\n" +
 "    </style>\n" +
 "    <![endif]-->\n" +
 "    <!--[if gte mso 9]><style>sup { font-size: 100% !important; }</style><![endif]-->\n" +
 "    <!--[if gte mso 9]>\n" +
 "<xml>\n" +
 "    <o:OfficeDocumentSettings>\n" +
 "    <o:AllowPNG></o:AllowPNG>\n" +
 "    <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
 "    </o:OfficeDocumentSettings>\n" +
 "</xml>\n" +
 "<![endif]-->\n" +
 "</head>\n" +
 "\n" +
 */