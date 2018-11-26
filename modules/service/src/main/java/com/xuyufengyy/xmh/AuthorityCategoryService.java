package com.xuyufengyy.xmh;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * service - 权限分类
 *
 * @author Xu minghua 2017/07/21
 */
@Service
public class AuthorityCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorityCategoryService.class);

    @Resource
    private AuthorityCategoryDao authorityCategoryDao;

    /**
     * 查询权限分类列表
     *
     * @param pageable 分页
     * @return Page<AuthorityCategory>
     */
    @Transactional(readOnly = true)
    public Page<AuthorityCategory> findAll(Pageable pageable) {
        Sort sort = new Sort(Sort.Direction.ASC, "order");
        pageable.getSortOr(sort);
        return authorityCategoryDao.findAll(pageable);
    }

    /**
     * 查询权限分类列表
     *
     * @return List<AuthorityCategory>
     */
    @Transactional(readOnly = true)
    public List<AuthorityCategory> findAll() {
        return authorityCategoryDao.findAll();
    }

    /**
     * 权限分类保存
     * @param authorityCategory
     */
    @Transactional
    public void save(AuthorityCategory authorityCategory) {
        authorityCategoryDao.save(authorityCategory);
        logger.info("save authority category success");
    }

    /**
     * 查询权限分类
     * @param id
     */
    @Transactional(readOnly = true)
    public AuthorityCategory getOne(Long id) {
        return authorityCategoryDao.getOne(id);
    }

    /**
     * 权限分类更新
     * @param authorityCategory
     */
    @Transactional
    public void update(AuthorityCategory authorityCategory) {
        AuthorityCategory pAuthorityCategory = authorityCategoryDao.getOne(authorityCategory.getId());
        try{
            BeanUtils.copyProperties(pAuthorityCategory, authorityCategory);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        logger.info("update authority category success");
    }

    /**
     * 删除权限分类
     * @param ids 权限分类id数组
     */
    @Transactional
    public AuthorityCategory delete(Long... ids){
        for(Long id:ids){
            AuthorityCategory authorityCategory = authorityCategoryDao.getOne(id);
            if (!authorityCategory.getAuthorities().isEmpty()) {
                return authorityCategory;
            }
            authorityCategoryDao.delete(authorityCategory);
        }
        return null;
    }
}
