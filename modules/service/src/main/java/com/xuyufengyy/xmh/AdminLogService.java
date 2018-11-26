package com.xuyufengyy.xmh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * service - 管理平台日志
 *
 * @author Xu minghua 2017/07/25
 */
@Service
public class AdminLogService {

    private static final Logger logger = LoggerFactory.getLogger(AdminLogService.class);

    @Resource
    private AdminLogDao adminLogDao;

    /**
     * 保存管理平台日志
     * @param adminLog 管理平台日志
     * @return
     */
    @Transactional
    public void save(AdminLog adminLog){
        adminLogDao.save(adminLog);
        logger.info(" save admin log success ! ");
    }
}
