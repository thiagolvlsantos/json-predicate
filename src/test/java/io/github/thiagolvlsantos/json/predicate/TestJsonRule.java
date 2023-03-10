package io.github.thiagolvlsantos.json.predicate;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.json.flattener.JsonFlattener;
import io.github.thiagolvlsantos.json.predicate.impl.PredicateFactoryJson;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TestJsonRule {

    String event = "{\r\n" +
            "  \"source\": \"weather\",\r\n" +
            "  \"serviceName\": \"iot-weather\",\r\n" +
            "  \"cloudType\": \"aliyun\",\r\n" +
            "  \"scope\": \"Service\",\r\n" +
            "  \"metricId\": 1234,\r\n" +
            "  \"spaceId\": 1000,\r\n" +
            "  \"metricType\": \"MetricType\",\r\n" +
            "  \"resources\": [\r\n" +
            "    \"0001\",\r\n" +
            "    \"0002\"\r\n" +
            "  ],\r\n" +
            "  \"status\": {\r\n" +
            "    \"timestamp\": 1619486736228,\r\n" +
            "    \"msgId\": \"3431\",\r\n" +
            "    \"cityNo\": \"101280604\",\r\n" +
            "    \"updateTime\": \"2021-07-27 17:00:00\",\r\n" +
            "    \"temperature\": 25,\r\n" +
            "    \"wetness\": 89,\r\n" +
            "    \"weatherText\": \"大雨啦\",\r\n" +
            "    \"windForce\": \"8.3km/h\",\r\n" +
            "    \"windDirection\": \"SW\",\r\n" +
            "    \"pm25\": 23,\r\n" +
            "    \"rainProbability\": \"18\",\r\n" +
            "    \"state\": \"running\",\r\n" +
            "    \"source-ip\": \"10.0.0.33\",\r\n" +
            "    \"list\": [\r\n" +
            "      {\r\n" +
            "        \"sceneId\": \"7890\",\r\n" +
            "        \"enable\": \"true\"\r\n" +
            "      },\r\n" +
            "      {\r\n" +
            "        \"sceneId\": \"7860\",\r\n" +
            "        \"enable\": \"true\"\r\n" +
            "      }\r\n" +
            "    ]\r\n" +
            "  }\r\n" +
            "}";

    private static IPredicateFactory factory = new PredicateFactoryJson();


    @Test
    public void testEmptyData() {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"source\": \"weather2\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        System.out.println(rule);
        Map<String, Object> mapData = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(mapData);
        Assert.assertFalse(test);
    }

    @Test
    public void testSimpleMatch() throws Exception {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"source\": \"weather\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(event, HashMap.class);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }

    @Test
    public void testFlatRuleMatch() throws Exception {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"status.state\": {\n" +
                "        \"$eq\": \"running\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        System.out.println(rule);
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(event, HashMap.class);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }


    @Test
    public void testTreeRuleMatchNotSupport() throws Exception {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"status\": {\n" +
                "        \"state\": {\n" +
                "          \"$eq\": \"running\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        System.out.println(rule);
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(event, HashMap.class);
        boolean test = false;
        try {
            Predicate<Object> pred = factory.read(rule.getBytes());
            test = pred.test(map);
        } catch (Exception e) {
            test = false;
        }
        Assert.assertFalse(test);
    }

    @Test
    public void testChineseMatch() throws Exception {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"status.weatherText\": {\n" +
                "        \"$eq\": \"大雨啦\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(event, HashMap.class);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }

    @Test
    public void testNotEqChineseMatch() throws Exception {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"status.weatherText\": {\n" +
                "        \"$ne\": \"大雨啦2\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(event, HashMap.class);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }

    @Test
    public void testNotMatch() throws Exception {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"status.state\": {\n" +
                "        \"$eq\": \"running\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"status.cityNo\": {\n" +
                "        \"$eq\": \"101280606\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        String rule2 = "{\n" +
                "  \"$not\": {\n" +
                "    \"$and\": [\n" +
                "      {\n" +
                "        \"status.state\": {\n" +
                "          \"$eq\": \"running\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"status.cityNo\": {\n" +
                "          \"$eq\": \"101280606\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(event, HashMap.class);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertFalse(test);
        //
        Predicate<Object> pred2 = factory.read(rule2.getBytes());
        boolean test2 = pred2.test(map);
        Assert.assertTrue(test2);
    }

    @Test
    public void testNumberMatch() throws Exception {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"status.temperature\": {\n" +
                "        \"$gt\": 10,\n" +
                "        \"$lt\": 26\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"status.pm25\": {\n" +
                "        \"$lt\": 90\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"status.wetness\": {\n" +
                "        \"$eq\": 89\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        System.out.println(rule);
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(event, HashMap.class);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }


    @Test
    public void testNumberMatch2() throws Exception {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"status.wetness\": {\n" +
                "        \"$gte\": 89\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(event, HashMap.class);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }

    @Test
    public void testNumberMatch3() throws Exception {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"status.wetness\": {\n" +
                "        \"$lte\": 89\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(event, HashMap.class);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }

    /***
     * 正则匹配
     * @throws Exception
     */
    @Test
    public void tesRegularMatch() throws Exception {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"status.weatherText\": {\n" +
                "        \"$match\": \".*雨.*\" \n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(event, HashMap.class);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }

    /***
     * 模糊匹配
     * @throws Exception
     */
    @Test
    public void tesRegularNotMatch() throws Exception {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"status.weatherText\": {\n" +
                "        \"$nmatch\": \".*雪.*\" \n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(event, HashMap.class);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }

    @Test
    public void tesContainMatch() throws Exception {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"resources\": {\n" +
                "        \"$contains\": \"0001\" \n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(event, HashMap.class);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }

    @Test
    public void tesNotContainMatch() throws Exception {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"resources\": {\n" +
                "        \"$ncontains\": \"00010\" \n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(event, HashMap.class);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }

    @Test
    public void testSimpleDirectMatch() throws Exception {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"source\": \"weather\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        System.out.println(rule);
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(event, HashMap.class);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }


    @Test
    public void testSimpleMultiattrMatch() throws Exception {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"source\": {\n" +
                "        \"$eq\": \"weather\"\n" +
                "      },\n" +
                "      \"scope\":{\n" +
                "        \"$eq\": \"Service\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        System.out.println(rule);
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(event, HashMap.class);

        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }


    @Test
    public void testSimpleMatch3() throws Exception {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"source\": {\n" +
                "        \"$eq\": \"weather\"\n" +
                "      },\n" +
                "      \"scope\": {\n" +
                "        \"$eq\": \"Service\"\n" +
                "      },\n" +
                "      \"status.state\": {\n" +
                "        \"$eq\": \"running\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        System.out.println(rule);
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(event, HashMap.class);

        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }

    @Test
    public void testAndIncludeOrMatch() throws Exception {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"source\": {\n" +
                "        \"$eq\": \"weather\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"$or\": [\n" +
                "        {\n" +
                "          \"scope\": {\n" +
                "            \"$eq\": \"Service\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"status.state\": {\n" +
                "            \"$eq\": \"running1\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        System.out.println(rule);
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(event, HashMap.class);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }


    @Test
    public void testOrIncludeAndMatch() throws Exception {
        String rule = "{\n" +
                "  \"$or\": [\n" +
                "    {\n" +
                "      \"source\": {\n" +
                "        \"$eq\": \"weather\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"$and\": [\n" +
                "        {\n" +
                "          \"scope\": {\n" +
                "            \"$eq\": \"Service\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"status.state\": {\n" +
                "            \"$eq\": \"running1\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        System.out.println(rule);
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(event, HashMap.class);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }


    @Test
    public void testMemberOfMatch() throws Exception {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"str\": {\n" +
                "        \"$memberOf\": [\n" +
                "          \"ab\",\n" +
                "          \"bc\",\n" +
                "          \"ef\"\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        Predicate<Object> pred = factory.read(rule.getBytes());
        Map<String, Object> map = new HashMap<>(2);
        map.put("str", "ab");
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }
    @Test
    public void testFlattening() {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"status.weatherText\": {\n" +
                "        \"$eq\": \"大雨啦\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        Map<String, Object> map = JsonFlattener.flattenAsMap(event);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }

    @Test
    public void testFlattening2() {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"status.weatherText\": {\n" +
                "        \"$eq\": \"大雨啦22\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        Map<String, Object> map = JsonFlattener.flattenAsMap(event);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertFalse(test);
    }

    @Test
    public void testFlattening3() {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"status.list[1].sceneId\": {\n" +
                "        \"$eq\": \"7860\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        Map<String, Object> map = JsonFlattener.flattenAsMap(event);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertTrue(test);
    }

    @Test
    public void testFlattening4() {
        String rule = "{\n" +
                "  \"$and\": [\n" +
                "    {\n" +
                "      \"status.list[1].sceneId\": {\n" +
                "        \"$eq\": \"78608\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        Map<String, Object> map = JsonFlattener.flattenAsMap(event);
        Predicate<Object> pred = factory.read(rule.getBytes());
        boolean test = pred.test(map);
        Assert.assertFalse(test);
    }

}
