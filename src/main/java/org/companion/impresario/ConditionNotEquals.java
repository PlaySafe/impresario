package org.companion.impresario;

import java.util.Map;
import java.util.Objects;

/**
 * Returns {@code true} if 2 parameters are consider not equals, otherwise {@code false}
 */
class ConditionNotEquals implements Condition {

    private final Function function1;
    private final Function function2;

    public ConditionNotEquals(ConditionDefinition definition) {
        this.function1 = Objects.requireNonNull(definition.getParameter1(), "No such parameter1 of ConditionNotEquals");
        this.function2 = Objects.requireNonNull(definition.getParameter2(), "No such parameter2 of ConditionNotEquals");
    }

    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        String result1 = function1.perform(input, definitions);
        String result2 = function2.perform(input, definitions);
        return !result1.equals(result2);
    }
}
