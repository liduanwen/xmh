package com.xuyufengyy.xmh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * service - 景区
 *
 * @author Xu minghua 2018/05/24
 */
@Service
public class ScenicSpotService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(ScenicSpotService.class);

    @Resource
    private ScenicSpotDao scenicSpotDao;

    @Resource
    private AreaService areaService;

    /**
     * 列表
     * @param pageable 分页
     * @return
     */
    @Transactional(readOnly = true)
    public Page<ScenicSpot> findPage(Pageable pageable){
        return scenicSpotDao.findAll(pageable);
    }

    /**
     * 列表--接口端使用
     * @param cityName 城市名称
     * @param name 景区名称
     * @param pageable 分页
     * @return
     */
    @Transactional(readOnly = true)
    public Page<Map<String, Object>> findPage(String cityName, String name, Pageable pageable){
        return scenicSpotDao.findPage(cityName, name, pageable);
    }

    /**
     * 推荐列表--接口端使用
     * @return
     */
    @Transactional(readOnly = true)
    public List<ScenicSpot> findlist(Boolean isRecommend){
        return scenicSpotDao.findByIsRecommend(isRecommend);
    }

    /**
     * 保存
     * @param scenicSpot
     */
    @Transactional
    public void save(ScenicSpot scenicSpot){
        scenicSpotDao.save(scenicSpot);
        logger.info(" save scenic spot success ");
    }

    /**
     * 更新
     * @param scenicSpot
     */
    @Transactional
    public void update(ScenicSpot scenicSpot, String... ignoreProperties){
        ScenicSpot pScenicSpot = scenicSpotDao.getOne(scenicSpot.getId());
        try{
            super.copyProperties(scenicSpot, pScenicSpot, ignoreProperties);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        logger.info(" update scenic spot success ");
    }

    /**
     * 查询
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public ScenicSpot getOne(Long id){
        return scenicSpotDao.getOne(id);
    }

    /**
     * 查询景区列表
     * @param areaId 地区id
     * @return
     */
    @Transactional(readOnly = true)
    public List<ScenicSpot> findList(Long areaId){
        return scenicSpotDao.findByArea(areaService.getOne(areaId));
    }
}
