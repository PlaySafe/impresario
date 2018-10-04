package org.companion.impresario;

import java.util.List;
import java.util.Map;

/**
 * For all conditions matches will consider as matches, otherwise not matches
 */
class ConditionAnd implements Condition {

    private final List<Condition> conditions;

    public ConditionAnd(ConditionDefinition definition) {
        this.conditions = definition.getPreConditions();
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
