package org.companion.impresario;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class DefaultMetaData implements MetaData {

    private final String tagDefinitionData;
    private final String attributeDefinitionName;
    private final String attributeDefinitionItemKey;
    private final String attributeDefinitionItemValue;

    private final String attributeFunctionName;
    private final String attributeFunctionParameter;

    private final String attributeConditionName;
    private final String attributeConditionParameter1;
    private final String attributeConditionParameter2;

    private Map<String, Class<? extends Function>> metaFunctions;
    private Map<String, Class<? extends Condition>> metaConditions;

    private DefaultMetaData(Builder builder) {
        this.tagDefinitionData = builder.tagDefinitionData;
        this.attributeDefinitionName = builder.attributeDefinitionName;
        this.attributeDefinitionItemKey = builder.attributeDefinitionItemKey;
        this.attributeDefinitionItemValue = builder.attributeDefinitionItemValue;

        this.attributeFunctionName = builder.attributeFunctionName;
        this.attributeFunctionParameter = builder.attributeFunctionParameter;

        this.attributeConditionName = builder.attributeConditionName;
        this.attributeConditionParameter1 = builder.attributeConditionParameter1;
        this.attributeConditionParameter2 = builder.attributeConditionParameter2;

        this.metaFunctions = Collections.unmodifiableMap(new HashMap<>(builder.metaFunctions));
        this.metaConditions = Collections.unmodifiableMap(new HashMap<>(builder.metaConditions));
    }

    @Override
    public String getDefinitionTagItem() {
        return tagDefinitionData;
    }

    @Override
    public String getAttributeDefinitionName() {
        return attributeDefinitionName;
    }

    @Override
    public String getAttributeDefinitionItemKey() {
        return attributeDefinitionItemKey;
    }

    @Override
    public String getAttributeDefinitionItemValue() {
        return attributeDefinitionItemValue;
    }

    @Override
    public String getAttributeFunctionName() {
        return attributeFunctionName;
    }

    @Override
    public String getAttributeFunctionParameter() {
        return attributeFunctionParameter;
    }

    @Override
    public String getAttributeConditionName() {
        return attributeConditionName;
    }

    @Override
    public String getAttributeConditionParameter1() {
        return attributeConditionParameter1;
    }

    @Override
    public String getAttributeConditionParameter2() {
        return attributeConditionParameter2;
    }

    @Override
    public Map<String, Class<? extends Function>> getMetaFunctions() {
        return metaFunctions;
    }

    @Override
    public Map<String, Class<? extends Condition>> getMetaConditions() {
        return metaConditions;
    }

    static final class Builder {

        private String tagDefinitionData;
        private String attributeDefinitionName;
        private String attributeDefinitionItemKey;
        private String attributeDefinitionItemValue;

        private String attributeFunctionName;
        private String attributeFunctionParameter;

        private String attributeConditionName;
        private String attributeConditionParameter1;
        private String attributeConditionParameter2;

        private Map<String, Class<? extends Function>> metaFunctions;
        private Map<String, Class<? extends Condition>> metaConditions;


        Builder() {
            this.metaFunctions = new HashMap<>();
            this.metaConditions = new HashMap<>();
        }

        Builder setDefinitionItemTag(String tagDefinitionData) {
            this.tagDefinitionData = tagDefinitionData;
            return this;
        }

        Builder setAttributeDefinitionName(String definitionName) {
            this.attributeDefinitionName = definitionName;
            return this;
        }

        Builder setAttributeDefinitionItemKey(String attributeItemKey) {
            this.attributeDefinitionItemKey = attributeItemKey;
            return this;
        }

        Builder setAttributeDefinitionItemValue(String attributeItemValue) {
            this.attributeDefinitionItemValue = attributeItemValue;
            return this;
        }

        Builder setAttributeFunctionName(String name) {
            this.attributeFunctionName = name;
            return this;
        }

        Builder setAttributeFunctionParameter(String parameter) {
            this.attributeFunctionParameter = parameter;
            return this;
        }

        Builder setAttributeConditionName(String name) {
            this.attributeConditionName = name;
            return this;
        }

        Builder setAttributeConditionParameter1(String parameter) {
            this.attributeConditionParameter1 = parameter;
            return this;
        }

        Builder setAttributeConditionParameter2(String parameter) {
            this.attributeConditionParameter2 = parameter;
            return this;
        }

        Builder setMetaFunction(Map<String, String> metaFunctions) throws ClassNotFoundException {
            for (Map.Entry<String, String> eachMeta : metaFunctions.entrySet()) {
                String className = eachMeta.getValue();
                Class<? extends Function> functionClass = (Class<? extends Function>) Class.forName(className);
                this.metaFunctions.put(eachMeta.getKey(), functionClass);
            }
            return this;
        }

        Builder setMetaCondition(Map<String, String> metaConditions) throws ClassNotFoundException {
            for (Map.Entry<String, String> eachMeta : metaConditions.entrySet()) {
                String className = eachMeta.getValue();
                Class<? extends Condition> functionClass = (Class<? extends Condition>) Class.forName(className);
                this.metaConditions.put(eachMeta.getKey(), functionClass);
            }
            return this;
        }

        MetaData build() {
            return new DefaultMetaData(this);
        }
    }
}
