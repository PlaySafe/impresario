package org.companion.impresario;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * Returns a decimal after add all values together regardless the precision digit
 * </p>
 */
class FunctionAddition implements Function {

    private final Condition preCondition;
    private final List<Function> preFunctions;

    public FunctionAddition(FunctionDefinition definition) {
        this.preCondition = definition.getPreCondition();

        List<Function> preFunctions = Objects.requireNonNull(definition.getPreFunctions());
        if (preFunctions.size() < 2) {
            throw new IllegalArgumentException("FunctionAddition require at least 2 pre-function");
        }
        this.preFunctions = preFunctions;
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionAddition due to the pre-condition does not match");
        }
        BigDecimal result = new BigDecimal(preFunctions.get(0).perform(input, definitions));
        for (int i = 1; i < preFunctions.size(); i++) {
            String value = preFunctions.get(i).perform(input, definitions);
            result = result.add(new BigDecimal(value));
        }
        return result.toString();
    }
}
