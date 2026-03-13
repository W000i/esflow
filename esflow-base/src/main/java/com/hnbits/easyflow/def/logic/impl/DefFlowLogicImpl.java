package com.hnbits.easyflow.def.logic.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hnbits.easyflow.base.model.DefModel;
import com.hnbits.easyflow.base.model.DefUtil;
import com.hnbits.easyflow.base.model.listener.NodeEvent;
import com.hnbits.easyflow.def.logic.DefFlowLogic;
import com.hnbits.easyflow.def.model.DefFlow;
import com.hnbits.easyflow.def.po.DefFlowPo;
import com.hnbits.easyflow.def.po.DefFlowValidatePo;
import com.hnbits.easyflow.def.repository.DefFlowRepository;
import com.hnbits.easyflow.def.vo.DefFlowKeyVo;
import com.hnbits.easyflow.def.vo.DefFlowVo;
import com.hnbits.easyflow.engine.service.FlowDefService;
import com.hnbits.easyflow.form.model.FlowForm;
import com.hnbits.easyflow.form.po.FlowFormPo;
import com.hnbits.easyflow.form.repository.FlowFormRepository;
import com.hnbits.easyflow.form.vo.FlowFormVo;
import com.hnbits.easyjava.common.logic.impl.BaseLogicImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

@Service("defFlowLogic")
public class DefFlowLogicImpl
		extends BaseLogicImpl<DefFlow, DefFlowPo, DefFlowVo, DefFlowRepository<DefFlow, DefFlowPo, DefFlowVo>>
		implements DefFlowLogic<DefFlow, DefFlowPo, DefFlowVo> {

	@Lazy
	@Autowired
	private FlowDefService flowDefService;

	@Autowired
	private FlowFormRepository<FlowForm, FlowFormPo, FlowFormVo> flowFormRepository;

	@Autowired
	private IdentifierGenerator idGenerator;

	@Override
	@Transactional
	public DefFlow saveOrUpdate(DefFlowPo po, String formDef) {
		// 保存表单定义
		if (StrUtil.isBlank(po.getFormId())) {
			FlowForm form = new FlowForm();
			form.setId(StrUtil.trimToNull(po.getFormId()));
			form.setFormName(po.getFlowName() + "表单");
			form.setFormInfo(formDef);
			form.setFormStatus("0");
			form.setFormTypeCode("10010");
			flowFormRepository.saveOrUpdate(form);
			po.setFormId(form.getId());
		}
		// 保存表单设计
		else if (StrUtil.isNotBlank(formDef)) {
			FlowForm form = new FlowForm();
			form.setId(po.getFormId());
			form.setFormInfo(formDef);
			flowFormRepository.saveOrUpdate(form);
		}
		po.setFlowIcon(po.getIcon().toJSONString());
		po.setFlowProps(po.getProps().toJSONString());
		DefModel defModel = null;
		DefFlow def = null;
		// 判断版本
		List<DefFlow> existDefFlows = this.repository.list(Wrappers.<DefFlow>lambdaQuery()
				.eq(DefFlow::getFlowKey, po.getFlowKey()).orderByDesc(DefFlow::getVersion));
		// 流程key不存在且状态为发布
		if ((CollUtil.isNotEmpty(existDefFlows) && po.getStatus().equals("1")) || StrUtil.isNotBlank(po.getId())) {
			if (CollUtil.isNotEmpty(existDefFlows)) {
				int newVersion = existDefFlows.get(0).getVersion() + 1;
				po.setVersion(newVersion);
			} else {
				po.setVersion(1);
			}
			po.setId(idGenerator.nextId(null).toString());
			def = new DefFlow();
			BeanUtil.copyProperties(po, def);
			this.repository.saveOrUpdate(def);

			defModel = DefUtil.parseJson2Model(def.getId(), def.getFlowKey(), def.getFormId(), def.getFlowDef(),
					this.idGenerator);
			// 解析流程事件
			List<NodeEvent> eventList = JSONArray.parseArray(po.getProps().getJSONArray("listeners").toJSONString(),
					NodeEvent.class);
			DefUtil.parse2EventListener(defModel, null, null, def.getId(), def.getFlowKey(), eventList, idGenerator);
		} else {
			def = new DefFlow();
			BeanUtil.copyProperties(po, def);
			this.repository.saveOrUpdate(def);

			defModel = DefUtil.parseJson2Model(def.getId(), def.getFlowKey(), def.getFormId(), def.getFlowDef(), null);

			// 解析流程事件
			List<NodeEvent> eventList = JSONArray.parseArray(po.getProps().getJSONArray("listeners").toJSONString(),
					NodeEvent.class);
			DefUtil.parse2EventListener(defModel, null, null, def.getId(), def.getFlowKey(), eventList, null);
		}
		// 保存模型信息
		flowDefService.saveOrUpdateDefFlow(defModel);
		return def;
	}

	@Transactional
	public Boolean deleteByIds(String ids) throws Exception {
		try {
			List<String> params = CollUtil.toList(ids.split(","));
			if (repository.deleteDbByIds(params) > 0) {
				for (String id : params) {
					flowDefService.removeDefFlowById(id);
				}
				return true;
			}
		} catch (Exception e) {
			this.exceptionHandler(e);
		}
		return false;
	}

	public Page<DefFlowVo> queryLatestPageList(Page<DefFlow> page, DefFlowPo po) throws Exception {
		Page<DefFlowVo> voPage = null;
		try {
			voPage = this.repository.queryLatestPageList(page, po, true);
		} catch (Exception e) {
			this.exceptionHandler(e);
		}
		return voPage;
	}

	public List<DefFlowKeyVo> queryLatestListByKey(String key) {
		List<DefFlowKeyVo> list = new ArrayList<>();
		List<DefFlowVo> latestList = this.repository.queryLatestListByKey(key);
		if (CollUtil.isNotEmpty(latestList)) {
			latestList.forEach((flow) -> {
				DefFlowKeyVo item = new DefFlowKeyVo();
				BeanUtil.copyProperties(flow, item);
				list.add(item);
			});
		}
		return list;
	}

	public DefFlowVo queryLatestByKey(String key) {
		return this.repository.queryLatestByKey(key);
	}

	public Boolean validate(DefFlowValidatePo po) {
		QueryWrapper<DefFlow> queryWrapper = Wrappers.query();
		queryWrapper.lambda().eq(StrUtil.isNotBlank(po.getFlowKey()), DefFlow::getFlowKey, po.getFlowKey())
				.ne(StrUtil.isNotBlank(po.getId()), DefFlow::getId, po.getId())
				.eq(null != po.getVersion(), DefFlow::getVersion, po.getVersion());
		List<DefFlow> list = this.repository.list(queryWrapper);
		if (null != list && CollUtil.isNotEmpty(list)) {
			return false;
		} else {
			return true;
		}
	}

}
