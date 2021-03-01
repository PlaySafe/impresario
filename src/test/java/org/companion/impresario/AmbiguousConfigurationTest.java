package org.companion.impresario;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AmbiguousConfigurationTest {

    private final LabelGeneratorFactory labelGeneratorFactory;
    private final Map<String, String> configAndErrorMap = new HashMap<String, String>() {{
        put("src/test/resources/ambiguous/add/ambiguous_add_miss_pre_function.xml", "FunctionAdd should not accept missing pre-function");
        put("src/test/resources/ambiguous/add/ambiguous_add_single_pre_function.xml", "FunctionAdd should not accept single pre-function");

        put("src/test/resources/ambiguous/and/ambiguous_and_miss_pre_condition.xml", "ConditionAnd should not accept missing pre-condition");
        put("src/test/resources/ambiguous/and/ambiguous_and_single_pre_condition.xml", "ConditionAnd should not accept single pre-condition");

        put("src/test/resources/ambiguous/char_at/ambiguous_char_at_duplicate_result_as_position.xml", "FunctionCharAt should not accept duplicate 'result-as=position'");
        put("src/test/resources/ambiguous/char_at/ambiguous_char_at_duplicate_result_as_text.xml", "FunctionCharAt should not accept duplicate 'result-as=text'");
        put("src/test/resources/ambiguous/char_at/ambiguous_char_at_miss_result_as_position.xml", "FunctionCharAt should not accept missing 'result-as=position'");
        put("src/test/resources/ambiguous/char_at/ambiguous_char_at_miss_result_as_text.xml", "FunctionCharAt should not accept missing 'result-as=text'");

        put("src/test/resources/ambiguous/choose/ambiguous_choose_miss_pre_function.xml", "FunctionChoose should not accept missing pre-function");
        put("src/test/resources/ambiguous/choose/ambiguous_choose_single_pre_function.xml", "FunctionChoose should not accept single pre-function");

        put("src/test/resources/ambiguous/concat/ambiguous_concat_miss_pre_function.xml", "FunctionConcat should not accept missing pre-function");
        put("src/test/resources/ambiguous/concat/ambiguous_concat_single_pre_function.xml", "FunctionConcat should not accept single pre-function");

        put("src/test/resources/ambiguous/cut_off/ambiguous_cut_off_duplicate_result_as_position.xml", "FunctionCutOff should not accept duplicate 'result-as=position'");
        put("src/test/resources/ambiguous/cut_off/ambiguous_cut_off_duplicate_result_as_text.xml", "FunctionCutOff should not accept duplicate 'result-as=text'");
        put("src/test/resources/ambiguous/cut_off/ambiguous_cut_off_miss_result_as_position.xml", "FunctionCutOff should not accept missing 'result-as=position'");
        put("src/test/resources/ambiguous/cut_off/ambiguous_cut_off_miss_result_as_text.xml", "FunctionCutOff should not accept missing 'result-as=text'");

        put("src/test/resources/ambiguous/division/ambiguous_division_duplicate_result_as_number.xml", "FunctionDivision should not accept duplicate 'result-as=number'");
        put("src/test/resources/ambiguous/division/ambiguous_division_miss_result_as_divisor.xml", "FunctionDivision should not accept missing 'result-as=divisor'");
        put("src/test/resources/ambiguous/division/ambiguous_division_miss_result_as_number.xml", "FunctionDivision should not accept missing 'result-as=number'");

        put("src/test/resources/ambiguous/equals/ambiguous_equals_duplicate_result_as_x.xml", "ConditionEquals should not accept duplicate 'result-as=x'");
        put("src/test/resources/ambiguous/equals/ambiguous_equals_duplicate_result_as_y.xml", "ConditionEquals should not accept duplicate 'result-as=y'");
        put("src/test/resources/ambiguous/equals/ambiguous_equals_miss_result_as_x.xml", "ConditionEquals should not accept missing 'result-as=x'");
        put("src/test/resources/ambiguous/equals/ambiguous_equals_miss_result_as_y.xml", "ConditionEquals should not accept missing 'result-as=y'");

        put("src/test/resources/ambiguous/equals_ignore_case/ambiguous_equals_ignore_case_duplicate_result_as_x.xml", "ConditionEqualsIgnoreCase should not accept duplicate 'result-as=x'");
        put("src/test/resources/ambiguous/equals_ignore_case/ambiguous_equals_ignore_case_duplicate_result_as_y.xml", "ConditionEqualsIgnoreCase should not accept duplicate 'result-as=y'");
        put("src/test/resources/ambiguous/equals_ignore_case/ambiguous_equals_ignore_case_miss_result_as_x.xml", "ConditionEqualsIgnoreCase should not accept missing 'result-as=x'");
        put("src/test/resources/ambiguous/equals_ignore_case/ambiguous_equals_ignore_case_miss_result_as_y.xml", "ConditionEqualsIgnoreCase should not accept missing 'result-as=y'");

        put("src/test/resources/ambiguous/exponential/ambiguous_exponential_duplicate_result_as_base.xml", "FunctionExponential should not accept duplicate 'result-as=base'");
        put("src/test/resources/ambiguous/exponential/ambiguous_exponential_miss_result_as_base.xml", "FunctionExponential should not accept missing 'result-as=base'");
        put("src/test/resources/ambiguous/exponential/ambiguous_exponential_miss_result_as_expo.xml", "FunctionExponential should not accept missing 'result-as=expo'");

        put("src/test/resources/ambiguous/greater_than/ambiguous_greater_than_duplicate_result_as_x.xml", "ConditionGreaterThan should not accept duplicate 'result-as=x'");
        put("src/test/resources/ambiguous/greater_than/ambiguous_greater_than_duplicate_result_as_y.xml", "ConditionGreaterThan should not accept duplicate 'result-as=y'");
        put("src/test/resources/ambiguous/greater_than/ambiguous_greater_than_miss_result_as_x.xml", "ConditionGreaterThan should not accept missing 'result-as=x'");
        put("src/test/resources/ambiguous/greater_than/ambiguous_greater_than_miss_result_as_y.xml", "ConditionGreaterThan should not accept missing 'result-as=y'");

        put("src/test/resources/ambiguous/greater_than_equals/ambiguous_greater_than_equals_duplicate_result_as_x.xml", "ConditionGreaterThanOrEquals should not accept duplicate 'result-as=x'");
        put("src/test/resources/ambiguous/greater_than_equals/ambiguous_greater_than_equals_duplicate_result_as_y.xml", "ConditionGreaterThanOrEquals should not accept duplicate 'result-as=y'");
        put("src/test/resources/ambiguous/greater_than_equals/ambiguous_greater_than_equals_miss_result_as_x.xml", "ConditionGreaterThanOrEquals should not accept missing 'result-as=x'");
        put("src/test/resources/ambiguous/greater_than_equals/ambiguous_greater_than_equals_miss_result_as_y.xml", "ConditionGreaterThanOrEquals should not accept missing 'result-as=y'");

        put("src/test/resources/ambiguous/has_no_text/ambiguous_has_no_text_miss_pre_condition.xml", "ConditionHasNoText should not accept missing pre-function");
        put("src/test/resources/ambiguous/has_no_text/ambiguous_has_no_text_multiple_pre_conditions.xml", "ConditionHasNoText should not accept multiple pre-functions");

        put("src/test/resources/ambiguous/has_text/ambiguous_has_text_miss_pre_condition.xml", "ConditionHasText should not accept missing pre-function");
        put("src/test/resources/ambiguous/has_text/ambiguous_has_text_multiple_pre_conditions.xml", "ConditionHasText should not accept multiple pre-functions");

        put("src/test/resources/ambiguous/is_letter/ambiguous_is_letter_miss_pre_condition.xml", "ConditionIsLetter should not accept missing pre-function");
        put("src/test/resources/ambiguous/is_letter/ambiguous_is_letter_multiple_pre_conditions.xml", "ConditionIsLetter should not accept multiple pre-functions");

        put("src/test/resources/ambiguous/is_not_null/ambiguous_is_not_null_miss_pre_condition.xml", "ConditionIsNotNull should not accept missing pre-function");
        put("src/test/resources/ambiguous/is_not_null/ambiguous_is_not_null_multiple_pre_conditions.xml", "ConditionIsNotNull should not accept multiple pre-functions");

        put("src/test/resources/ambiguous/is_null/ambiguous_is_null_miss_pre_condition.xml", "ConditionIsNull should not accept missing pre-function");
        put("src/test/resources/ambiguous/is_null/ambiguous_is_null_multiple_pre_conditions.xml", "ConditionIsNull should not accept multiple pre-functions");


        put("src/test/resources/ambiguous/join/ambiguous_join_duplicate_result_as_delimiter.xml", "FunctionJoin should not accept duplicate 'result-as=delimiter'");
        put("src/test/resources/ambiguous/join/ambiguous_join_miss_result_as_delimiter.xml", "FunctionJoin should not accept missing 'result-as=delimiter'");
        put("src/test/resources/ambiguous/join/ambiguous_join_miss_result_as_text.xml", "FunctionJoin should not accept missing 'result-as=text'");
        put("src/test/resources/ambiguous/join/ambiguous_join_single_result_as_text.xml", "FunctionJoin should not accept single 'result-as=text'");

        put("src/test/resources/ambiguous/length/ambiguous_length_multiple_pre_functions.xml", "FunctionLength should not accept multiple pre-functions");
        put("src/test/resources/ambiguous/length/ambiguous_length_no_pre_function.xml", "FunctionLength should not accept 0 pre-function");

        put("src/test/resources/ambiguous/less_than/ambiguous_less_than_duplicate_result_as_x.xml", "ConditionLessThan should not accept duplicate 'result-as=x'");
        put("src/test/resources/ambiguous/less_than/ambiguous_less_than_duplicate_result_as_y.xml", "ConditionLessThan should not accept duplicate 'result-as=y'");
        put("src/test/resources/ambiguous/less_than/ambiguous_less_than_miss_result_as_x.xml", "ConditionLessThan should not accept missing 'result-as=x'");
        put("src/test/resources/ambiguous/less_than/ambiguous_less_than_miss_result_as_y.xml", "ConditionLessThan should not accept missing 'result-as=y'");

        put("src/test/resources/ambiguous/less_than_equals/ambiguous_less_than_equals_duplicate_result_as_x.xml", "ConditionLessThanOrEquals should not accept duplicate 'result-as=x'");
        put("src/test/resources/ambiguous/less_than_equals/ambiguous_less_than_equals_duplicate_result_as_y.xml", "ConditionLessThanOrEquals should not accept duplicate 'result-as=y'");
        put("src/test/resources/ambiguous/less_than_equals/ambiguous_less_than_equals_miss_result_as_x.xml", "ConditionLessThanOrEquals should not accept missing 'result-as=x'");
        put("src/test/resources/ambiguous/less_than_equals/ambiguous_less_than_equals_miss_result_as_y.xml", "ConditionLessThanOrEquals should not accept missing 'result-as=y'");

        put("src/test/resources/ambiguous/lower/ambiguous_lower_miss_pre_function.xml", "FunctionLower should not accept missing pre-function");
        put("src/test/resources/ambiguous/lower/ambiguous_lower_multiple_pre_functions.xml", "FunctionLower should not accept multiple pre-functions");

        put("src/test/resources/ambiguous/modulo/ambiguous_modulo_duplicate_result_as_number.xml", "FunctionModulo should not accept duplicate 'result-as=number'");
        put("src/test/resources/ambiguous/modulo/ambiguous_modulo_miss_result_as_divisor.xml", "FunctionModulo should not accept single pre-function");
        put("src/test/resources/ambiguous/modulo/ambiguous_modulo_miss_result_as_number.xml", "FunctionModulo should not accept missing 'result-as=number'");

        put("src/test/resources/ambiguous/multiplication/ambiguous_multiplication_miss_pre_function.xml", "FunctionMultiplication should not accept missing pre-function");
        put("src/test/resources/ambiguous/multiplication/ambiguous_multiplication_single_pre_function.xml", "FunctionMultiplication should not accept single pre-function");

        put("src/test/resources/ambiguous/not/ambiguous_not_multiple_pre_condition.xml", "ConditionNot should not accept multiple pre-condition");

        put("src/test/resources/ambiguous/not_equals/ambiguous_not_equals_duplicate_result_as_x.xml", "ConditionNotEquals should not accept duplicate 'result-as=x'");
        put("src/test/resources/ambiguous/not_equals/ambiguous_not_equals_duplicate_result_as_y.xml", "ConditionNotEquals should not accept duplicate 'result-as=y'");
        put("src/test/resources/ambiguous/not_equals/ambiguous_not_equals_miss_result_as_x.xml", "ConditionNotEquals should not accept missing 'result-as=x'");
        put("src/test/resources/ambiguous/not_equals/ambiguous_not_equals_miss_result_as_y.xml", "ConditionNotEquals should not accept missing 'result-as=y'");

        put("src/test/resources/ambiguous/or/ambiguous_or_miss_pre_condition.xml", "ConditionOr should not accept missing pre-condition");
        put("src/test/resources/ambiguous/or/ambiguous_or_single_pre_condition.xml", "ConditionOr should not accept single pre-condition");

        put("src/test/resources/ambiguous/replace_all/ambiguous_replace_all_duplicate_result_as_replace_values.xml", "FunctionReplaceAll should not accept duplicate 'result-as=replace_values'");
        put("src/test/resources/ambiguous/replace_all/ambiguous_replace_all_duplicate_result_as_text.xml", "FunctionReplaceAll should not accept duplicate 'result-as=text'");
        put("src/test/resources/ambiguous/replace_all/ambiguous_replace_all_miss_result_as_replace_values.xml", "FunctionReplaceAll should not accept missing 'result-as=replace_values'");
        put("src/test/resources/ambiguous/replace_all/ambiguous_replace_all_miss_result_as_text.xml", "FunctionReplaceAll should not accept missing 'result-as=text'");

        put("src/test/resources/ambiguous/replace_one/ambiguous_replace_one_duplicate_result_as_replace_target.xml", "FunctionReplaceOne should not accept duplicate 'result-as=replace_target'");
        put("src/test/resources/ambiguous/replace_one/ambiguous_replace_one_duplicate_result_as_replace_value.xml", "FunctionReplaceOne should not accept duplicate 'result-as=replace_value'");
        put("src/test/resources/ambiguous/replace_one/ambiguous_replace_one_duplicate_result_as_text.xml", "FunctionReplaceOne should not accept duplicate 'result-as=text'");
        put("src/test/resources/ambiguous/replace_one/ambiguous_replace_one_miss_result_as_replace_target.xml", "FunctionReplaceOne should not accept missing 'result-as=replace_target'");
        put("src/test/resources/ambiguous/replace_one/ambiguous_replace_one_miss_result_as_replace_value.xml", "FunctionReplaceOne should not accept missing 'result-as=replace_value'");
        put("src/test/resources/ambiguous/replace_one/ambiguous_replace_one_miss_result_as_text.xml", "FunctionReplaceOne should not accept missing 'result-as=text'");

        put("src/test/resources/ambiguous/round_down/ambiguous_round_down_duplicate_result_as_scale.xml", "FunctionRoundDown should not accept duplicate 'result-as=scale'");
        put("src/test/resources/ambiguous/round_down/ambiguous_round_down_duplicate_result_as_value.xml", "FunctionRoundDown should not accept duplicate 'result-as=value'");
        put("src/test/resources/ambiguous/round_down/ambiguous_round_down_miss_result_as_scale.xml", "FunctionRoundDown should not accept missing 'result-as=scale'");
        put("src/test/resources/ambiguous/round_down/ambiguous_round_down_miss_result_as_value.xml", "FunctionRoundDown should not accept missing 'result-as=value'");

        put("src/test/resources/ambiguous/round_half_down/ambiguous_round_half_down_duplicate_result_as_scale.xml", "FunctionRoundHalfDown should not accept duplicate 'result-as=scale'");
        put("src/test/resources/ambiguous/round_half_down/ambiguous_round_half_down_duplicate_result_as_value.xml", "FunctionRoundHalfDown should not accept duplicate 'result-as=value'");
        put("src/test/resources/ambiguous/round_half_down/ambiguous_round_half_down_miss_result_as_scale.xml", "FunctionRoundHalfDown should not accept missing 'result-as=scale'");
        put("src/test/resources/ambiguous/round_half_down/ambiguous_round_half_down_miss_result_as_value.xml", "FunctionRoundHalfDown should not accept missing 'result-as=value'");

        put("src/test/resources/ambiguous/round_half_up/ambiguous_round_half_up_duplicate_result_as_scale.xml", "FunctionRoundHalfUp should not accept duplicate 'result-as=scale'");
        put("src/test/resources/ambiguous/round_half_up/ambiguous_round_half_up_duplicate_result_as_value.xml", "FunctionRoundHalfUp should not accept duplicate 'result-as=value'");
        put("src/test/resources/ambiguous/round_half_up/ambiguous_round_half_up_miss_result_as_scale.xml", "FunctionRoundHalfUp should not accept missing 'result-as=scale'");
        put("src/test/resources/ambiguous/round_half_up/ambiguous_round_half_up_miss_result_as_value.xml", "FunctionRoundHalfUp should not accept missing 'result-as=value'");

        put("src/test/resources/ambiguous/round_up/ambiguous_round_up_duplicate_result_as_scale.xml", "FunctionRoundUp should not accept duplicate 'result-as=scale'");
        put("src/test/resources/ambiguous/round_up/ambiguous_round_up_duplicate_result_as_value.xml", "FunctionRoundUp should not accept duplicate 'result-as=value'");
        put("src/test/resources/ambiguous/round_up/ambiguous_round_up_miss_result_as_scale.xml", "FunctionRoundUp should not accept missing 'result-as=scale'");
        put("src/test/resources/ambiguous/round_up/ambiguous_round_up_miss_result_as_value.xml", "FunctionRoundUp should not accept missing 'result-as=value'");

        put("src/test/resources/ambiguous/substring/ambiguous_substring_duplicate_result_as_position.xml", "FunctionSubstring should not accept duplicate 'result-as=position'");
        put("src/test/resources/ambiguous/substring/ambiguous_substring_duplicate_result_as_text.xml", "FunctionSubstring should not accept duplicate 'result-as=text'");
        put("src/test/resources/ambiguous/substring/ambiguous_substring_miss_result_as_position.xml", "FunctionSubstring should not accept missing 'result-as=position'");
        put("src/test/resources/ambiguous/substring/ambiguous_substring_miss_result_as_text.xml", "FunctionSubstring should not accept missing 'result-as=text'");

        put("src/test/resources/ambiguous/subtraction/ambiguous_subtraction_duplicate_result_as_number.xml", "FunctionSubtraction should not accept duplicate 'result-as=number'");
        put("src/test/resources/ambiguous/subtraction/ambiguous_subtraction_miss_result_as_number.xml", "FunctionSubtraction should not accept missing 'result-as=number'");
        put("src/test/resources/ambiguous/subtraction/ambiguous_subtraction_miss_result_as_subtrahend.xml", "FunctionSubtraction should not accept missing 'result-as=subtrahend'");

        put("src/test/resources/ambiguous/upper/ambiguous_upper_multiple_pre_functions.xml", "FunctionUpper should not accept multiple pre-functions");
        put("src/test/resources/ambiguous/upper/ambiguous_upper_miss_pre_function.xml", "FunctionUpper should not accept missing pre-function");
    }};

    public AmbiguousConfigurationTest() throws IOException {
        File metaResource = new File("src/test/resources/meta_data.xml");
        MetaData metaData = new MetaDataFactory().compile(metaResource);
        LabelGeneratorFactory labelGeneratorFactory = new SingleLabelGeneratorFactory(metaData);
        Assert.assertNotNull(labelGeneratorFactory);
        this.labelGeneratorFactory = labelGeneratorFactory;
    }


    @Test
    public void expectAmbiguousCases() throws IOException {
        Assert.assertNotEquals(0, configAndErrorMap.size());
        for (Map.Entry<String, String> config : configAndErrorMap.entrySet()) {
            File configResource = new File(config.getKey());
            Assert.assertThrows(InvalidConfigurationException.class, () -> {
                labelGeneratorFactory.compile(configResource);
            });
        }
    }

}
