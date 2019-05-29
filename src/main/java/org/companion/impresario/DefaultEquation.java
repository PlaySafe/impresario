package org.companion.impresario;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class DefaultEquation implements Equation {

    private final Condition preCondition;
    private final Function function;
    private final Map<String, Map<String, Object>> definitions;

    DefaultEquation(Condition preCondition, Function function, Map<String, Map<String, Object>> definitions) {
        this.preCondition = preCondition;
        this.function = Objects.requireNonNull(function);
        this.definitions = Collections.unmodifiableMap(new HashMap<>(definitions));
    }

    @Override
    public BigDecimal perform(Object input) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot solve equation because of pre-condition does not match");
        }
        return new BigDecimal(function.perform(input, definitions));
    }
}
