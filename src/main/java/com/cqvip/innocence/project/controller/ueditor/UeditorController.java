package com.cqvip.innocence.project.controller.ueditor;

import com.baidu.ueditor.ActionEnter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



/**
 * 用于处理关于ueditor插件相关的请求
 *
 * @author Guoqing
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/ueditor/")
@Api(tags="百度富文本编辑器",hidden = true)
@ApiIgnore
public class UeditorController {

	@Value("${ueditor.path}")
	private String ueditorPath ;

	private static String configPath;

	private  String getConfigPath() {
		if(configPath==null) {
//			configPath = URLDecoder.decode(UeditorController.class.getResource("/ueditor/config.json").getFile(), "UTF-8");
			// 打成jar包后上面的方式获取不到
			ApplicationHome applicationHome = new ApplicationHome(UeditorController.class);
			String rootPath = applicationHome.getSource().getParentFile().toString();
			configPath = rootPath + ueditorPath;
		}
		return configPath;
	}

	@RequestMapping("action")
	public void action(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding( "utf-8" );
		response.setHeader("Content-Type" , "text/html");
		String rootPath = request.getServletContext().getRealPath( "/" );
		response.getWriter().write(new ActionEnter(request, rootPath, getConfigPath()).exec());
	}
}
