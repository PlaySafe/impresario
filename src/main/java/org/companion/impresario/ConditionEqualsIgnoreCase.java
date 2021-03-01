package org.companion.impresario;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns {@code true} if 2 parameters are consider equals regardless of lowercase or capital letter),
 * otherwise {@code false}
 * </p>
 */
class ConditionEqualsIgnoreCase implements Condition {

    private final Function function1;
    private final Function function2;

    public ConditionEqualsIgnoreCase(ConditionDefinition definition) {
        String parameter1 = definition.getMetaParameters().getOrDefault(0, "");
        String parameter2 = definition.getMetaParameters().getOrDefault(1, "");

        List<Function> functions1 = definition.getPreFunctions().getOrDefault(parameter1, Collections.emptyList());
        if (functions1.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousParameter(parameter1, getClass()));
        }
        this.function1 = functions1.get(0);

        List<Function> functions2 = definition.getPreFunctions().getOrDefault(parameter2, Collections.emptyList());
        if (functions2.size() != 1) {
            throw new InvalidConfigurationException(ErrorMessageBuilder.ambiguousParameter(parameter2, getClass()));
        }
        this.function2 = functions2.get(0);
    }

    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        String result1 = function1.perform(input, definitions);
        String result2 = function2.perform(input, definitions);
        return result1.equalsIgnoreCase(result2);
    }
}
