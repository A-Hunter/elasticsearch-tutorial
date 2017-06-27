package elasticsearch.service;

import entities.Person;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;


/**
 * Created by Ghazi Naceur on 15/06/2017.
 */
public class RepositoryService implements Repository{

    private Client client;

    private static final String INDEX = "persons";

    private static final String TYPE = "person";

    private static final String HOST = "localhost";

    private static final int PORT = 9300;

    private static final String CLUSTER_NAME = "cluster.name";

    private static final String CLUSTER = "elasticsearch";

    public RepositoryService() {
        super();
    }

    private Client createClient() {
        Settings settings = Settings.builder()
                .put(CLUSTER_NAME, CLUSTER)
                .build();
        try {
            return client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(HOST), PORT));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        }
    }

    private String returnID(String identifier){
        Client client = createClient();
        SearchResponse response = client.prepareSearch(INDEX)
                .setTypes(TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchPhraseQuery("id", identifier))
                .execute()
                .actionGet();
        String EsId = "";
        client.admin().indices().prepareFlush(INDEX).get();
        SearchHit[] results = response.getHits().getHits();
        for (SearchHit hit : results) {
            EsId = hit.getId();
        }
        client.close();
        return EsId;
    }

    public void insert(Person person){
        Client client = createClient();
        Map<String, Object> map = person.asMap();
        client.prepareIndex(INDEX, TYPE).setSource(map).get();
        client.admin().indices().prepareFlush(INDEX).get();
        client.close();
    }

    public Person get(String modelID) {

        Client c = createClient();
        RepositoryService repository = new RepositoryService();
        String EsId = repository.returnID(modelID);
        GetResponse response1 = c.prepareGet(INDEX, TYPE, EsId).get();
        Map<String, Object> mapResult = response1.getSourceAsMap();
        Person person = new Person(mapResult);
        System.out.println(person.asMap());
        c.close();
        return person;
    }

    public void delete(Person person) {
        Client client = createClient();
        Map<String, Object> map = person.asMap();
        Object value = map.get("identifier");
        String id = value.toString();
        RepositoryService repository = new RepositoryService();
        String EsId = repository.returnID(id);
        client.prepareDelete(INDEX, TYPE, EsId).get();
        client.close();
    }

    public void update(Person person) {
        Client client = createClient();
        Map<String, Object> map = person.asMap();
        Object value = map.get("identifier");
        String modelID = value.toString();
        RepositoryService repository = new RepositoryService();
        String EsId = repository.returnID(modelID);
        client.prepareUpdate(INDEX, TYPE, EsId).setDoc(map).execute().actionGet();
        client.close();
    }

    public Collection<Person> search(Map<String, Object> metaData) {

        Client c = createClient();
        BoolQueryBuilder b = boolQuery();
        for (Map.Entry<String, Object> entry : metaData.entrySet()) {
            b.must(matchPhraseQuery(entry.getKey(), entry.getValue()));
        }
        SearchResponse response = c
                .prepareSearch(INDEX)
                .setTypes(TYPE)
                .setQuery(b)
                .execute().actionGet();

        SearchHit[] results = response.getHits().getHits();

        Person person;
        Map<String, Object> res = new HashMap<>();
        Collection<Person> result = new ArrayList<>();
        Collection<Map<String, Object>> test = new ArrayList<>();
        for (SearchHit hit : results) {
            person = new Person(hit.getSource());
            result.add(person);
            System.out.println(person.asMap());
        }
        c.close();
        return result;
    }


}
