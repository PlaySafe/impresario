package org.companion.impresario;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns a character at the specific index. The positive returns character from front, and negative from the back.
 * For example the input is "Hello World"
 * </p>
 * <ul>
 * <li>The index of 0 is 'H'</li>
 * <li>The index of 4 is 'o'</li>
 * <li>The index of -3 is 'r'</li>
 * </ul>
 *
 * <p>
 * This function requires 2 parameters in the metadata. The first parameter refers to the text.
 * The second parameter refers to the position index.
 * </p>
 */
class FunctionCharAt implements Function {

    private final Condition preCondition;
    private final Function textFunction;
    private final Function positionFunction;

    public FunctionCharAt(FunctionDefinition definition) {
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
            throw new ConditionNotMatchException("Cannot execute FunctionCharAt due to the pre-condition does not match");
        }
        int position = Integer.parseInt(positionFunction.perform(input, definitions), 10);
        String value = textFunction.perform(input, definitions);
        char c = (position >= 0) ? value.charAt(position) : value.charAt(value.length() + position);
        return String.valueOf(c);
    }
}
