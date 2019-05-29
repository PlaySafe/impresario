package org.companion.impresario;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Returns the opposite result of a condition
 */
class ConditionNot implements Condition {

    private final Condition condition;

    public ConditionNot(ConditionDefinition definition) {
        List<Condition> conditions = Objects.requireNonNull(definition.getPreConditions());
        if (conditions.size() == 1) {
            this.condition = conditions.get(0);
        }
        else {
            throw new IllegalArgumentException("Ambiguous pre-condition for 'not': Allow only 1 pre-condition");
        }
    }

    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        return !condition.matches(input, definitions);
    }
}
