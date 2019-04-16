package org.companion.impresario;

/**
 * Exception during conditional checking when the condition doesn't match.
 */
public class ConditionNotMatchException extends Exception {

    ConditionNotMatchException(String message) {
        super(message);
    }

}
