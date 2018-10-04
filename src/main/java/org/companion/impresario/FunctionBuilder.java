package org.companion.impresario;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class FunctionBuilder {

    private final Map<String, Class<? extends Function>> functionLogicMap;

    FunctionBuilder(Map<String, Class<? extends Function>> functionLogicMap) {
        this.functionLogicMap = Collections.unmodifiableMap(new HashMap<>(functionLogicMap));
    }

    Function build(FunctionDefinition definition) {
        String logic = definition.getLogic();
        Class<? extends Function> functionClass = functionLogicMap.get(logic);
        if (functionClass == null) {
            throw new IllegalArgumentException("There is no function : " + logic);
        }
        try {
            return functionClass.getConstructor(definition.getClass()).newInstance(definition);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
