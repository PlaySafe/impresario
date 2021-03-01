package org.companion.impresario;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns {@code true} if parameter1 < parameter2, otherwise {@code false}.
 * Using this class with non-Arithmetic can raise Exception
 * </p>
 */
class ConditionLessThan implements Condition {

    private final Function function1;
    private final Function function2;

    public ConditionLessThan(ConditionDefinition definition) {
        String parameter1 = definition.getMetaParameters().getOrDefault(0, "");
        String parameter2 = definition.getMetaParameters().getOrDefault(1, "");

        List<Function> functions1 = definition.getPreFunctions().getOrDefault(parameter1, Collections.emptyList());
        if (functions1.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousParameter(parameter1, getClass()));
        }
        this.function1 = functions1.get(0);

        List<Function> functions2 = definition.getPreFunctions().getOrDefault(parameter2, Collections.emptyList());
        if (functions2.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousParameter(parameter2, getClass()));
        }
        this.function2 = functions2.get(0);
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
