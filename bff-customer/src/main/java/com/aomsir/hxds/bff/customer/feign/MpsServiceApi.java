package com.aomsir.hxds.bff.customer.feign;

import com.aomsir.hxds.bff.customer.controller.form.EstimateOrderMileageAndMinuteForm;
import com.aomsir.hxds.bff.customer.controller.form.SearchBefittingDriverAboutOrderForm;
import com.aomsir.hxds.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "hxds-mps")
public interface MpsServiceApi {
    @PostMapping("/map/estimateOrderMileageAndMinute")
    public R estimateOrderMileageAndMinute(EstimateOrderMileageAndMinuteForm form);

    @PostMapping("/driver/location/searchBefittingDriverAboutOrder")
    public R searchBefittingDriverAboutOrder(SearchBefittingDriverAboutOrderForm form);
}

