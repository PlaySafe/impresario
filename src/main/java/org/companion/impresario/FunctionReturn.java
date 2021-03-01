package org.companion.impresario;

import java.util.Map;

/**
 * <p>
 * Returns a string value from the configuration directly without any process.
 * </p>
 */
class FunctionReturn implements Function {

    private final String value;
    private final Condition preCondition;

    FunctionReturn(String value) {
        this.value = value;
        preCondition = null;
    }

    public FunctionReturn(FunctionDefinition definition) {
        String parameterName = definition.getMetaParameters().getOrDefault(0, "");
        this.value = definition.getParameters().get(parameterName);
        this.preCondition = definition.getPreCondition();
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionGet due to the pre-condition does not match");
        }
        return value;
    }
}
