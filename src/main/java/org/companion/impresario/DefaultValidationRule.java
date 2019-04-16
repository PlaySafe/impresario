package org.companion.impresario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DefaultValidationRule implements ValidationRule {

    private List<Condition> conditions;
    private Map<String, Map<String, Object>> definitions;

    DefaultValidationRule(List<Condition> conditions, Map<String, Map<String, Object>> definitions) {
        this.conditions = Collections.unmodifiableList(new ArrayList<>(conditions));
        this.definitions = Collections.unmodifiableMap(new HashMap<>(definitions));
    }

    @Override
    public boolean validate(Object input) throws ConditionNotMatchException {
        if (conditions.isEmpty()) {
            return false;
        }
        for (Condition condition : conditions) {
            if (!condition.matches(input, definitions)) {
                return false;
            }
        }
        return true;
    }
}
