package org.companion.impresario;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * <p>
 * Returns value after concatenate each string from all functions with delimiter
 * </p>
 */
class FunctionJoin implements Function {

    private final Condition preCondition;
    private final Function delimiterFunction;
    private final List<Function> textFunctions;

    public FunctionJoin(FunctionDefinition definition) {
        String parameterDelimiter = definition.getMetaParameters().getOrDefault(0, "");
        String parameterValue = definition.getMetaParameters().getOrDefault(1, "");

        List<Function> delimiterFunctions = definition.getPreFunctions().getOrDefault(parameterDelimiter, Collections.emptyList());
        if (delimiterFunctions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousParameter(parameterDelimiter, getClass()));
        }
        this.delimiterFunction = delimiterFunctions.get(0);

        List<Function> textFunctions = definition.getPreFunctions().getOrDefault(parameterValue, Collections.emptyList());
        if (textFunctions.size() < 2) {
            throw new InvalidConfigurationException("FunctionJoin requires at least 2 functions of " + parameterValue);
        }
        this.textFunctions = textFunctions;
        this.preCondition = definition.getPreCondition();
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionJoin due to the pre-condition does not match");
        }
        String delimiter = delimiterFunction.perform(input, definitions);
        StringJoiner stringJoiner = new StringJoiner(delimiter);
        for (Function function : textFunctions) {
            stringJoiner.add(function.perform(input, definitions));
        }
        return stringJoiner.toString();
    }
}
