package com.xuyufengyy.xmh;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * service - 管理员
 *
 * @author Xu minghua 2017/07/19
 */
@Service
public class AdminService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Resource
    private AdminDao adminDao;

    /**
     * 判断用户名是否存在
     * @param username 用户名
     * @return 用户名是否存在
     */
    @Transactional(readOnly = true)
    public boolean usernameExists(String username) {
        if (StringUtils.isBlank(username)) {
            return false;
        }
        Admin admin =  adminDao.findByUsername(username);
        if(admin == null){
            return false;
        }
        return true;
    }

    /**
     * 判断email是否存在
     * @param email 邮箱
     * @return email是否存在
     */
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        Admin admin =  adminDao.findByEmail(email);
        if(admin == null){
            return false;
        }
        return true;
    }

    /**
     * 查询管理员
     *
     * @param email 邮箱
     * @return Admin
     */
    @Transactional(readOnly = true)
    public Admin findByEmail(String email) {
        return adminDao.findByEmail(email);
    }

    /**
     * 查询管理员
     *
     * @param username 用户名
     * @return Admin
     */
    @Transactional(readOnly = true)
    public Admin findByUsername(String username) {
        return adminDao.findByUsername(username);
    }

    /**
     * 查询管理员
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Transactional(readOnly = true)
    public Admin findByUsernameAndPassword(String username, String password) {
        return adminDao.findByUsernameAndPassword(username, password);
    }

    /**
     * 查询管理员列表
     *
     * @param pageable 分页
     * @return Page<Admin>
     */
    @Transactional(readOnly = true)
    public Page<Admin> findAll(Pageable pageable) {
        return adminDao.findAll(pageable);
    }

    /**
     * 查询管理员总数
     * @return
     */
    @Transactional(readOnly = true)
    public int count() {
        List<Admin> admins = adminDao.findAll();
        return admins.size();
    }

    /**
     * 保存管理员
     * @param admin 管理员
     * @return
     */
    @Transactional
    public void save(Admin admin){
        adminDao.save(admin);
        logger.info(" save admin success ! ");
    }

    /**
     * 更新管理员
     * @param admin 管理员
     * @return
     */
    @Transactional
    public void update(Admin admin, String... ignoreProperties){
        Admin pAdmin = adminDao.getOne(admin.getId());
        try{
            super.copyProperties(admin, pAdmin, ignoreProperties);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        logger.info(" update admin success ! ");
    }

    /**
     * 更新管理员
     * @param admin 管理员
     * @return
     */
    @Transactional
    public void update(Admin admin){
        Admin pAdmin = adminDao.getOne(admin.getId());
        try{
            super.copyProperties(admin, pAdmin, null);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        logger.info(" update admin success ! ");
    }

    /**
     * 重置管理员密码
     * @param id 管理员id
     * @param password 加密后的新密码
     * @return
     */
    @Transactional
    public void update(long id, String password){
        Admin pAdmin = adminDao.getOne(id);
        pAdmin.setPassword(password);
        logger.info(" update admin success ! ");
    }

    /**
     * 删除管理员
     * @param ids 管理员id数组
     */
    @Transactional
    public void delete(Long... ids){
        for(Long id:ids){
            Admin admin = adminDao.getOne(id);
            adminDao.delete(admin);
        }
    }

    /**
     * 查询管理员
     * @param id 管理员id
     * @return Admin
     */
    @Transactional(readOnly = true)
    public Admin getOne(Long id) {
        return adminDao.getOne(id);
    }
}
