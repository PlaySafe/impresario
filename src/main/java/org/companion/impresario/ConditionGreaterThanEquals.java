package org.companion.impresario;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Returns {@code true} if parameter1 >= parameter2, otherwise {@code false}.
 * Using this class with non-Arithmetic can raise Exception
 */
class ConditionGreaterThanEquals implements Condition {

    private final List<Condition> preConditions;
    private final Function function1;
    private final Function function2;

    public ConditionGreaterThanEquals(ConditionDefinition definition) {
        Objects.requireNonNull(definition);
        this.preConditions = definition.getPreConditions();
        this.function1 = definition.getValue1();
        this.function2 = definition.getValue2();
    }

    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        for (Condition preCondition : preConditions) {
            if (!preCondition.matches(input, definitions)) {
                throw new ConditionNotMatchException("Cannot execute 'greater than or equals' due to the pre-condition does not match");
            }
        }
        String resultFunction1 = function1.perform(input, definitions);
        String resultFunction2 = function2.perform(input, definitions);
        BigDecimal result1 = new BigDecimal(resultFunction1);
        BigDecimal result2 = new BigDecimal(resultFunction2);
        return result1.subtract(result2).signum() >= 0;
    }
}
