package elasticsearch.service;

import entities.Person;

import java.util.Collection;
import java.util.Map;


public interface Repository {

    void insert(Person person);

    void update(Person person);

    void delete(Person person);

    Person get(String identifier);

    Collection<Person> search(Map<String, Object> map);

}
