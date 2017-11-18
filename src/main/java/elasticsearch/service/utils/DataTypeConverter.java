package elasticsearch.service.utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Ghazi Ennacer on 18/11/2017.
 */
public class DataTypeConverter {

    public DataTypeConverter() {
    }

    /**
     *
     * @param entity The provided string is encapsulated with brackets [].
     *               It represents the Value of an entry of a Map.
     *               Map from elasticsearch
     * @return
     */
    public Map<String, Object> stringToMap(String entity){
            String str = entity.substring(1, entity.length()-1);
            String[] pairs = str.split(",");
            Map<String,Object> res = new HashMap<>();
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            res.put(keyValue[0], keyValue[1]);
        }
        return res;
    }

    /**
     *
     * @param entity data to be inserted in elasticsearch
     * @return
     */
    public String mapToString(Map<String, Object> entity){
        JSONObject json = new JSONObject();
        String result = entity.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(","));
        json.put(String.valueOf(1),result);
        return json.toString();
    }

    public static void main(String[] args) {
        DataTypeConverter converter = new DataTypeConverter();
        String entity = "[firstName:Naruto,lastName:Uzumaki,occupation:Hunter,Address:Konoha,id:id1,age:15]";
        Map<String, Object> result = converter.stringToMap(entity);
        System.out.println(result.toString());

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key1","value1");
        metadata.put("key2","value2");
        metadata.put("key3","value3");
        metadata.put("key4","value4");
        metadata.put("key5","value5");
        String stringfied = converter.mapToString(metadata);
        System.out.println(stringfied);
    }
}
