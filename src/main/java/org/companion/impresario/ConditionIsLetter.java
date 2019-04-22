package org.companion.impresario;

import java.util.Map;
import java.util.Objects;

/**
 * Returns {@code true} the whole strings has only letter, otherwise {@code false}
 */
class ConditionIsLetter implements Condition {

    private final Function function1;

    public ConditionIsLetter(ConditionDefinition definition) {
        this.function1 = Objects.requireNonNull(definition.getParameter1(), "No such parameter1");
    }

    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        String result1 = function1.perform(input, definitions);
        boolean isLetter = result1.length() > 0;
        for (int i = 0; isLetter && i < result1.length(); i++) {
            isLetter &= Character.isLetter(result1.charAt(i));
        }
        return isLetter;
    }
}
