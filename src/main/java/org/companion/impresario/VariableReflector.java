package org.companion.impresario;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Provides functions to retrieve all acceptable configuration values including fields, definition, and properties.
 * </p>
 * <ul>
 * <li>The accept field value must be formatted by start with @ and wrapped by curly brackets like <b>@{fieldValue}</b>.</li>
 * <li>The accept properties value must be formatted by start with $ and wrapped by curly brackets like <b>${property}</b></li>
 * <li>The accept definition value could be formatted by start with # and wrapped definition name by curly brackets like
 * <b>#{definitionName}</b>, or specify the key of definition separated by period like <b>#{definitionName.key}</b>
 * </li>
 * </ul>
 *
 * <p>
 * This class also provide the functionality to create a method name based on the field name {@link #reflectFieldMethodOf(String)}
 * </p>
 */
public class VariableReflector {

    private static final Pattern FIELD_PATTERN = Pattern.compile("@\\{(.*)\\}");
    private static final Pattern DEFINITION_PATTERN = Pattern.compile("^#\\{([^.]*)(?:\\.([^.]*))?\\}$");
    private static final Pattern PROPERTIES_PATTERN = Pattern.compile("\\$\\{(.*)\\}");

    private VariableReflector() {
    }

    /**
     * <p>
     * Creates a method name to get the data follow on the specified field name.
     * The text input <b>@{data}</b> will be result as <b>getData</b>.
     * User can use the result of the class to create the method object using java reflection.
     * </p>
     *
     * @param text the text of field configuration (in format)
     *
     * @return a method name based on field name.
     */
    public static final String reflectFieldMethodOf(String text) {
        String field = reflectFieldOf(text);
        StringBuilder stringBuilder = new StringBuilder("get");
        stringBuilder.append(Character.toUpperCase(field.charAt(0)));
        stringBuilder.append(field.substring(1));
        return stringBuilder.toString();
    }

    /**
     * @param text an arbitrary text
     *
     * @return <code>true</code> if text matches field format, otherwise <code>false</code>
     */
    public static final boolean isField(String text) {
        Matcher matcher = FIELD_PATTERN.matcher(text);
        return matcher.matches();
    }


    /**
     * <p>
     * Extracts the value of the field string configuration.
     * The text <b>@{name}</b> will result as <b>name</b>
     * </p>
     *
     * @param text the field configuration text
     *
     * @return the field value without any format
     *
     * @throws IllegalArgumentException if the text doesn't match the field format
     */
    public static final String reflectFieldOf(String text) {
        Matcher matcher = FIELD_PATTERN.matcher(text);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        throw new InvalidConfigurationException("The text '" + text + "' does not refer to the field");
    }

    /**
     * <p>
     * Extracts the value of the definition name configuration.
     * The text <b>#{defName.key}</b> will result as <b>defName</b>
     * </p>
     *
     * @param text the definition configuration text
     *
     * @return the definition name without any format
     *
     * @throws IllegalArgumentException if the text doesn't match the definition format
     */
    public static final String reflectDefinitionNameOf(String text) {
        Matcher matcher = DEFINITION_PATTERN.matcher(text);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        throw new InvalidConfigurationException("The text '" + text + "' does not refer to definition");
    }

    /**
     * <p>
     * Extracts the value of the definition key configuration.
     * The text <b>#{defName.key}</b> will result as <b>key</b>
     * </p>
     *
     * @param text the definition (with key) configuration text
     *
     * @return the definition key without any format
     *
     * @throws IllegalArgumentException if the text doesn't match the definition format
     */
    public static final String reflectDefinitionKeyOf(String text) {
        Matcher matcher = DEFINITION_PATTERN.matcher(text);
        if (matcher.matches()) {
            return matcher.group(2);
        }
        throw new InvalidConfigurationException("The text '" + text + "' does not refer to definition");
    }

    /**
     * @param text an arbitrary text
     *
     * @return <code>true</code> if text matches definition (without key) format, otherwise <code>false</code>
     */
    public static final boolean isDefinition(String text) {
        Matcher matcher = DEFINITION_PATTERN.matcher(text);
        return matcher.matches();
    }

    /**
     * @param text an arbitrary text
     *
     * @return <code>true</code> if text matches definition (with key) format, otherwise <code>false</code>
     */
    public static final boolean isDefinitionWithSpecificKey(String text) {
        Matcher matcher = DEFINITION_PATTERN.matcher(text);
        if (matcher.matches()) {
            return matcher.group(2) != null;
        }
        return false;
    }

    /**
     * <p>
     * Extracts the value of the properties string configuration.
     * The text <b>${some.properties}</b> will result as <b>some.properties</b>
     * </p>
     *
     * @param text the property configuration text
     *
     * @return the property value without any format
     *
     * @throws IllegalArgumentException if the text doesn't match the properties format
     */
    public static final String reflectPropertiesOf(String text) {
        Matcher matcher = PROPERTIES_PATTERN.matcher(text);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        throw new InvalidConfigurationException("The text '" + text + "' does not refer to properties");
    }

    /**
     * @param text an arbitrary text
     *
     * @return <code>true</code> if text matches properties format, otherwise <code>false</code>
     */
    public static final boolean isProperties(String text) {
        Matcher matcher = PROPERTIES_PATTERN.matcher(text);
        return matcher.matches();
    }

    /**
     * Executes the method of input object. The specified method name will be used to trigger the input object
     * using java reflection. Therefore, some exceptions might be raise if something went wrong such as no such method,
     * or the method is not accessible.
     *
     * @param input      an arbitrary object
     * @param methodName the specific method name
     *
     * @return the result of executed method
     *
     * @throws IllegalArgumentException if no such specific method, or the method is not accessible.
     */
    public static final Object invoke(Object input, String methodName) {
        try {
            Method method = input.getClass().getMethod(methodName);
            return method.invoke(input);
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            String errorMsg = String.format("Cannot invoke method '%s.%s'", input.getClass().getName(), methodName);
            throw new NoSuchExecutableMethodException(errorMsg, e);
        }
    }
}
