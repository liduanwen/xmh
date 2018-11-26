package com.xuyufengyy.xmh.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

/**
 * dao - 基类
 *
 * @author Xu minghua 2018/05/21
 */
@Repository
public class BaseDaoImpl {

    @Resource
    protected EntityManager entityManager;

    @Resource
    protected JdbcTemplate jdbcTemplate;


    /**
     * 根据SQL查询分页记录.
     */
    public Page<Map<String, Object>> findPage(String sql, Object[] params, Pageable pageable) {

        String totalSql = String.format("select count(*) from (%s) tt", sql);
        Number number = jdbcTemplate.queryForObject(totalSql, params, Long.class);
        long total = (number != null ? number.longValue() : 0);

        int start = pageable.getPageNumber() * pageable.getPageSize();

        sql = String.format("%s limit %d, %d", sql, start, pageable.getPageSize());

        List<Map<String, Object>> content = jdbcTemplate.queryForList(sql, params);

        return new PageImpl<Map<String, Object>>(content, pageable, total);
    }
}
