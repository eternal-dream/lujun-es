package com.cqvip.innocence.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.util.file.FileUploadUtils;
import com.cqvip.innocence.common.util.file.FileUtil;
import com.cqvip.innocence.project.mapper.ArmAnnexMapper;
import com.cqvip.innocence.project.model.entity.ArmAnnex;
import com.cqvip.innocence.project.model.entity.ArmNetdisk;
import com.cqvip.innocence.project.mapper.ArmNetdiskMapper;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmAnnexService;
import com.cqvip.innocence.project.service.ArmNetdiskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.cqvip.innocence.common.util.file.FileUtil.DIRPATHBASE;

/**
 * <p>
 * 用户网盘表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@Slf4j
@Service
public class ArmNetdiskServiceImpl extends ServiceImpl<ArmNetdiskMapper, ArmNetdisk> implements ArmNetdiskService {

    @Autowired
    private ArmAnnexService annexService;

    @Autowired
    FileUtil fileUtil;

    @Override
    public Page<ArmNetdisk> getPageList(Page page, ArmNetdisk disk) {
        return baseMapper.getPageList(page,disk);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByIdsTransactional(List<Serializable> ids) {
        List<Serializable> collect = ids.stream().distinct().collect(Collectors.toList());
        int i = baseMapper.deleteBatchIds(collect);
        LambdaQueryWrapper<ArmAnnex> lambda = new QueryWrapper<ArmAnnex>().lambda()
                .eq(ArmAnnex::getAnnexSrc, VipEnums.AnnexSrc.USERDISK.name());
        collect.forEach(item-> lambda.or().eq(ArmAnnex::getRealId,item));
        boolean remove = annexService.remove(lambda);
        if ((i == collect.size() && remove)){
            List<ArmAnnex> list = annexService.list(lambda);
            list.forEach(item -> fileUtil.deleteFile(fileUtil.getPath(DIRPATHBASE)+item.getFilePath()));
        }
        return (i == collect.size() && remove);
    }

    @Override
    public boolean saveOrUpdateInfo(ArmNetdisk armNetdisk, MultipartFile file,String oldFile,VipEnums.AnnexSrc annexSrc) {
        LocalDate today = LocalDate.now();
        String dateDir = today.getYear() + "" + today.getMonthValue();
        //修改
        if(armNetdisk.getId() != null){
            ArmNetdisk netdisk = baseMapper.selectById(armNetdisk.getId());
            netdisk.setTitle(armNetdisk.getTitle());
            netdisk.setContent(armNetdisk.getContent());
            netdisk.setSortId(armNetdisk.getSortId());
            netdisk.setDown(armNetdisk.getDown());
            netdisk.setRequireScore(armNetdisk.getRequireScore());
            netdisk.setLevel(armNetdisk.getLevel());
            netdisk.setSource(armNetdisk.getSource());
            netdisk.setShareArticleStatus(armNetdisk.getShareArticleStatus());
            //有附件
            if(file != null){
                if(StringUtils.isBlank(oldFile)){
                    //删除以前的附件
                    UpdateWrapper<ArmAnnex> deleteWrapper = new UpdateWrapper<>();
                    deleteWrapper.eq("REAL_ID", armNetdisk.getId());
                    annexService.remove(deleteWrapper);
                }
                //附件
                String fileName = null;
                //以前有没有文件？
                try {
                    fileName = FileUploadUtils.setRename(true).setSuperDir(annexSrc.name()+ File.separator+dateDir).upload(file);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
                if (StringUtils.isNotBlank(fileName)) {
                    //保存附件
                    ArmAnnex armAnnex = new ArmAnnex();
                    String name = file.getOriginalFilename();
                    armAnnex.setFileSize(new Long(file.getSize() / 1024).intValue());
                    armAnnex.setAnnexSrc(VipEnums.AnnexSrc.WEBPUBLISH);
                    armAnnex.setFilePath(fileName);
                    armAnnex.setTitle(name);
                    armAnnex.setRealId(armNetdisk.getId());
                    annexService.save(armAnnex);
                } else {
                    log.info("附件上传失败");
                    return false;
                }


            }else{
                if(StringUtils.isBlank(oldFile)){
                    //删除以前的附件
                    UpdateWrapper<ArmAnnex> deleteWrapper = new UpdateWrapper<>();
                    deleteWrapper.eq("REAL_ID", armNetdisk.getId());
                    annexService.remove(deleteWrapper);
                }
            }

            //保存信息
            baseMapper.updateById(netdisk);

        }else{
            //有附件的情况
            if(file != null){
                //附件
                String fileName = null;
                try {
                    fileName = FileUploadUtils.setRename(true).setSuperDir(annexSrc.name().toString()+ File.separator+dateDir).upload(file);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }

                //保存信息
                baseMapper.insert(armNetdisk);

                if (StringUtils.isNotBlank(fileName)) {
                    //保存附件
                    ArmAnnex armAnnex = new ArmAnnex();
                    String name = file.getOriginalFilename();
                    armAnnex.setFileSize(new Long(file.getSize() / 1024).intValue());
                    armAnnex.setAnnexSrc(VipEnums.AnnexSrc.WEBPUBLISH);
                    armAnnex.setFilePath(fileName);
                    armAnnex.setTitle(name);
                    armAnnex.setRealId(armNetdisk.getId());
                    annexService.save(armAnnex);
                } else {
                    log.info("附件上传失败");
                    return false;
                }
            }

        }

        return true;
    }
}
