package org.wmc.integrated.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
public class ASCIISort {

    private String sort(Map<String, String> map, String keys) {
        String sign = "";
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<>(map.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            infoIds.sort(Comparator.comparing(Map.Entry::getKey));
            // 构造签名键值对的格式
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (item.getKey() != null || item.getKey() != "") {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (!(val == "" || val == null)) {
                        sb.append(key + "=" + val + "&");
                    }
                }
            }
            String msg = sb.substring(0, sb.length() - 1) + keys;//sb.substring(0,sb.length()-1).toString()：截取最后一个&
            //String result = (sb.toString().length()-1)+keys;
            log.info("================accsii排序===============" + msg);
        } catch (Exception e) {
            return null;
        }
        return sign;
    }

    public static String sort1(SortedMap<String, String> map, String key) {
        if (!StringUtils.hasText(key)) {
            throw new RuntimeException("签名key不能为空");
        }
        Set<Map.Entry<String, String>> entries = map.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        List<String> values = Lists.newArrayList();

        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String k = String.valueOf(entry.getKey());
            String v = String.valueOf(entry.getValue());
            if (StringUtils.hasText(v)) {
                values.add(k + "=" + v);
            }
        }
        values.add("key=" + key);
        String sign = StringUtils.collectionToDelimitedString(values, "&");
        return sign;
    }

    public static String sort2(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        List<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys);//排序。
        for (String k : keys) {
            String v = map.get(k);
            if (StringUtils.hasText(v)) {
                sb.append(v);
            }
        }
        return sb.toString();
    }
}
