package org.companion.impresario;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Returns {@code true} if parameter1 < parameter2, otherwise {@code false}.
 * Using this class with non-Arithmetic can raise Exception
 */
class ConditionLessThan implements Condition {

    private final Function function1;
    private final Function function2;

    public ConditionLessThan(ConditionDefinition definition) {
        this.function1 = Objects.requireNonNull(definition.getParameter1(), "No such parameter1 of ConditionLessThan");
        this.function2 = Objects.requireNonNull(definition.getParameter2(), "No such parameter2 of ConditionLessThan");
    }

    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        String resultFunction1 = function1.perform(input, definitions);
        String resultFunction2 = function2.perform(input, definitions);
        BigDecimal result1 = new BigDecimal(resultFunction1);
        BigDecimal result2 = new BigDecimal(resultFunction2);
        return result1.subtract(result2).signum() < 0;
    }
}
