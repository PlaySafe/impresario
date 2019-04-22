package org.companion.impresario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Define the meta data compile the function for the builder.
 * this class store data from XML configuration.
 */
public class FunctionDefinition {

    private final String name;
    private final Condition preCondition;
    private final List<Function> preFunctions;
    private final String parameter1;
    private final String parameter2;

    private FunctionDefinition(Builder builder) {
        this.name = Objects.requireNonNull(builder.name);
        this.preCondition = builder.preCondition;
        this.preFunctions = Collections.unmodifiableList(new ArrayList<>(builder.preFunctions));
        this.parameter1 = builder.parameter1;
        this.parameter2 = builder.parameter2;
    }

    String getParameter1() {
        return parameter1;
    }

    String getParameter2() {
        return parameter2;
    }

    Condition getPreCondition() {
        return preCondition;
    }

    List<Function> getPreFunctions() {
        return preFunctions;
    }

    String getName() {
        return name;
    }

    static final class Builder {

        private String parameter1;
        private String parameter2;
        private Condition preCondition;
        private List<Function> preFunctions = new ArrayList<>();
        private String name;

        Builder setParameter1(String parameter1) {
            this.parameter1 = parameter1;
            return this;
        }

        Builder setParameter2(String parameter1) {
            this.parameter2 = parameter1;
            return this;
        }

        Builder setPreCondition(Condition preCondition) {
            this.preCondition = preCondition;
            return this;
        }

        Builder addPreFunction(Function preFunction) {
            this.preFunctions.add(preFunction);
            return this;
        }

        Builder setName(String name) {
            this.name = name;
            return this;
        }

        FunctionDefinition build() {
            return new FunctionDefinition(this);
        }
    }

}
