package Msg;

public class Msg implements java.io.Serializable {

//    private static final long serialVersionUID = 2709425275741743919L;

    // 消息的类型
    private String MessageType;

    // 消息内容
    private String content;

    // 发送消息的
    private String sender;

    // 接收消息的
    private String getter;

    // 日期
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

}
