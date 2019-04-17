package org.companion.impresario;

import java.util.List;
import java.util.Map;

/**
 * Returns {@code true} if one of all conditions is true, otherwise {@code false}
 */
class ConditionOr implements Condition {

    private final List<Condition> conditions;

    public ConditionOr(ConditionDefinition definition) {
        this.conditions = definition.getPreConditions();
    }

    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        for (Condition condition : conditions) {
            if (condition.matches(input, definitions)) {
                return true;
            }
        }
        return false;
    }
}
