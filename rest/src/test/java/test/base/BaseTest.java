package test.base;

import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BaseTest extends TestAuthenticationBase
{
    @Test
    public void whenResourceDoesntExist_thenNotFound() throws Exception
    {
        mockMvc.perform(get("/this_url_doesnt_exist"))
                .andExpect(status().isNotFound());
    }
}
