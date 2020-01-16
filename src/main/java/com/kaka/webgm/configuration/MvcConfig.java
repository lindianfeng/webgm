package com.kaka.webgm.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

import java.util.Locale;

/**
 * @author ylxia
 * @version 1.0
 * @package com.iyerka.server.config
 * @date 九月
 */
@Configuration
//@EnableWebMvc
public class MvcConfig extends WebMvcConfigurerAdapter {


    /**
     * 不需要拦截处理的URI
     */
    public static final String[] IGNORE_URIS = {
            "/error/**",
            "/kaptcha/**",
            "/auth/**",
            "/login",
            "/signin/**",
            "/signup/**",
    };


    /**
     * 过滤资源
     */
    public static final String[] IGNORE_RESOURCES = {
            "/webjars/**",
            "/dist/**",
            "/bower_components/**",
            "/**/*.css",
            "/**/*.png",
            "/**/*.gif",
            "/**/*.jpg",
            "/**/*.js"
    };


    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.CHINA);
        return resolver;
    }


    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(localeChangeInterceptor());
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        super.addViewControllers(registry);
        registry.addViewController("/").setViewName("index");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public UrlTemplateResolver urlTemplateResolver() {
        UrlTemplateResolver urlTemplateResolver = new UrlTemplateResolver();
        urlTemplateResolver.setOrder(20);
        return urlTemplateResolver;
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }


}