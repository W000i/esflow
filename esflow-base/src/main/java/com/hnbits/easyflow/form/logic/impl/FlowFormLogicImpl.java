package com.hnbits.easyflow.form.logic.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hnbits.easyflow.form.logic.FlowFormLogic;
import com.hnbits.easyflow.form.model.FlowForm;
import com.hnbits.easyflow.form.po.FlowFormPo;
import com.hnbits.easyflow.form.repository.FlowFormRepository;
import com.hnbits.easyflow.form.vo.FlowFormItem;
import com.hnbits.easyflow.form.vo.FlowFormVo;
import com.hnbits.easyjava.common.logic.impl.BaseLogicImpl;

import cn.hutool.core.util.StrUtil;

@Service
public class FlowFormLogicImpl
		extends BaseLogicImpl<FlowForm, FlowFormPo, FlowFormVo, FlowFormRepository<FlowForm, FlowFormPo, FlowFormVo>>
		implements FlowFormLogic<FlowForm, FlowFormPo, FlowFormVo> {

	@Transactional
	public Boolean insertBatch(FlowFormPo po) throws Exception {
		try {
			int rows = repository.insertDb(po);
			if (rows > 0) {
				return true;
			}
		} catch (Exception e) {
			super.exceptionHandler(e);
		}
		return false;
	}

	@Transactional
	public Boolean updateBatch(FlowFormPo po) throws Exception {
		try {
			int rows = repository.updateDb(po);
			if (rows > 0) {
				return true;
			}
		} catch (Exception e) {
			super.exceptionHandler(e);
		}
		return false;
	}

	@Transactional
	public Boolean insertOrUpdate(FlowFormPo po) throws Exception {
		if (StrUtil.isNotBlank(po.getId())) {
			return this.updateBatch(po);
		} else {
			return this.insertBatch(po);
		}
	}

	/**
	 * 获取表单内所有item
	 */
	public List<FlowFormItem> queryFormItemList(String formId) throws Exception {
		try {
			List<FlowFormItem> itemList = new ArrayList<>();
			FlowForm form = this.repository.getById(formId);
			String formJson = form.getFormInfo();
			List<FlowFormItem> list = parseUnionFormItem(form.getId(), formJson);
			itemList.addAll(list);
			return itemList;
		} catch (Exception e) {
			super.exceptionHandler(e);
		}
		return null;
	}

	private List<FlowFormItem> parseUnionFormItem(String formId, String formJson) {
		List<FlowFormItem> itemList = new ArrayList<>();
		JSONObject formJsonObj = JSONObject.parseObject(formJson);
		JSONArray widgetList = formJsonObj.getJSONArray("components");
		for (int i = 0; i < widgetList.size(); i++) {
			JSONObject item = widgetList.getJSONObject(i);
			parse(formId, item, itemList);
		}
		return itemList;
	}

	private void parse(String formId, JSONObject item, List<FlowFormItem> itemList) {
		JSONObject itemProps = item.getJSONObject("props");
		if (itemProps.getBoolean("isContainer") || itemProps.containsKey("columns")) {
			parseColumnsFormItem(formId, item, itemList);
		} else if (itemProps.getBoolean("isFormItem")) {
			parseFormItem(formId, item, itemList);
		}
	}

	private void parseFormItem(String formId, JSONObject item, List<FlowFormItem> itemList) {
		FlowFormItem formItem = new FlowFormItem();
		formItem.setFormId(formId);
		formItem.setColumnKey(item.getString("code"));
		formItem.setColumnComment(item.getString("name"));
		formItem.setTableKey(null);
		formItem.setTableComment(null);
		formItem.setMainSubRadio("0");
		itemList.add(formItem);
	}

	private void parseTableListFormItem(String formId, String tableKey, String tableComment, JSONObject item,
			List<FlowFormItem> itemList) {
		FlowFormItem formItem = new FlowFormItem();
		formItem.setFormId(formId);
		formItem.setColumnKey(item.getString("code"));
		formItem.setColumnComment(item.getString("name"));
		formItem.setTableKey(tableKey);
		formItem.setTableComment(tableComment);
		formItem.setMainSubRadio("1");
		itemList.add(formItem);
	}

	private void parseColumnsFormItem(String formId, JSONObject item, List<FlowFormItem> itemList) {
		JSONObject itemProps = item.getJSONObject("props");
		if (itemProps.containsKey("columns")) {
			String componentCode = itemProps.getString("code");
			String componentName = itemProps.getString("name");
			JSONArray itemColumns = itemProps.getJSONArray("columns");
			if (null != itemColumns && itemColumns.size() > 0) {
				for (int i = 0; i < itemColumns.size(); i++) {
					String columnItemType = item.getString("type");
					if (columnItemType.equalsIgnoreCase("SpanLayout")) {
						JSONArray colItems = itemColumns.getJSONArray(i);
						for (int j = 0; j < colItems.size(); j++) {
							parse(formId, colItems.getJSONObject(j), itemList);
						}
					} else if (columnItemType.equalsIgnoreCase("CardLayout")) {
						JSONObject colItem = itemColumns.getJSONObject(i);
						parse(formId, colItem, itemList);
					} else if (columnItemType.equalsIgnoreCase("TableList")) {
						JSONObject colItem = itemColumns.getJSONObject(i);
						parseTableListFormItem(formId, componentCode, componentName, colItem, itemList);
					} else {
						JSONObject colItem = itemColumns.getJSONObject(i);
						parseFormItem(formId, colItem, itemList);
					}
				}
			}
		}
	}

}
