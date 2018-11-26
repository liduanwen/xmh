package com.xuyufengyy.xmh.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ScenicSpotDaoImpl extends BaseDaoImpl {

    /**
     * 查询景区列表
     * @param areaName 地区名称
     * @param name 景区名称
     * @param pageable 分页
     * @return
     */
    public Page<Map<String, Object>> findPage(String areaName, String name, Pageable pageable){
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT a.id, a.name, a.image, COUNT(c.id) AS products, GROUP_CONCAT(c.name SEPARATOR '、') AS productName, a.description FROM xmh_scenic_spot AS a ");
        sql.append(" LEFT JOIN xmh_area AS b ON a.area_id = b.id ");
        sql.append(" LEFT JOIN xmh_product AS c ON a.id = c.scenic_spot_id WHERE 1=1 ");

        List<Object> list = new ArrayList<Object>();

        if(StringUtils.isNoneBlank(areaName)){
            sql.append(" AND b.name LIKE ? ");
            list.add("%" + areaName + "%");
        }

        if(StringUtils.isNoneBlank(name)){
            sql.append(" AND a.name LIKE ? ");
            list.add("%" + name + "%");
        }

        sql.append(" GROUP BY a.id ");

        return super.findPage(sql.toString(), list.toArray(), pageable);
    }
}
