package com.xuyufengyy.xmh.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAnthencationProder myAnthencationProder;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // 关闭csrf防护
                .authorizeRequests()
                .antMatchers("/", "/login", "/reset_password/**", "403").permitAll() //访问：/home 无需登录认证权限
                //主页
                .antMatchers("/home/**").hasAuthority("admin:home")
                //网站管理
                .antMatchers("/company_profile/**").hasAuthority("admin:companyProfile")//公司简介
                .antMatchers("/join_us/**").hasAuthority("admin:joinUs")//诚聘英才
                .antMatchers("/news/**").hasAuthority("admin:news")//最新动态
                .antMatchers("/company_programme/**").hasAuthority("admin:companyProgramme")//企业方案
                //系统管理
                .antMatchers("/authority_category/**").hasAuthority("admin:authorityCategory")//权限分类管理
                .antMatchers("/authority/**").hasAuthority("admin:authority")//权限管理
                .antMatchers("/role/**").hasAuthority("admin:role")//角色管理
                .antMatchers("/admin/**").hasAuthority("admin:admin")//管理员管理
                .antMatchers("/area/**").hasAuthority("admin:area")//地区管理
                //产品管理
                .antMatchers("/scenic_spot/**").hasAuthority("admin:scenicSpot")//景区管理
                .antMatchers("/product/**").hasAuthority("admin:product")//产品
//                    .antMatchers("/user/**").hasAnyRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login") // 设置登录页面
                .loginProcessingUrl("/login")   // 登录请求路径
                .defaultSuccessUrl("/home")     //登录成功后默认跳转地址
//                    .failureUrl("/login?error=true") //如果不设置error为空，登录页就要判断session.SPRING_SECURITY_LAST_EXCEPTION
                .and()
                .rememberMe()//登录后记住用户，下次自动登录,数据库中必须存在名为persistent_logins的表
                .tokenValiditySeconds(604800) //记住我功能，cookies有限期是一周
                .rememberMeParameter("remember-me") //登陆时是否激活记住我功能的参数名字，在登陆页面有展示
                .rememberMeCookieName("workspace") //cookies的名字，登陆后可以通过浏览器查看cookies名字
                .and()
                .logout()
//                  .deleteCookies("remove")
//                  .invalidateHttpSession(false) //默认为true,用户在退出后Http session失效
                //添加退出成功地址，会进行地址跳转，不添加，成功地址为login?logout
//                    .logoutUrl("/logout")           //登录退出地址
//                    .logoutSuccessUrl("/login")           //登录退出成功地址
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(myAnthencationProder);
    }

    @Override
    public void configure(WebSecurity web) {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/assets/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.addDialect(new SpringSecurityDialect());//注册安全方言
        return engine;
    }
}