package org.companion.impresario;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class DefaultLabelGenerator implements LabelGenerator {

    private final Condition preCondition;
    private final Function function;
    private final Map<String, Map<String, Object>> definitions;

    DefaultLabelGenerator(Condition preCondition, Function function, Map<String, Map<String, Object>> definitions) {
        Objects.requireNonNull(function);
        this.preCondition = preCondition;
        this.function = function;
        this.definitions = Collections.unmodifiableMap(new HashMap<>(definitions));
    }

    @Override
    public String labelOf(Object input) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot generate label because of pre-condition does not match");
        }
        return function.perform(input, definitions);
    }
}
