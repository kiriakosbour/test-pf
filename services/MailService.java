package gr.deddie.pfr.services;

import gr.deddie.pfr.dao.MyBatisConnectionFactory;
import gr.deddie.pfr.dao.UTILITYMapper;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * Created by M.Masikos on 23/4/2017.
 */
public class MailService {
    private static Logger logger = LogManager.getLogger(MailService.class);
    private static final String EMAIL_SIGNATURE = "ΠΡΟΣΟΧΗ: Το e-mail αυτό σας αποστέλλεται από το πληροφοριακό σύστημα Διαχείρισης Βλαβών του ΔΕΔΔΗΕ. " +
            "Παρακαλούμε MHN απαντήσετε σε αυτό το e-mail. Εάν έχετε απορίες, μπορείτε να επικοινωνήσετε τηλεφωνικά στα " +
            "τηλέφωνα βλαβών που αναγράφονται στο λογαριασμό του προμηθευτή ηλεκτρικής ενέργειας που έχετε επιλέξει.";

    public MailService() {
    }

    public void sendEmail(String aToEmailAddr, String aSubject, String aBody) throws MessagingException, ConfigurationException {

        Properties props = new Properties();
        //props.put("mail.host", "10.10.58.135"); cashub02
        props.put("mail.host", "outlook.deddie.gr");
        props.put("mail.port","25");
        props.put("mail.from", "redmine@deddie.gr");
        Authenticator authenticator = null;
        authenticator = new Authenticator("redmine", "fb[[6S?;");
        props.put("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());

        Session session = Session.getInstance(props, authenticator);
        MimeMessage message = new MimeMessage(session);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(aToEmailAddr));
        message.setSubject(aSubject, "UTF-8");
        message.setText(aBody, "UTF-8");
        Transport.send(message);
    }

    private static class Authenticator extends javax.mail.Authenticator {

        private PasswordAuthentication authentication;

        public Authenticator(String username, String password) {
            authentication = new PasswordAuthentication(username, password);
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return authentication;
        }
    }

    public void sendEmailviaOracleProc(String aToEmailAddr, String aCCEmailAddr, String aSubject, String aBody) throws PersistenceException {

        SqlSession sqlSession = null;
        logger.info("sendEmailviaOracleProc");
        try {
            sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

            UTILITYMapper utilityMapper = sqlSession.getMapper(UTILITYMapper.class);
            utilityMapper.sendEmail("vlaves@deddie.gr", aToEmailAddr, aCCEmailAddr,aSubject, aBody, EMAIL_SIGNATURE);

            sqlSession.commit();
        } catch (PersistenceException e) {
            logger.error("exception in sendEmailviaOracleProc", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

}
