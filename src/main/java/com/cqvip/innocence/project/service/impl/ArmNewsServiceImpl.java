package com.cqvip.innocence.project.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqvip.innocence.common.sessions.SessionKeys;
import com.cqvip.innocence.common.util.file.FileUploadUtils;
import com.cqvip.innocence.common.util.html.IpUtils;
import com.cqvip.innocence.common.util.redis.RedisUtil;
import com.cqvip.innocence.common.util.yml.YmlReadUtil;
import com.cqvip.innocence.common.util.zip.ZipUtil;
import com.cqvip.innocence.project.mapper.ArmAnnexMapper;
import com.cqvip.innocence.project.mapper.ArmNewsLogMapper;
import com.cqvip.innocence.project.mapper.ArmNewsMapper;
import com.cqvip.innocence.project.mapper.ArmUserScoreLogMapper;
import com.cqvip.innocence.project.model.entity.*;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmNewsService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import static com.cqvip.innocence.common.constant.ConfigConstant.YML_FILE_NAME;

/**
 * <p>
 * 新闻表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArmNewsServiceImpl extends ServiceImpl<ArmNewsMapper, ArmNews> implements ArmNewsService {
    public static final String TEMP_PATH = YmlReadUtil.newInstance().getValueToString(YML_FILE_NAME, "myFile.temp-path");

    private static ReentrantLock lock = new ReentrantLock();

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ArmUserScoreLogMapper armUserScoreLogMapper;

    @Autowired
    private ArmNewsLogMapper logMapper;

    @Autowired
    ArmAnnexMapper armAnnexMapper;


    @Override
    public Page<ArmNews> getNewsByPage(ArmNews armNews, Date beginTime, Date endTime, Page page,String order) {
        return baseMapper.getNewsByPage(armNews,beginTime,endTime,page,order);
    }

    @Override
    public int deleteByIdWithXml(Serializable newsId, Serializable logId) {
        int i = logMapper.deleteByIdToXml(logId);
        int i1 = baseMapper.deleteByIdWithXml(newsId);
        if (i == i1 && i == 1){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public boolean restoreNews(ArmNewsLog log) {
        int restore = baseMapper.restore(log.getObjId());
        int i = logMapper.deleteByIdToXml(log.getId());
        if (i == restore && i == 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean deleteByIdsWithTransaction(Long[] ids, HttpServletRequest request) {
        try{
            baseMapper.deleteBatchIds(Arrays.asList(ids));
            //操作日志
            for (Long id : ids) {
                ArmAdminUser currentAdmin = (ArmAdminUser) SecurityUtils.getSubject().getPrincipal();
                ArmNewsLog newsLog = new ArmNewsLog();
                newsLog.setAdminUserName(currentAdmin.getRealName());
                newsLog.setAdminUserId(currentAdmin.getId());
                newsLog.setOperateType(VipEnums.OperateType.DELETE);
                newsLog.setTitle("修改发布内容");
                newsLog.setIpAddress(IpUtils.getIpAddr(request));
                newsLog.setObjId(id);
                logMapper.insert(newsLog);
            }
        }catch(Exception ex){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public List<Map<String,Object>> getNewsListToIndex() {
        return baseMapper.getNewsListToIndex();
    }

    @Override
    public Page<ArmNews> getNewsPageList(Page page) {
        return baseMapper.getNewsPageList(page);
    }

    @Override
    public void downloadAnnexAndOtherToDo(Long id, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        // 获取新闻对应附件列表
        QueryWrapper<ArmAnnex> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("REAL_ID", id);
        // 附件列表
        List<ArmAnnex> list = armAnnexMapper.selectList(queryWrapper);
        // 多文件下载,压缩后再下载

        try{
            lock.lock();
            /*
             * 1.创建临时文件夹
             */
            File temDir = new File(FileUploadUtils.getPath(TEMP_PATH));
            if (!temDir.exists()) {
                temDir.mkdirs();
            }

            /*
             * 2.生成需要下载的文件，存放在临时文件夹内
             */
            try {
                for (ArmAnnex annex : list) {
                    ZipUtil.copyFileToDir(FileUploadUtils.getPath(annex.getFilePath()), FileUploadUtils.getPath(TEMP_PATH));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
             * 3.设置response的header
             */
            response.setContentType("application/zip");
            //String s = new String(armNews.getTitle().getBytes("UTF-8"), "ISO8859-1");
            //response.setHeader("Content-disposition", "attachment; filename=" + s);
            long current = DateUtil.current(true);
            response.setHeader("Content-disposition", "attachment; filename=" + current+".zip");
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            /*
             * 4.调用工具类，下载zip压缩包
             */
            try {
                ZipUtil.toZip(temDir.getPath(), response.getOutputStream(), false);
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
             * 5.删除临时文件和文件夹,没写递归，直接删除
             */
            File[] listFiles = temDir.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                listFiles[i].delete();
            }
            temDir.delete();

            //下载次数加1
            ArmNews armNews = baseMapper.selectById(id);
            armNews.setDownCount(armNews.getDownCount()+1);
            baseMapper.updateById(armNews);

            //积分
            //String sessionId = request.getSession().getId();
            //ArmUserInfo user = (ArmUserInfo)redisUtil.get(sessionId);
            //ArmUserScoreLog armUserScoreLog = new ArmUserScoreLog();
            //armUserScoreLog.setUserId(user.getId());
            //armUserScoreLog.setOperateType(VipEnums.ScoreType.DOWNLOADED);
            //armUserScoreLog.setObjId(id+"");
            //armUserScoreLog.setScore();
            //armUserScoreLogMapper.insert(armUserScoreLog);
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
