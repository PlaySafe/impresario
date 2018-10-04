package org.companion.impresario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The general definition of all conditions for {@link ConditionBuilder}.
 * Its property will corresponds to the configuration. This class is used to store all configuration data regardless
 * the real condition type.
 *
 * @see ConditionBuilder
 */
class ConditionDefinition {

    private final String logic;
    private final List<Condition> preConditions;
    private final Function value1;
    private final Function value2;

    private ConditionDefinition(Builder builder) {
        this.logic = builder.logic;
        this.preConditions = Collections.unmodifiableList(new ArrayList<>(builder.preConditions));
        this.value1 = builder.value1;
        this.value2 = builder.value2;
    }

    String getLogic() {
        return logic;
    }

    List<Condition> getPreConditions() {
        return preConditions;
    }

    Function getValue1() {
        return value1;
    }

    Function getValue2() {
        return value2;
    }

    static final class Builder {

        private String logic;
        private List<Condition> preConditions = new ArrayList<>();
        private Function value1;
        private Function value2;

        Builder setLogic(String logic) {
            this.logic = logic;
            return this;
        }

        Builder addPreCondition(Condition preCondition) {
            this.preConditions.add(preCondition);
            return this;
        }

        Builder setValue1(Function value1) {
            this.value1 = value1;
            return this;
        }

        Builder setValue2(Function value2) {
            this.value2 = value2;
            return this;
        }

        boolean hasValue1() {
            return this.value1 != null;
        }

        boolean hasValue2() {
            return this.value2 != null;
        }

        ConditionDefinition build() {
            return new ConditionDefinition(this);
        }

    }
}
