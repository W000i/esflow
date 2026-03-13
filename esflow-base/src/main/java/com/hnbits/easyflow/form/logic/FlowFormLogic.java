package com.hnbits.easyflow.form.logic;

import java.util.List;

import com.hnbits.easyflow.form.po.FlowFormPo;
import com.hnbits.easyflow.form.vo.FlowFormComponentLibrary;
import com.hnbits.easyflow.form.vo.FlowFormItem;
import com.hnbits.easyjava.common.logic.BaseLogic;
import com.hnbits.easyjava.common.model.BaseModel;

public interface FlowFormLogic<B extends BaseModel, P, V> extends BaseLogic<B, P, V> {

	public Boolean insertBatch(P po) throws Exception;

	public Boolean updateBatch(P po) throws Exception;

	public Boolean insertOrUpdate(FlowFormPo po) throws Exception;

	public List<FlowFormItem> queryFormItemList(String formId) throws Exception;
	
	public default FlowFormComponentLibrary queryLibrary(String formId) throws Exception{
		return new FlowFormComponentLibrary();
	}
}
