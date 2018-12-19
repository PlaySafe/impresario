# Welcome to Impresario Project

> A lightweight framework for complex string generation

## Get Started
Add dependency

Maven
```
<dependency>
    <groupId>io.github.playsafe</groupId>
    <artifactId>impresario</artifactId>
    <version>1.0.1</version>
</dependency>
```
Gradle
```
compile group: 'io.github.playsafe', name: 'impresario', version: '1.0.1'
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

## Step 1: Create a meta data file
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Meta>
    <Definition reference-to-name="name" reference-item-tag="Item"
                reference-item-key="key" reference-item-value="value" />
    <FunctionAttribute reference-to-name="name" reference-to-parameter="param" />
    <ConditionAttribute reference-to-name="name"
                        reference-to-parameter1="value1" reference-to-parameter2="value2" />
    <Conditions>
        <Condition name="or" class="org.companion.impresario.ConditionOr" />
        <Condition name="and" class="org.companion.impresario.ConditionAnd" />
        <Condition name="has_text" class="org.companion.impresario.ConditionHasText" />
        <Condition name="greater_than" class="org.companion.impresario.ConditionGreaterThan" />
    </Conditions>
    <Functions>
        <Function name="get" class="org.companion.impresario.FunctionGet" />
        <Function name="concat" class="org.companion.impresario.FunctionConcat" />
        <Function name="replace" class="org.companion.impresario.FunctionReplace" />
        <Function name="choose" class="org.companion.impresario.FunctionChoose" />
        <Function name="length" class="org.companion.impresario.FunctionLength" />
    </Functions>
</Meta>
```
This file will define the available functions and conditions

1. The configuration of **\<FunctionAttribute\>** refers to the configuration attributes of **\<Function\>**
   * **reference-to-name="logic"** refers to attribute of **\<Function name="..."\>**
   * **reference-to-parameter="param"** refers to attribute of **\<Function param="..."\>**
2. The configuration of **\<ConditionAttribute\>** refers to the configuration attributes of **\<Condition\>**
   * **reference-to-name="logic"** refers to attribute of **\<Condition name="..."\>**
   * **reference-to-parameter1="value1"**, and **reference-to-parameter2="value2"** refer to **\<Condition value1="..." value2="..."\>**
3. The configuration of **\<Definition\>** refers to the configuration of **\<Definition\>** and its attributes
   * **reference-to-name="name"** refers to **\<Definition name="..."\>**
   * **reference-item-tag="Item"** refers to **\<Item\>**
   * **reference-item-key="key"** refers to **\<Item key="..."\>**
   * **reference-item-value="value"** refers to **\<Item value="..."\>**
4. The **\<Condition\>** uses to define the available conditions, so does the **\<Function\>**


## Step2: Create a generation logic
For example, I want to generate address label of belgium. The general format is 
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
There is neither new line if no data above, nor space if no data in front.
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
                        <Function name="get" param="@{street}" />
                    </Condition>

                    <Function name="replace" param="#{STREET_REPLACEMENTS}">
                        <Condition name="greater_than" value2="50">
                            <Function name="length">
                                <Function name="get" param="@{street}" />
                            </Function>
                        </Condition>
                        <Function name="get" param="@{street}" />
                    </Function>

                    <Function name="get" param="@{street}" />
                </Function>

                <Function name="get" param="" />
            </Function>

            <!-- Add new line before city and/or postal code -->
            <Function name="choose">
                <Function name="get" param="${line.separator}">
                    <Condition name="and">
                        <Condition name="has_text">
                            <Function name="get" param="@{street}" />
                        </Condition>
                        <Condition name="or">
                            <Condition name="has_text">
                                <Function name="get" param="@{postalCode}" />
                            </Condition>
                            <Condition name="has_text">
                                <Function name="get" param="@{city}" />
                            </Condition>
                        </Condition>
                    </Condition>
                </Function>
                <Function name="get" param="" />
            </Function>

            <!-- Append postal code -->
            <Function name="choose">
                <Function name="get" param="@{postalCode}">
                    <Condition name="has_text">
                        <Function name="get" param="@{postalCode}" />
                    </Condition>
                </Function>
                <Function name="get" param="" />
            </Function>

            <!-- Append a space before city -->
            <Function name="choose">
                <Function name="get" param=" ">
                    <Condition name="and">
                        <Condition name="has_text">
                            <Function name="get" param="@{postalCode}" />
                        </Condition>
                        <Condition name="has_text">
                            <Function name="get" param="@{city}" />
                        </Condition>
                    </Condition>
                </Function>
                <Function name="get" param="" />
            </Function>

            <!-- Append city -->
            <Function name="choose">
                <Function name="get" param="@{city}">
                    <Condition name="has_text">
                        <Function name="get" param="@{city}" />
                    </Condition>
                </Function>
                <Function name="get" param="" />
            </Function>

            <!-- Add new line before country -->
            <Function name="choose">
                <Function name="get" param="${line.separator}">
                    <Condition name="and">
                        <Condition name="has_text">
                            <Function name="get" param="@{country}" />
                        </Condition>
                        <Condition name="or">
                            <Condition name="has_text">
                                <Function name="get" param="@{postalCode}" />
                            </Condition>
                            <Condition name="has_text">
                                <Function name="get" param="@{city}" />
                            </Condition>
                            <Condition name="has_text">
                                <Function name="get" param="@{street}" />
                            </Condition>
                        </Condition>
                    </Condition>
                </Function>
                <Function name="get" param="" />
            </Function>

            <!-- Append country -->
            <Function name="choose">
                <Function name="upper">
                    <Condition name="has_text">
                        <Function name="get" param="@{country}" />
                    </Condition>
                    <Function name="get" param="@{country}" />
                </Function>
                <Function name="get" param="" />
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

## Step 4: Write Java code to generate
First, you need to load both meta data and configuration first, as the code below

```
File metaResource = new File(<path to meta data file>);
File configResource = new File(<path to config file>);
MetaData metaData = new MetaLabelFactory().compile(metaResource);
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
        // Perform the function logic directly, or check the existing of pre-condition first
        // ConditionNotMatchException should be thrown when pre-condition doesn't match
    }
}
```

Notice:
* You can get many pre-conditions from definition, it corresponds to the condition tag in the configuration
* The **Map<String, Map<String, Object>> definitions** is the data of definition tag in the configuration
* The **Object input** is the data object that send from user
* **Ignore the definition.getLogic()**, it is used to select the right implementation from meta data
* The **value1**, and **value2** refer to the **parameter1** and **parameter2** in the configuration respectively
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
* **Ignore the definition.getLogic()**, it is used to select the right implementation from meta data
* The **value1**, and **value2** refer to the **parameter1** and **parameter2** in the configuration respectively
* You don't need to define class to be public, but you need a public constructor
* org.companion.impresario.VariableReflector can help you when
   * You want value from object
   * You want value of properties
   * You want the definition or the value of particular definition key

---

### Available Function
    org.companion.impresario.FunctionGet

Returns value from the specific definition, properties, specific field, or the value itself corresponds to the configuration.

Since: 1.0.0

Support a map value since 1.0.2

---
    org.companion.impresario.FunctionConcat

Returns value after concatenate string from all functions together

Since: 1.0.0

---
    org.companion.impresario.FunctionJoin

Returns value after concatenate each string from all functions with delimiter 

Since: 1.0.1

Require: parameter1

---
    org.companion.impresario.FunctionReplace

Returns value after replace specific strings by the specific strings of definition

Since: 1.0.0

---
    org.companion.impresario.FunctionLength

Returns length of the string

Since: 1.0.0

---
    org.companion.impresario.FunctionUpper

Returns value after converts string to upper case

Since: 1.0.0

---
    org.companion.impresario.FunctionLower
    
Returns value after converts string to lower case

Since: 1.0.0

---
    org.companion.impresario.FunctionChoose

Returns the first executable value among multiple functions

Since: 1.0.0

---
    org.companion.impresario.FunctionSubstring

* The negative index (-X): return since first character until the last X character exclude the last character e.g 9876543 substring -3 = 9876
* The positive index (+X): return since character X to the last character e.g. 123456 substring 2 = 3456

Since: 1.0.0

---
    org.companion.impresario.FunctionCutOff

 * The negative index (-X): return last X character e.g 9876543 cut off -3 = 543
 * The positive index (+X): return first X character e.g. 123456 cut off 2 = 12

Since: 1.0.0

---
    org.companion.impresario.FunctionCharAt

Returns a character at the specific index

Since: 1.0.0

---

### Available Condition

    org.companion.impresario.ConditionOr

Returns true if one of all conditions is true, otherwise false

Since: 1.0.0

---
    org.companion.impresario.ConditionAnd

Returns true if all conditions are true, otherwise false

Since: 1.0.0

---
    org.companion.impresario.ConditionEquals

Returns true if 2 parameters are consider equals, otherwise false

Since: 1.0.0

---
    org.companion.impresario.ConditionNotEquals

Returns true if 2 parameters are consider not equals, otherwise false

Since: 1.0.0

---
    org.companion.impresario.ConditionLessThan

Returns true if parameter1 < parameter2, otherwise false

Since: 1.0.0

---
    org.companion.impresario.ConditionLessThanEquals
    
Returns true if parameter1 <= parameter2, otherwise false

Since: 1.0.0

---
    org.companion.impresario.ConditionGreaterThan

Returns true if parameter1 > parameter2, otherwise false

Since: 1.0.0

---
    org.companion.impresario.ConditionGreaterThanEquals

Returns true if parameter1 >= parameter2, otherwise false

Since: 1.0.0

---
    org.companion.impresario.ConditionIsNull

Returns true if parameter1 is null, otherwise false

Since: 1.0.0

---
    org.companion.impresario.ConditionIsNotNull

Returns true if parameter1 is not null, otherwise false

Since: 1.0.0

---
    org.companion.impresario.ConditionHasText

Returns true if parameter1 has text (length > 0), otherwise false

Since: 1.0.0

---
    org.companion.impresario.ConditionHasNoText

Returns true if parameter1 has no text (null or length = 0), otherwise false

Since: 1.0.0

---
    org.companion.impresario.ConditionIsLetter
    
Returns true the whole strings has only letter, otherwise false

Since: 1.0.0

---
    org.companion.impresario.ConditionNot

Returns the opposite result of a condition

Since: 1.0.0

## License
This project is licensed under the Apache 2.0 License - see the [LICENSE](https://github.com/PlaySafe/impresario/blob/master/LICENSE) file for details
