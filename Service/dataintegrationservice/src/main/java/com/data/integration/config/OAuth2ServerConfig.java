package com.data.integration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.data.integration.service.impl.ClientUserDetailsService;
import com.data.integration.service.impl.CustomClientDetailsService;

@Configuration
public class OAuth2ServerConfig {

    public static final String RESOURCEID = "DataIntegrationAPI";

    @Configuration
    @EnableResourceServer
    protected static class ResourceServer extends
            ResourceServerConfigurerAdapter {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/v2/api-docs", "/swagger-resources",
                            "/swagger-ui.html", "/lib/*", "/images/*",
                            "/css/*", "/swagger-ui.js", "/swagger-ui.min.js",
                            "/api-docs", "/fonts/*", "/api-docs/*",
                            "/api-docs/default/*", "/o2c.html", "index.html",
                            "/webjars/**", "/hystrix/**", "/configuration/ui",
                            "/configuration/security", "/login").permitAll()
                    .anyRequest().fullyAuthenticated().and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().httpBasic().and().csrf().disable();
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources)
                throws Exception {
            resources.resourceId(RESOURCEID);
            resources.stateless(true);
        }

    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServer extends
            AuthorizationServerConfigurerAdapter {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private TokenStore tokenStore;

        @Autowired
        private CustomClientDetailsService customClientDetailsService;

        @Autowired
        private ClientUserDetailsService clientUserDetailsService;

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints)
                throws Exception {
            endpoints.authenticationManager(authenticationManager);
            endpoints.tokenStore(tokenStore);
            endpoints.userDetailsService(clientUserDetailsService);

        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security)
                throws Exception {
            security.checkTokenAccess("permitAll()");
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients)
                throws Exception {

            clients.withClientDetails(customClientDetailsService);

        }

    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

}
