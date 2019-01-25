package org.companion.impresario;

import java.util.List;
import java.util.Map;
import java.util.Objects;


class FunctionReplace implements Function {

    private final Function delegateFunction;
    private final Condition preCondition;

    public FunctionReplace(FunctionDefinition definition) {
        this.preCondition = definition.getPreCondition();
        List<Function> preFunctions = definition.getPreFunctions();

        String parameter1 = definition.getParameter1();
        if (parameter1 == null) {
            throw new IllegalArgumentException("Invalid 'param' configuration");
        }
        else if (VariableReflector.isDefinition(parameter1)) {
            delegateFunction = new FunctionReplaceDefinition(preFunctions.get(0), parameter1);
        }
        else {
            delegateFunction = new FunctionReplaceByVariable(preFunctions.get(0), preFunctions.get(1), parameter1);
        }
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute 'replace' due to the pre-condition does not match");
        }
        return delegateFunction.perform(input, definitions);
    }

    /**
     * Replace specific strings by the specific strings of definition.
     * There are 2 possible configuration.
     * <ol>
     * <li>Definition without key specific #{DefinitionName}</li>
     * <li>Definition with key specific #{DefinitionName.Key}</li>
     * </ol>
     */
    private class FunctionReplaceDefinition implements Function {

        private final Function preFunction;
        private final String definitionName;
        private String definitionKey;

        FunctionReplaceDefinition(Function preFunction, String definitionConfig) {
            this.preFunction = preFunction;
            this.definitionName = VariableReflector.reflectDefinitionNameOf(definitionConfig);
            if (VariableReflector.isDefinitionWithSpecificKey(definitionConfig)) {
                this.definitionKey = VariableReflector.reflectDefinitionKeyOf(definitionConfig);
            }
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
            if (preCondition != null && !preCondition.matches(input, definitions)) {
                throw new ConditionNotMatchException("Cannot execute 'replace' due to the pre-condition does not match");
            }
            Objects.requireNonNull(definitions, "Cannot perform 'replace' due to missing 'definition'");
            String value = preFunction.perform(input, definitions);
            Map<String, Object> targetDefinition = definitions.get(definitionName);
            if (definitionKey != null) {
                String replaceTarget = definitionKey;
                String replaceValue = String.valueOf(targetDefinition.get(definitionKey));
                return replace(value, input, replaceTarget, replaceValue);
            }
            return replace(value, input, targetDefinition);
        }

        private String replace(String text, Object input, Map<String, Object> replaceMap) {
            String value = text;
            for (Map.Entry<String, Object> replaceItem : replaceMap.entrySet()) {
                value = replace(value, input, replaceItem.getKey(), replaceItem.getValue().toString());
            }
            return value;
        }

        private String replace(String inputText, Object input, String replaceTarget, String replaceValue) {
            String finalReplaceTarget = getFinalValueOf(input, replaceTarget);
            String finalReplaceValue = getFinalValueOf(input, replaceValue);
            return inputText.replace(finalReplaceTarget, finalReplaceValue);
        }

        private String getFinalValueOf(Object input, String target) {
            if (VariableReflector.isField(target)) {
                String methodName = VariableReflector.reflectFieldMethodOf(target);
                return VariableReflector.invoke(input, methodName);
            }
            return target;
        }
    }

    private class FunctionReplaceByVariable implements Function {

        private final Function originalText;
        private final Function replaceByText;
        private final String targetReplace;

        FunctionReplaceByVariable(Function originalText, Function replaceByText, String targetReplace) {
            this.originalText = originalText;
            this.replaceByText = replaceByText;
            this.targetReplace = targetReplace;
        }

        @Override
        public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
            String textToBeReplaced = targetReplace;
            if (VariableReflector.isField(targetReplace)) {
                if(input instanceof Map){
                    String mapKey = VariableReflector.reflectFieldOf(targetReplace);
                    Map<String, Object> userInput = (Map<String, Object>) input;
                    textToBeReplaced = String.valueOf(userInput.get(mapKey));
                }
                else {
                    String methodName = VariableReflector.reflectFieldMethodOf(targetReplace);
                    textToBeReplaced = VariableReflector.invoke(input, methodName);
                }
            }
            else if (VariableReflector.isProperties(targetReplace)) {
                textToBeReplaced = VariableReflector.reflectPropertiesOf(targetReplace);
            }
            String originalText = this.originalText.perform(input, definitions);
            String replaceByText = this.replaceByText.perform(input, definitions);
            return originalText.replace(textToBeReplaced, replaceByText);
        }
    }

}
