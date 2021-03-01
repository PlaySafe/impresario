package org.companion.impresario;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns a decimal after multiply all values together regardless the precision digit
 * </p>
 */
class FunctionMultiplication implements Function {

    private final Condition preCondition;
    private final List<Function> preFunctions;

    public FunctionMultiplication(FunctionDefinition definition) {
        String parameterName = definition.getMetaParameters().getOrDefault(0, "");
        List<Function> preFunctions = definition.getPreFunctions().getOrDefault(parameterName, Collections.emptyList());
        if (preFunctions.size() < 2) {
            throw new InvalidConfigurationException("FunctionMultiplication requires at least 2 pre-functions");
        }
        this.preCondition = definition.getPreCondition();
        this.preFunctions = preFunctions;
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionMultiplication due to the pre-condition does not match");
        }
        BigDecimal result = new BigDecimal(preFunctions.get(0).perform(input, definitions));
        for (int i = 1; i < preFunctions.size(); i++) {
            String value = preFunctions.get(i).perform(input, definitions);
            result = result.multiply(new BigDecimal(value));
        }
        return result.toString();
    }
}
