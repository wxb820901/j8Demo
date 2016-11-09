
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.tibco.tibjms.TibjmsQueueConnectionFactory;

public class ConnectionFactoryWrapper extends TibjmsQueueConnectionFactory {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ConnectionFactoryWrapper.class);
    
    private AtomicBoolean emsSSLIdentitySet = new AtomicBoolean(false);
    private AtomicBoolean emsSSLTrustedCertificateSet = new AtomicBoolean(false);

    public void setEmsSSLIdentity(String val) {
        if (val != null && !val.trim().isEmpty()) {
            checkFlag(emsSSLIdentitySet, "emsSSLIdentity");
            super.setSSLIdentity(val);
        }        
    }

    public void setEmsSSLTrustedCertificate(String val) {
        if (val != null && !val.trim().isEmpty()) {
            checkFlag(emsSSLTrustedCertificateSet, "emsSSLTrustedCertificateSet");
            super.setSSLTrustedCertificate(val);
        }          
    }
    
    public void setEmsSSLIdentityResource(Resource res) throws IOException {
        if (res != null) {
            checkFlag(emsSSLIdentitySet, "emsSSLIdentity");
            checkResource(res);
            
            byte[] certBytes = readResource(res);
            super.setSSLIdentity(certBytes);
        }          
    }

    public void setEmsSSLTrustedCertificateResource(Resource res) throws IOException {
        if (res != null) {
            checkFlag(emsSSLTrustedCertificateSet, "emsSSLTrustedCertificateSet");
            checkResource(res);
            
            byte[] certBytes = readResource(res);
            super.setSSLTrustedCertificate(certBytes, null);               
        }         
    }

    private void checkResource(Resource res) {
        if (!res.exists()) {
            throw new IllegalArgumentException("Resource not found: " + res);
        }
    }

    /**
     * This ensures that only one of the properties protected by the flag is set to a non null value
     * @param flag
     * @param label
     */
    private void checkFlag(AtomicBoolean flag, String label) {
        boolean flagSet = flag.get();
        if (flagSet || !flag.compareAndSet(false, true)) {
            throw new IllegalStateException(label + " already set");
        }                
    }    
    
    private byte[] readResource(Resource res) throws IOException {
        int bufSize = 1024;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bufSize);
        BufferedInputStream in = new BufferedInputStream(res.getInputStream());

        byte[] buf = new byte[bufSize];
        int read = -1;
        
        while ((read = in.read(buf)) != -1) {
            baos.write(buf, 0, read);
        }
        
        return baos.toByteArray();
    }    
    
    public Connection createConnection() throws JMSException {
        Connection connection = null;

        try {

            LOGGER.info("Creating Tibco connection for: {}", super.getUrl());
            connection = super.createConnection();
            LOGGER.info("Created Tibco connection for: {}", super.getUrl());

        } catch (JMSException e) {
            LOGGER.error("Couldn't create Tibco connection for:{}", super.getUrl());
            LOGGER.error("Couldn't create Tibco connection:", e);
            throw e;
        }

        return connection;
    }

    /**
     * 
     */
    public Connection createConnection(String userName, String password) throws JMSException {
        Connection connection = null;

        try {
            LOGGER.info("Creating Tibco connection for: {}", super.getUrl());
            connection = super.createConnection(userName, password);
            LOGGER.info("Created Tibco connection for: {}", super.getUrl());

        } catch (JMSException e) {
            LOGGER.error("Couldn't create Tibco connection for:{} msg: {}", super.getUrl());
            LOGGER.error("Couldn't create Tibco connection:", e);
            throw e;
        }

        return connection;
    }


}
