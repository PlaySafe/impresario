package org.companion.impresario;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>Returns the opposite result of its pre-condition</p>
 */
class ConditionNot implements Condition {

    private final Condition condition;

    public ConditionNot(ConditionDefinition definition) {
        String parameter = definition.getMetaParameters().getOrDefault(0, "");
        List<Condition> conditions = definition.getPreConditions().getOrDefault(parameter, Collections.emptyList());
        if (conditions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousNumberOfPreCondition(1, conditions.size(), getClass()));
        }
        this.condition = conditions.get(0);
    }

    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        return !condition.matches(input, definitions);
    }
}
