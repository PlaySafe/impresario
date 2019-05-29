package org.companion.impresario;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * Returns a decimal after modulo 2 values together regardless the precision digit
 * </p>
 */
class FunctionModulo implements Function {

    private Condition preCondition;
    private List<Function> preFunctions;

    public FunctionModulo(FunctionDefinition definition) {
        this.preCondition = definition.getPreCondition();
        List<Function> preFunctions = Objects.requireNonNull(definition.getPreFunctions());
        if (preFunctions.size() != 2) {
            throw new IllegalArgumentException("FunctionModulo allow only 2 pre-function");
        }
        this.preFunctions = preFunctions;
    }


    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionModulo due to the pre-condition does not match");
        }
        BigDecimal decimal1 = new BigDecimal(preFunctions.get(0).perform(input, definitions));
        BigDecimal modulo = new BigDecimal(preFunctions.get(1).perform(input, definitions));
        return decimal1.remainder(modulo).toString();
    }
}
