import javax.jms.*;

public class XXMessageListener implements MessageListener {
    public void onMessage(Message message) {
        if (message instanceof MapMessage) {
            try {
                if("sometype".equalsIgnoreCase(message.getJMSType().toString())){
                    System.out.println(message);
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

}
