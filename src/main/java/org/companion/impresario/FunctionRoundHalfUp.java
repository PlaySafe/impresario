package org.companion.impresario;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * Returns a decimal after rounding half up a value after next specific precision digit
 * </p>
 */
class FunctionRoundHalfUp implements Function {

    private final Condition preCondition;
    private final Function preFunction;
    private final int scale;

    public FunctionRoundHalfUp(FunctionDefinition definition) {
        this.preCondition = definition.getPreCondition();
        this.scale = Integer.parseInt(Objects.requireNonNull(definition.getParameter1()));
        List<Function> preFunctions = Objects.requireNonNull(definition.getPreFunctions());
        if (preFunctions.size() != 1) {
            throw new IllegalArgumentException("Ambiguous pre-function of FunctionRoundHalfUp: Allow only 1 pre-function");
        }
        this.preFunction = preFunctions.get(0);
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionRoundHalfUp due to the pre-condition does not match");
        }
        BigDecimal result = new BigDecimal(preFunction.perform(input, definitions));
        return result.setScale(scale, RoundingMode.HALF_UP).toString();
    }
}