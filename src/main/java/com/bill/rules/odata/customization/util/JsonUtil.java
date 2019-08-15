package com.bill.rules.odata.customization.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.*;
import java.util.function.Predicate;
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

    private JsonUtil() {

    }

    /**
     * json ==> map/list
     * get a json structure map/list from a json
     *
     * @param json String json
     * @return Map or List of json structure
     */
    public static Object getMapByJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Map.class);
    }


    /**
     * map/list ==> json
     * get a json by a json structure map/list
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
     * json ==> json path map
     * Facade function to convert a json String to a map (of json path mapping to value)
     */
    public static Map<String, String> convertJsonToMap(String json) {
        Map<String, String> jsonPathsMapingValue = new LinkedHashMap<>();
        Object jsonObj = getMapByJson(json);
        getJsonPathMapValue(JSON_PATH_PREFIX, jsonObj, jsonPathsMapingValue);
        return jsonPathsMapingValue;
    }


    /**
     * json path map ==> json
     * Facade function to convert a map (of json path mapping and value) to a json String
     */
    public static String convertMapToJson(Map<String, String> json) {
        Object jsonObj = getJsonMap(json);
        return getJsonByMap(jsonObj);
    }


    /**
     * json path ==> map/list
     *
     * @param json
     * @return
     */
    public static Object convertJsonPathToMap(Map<String, String> json) {
        return getMapByJson(convertMapToJson(json));
    }

    /**
     * map/list ==> json path
     *
     * @param obj
     * @return
     */
    public static Map<String, String> convertMapToJsonPath(Object obj) {
        return convertJsonToMap(getJsonByMap(obj));
    }


    public static final String JSON_PATH_PREFIX = "$";

    /**
     * json path map ==> map
     *
     * @param jsonPathsMapValue json path and value map
     * @return Object (list/map)
     */
    public static Object getJsonMap(Map<String, String> jsonPathsMapValue) {
        Map merged = new LinkedHashMap();
        for (Map.Entry<String, String> key : jsonPathsMapValue.entrySet()) {
            Object jsonMapByOneJsonPath = generateMapByJsonPath(key.getKey(), key.getValue());
            mergeMap(merged, jsonMapByOneJsonPath);
        }
        return merged.get(JSON_PATH_PREFIX);
    }

    /**
     * self loop for a map, entry key are json path to leaf value, entry value is the leaf value
     *
     * @param nameAppended         used by loop invoke
     * @param currentNode          used by loop invoke
     * @param jsonPathsMapingValue result map
     * @return void
     */
    private static void getJsonPathMapValue(String nameAppended, Object currentNode, Map<String, String> jsonPathsMapingValue) {

        if (currentNode instanceof Map) {
            for (Object key : ((Map) currentNode).keySet()) {
                Object obj = ((Map) currentNode).get(key);
                if (obj instanceof Map || obj instanceof List) {
                    getJsonPathMapValue(nameAppended + "." + key, obj, jsonPathsMapingValue);
                } else {
                    if( obj != null) {
                        jsonPathsMapingValue.put(nameAppended + "." + key, obj.toString());
                    }
                }
            }
        } else if (currentNode instanceof List) {
            int index = 0;
            for (Object obj : (List) currentNode) {
                if (obj instanceof Map || obj instanceof List) {
                    getJsonPathMapValue(nameAppended + ("[" + (index++) + "]"), obj, jsonPathsMapingValue);
                } else {
                    if(obj != null) {
                        jsonPathsMapingValue.put(nameAppended + ("[" + (index++) + "]"), obj.toString());
                    }
                }
            }
        } else {
            jsonPathsMapingValue.put(nameAppended, currentNode.toString());
        }
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
            if (keys[i].indexOf('[') > -1) {//list
                Map currentLevelMap = new HashMap<String, Object>();
                int index = Integer.parseInt(keys[i].substring(keys[i].indexOf('[') + 1, keys[i].indexOf(']')));
                if (i == keys.length - 1) {
                    currentLevelMap.put((keys[i].substring(0, keys[i].indexOf('['))), setValueInList(null, index, value));
                } else {
                    currentLevelMap.put((keys[i].substring(0, keys[i].indexOf('['))), setValueInList(null, index, json));
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
        if (from instanceof Map && to != null) {
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

        } else if (from instanceof List && to != null) {
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
        logger.debug(prettyJsonString);
    }


    /**
     * from a json structure like below, to get sub list of instrument...
     * "{\n" +
     * "    \"instrument\":[\n" +
     * "          {\n" +
     * "              \"label1\":\"asas\",\n" +
     * "              \"label2\":\"dfdf\",\n" +
     * "              \"label3\":\"fgfg\"\n" +
     * "          },\n" +
     * "          {\n" +
     * "              \"label1\":\"sdsdsdsd\",\n" +
     * "              \"label2\":\"dfdf\",\n" +
     * "              \"label3\":\"fgfg\"\n" +
     * "          }\n" +
     * "    ],\n" +
     * "    \"quote\":[\n" +
     * "          {\n" +
     * "              \"label1\":\"asas\",\n" +
     * "              \"label2\":\"dfdf\",\n" +
     * "              \"label3\":\"fgfg\"\n" +
     * "          },\n" +
     * "          {\n" +
     * "              \"label1\":\"sdsdsdsd\",\n" +
     * "              \"label2\":\"dfdf\",\n" +
     * "              \"label3\":\"fgfg\"\n" +
     * "          }\n" +
     * "    ],\n" +
     * "    \"org\":[\n" +
     * "          {\n" +
     * "              \"label1\":\"asas\",\n" +
     * "              \"label2\":\"dfdf\",\n" +
     * "              \"label3\":\"fgfg\"\n" +
     * "          },\n" +
     * "          {\n" +
     * "              \"label1\":\"sdsdsdsd\",\n" +
     * "              \"label2\":\"dfdf\",\n" +
     * "              \"label3\":\"fgfg\"\n" +
     * "          }\n" +
     * "    ]\n" +
     * "}"
     *
     * @param originjsonPaths
     * @param prefix
     * @return
     */


    public static Map<String, String> getListByPrefix(Map<String, String> originjsonPaths, String prefix) {
        return originjsonPaths
                .entrySet().stream().filter(
                        entry ->
                                entry.getKey().indexOf(prefix) > -1
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map<String, String> isJsonPathExist(Map<String, String> originjsonPaths, String prefix) {
        return originjsonPaths
                .entrySet().stream().filter(
                        entry ->
                                entry.getKey().indexOf(prefix) > -1
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * FILTER json path map key with array list inex = *
     * for example,search json path map with
     * $.table1[*].label1.l1d[1].l2a[*].l3v1
     * result
     * $.table1[1].label1.l1d[1].l2a[2].l3v1  ==> picked
     * $.table1[1].label1.l1d[2].l2a[2].l3v1  ==> not picked
     * $.table1[1].label1.l1d[1].l2a[3].l3v1  ==> picked
     *
     * @param originjsonPaths
     * @param match
     * @return
     */
    public static Map<String, String> getSubJsonPathMapWithArrayIndex(Map<String, String> originjsonPaths, String match) {
        return originjsonPaths.entrySet().stream().filter(
                entry -> replaceArrayIndexWithStar(match, entry.getKey()).indexOf(match) > -1
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    /**
     * replace array index of path map key with *
     * for example,
     * $.table1[*].label1.l1d[1].l2a[*].l3v1
     * result
     * $.table1[1].label1.l1d[1].l2a[2].l3v1  ==> $.table1[*].label1.l1d[1].l2a[*].l3v1
     * $.table1[1].label1.l1d[2].l2a[2].l3v1  ==> $.table1[*].label1.l1d[2].l2a[*].l3v1
     * $.table1[1].label1.l1d[1].l2a[3].l3v1  ==> $.table1[*].label1.l1d[1].l2a[*].l3v1
     *
     * @param match
     * @param jsonPath
     * @return
     */
    public static String replaceArrayIndexWithStar(String match, String jsonPath) {

        List<Boolean> starHappenWhen = Arrays.asList(
                match.split("\\[")).stream().map(
                str -> '*' == str.charAt(0)
        ).collect(Collectors.toList());
        String[] splitedJsonpath = jsonPath.split("\\[");
        if (splitedJsonpath.length >= starHappenWhen.size()) {
            StringBuilder result = new StringBuilder();
            int index = 0;
            for (String prieOfJsonPath : splitedJsonpath) {
                if (index != 0) {
                    result.append("[");
                }
                if (index < starHappenWhen.size()) {
                    if (starHappenWhen.get(index)) {
                        result.append("*" + prieOfJsonPath.substring(prieOfJsonPath.indexOf(']')));
                    } else {
                        result.append(prieOfJsonPath);
                    }
                } else {
                    result.append(prieOfJsonPath);
                }
                index++;
            }
            //prevent from the array consequence right but content in gap are not
            if (result.toString().indexOf(match) > -1) {
                return result.toString();
            } else {
                return jsonPath;
            }
        } else {
            return jsonPath;
        }

    }


    public static String replaceArrayIndexWithStarWhereEver(String jsonPath) {
        return Arrays.asList(
                jsonPath.split("\\[")).stream().map(
                str -> "[*" + str.substring(str.indexOf(']'))).collect(Collectors.joining());
    }


    public static boolean like(String strSelf, String expression) {
        String str = strSelf.toLowerCase();
        String expr = expression.toLowerCase();
        if ("".equals(expr)) {
            return false;
        }
        if (expr.contains("*")) {
            String[] piecesOfKeyWord = expr.split("\\*");
            int index = 0;
            for (String piece : piecesOfKeyWord) {
                if (!"".equals(piece)) {
                    if (!str.contains(piece)) {
                        return false;
                    } else {
                        int indexOfCurrentPieceOfKeyWord = str.indexOf(piece, index);
                        if (indexOfCurrentPieceOfKeyWord == -1) {
                            return false;
                        } else {
                            index = indexOfCurrentPieceOfKeyWord + piece.length();
                        }
                    }
                }
            }
        } else {
            return str.contains(expr);
        }
        return true;
    }

    public static String getValueByJsonPath(String darkKey, Map json) {
        return convertMapToJsonPath(json).get("$." + darkKey);
    }

    public static boolean getSubJsonPathMapWithArrayIndex(Map json, String darkKey, Predicate<String[]> predicate, String value) {
        Map<String, String> jsonPath = convertMapToJsonPath(json);
        for (Map.Entry entry : jsonPath.entrySet()) {
            String[] predicateParam = new String[]{jsonPath.get(entry.getKey()), value};
            if (
                    replaceArrayIndexWithStar("$." + darkKey, entry.getKey().toString()).indexOf(darkKey) > -1 //match dark json path (jsonpath with [*])
                            && predicate.test(predicateParam)) {
                return true;
            }
        }

        return false;

    }
}

