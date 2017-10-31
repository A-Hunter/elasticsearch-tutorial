package elasticsearch.service;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by Ghazi Ennacer on 31/10/2017.
 */
public class RepositoryServiceElasticSearch {

    private Client client;

    public RepositoryServiceElasticSearch() {
    }

    private Client createClient() {
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", "elasticsearch")
                //   .put("client.transport.sniff",true) // we need to disable the sniffing because we want to connect to bulk
                .build();
        try {
            return client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        }
    }

    public void insert(String index, String type, Map<String, String> data) {
        Client client = createClient();
        IndexResponse response = client.prepareIndex(index, type)
                .setSource(data).get();

        client.close();
    }

    public String get(String index, String type, String id) {
        Client c = createClient();
        GetResponse response = c.prepareGet(index, type, id).get();
        String result = response.getSourceAsString();
        c.close();
        return result;
    }

    public void delete(String index, String type, String id) {
        Client c = createClient();
        c.prepareDelete(index, type, id).get();
        c.close();
    }

    public void update(String index, String type, String id, String field, String newValue) {
        Client c = createClient();
        Map<String, Object> updateObject = new HashMap<String, Object>();
        updateObject.put(field, newValue);
        c.prepareUpdate(index, type, id).setDoc(updateObject).execute().actionGet();
        c.close();
    }

    public void upsert(String index, String type, String id, String field, String newValue) {
        Client c = createClient();
        try {
            IndexRequest indexRequest = new IndexRequest(index, type, id)
                    .source(jsonBuilder()
                            .startObject()
                            .field(field, newValue)
                            .endObject());
            UpdateRequest updateRequest = new UpdateRequest(index, type, id)
                    .doc(jsonBuilder()
                            .startObject()
                            .field(field, newValue)
                            .endObject())
                    .upsert(indexRequest);

            c.update(updateRequest).get();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
    }

    public void multiGet(String index, String type, String id1, String id2) {
        Client c = createClient();
        MultiGetResponse multiGetItemResponses = c.prepareMultiGet()
                .add(index, type, id1)
                .add(index, type, id2)
                .get();

        for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                String json = response.getSourceAsString();
                System.out.println(json);
            }
        }
        c.close();
    }

    public void multiAllGet(String index, String type, String[] ids) {
        Client c = createClient();
        for (String id : ids) {
            MultiGetResponse multiGetItemResponses = c.prepareMultiGet()
                    .add(index, type, id)
                    .get();

            for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
                GetResponse response = itemResponse.getResponse();
                if (response.isExists()) {
                    String json = response.getSourceAsString();
                    System.out.println(json);
                }
            }
        }
        c.close();
    }

    public void search(String index, String type, String field, String value) {

        Client c = createClient();
        SearchResponse response = c.prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchQuery(field, value))
                .execute()
                .actionGet();
        SearchHit[] results = response.getHits().getHits();

        System.out.println("Current results: " + results.length);
        for (SearchHit hit : results) {
            System.out.println("------------------------------");
            Map<String, Object> result = hit.getSource();
            System.out.println(result);
        }
        c.close();
    }

    public void multiSearch(String field, String value, String searchableValue) {
        Client c = createClient();
        SearchRequestBuilder srb1 = c
                .prepareSearch().setQuery(QueryBuilders.queryStringQuery(searchableValue)).setSize(1);
        SearchRequestBuilder srb2 = c
                .prepareSearch().setQuery(QueryBuilders.matchQuery(field, value)).setSize(1);

        MultiSearchResponse sr = c.prepareMultiSearch()
                .add(srb1)
                .add(srb2)
                .execute().actionGet();

// You will get all individual responses from MultiSearchResponse#getResponses()
        long nbHits = 0;
        for (MultiSearchResponse.Item item : sr.getResponses()) {
            SearchResponse response = item.getResponse();
            nbHits += response.getHits().getTotalHits();
        }
        System.out.println("The number of hits is : " + nbHits);
        c.close();
    }

    public static void main(String[] args) {
        String index = "persons";
        String type = "person";

        RepositoryServiceElasticSearch rep = new RepositoryServiceElasticSearch();

        rep.search(index,type, "key","value");

    }
}
