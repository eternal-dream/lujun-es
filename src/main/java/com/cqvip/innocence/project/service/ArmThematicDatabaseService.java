package com.cqvip.innocence.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqvip.innocence.project.model.entity.ArmThematicDatabase;
import com.cqvip.innocence.project.model.enums.VipEnums;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 专题数据库 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-24
 */
public interface ArmThematicDatabaseService extends IService<ArmThematicDatabase> {

    IPage<ArmThematicDatabase>  getThematicTypesByPage(ArmThematicDatabase armThematicDatabase, Page page);

    Map<String,Object> getThematicArticles(ArmThematicDatabase thematicDatabase, Page page, Integer articleType);

 List recommendation(ArmThematicDatabase thematicDatabase);
}
