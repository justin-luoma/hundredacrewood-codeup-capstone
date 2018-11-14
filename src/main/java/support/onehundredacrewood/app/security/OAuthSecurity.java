package support.onehundredacrewood.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Order(1)
@Configuration
public class OAuthSecurity extends WebSecurityConfigurerAdapter {
    @Bean
    AuthenticationSuccessHandler oauthSuccessHandler() {
        return new OAuthAuthSuccessHandler();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/oauth2/**")
                .oauth2Login()
                    .redirectionEndpoint()
                    .baseUri("/oauth2/login/oauth2/code/*")
                .and()
                .loginPage("/oauth2/login")
                .defaultSuccessUrl("/oauth2/success")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth2/registering")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/oauth2/register")
                .authenticated();
//                .successHandler(oauthSuccessHandler());
    }
}
