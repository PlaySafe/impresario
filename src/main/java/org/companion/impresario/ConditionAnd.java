package org.companion.impresario;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Returns true if all conditions are {@code true}, otherwise {@code false}
 */
class ConditionAnd implements Condition {

    private final List<Condition> conditions;

    public ConditionAnd(ConditionDefinition definition) {
        this.conditions = Objects.requireNonNull(definition.getPreConditions(), "No such pre-conditions of ConditionAnd");
    }

    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        for (Condition condition : conditions) {
            if (!condition.matches(input, definitions)) {
                return false;
            }
        }
        return true;
    }
}
