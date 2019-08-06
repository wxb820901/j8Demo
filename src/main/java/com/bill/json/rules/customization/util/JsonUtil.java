package com.bill.json.rules.customization.util;

import com.bill.json.rules.customization.ODataQueryExpression;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;


import java.util.*;
import java.util.stream.Collectors;

/**
 * transfer between json and matched structure map,
 * transfer between json and jsonpath map value
 *
 * @author Bill Wang
 * @throws
 * @created 2019-07-26 10:30
 * @see Gson, JSONObject
 */
public class JsonUtil {
    private static final Logger logger = LogManager.getLogger(JsonUtil.class);

    /**
     * get java.util.Map from a json
     *
     * @param json String json
     * @return Map or List of json structure
     */
    public static Object getMapByJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Map.class);
    }

    /**
     * self loop for a map, entry key are json path to leaf value, entry value is the leaf value
     *
     * @param nameAppended used by loop invoke
     * @param currentNode used by loop invoke
     * @param jsonPathsMapingValue result map
     * @return void
     */
    public static final String JSON_PATH_PREFIX = "$";

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
     * get a json structure map by a match json path and value entry map
     *
     * @param jsonPathsMapValue json path and value map
     * @return Object (list/map)
     */
    public static Object getJsonMap(Map<String, String> jsonPathsMapValue) {
        Map merged = new LinkedHashMap();
        for (String key : jsonPathsMapValue.keySet()) {
            Object jsonMapByOneJsonPath = generateMapByJsonPath(key, jsonPathsMapValue.get(key));
            mergeMap(merged, (Map) jsonMapByOneJsonPath);
        }
        return merged.get(JSON_PATH_PREFIX);
    }

    /**
     * get a json by a json structure map
     *
     * @param jsonMap a json structure map
     * @return String
     */
    public static String getJsonByMap(Object jsonMap) {
        if (jsonMap == null) {
            return "{}";
        }
        Map<String, Object> outterMap = new LinkedHashMap<>();
        outterMap.put("data", jsonMap);
        JSONObject json = new JSONObject(outterMap);

        return json.get("data").toString();
    }


    /**
     * add element to list by index for generateMapByJsonPath
     *
     * @param currentList the list
     * @param index       the index
     * @param obj         the value
     * @return List
     */
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

    /**
     * generate json structure by one entry of jsonPathsMapValue map
     *
     * @param jsonPath jsonPathsMapValue entry key
     * @param value    jsonPathsMapValue entry value
     * @return Object (list/map)
     */
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
        return json;

    }

    /**
     * used by mergeMap, to merge list element, make sure the index
     *
     * @param to   accumulated map
     * @param from result of generateMapByJsonPath
     * @return index of inserted element
     */
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

    /**
     * merge the map from generateMapByJsonPath
     *
     * @param to   accumulated map
     * @param from result of generateMapByJsonPath
     */

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

    }

    /**
     * print json with structure and indent
     *
     * @param uglyJSONString a json String
     * @return String pretty json String
     */
    public static void prettyPrint(String uglyJSONString) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonString = gson.toJson(je);
        System.out.println(prettyJsonString);
    }

    /**
     * Facade function to convert a json String to a map (of json path mapping to value)
     */
    public static Map<String, String> convertJsonToMap(String json) {
        Map<String, String> jsonPathsMapingValue = new LinkedHashMap<>();
        Object jsonObj = getMapByJson(json);
        getJsonPathMapValue(JSON_PATH_PREFIX, jsonObj, jsonPathsMapingValue);
        return jsonPathsMapingValue;
    }

    /**
     * Facade function to convert a map (of json path mapping and value) to a json String
     */
    public static String convertMapToJson(Map<String, String> json) {
        Object jsonObj = getJsonMap(json);
        return getJsonByMap(jsonObj);
    }


    public static Map<String, String> getListByPrefix(Map<String, String> originjsonPaths, ODataQueryExpression filterOrSelect) {
        return originjsonPaths
                .entrySet().stream().filter(
                        entry ->
                                entry.getKey().indexOf(filterOrSelect.getPrefix()) > -1
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}

