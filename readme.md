# Welcome to Impresario Project

> A lightweight framework for complex validation name and string generation

## Get Started
Add dependency

Maven
```
<dependency>
    <groupId>io.github.playsafe</groupId>
    <artifactId>impresario</artifactId>
    <version>2.0.0</version>
</dependency>
```
Gradle
```
compile group: 'io.github.playsafe', name: 'impresario', version: '2.0.0'
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
to validate the same object in different context for example, the mail address require full describe 
while the user address might require only city but those 2 context use the same Address object.

## Step 1: Create a meta data file
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Meta>
    <Definition reference-to-name="name" reference-item-tag="Item"
                reference-item-key="key" reference-item-value="value" />
    <FunctionAttribute  reference-to-name="name" 
                        reference-to-parameter1="param1" reference-to-parameter2="param2"/>
    <ConditionAttribute reference-to-name="name"
                        reference-to-parameter1="param1" reference-to-parameter2="param2" />
    <Conditions>
        <Condition name="or" class="org.companion.impresario.ConditionOr" />
        <Condition name="and" class="org.companion.impresario.ConditionAnd" />
        <Condition name="equals" class="org.companion.impresario.ConditionEquals" />
        <Condition name="not_equals" class="org.companion.impresario.ConditionNotEquals" />
        <Condition name="less_than" class="org.companion.impresario.ConditionLessThan" />
        <Condition name="less_than_or_equals" class="org.companion.impresario.ConditionLessThanEquals" />
        <Condition name="greater_than" class="org.companion.impresario.ConditionGreaterThan" />
        <Condition name="greater_than_or_equals" class="org.companion.impresario.ConditionGreaterThanEquals" />
        <Condition name="is_null" class="org.companion.impresario.ConditionIsNull" />
        <Condition name="is_not_null" class="org.companion.impresario.ConditionIsNotNull" />
        <Condition name="has_text" class="org.companion.impresario.ConditionHasText" />
        <Condition name="has_no_text" class="org.companion.impresario.ConditionHasNoText" />
        <Condition name="is_letter" class="org.companion.impresario.ConditionIsLetter" />
        <Condition name="not" class="org.companion.impresario.ConditionNot" />
    </Conditions>
    <Functions>
        <Function name="get" class="org.companion.impresario.FunctionGet" />
        <Function name="concat" class="org.companion.impresario.FunctionConcat" />
        <Function name="join" class="org.companion.impresario.FunctionJoin" />
        <Function name="replace" class="org.companion.impresario.FunctionReplace" />
        <Function name="length" class="org.companion.impresario.FunctionLength" />
        <Function name="upper" class="org.companion.impresario.FunctionUpper" />
        <Function name="lower" class="org.companion.impresario.FunctionLower" />
        <Function name="choose" class="org.companion.impresario.FunctionChoose" />
        <Function name="substring" class="org.companion.impresario.FunctionSubstring" />
        <Function name="cut_off" class="org.companion.impresario.FunctionCutOff" />
        <Function name="char_at" class="org.companion.impresario.FunctionCharAt" />
    </Functions>
</Meta>
```
This file will define the available functions and conditions

1. The configuration of **\<FunctionAttribute\>** refers to the configuration attributes of **\<Function\>**
   * **reference-to-name="name"** refers to attribute of **\<Function name="..."\>**
   * **reference-to-parameter1="param1"**, and **reference-to-parameter2="param2" refer to attribute of **\<Function param1="..." param2="..."\>**
2. The configuration of **\<ConditionAttribute\>** refers to the configuration attributes of **\<Condition\>**
   * **reference-to-name="name"** refers to attribute of **\<Condition name="..."\>**
   * **reference-to-parameter1="param1"**, and **reference-to-parameter2="param2"** refer to **\<Condition param1="..." param2="..."\>**
3. The configuration of **\<Definition\>** refers to the configuration of **\<Definition\>** and its attributes
   * **reference-to-name="name"** refers to **\<Definition name="..."\>**
   * **reference-item-tag="Item"** refers to **\<Item\>**
   * **reference-item-key="key"** refers to **\<Item key="..."\>**
   * **reference-item-value="value"** refers to **\<Item value="..."\>**
4. The **\<Condition\>** uses to define the available conditions, so does the **\<Function\>**


## Step 2.1: Create a generation name
For example, I want to generate address label of belgium. The format might depend on country specific
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
From the belgium format above, there are 3 lines and upper case for country name. 
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
                        <Function name="get" param1="@{street}" />
                    </Condition>

                    <Function name="replace" param1="#{STREET_REPLACEMENTS}">
                        <Condition name="greater_than" param2="50">
                            <Function name="length">
                                <Function name="get" param1="@{street}" />
                            </Function>
                        </Condition>
                        <Function name="get" param1="@{street}" />
                    </Function>

                    <Function name="get" param1="@{street}" />
                </Function>

                <Function name="get" param1="" />
            </Function>

            <!-- Add new line before city and/or postal code -->
            <Function name="choose">
                <Function name="get" param1="${line.separator}">
                    <Condition name="and">
                        <Condition name="has_text">
                            <Function name="get" param1="@{street}" />
                        </Condition>
                        <Condition name="or">
                            <Condition name="has_text">
                                <Function name="get" param1="@{postalCode}" />
                            </Condition>
                            <Condition name="has_text">
                                <Function name="get" param1="@{city}" />
                            </Condition>
                        </Condition>
                    </Condition>
                </Function>
                <Function name="get" param1="" />
            </Function>

            <!-- Append postal code -->
            <Function name="choose">
                <Function name="get" param1="@{postalCode}">
                    <Condition name="has_text">
                        <Function name="get" param1="@{postalCode}" />
                    </Condition>
                </Function>
                <Function name="get" param1="" />
            </Function>

            <!-- Append a space before city -->
            <Function name="choose">
                <Function name="get" param1=" ">
                    <Condition name="and">
                        <Condition name="has_text">
                            <Function name="get" param1="@{postalCode}" />
                        </Condition>
                        <Condition name="has_text">
                            <Function name="get" param1="@{city}" />
                        </Condition>
                    </Condition>
                </Function>
                <Function name="get" param1="" />
            </Function>

            <!-- Append city -->
            <Function name="choose">
                <Function name="get" param1="@{city}">
                    <Condition name="has_text">
                        <Function name="get" param1="@{city}" />
                    </Condition>
                </Function>
                <Function name="get" param1="" />
            </Function>

            <!-- Add new line before country -->
            <Function name="choose">
                <Function name="get" param1="${line.separator}">
                    <Condition name="and">
                        <Condition name="has_text">
                            <Function name="get" param1="@{country}" />
                        </Condition>
                        <Condition name="or">
                            <Condition name="has_text">
                                <Function name="get" param1="@{postalCode}" />
                            </Condition>
                            <Condition name="has_text">
                                <Function name="get" param1="@{city}" />
                            </Condition>
                            <Condition name="has_text">
                                <Function name="get" param1="@{street}" />
                            </Condition>
                        </Condition>
                    </Condition>
                </Function>
                <Function name="get" param1="" />
            </Function>

            <!-- Append country -->
            <Function name="choose">
                <Function name="upper">
                    <Condition name="has_text">
                        <Function name="get" param1="@{country}" />
                    </Condition>
                    <Function name="get" param1="@{country}" />
                </Function>
                <Function name="get" param1="" />
            </Function>
        </Function>
    </Label>
</Labels>
```
Configuration file tips
* Specify properties using format **${}**. 
  * The **${ABC.DEF}** refers to properties key **ABC.DEF**
  * Properties value will get from System properties, so you have to load all properties first
* Specify data using format **@{}**. 
  * The **@{name}** refers to method **getName()** of the object input
* Specify definition using format **#{}**. 
  * The **#{XYZ}** refers to definition name **XYZ** 
  * The **#{XYZ.ABC}** refers to definition key **ABC** of definition name **XYZ** 
* Add a the new line using **${line.separator}**

## Step 2.2: Create validation rules
```
<ValidationRules>
    <!-- Rule 1: Postal Code's length = 5 -->
    <ValidationRule group="POSTAL_CODE_LENGTH">
        <Definitions>
            <Definition name="EXPECT_LENGTH">
                <Item key="Postal" value="5" />
            </Definition>
        </Definitions>
        <Condition name="has_text">
            <Function name="get" param1="@{postalCode}" />
        </Condition>
        
        <!-- check if @{postalCode}.length == #{EXPECT_LENGTH.Postal} -->
        <Condition name="equals">
            <Function name="length">
                <Function name="get" param1="@{postalCode}" />
            </Function>
            <Function name="get" param1="#{EXPECT_LENGTH.Postal}" />
        </Condition>
    </ValidationRule>

    <!-- Rule 2: Postal Code's length > 5 -->
    <ValidationRule group="POSTAL_CODE_LENGTH">
        <Condition name="has_text">
            <Function name="get" param1="@{postalCode}" />
        </Condition>
        <Condition name="greater_than" param2="5">
            <Function name="length">
                <Function name="get" param1="@{postalCode}" />
            </Function>
        </Condition>
        <Condition name="is_letter">
            <Function name="char_at" param1="0">
                <Function name="get" param1="@{postalCode}" />
            </Function>
        </Condition>
        <Condition name="is_letter">
            <Function name="char_at" param1="1">
                <Function name="get" param1="@{postalCode}" />
            </Function>
        </Condition>
    </ValidationRule>
</ValidationRules>
```

**Notice:** The multiple **\<Condition\>** under **\<ValidationRule\>** consider as **and** condition, 
but the same validation rule group consider **or** condition. 
As the code above there is only 1 group (POSTAL_CODE_LENGTH) to validate if
**Postal code have 5 characters or starts with 2 letters if longer than 5**


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
First, you need to load both meta data and configuration first, as the code below

```
File metaResource = new File(<path to meta data file>);
File configResource = new File(<path to config file>);
MetaData metaData = new MetaDataFactory().compile(metaResource);
LabelGeneratorFactory labelGeneratorFactory = new LabelGeneratorFactory(metaData);
Map<String, LabelGenerator> labelGenerators = labelGeneratorFactory.compile(configResource);
```

Then, you can choose the label generator using group as the key 

```
// Set data to your data object first
Address address = new Address();

LabelGenerator labelGenerator = labelGenerators.get("BE")
String result = labelGenerator.labelOf(address);
```


## Step 4.2: Write Java code to generate
First, you need to load both meta data and configuration first, as the code below

```
File metaResource = new File(<path to meta data file>);
File configResource = new File(<path to config file>);
MetaData metaData = new MetaDataFactory().compile(metaResource);
ValidatorFactory validatorFactory = new ValidatorFactory(metaData);
Map<String, ValidationRule> validators = validatorFactory.compile(configResource);
```

Then, you can choose the label generator using group as the key 

```
// Set data to your data object first
Address address = new Address();

ValidationRule validationRule = validators.get("POSTAL_CODE_LENGTH");
boolean isValid = validationRule.validate(address);
```


## Create your custom Function
You can create a new function yourselves by
1. Creating a new class and implement Function interface
2. Define a constructor that require FunctionDefinition
3. Register your custom function in meta data file


```
public class MyCustomFunction implements Function {
    
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
* You can get many pre-conditions from definition, it corresponds to the condition tag in the configuration
* The **Map<String, Map<String, Object>> definitions** is the data of definition tag in the configuration
* The **Object input** is the data object that send from user
* **Ignore the definition.getName()**, it is used to select the right implementation from meta data
* The **param1**, and **param2** refer to the **parameter1** and **parameter2** in the configuration respectively
* You don't need to define class to be public, but you need a public constructor


## Create your custom Condition
You can create a new condition yourselves by 
1. Creating a new class and implement Condition interface
2. Define a constructor that require ConditionDefinition
3. Register your custom condition in meta data file


```
public class MyCustomCondition implements Condition {
    
    public MyCustomCondition(ConditionDefinition definition){
        // You can retrieve configuration from definition
    }
    
    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        // return the result
    }
}
```

Notice:
* You can get many pre-conditions from definition, it corresponds to the condition tag in the configuration
* The **Map<String, Map<String, Object>> definitions** is the data of definition tag in the configuration
* The **Object input** is the data object that send from user
* **Ignore the definition.getName()**, it is used to select the right implementation from meta data
* The **param1**, and **param2** refer to the **parameter1** and **parameter2** in the configuration respectively
* You don't need to define class to be public, but you need a public constructor
* org.companion.impresario.VariableReflector can help you when
   * You want value from object
   * You want value of properties
   * You want the definition or the value of particular definition key

---------------------------------------------------------------------------------------------------

### Available Function
    org.companion.impresario.FunctionGet

Returns value from the specific definition, properties, specific field, value of map, or the value itself corresponds to the configuration.

Example Configuration
```
<Function name="get" param1="@{fieldA}"/>
```
```
<Function name="get" param1="#{DefinitionName.Key}>
    <Condition name="has_text" param1="@{fieldA} />
</Function>
```

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionConcat

Returns value after concatenate string from all functions together

Example Configuration
```
<Function name="concat">
    <Function name="get" param1="@{fieldA} />
    <Function name="get" param1=" "/>
    <Function name="get" param1="@{fieldB} />
</Function>
```
From this config, assume that `@{fieldA} = "Hello"` and `@{fieldB} = "World"`. The result will be `Hello World`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionJoin

Returns value after concatenate each string from all functions with delimiter 

Example Configuration
```
<Function name="join" param1=",">
    <Function name="get" param1="@{fieldA} />
    <Function name="get" param1="@{fieldB} />
</Function>
```
From this config, assume that `@{fieldA} = "Foo"` and `@{fieldB} = "Bar"`. The result will be `Foo,Bar`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionReplace

Returns value after replace specific strings by the specific strings of definition.
Be careful, this function can raise performance issue for the long string.

Example Configuration
```
<Label group="REPLACE_DEFINITION_AT_SPECIFIC_KEY_BY_MAP">
    <Definitions>
        <Definition name="REPLACE_KEY">
            <Item key="AMOUNT_PATTERN" value="AMOUNT=!@#$" />
        </Definition>
    </Definitions>
    <Function name="replace" param1="@{target}">
        <Function name="get" param1="#{REPLACE_KEY.AMOUNT_PATTERN}" />
        <Function name="get" param1="@{amount}" />
    </Function>
</Label>
```
From this config, assume that `@{target} = "!@#$"` and `@{amount} = "99.99"`
The result will be `AMOUNT=99.99`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionLength

Returns length of the string

Example Configuration
```
<Label group="LENGTH_OF_CITY">
    <Function name="length">
        <Function name="get" param1="@{city}" />
    </Function>
</Label>
```
From this config, assume that `@{city} = "ABCDE"`. The result will be `5`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionUpper

Returns value after converts string to upper case

Example Configuration
```
<Label group="UPPER_COUNTRY">
    <Function name="upper">
        <Function name="get" param1="@{country}" />
    </Function>
</Label>
```
From this config, assume that `@{country} = "netherlands"`. The result will be `NETHERLANDS`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionLower
    
Returns value after converts string to lower case

Example Configuration
```
<Label group="LOWER_SOMETHING">
    <Function name="lower">
        <Function name="get" param1="@{something}" />
    </Function>
</Label>
```
From this config, assume that `@{something} = "XYZ"`. The result will be `xyz`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionChoose

Returns the result of the first executable value among functions

Example Configuration
```
<Label group="HALF_PRICE_FOR_CHILDREN_OR_ELDER">
    <Function name="choose">
        <Function name="get" param1="HALF_PRICE">
            <Condition name="or">
                <Condition name="less_than" param2="12">
                    <Function name="get" param1="@{age}" />
                </Condition>
                <Condition name="greater_than" param2="60">
                    <Function name="get" param1="@{age}" />
                </Condition>
            </Condition>
        </Function>
        <Function name="get" param1="FULL_PRICE" />
    </Function>
</Label>
```
From this config, The result will be `HALF_PRICE` if `@{age} < 12 || @{age} > 60`, otherwise `FULL_PRICE` 

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionSubstring

* The negative index (-X): return since first character until the last X character exclude the last character e.g 9876543 substring -3 = 9876
* The positive index (+X): return since character X to the last character e.g. 123456 substring 2 = 3456

Example Configuration
```
<Label group="NOT_FIRST_2_AND_LAST_3">
    <Function name="substring" param1="-3">
        <Function name="substring" param1="2">
            <Function name="get" param1="@{param}" />
        </Function>
    </Function>
</Label>
```
From this config, assume that `@{param} = "Hello World"`. <br>
The 1st substring will result `llo World` <br>
The 2nd will take result from the 1st, the result will be `llo Wo`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionCutOff

 * The negative index (-X): return last X character e.g 9876543 cut off -3 = 543
 * The positive index (+X): return first X character e.g. 123456 cut off 2 = 12

Example Configuration
```
<Label group="KEEP_FIRST_5_NAME_CHARS">
    <Function name="cut_off" param1="5">
        <Function name="get" param1="@{name}" />
    </Function>
</Label>
```
From this config, assume that `@{name} = "Tony Stark"`. The result will be `Stark`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.FunctionCharAt

Returns a character at the specific index. The positive returns character from front, and negative from the back.
For example the input is "Hello World"
 * The index of 0 is `H`
 * The index of 4 is `o`
 * The index of -3 is `r`

Example Configuration
```
<Label group="SECOND_CHAR_AT_FROM_BEHIND">
    <Function name="char_at" param1="-2">
        <Function name="get" param1="@{param}" />
    </Function>
</Label>
```
From this config, assume that `@{param} = "ABCDE"`. The result will be `D`

---------------------------------------------------------------------------------------------------

### Available Condition

    org.companion.impresario.ConditionOr

Returns true if one of all conditions is true, otherwise false
```
<Condition name="or">
    <Condition name="less_than" param2="12">
        <Function name="get" param1="@{age}" />
    </Condition>
    <Condition name="greater_than" param2="60">
        <Function name="get" param1="@{age}" />
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
    <Condition name="greater_than" param2="17">
        <Function name="get" param1="@{age}" />
    </Condition>
    <Condition name="greater_than_or_equals">
        <Function name="get" param1="@{walletAmount}" />
        <Function name="get" param1="@{productPrice}" />
    </Condition>
</Condition>
```
From this config, The result will be `true` if `@{age} > 17 && @{walletAmount} >= @{productPrice}`,
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionEquals

Returns true if 2 parameters are consider equals, otherwise false

Example Configuration
```
<Condition name="equals">
    <Function name="get" param1="@{param1}" />
    <Function name="get" param1="@{param2}" />
</Condition>
```
From this config, The result will be `true` if `@{param1}.equals(@{param2})`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionNotEquals

Returns true if 2 parameters are consider not equals, otherwise false

Example Configuration
```
<Condition name="not_equals">
    <Function name="get" param1="@{param1}" />
    <Function name="get" param1="@{param2}" />
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
    <Function name="get" param1="@{param1}" />
    <Function name="get" param1="@{param2}" />
</Condition>
```
From this config, The result will be `true` if `@{param1} < @{param2}`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionLessThanEquals
    
Returns true if parameter1 <= parameter2, otherwise false

Example Configuration
```
<Condition name="less_than_or_equals">
    <Function name="get" param1="@{param1}" />
    <Function name="get" param1="@{param2}" />
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
    <Function name="get" param1="@{age}" />
    <Function name="get" param1="17" />
</Condition>
```
From this config, The result will be `true` if `@{age} > 17`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionGreaterThanEquals

Returns true if parameter1 >= parameter2, otherwise false

Example Configuration
```
<Condition name="greater_than_or_equals">
    <Function name="get" param1="@{walletAmount}" />
    <Function name="get" param1="@{productPrice}" />
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
    <Function name="get" param1="@{param1}" />
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
    <Function name="get" param1="@{param1}" />
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
    <Function name="get" param1="@{param1}" />
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
    <Function name="get" param1="@{param1}" />
</Condition>
```
From this config, The result will be `true` if `@{param1} == null || @{param1}.length == 0`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.impresario.ConditionIsLetter
    
Returns true the whole strings has only letter, otherwise false

Example Configuration
```
<Condition name="is_letter">
    <Function name="get" param1="@{param}" />
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
        <Function name="get" param1="@{param1}" />
    </Condition>
</Condition>
```
From this config, The result will be `true` if `!(@{param1} == null)` 
otherwise `false`

## License
This project is licensed under the Apache 2.0 License - see the [LICENSE](https://github.com/PlaySafe/impresario/blob/master/LICENSE) file for details
