package com.bill.demo.j8.serializable;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertTrue;

public class SerializableTest {

    @Test
    public void test() throws IOException, ClassNotFoundException {
        Person person = new Person();
        person.setAge(20);
        person.setName("Joe");

        AnotherPersonWithDuplicatedSerialVersionUID personWithDupicatedSerialVersionUID = new AnotherPersonWithDuplicatedSerialVersionUID();
        personWithDupicatedSerialVersionUID.setAge(20);
        personWithDupicatedSerialVersionUID.setName("Joe");

        File tempFile = writeObjectInFile(person);
        Person p2 = (Person) readObjectFromFile(tempFile);

        assertTrue(p2.getAge() == person.getAge());
        assertTrue(p2.getName().equals(person.getName()));
        tempFile.deleteOnExit();
    }

//    @Test
//    public void testDupicatedSerialVersionUID() throws IOException, ClassNotFoundException {
//        Person person = new Person();
//        person.setAge(20);
//        person.setName("Joe");
//
//        AnotherPersonWithDupicatedSerialVersionUID personWithDupicatedSerialVersionUID = new AnotherPersonWithDupicatedSerialVersionUID();
//        personWithDupicatedSerialVersionUID.setAge(20);
//        personWithDupicatedSerialVersionUID.setName("Joe");
//
//        File tempFile = writeObjectInFile(person);
//        File tempFile2 = writeObjectInFile(personWithDupicatedSerialVersionUID);
//
//        Person outputPerson = (Person) readObjectFromFile(tempFile);
//        AnotherPersonWithDupicatedSerialVersionUID outputAnother = (AnotherPersonWithDupicatedSerialVersionUID) readObjectFromFile(tempFile2);
//        assertTrue(outputAnother.getAge() == person.getAge());
//        assertTrue(outputAnother.getName().equals(person.getName()));
//        tempFile.deleteOnExit();
//        tempFile2.deleteOnExit();
//    }

    @Test
    public void testDupicatedSerialVersionUID() throws IOException, ClassNotFoundException {
        Person person = new Person();
        person.setAge(20);
        person.setName("Joe");
        SubPersonWithSameSerialUID subPersonWithSameSerialUID = new SubPersonWithSameSerialUID();
        subPersonWithSameSerialUID.setAge(20);
        subPersonWithSameSerialUID.setName("Joe");


        File tempFile = writeObjectsInFile(person, subPersonWithSameSerialUID);


        Person[] outputPerson = (Person[]) readObjectFromFile(tempFile);


        assertTrue(outputPerson[0].getAge() == person.getAge());
        assertTrue(outputPerson[0].getName().equals(person.getName()));
        tempFile.deleteOnExit();

    }

    private File writeObjectInFile(Object object) throws IOException {
        File tempFile = File.createTempFile("SerializableTest-",".txt");
        FileOutputStream fileOutputStream
                = new FileOutputStream(tempFile);
        ObjectOutputStream objectOutputStream
                = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        objectOutputStream.close();
        return tempFile;
    }

    private File writeObjectsInFile(Object... object) throws IOException {
        File tempFile = File.createTempFile("SerializableTest-",".txt");
        FileOutputStream fileOutputStream
                = new FileOutputStream(tempFile);
        ObjectOutputStream objectOutputStream
                = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        objectOutputStream.close();
        return tempFile;
    }


    private Object readObjectFromFile(File tempFile) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream
                = new FileInputStream(tempFile);
        ObjectInputStream objectInputStream
                = new ObjectInputStream(fileInputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        return object;
    }

    private Object[] readObjectsFromFile(File tempFile) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream
                = new FileInputStream(tempFile);
        ObjectInputStream objectInputStream
                = new ObjectInputStream(fileInputStream);
        Object[] object = (Object[]) objectInputStream.readObject();
        objectInputStream.close();
        return object;
    }



//inner class Serializable and outer class not  ==> will issue java.io.NotSerializableException
//inner class Serializable and outer class also ==> OK

}
