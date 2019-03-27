package org.aztec.autumn.common.utils.mail;

public class MailServiceFactoryImpl implements EMailServiceFactory {

  public MailServiceFactoryImpl() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public EMailService createService(String smptHost, String username, String pwd) {
    return new SimpleMailSender(smptHost, username, pwd);
  }

@Override
public EMailService createService(String smptHost, int port, String username, String pwd) {

    return new SimpleMailSender(smptHost,port, username, pwd);
}

}
