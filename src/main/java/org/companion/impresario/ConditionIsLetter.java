package org.companion.impresario;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns {@code true} if the whole strings has only letter, otherwise {@code false}
 * </p>
 */
class ConditionIsLetter implements Condition {

    private final Function function;

    public ConditionIsLetter(ConditionDefinition definition) {
        String parameter = definition.getMetaParameters().getOrDefault(0, "");
        List<Function> functions = definition.getPreFunctions().getOrDefault(parameter, Collections.emptyList());
        if (functions.isEmpty()) {
            throw new InvalidConfigurationException("No such pre-function of ConditionIsLetter or not define 'result-as' attribute");
        }
        if (functions.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousNumberOfPreFunction(1, functions.size(), getClass()));
        }
        this.function = definition.getPreFunctions().get(parameter).get(0);
    }

    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        String result1 = function.perform(input, definitions);
        boolean isLetter = result1.length() > 0;
        for (int i = 0; isLetter && i < result1.length(); i++) {
            isLetter &= Character.isLetter(result1.charAt(i));
        }
        return isLetter;
    }
}
