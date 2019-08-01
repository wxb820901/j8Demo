package com.bill.demo.j8;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import com.jayway.jsonpath.JsonPath;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class JsonTest {
    @Test
    public void testOrgJson1() throws JSONException {
        //对象嵌套数组嵌套对象
        String json1 = "{'id':1,'name':'JAVAEE-1703','stus':[{'id':101,'name':'刘一','age':16}]}";
        //解析第一层---对象
        JSONObject jObject1 = new JSONObject(json1);
        System.out.println(jObject1.getInt("id"));
        //解析第二层----数组
        JSONArray jsonArray2 = jObject1.getJSONArray("stus");
        //遍历数组获取元素----对象
        for (int i = 0; i < jsonArray2.length(); i++) {
            //解析第三层----对象
            JSONObject jObject3 = jsonArray2.getJSONObject(i);
            System.out.println(jObject3.getInt("id"));
            System.out.println(jObject3.getInt("name"));
            System.out.println(jObject3.getInt("age"));
        }

        //数组
        String json2 = "['北京','天津','杭州']";
        //获取数组对象
        JSONArray jArray = new JSONArray(json2);
        ArrayList<String> list = new ArrayList<>();
        //遍历获取元素
        for (int i = 0; i < jArray.length(); i++) {
            //jArray.optString(i);//等价于getXXX
            list.add(jArray.getString(i));
        }
        System.out.println("解析结果：" + list);
    }

    @Test
    public void testOrgJson2() {
        String json1 = "{'id':1,'name':'JAVAEE-1703','stus':[{'id':101,'name':'刘一','age':16}]}";
        JSONObject jObject1 = new JSONObject(json1);
        System.out.println("jObject1.names():" + jObject1.names());
        Iterator keys = jObject1.keys();
        while (keys.hasNext()) {
            System.out.println("  jObject1.keys():" + keys.next());
        }
        System.out.println("jjObject1.keySet():" + jObject1.keySet());
    }


    private void prettyPrint(String uglyJSONString) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonString = gson.toJson(je);
        System.out.println(prettyJsonString);
    }


    private String json1 =
            "{\n" +
                    "        \"l1v\":\"1111\",\n" +
                    "        \"l1d\":{\n" +
                    "            \"l2v\":\"2222\",\n" +
                    "            \"l2d\":{\n" +
                    "                \"l3v\":\"aa3\"\n" +
                    "            },\n" +
                    "            \"l2a\":[\n" +
                    "                    {\"l3v1\":\"aaa4\"},\n" +
                    "                    {\"l3v2\":\"aaa4\"}\n" +
                    "                    ]\n" +
                    "        },\n" +
                    "        \"l1a\":[ \"22cv\",\"22cv\"]\n" +
                    "    }";

//    static final Set<String> jsonFilter = new HashSet(Arrays.asList(
//            "$.l1v",
//            "$.l1a",
//            "$.l1d.l2a[1]"));


    @Test
    public void testGetStructureByGson() {
        System.out.println("===================json as below  ===================");
        prettyPrint(json1);
        Object jsonObj = getMapByJson(json1);
        System.out.println("=================== get json map  ===================");
        System.out.println(jsonObj);
        Map<String, String> jsonPathsMapValue = new HashMap();
        getJsonPathMapValue("$", jsonObj, jsonPathsMapValue);
        System.out.println("=================== get mapping from json-path to value   by above json ===================");
        jsonPathsMapValue.entrySet().stream().forEach(System.out::println);
        System.out.println("===================validate preview step (check if be able to get value by the json-path key) ===================");
        jsonPathsMapValue.keySet().stream().forEach(
                jsonPath -> Assert.assertEquals(
                        jsonPathsMapValue.get(jsonPath),
                        JsonPath.read(json1, jsonPath).toString()));
//        System.out.println("=================== filter json-paths ===================");
//        jsonFilter.stream().forEach(System.out::println);
//        System.out.println("=================== wanted json-paths ===================");
//        Set<String> wanted = getWantedJsonPath(jsonPathsMapValue.keySet(), jsonFilter);
//        wanted.stream().forEach(System.out::println);
//        System.out.println("=================== build json by wanted json-paths ===================");
//        System.out.println(buildJsonByWantedJsonPaths(jsonPathsMapValue, wanted));
        System.out.println("=================== build json map from json-path and valuemap ===================");
        Map jsonMap = (Map<String, ?>) getJsonMap(jsonPathsMapValue);
        System.out.println(jsonMap);
        System.out.println("=================== restructure origin json  ===================");
        prettyPrint(getJsonByMap(jsonMap));
    }


//    private Set<String> getWantedJsonPath(Set<String> originjsonPaths, Set<String> jsonPaths) {
//        return originjsonPaths.stream().filter(jsonPath -> {
//            for (String filterJsonPath : jsonPaths) {
//                if (jsonPath.indexOf(filterJsonPath) > -1) {//regex
//                    return true;
//                }
//            }
//            return false;
//        }).collect(Collectors.toCollection(HashSet::new));
//    }


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
    public static String getJsonByMap(Map jsonMap) {
        net.minidev.json.JSONObject json = new net.minidev.json.JSONObject(jsonMap);
        return json.toJSONString();
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
