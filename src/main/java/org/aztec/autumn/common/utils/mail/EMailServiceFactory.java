package org.aztec.autumn.common.utils.mail;

public interface EMailServiceFactory {

  public EMailService createService(String smptHost,String username,String pwd);

  public EMailService createService(String smptHost,int port,String username,String pwd);
}
