package org.companion.impresario;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>the negative index (-x): return since first character until the last x character exclude the last character e.g 9876543 substring -3 = 9876</p><br/>
 * <p>the positive index (+x): return since character x to the last character e.g. 123456 substring 2 = 3456</p>
 */
class FunctionSubstring implements Function {

    private final Condition preCondition;
    private final Function textFunction;
    private final Function positionFunction;

    public FunctionSubstring(FunctionDefinition definition) {
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
            throw new ConditionNotMatchException("Cannot execute FunctionSubstring due to the pre-condition does not match");
        }
        String text = textFunction.perform(input, definitions);
        int position = Integer.parseInt(positionFunction.perform(input, definitions), 10);
        int length = text.length();
        return (position > 0) ? text.substring(position, length) : text.substring(0, length + position);
    }
}
