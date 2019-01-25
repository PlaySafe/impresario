package org.companion.impresario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Define the meta data compile the function for the builder.
 * this class store data from XML configuration.
 */
public class FunctionDefinition {

    private final String parameter1;
    private final String parameter2;
    private final Condition preCondition;
    private final List<Function> preFunctions;
    private final String logic;

    private FunctionDefinition(Builder builder) {
        this.parameter1 = builder.parameter1;
        this.parameter2 = builder.parameter2;
        this.preCondition = builder.preCondition;
        this.preFunctions = Collections.unmodifiableList(new ArrayList<>(builder.preFunctions));
        this.logic = builder.logic;
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

    String getLogic() {
        return logic;
    }

    static final class Builder {

        private String parameter1;
        private String parameter2;
        private Condition preCondition;
        private List<Function> preFunctions = new ArrayList<>();
        private String logic;

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

        Builder setLogic(String logic) {
            this.logic = logic;
            return this;
        }

        FunctionDefinition build() {
            return new FunctionDefinition(this);
        }
    }

}
