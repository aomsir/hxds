package com.aomsir.hxds.bff.driver.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.aomsir.hxds.bff.driver.controller.form.UpdateLocationCacheForm;
import com.aomsir.hxds.bff.driver.controller.form.UpdateOrderLocationCacheForm;
import com.aomsir.hxds.bff.driver.service.DriverLocationService;
import com.aomsir.hxds.common.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/driver/location")
@Tag(name = "DriverLocationController", description = "司机定位服务Web接口")
public class DriverLocationController {
    @Resource
    private DriverLocationService locationService;

    @PostMapping("/updateLocationCache")
    @Operation(summary = "更新司机缓存GPS定位")
    @SaCheckLogin
    public R updateLocationCache(@RequestBody @Valid UpdateLocationCacheForm form) {
        long driverId = StpUtil.getLoginIdAsLong();
        form.setDriverId(driverId);
        this.locationService.updateLocationCache(form);
        return R.ok();
    }

    @PostMapping("/updateOrderLocationCache")
    @Operation(summary = "更新订单定位缓存")
    @SaCheckLogin
    public R updateOrderLocationCache(@RequestBody @Valid UpdateOrderLocationCacheForm form) {
        this.locationService.updateOrderLocationCache(form);
        return R.ok();
    }

}
