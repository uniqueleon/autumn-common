package org.aztec.autumn.common.utils.mail;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public interface EMailService {

  public void send(String recipient, String subject, Object content)
      throws AddressException, MessagingException;
  
  public void send(String recipient, String sender, String subject,Object content)
	      throws AddressException, MessagingException;
  
  public void send(List<String> recipients, String subject, Object content)
      throws AddressException, MessagingException;

  public void send(String recipient, SimpleMail mail) throws AddressException,
      MessagingException;

  public void send(List<String> recipients, SimpleMail mail)
      throws AddressException, MessagingException;
}
