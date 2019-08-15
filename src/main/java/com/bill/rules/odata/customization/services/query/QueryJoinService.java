package com.bill.rules.odata.customization.services.query;

import com.bill.rules.odata.customization.ODataQueryExpression;
import com.bill.rules.odata.customization.services.Rule;
import com.bill.rules.odata.customization.util.JsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryJoinService implements Rule {
    /**
     * JOIN expression string as 'table1?join=table2 on table2[*].label1 = table1[*].label1&count'
     *
     * @param originjsonPaths
     * @param expression
     * @param prefix
     * @param expressionStrs
     * @return
     */
    @Override
    public Map<String, String> apply(
            Map<String, String> originjsonPaths,
            ODataQueryExpression expression,
            String prefix,
            List<String> expressionStrs) {

        Map<String, String> tempoResult = originjsonPaths;

        expressionStrs.stream().forEach(
                expresisonStr -> {
                    String anotherPrefix = expresisonStr.split(" on ")[0].trim();
                    String currentPrefixLabel =
                                    expresisonStr.split(" on ")[1].split(" = ")[1].trim();
                    String anotherPrefixLabel =
                                    expresisonStr.split(" on ")[1].split(" = ")[0].trim();
                    bindAnotherPrefixListOnCurrentPrefixlabel(prefix, currentPrefixLabel, tempoResult, anotherPrefix, anotherPrefixLabel);
                }
        );
        return tempoResult;
    }

    private Map<String, String> getAnotherPrefixListByLabelAndValue(Map<String, String> originjsonPaths, String anotherPrefix, String anotherPrefixLabel, String value) {
        Object anotherPrefixMap = JsonUtil.convertJsonPathToMap(JsonUtil.getListByPrefix(originjsonPaths, anotherPrefix));
        Map result = new HashMap();
        result.put(anotherPrefix, ((List) (((Map<String, List>) anotherPrefixMap).get(anotherPrefix))).stream().filter(
                item -> {
                    for (Map.Entry<String, String> entry : JsonUtil.convertMapToJsonPath(item).entrySet()) {
                        String prefixListlabel = "$." + anotherPrefixLabel.replace(anotherPrefix + "[*].", "");
                        if (JsonUtil.replaceArrayIndexWithStar(prefixListlabel, entry.getKey()).indexOf(prefixListlabel) > -1
                                && value.equals(entry.getValue())) {
                            return true;
                        }
                    }
                    return false;

                }
        ).collect(Collectors.toList()));

        return JsonUtil.convertMapToJsonPath(result);
    }

    private void bindAnotherPrefixListOnCurrentPrefixlabel(String currentPrefix, String currentPrefixLabel, Map<String, String> originjsonPaths, String anotherPrefix, String anotherPrefixLabel) {
        Object currentPrefixMap = JsonUtil.convertJsonPathToMap(JsonUtil.getListByPrefix(originjsonPaths, currentPrefix));
        int index = 0;
        for (Object item : (((Map<String, List>) currentPrefixMap).get(currentPrefix))) {
            for (Map.Entry<String, String> entry : JsonUtil.convertMapToJsonPath(item).entrySet()) {
                String prefixListlabel = "$." + currentPrefixLabel.replace(currentPrefix + "[*].", "");
                if (JsonUtil.replaceArrayIndexWithStar(prefixListlabel, entry.getKey()).indexOf(prefixListlabel) > -1) {
                    Map<String, String> anotherPrefixListMatched = getAnotherPrefixListByLabelAndValue(originjsonPaths, anotherPrefix, anotherPrefixLabel, entry.getValue());
                    for(Map.Entry<String, String> entryInner: anotherPrefixListMatched.entrySet()){
                        originjsonPaths.put(entry.getKey().replace("$.","$."+currentPrefix+"["+(index)+"].") + "_" + anotherPrefix + "." + entryInner.getKey().replace("$.",""), entryInner.getValue());
                    }
                }
            }
            index++;
        }
    }


}
