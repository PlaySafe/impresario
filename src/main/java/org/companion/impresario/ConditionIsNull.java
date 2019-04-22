package org.companion.impresario;

import java.util.Map;
import java.util.Objects;

/**
 * Returns {@code true} if parameter1 is null, otherwise {@code false}
 */
class ConditionIsNull implements Condition {

    private final Function function1;

    public ConditionIsNull(ConditionDefinition definition) {
        this.function1 = Objects.requireNonNull(definition.getParameter1(), "No such parameter1");
    }

    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        String result1 = function1.perform(input, definitions);
        return result1 == null;
    }
}
