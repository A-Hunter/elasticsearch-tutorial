package elasticsearch.service;

import entities.Person;

/**
 * Created by Ghazi Naceur on 26/06/2017.
 */
public class Main {

    public static void main(String[] args) {


        RepositoryService service = new RepositoryService();

//        service.insert(new Person("id1","Netero","Isaac",125,"Hunter","Hitachi"));
//        service.insert(new Person("id2","Itachi","Uchiha",25,"Shinobi","Konoha"));
//        service.insert(new Person("id3","Gon","Freecss",15,"Hunter","York city"));
//        service.insert(new Person("id4","Zino","Zoldick",75,"Assassin","Yokohama"));
//        service.insert(new Person("id5","Kakashi","Hatake",45,"Shinobi","Konoha"));
//
//        Person p = new Person("id1", "Netero", "Isaac", 125, "Hunter", "Dark continent");
//        service.update(p);

        Person per = new Person("id2", "Itachi","Uchiha",25,"Shinobi","Konoha");
        service.delete(per);
//
//        Person person = service.get("");
//        System.out.println(person);
    }
}
