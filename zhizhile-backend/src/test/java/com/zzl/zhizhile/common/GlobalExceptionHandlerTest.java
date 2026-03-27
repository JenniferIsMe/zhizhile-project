package com.zzl.zhizhile.common;

import com.zzl.zhizhile.common.constant.ErrorCode;
import com.zzl.zhizhile.common.exception.BizException;
import com.zzl.zhizhile.common.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    @Test
    void shouldHandleBizException() throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new DemoController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mvc.perform(get("/biz").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("PROJECT_NOT_FOUND"));
    }

    @Test
    void shouldHandleSystemException() throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new DemoController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mvc.perform(get("/sys").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"));
    }

    @RestController
    static class DemoController {
        @GetMapping("/biz")
        public String biz() {
            throw new BizException(ErrorCode.PROJECT_NOT_FOUND);
        }

        @GetMapping("/sys")
        public String sys() {
            throw new RuntimeException("boom");
        }
    }
}
