package org.companion.impresario;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns value after replace all strings by the specific map of key-value.
 * The map of key-value could be specified by the field or definition name.
 * </p>
 * <p>
 * Be careful, this function can be the cause of performance issue if there are many keys to be replaced.
 * </p>
 */
class FunctionReplaceAll implements Function {

    private final Condition preCondition;
    private final Function textFunction;
    private final Function replaceValueFunction;

    public FunctionReplaceAll(FunctionDefinition definition) {
        String parameterText = definition.getMetaParameters().getOrDefault(0, "");
        String parameterReplaceValue = definition.getMetaParameters().getOrDefault(1, "");

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
        this.preCondition = definition.getPreCondition();
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute FunctionReplaceAll due to the pre-condition does not match");
        }

        String text = textFunction.perform(input, definitions);
        String values = replaceValueFunction.perform(input, definitions);

        if (VariableReflector.isField(values)) {
            return new ReplaceAll(text, values).perform(input, definitions);
        }
        else if (VariableReflector.isDefinition(values)) {
            return new ReplaceByDefinition(text, values).perform(input, definitions);
        }

        throw new IllegalArgumentException("FunctionReplaceAll requires either field or definition name");
    }

    private class ReplaceAll implements Function {

        private final Function replaceByMethod;
        private final Function replaceByMap;

        ReplaceAll(String text, String field) {
            this.replaceByMethod = new ReplaceAllByMethod(text, field);
            this.replaceByMap = new ReplaceAllByMap(text, field);
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
            return (input instanceof Map)
                    ? replaceByMap.perform(input, definitions)
                    : replaceByMethod.perform(input, definitions);
        }
    }

    private class ReplaceAllByMap implements Function {

        private final String text;
        private final String key;

        ReplaceAllByMap(String text, String field) {
            this.text = text;
            this.key = VariableReflector.reflectFieldOf(field);
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
            Map<String, ?> data = ((Map<String, Map<String, ?>>) input).get(key);
            if (data == null) {
                return text;
            }
            String result = text;
            for (Map.Entry<String, ?> each : data.entrySet()) {
                result = result.replace(each.getKey(), String.valueOf(each.getValue()));
            }
            return result;
        }
    }

    private class ReplaceAllByMethod implements Function {

        private final String text;
        private final String methodName;

        ReplaceAllByMethod(String text, String field) {
            this.text = text;
            this.methodName = VariableReflector.reflectFieldMethodOf(field);
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
            Object keyValues = VariableReflector.invoke(input, methodName);
            if (keyValues instanceof Map) {
                String result = text;
                Map<String, Object> replaceKeyValues = ((Map<String, Object>) keyValues);
                for (Map.Entry<String, Object> replaceKeyValue : replaceKeyValues.entrySet()) {
                    result = result.replace(replaceKeyValue.getKey(), String.valueOf(replaceKeyValue.getValue()));
                }
                return result;
            }
            throw new IllegalArgumentException("Expect a Map<String, Object> from method " + methodName);
        }
    }

    private class ReplaceByDefinition implements Function {

        private final String text;
        private final String definitionName;

        private ReplaceByDefinition(String text, String defConfig) {
            this.text = text;
            this.definitionName = VariableReflector.reflectDefinitionNameOf(defConfig);
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
            Map<String, Object> replaceItems = definitions.get(definitionName);
            if (replaceItems == null) {
                throw new NoSuchDefinitionException("Cannot find the definition " + definitionName);
            }
            String result = text;
            for (Map.Entry<String, Object> replaceItem : replaceItems.entrySet()) {
                result = result.replace(replaceItem.getKey(), String.valueOf(replaceItem.getValue()));
            }
            return result;
        }
    }


}
