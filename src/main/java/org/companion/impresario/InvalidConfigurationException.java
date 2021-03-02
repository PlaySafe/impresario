package org.companion.impresario;

/**
 * <p>
 * This exception will be thrown during the compilation of invalid xml configuration.
 * It's a good practice to throw this exception from the constructor of any custom Function and/or Condition if
 * something went wrong in order to stop the compilation process.
 * </p>
 *
 * <pre>
 * class MyCustomFunction implements Function {
 *
 *     public MyCustomFunction(FunctionDefinition definition) {
 *         if ( something went wrong ) {
 *             throw new InvalidConfigurationException("Some error message");
 *         }
 *     }
 *
 *     &#64;Override
 *     public String perform(Object input, Map&lt;String, Map&lt;String, Object&gt;&gt; definitions) throws ConditionNotMatchException {
 *         // Do something
 *     }
 * }
 * </pre>
 *
 * <pre>
 * class MyCustomCondition implements Condition {
 *
 *     public MyCustomCondition(ConditionDefinition definition) {
 *         if ( something went wrong ) {
 *             throw new InvalidConfigurationException("Some error message");
 *         }
 *     }
 *
 *     &#64;Override
 *     public boolean matches(Object input, Map&lt;String, Map&lt;String, Object&gt;&gt; definitions) throws ConditionNotMatchException {
 *         // Do something
 *     }
 * }
 * </pre>
 */
public class InvalidConfigurationException extends RuntimeException {

    public InvalidConfigurationException(String message) {
        super(message);
    }

    public InvalidConfigurationException(Throwable cause) {
        super(cause);
    }

    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
