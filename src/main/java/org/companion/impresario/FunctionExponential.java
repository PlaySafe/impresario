package org.companion.impresario;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns a decimal after exponential 2 values together regardless the precision digit
 * </p>
 */
class FunctionExponential implements Function {

    private final Condition preCondition;
    private final Function baseFunction;
    private final List<Function> powerFunctions;

    public FunctionExponential(FunctionDefinition definition) {
        String parameterBase = definition.getMetaParameters().getOrDefault(0, "");
        String parameterPower = definition.getMetaParameters().getOrDefault(1, "");
        List<Function> baseFunctions = definition.getPreFunctions().getOrDefault(parameterBase, Collections.emptyList());
        if (baseFunctions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousParameter(parameterBase, getClass()));
        }
        this.baseFunction = baseFunctions.get(0);

        List<Function> powerFunctions = definition.getPreFunctions().getOrDefault(parameterPower, Collections.emptyList());
        if (powerFunctions.isEmpty()) {
            throw new InvalidConfigurationException("FunctionExponential requires at least 1 function of " + parameterPower);
        }
        this.powerFunctions = powerFunctions;
        this.preCondition = definition.getPreCondition();
    }


    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionExponential due to the pre-condition does not match");
        }
        BigDecimal result = new BigDecimal(baseFunction.perform(input, definitions));
        for (Function powerFunction : powerFunctions) {
            int expo = Integer.parseInt(powerFunction.perform(input, definitions), 10);
            result = result.pow(expo);
        }
        return result.toString();
    }
}
