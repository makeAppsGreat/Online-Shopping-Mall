package kr.makeappsgreat.onlinemall.user;

import kr.makeappsgreat.onlinemall.config.SecurityConfig;
import kr.makeappsgreat.onlinemall.config.WebConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = AccountController.class,
        includeFilters = @ComponentScan.Filter(classes = SecurityConfig.class, type = FilterType.ASSIGNABLE_TYPE),
        excludeFilters = @ComponentScan.Filter(classes = WebConfig.class, type = FilterType.ASSIGNABLE_TYPE))
class AccountControllerTest {

    private static ResourceBundleMessageSource validationMessageSource;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageSource messageSource;

    @MockBean
    private AccountService<Account> accountService;

    // Given
    private final String username = "user@domain.com";
    private final String duplicatedUsername = "duplicatedUser@domain.com";
    private final String notAWellFormedUsername = "user";

    @BeforeAll
    static void init() {
        validationMessageSource = new ResourceBundleMessageSource();
        validationMessageSource.setBasename("org.hibernate.validator/ValidationMessages");
    }

    @BeforeEach
    void mock() {
        given(accountService.isDuplicatedUser(username)).willReturn(false);
        given(accountService.isDuplicatedUser(duplicatedUsername)).willReturn(true);
    }

    @Test
    void isUsableUsername() throws Exception {
        mockMvc.perform(get("/account/usable-username/" + username))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(nullValue()));
    }

    @Test
    void isUsableUsername_empty_200ButResultIsFalse() throws Exception {
        mockMvc.perform(get("/account/usable-username/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message")
                        .value(
                                validationMessageSource.getMessage(
                                        "javax.validation.constraints.NotBlank.message",
                                        null,
                                        Locale.ENGLISH))
                );
    }

    @Test
    void isUsableUsername_blank_200ButResultIsFalse() throws Exception {
        mockMvc.perform(get("/account/usable-username/" + " "))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(anyOf(
                        is(
                                validationMessageSource.getMessage(
                                        "javax.validation.constraints.NotBlank.message",
                                        null,
                                        Locale.ENGLISH)),
                        is(
                                validationMessageSource.getMessage(
                                        "javax.validation.constraints.Email.message",
                                        null,
                                        Locale.ENGLISH))
                )));
    }

    @Test
    void isUsableUsername_duplicatedUsername_200ButResultIsFalse() throws Exception {
        mockMvc.perform(get("/account/usable-username/" + duplicatedUsername))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.message").value(
                        messageSource.getMessage("account.username.duplicated", null, Locale.getDefault())));
    }

    @Test
    void isUsableUsername_notAWellFormedUsername_200ButResultIsFalse() throws Exception {
        mockMvc.perform(get("/account/usable-username/" + notAWellFormedUsername))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(
                        validationMessageSource.getMessage("javax.validation.constraints.Email.message", null, Locale.ENGLISH)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void isUsableUsername_notAWellFormedUsernameWithAdminAccount_200() throws Exception {
        mockMvc.perform(get("/account/usable-username/" + notAWellFormedUsername))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(nullValue()));
    }
}