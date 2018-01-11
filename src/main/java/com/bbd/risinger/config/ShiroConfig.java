package com.bbd.risinger.config;

import com.bbd.risinger.common.security.shiro.session.CacheSessionDAO;
import com.bbd.risinger.modules.sys.security.FormAuthenticationFilter;
import com.bbd.risinger.modules.sys.security.SystemAuthorizingRealm;
import com.google.common.collect.Maps;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shrio配置类
 * @author	zzp
 */
@Component
public class ShiroConfig {

    /**
     * 全局的环境变量的设置
     * shiro的拦截
     *
     * @param environment
     * @param adminPath
     * @return
     */
    @Bean(name = "shiroFilterChainDefinitions")
    public LinkedHashMap<String, String> shiroFilterChainDefinitions(Environment environment, @Value("${adminPath}") String adminPath) {
        Global.resolver = new RelaxedPropertyResolver(environment);
        // 配置访问权限
        LinkedHashMap<String, String> linkedHashMap = Maps.newLinkedHashMap();
        linkedHashMap.put("/static/**", "anon");
        linkedHashMap.put("/userfiles/**", "anon");
        linkedHashMap.put(adminPath + "/login", "authc");
        linkedHashMap.put(adminPath + "/logout", "logout");
        linkedHashMap.put(adminPath + "/**", "user");
        linkedHashMap.put("/act/editor/**", "user");
        linkedHashMap.put("/ReportServer/**", "user");
        return linkedHashMap;
    }

    @Bean(name = "basicHttpAuthenticationFilter")
    public BasicHttpAuthenticationFilter casFilter(@Value("${adminPath}") String adminPath) {
        BasicHttpAuthenticationFilter basicHttpAuthenticationFilter = new BasicHttpAuthenticationFilter();
        basicHttpAuthenticationFilter.setLoginUrl(adminPath + "/login");
        return basicHttpAuthenticationFilter;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            @Value("${adminPath:/a}") String adminPath,
            BasicHttpAuthenticationFilter basicHttpAuthenticationFilter,
            FormAuthenticationFilter formAuthenticationFilter,
            DefaultWebSecurityManager securityManager,
            @Qualifier("shiroFilterChainDefinitions") LinkedHashMap<String, String> shiroFilterChainDefinitions) {
        Map<String, Filter> filters = new HashMap<>();
        filters.put("basic", basicHttpAuthenticationFilter);
        filters.put("authc", formAuthenticationFilter);
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setFilters(filters);
        bean.setSecurityManager(securityManager);
        bean.setLoginUrl(adminPath + "/login");
        bean.setSuccessUrl(adminPath + "?login");
        bean.setFilterChainDefinitionMap(shiroFilterChainDefinitions);
        return bean;
    }

//    @Bean(name = "shiroCacheManager")
//    public EhCacheManager shiroCacheManager(CacheManager manager) {
//        EhCacheManager ehCacheManager = new EhCacheManager();
//        ehCacheManager.setCacheManager(manager);
//        return ehCacheManager;
//    }

    @Bean(name = "sessionManager")
    public DefaultWebSessionManager sessionManager(
    		CacheSessionDAO dao,
    		@Value("${session.sessionTimeout}") Long sessionTimeout, 
    		@Value("${session.sessionTimeoutClean}") Long sessionValidationInterval, 
    		@Value("${session.simpleCookie}") String simpleCookie,
            ShrioRedisCacheManager redisCacheManager) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setCacheManager(redisCacheManager);
        sessionManager.setSessionDAO(dao);
        sessionManager.setGlobalSessionTimeout(sessionTimeout);
        sessionManager.setSessionValidationInterval(sessionValidationInterval);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookie(new SimpleCookie(simpleCookie));
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(
            SystemAuthorizingRealm systemAuthorizingRealm,
            DefaultWebSessionManager sessionManager,
            ShrioRedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setSessionManager(sessionManager);
        defaultWebSecurityManager.setCacheManager(redisCacheManager);
        defaultWebSecurityManager.setRealm(systemAuthorizingRealm);
        return defaultWebSecurityManager;
    }

    @Bean(name="shrioRedisCacheManager")
    @DependsOn(value="redisTemplate")
    public ShrioRedisCacheManager redisCacheManager(RedisTemplate redisTemplate) {
        ShrioRedisCacheManager cacheManager = new ShrioRedisCacheManager(redisTemplate);
        return cacheManager;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            DefaultWebSecurityManager defaultWebSecurityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }
}
