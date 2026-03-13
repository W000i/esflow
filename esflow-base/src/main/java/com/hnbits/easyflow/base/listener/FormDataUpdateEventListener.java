package com.hnbits.easyflow.base.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hnbits.easyflow.def.model.DefFlow;
import com.hnbits.easyflow.def.po.DefFlowPo;
import com.hnbits.easyflow.def.repository.DefFlowRepository;
import com.hnbits.easyflow.def.vo.DefFlowVo;
import com.hnbits.easyflow.form.logic.FlowFormDataLogic;
import com.hnbits.easyflow.form.model.FlowForm;
import com.hnbits.easyflow.form.model.FlowFormData;
import com.hnbits.easyflow.form.po.FlowFormDataPo;
import com.hnbits.easyflow.form.po.FlowFormPo;
import com.hnbits.easyflow.form.repository.FlowFormRepository;
import com.hnbits.easyflow.form.vo.FlowFormDataVo;
import com.hnbits.easyflow.form.vo.FlowFormVo;
import com.hnbits.easyflow.runtime.model.RunFlow;
import com.hnbits.easyflow.runtime.po.RunFlowPo;
import com.hnbits.easyflow.runtime.repository.RunFlowRepository;
import com.hnbits.easyflow.runtime.vo.RunFlowVo;

@Service("FormDataUpdateListener")
public class FormDataUpdateEventListener implements IEventListener {

	@Autowired
	RunFlowRepository<RunFlow, RunFlowPo, RunFlowVo> runFlowRepository;

	@Autowired
	DefFlowRepository<DefFlow, DefFlowPo, DefFlowVo> defFlowRepository;

	@Autowired
	FlowFormRepository<FlowForm, FlowFormPo, FlowFormVo> flowFormRepository;

	@Autowired
	private FlowFormDataLogic<FlowFormData, FlowFormDataPo, FlowFormDataVo> flowFormDataLogic;

	/**
	 * 待确认 更新map中key对应字段的值
	 */
	@Override
	public void execute(String flowInstId, Map<String, Object> params) {
		RunFlow runFlow = runFlowRepository.getById(flowInstId);
		DefFlow defFlow = defFlowRepository.getById(runFlow.getDefFlowId());
		FlowForm flowForm = flowFormRepository.getById(defFlow.getFormId());
		flowFormDataLogic.updateKeyDataByFlowInstId(flowForm.getId(), flowInstId, params);
	}
}
