package org.companion.impresario;

import java.util.Map;

/**
 * Returns {@code true} if 2 parameters are consider equals, otherwise {@code false}
 */
class ConditionEquals implements Condition {

    private final Function function1;
    private final Function function2;

    public ConditionEquals(ConditionDefinition definition) {
        this.function1 = definition.getValue1();
        this.function2 = definition.getValue2();
    }

    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        String result1 = function1.perform(input, definitions);
        String result2 = function2.perform(input, definitions);
        return result1.equals(result2);
    }
}
