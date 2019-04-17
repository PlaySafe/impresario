package org.companion.impresario;

/**
 * Exception during function execution when the conditions to execute function do not match
 */
public class ConditionNotMatchException extends Exception {

    public ConditionNotMatchException(String message) {
        super(message);
    }

}
