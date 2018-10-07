package org.companion.impresario;

import java.util.List;
import java.util.Map;

/**
 * Replace specific strings by the specific strings of definition
 */
class FunctionReplace implements Function {

    private final Function preFunction;
    private final Condition preCondition;
    private final String definitionName;
    private final String definitionKey;

    public FunctionReplace(FunctionDefinition definition) {
        this.preCondition = definition.getPreCondition();
        List<Function> preFunctions = definition.getPreFunctions();
        if (preFunctions.size() == 1) {
            this.preFunction = preFunctions.get(0);
        }
        else {
            throw new IllegalArgumentException("Ambiguous pre-function for 'replace': Allow only 1 pre-function");
        }

        String definitionName = VariableReflector.reflectDefinitionNameOf(definition.getParam());
        if (definitionName == null) {
            throw new IllegalArgumentException("There is no reference definition name");
        }
        this.definitionName = definitionName;
        this.definitionKey = VariableReflector.reflectDefinitionKeyOf(definition.getParam());
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        if (preCondition != null && !preCondition.matches(input, definitions)) {
            throw new ConditionNotMatchException("Cannot execute 'replace' due to the pre-condition does not match");
        }
        if (definitions == null) {
            throw new IllegalArgumentException("Cannot perform 'replace' due to missing 'definition'");
        }
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
