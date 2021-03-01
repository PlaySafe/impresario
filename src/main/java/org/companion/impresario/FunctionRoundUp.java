package org.companion.impresario;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns a decimal after rounding up a value after next specific precision digit
 * </p>
 */
class FunctionRoundUp implements Function {

    private final Condition preCondition;
    private final Function valueFunction;
    private final Function scaleFunction;

    public FunctionRoundUp(FunctionDefinition definition) {
        String valueParameter = definition.getMetaParameters().getOrDefault(0, "");
        String scaleParameter = definition.getMetaParameters().getOrDefault(1, "");
        List<Function> valueFunctions = definition.getPreFunctions().getOrDefault(valueParameter, Collections.emptyList());
        if (valueFunctions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousParameter(valueParameter, getClass()));
        }
        this.valueFunction = valueFunctions.get(0);

        List<Function> scaleFunctions = definition.getPreFunctions().getOrDefault(scaleParameter, Collections.emptyList());
        if (scaleFunctions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousParameter(scaleParameter, getClass()));
        }
        this.scaleFunction = scaleFunctions.get(0);
        this.preCondition = definition.getPreCondition();
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionRoundUp due to the pre-condition does not match");
        }
        BigDecimal result = new BigDecimal(valueFunction.perform(input, definitions));
        int scale = Integer.parseInt(scaleFunction.perform(input, definitions), 10);
        return result.setScale(scale, RoundingMode.UP).toString();
    }
}
