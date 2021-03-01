package org.companion.impresario;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns value after replace a specific string by a specific string
 * </p>
 */
class FunctionReplaceOne implements Function {

    private final Condition preCondition;
    private final Function textFunction;
    private final Function replaceValueFunction;
    private final Function replaceTargetFunction;

    public FunctionReplaceOne(FunctionDefinition definition) {
        String parameterText = definition.getMetaParameters().getOrDefault(0, "");
        String parameterReplaceValue = definition.getMetaParameters().getOrDefault(1, "");
        String parameterReplaceTarget = definition.getMetaParameters().getOrDefault(2, "");

        List<Function> textFunctions = definition.getPreFunctions().getOrDefault(parameterText, Collections.emptyList());
        if (textFunctions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousParameter(parameterText, getClass()));
        }
        this.textFunction = textFunctions.get(0);

        List<Function> replaceValueFunctions = definition.getPreFunctions().getOrDefault(parameterReplaceValue, Collections.emptyList());
        if (replaceValueFunctions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousParameter(parameterReplaceValue, getClass()));
        }
        this.replaceValueFunction = replaceValueFunctions.get(0);

        List<Function> replaceTargetFunctions = definition.getPreFunctions().getOrDefault(parameterReplaceTarget, Collections.emptyList());
        if (replaceTargetFunctions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousParameter(parameterReplaceTarget, getClass()));
        }
        this.replaceTargetFunction = replaceTargetFunctions.get(0);
        this.preCondition = definition.getPreCondition();
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionReplaceOne due to the pre-condition does not match");
        }

        String text = textFunction.perform(input, definitions);
        String replaceValue = replaceValueFunction.perform(input, definitions);
        String replaceTarget = replaceTargetFunction.perform(input, definitions);

        return text.replace(replaceTarget, replaceValue);
    }

}
