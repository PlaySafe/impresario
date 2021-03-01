package org.companion.impresario;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns {@code true} if one of all conditions is true, otherwise {@code false}
 * </p>
 */
class ConditionOr implements Condition {

    private final List<Condition> preConditions;

    public ConditionOr(ConditionDefinition definition) {
        String parameterName = definition.getMetaParameters().getOrDefault(0, "");
        List<Condition> preConditions = definition.getPreConditions().getOrDefault(parameterName, Collections.emptyList());
        if (preConditions.size() < 2) {
            throw new InvalidConfigurationException("ConditionOr requires at least 2 pre-conditions");
        }
        this.preConditions = preConditions;
    }

    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        for (Condition condition : preConditions) {
            if (condition.matches(input, definitions)) {
                return true;
            }
        }
        return false;
    }
}
