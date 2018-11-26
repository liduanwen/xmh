package com.xuyufengyy.xmh;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * service - 产品
 *
 * @author Xu minghua 2018/05/24
 */
@Service
public class ProductService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Resource
    private ProductDao productDao;

    @Resource
    private ScenicSpotService scenicSpotService;

    /**
     * 列表
     * @param pageable 分页
     * @return
     */
    @Transactional(readOnly = true)
    public Page<Product> findPage(Pageable pageable){
        return productDao.findAll(pageable);
    }

    /**
     * 判断名称是否存在
     * @param name 名称
     * @return
     */
    @Transactional(readOnly = true)
    public boolean nameExists(String name) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        Product product =  productDao.findByName(name);
        if(product == null){
            return false;
        }
        return true;
    }

    /**
     * 列表--接口端使用
     * @param scenicSpotId 景区id
     * @param pageable 分页
     * @return
     */
    @Transactional(readOnly = true)
    public Page<Product> findPage(Long scenicSpotId, Pageable pageable){
        ScenicSpot scenicSpot = scenicSpotService.getOne(scenicSpotId);
        return productDao.findByScenicSpot(scenicSpot, pageable);
    }

    /**
     * 保存
     * @param product
     */
    @Transactional
    public void save(Product product){
        productDao.save(product);
        logger.info(" save product success ");
    }

    /**
     * 更新
     * @param product
     */
    @Transactional
    public void update(Product product, String... ignoreProperties){
        Product pProduct = productDao.getOne(product.getId());
        try{
            super.copyProperties(product, pProduct, ignoreProperties);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        logger.info(" update product success ");
    }

    /**
     * 更新
     * @param id 产品id
     */
    @Transactional
    public void update(Long id){
        Product product = productDao.getOne(id);
        product.setListenIn(product.getListenIn()+1);
        logger.info(" update product success ");
    }

    /**
     * 查询
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Product getOne(Long id){
        return productDao.getOne(id);
    }
}
