package com.xuyufengyy.xmh.security;

import com.xuyufengyy.xmh.Admin;
import com.xuyufengyy.xmh.AdminService;
import com.xuyufengyy.xmh.Authority;
import com.xuyufengyy.xmh.Role;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class MyAnthencationProder implements AuthenticationProvider {

    @Autowired
    private AdminService adminService;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        String remoteAddress = null;
        Object details = authentication.getDetails();
        if (details instanceof WebAuthenticationDetails) {
            WebAuthenticationDetails webDetails = (WebAuthenticationDetails) details;
            remoteAddress = webDetails.getRemoteAddress();
        }

        Admin admin = adminService.findByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        if (!admin.getIsEnabled()) {
            throw new DisabledException("用户已被禁用");
        }

//        if(userDetails == null) {
//            throw new UsernameNotFoundException("用户名/密码无效");
//        }else if (!userDetails.isEnabled()){
//            throw new DisabledException("用户已被禁用");
//        }else if (!userDetails.isAccountNonExpired()) {
//            throw new AccountExpiredException("账号已过期");
//        }else if (!userDetails.isAccountNonLocked()) {
//            throw new LockedException("账号已被锁定");
//        }else if (!userDetails.isCredentialsNonExpired()) {
//            throw new LockedException("凭证已过期");
//        }


        if (admin.getIsLocked()) {
            Date unlockDate = DateUtils.addMinutes(admin.getLockedDate(), 30);//锁定时间加30分钟
            if (new Date().before(unlockDate)) {
                throw new LockedException("账号已被锁定");
            }
            admin.setLoginFailureCount(0);
            admin.setIsLocked(false);
            admin.setLockedDate(null);
            adminService.update(admin);
        }
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            int loginFailureCount = admin.getLoginFailureCount() + 1;
            if (loginFailureCount >= 5) {//失败5次锁定账号
                admin.setIsLocked(true);
                admin.setLockedDate(new Date());
            }
            admin.setLoginFailureCount(loginFailureCount);
            adminService.update(admin);
            throw new BadCredentialsException("密码错误" + loginFailureCount + "次，若连续5次密码错误账号将被锁定");
        }
        admin.setLoginIp(remoteAddress);
        admin.setLoginDate(new Date());
        admin.setLoginFailureCount(0);
        adminService.update(admin);

        List<SimpleGrantedAuthority> auths = new ArrayList<>();

        for (Role role : admin.getRoles()) {
            auths.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            for (Authority authority : role.getAuthorities()) {
                auths.add(new SimpleGrantedAuthority(authority.getAuthorityValue()));
            }
        }

        return new UsernamePasswordAuthenticationToken(admin, password, auths);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
