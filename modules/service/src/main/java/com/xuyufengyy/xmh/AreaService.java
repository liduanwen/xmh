package com.xuyufengyy.xmh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * service - 地区
 *
 * @author Xu minghua 2018/05/21
 */
@Service
public class AreaService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(AreaService.class);

    @Resource
    private AreaDao areaDao;

    /**
     * 查询地区
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Area getOne(Long id){
        return areaDao.getOne(id);
    }

    /**
     * 查找顶级地区
     *
     * @return 顶级地区
     */
    @Transactional(readOnly = true)
    public List<Area> findRoots() {
        return areaDao.findRoots(null);
    }

    /**
     * 查找顶级地区
     *
     * @param count
     *            数量
     * @return 顶级地区
     */
    @Transactional(readOnly = true)
    public List<Area> findRoots(Integer count) {
        return areaDao.findRoots(count);
    }

    /**
     * 保存
     * @param area 地区
     */
    @Transactional
    public void save(Area area) {
        areaDao.save(area);
        logger.info(" save area success ");
    }

    /**
     * 更新
     * @param area 地区
     * @param ignoreProperties 忽略属性
     */
    @Transactional
    public void update(Area area, String... ignoreProperties) {
        Area pArea = areaDao.getOne(area.getId());
        try{
            super.copyProperties(area, pArea, ignoreProperties);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        logger.info(" update area success ");
    }

    /**
     * 查找地区
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Area getOne(String name) {
        return areaDao.findByNameLike("%"+name+"%");
    }
}
