package com.aomsir.hxds.dr.controller;


import cn.hutool.core.bean.BeanUtil;
import com.aomsir.hxds.common.util.R;
import com.aomsir.hxds.dr.controller.form.CreateDriverFaceModelForm;
import com.aomsir.hxds.dr.controller.form.LoginForm;
import com.aomsir.hxds.dr.controller.form.RegisterNewDriverForm;
import com.aomsir.hxds.dr.controller.form.UpdateDriverAuthForm;
import com.aomsir.hxds.dr.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/driver")
@Tag(name = "DriverController", description = "司机模块Web接口")
public class DriverController {
    @Resource
    private DriverService driverService;
    
	@PostMapping("/registerNewDriver")
    @Operation(summary = "新司机注册")
    public R registerNewDriver(@RequestBody @Valid RegisterNewDriverForm form) {
        Map param = BeanUtil.beanToMap(form);    // 只能转换为Map,所以dao参数就都是Map
        String userId = this.driverService.registerNewDriver(param);
        return R.ok()
                .put("userId", userId);
    }

    @PostMapping("/updateDriverAuth")
    @Operation(summary = "更新实名认证信息")
    public R updateDriverAuth(@RequestBody @Valid UpdateDriverAuthForm form) {
        Map param = BeanUtil.beanToMap(form);   // JSON转Map
        int rows = this.driverService.updateDriverAuth(param);

        return R.ok()
                .put("rows", rows);
    }

    @PostMapping("/createDriverFaceModel")
    @Operation(summary = "创建司机人脸模型归档")
    public R createDriverFaceModel(@RequestBody @Valid CreateDriverFaceModelForm form) {
        String result = this.driverService.createDriverFaceModel(form.getDriverId(), form.getPhoto());
        return R.ok()
                .put("result", result);
    }


    @PostMapping("/login")
    @Operation(summary = "登陆系统")
    public R login(@RequestBody @Valid LoginForm form) {

        // code是bff系统传递过来的临时ID
        HashMap map = this.driverService.login(form.getCode());
        return R.ok().put("result", map);
    }

}
