package org.companion.impresario;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns a decimal after divide all values together regardless the precision digit
 * </p>
 */
class FunctionDivision implements Function {

    private final Condition preCondition;
    private final Function numberFunction;
    private final List<Function> divisorFunctions;

    public FunctionDivision(FunctionDefinition definition) {
        String parameterNumber = definition.getMetaParameters().getOrDefault(0, "");
        String parameterDivisor = definition.getMetaParameters().getOrDefault(1, "");
        List<Function> numberFunctions = definition.getPreFunctions().getOrDefault(parameterNumber, Collections.emptyList());
        if (numberFunctions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousParameter(parameterNumber, getClass()));
        }
        this.numberFunction = numberFunctions.get(0);

        List<Function> divisorFunctions = definition.getPreFunctions().getOrDefault(parameterDivisor, Collections.emptyList());
        if (divisorFunctions.isEmpty()) {
            throw new InvalidConfigurationException("FunctionDivision requires at least 1 function of " + parameterDivisor);
        }
        this.divisorFunctions = divisorFunctions;
        this.preCondition = definition.getPreCondition();
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionDivision due to the pre-condition does not match");
        }
        BigDecimal result = new BigDecimal(numberFunction.perform(input, definitions));
        for (Function divisorFunction : divisorFunctions) {
            String value = divisorFunction.perform(input, definitions);
            result = result.divide(new BigDecimal(value), MathContext.DECIMAL64);
        }
        return result.toString();
    }
}
