package org.companion.impresario;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>The negative index (-X): return last X character e.g 9876543 cut off -3 = 543</p><br/>
 * <p>The positive index (+X): return first X character e.g. 123456 cut off 2 = 12</p>
 */
class FunctionCutOff implements Function {

    private final Condition preCondition;
    private final Function textFunction;
    private final Function positionFunction;

    public FunctionCutOff(FunctionDefinition definition) {
        String parameterText = definition.getMetaParameters().getOrDefault(0, "");
        String parameterPosition = definition.getMetaParameters().getOrDefault(1, "");

        List<Function> textFunctions = definition.getPreFunctions().getOrDefault(parameterText, Collections.emptyList());
        if (textFunctions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousParameter(parameterText, getClass()));
        }
        this.textFunction = textFunctions.get(0);

        List<Function> positionFunctions = definition.getPreFunctions().getOrDefault(parameterPosition, Collections.emptyList());
        if (positionFunctions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousParameter(parameterPosition, getClass()));
        }
        this.positionFunction = positionFunctions.get(0);
        this.preCondition = definition.getPreCondition();
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionCutOff due to the pre-condition does not match");
        }
        int position = Integer.parseInt(positionFunction.perform(input, definitions), 10);
        String value = textFunction.perform(input, definitions);
        int length = value.length();
        return (position >= 0) ? value.substring(0, position) : value.substring(length + position, length);
    }
}
