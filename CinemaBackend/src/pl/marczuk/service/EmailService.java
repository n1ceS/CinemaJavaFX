package pl.marczuk.service;

import org.stringtemplate.v4.ST;
import pl.marczuk.model.Reservation;
import pl.marczuk.model.User;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class EmailService {
    private static String from = "oranzeria@kino.pl";
    private static String host = "localhost";
    private static Properties properties = System.getProperties();
    public static void sendEmail(User user, Reservation reservation, List<String> reservedSeats) {
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.mailtrap.io");
        properties.put("mail.smtp.port", 465);
        properties.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("b85e55fcc03e6c", "d9bb2a447fa951");
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            
            StringBuilder emailTemplate = new StringBuilder(readFile("C:\\Users\\Michal\\IdeaProjects\\CinemaBackend\\src\\pl\\marczuk\\service\\emailtemplate.html"));

            emailTemplate.append("<tr ><td height=\"100\" ><h1>Rezerwacja filmu: " +"\"" + reservation.getSeance().getMovie().getTitle()+"\"" + "</h1></td></tr>");
            emailTemplate.append("<tr ><td height=\"100\" ><h2>Witaj " + user.getFirstName() + " " + user.getLastName() + ", twoja rezerwacja została przyjęta!</h2></td></tr>");
            emailTemplate.append("<tr ><td height=\"50\" ><h3>Film: " + reservation.getSeance().getMovie().getTitle() + "</h3></td></tr>");
            emailTemplate.append("<tr ><td height=\"50\" ><h3>Data:" + reservation.getSeance().getDate() + " " + reservation.getSeance().getStartTime().getHour()+ ":" + reservation.getSeance().getStartTime().getMinute() + "</h3></td></tr>");
            emailTemplate.append("<tr ><td height=\"50\" ><h3>Zarezerwowane miejsca: " + String.join(", ", reservedSeats) + "</h3></td></tr>");
            emailTemplate.append("<tr><td height=\"50\" ><p>KINO ORANŻERIA</p></td></tr></table></body></html>");
            message.setSubject("Rezerwacja " + reservation.getSeance().getMovie().getTitle() +  " - Kino Oranżeria");
            message.setContent(emailTemplate.toString(), "text/html; charset=UTF-8");
            Transport.send(message);
            System.out.println("Mail został wysłany");
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String readFile(String pathname) throws IOException {

        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int)file.length());

        try (Scanner scanner = new Scanner(file)) {
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + System.lineSeparator());
            }
            return fileContents.toString();
        }
    }
}
