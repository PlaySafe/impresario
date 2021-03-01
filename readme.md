# Welcome to Impresario Project

> A lightweight framework for complex validation, string generation, and math calculation

## Migration Information

The intention of this version is to improve the flexibility of the custom function.
Since the old version we allow only up to 2 parameters to both functions and conditions
that might not enough for custom ones.
    
Since the 4th version, any user can define the unlimited number of parameters in the metadata file,
then refer them using the parameter name in the configuration. This could provide the more flexible 
configuration because users can create more custom implementations. The side benefits are the parameter 
configurations don't need to be in order and much more readable.

Since the version 4.0.0 has some major change and do not be backward compatible with the previous versions, 
any user who use version since 3.0.0 or later, need to upgrade some configurations follow this
* The metadata config has no longer `reference-to-parameter1` and `reference-to-parameter2`
* Introduce a new `<Parameter>` at the condition and function metadata instead, so that any custom implementations can 
  define unlimited number of parameters. 
  
Check the document below.


## Get Started
Add dependency

Maven
```
<dependency>
    <groupId>io.github.playsafe</groupId>
    <artifactId>impresario</artifactId>
    <version>4.0.0</version>
</dependency>
```
Gradle
```
compile group: 'io.github.playsafe', name: 'impresario', version: '4.0.0'
```

The repository is [https://mvnrepository.com/artifact/io.github.playsafe/impresario](https://mvnrepository.com/artifact/io.github.playsafe/impresario)

## Prerequisite
1. Java 8 or above
2. Basic knowledge of xml format


## Understand the problem this project can solve
In the enterprise project, you might have to deal with the complex string generation rules to 
generate

* Sentences with quantity (such as **"There is an apple"**, **"There are 2 apples"**, **"Da ist ein Apfel"**, **"Es gibt 2 Äpfel"**)
* Internationalization of some languages that use different words, pattern, or even some languages has gender
  * There are 2 apples = 2つのリンゴがあります
  * There is 1 apple = リンゴが1つあります
* A string which is base on the contexts, and/or countries like address format of each country
* etc

In addition, you might have to deal with the complex validation rules 
to validate the same object in different contexts for example, the mail address requires full describe 
while the user address might require only city, but those 2 contexts use the same Address object.

This is a free framework to use, however I'm very appreciated to any people who want to donate for my snack here
https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=XKEX4E72J44B2&source=url


## Step 1: Create a metadata file
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Meta>
    <Definition reference-to-name="name" reference-item-tag="Item"
                reference-item-key="key" reference-item-value="value"/>

    <Conditions reference-to-name="name">
        <Condition name="or" class="org.companion.impresario.ConditionOr"/>
        <Condition name="and" class="org.companion.impresario.ConditionAnd"/>

        <Condition name="equals" class="org.companion.impresario.ConditionEquals">
            <Parameter name="x"/>
            <Parameter name="y"/>
        </Condition>

        <Condition name="equals_ignore_case" class="org.companion.impresario.ConditionEqualsIgnoreCase">
            <Parameter name="x"/>
            <Parameter name="y"/>
        </Condition>

        <Condition name="not_equals" class="org.companion.impresario.ConditionNotEquals">
            <Parameter name="x"/>
            <Parameter name="y"/>
        </Condition>

        <Condition name="less_than" class="org.companion.impresario.ConditionLessThan">
            <Parameter name="x"/>
            <Parameter name="y"/>
        </Condition>

        <Condition name="less_than_or_equals" class="org.companion.impresario.ConditionLessThanOrEquals">
            <Parameter name="x"/>
            <Parameter name="y"/>
        </Condition>

        <Condition name="greater_than" class="org.companion.impresario.ConditionGreaterThan">
            <Parameter name="x"/>
            <Parameter name="y"/>
        </Condition>

        <Condition name="greater_than_or_equals" class="org.companion.impresario.ConditionGreaterThanOrEquals">
            <Parameter name="x"/>
            <Parameter name="y"/>
        </Condition>

        <Condition name="is_null" class="org.companion.impresario.ConditionIsNull"/>
        <Condition name="is_not_null" class="org.companion.impresario.ConditionIsNotNull"/>
        <Condition name="has_text" class="org.companion.impresario.ConditionHasText"/>
        <Condition name="has_no_text" class="org.companion.impresario.ConditionHasNoText"/>
        <Condition name="is_letter" class="org.companion.impresario.ConditionIsLetter"/>
        <Condition name="not" class="org.companion.impresario.ConditionNot"/>
    </Conditions>

    <Functions reference-to-name="name">
        <Function name="get" class="org.companion.impresario.FunctionGet">
            <Parameter name="target"/>
        </Function>

        <Function name="return" class="org.companion.impresario.FunctionReturn">
            <Parameter name="value"/>
        </Function>

        <Function name="concat" class="org.companion.impresario.FunctionConcat"/>

        <Function name="join" class="org.companion.impresario.FunctionJoin">
            <Parameter name="delimiter"/>
            <Parameter name="text"/>
        </Function>

        <Function name="replace_one" class="org.companion.impresario.FunctionReplaceOne">
            <Parameter name="text"/>
            <Parameter name="replace_value"/>
            <Parameter name="replace_target"/>
        </Function>

        <Function name="replace_all" class="org.companion.impresario.FunctionReplaceAll">
            <Parameter name="text"/>
            <Parameter name="replace_values"/>
        </Function>

        <Function name="length" class="org.companion.impresario.FunctionLength"/>
        <Function name="upper" class="org.companion.impresario.FunctionUpper"/>
        <Function name="lower" class="org.companion.impresario.FunctionLower"/>
        <Function name="choose" class="org.companion.impresario.FunctionChoose"/>

        <Function name="substring" class="org.companion.impresario.FunctionSubstring">
            <Parameter name="text"/>
            <Parameter name="position"/>
        </Function>

        <Function name="cut_off" class="org.companion.impresario.FunctionCutOff">
            <Parameter name="text"/>
            <Parameter name="position"/>
        </Function>

        <Function name="char_at" class="org.companion.impresario.FunctionCharAt">
            <Parameter name="text"/>
            <Parameter name="position"/>
        </Function>

        <Function name="add" class="org.companion.impresario.FunctionAddition"/>

        <Function name="subtract" class="org.companion.impresario.FunctionSubtraction">
            <Parameter name="number"/>
            <Parameter name="subtrahend"/>
        </Function>

        <Function name="multiply" class="org.companion.impresario.FunctionMultiplication"/>

        <Function name="divide" class="org.companion.impresario.FunctionDivision">
            <Parameter name="number"/>
            <Parameter name="divisor"/>
        </Function>

        <Function name="modulo" class="org.companion.impresario.FunctionModulo">
            <Parameter name="number"/>
            <Parameter name="divisor"/>
        </Function>

        <Function name="round_down" class="org.companion.impresario.FunctionRoundDown">
            <Parameter name="value"/>
            <Parameter name="scale"/>
        </Function>

        <Function name="round_up" class="org.companion.impresario.FunctionRoundUp">
            <Parameter name="value"/>
            <Parameter name="scale"/>
        </Function>

        <Function name="round_half_up" class="org.companion.impresario.FunctionRoundHalfUp">
            <Parameter name="value"/>
            <Parameter name="scale"/>
        </Function>

        <Function name="exponential" class="org.companion.impresario.FunctionExponential">
            <Parameter name="base"/>
            <Parameter name="power"/>
        </Function>
    </Functions>
</Meta>
```
This file will define the built-in functions and conditions.

1. The **\<Function\>** in the metadata will be matched against **\<Function\>** of configuration.\
   • **reference-to-name="name"** refers to attribute of **\<Function name="..."\>** of configuration.\
   • **reference-to-name="alias"** refers to attribute of **\<Function alias="..."\>** of configuration


2. The **\<Condition\>** in the metadata will be matched against **\<Condition\>** of configuration.\
   • **reference-to-name="name"** refers to attribute of **\<Condition name="..."\>** of configuration.\
   • **reference-to-name="alias"** refers to attribute of **\<Condition alias="..."\>** of configuration. 


3. The configuration of **\<Definition\>** refers to the configuration of **\<Definition\>** and its attributes
   * **reference-to-name="name"** refers to **\<Definition name="..."\>**
   * **reference-item-tag="Item"** refers to **\<Item\>**
   * **reference-item-key="key"** refers to **\<Item key="..."\>**
   * **reference-item-value="value"** refers to **\<Item value="..."\>**


4. The **\<Parameter\>** uses to define the name of both function and condition parameter, the name could be any value 
   that will be match against the **result-as** in the configuration. The number of **\<Parameter\>** under each 
   **\<Function\>** and **\<Condition\>** depends on the implementation of the concrete class. It means if you create 
   your own custom class, you will know how many parameters it requires. However, your implementation will not depend on 
   the parameter name, but instead the index (0, 1, 2, ...). The parameter name will be used only in the configuration file.


5. The non `<Parameter>` definition, of any built-in implementations in metadata, means expects only 1 and not
   specify any `result-as` in the configuration. However, you can add a `<Parameter name="...">` and define
   the `result-as` in the configuration.


**Note1**: The configurations are case-sensitive.\
**Note2**: You can change the `<Parameter name="..">` to any name you want, and the `result-as` must be the same name. 
Nevertheless, you cannot change the order of parameters, because the concrete implementations, especially the built-in ones,
expect it in order.


## Step 2.1: Create a generation name
For example, If I want to generate address label of some countries. The format might depend on country specific
```
Belgium format
<street>
<zip> <city>
<COUNTRY>

India format
<street>
<city>
<state>
<zip>
<COUNTRY>
```
From the belgium format above, there are 3 lines and upper case the country name.
There are neither new lines if no data above, nor spaces if no data in front.
We need to replace the street name with abbreviations if street is longer than 50 characters.

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Labels>
    <Label group="BE">
        <Definitions>
            <Definition name="STREET_REPLACEMENTS">
                <Item value="Z.I." key="Zone industrielle" />
                <Item value="Rés." key="Résidence" />
                <Item value="Bd"   key="Boulevard" />
                <Item value="Chée" key="Chaussée" />
                <Item value="Imp"  key="Impasse" />
                <Item value="Av."  key="Avenue" />
                <Item value="Sq."  key="Square" />
                <Item value="Ctre" key="Centre" />
                <Item value="Rte"  key="Route" />
                <Item value="Pl."  key="Place" />
            </Definition>
        </Definitions>

        <Function name="concat">

            <!-- Append street -->
            <Function name="choose">
                <Function name="choose">
                    <Condition name="has_text">
                        <Function name="get" target="@{street}" />
                    </Condition>

                    <Function name="replace_all">
                        <Condition name="greater_than">
                            <Function name="length" result-as="x">
                                <Function name="get" target="@{street}" />
                            </Function>
                            <Function name="return" value="50" result-as="y" />
                        </Condition>
                        <Function name="get" target="@{street}" result-as="text"/>
                        <Function name="return" value="#{STREET_REPLACEMENTS}" result-as="replace_values" />
                    </Function>

                    <Function name="get" target="@{street}" />
                </Function>

                <Function name="return" value="" />
            </Function>

            <!-- Add new line before city and/or postal code -->
            <Function name="choose">
                <Function name="get" target="${line.separator}">
                    <Condition name="and">
                        <Condition name="has_text">
                            <Function name="get" target="@{street}" />
                        </Condition>
                        <Condition name="or">
                            <Condition name="has_text">
                                <Function name="get" target="@{postalCode}" />
                            </Condition>
                            <Condition name="has_text">
                                <Function name="get" target="@{city}" />
                            </Condition>
                        </Condition>
                    </Condition>
                </Function>
                <Function name="return" value="" />
            </Function>

            <!-- Append postal code -->
            <Function name="choose">
                <Function name="get" target="@{postalCode}">
                    <Condition name="has_text">
                        <Function name="get" target="@{postalCode}" />
                    </Condition>
                </Function>
                <Function name="return" value="" />
            </Function>

            <!-- Append a space before city -->
            <Function name="choose">
                <Function name="return" value=" ">
                    <Condition name="and">
                        <Condition name="has_text">
                            <Function name="get" target="@{postalCode}" />
                        </Condition>
                        <Condition name="has_text">
                            <Function name="get" target="@{city}" />
                        </Condition>
                    </Condition>
                </Function>
                <Function name="return" value="" />
            </Function>

            <!-- Append city -->
            <Function name="choose">
                <Function name="get" target="@{city}">
                    <Condition name="has_text">
                        <Function name="get" target="@{city}" />
                    </Condition>
                </Function>
                <Function name="return" value="" />
            </Function>

            <!-- Add new line before country -->
            <Function name="choose">
                <Function name="get" target="${line.separator}">
                    <Condition name="and">
                        <Condition name="has_text">
                            <Function name="get" target="@{country}" />
                        </Condition>
                        <Condition name="or">
                            <Condition name="has_text">
                                <Function name="get" target="@{postalCode}" />
                            </Condition>
                            <Condition name="has_text">
                                <Function name="get" target="@{city}" />
                            </Condition>
                            <Condition name="has_text">
                                <Function name="get" target="@{street}" />
                            </Condition>
                        </Condition>
                    </Condition>
                </Function>
                <Function name="return" value="" />
            </Function>

            <!-- Append country -->
            <Function name="choose">
                <Function name="upper">
                    <Condition name="has_text">
                        <Function name="get" target="@{country}" />
                    </Condition>
                    <Function name="get" target="@{country}" />
                </Function>
                <Function name="return" value="" />
            </Function>
        </Function>
    </Label>
</Labels>
```
Configuration file tips
* Specify properties using format **${}**.
  * The **${ABC.DEF}** refers to a property key **ABC.DEF**
  * Property values will be gotten from the System properties, so you have to load all properties first
* Specify the data using format **@{}**.
  * The **@{name}** refers to method **getName()** of the object input or key **name** of the Map
* Specify definition using format **#{}**.
  * The **#{XYZ}** refers to definition name **XYZ** 
  * The **#{XYZ.ABC}** refers to definition key **ABC** of definition name **XYZ** 
* Specify the new line using **${line.separator}** in order to be independent of operating system. 

## Step 2.2: Create validation rules
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ValidationRules>
    <!-- Rule 1: Postal Code's length = 5 -->
    <ValidationRule group="POSTAL_CODE_LENGTH">
        <Condition name="has_text">
            <Function name="get" target="@{postalCode}"/>
        </Condition>
        <Condition name="equals">
            <Function name="length" result-as="x">
                <Function name="get" target="@{postalCode}"/>
            </Function>
            <Function name="return" value="5" result-as="y"/>
        </Condition>
    </ValidationRule>

    <!-- Rule 2: Postal Code's length > 5 -->
    <ValidationRule group="POSTAL_CODE_LENGTH">
        <Condition name="has_text">
            <Function name="get" target="@{postalCode}"/>
        </Condition>
        <Condition name="greater_than">
            <Function name="length" result-as="x">
                <Function name="get" target="@{postalCode}"/>
            </Function>
            <Function name="return" value="5" result-as="y"/>
        </Condition>
        <Condition name="is_letter">
            <Function name="char_at">
                <Function name="get" target="@{postalCode}" result-as="text"/>
                <Function name="return" value="0" result-as="position"/>
            </Function>
        </Condition>
        <Condition name="is_letter">
            <Function name="char_at">
                <Function name="get" target="@{postalCode}" result-as="text"/>
                <Function name="return" value="1" result-as="position"/>
            </Function>
        </Condition>
    </ValidationRule>
</ValidationRules>
```

**Notice:** The multiple **\<Condition\>** under **\<ValidationRule\>** consider as **and** condition, 
but the same validation rule group consider **or** condition.
As the code above there is only 1 group, **POSTAL_CODE_LENGTH**, to validate if
**Postal code have 5 characters OR starts with 2 letters if longer than 5**


## Step 2.3: Create equation
```
<Equations>
    <Equation group="GROSS_AMOUNT_AFTER_DISCOUNT_100_EVERY_1000">
        <Function name="multiply">
            <Function name="subtract">
                <Function name="get" target="@{amount}" result-as="number"/>
                <Function name="multiply" result-as="subtrahend">
                    <Function name="return" value="100"/>
                    <Function name="round_down">
                        <Function name="divide" result-as="value">
                            <Function name="get" target="@{amount}" result-as="number"/>
                            <Function name="return" value="1000" result-as="divisor"/>
                        </Function>
                        <Function name="return" value="0" result-as="scale"/>
                    </Function>
                </Function>
            </Function>
            <Function name="add">
                <Function name="return" value="1"/>
                <Function name="divide">
                    <Function name="get" target="@{vatRate}" result-as="number"/>
                    <Function name="return" value="100" result-as="divisor"/>
                </Function>
            </Function>
        </Function>
    </Equation>

    <Equation group="MUL_GROUP_PAY_INCLUDE_TIP">
        <Function name="add">
            <Condition name="greater_than_or_equals">
                <Function name="get" target="@{amount}" result-as="x"/>
                <Function name="return" value="10000" result-as="y"/>
            </Condition>
            <Function name="return" value="200"/>
            <Function name="get" target="@{amount}"/>
        </Function>
    </Equation>

    <Equation group="MUL_GROUP_PAY_INCLUDE_TIP">
        <Function name="add">
            <Condition name="greater_than_or_equals">
                <Function name="get" target="@{amount}" result-as="x"/>
                <Function name="return" value="1000" result-as="y"/>
            </Condition>
            <Function name="return" value="100"/>
            <Function name="get" target="@{amount}"/>
        </Function>
    </Equation>

    <Equation group="MUL_GROUP_PAY_INCLUDE_TIP">
        <Function name="add">
            <Function name="return" value="10"/>
            <Function name="get" target="@{amount}"/>
        </Function>
    </Equation>
</Equations>
```

**Notice**: The configuration accepts the same group name, but the execution will be in order.
The 1st config will be executed first, unless not match condition, then the next one will be executed.
You need to be careful if you separate same group to multiple files.

## Step3: Create a new class or interface for this config.
```
public class Address {

    public String getStreet() {
        ...
    }

    public String getState() {
        ...
    }

    public String getCity() {
        ...
    }

    public String getPostalCode() {
        ...
    }
    
    public String getCountry() {
        ...
    }

```

## Step 4.1: Write Java code to generate
First, you need to load both metadata and configuration first, as the code below

```
File metaResource = new File(<path to metadata file>);
File configResource = new File(<path to config file>);
MetaData metaData = new MetaDataFactory().compile(metaResource);
LabelGeneratorFactory labelGeneratorFactory = new SingleLabelGeneratorFactory(metaData);
Map<String, LabelGenerator> labelGenerators = labelGeneratorFactory.compile(configResource);
```

Then, you can choose the label generator using group as the key 

```
// Set data to your data object first
Address address = new Address();

LabelGenerator labelGenerator = labelGenerators.get("BE")
String result = labelGenerator.labelOf(address);
```


## Step 4.2: Write Java code to validate
First, you need to load both metadata and configuration first, as the code below

```
File metaResource = new File(<absolute path to metadata file>);
File configResource = new File(<absolute path to config file>);
MetaData metaData = new MetaDataFactory().compile(metaResource);
ValidationRuleFactory validationRuleFactory = new SingleValidationRuleFactory(metaData);
Map<String, ValidationRule> validationRules = validationRuleFactory.compile(configResource);
```

Then, you can choose the label generator using group as the key 

```
// Set data to your data object first
Address address = new Address();

ValidationRule validationRule = validationRules.get("POSTAL_CODE_LENGTH");
boolean isValid = validationRule.validate(address);
```

## Step 4.3: Write Java code to calculate
First, you need to load both metadata and configuration first, as the code below

```
File metaResource = new File(<absolute path to metadata file>);
File configResource = new File(<absolute path to config file>);
MetaData metaData = new MetaDataFactory().compile(metaResource);
EquationFactory equationFactory = new SingleEquationFactory(metaData);
Map<String, Equation> equations = equationFactory.compile(configResource);
```

Then, you can choose the label generator using group as the key 

```
Price price = new Price("7325.00");

Equation equation = equations.get("MUL_GROUP_PAY_INCLUDE_TIP");
BigDecimal result = equation.perform(price);
```

## Optional: Multiple configuration files compilation
Since version 3.0.0, Impresario introduce a new utility to compile multiple configuration.
However, there is one more file you need to add like this
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Files>
    <Label src="<absolute path of xml file>" />
    <Label src="<absolute path of xml file>" />

    <ValidationRule src="<absolute path of xml file>" />
    <ValidationRule src="<absolute path of xml file>" />
</Files>
```
To compile label generator, use the code below
```
File multipleFileCompile = new File(<absolute path of file above>);
LabelGeneratorFactory labelGeneratorFactory = new MultipleLabelGeneratorFactory(metaData);
Map<String, LabelGenerator> labelGenerators = labelGeneratorFactory.compile(multipleFileCompile);
```

for validation rule
```
File multipleFileCompile = new File(<absolute path of file above>);
ValidationRuleFactory validationRuleFactory = new MultipleValidationRuleFactory(metaData);
Map<String, ValidationRule> validationRules = validationRuleFactory.compile(multipleFileCompile);
```
From the code, you will notice that you can separate `<Label>` from `<ValidationRule>` to another file 


## Create your custom Function
You can create a new function yourselves by
1. Creating a new class and implement Function interface
2. Define a constructor that require FunctionDefinition
3. Register your custom function in metadata file


```
class MyCustomFunction implements Function {

    public MyCustomFunction(FunctionDefinition definition) {
        // You can retrieve configuration from definition
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        // Perform the function name directly, or check the existing of pre-condition first
        // ConditionNotMatchException should be thrown when pre-condition doesn't match
    }
}
```

Notice:
* You can get a pre-condition from the definition, it corresponds to the condition tag in the configuration
* The `Map<String, Map<String, Object>> definitions` is the data of definition tag in the configuration
* The `Object input` is the data object that send from user
* You don't need to define class to be public, but you need a public constructor


## Create your custom Condition
You can create a new condition yourselves by 
1. Creating a new class and implement Condition interface
2. Define a constructor that require ConditionDefinition
3. Register your custom condition in metadata file


```
class MyCustomCondition implements Condition {

    public MyCustomCondition(ConditionDefinition definition){
        // You can retrieve confiquration from definition
    }

    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        // return the result
    }
}
```

Notice:
* You can get many pre-conditions from definition, it corresponds to the condition tag in the configuration
* The `Map<String, Map<String, Object>> definitions` is the data of definition tag in the configuration
* The `Object input` is the data object that send from user
* You don't need to define class to be public, but you need a public constructor
* org.companion.impresario.VariableReflector can help you when
   * You want value from object
   * You want value of properties
   * You want the definition, or the value of particular definition key

---------------------------------------------------------------------------------------------------

### Available Function
    org.companion.impresario.FunctionGet

Returns value from the specific definition, properties, specific field, value of map, or the value itself corresponds to the configuration.

Example Configuration
```
<Function name="get" target="@{fieldA}" />
```
```
<Function name="get" target="#{DefinitionName.Key}>
    <Condition name="has_text">
        <Function name="get" target="@{fieldA}" />
    </Condition>
</Function>
```

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionReturn

Returns a string value from the configuration directly without any process.

Example Configuration
```
<Function name="return" value="123" />
```
```
<Function name="return" value="Has Text">
    <Condition name="has_text">
        <Function name="return" value="ABC" />
    </Condition>
</Function>
```

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionConcat

Returns value after concatenate string from all functions together

Example Configuration
```
<Function name="concat">
    <Function name="get" target="@{fieldA}" />
    <Function name="return" target=" " />
    <Function name="get" target="@{fieldB}" />
</Function>
```
From this config, result of `@{fieldA} = "Hello"` and `@{fieldB} = "World"` will be `Hello World`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionJoin

Returns value after concatenate each string from all functions with delimiter 

Example Configuration
```
<Function name="join">
    <Function name="get" target="," result-as="delimiter" />
    <Function name="get" target="@{fieldA} result-as="text" />
    <Function name="get" target="@{fieldB} result-as="text" />
</Function>
```
From this config, result of `@{fieldA} = "Foo"` and `@{fieldB} = "Bar"` will be `Foo,Bar`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionReplaceOne

Returns value after replace a specific string by a specific string

Example Configuration
```
<Label group="REPLACE_DEFINITION_AT_SPECIFIC_KEY_BY_MAP">
    <Definitions>
        <Definition name="REPLACE_KEY">
            <Item key="AMOUNT_PATTERN" value="AMOUNT=!@#$"/>
        </Definition>
    </Definitions>
    <Function name="replace_one">
        <Function name="get" target="#{REPLACE_KEY.AMOUNT_PATTERN}" result-as="text"/>
        <Function name="get" target="@{amount}" result-as="replace_value"/>
        <Function name="get" target="@{target}" result-as="replace_target"/>
    </Function>
</Label>
```
This function requires 3 parameters (result-as)
1. **text** a string that includes the *replace_target*
2. **replace_target** a string that will be replaced by *replace_value*
3. **replace_value** a string that will be used to replace *replace_target*
This is equivalent as calling `text.replace(replace_target, replace_value)`
From this config, result of `@{target} = "!@#$"` and `@{amount} = "99.99"` will be `AMOUNT=99.99`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionReplaceAll

Returns value after replace all strings by the specific map of key-value. The map of key-value could be specified by 
the field or definition name. Be careful, this function can be the cause of performance issue if there are many keys to 
be replaced.

Example Configuration
```
<Label group="REPLACE_ALL_BY_NON_EXISTING_DEFINITION">
    <Definitions>
        <Definition name="TEXT">
            <Item key="FORMAT_TEXT" value="Hello [NAME]! [GREET]"/>
        </Definition>
        <Definition name="KEYS">
            <Item key="[NAME]" value="John"/>
            <Item key="[GREET]" value="Never Better"/>
        </Definition>
    </Definitions>
    <Function name="replace_all">
        <Function name="get" target="#{TEXT.FORMAT_TEXT}" result-as="text"/>
        <Function name="return" value="#{KEYS}" result-as="replace_values"/>
    </Function>
</Label>
```

This function requires 2 parameters (result-as)
1. **text** a string that includes the key of *replace_values*
2. **replace_values** a result of method of type `Map<String, ?>`, or a definition
From this config, result of the config above will be `Hello John! Never Better`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionLength

Returns length of the string

Example Configuration
```
<Label group="LENGTH_OF_CITY">
    <Function name="length">
        <Function name="get" target="@{city}" />
    </Function>
</Label>
```
From this config, result of `@{city} = "ABCDE"` will be `5`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionUpper

Returns value after converts string to upper case

Example Configuration
```
<Label group="UPPER_COUNTRY">
    <Function name="upper">
        <Function name="get" target="@{country}" />
    </Function>
</Label>
```
From this config, result of `@{country} = "netherlands"` will be `NETHERLANDS`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionLower
    
Returns value after converts string to lower case

Example Configuration
```
<Label group="LOWER_SOMETHING">
    <Function name="lower">
        <Function name="get" target="@{something}" />
    </Function>
</Label>
```
From this config, result of `@{something} = "XYZ"` will be `xyz`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionChoose

Returns the result of the first executable value among functions

Example Configuration
```
<Label group="HALF_PRICE_FOR_CHILDREN_OR_ELDER">
    <Function name="choose">
        <Function name="get" target="HALF_PRICE">
            <Condition name="or">
                <Condition name="less_than">
                    <Function name="get" target="@{age}" result-as="x" />
                    <Function name="return" value="12" result-as="y" />
                </Condition>
                <Condition name="greater_than">
                    <Function name="get" target="@{age}" result-as="x" />
                    <Function name="return" value="60" result-as="y" />
                </Condition>
            </Condition>
        </Function>
        <Function name="return" value="FULL_PRICE" />
    </Function>
</Label>
```
From this config, The result will be `HALF_PRICE` if `@{age} < 12 || @{age} > 60`, otherwise `FULL_PRICE` 

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionSubstring
Extract a part of string either from an index to end, or from start to the index. 
This function requires both **text** and **position**
  * The negative position (-X): return since first character until the last X character exclude the last character e.g. 9876543 substring -3 = 9876
  * The positive position (+X): return since character X to the last character e.g. 123456 substring 2 = 3456

Example Configuration
```
<Label group="NOT_FIRST_2_AND_LAST_3">
    <Function name="substring">
        <Function name="substring" result-as="text">
            <Function name="get" target="@{param}" result-as="text" />
            <Function name="return" value="2" result-as="position" />
        </Function>
        <Function name="return" value="-3" result-as="position" />
    </Function>
</Label>
```
From this config, assume that `@{param} = "Hello World"`. <br>
The 1st substring will result `llo World` <br>
The 2nd will take result from the 1st, the result will be `llo Wo`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionCutOff
Extract a part of string either first X characters, or last X characters. 
This function requires both **text** and **position**
 * The negative position (-X): return last X character e.g. 9876543 cut off -3 = 543
 * The positive position (+X): return first X character e.g. 123456 cut off 2 = 12

Example Configuration
```
<Label group="KEEP_FIRST_4_NAME_CHARS">
    <Function name="cut_off">
        <Function name="get" target="@{name}" result-as="text" />
        <Function name="return" value="4" result-as="position" />
    </Function>
</Label>
```
From this config, result of result `@{name} = "Tony Stark"` will be `Tony`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionCharAt

Returns a character at the specific position. The positive returns character from front, and negative from the back.
This function requires both **text** and **position**.
For example the input is "Hello World"
 * The position 0 is `H`
 * The position 4 is `o`
 * The position -3 is `r`

Example Configuration
```
<Label group="SECOND_CHAR_AT_FROM_BEHIND">
    <Function name="char_at">
        <Function name="get" target="@{param}" result-as="text" />
        <Function name="return" value="-2" result-as="position" />
    </Function>
</Label>
```
From this config, result of result `@{param} = "ABCDE"` will be `D`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionAddition

Returns a decimal after add all values together regardless the precision digit.

Example Configuration
```
<Function name="add">
    <Function name="get" target="@{amount}" />
    <Function name="get" target="#{x.y}" />
    <Function name="get" target="${value}" />
    ...
</Function>
```
From this config, result of `@{amount} = "15.74"`, `#{x.y} = 78.43728570`, `${value} = 25.472374` will be `15.74 + 78.43728570 + 25.472374 = 119.64965970`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionSubtraction

Returns a decimal after subtract all values together regardless the precision digit.
This function requires a parameter **number**, and an unlimited number of parameter **subtrahend**.

Example Configuration
```
<Function name="subtract">
    <Function name="get" target="@{amount}" result-as="number"/>
    <Function name="get" target="#{x.y}" result-as="subtrahend"/>
    <Function name="get" target="${value}" result-as="subtrahend"/>
    ...
</Function>
```
From this config, result of `@{amount} = "15.74"`, `#{x.y} = 78.43728570`, `${value} = 25.472374` will be `15.74 - 78.43728570 - 25.472374 = -88.16965970`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionMultiplication

Returns a decimal after multiply all values together regardless the precision digit.

Example Configuration
```
<Function name="multiply">
    <Function name="get" target="@{amount}" />
    <Function name="get" target="#{x.y}" />
    <Function name="get" target="${value}" />
    ...
</Function>
```
From this config, result of `@{amount} = "15.74"`, `#{x.y} = 78.43728570`, `${value} = 25.472374` will be `15.74 * 78.43728570 * 25.472374 = 31448.2662223312633320`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionDivision

Returns a decimal after divide all values together regardless the precision digit.
This function requires a parameter **number**, and an unlimited number of parameter **divisor**.

Example Configuration
```
<Function name="divide">
    <Function name="get" target="@{amount}" result-as="number"/>
    <Function name="get" target="#{x.y}" result-as="divisor"/>
    <Function name="get" target="${value}" result-as="divisor"/>
    ...
</Function>
```

From this config, result `@{amount} = 15.74`, `#{x.y} = 78.43728570`, `${value} = 25.472374` will be `15.74 / 78.43728570 / 25.472374 = 0.007877941449887486`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionModulo

Returns a decimal after modulo 2 values together regardless the precision digit.
This function requires a parameter **number**, and a parameter **divisor**.

Example Configuration
```
<Function name="modulo">
    <Function name="get" target="@{item}" result-as="number"/>
    <Function name="get" target="@{count}" result-as="divisor"/>
</Function>
```

From this config, result `@{item} = 25.337`, `@{count} = 3.255` will be `25.337 % 3.255 = 2.552`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionRoundDown

Returns a decimal after rounding down a value after next specific precision digit.
This function requires a parameter **value**, and a parameter **scale**.

Example Configuration
```
<Function name="round_down">
    <Function name="get" target="@{amount}" result-as="value" />
    <Function name="return" value="3" result-as="scale" />
</Function>
```
From this config, result of `@{amount} = "15.7489"` will be `15.748`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionRoundUp

Returns a decimal after rounding up a value after next specific precision digit.
This function requires a parameter **value**, and a parameter **scale**.

Example Configuration
```
<Function name="round_up">
    <Function name="get" target="@{amount}" result-as="value" />
    <Function name="return" value="3" result-as="scale" />
</Function>
```
From this config, result of `@{amount} = "15.7481"` will be `15.749`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionRoundHalfUp

Returns a decimal after rounding half up a value after next specific precision digit.
This function requires a parameter **value**, and a parameter **scale**.

Example Configuration
```
<Function name="round_half_up">
    <Function name="get" target="@{amount}" result-as="value" />
    <Function name="return" value="3" result-as="scale" />
</Function>
```
From this config, result of `@{amount} = "15.7484"`  will be `15.748`.

However, result of `@{amount} = "15.7485"`  will be `15.749`.

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionExponential

Returns a decimal after exponential 2 values together regardless the precision digit.
This function requires a parameter **base**, and a parameter **power**.

Example Configuration
```
<Function name="exponential">
    <Function name="get" target="@{a}" result-as="base" />
    <Function name="get" target="@{b}" result-as="power" />
</Function>
```

From this config, result of `@{a} = "15.7484"`, and `@{b} = 3` will be `3905.793795955904`

---------------------------------------------------------------------------------------------------

### Available Condition

    org.companion.impresario.ConditionOr

Returns true if one of all conditions is true, otherwise false
```
<Condition name="or">
    <Condition name="less_than">
        <Function name="get" target="@{age}" result-as="x" />
        <Function name="return" value="12" result-as="y" />
    </Condition>
    <Condition name="greater_than">
        <Function name="get" target="@{age}" result-as="x" />
        <Function name="return" value="60" result-as="y" />
    </Condition>
</Condition>
```
From this config, The result will be `true` if `@{age} < 12 || @{age} > 60`, 
otherwise the `false`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionAnd

Returns true if all conditions are true, otherwise false

Example Configuration
```
<Condition name="and">
    <Condition name="greater_than">
        <Function name="get" target="@{age}" result-as="x" />
        <Function name="get" target="17" result-as="y" />
    </Condition>
    <Condition name="greater_than_or_equals">
        <Function name="get" target="@{walletAmount}" result-as="x" />
        <Function name="get" target="@{productPrice}" result-as="y" />
    </Condition>
</Condition>
```
From this config, The result will be `true` if `@{age} > 17 && @{walletAmount} >= @{productPrice}`,
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionEquals

Returns true if 2 parameters are consider equals, otherwise false.

Example Configuration
```
<Condition name="equals">
    <Function name="get" target="@{param1}" result-as="x" />
    <Function name="get" target="@{param2}" result-as="y" />
</Condition>
```
From this config, The result will be `true` if `@{param1}.equals(@{param2})`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionEqualsIgnoreCase

Returns true if 2 parameters are consider equals regardless of lowercase or capital letter, otherwise false

Example Configuration
```
<Condition name="equals_ignore_case">
    <Function name="get" target="@{param1}" result-as="x" />
    <Function name="get" target="@{param2}" result-as="y" />
</Condition>
```
From this config, The result will be `true` if `@{param1}.equalsIgnoreCase(@{param2})`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionNotEquals

Returns true if 2 parameters are considered not equals, otherwise false

Example Configuration
```
<Condition name="not_equals">
    <Function name="get" target="@{param1}" result-as="x" />
    <Function name="get" target="@{param2}" result-as="y" />
</Condition>
```
From this config, The result will be `true` if `!@{param1}.equals(@{param2})`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionLessThan

Returns true if parameter1 < parameter2, otherwise false

Example Configuration
```
<Condition name="less_than">
    <Function name="get" target="@{param1}" result-as="x" />
    <Function name="get" target="@{param2}" result-as="y" />
</Condition>
```
From this config, The result will be `true` if `@{param1} < @{param2}`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionLessThanOrEquals
    
Returns true if parameter1 <= parameter2, otherwise false

Example Configuration
```
<Condition name="less_than_or_equals">
    <Function name="get" target="@{param1}" result-as="x" />
    <Function name="get" target="@{param2}" result-as="y" />
</Condition>
```
From this config, The result will be `true` if `@{param1} <= @{param2}`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionGreaterThan

Returns true if parameter1 > parameter2, otherwise false

Example Configuration
```
<Condition name="greater_than">
    <Function name="get" target="@{age}" result-as="x" />
    <Function name="return" value="17" result-as="y" />
</Condition>
```
From this config, The result will be `true` if `@{age} > 17`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionGreaterThanOrEquals

Returns true if parameter1 >= parameter2, otherwise false

Example Configuration
```
<Condition name="greater_than_or_equals">
    <Function name="get" target="@{walletAmount}" result-as="x" />
    <Function name="get" target="@{productPrice}" result-as="y" />
</Condition>
```
From this config, The result will be `true` if `@{walletAmount} >= @{productPrice}`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionIsNull

Returns true if parameter1 is null, otherwise false
                
Example Configuration
```
<Condition name="is_null">
    <Function name="get" target="@{param1}" />
</Condition>
```
From this config, The result will be `true` if `@{param1} == null`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionIsNotNull

Returns true if parameter1 is not null, otherwise false

Example Configuration
```
<Condition name="is_not_null">
    <Function name="get" target="@{param1}" />
</Condition>
```
From this config, The result will be `true` if `@{param1} != null`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionHasText

Returns true if parameter1 has text (length > 0), otherwise false

Example Configuration
```
<Condition name="has_text">
    <Function name="get" target="@{param1}" />
</Condition>
```
From this config, The result will be `true` if `@{param1} != null && @{param1}.length > 0`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionHasNoText

Returns true if parameter1 has no text (null or length = 0), otherwise false

Example Configuration
```
<Condition name="has_no_text">
    <Function name="get" target="@{param1}" />
</Condition>
```
From this config, The result will be `true` if `@{param1} == null || @{param1}.length == 0`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionIsLetter
    
Returns true the whole strings has only a letter, otherwise false

Example Configuration
```
<Condition name="is_letter">
    <Function name="get" target="@{param}" />
</Condition>
```
From this config, The result will be the result of `java.lang.Character.isLetter(@{param1})`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionNot

Returns the opposite result of a condition

Example Configuration
```
<Condition name="not">
    <Condition name="is_null">
        <Function name="get" target="@{param1}" />
    </Condition>
</Condition>
```
From this config, The result will be `true` if `!(@{param1} == null)` 
otherwise `false`

## License
This project is licensed under the Apache 2.0 License - see the [LICENSE](https://github.com/PlaySafe/impresario/blob/master/LICENSE) file for details
