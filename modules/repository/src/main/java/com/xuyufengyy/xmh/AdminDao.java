package com.xuyufengyy.xmh;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * dao - 管理员
 *
 * @author Xu minghua 2017/07/19
 */
public interface AdminDao extends JpaRepository<Admin,Long> {

    /**
     * 查询管理员
     * @param username 用户名
     * @return Admin
     */
    Admin findByUsername(String username);

    /**
     * 查询管理员
     * @param email 邮箱
     * @return Admin
     */
    Admin findByEmail(String email);

    /**
     * 查询管理员
     * @param username 用户名
     * @param password 密码
     * @return Admin
     */
    Admin findByUsernameAndPassword(String username, String password);
}
