package org.companion.impresario;

import java.util.Map;
import java.util.Objects;

/**
 * Returns {@code true} if 2 parameters are consider equals regardless of lowercase or capital letter),
 * otherwise {@code false}
 */
class ConditionEqualsIgnoreCase implements Condition {

    private final Function function1;
    private final Function function2;

    public ConditionEqualsIgnoreCase(ConditionDefinition definition) {
        this.function1 = Objects.requireNonNull(definition.getParameter1(), "No such parameter1 of ConditionEqualsIgnoreCase");
        this.function2 = Objects.requireNonNull(definition.getParameter2(), "No such parameter2 of ConditionEqualsIgnoreCase");
    }

    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        String result1 = function1.perform(input, definitions);
        String result2 = function2.perform(input, definitions);
        return result1.equalsIgnoreCase(result2);
    }
}
