package com.bill.demo.j8;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.jasypt.salt.RandomSaltGenerator;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Java Simplified Encryption Home Page: http://www.jasypt.org/
 * <p>
 * <p>
 * POM dependency:
 *  <dependency>
 *      <groupId>org.jasypt</groupId>
 *      <artifactId>jasypt</artifactId>
 *      <version>1.9.2</version>
 *  </dependency>
 * <p>
 * <p>
 * encrypt by cli script: https://github.com/jasypt/jasypt/tree/master/jasypt-dist/src/main/bin
 * <p>
 * <p>
 * encrypt by cli invoke java library: java -cp $HOME\.m2\repository\org\jasypt\jasypt\1.9.2\jasypt-1.9.2.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input="reports_passwd" password=jasypt algorithm=PBEWithMD5AndDES
 * # the option password=jasypt            mapping to encryptor.setPassword("jasypt")
 * # the option algorithm=PBEWithMD5AndDES mapping to encryptor.setAlgorithm("PBEWithMD5AndDES")
 * <p>
 * <p>
 * testEncryptableProperties
 *      is demo for loading  properties with encrypted value
 * testBasicPasswordEncryptor and testConfigurablePasswordEncryptor
 *      is Java Simplified Encryption function
 *      to compare plain string and its encrypted version
 * <p>
 * <p>
 * Java Simplified Encryption also deliver encrypt and decrypt function (http://www.jasypt.org/easy-usage.html) as below
 * BasicTextEncryptor/StrongTextEncryptor for Text encryption
 * BasicIntegerNumberEncryptor/StrongIntegerNumberEncryptor for Number encryption
 * BasicBinaryEncryptor/StrongBinaryEncryptor for Binary encryption
 *
 */
public class JasyptTest {


    public static final String propertiesContent =
            "datasource.driver=com.mysql.jdbc.Driver\n" +
                    "datasource.url=jdbc:mysql://localhost/reportsdb\n" +
                    "datasource.username=reportsUser\n" +
                    "datasource.password=ENC(abD/TIInJvrw8K1lR17WW2SDDE8Ohhah)";


    /**
     * test properties password encrypt which wrapped ENC(...)
     *
     * @throws IOException
     */
    @Test
    public void testEncryptableProperties() throws IOException {
        //StandardPBEStringEncryptor mappint to above cli which encrypt properties value
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("jasypt"); // whatever you set
        encryptor.setAlgorithm("PBEWithMD5AndDES"); //default Algorithm
        encryptor.setSaltGenerator(new RandomSaltGenerator());
        //load encrypt properties and decrypt value wrapped by ENC(...)
        Properties props = new EncryptableProperties(encryptor);
        props.load(new ByteArrayInputStream(propertiesContent.getBytes()));
        String datasourceUsername = props.getProperty("datasource.username");
        String datasourcePassword = props.getProperty("datasource.password");
        Assert.assertEquals("reportsUser", datasourceUsername);
        Assert.assertEquals("reports_passwd", datasourcePassword);
    }

    /**
     * use BasicPasswordEncryptor compare plain password and encrypt
     * StrongPasswordEncryptor is the same as it but more security
     */
    @Test
    public void testBasicPasswordEncryptor() {
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword("reports_passwd");
        if (passwordEncryptor.checkPassword(
                "reports_passwd", encryptedPassword)) {
            Assert.assertTrue(true);
        } else {
            Assert.assertTrue(false);
        }
    }


    /**
     * ConfigurablePasswordEncryptor is the same as BasicPasswordEncryptor and StrongPasswordEncryptor
     * but detail can be set as required
     */
    @Test
    public void testConfigurablePasswordEncryptor() {
        ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
        passwordEncryptor.setAlgorithm("SHA-1");
        passwordEncryptor.setPlainDigest(true);
        String encryptedPassword = passwordEncryptor.encryptPassword("reports_passwd");
        if (passwordEncryptor.checkPassword(
                "reports_passwd", encryptedPassword)) {
            Assert.assertTrue(true);
        } else {
            Assert.assertTrue(false);
        }
    }

    /**
     * Text encryption and decrypt
     */
    @Test
    public void testTextEncryption() {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("jasypt");
        String myEncryptedText = textEncryptor.encrypt("reports_passwd");
        String plainText = textEncryptor.decrypt(myEncryptedText);
        Assert.assertEquals("reports_passwd", plainText);
    }

}
