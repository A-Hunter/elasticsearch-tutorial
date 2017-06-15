package entities;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ghazi Naceur on 15/06/2017.
 */
public class Person {

    private String identifier;
    private String firstName;
    private String lastName;
    private Long age;
    private String occupation;
    private String address;

    public Person() {
        super();
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

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
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
        personMap.put("identifier",identifier);
        personMap.put("firstName",firstName);
        personMap.put("lastName",lastName);
        personMap.put("age",age);
        personMap.put("occupation",occupation);
        personMap.put("address",address);
        return personMap;
    }
}
