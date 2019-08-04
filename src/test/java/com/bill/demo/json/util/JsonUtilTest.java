package com.bill.demo.json.util;

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

import static com.bill.demo.json.util.JsonUtil.*;
import static com.jayway.jsonpath.internal.JsonFormatter.prettyPrint;

public class JsonUtilTest {
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
            System.out.println(jObject3.getString("name"));
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





}
