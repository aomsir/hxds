package com.aomsir.hxds.mis.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.aomsir.hxds.common.exception.HxdsException;
import com.aomsir.hxds.common.util.PageUtils;
import com.aomsir.hxds.common.util.R;
import com.aomsir.hxds.mis.api.controller.form.*;
import com.aomsir.hxds.mis.api.feign.*;
import com.aomsir.hxds.mis.api.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OdrServiceApi odrServiceApi;


    @Resource
    private NebulaServiceApi nebulaServiceApi;

    @Resource
    private MpsServiceApi mpsServiceApi;

    @Resource
    private RuleServiceApi ruleServiceApi;

    @Resource
    private CstServiceApi cstServiceApi;

    @Resource
    private DrServiceApi drServiceApi;
    
    @Override
    public PageUtils searchOrderByPage(SearchOrderByPageForm form) {
        R r = this.odrServiceApi.searchOrderByPage(form);
        HashMap map = (HashMap) r.get("result");
        PageUtils pageUtils = BeanUtil.toBean(map, PageUtils.class);
        return pageUtils;
    }


    @Override
    public HashMap searchOrderComprehensiveInfo(long orderId) {
        HashMap map = new HashMap();

        //查询订单内容信息
        SearchOrderContentForm form_1 = new SearchOrderContentForm();
        form_1.setOrderId(orderId);
        R r = this.odrServiceApi.searchOrderContent(form_1);
        if (!r.containsKey("result")) {
            throw new HxdsException("不存在订单记录");
        }
        HashMap content = (HashMap) r.get("result");
        map.put("content", content);

        //查询客户信息
        long customerId = MapUtil.getLong(content, "customerId");
        SearchCustomerBriefInfoForm form_2 = new SearchCustomerBriefInfoForm();
        form_2.setCustomerId(customerId);
        r = this.cstServiceApi.searchCustomerBriefInfo(form_2);
        HashMap customerInfo = (HashMap) r.get("result");
        map.put("customerInfo", customerInfo);

        //查询司机信息
        long driverId = MapUtil.getLong(content, "driverId");
        SearchDriverBriefInfoForm form_3 = new SearchDriverBriefInfoForm();
        form_3.setDriverId(driverId);
        r = this.drServiceApi.searchDriverBriefInfo(form_3);
        HashMap driverInfo = (HashMap) r.get("result");
        map.put("driverInfo", driverInfo);

        //查询消费规则
        if (content.containsKey("chargeRuleId")) {
            long chargeRuleId = MapUtil.getLong(content, "chargeRuleId");
            SearchChargeRuleByIdForm form_4 = new SearchChargeRuleByIdForm();
            form_4.setRuleId(chargeRuleId);
            r = this.ruleServiceApi.searchChargeRuleById(form_4);
            HashMap chargeRule = (HashMap) r.get("result");
            map.put("chargeRule", chargeRule);
        }

        //查询取消规则
        if (content.containsKey("cancelRuleId")) {
            long cancelRuleId = MapUtil.getLong(content, "cancelRuleId");
            SearchCancelRuleByIdForm form_5 = new SearchCancelRuleByIdForm();
            form_5.setRuleId(cancelRuleId);
            r = this.ruleServiceApi.searchCancelRuleById(form_5);
            HashMap cancelRule = (HashMap) r.get("result");
            map.put("cancelRule", cancelRule);
        }

        //查询分账规则
        if (content.containsKey("profitsharingRuleId")) {
            long profitsharingRuleId = MapUtil.getLong(content, "profitsharingRuleId");
            SearchProfitsharingRuleByIdForm form_6 = new SearchProfitsharingRuleByIdForm();
            form_6.setRuleId(profitsharingRuleId);
            r = this.ruleServiceApi.searchProfitsharingRuleById(form_6);
            HashMap profitsharingRule = (HashMap) r.get("result");
            map.put("profitsharingRule", profitsharingRule);
        }


        //查询GPS规划线路
        CalculateDriveLineForm form_7 = new CalculateDriveLineForm();
        HashMap startPlaceLocation = (HashMap) content.get("startPlaceLocation");
        HashMap endPlaceLocation = (HashMap) content.get("endPlaceLocation");
        form_7.setStartPlaceLatitude(MapUtil.getStr(startPlaceLocation, "latitude"));
        form_7.setStartPlaceLongitude(MapUtil.getStr(startPlaceLocation, "longitude"));
        form_7.setEndPlaceLatitude(MapUtil.getStr(endPlaceLocation, "latitude"));
        form_7.setEndPlaceLongitude(MapUtil.getStr(endPlaceLocation, "longitude"));
        r = this.mpsServiceApi.calculateDriveLine(form_7);
        HashMap driveLine = (HashMap) r.get("result");
        map.put("driveLine", driveLine);

        int status = MapUtil.getInt(content, "status");
        if (status >= 5 && status <= 8) {
            //查询订单GPS定位记录
            SearchOrderGpsForm form_8 = new SearchOrderGpsForm();
            form_8.setOrderId(orderId);
            r = this.nebulaServiceApi.searchOrderGps(form_8);
            ArrayList<HashMap> orderGps = (ArrayList<HashMap>) r.get("result");
            map.put("orderGps", orderGps);
        } else if (status == 4) {
            //查询订单中最后的GPS定位
            SearchOrderLastGpsForm form_9 = new SearchOrderLastGpsForm();
            form_9.setOrderId(orderId);
            r = this.nebulaServiceApi.searchOrderLastGps(form_9);
            HashMap lastGps = (HashMap) r.get("result");
            map.put("lastGps", lastGps);
        }
        return map;
    }


    @Override
    public HashMap searchOrderLastGps(SearchOrderLastGpsForm form) {
        SearchOrderStatusForm statusForm = new SearchOrderStatusForm();
        statusForm.setOrderId(form.getOrderId());
        R r = this.odrServiceApi.searchOrderStatus(statusForm);
        if (!r.containsKey("result")) {
            throw new HxdsException("没有对应的订单记录");
        }
        int status = MapUtil.getInt(r, "result");
        if (status == 4) {
            //查询订单最后的GPS记录
            r = this.nebulaServiceApi.searchOrderLastGps(form);
            HashMap lastGps = (HashMap) r.get("result");
            return lastGps;
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<HashMap> searchOrderStartLocationIn30Days() {
        R r = this.odrServiceApi.searchOrderStartLocationIn30Days();
        ArrayList<HashMap> list = (ArrayList<HashMap>) r.get("result");
        ArrayList<HashMap> result = new ArrayList<>();
        Map<HashMap, Integer> map = CollectionUtil.countMap(list);
        map.forEach((key, value) -> {
            key.replace("latitude", MapUtil.getDouble(key, "latitude"));
            key.replace("longitude", MapUtil.getDouble(key, "longitude"));
            key.put("count", value);
            result.add(key);
        });
        return result;
    }
}
