package com.hnbits.easyflow.form.service.impl;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hnbits.easyflow.form.model.FlowFormData;
import com.hnbits.easyflow.form.po.FlowFormDataPo;
import com.hnbits.easyflow.form.repository.FlowFormDataRepository;
import com.hnbits.easyflow.form.service.FormDataService;
import com.hnbits.easyflow.form.vo.FlowFormDataVo;
import com.hnbits.easyflow.order.model.FlowOrderInst;
import com.hnbits.easyflow.order.po.FlowOrderInstPo;
import com.hnbits.easyflow.order.repository.FlowOrderInstRepository;
import com.hnbits.easyflow.order.vo.FlowOrderInstVo;
import com.hnbits.easyflow.runtime.model.RunFlow;
import com.hnbits.easyflow.runtime.po.RunFlowPo;
import com.hnbits.easyflow.runtime.repository.RunFlowRepository;
import com.hnbits.easyflow.runtime.vo.RunFlowVo;
import com.hnbits.easyjava.common.exception.BizException;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

@Service("FormDataService")
public class FormDataServiceImpl implements FormDataService {

	@Autowired
	private RunFlowRepository<RunFlow, RunFlowPo, RunFlowVo> runFlowRepository;

	@Autowired
	private FlowOrderInstRepository<FlowOrderInst, FlowOrderInstPo, FlowOrderInstVo> flowOrderInstRepository;

	@Autowired
	private FlowFormDataRepository<FlowFormData, FlowFormDataPo, FlowFormDataVo> flowFormDataRepository;

	/**
	 * 表单作为菜单按钮时或者非工单提交或者工单暂存草稿时保存表单数据
	 */
	@Transactional
	public Boolean saveOrUpdateOfForm(String formId, String orderInstId, String flowInstId, Map<String, Object> params)
			throws Exception { 
		return this.saveOrUpdate(formId, orderInstId, flowInstId, params);
	}
	
	/**
	 * 工单提交或者工单暂存草稿时保存表单数据
	 */
	@Transactional
	public Boolean saveOrUpdateOfFlow(String formId, String orderInstId, String flowInstId, Map<String, Object> params)
			throws Exception {
		if (StrUtil.isBlank(flowInstId) && StrUtil.isBlankIfStr(orderInstId)) {
			throw new BizException("保存表单数据时工单实例id和流程实例id至少需要有一个不为空");
		}
		return this.saveOrUpdate(formId, orderInstId, flowInstId, params);
	}
	
	/**
	 * 保存工单表单数据
	 */
	@Transactional
	public Boolean saveOrUpdate(String formId, String orderInstId, String flowInstId, Map<String, Object> params)
			throws Exception {
		QueryWrapper<FlowFormData> queryWrapper = Wrappers.query();
		queryWrapper.lambda().eq(StrUtil.isNotBlank(flowInstId), FlowFormData::getFlowInstId, flowInstId)
				.eq(StrUtil.isNotBlank(orderInstId), FlowFormData::getOrderInstId, orderInstId)
				.eq(StrUtil.isNotBlank(formId), FlowFormData::getFormId, formId);
		FlowFormData dbData = this.flowFormDataRepository.getOne(queryWrapper);
		if (null != dbData) {
			FlowFormData data = new FlowFormData();
			data.setFormData(JSONObject.toJSONString(params));
			data.setId(dbData.getId());
			this.flowFormDataRepository.updateById(data);
		} else {
			FlowFormData data = new FlowFormData();
			data.setOrderInstId(orderInstId);
			data.setFormId(formId);
			data.setFlowInstId(flowInstId);
			data.setFormData(JSONObject.toJSONString(params));
			this.flowFormDataRepository.save(data);
		}
		return true;
	}

	public Map<String, Object> getDataMapByFlowInstId(String flowInstId, Boolean containMultiRadioData) {
		RunFlow runFlow = runFlowRepository.getById(flowInstId);
		return this.getDataMapByFlowInstId(runFlow.getFormId(), flowInstId, containMultiRadioData);
	}

	public Map<String, Object> getDataMapByOrderInstId(String orderInstId, Boolean containMultiRadioData) {
		FlowOrderInst orderInst = flowOrderInstRepository.getById(orderInstId);
		return this.getDataMapByOrderInstId(orderInst.getFormId(), orderInstId, containMultiRadioData);
	}

	public Map<String, Object> getDataMapByOrderInstId(String formId, String orderInstId,
			Boolean containMultiRadioData) {
		Map<String, Object> form = null;
		String dataJson = this.flowFormDataRepository.queryDataByOrderInstId(orderInstId);
		form = JSONUtil.parseObj(dataJson);
		return form;
	}

	public Map<String, Object> getDataMapByFlowInstId(String formId, String flowInstId, Boolean containMultiRadioData) {
		Map<String, Object> form = null;
		String dataJson = this.flowFormDataRepository.queryDataByFlowInstId(flowInstId);
		form = JSONUtil.parseObj(dataJson);
		return form;
	}

}
