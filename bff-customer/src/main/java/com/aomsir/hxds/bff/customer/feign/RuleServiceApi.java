package com.aomsir.hxds.bff.customer.feign;

import com.aomsir.hxds.bff.customer.controller.form.EstimateOrderChargeForm;
import com.aomsir.hxds.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "hxds-rule")
public interface RuleServiceApi {
    @PostMapping("/charge/estimateOrderCharge")
    public R estimateOrderCharge(EstimateOrderChargeForm form);
}
