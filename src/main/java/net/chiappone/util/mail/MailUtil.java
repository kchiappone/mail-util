package net.chiappone.util.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author Kurtis Chiappone
 */
public class MailUtil {

    private static final Logger logger = LoggerFactory.getLogger( MailUtil.class );

    public static void sendMessage( String mailhost, String from, String to, String subject, String body ) {

        sendMessage( mailhost, from, to, null, subject, body, false );

    }

    public static void sendMessage( String mailhost, String from, String to, String bcc, String subject, String body ) {

        sendMessage( mailhost, from, to, bcc, subject, body, false );

    }

    public static void sendMessage( String mailhost, String from, String to, String bcc, String subject, String body,
                    boolean isHtml ) {

        try {

            Properties properties = System.getProperties();
            properties.setProperty( "mail.smtp.host", mailhost );
            Session session = Session.getDefaultInstance( properties );
            MimeMessage message = new MimeMessage( session );
            message.setFrom( new InternetAddress( from ) );

            // Multiple recipients

            if ( to.indexOf( "," ) > 0 ) {

                String[] recipientArray = to.split( "," );

                for ( String s : recipientArray ) {

                    message.addRecipient( Message.RecipientType.TO, new InternetAddress( s ) );
                    logger.info( "Sending mail TO: {}", s );

                }

            } else {

                message.addRecipient( Message.RecipientType.TO, new InternetAddress( to ) );
                logger.info( "Sending mail TO: {}", to );

            }

            // BCC recipients

            if ( bcc != null ) {

                // Multiple BCC recipients

                if ( bcc.indexOf( "," ) > 0 ) {

                    String[] bccArray = bcc.split( "," );

                    for ( String s : bccArray ) {

                        message.addRecipient( Message.RecipientType.BCC, new InternetAddress( s ) );
                        logger.info( "Sending mail BCC: {}", s );

                    }

                } else {

                    message.addRecipient( Message.RecipientType.BCC, new InternetAddress( bcc ) );
                    logger.info( "Sending mail BCC: {}", bcc );

                }

            }

            if ( subject != null ) {

                message.setSubject( subject );

            }

            if ( isHtml ) {

                message.setContent( body, "text/html" );

            } else {

                message.setText( body );

            }

            Transport.send( message );

        } catch ( Exception e ) {

            logger.error( "Error sending mail", e );

        }

    }

}
