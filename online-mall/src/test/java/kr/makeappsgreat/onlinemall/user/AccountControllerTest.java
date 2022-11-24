package kr.makeappsgreat.onlinemall.user;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.DisplayName.class)
class AccountControllerTest {

    @Autowired
    MessageSource messageSource;

    @Autowired
    MockMvc mockMvc;

    private AccountUserDetails admin = new AccountUserDetails(
            Account.builder()
                    .username("simpleadmin")
                    .password("simple")
                    .roles(Set.of(AccountRole.ROLE_ADMIN))
                    .build());

    @Test
    public void isUsableUsername_properRequest_200() throws Exception {
        // Given
        String username = "makeappsgreat@simple.com";

        // When & Then
        MvcResult mvcResult = mockMvc.perform(get("/account/usable-username/" + username))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/account/usable-username/" + username + "/response"))
                .andReturn();

        mockMvc.perform(get(mvcResult.getResponse().getForwardedUrl()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    public void isUsableUsername_notAEmailWithAdmin_200() throws Exception {
        // Given
        String username = "makeappsgreat";

        // When & Then
        MvcResult mvcResult = mockMvc.perform(get("/account/usable-username/" + username)
                        .with(user(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/account/usable-username/" + username + "/response"))
                .andReturn();

        mockMvc.perform(get(mvcResult.getResponse().getForwardedUrl()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    public void isUsableUsername_duplicatedUsername_200ButResultIsFalse() throws Exception {
        // Given
        String username = "makeappsgreat@gmail.com";

        // When & Then
        MvcResult mvcResult = mockMvc.perform(get("/account/usable-username/" + username))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/account/usable-username/" + username + "/response"))
                .andReturn();

        mockMvc.perform(get(mvcResult.getResponse().getForwardedUrl()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.message").value(messageSource.getMessage(
                        "account.duplicated-username", null, Locale.getDefault())
                ));
    }

    @Test
    public void isUsableUsername_notAEmail_400() throws Exception {
        // Given
        String username = "makeappsgreat";

        MvcResult mvcResult = mockMvc.perform(get("/account/usable-username/" + username))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/account/error"))
                .andReturn();
        Map<String, Object> forwardedModel = mvcResult.getModelAndView().getModel();

        // When & Then
        mockMvc.perform(get(mvcResult.getResponse().getForwardedUrl())
                        .requestAttr("exception", forwardedModel.get("exception"))
                        .requestAttr("request", forwardedModel.get("request")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("must be a well-formed email address"));
    }

    @Test
    public void isUsableUsername_empty_400() throws Exception {
        // Given
        String username = "";

        MvcResult mvcResult = mockMvc.perform(get("/account/usable-username/" + username))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/account/error"))
                .andReturn();
        Map<String, Object> forwardedModel = mvcResult.getModelAndView().getModel();

        // When & Then
        mockMvc.perform(get(mvcResult.getResponse().getForwardedUrl())
                        .requestAttr("exception", forwardedModel.get("exception"))
                        .requestAttr("request", forwardedModel.get("request")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("must not be empty"));
    }

    @Test
    public void isUsableUsername_blank_400() throws Exception {
        // Given
        String username = " ";

        MvcResult mvcResult = mockMvc.perform(get("/account/usable-username/" + username))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/account/error"))
                .andReturn();
        Map<String, Object> forwardedModel = mvcResult.getModelAndView().getModel();

        // When & Then
        mockMvc.perform(get(mvcResult.getResponse().getForwardedUrl())
                        .requestAttr("exception", forwardedModel.get("exception"))
                        .requestAttr("request", forwardedModel.get("request")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("must be a well-formed email address"));
    }
}