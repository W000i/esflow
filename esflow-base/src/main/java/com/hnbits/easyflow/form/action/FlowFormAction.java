package com.hnbits.easyflow.form.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hnbits.easyflow.form.logic.FlowFormLogic;
import com.asiainfo.cuc.ocdp.model.FlowForm;
import com.hnbits.easyflow.form.po.FlowFormPo;
import com.hnbits.easyflow.form.vo.FlowFormComponentLibrary;
import com.hnbits.easyflow.form.vo.FlowFormItem;
import com.hnbits.easyflow.form.vo.FlowFormVo;
import com.hnbits.easyjava.common.action.RestBaseAction;
import com.hnbits.easyjava.common.logic.BaseLogic;
import com.hnbits.easyjava.common.response.CommonResult;

import com.asiainfo.cuc.ocdp.model.FlowForm.CollUtil;
import io.swagger.v3.oas.annotations.Operation;

@Controller
@RequestMapping("/esflow/form")
public class FlowFormAction extends RestBaseAction<FlowForm, FlowFormPo, FlowFormVo> {

	public FlowFormAction() {
		super.log = log;
	}

	@Autowired
	private FlowFormLogic<FlowForm, FlowFormPo, FlowFormVo> flowFormLogic;

	@Override
	public BaseLogic<FlowForm, FlowFormPo, FlowFormVo> getLogic() {
		return this.flowFormLogic;
	}

	@Operation(summary = "新增流程表单及关联子表", description = "")
	@PostMapping(value = { "/insert/batch", "/add/batch" })
	@ResponseBody
	public CommonResult<Boolean> insertBatch(HttpServletRequest request,
			@ModelAttribute @Validated(FlowFormPo.Save.class) FlowFormPo po) throws Exception {
		if (this.flowFormLogic.insertBatch(po)) {
			return CommonResult.success();
		} else {
			return CommonResult.failed();
		}
	}

	@Operation(summary = "修改流程表单及关联子表", description = "")
	@PostMapping(value = "/update/batch")
	@ResponseBody
	public CommonResult<Boolean> updateBatch(HttpServletRequest request,
			@ModelAttribute @Validated(FlowFormPo.Update.class) FlowFormPo po) throws Exception {
		if (this.flowFormLogic.updateBatch(po)) {
			return CommonResult.success();
		} else {
			return CommonResult.failed();
		}
	}

	@Operation(summary = "根据表单ID新增或者修改流程表单及关联子表", description = "")
	@PostMapping(value = "/insert/publish")
	@ResponseBody
	public CommonResult<Boolean> insertPublish(HttpServletRequest request,
			@ModelAttribute @Validated(FlowFormPo.Update.class) FlowFormPo po) throws Exception {
		if (this.flowFormLogic.insertOrUpdate(po)) {
			return CommonResult.success();
		} else {
			return CommonResult.failed();
		}
	}

	@Operation(summary = "查询流程表单项列表")
	@GetMapping(value = { "/get/item/list/{id}" })
	@ResponseBody
	public CommonResult<List<FlowFormItem>> queryFieldList(@PathVariable(name = "id") @NotEmpty String id)
			throws Exception {
		log.info("开始查询!");
		List<FlowFormItem> list = this.flowFormLogic.queryFormItemList(id);
		if (CollUtil.isNotEmpty(list)) {
			return CommonResult.success(list);
		} else {
			return CommonResult.failed();
		}
	}

	@Operation(summary = "查询独立存储流程表单组件库")
	@GetMapping(value = { "/get/library/{id}" })
	@ResponseBody
	public CommonResult<FlowFormComponentLibrary> queryRenderView(@PathVariable(name = "id") @NotEmpty String id)
			throws Exception {
		log.info("开始查询!");
		FlowFormComponentLibrary render = this.flowFormLogic.queryLibrary(id);
		if (null != render) {
			return CommonResult.success(render);
		} else {
			return CommonResult.failed();
		}
	}

}
