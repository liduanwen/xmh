package com.xuyufengyy.xmh;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * dao - 地区
 *
 * @author Xu minghua 2018/05/21
 */
public interface AreaDao extends JpaRepository<Area,Long> {

    /**
     * 查找顶级地区
     *
     * @param count
     *            数量
     * @return 顶级地区
     */
    List<Area> findRoots(Integer count);

    /**
     * 查找地区
     *
     * @param name
     *            城市名
     * @return
     */
    Area findByNameLike(String name);
}
