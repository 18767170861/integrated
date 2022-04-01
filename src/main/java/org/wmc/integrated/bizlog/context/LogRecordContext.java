package org.wmc.integrated.bizlog.context;

import java.util.HashMap;
import java.util.Map;

public class LogRecordContext {

    private static InheritableThreadLocal<Map<String, Object>> variableMap = new InheritableThreadLocal<>();

    static {
        variableMap.set(new HashMap<>());
    }

    public static void putVariable(String name, Object value) {
        variableMap.get().put(name, value);
    }

    public static Map<String, Object> getVariables() {
        return variableMap.get();
    }

    public static void clear() {
        variableMap.get().clear();
    }
}