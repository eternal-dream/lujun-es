package com.cqvip.innocence.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.ArmNetdisk;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqvip.innocence.project.model.enums.VipEnums;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户网盘表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
public interface ArmNetdiskService extends IService<ArmNetdisk> {

    /**
     * 管理员获取个人网盘分页列表
     * @author Innocence
     * @date 2021/8/23
     * @param page
     * @param disk
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.cqvip.innocence.project.model.entity.ArmNetdisk>
     */
    Page<ArmNetdisk> getPageList(Page page,ArmNetdisk disk);

    /**
     * 根据id删除个人网盘信息，并且删除附件表数据
     * @author Innocence
     * @date 2021/8/26
     * @param ids
     * @return java.lang.Boolean
     */
    Boolean deleteByIdsTransactional(List<Serializable> ids);

    /**
     * 保存或者修改网盘信息
     *
     * @param armNetdisk 网盘信息
     * @param file 附件
     * @return {@link boolean}
     * @author 01
     * @date 2021/9/30 16:10
     */
    boolean saveOrUpdateInfo(ArmNetdisk armNetdisk, MultipartFile file, String oldFile, VipEnums.AnnexSrc annexSrc);
}
