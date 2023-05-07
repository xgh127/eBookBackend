package com.zzq.ebook.interceptor;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.FilterConfig;


/**
 * @Author : zzq
 * @Description : 简单的测试一下拦截器这个
 * */
@ExtendWith(MockitoExtension.class)
public class interceptorTest {

    @Test
    public void testInterceptor(){
        InterceptorConfig interceptorConfig = new InterceptorConfig();
        CorsFilter corsFilter = interceptorConfig.corsFilter();
        Assertions.assertNotNull(corsFilter);
    }

}
