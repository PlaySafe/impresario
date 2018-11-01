# Welcome to Impresario Project

> A lightweight string generation which is developed base on the complex string generation problem.

As we know that there are many rules, and conditions to generate a string such as

* Quantity in english which is append ***s***, ***es*** base on the quantity
* Internationalization of some languages that use different words for the specific gender
* A string which is base on the contexts, and/or countries
* etc

Using this project, you are able to

* Reuse code
* De-coupling the logic in your code to configuration rules
* Separate the context of each problem
* Customize / Self-Defined new functions, and/or conditions to your system

---

### Understand the configuration format

##### There are 2 main XML configuration files
1. **Meta Data**, which represents the configuration format, available functions, and available conditions
2. **Configuration**, which represents the logic, and conditions to generate a string 

---

### Example format of *Meta Data* file
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Meta>
    <Definition reference-to-name="name" reference-item-tag="Item"
                reference-item-key="key" reference-item-value="value" />
    <FunctionAttribute reference-to-name="logic" reference-to-parameter="param" />
    <ConditionAttribute reference-to-name="logic"
                        reference-to-parameter1="value1" reference-to-parameter2="value2" />
    <Conditions>
        <Condition name="or" class="org.companion.impresario.ConditionOr" />
        <Condition name="and" class="org.companion.impresario.ConditionAnd" />
        <Condition name="has_text" class="org.companion.impresario.ConditionHasText" />
    </Conditions>
    <Functions>
        <Function name="get" class="org.companion.impresario.FunctionGet" />
        <Function name="concat" class="org.companion.impresario.FunctionConcat" />
        <Function name="replace" class="org.companion.impresario.FunctionReplace" />
    </Functions>
</Meta>
```

1. The configuration of **\<FunctionAttribute\>** refers to the configuration attributes of **\<Function\>**
   * **reference-to-name="logic"** refers to attribute of **\<Function logic="..."\>**
   * **reference-to-parameter="param"** refers to attribute of **\<Function param="..."\>**
2. The configuration of **\<ConditionAttribute\>** refers to the configuration attributes of **\<Condition\>**
   * **reference-to-name="logic"** refers to attribute of **\<Condition logic="..."\>**
   * **reference-to-parameter1="value1"**, and **reference-to-parameter2="value2"** refer to **\<Condition value1="..." value2="..."\>**
3. The configuration of **\<Definition\>** refers to the configuration of **\<Definition\>** and its attributes
   * **reference-to-name="name"** refers to **\<Definition name="..."\>**
   * **reference-item-tag="Item"** refers to **\<Item\>**
   * **reference-item-key="key"** refers to **\<Item key="..."\>**
   * **reference-item-value="value"** refers to **\<Item value="..."\>**
4. The **\<Condition\>** uses to define the available conditions, so does the **\<Function\>**

---

### Example format of *Configuration* file
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Labels>
    <Label group="ABC">
        <Definitions>
            <Definition name="DefinitionName">
                <Item key="ABC" value="XYZ" />
                <Item key="DEF" value="${XYZ}" />
                <Item key="GHI" value="@{XYZ}" />
            </Definition>
            <Definition name="DefinitionName">
                    ...
            </Definition>
        </Definitions>
        <Function logic="concat">
            <Condition logic="and">
                <Condition logic="or">
                    ...
                </Condition>
                <Condition logic="and">
                    ...
                </Condition>
                <Condition logic="or">
                    ...
                </Condition>
            </Condition>
            
            <Function logic="get" param="@{ABC}">
                <Condition logic="has_text">
                    <Function logic="get" param="@{ABC}" />
                </Condition>
            </Function>
        </Function>
    </Label>
    <Label group="ABC">
            ...
    </Label>
</Labels>
```

In the configuration file, you can
* Define as many labels as you want, you can use the same group also
* Define as many definitions as you want, the same definition name will consider as the same set of item even separate in the configuration
* Define as many functions as you want, you can decorate the function but the execute order depends on your implementation.
All functions that we provide will execute the deepest functions first. 
Nevertheless, it's a good practice to execute the deepest function and condition first if you create functions and/or conditions yourselves.
* Define as many conditions as you want, but depends on the implementation 
* Define as many functions as you want, but depends on the implementation

Configuration file tips
* Specify properties using format **${}**. 
  * The **${ABC.DEF}** refers to properties key **ABC.DEF**
  * Properties value will get from System properties, so you have to load all properties first
* Specify data using format **@{}**. 
  * The **@{name}** refers to method **getName()** of the object input
* Specify definition using format **#{}**. 
  * The **#{XYZ}** refers to definition name **XYZ** 
  * The **#{XYZ.ABC}** refers to definition key **ABC** of definition name **XYZ** 
* Add a the new line using ${line.separator}
---

### Customize Function
You can create a new function yourselves by
1. creating a new class and implement Function interface
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
* **Ignore the definition.getLogic()**, it is use to select the right implementation from meta data
* The value1, and value2 refer to the parameter1 and parameter2 in the configuration respectively
* You don't need to define class to be public, but you need a public constructor
---

### Customize Condition 
You can create a new condition yourselves by 
1. creating a new class and implement Condition interface
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
* **Ignore the definition.getLogic()**, it is use to select the right implementation from meta data
* The value1, and value2 refer to the parameter1 and parameter2 in the configuration respectively
* You don't need to define class to be public, but you need a public constructor
* org.companion.impresario.VariableReflector can help you when
   * You want value from object
   * You want value of properties
   * You want the definition or the value of particular definition key

---

### How to use
In order to use this library, you need to load both meta data and configuration first, as the code below

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
Object data = new MyDataObject();

LabelGenerator labelGenerator = labelGenerators.get("ABC")
String result = labelGenerator.labelOf(data);
```

---

### Available Function
    org.companion.impresario.FunctionGet

Returns value from the specific definition, properties, specific field, or the value itself corresponds to the configuration.

---
    org.companion.impresario.FunctionConcat

Returns value after concatenate string from all functions together

---
    org.companion.impresario.FunctionReplace

Returns value after replace specific strings by the specific strings of definition

---
    org.companion.impresario.FunctionLength

Returns length of the string

---
    org.companion.impresario.FunctionUpper

Returns value after converts string to upper case

---
    org.companion.impresario.FunctionLower
    
Returns value after converts string to lower case

---
    org.companion.impresario.FunctionChoose

Returns the first executable value among multiple functions

---
    org.companion.impresario.FunctionSubstring

* The negative index (-X): return since first character until the last X character exclude the last character e.g 9876543 substring -3 = 9876
* The positive index (+X): return since character X to the last character e.g. 123456 substring 2 = 3456

---
    org.companion.impresario.FunctionCutOff

 * The negative index (-X): return last X character e.g 9876543 cut off -3 = 543
 * The positive index (+X): return first X character e.g. 123456 cut off 2 = 12

---
    org.companion.impresario.FunctionCharAt

Returns a character at the specific index

---

### Available Condition

    org.companion.impresario.ConditionOr

Returns true if one of all conditions is true, otherwise false

---
    org.companion.impresario.ConditionAnd

Returns true if all conditions are true, otherwise false

---
    org.companion.impresario.ConditionEquals

Returns true if 2 parameters are consider equals, otherwise false

---
    org.companion.impresario.ConditionNotEquals

Returns true if 2 parameters are consider not equals, otherwise false

---
    org.companion.impresario.ConditionLessThan

Returns true if parameter1 < parameter2, otherwise false

---
    org.companion.impresario.ConditionLessThanEquals
    
Returns true if parameter1 <= parameter2, otherwise false

---
    org.companion.impresario.ConditionGreaterThan

Returns true if parameter1 > parameter2, otherwise false

---
    org.companion.impresario.ConditionGreaterThanEquals

Returns true if parameter1 >= parameter2, otherwise false

---
    org.companion.impresario.ConditionIsNull

Returns true if parameter1 is null, otherwise false

---
    org.companion.impresario.ConditionIsNotNull

Returns true if parameter1 is not null, otherwise false

---
    org.companion.impresario.ConditionHasText

Returns true if parameter1 has text (length > 0), otherwise false

---
    org.companion.impresario.ConditionHasNoText

Returns true if parameter1 has no text (null or length = 0), otherwise false

---
    org.companion.impresario.ConditionIsLetter
    
Returns true the whole strings has only letter, otherwise false

---
    org.companion.impresario.ConditionNot

Returns the opposite result of a condition