package org.aztec.autumn.common.utils.mail;

public class SimpleMail {

  private String subject;
  private String content;
  private String[] copyTo;
  private Object[] attachments;
  
  public SimpleMail() {
    // TODO Auto-generated constructor stub
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Object[] getAttachments() {
    return attachments;
  }

  public void setAttachments(Object[] attachments) {
    this.attachments = attachments;
  }

  public String[] getCopyTo() {
    return copyTo;
  }

  public void setCopyTo(String[] copyTo) {
    this.copyTo = copyTo;
  }

}
