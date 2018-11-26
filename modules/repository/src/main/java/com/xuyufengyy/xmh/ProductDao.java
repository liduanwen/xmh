package com.xuyufengyy.xmh;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * dao - 产品
 *
 * @author Xu minghua 2018/05/24
 */
public interface ProductDao extends JpaRepository<Product,Long> {

    /**
     * 查询产品列表
     * @param scenicSpot 景区
     * @param pageable 分页
     * @return
     */
    Page<Product> findByScenicSpot(ScenicSpot scenicSpot, Pageable pageable);


    /**
     * 查询产品
     * @param name 名称
     * @return
     */
    Product findByName(String name);
}
