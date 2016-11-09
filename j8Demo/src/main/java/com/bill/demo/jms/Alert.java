

import org.springframework.jms.core.MessageCreator;

import javax.jms.*;



public class Alert implements MessageCreator {
    public NeoAlert(){

    }

    private String uid;
    
    @Override
    public Message createMessage(Session session) throws JMSException {
        return createMapMessage(session);
    }

    public MapMessage createMapMessage(Session session) throws JMSException {
        MapMessage message = session.createMapMessage();
        message.setJMSType("sometype");//header type
        message.setString("id", id);
       
        return message;
    }
}
