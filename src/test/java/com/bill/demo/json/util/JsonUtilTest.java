package com.bill.demo.json.util;

import com.bill.demo.json.util.rules.ODataOperation;
import com.bill.demo.json.util.rules.ODataOperationExpression;
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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.bill.demo.json.util.JsonUtil.*;
import static com.jayway.jsonpath.internal.JsonFormatter.prettyPrint;
import static java.util.Map.*;

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





    public static final String inputJson =
            "{\n" +
                    "    \"prefix1\":[\n" +
                    "         {\n" +
                    "             \"label1\":\"5\",\n" +
                    "             \"label2\":\"0\",\n" +
                    "             \"label3\":\"fgfg\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "             \"label1\":\"2\",\n" +
                    "             \"label2\":\"9\",\n" +
                    "             \"label3\":\"fgfg\"\n" +
                    "         }\n" +
                    "    ],\n" +
                    "    \"prefix2\":[\n" +
                    "          {\n" +
                    "              \"label1\":\"2\",\n" +
                    "              \"label2\":\"100\",\n" +
                    "              \"label3\":\"fgfg\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"label1\":\"5\",\n" +
                    "              \"label2\":\"101\",\n" +
                    "              \"label3\":\"fgfg\"\n" +
                    "          }\n" +
                    "    ]\n" +
                    "}";
    @Test
    public void testjsonFilterAndSelect() {
        Map<String, String> obj =  convertJsonToMap(inputJson);
        obj.entrySet().stream().forEach(System.out::println);


        String json = convertMapToJson(obj);
        prettyPrint(json);
    }


    /**
     * assume ODataOperationExpression.getExpression() like 'label1 eq sdsd and label2 eq 1234 or  label3 = as34'
     *
     * @param originjsonPaths
     * @param filterOrSelect
     * @return
     */
    public static Map<String, String> getFiltered(Map<String, String> originjsonPaths, ODataOperationExpression filterOrSelect){
        Set wantedPath = originjsonPaths
                .keySet().stream().filter(
                        key -> key.indexOf(filterOrSelect.getExpression())>-1)
                .collect(Collectors.toCollection(HashSet::new));
        Map<String, String> wantedPathAndValue = new HashMap<>();

        return wantedPathAndValue;

    }




    @Test
    public void testGetSelect(){
        ODataOperationExpression expression = ODataOperationExpression.listExpression.withExression("label1, label2");
        Map<String, String> obj =  convertJsonToMap(inputJson);
        obj.entrySet().stream().forEach(System.out::println);
        System.out.println("============================================================================");
        obj = getSelected(obj, expression);
        obj.entrySet().stream().forEach(System.out::println);
        String json = convertMapToJson(obj);
        prettyPrint(json);
    }


    @Test
    public void testOrdered() throws Exception {
        ODataOperationExpression expression = ODataOperationExpression.listExpression.withExression("label1").withPrefix("prefix1");
        Map<String, String> obj =  convertJsonToMap(inputJson);
        obj.entrySet().stream().forEach(System.out::println);
        System.out.println("============================================================================");
        obj = getOrdered(obj, expression);
        obj.entrySet().stream().forEach(System.out::println);
    }

    @Test
    public void testCount() throws Exception {
        ODataOperationExpression expression = ODataOperationExpression.listExpression.withExression("").withPrefix("prefix1");
        Map<String, String> obj =  convertJsonToMap(inputJson);
        obj.entrySet().stream().forEach(System.out::println);
        System.out.println("============================================================================");
        obj = getCount(obj, expression);
        obj.entrySet().stream().forEach(System.out::println);
    }



    @Test
    public void testjsonRegex() {

        System.out.println(Pattern.compile("$.prefix1[*\\d].label3").matcher("$.prefix1[1].label3").find());
    }

}
