package org.companion.impresario;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns a decimal after subtract all values together regardless the precision digit
 * </p>
 */
class FunctionSubtraction implements Function {

    private final Condition preCondition;
    private final Function numberFunction;
    private final List<Function> subtrahendFunctions;

    public FunctionSubtraction(FunctionDefinition definition) {
        String parameterNumber = definition.getMetaParameters().getOrDefault(0, "");
        String parameterSubtrahend = definition.getMetaParameters().getOrDefault(1, "");
        List<Function> numberFunctions = definition.getPreFunctions().getOrDefault(parameterNumber, Collections.emptyList());
        if (numberFunctions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousParameter(parameterNumber, getClass()));
        }
        this.numberFunction = numberFunctions.get(0);

        List<Function> subtrahendFunctions = definition.getPreFunctions().getOrDefault(parameterSubtrahend, Collections.emptyList());
        if (subtrahendFunctions.isEmpty()) {
            throw new InvalidConfigurationException("FunctionDivision requires at least 1 function of " + parameterSubtrahend);
        }
        this.subtrahendFunctions = subtrahendFunctions;
        this.preCondition = definition.getPreCondition();
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionSubtraction due to the pre-condition does not match");
        }
        BigDecimal result = new BigDecimal(numberFunction.perform(input, definitions));
        for (Function subtrahendFunction : subtrahendFunctions) {
            String value = subtrahendFunction.perform(input, definitions);
            result = result.subtract(new BigDecimal(value));
        }
        return result.toString();
    }
}
