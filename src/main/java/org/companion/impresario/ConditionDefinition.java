package org.companion.impresario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The general definition of all conditions for {@link ConditionBuilder}.
 * Its property will corresponds to the configuration. This class is used to store all configuration data regardless
 * the real condition type.
 *
 * @see ConditionBuilder
 */
public class ConditionDefinition {

    private final String name;
    private final List<Condition> preConditions;
    private final Function parameter1;
    private final Function parameter2;

    private ConditionDefinition(Builder builder) {
        this.name = Objects.requireNonNull(builder.name);
        this.preConditions = Collections.unmodifiableList(new ArrayList<>(builder.preConditions));
        this.parameter1 = builder.parameter1;
        this.parameter2 = builder.parameter2;
    }

    String getName() {
        return name;
    }

    List<Condition> getPreConditions() {
        return preConditions;
    }

    Function getParameter1() {
        return parameter1;
    }

    Function getParameter2() {
        return parameter2;
    }

    static final class Builder {

        private String name;
        private List<Condition> preConditions = new ArrayList<>();
        private Function parameter1;
        private Function parameter2;

        Builder setName(String name) {
            this.name = name;
            return this;
        }

        Builder addPreCondition(Condition preCondition) {
            this.preConditions.add(preCondition);
            return this;
        }

        Builder setParameter1(Function param) {
            this.parameter1 = param;
            return this;
        }

        Builder setParameter2(Function param) {
            this.parameter2 = param;
            return this;
        }

        boolean hasParameter1() {
            return this.parameter1 != null;
        }

        boolean hasParameter2() {
            return this.parameter2 != null;
        }

        ConditionDefinition build() {
            return new ConditionDefinition(this);
        }

    }
}
