package com.bill.demo.json.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    private void prettyPrint(String uglyJSONString) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonString = gson.toJson(je);
        System.out.println(prettyJsonString);
    }

    /**
     * Facade function to convert a json String to a map (of json path mapping to value)
     */
    public Map<String, String> convertJsonToMap(String json){
        Map<String, String> jsonPathsMapingValue = new HashMap<>();
        Object jsonObj = getMapByJson(json);
        getJsonPathMapValue(JSON_PATH_PREFIX, jsonObj, jsonPathsMapingValue);
        return jsonPathsMapingValue;
    }
    /**
     * Facade function to convert a map (of json path mapping and value) to a json String
     */
    public String convertMapToJson(Map<String, String>  json){
        Object jsonObj = getJsonMap(json);
        return getJsonByMap(jsonObj);
    }





    /**
     * get a Map of json structure from json
     *
     * @param json
     * @return
     */
    public static Object getMapByJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Map.class);
    }

    public static final String JSON_PATH_PREFIX = "$";

    /**
     * get a Map of jsonpath mapping to value
     *
     * @param nameAppended
     * @param currentNode
     * @param jsonPathsMapingValue
     */
    public static void getJsonPathMapValue(String nameAppended, Object currentNode, Map<String, String> jsonPathsMapingValue) {
        if (nameAppended == null || "".equals(nameAppended.trim())) {
            nameAppended = JSON_PATH_PREFIX;
        }
        if (currentNode instanceof Map) {
            if (((Map) currentNode).isEmpty()) {
                jsonPathsMapingValue.put(nameAppended, null);
            }
            for (Object key : ((Map) currentNode).keySet()) {
                Object obj = ((Map) currentNode).get(key);
                if (obj instanceof Map) {
                    getJsonPathMapValue(nameAppended + "." + key, obj, jsonPathsMapingValue);
                } else if (obj instanceof List) {
                    getJsonPathMapValue(nameAppended + "." + key, obj, jsonPathsMapingValue);
                } else {
                    if (obj != null) {
                        jsonPathsMapingValue.put(nameAppended + "." + key, obj.toString());
                    } else {
                        jsonPathsMapingValue.put(nameAppended + "." + key, "null");
                    }
                }
            }
        } else if (currentNode instanceof List) {
            if (((List) currentNode).isEmpty()) {
                jsonPathsMapingValue.put(nameAppended, null);
            }
            int index = 0;
            for (Object obj : (List) currentNode) {
                if (obj instanceof Map) {
                    getJsonPathMapValue(nameAppended + ("[" + (index++) + "]"), obj, jsonPathsMapingValue);
                } else if (obj instanceof List) {
                    getJsonPathMapValue(nameAppended + ("[" + (index++) + "]"), obj, jsonPathsMapingValue);
                } else {
                    if (obj != null) {
                        jsonPathsMapingValue.put(nameAppended + ("[" + (index++) + "]"), obj.toString());
                    } else {
                        jsonPathsMapingValue.put(nameAppended + ("[" + (index++) + "]"), "null");
                    }
                }
            }
        } else {
            jsonPathsMapingValue.put(nameAppended, currentNode.toString());
        }
    }

    /**
     * from a Map of json path mapping to value, get a Map of json structure
     *
     * @param jsonPathsMapValue
     * @return
     */
    public static Object getJsonMap(Map<String, String> jsonPathsMapValue) {
        Map merged = new HashMap();
        for (String key : jsonPathsMapValue.keySet()) {
            Object jsonMapByOneJsonPath = generateMapByJsonPath(key, jsonPathsMapValue.get(key));
            mergeMap(merged, (Map) jsonMapByOneJsonPath);
        }
        return merged.get(JSON_PATH_PREFIX);
    }

    /**
     * get a json from a Map of json structure
     *
     * @param jsonMap
     * @return
     */
    public static String getJsonByMap(Object jsonMap) {
        JSONObject json;
        if(jsonMap instanceof Map) {
            json = new JSONObject(jsonMap);
        }else if(jsonMap instanceof List){
            Map<String, Object> outterMap = new HashMap<>();
            outterMap.put("data", jsonMap);
            json = new JSONObject(outterMap);
        }else{
            Map<String, Object> outterMap = new HashMap<>();
            json = new JSONObject(outterMap);
        }

        return json.toString();
    }


    private static List setValueInList(List currentList, int index, Object obj) {
        if (currentList == null) {
            currentList = new ArrayList();
        }
        while (currentList.size() < index + 1) {
            currentList.add(null);
        }
        currentList.set(index, obj);
        return currentList;
    }


    private static Object generateMapByJsonPath(String jsonPath, String value) {
        String[] keys = jsonPath.split("\\.");
        Object json = null;
        for (int i = keys.length - 1; i > -1; i--) {
            if (keys[i].indexOf("[") > -1) {//list
                Map currentLevelMap = new HashMap<String, Object>();
                int index = Integer.parseInt(keys[i].substring(keys[i].indexOf("[") + 1, keys[i].indexOf("]")));
                if (i == keys.length - 1) {
                    currentLevelMap.put((keys[i].substring(0, keys[i].indexOf("["))), setValueInList(null, index, value));
                } else {
                    currentLevelMap.put((keys[i].substring(0, keys[i].indexOf("["))), setValueInList(null, index, json));
                }
                json = currentLevelMap;
            } else {//map
                Map currentLevelMap = new HashMap<String, Object>();
                if (i == keys.length - 1) {
                    currentLevelMap.put(keys[i], value);
                } else {
                    currentLevelMap.put(keys[i], json);
                }
                json = currentLevelMap;
            }
        }
//        System.out.println("DEBUG:======>"+jsonPath+"======>"+json);
        return json;

    }

    private static int mergeListElement(List to, List from) {
        if (to.size() < from.size()) {
            while (to.size() < from.size()) {
                to.add(null);
            }
            to.set(from.size() - 1, from.get(from.size() - 1));//since from oly have one element
        } else {
            to.set(from.size() - 1, from.get(from.size() - 1));//since from oly have one element
        }
        return from.size() - 1;
    }


    private static void mergeMap(Object to, Object from) {
        if (from instanceof Map) {
            if (to != null) {
                for (String key : (((Map<String, Object>) from).keySet())) {//loop next level
                    Object fromEntryValue = ((Map<String, Object>) from).get(key);//next level value
                    if (fromEntryValue instanceof Map
                            || fromEntryValue instanceof List) {
                        Object toEntryValue = ((Map<String, Object>) to).get(key);
                        if (toEntryValue == null) {
                            ((Map<String, Object>) to).put(key, fromEntryValue);
                        } else {
                            mergeMap(toEntryValue, fromEntryValue);
                        }

                    } else {
                        ((Map<String, Object>) to).put(key, fromEntryValue);
                    }
                }
            } else {
                to = from;
            }
        } else if (from instanceof List) {
            if (to != null) {
                int index = (((List) from).size() - 1);
                if (((List) to).size() >= ((List) from).size()
                        && ((List) to).get(index) != null) { //loop next level. from as list must only have one element and at the end of the list
                    Object fromEntryValue = ((List) from).get(index);//nextlevel
                    Object toEntryValue = ((List) to).get(index);//nextlevel
                    if (fromEntryValue instanceof List
                            || fromEntryValue instanceof Map) {
                        mergeMap(toEntryValue, fromEntryValue);
                    }
                } else {
                    mergeListElement((List) to, (List) from);
                }
            } else {
                to = from;
            }

        } else {
            to = from;
        }

        System.out.println("DEBUG:to======>" + to);

    }
}
