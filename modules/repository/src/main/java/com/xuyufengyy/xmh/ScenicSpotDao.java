package com.xuyufengyy.xmh;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

/**
 * dao - 景区
 *
 * @author Xu minghua 2018/05/24
 */
public interface ScenicSpotDao extends JpaRepository<ScenicSpot,Long> {

    /**
     * 查询景区列表
     * @param areaName 地区名称
     * @param name 景区名称
     * @param pageable 分页
     * @return
     */
    Page<Map<String, Object>> findPage(String areaName, String name, Pageable pageable);

    /**
     * 查询景区列表
     * @param area 地区
     * @return
     */
    List<ScenicSpot> findByArea(Area area);

    /**
     * 查询景区推荐列表
     * @param isRecommend 是否推荐
     * @return
     */
    List<ScenicSpot> findByIsRecommend(Boolean isRecommend);
}
