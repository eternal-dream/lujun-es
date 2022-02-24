package com.baidu.ueditor.upload;

import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.State;
import com.cqvip.innocence.project.model.entity.ArmAnnex;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmAnnexService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class Uploader {
	private HttpServletRequest request = null;
	private Map<String, Object> conf = null;

	@Autowired
	private ArmAnnexService annexService;

	public Uploader(HttpServletRequest request, Map<String, Object> conf) {
		this.request = request;
		this.conf = conf;
	}


	/**
	 * 重写了百度编辑器的上传接口，将附件信息统一入库，然后将主键id返回给前端
	 * @author Innocence
	 * @date 2021/8/27
	 * @return com.baidu.ueditor.define.State
	 */
	public final State doExec() {
		String filedName = (String) this.conf.get("fieldName");
		State state = null;

		if ("true".equals(this.conf.get("isBase64"))) {
			state = Base64Uploader.save(this.request.getParameter(filedName),
					this.conf);
		} else {
			state = BinaryUploader.save(this.request, this.conf, filedName);
		}
		Map<String, String> infoMap = ((BaseState) state).getInfoMap();

		ArmAnnex armAnnex = new ArmAnnex();
		armAnnex.setAnnexSrc(VipEnums.AnnexSrc.WEBPUBLISH);
		armAnnex.setTitle(infoMap.get("original"));
		armAnnex.setFilePath(infoMap.get("url"));
		armAnnex.setFileSize(Integer.valueOf(infoMap.get("size"))/1024);
		armAnnex.insert();
		state.putInfo("annexId",armAnnex.getId());
		return state;
	}
}
