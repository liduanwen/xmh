package com.xuyufengyy.xmh;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * service - 权限
 *
 * @author Xu minghua 2017/07/19
 */
@Service
public class AuthorityService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorityService.class);

    @Resource
    private AuthorityDao authorityDao;

    @Resource
    private AuthorityCategoryDao authorityCategoryDao;

    /**
     * 查询权限列表
     *
     * @param pageable 分页
     * @return Page<Authority>
     */
    @Transactional(readOnly = true)
    public Page<Authority> findAll(Pageable pageable) {
        return authorityDao.findAll(pageable);
    }

    /**
     * 权限保存
     * @param authority
     */
    @Transactional
    public void save(AuthorityCategory authorityCategory, Authority authority) {
        authority.setAuthorityCategory(authorityCategory);
        authorityDao.save(authority);
        logger.info("save authority success");
    }

    /**
     * 查询权限列表
     * @param authorityIds 权限id数组
     * @return
     */
    @Transactional(readOnly = true)
    public List<Authority> findList(Long... authorityIds) {
        List<Authority> result = new ArrayList<Authority>();
        if (authorityIds != null) {
            for (Long id : authorityIds) {
                Authority entity = authorityDao.getOne(id);
                if (entity != null) {
                    result.add(entity);
                }
            }
        }
        return result;
    }

    /**
     * 权限查询
     * @param id 权限id
     */
    @Transactional(readOnly = true)
    public Authority getOne(Long id) {
        logger.info("query authority success");
        return authorityDao.getOne(id);
    }

    /**
     * 权限更新
     * @param authority 权限
     * @param authorityCategoryId 权限分类id
     */
    @Transactional
    public void update(Authority authority, Long authorityCategoryId) {
        Authority pAuthority = authorityDao.getOne(authority.getId());
        try{
            BeanUtils.copyProperties(pAuthority, authority);
            pAuthority.setAuthorityCategory(authorityCategoryDao.getOne(authorityCategoryId));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        logger.info("update authority category success");
    }

    /**
     * 删除权限
     * @param ids 权限id数组
     */
    @Transactional
    public Authority delete(Long... ids){
        for(Long id:ids){
            Authority authority = authorityDao.getOne(id);
            if (!authority.getRoles().isEmpty()) {
                return authority;
            }
            authorityDao.delete(authority);
        }
        return null;
    }
}
