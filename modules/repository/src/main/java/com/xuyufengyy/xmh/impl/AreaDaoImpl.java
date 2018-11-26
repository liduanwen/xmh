package com.xuyufengyy.xmh.impl;

import com.xuyufengyy.xmh.Area;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class AreaDaoImpl extends BaseDaoImpl {

    /**
     * 查找顶级地区
     *
     * @param count
     *            数量
     * @return 顶级地区
     */
    public List<Area> findRoots(Integer count) {
        String jpql = " SELECT area FROM Area area WHERE area.parent IS NULL ORDER BY area.order ASC ";
        TypedQuery<Area> query = entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT);
        if (count != null) {
            query.setMaxResults(count);
        }
        return query.getResultList();
    }
}
