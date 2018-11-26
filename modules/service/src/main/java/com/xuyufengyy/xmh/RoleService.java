package com.xuyufengyy.xmh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * service - 角色
 *
 * @author Xu minghua 2017/07/21
 */
@Service
public class RoleService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Resource
    private RoleDao roleDao;

    /**
     * 查询角色列表
     *
     * @return Page<Role>
     */
    @Transactional(readOnly = true)
    public Page<Role> findAll(Pageable pageable) {
        return roleDao.findAll(pageable);
    }

    /**
     * 查询角色列表
     *
     * @return List<Role>
     */
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    /**
     * 保存角色
     * @param role 角色
     */
    @Transactional
    public void save(Role role){
        roleDao.save(role);
        logger.info("save role success");
    }

    /**
     * 查询角色列表
     * @param roleIds 角色id数组
     * @return
     */
    @Transactional(readOnly = true)
    public List<Role> findList(Long... roleIds) {
        List<Role> result = new ArrayList<Role>();
        if (roleIds != null) {
            for (Long id : roleIds) {
                Role entity = roleDao.getOne(id);
                if (entity != null) {
                    result.add(entity);
                }
            }
        }
        return result;
    }

    /**
     * 查询角色
     * @param id 角色id
     * @return Role
     */
    @Transactional(readOnly = true)
    public Role getOne(Long id) {
        return roleDao.getOne(id);
    }

    /**
     * 角色更新
     * @param role 角色
     */
    @Transactional
    public void update(Role role) {
        Role pRole = roleDao.getOne(role.getId());
        try{
            super.copyProperties(role, pRole, new String[]{"isSystem"});
        }catch (Exception ex){
            ex.printStackTrace();
        }
        logger.info("update role success");
    }

    /**
     * 删除角色
     * @param ids 角色id数组
     */
    @Transactional
    public Role delete(Long... ids){
        for(Long id:ids){
            Role role = roleDao.getOne(id);
            if (!role.getAdmins().isEmpty()) {
                return role;
            }
            roleDao.delete(role);
        }
        return null;
    }
}
