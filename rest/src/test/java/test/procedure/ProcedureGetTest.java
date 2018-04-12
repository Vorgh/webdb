package test.procedure;

import test.base.TestAuthenticationBase;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProcedureGetTest extends TestAuthenticationBase
{
    @Test
    public void whenRequestingAllProcedures_andSuccessful_thenReturnArray() throws Exception
    {
        mockMvc.perform(
                get("/procedure/all")
                        .param("schema", "teszt_schema")
                        .headers(getAuthHeaders()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(status().isOk());
    }

    @Test
    public void whenRequestingSingleProcedure_andSuccessful_thenReturnProcedure() throws Exception
    {
        mockMvc.perform(
                get("/procedure/single")
                        .param("schema", "teszt_schema")
                        .param("procedure", "get_test_procedure")
                        .headers(getAuthHeaders()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.schema").isString())
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.type", equalToIgnoringCase("procedure")))
                .andExpect(jsonPath("$.returnType").doesNotExist())
                .andExpect(jsonPath("$.body").isString())
                .andExpect(jsonPath("$.modified").isString())
                .andExpect(jsonPath("$.paramList", anyOf(nullValue(), hasSize(greaterThan(0)))))
                .andExpect(status().isOk());
    }

    @Test
    public void whenRequestingSingleFunction_andSuccessful_thenReturnFunction() throws Exception
    {
        mockMvc.perform(
                get("/procedure/single")
                        .param("schema", "teszt_schema")
                        .param("procedure", "get_test_function")
                        .headers(getAuthHeaders()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.schema").isString())
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.type", equalToIgnoringCase("function")))
                .andExpect(jsonPath("$.returnType", is(not(isEmptyOrNullString()))))
                .andExpect(jsonPath("$.body").isString())
                .andExpect(jsonPath("$.modified").isString())
                .andExpect(jsonPath("$.paramList", anyOf(nullValue(), hasSize(greaterThan(0)))))
                .andExpect(status().isOk());
    }

    @Test
    public void whenProcedureDoesntExist_thenNotFound() throws Exception
    {
        mockMvc.perform(
                get("/procedure/single")
                        .param("schema", "teszt_schema")
                        .param("procedure", "procedure_that_doesnt_exist")
                        .headers(getAuthHeaders()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenSchemaParamIsMissing_thenBadRequest() throws Exception
    {
        mockMvc.perform(
                get("/procedure/single")
                        .param("procedure", "get_test_procedure")
                        .headers(getAuthHeaders()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenProcedureParamIsMissing_thenBadRequest() throws Exception
    {
        mockMvc.perform(
                get("/procedure/single")
                        .param("schema", "teszt_schema")
                        .headers(getAuthHeaders()))
                .andExpect(status().isBadRequest());
    }


}
