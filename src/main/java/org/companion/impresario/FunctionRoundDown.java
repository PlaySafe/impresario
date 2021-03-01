package org.companion.impresario;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns a decimal after rounding down a value after next specific precision digit
 * </p>
 */
class FunctionRoundDown implements Function {

    private final Condition preCondition;
    private final Function valueFunction;
    private final Function scaleFunction;

    public FunctionRoundDown(FunctionDefinition definition) {
        String valueParameterName = definition.getMetaParameters().getOrDefault(0, "");
        String scaleParameterName = definition.getMetaParameters().getOrDefault(1, "");
        List<Function> valueFunctions = definition.getPreFunctions().getOrDefault(valueParameterName, Collections.emptyList());
        if (valueFunctions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousParameter(valueParameterName, getClass()));
        }
        this.valueFunction = valueFunctions.get(0);

        List<Function> scaleFunctions = definition.getPreFunctions().getOrDefault(scaleParameterName, Collections.emptyList());
        if (scaleFunctions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousParameter(scaleParameterName, getClass()));
        }
        this.scaleFunction = scaleFunctions.get(0);
        this.preCondition = definition.getPreCondition();
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionRoundDown due to the pre-condition does not match");
        }
        BigDecimal result = new BigDecimal(valueFunction.perform(input, definitions));
        int scale = Integer.parseInt(scaleFunction.perform(input, definitions), 10);
        return result.setScale(scale, RoundingMode.DOWN).toString();
    }
}
