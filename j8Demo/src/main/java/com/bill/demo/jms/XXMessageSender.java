import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

/**
 * Created by wangbil on 11/2/2016.
 */
public class XXMessageSender {
    private static final Logger logger = LoggerFactory.getLogger(XXMessageSender.class);
    @Autowired
    private JmsTemplate jmsTemplate;

    public XXMessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void send(Alert alert) throws MessageSendException {
        if (alert == null) {
            throw new MessageSendException("message is null");
        } else {
            jmsTemplate.send(alert);
        }

    }

}
