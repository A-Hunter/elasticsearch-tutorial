package entities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Ghazi Naceur on 15/06/2017.
 */
public class Person {

    private String identifier;
    private String firstName;
    private String lastName;
    private int age;
    private String occupation;
    private String address;

    Map<String, Object> attributes = new HashMap<>();

    public Person() {
        super();
    }

    public Person(Map<String, Object> attributes) {
        Iterator it = attributes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if (pair.getKey().equals("identifier")){
                this.identifier = (String) pair.getValue();
            }else if (pair.getKey().equals("firstName")){
                this.firstName = (String) pair.getValue();
            }else if (pair.getKey().equals("lastName")){
                this.lastName = (String) pair.getValue();
            }else if (pair.getKey().equals("age")){
                this.age = (int) pair.getValue();
            }else if (pair.getKey().equals("occupation")){
                this.occupation = (String) pair.getValue();
            }else if (pair.getKey().equals("address")){
                this.address = (String) pair.getValue();
            }

            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public Person(String identifier, String firstName, String lastName, int age, String occupation, String address) {
        this.identifier = identifier;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.occupation = occupation;
        this.address = address;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<String, Object> asMap() {
        Map<String, Object> personMap = new HashMap<>();
        personMap.put("identifier", identifier);
        personMap.put("firstName", firstName);
        personMap.put("lastName", lastName);
        personMap.put("age", age);
        personMap.put("occupation", occupation);
        personMap.put("address", address);
        return personMap;
    }

    @Override
    public String toString() {
        return "Person{" +
                "identifier='" + identifier + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", occupation='" + occupation + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
