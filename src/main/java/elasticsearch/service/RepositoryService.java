package elasticsearch.service;

import entities.Person;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Map;


/**
 * Created by Ghazi Naceur on 15/06/2017.
 */
public class RepositoryService implements Repository{

    private Client client;

    private static final String INDEX = "profiles";

    private static final String TYPE = "profile";

    private static final String HOST = "localhost";

    private static final int PORT = 9300;

    private static final String CLUSTER_NAME = "cluster.name";

    private static final String CLUSTER = "elasticsearch";

    public RepositoryService() {
        super();
    }

    private Client createClient() {
        Settings settings = Settings.settingsBuilder()
                .put(CLUSTER_NAME, CLUSTER)
                .build();
        try {
            return client = TransportClient.builder().settings(settings).build()
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

    @Override
    public void insert(Person person){
        Client client = createClient();
        Map<String, Object> map = person.asMap();
        client.prepareIndex(INDEX, TYPE).setSource(map).get();
        client.admin().indices().prepareFlush(INDEX).get();
        client.close();
    }

    @Override
    public void update(Person person) {

    }

    @Override
    public void delete(Person person) {

    }

    @Override
    public Person get(String identifier) {
        return null;
    }

    @Override
    public Collection<Person> search(Map<String, Object> map) {
        return null;
    }


}
