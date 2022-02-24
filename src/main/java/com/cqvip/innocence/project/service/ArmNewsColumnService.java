package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.entity.ArmNewsColumn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 新闻栏目表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
public interface ArmNewsColumnService extends IService<ArmNewsColumn> {
    /**
     * 获取栏目tree
     *
     * @return {@link List< ArmNewsColumn>}
     * @author 01
     * @date 2021/9/7 13:17
     */
    List<ArmNewsColumn> getNewsColumnsOfTreeData(Integer level);
}
