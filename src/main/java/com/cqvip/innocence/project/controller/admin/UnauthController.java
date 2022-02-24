package com.cqvip.innocence.project.controller.admin;

import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @ClassName UnauthController
 * @Description TODO
 * @Author Innocence
 * @Date 2020/7/21 14:40
 * @Version 1.0
 */
@RestController
@Api(hidden = true)
@ApiIgnore
public class UnauthController extends AbstractController {

    /**
     * 未登录（即未认证的请求response）
     * @author Innocence
     * @date 2020/7/21
     * @return com.cqvip.innocence.model.dto.JsonResult
     */

    @RequestMapping("/unauth")
    public JsonResult unauth(){
        return JsonResult.Get(false, JsonResult.CODE_NOAUTH);
    }
}
