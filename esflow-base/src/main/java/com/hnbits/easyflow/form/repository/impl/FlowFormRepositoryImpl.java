package com.hnbits.easyflow.form.repository.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hnbits.easyflow.form.dao.FlowFormDao;
import com.hnbits.easyflow.form.model.FlowForm;
import com.hnbits.easyflow.form.po.FlowFormPo;
import com.hnbits.easyflow.form.repository.FlowFormRepository;
import com.hnbits.easyflow.form.vo.FlowFormVo;
import com.hnbits.easyjava.common.convert.ConvertColumn;
import com.hnbits.easyjava.common.convert.ConvertConfig;
import com.hnbits.easyjava.common.convert.ConvertSubTableLimit;
import com.hnbits.easyjava.common.exception.ConvertViewException;
import com.hnbits.easyjava.common.repository.impl.BaseRepositoryImpl;
import com.hnbits.easyjava.common.util.EbaseStringUtil;
import com.hnbits.easyjava.common.util.ViewConvertUtil;
import com.hnbits.easyjava.system.model.SysTree;
import com.hnbits.easyjava.system.model.SysTreeNode;
import com.hnbits.easyjava.system.po.SysTreeNodePo;
import com.hnbits.easyjava.system.po.SysTreePo;
import com.hnbits.easyjava.system.repository.SysTreeNodeRepository;
import com.hnbits.easyjava.system.repository.SysTreeRepository;
import com.hnbits.easyjava.system.vo.SysTreeNodeVo;
import com.hnbits.easyjava.system.vo.SysTreeVo;
import com.hnbits.easyjava.sysuser.model.SysUser;
import com.hnbits.easyjava.sysuser.po.SysUserPo;
import com.hnbits.easyjava.sysuser.repository.SysUserRepository;
import com.hnbits.easyjava.sysuser.vo.SysUserVo;

@Repository
public class FlowFormRepositoryImpl extends BaseRepositoryImpl<FlowForm, FlowFormPo, FlowFormVo,FlowFormDao>
		implements FlowFormRepository<FlowForm, FlowFormPo, FlowFormVo> {

	@Autowired
	private SysUserRepository<SysUser, SysUserPo, SysUserVo> SysUserDao;

	@Autowired(required = false)
	private SysTreeNodeRepository<SysTreeNode, SysTreeNodePo, SysTreeNodeVo> sysTreeNodeDao;

	@Autowired(required = false)
	private SysTreeRepository<SysTree, SysTreePo, SysTreeVo> sysTreeDao;

	@Override
	public FlowFormVo convertModel(FlowForm model, Boolean convertView) throws ConvertViewException {
		FlowFormVo vo = null;
		try {
			vo = this.convertModelDefault(model, convertView);
			if (null != vo && convertView) {
				ConvertConfig config = new ConvertConfig();
				config.setMainTableColumns(new String[] { "create_user_id", "update_user_id" });
				config.setSubTableColumn("id");
				config.setConvertColumns(
						new ConvertColumn[] { new ConvertColumn("create_user_id", "create_user_name", "user_name"),
								new ConvertColumn("update_user_id", "update_user_name", "user_name"), });
				ViewConvertUtil.convertView(vo, config, SysUserDao, SysUserPo.class);

				SysTreePo treePo = new SysTreePo();
				treePo.setTreeCode("form");
				List<SysTree> sysTree = this.sysTreeDao.queryDbList(treePo);
				
				config = new ConvertConfig();
				config.setMainTableColumns(new String[] { "form_type_code" });
				config.setSubTableColumn("node_code");
				config.setConvertColumns(
						new ConvertColumn[] { new ConvertColumn("form_type_code", "form_type_name", "node_name") });
				config.setConvertSubTableLimit(new ConvertSubTableLimit[] {
						new ConvertSubTableLimit("tree_id", sysTree.get(0).getId()) });
				ViewConvertUtil.convertView(vo, config, sysTreeNodeDao, SysTreeNodePo.class);
			}
		} catch (Exception e) {
			log.error("转化视图错误" + e.getMessage(), e);
		}
		return vo;
	}

	@Override
	public List<FlowFormVo> convertModelList(List<FlowForm> modelList, Boolean convertView) throws ConvertViewException {
		List<FlowFormVo> viewList = null;
		try {
			viewList = this.convertModelListDefault(modelList, convertView);
			if (null != modelList && modelList.size() > 0 && convertView) {
				ConvertConfig config = new ConvertConfig();
				config.setMainTableColumns(new String[] { "create_user_id", "update_user_id" });
				config.setSubTableColumn("id");
				config.setConvertColumns(
						new ConvertColumn[] { new ConvertColumn("create_user_id", "create_user_name", "user_name"),
								new ConvertColumn("update_user_id", "update_user_name", "user_name"), });
				ViewConvertUtil.convertView(viewList, config, SysUserDao, SysUserPo.class);

				SysTreePo treePo = new SysTreePo();
				treePo.setTreeCode("form");
				List<SysTree> sysTree = this.sysTreeDao.queryDbList(treePo);
				
				config = new ConvertConfig();
				config.setMainTableColumns(new String[] { "form_type_code" });
				config.setSubTableColumn("node_code");
				config.setConvertColumns(
						new ConvertColumn[] { new ConvertColumn("form_type_code", "form_type_name", "node_name") });
				config.setConvertSubTableLimit(new ConvertSubTableLimit[] {
						new ConvertSubTableLimit("tree_id", sysTree.get(0).getId()) });
				ViewConvertUtil.convertView(viewList, config, sysTreeNodeDao, SysTreeNodePo.class);
			}
		} catch (Exception e) {
			log.error("转化视图错误" + e.getMessage(), e);
		}
		return viewList;
	}

	@Override
	public QueryWrapper<FlowForm> getQueryWrapper(FlowFormPo po) {
		QueryWrapper<FlowForm> queryWrapper = Wrappers.query();
		queryWrapper.lambda().eq(EbaseStringUtil.isNotBlank(po.getId()), FlowForm::getId, po.getId());
		queryWrapper.lambda().eq(EbaseStringUtil.isNotBlank(po.getTableId()), FlowForm::getTableId,
				po.getTableId());
		queryWrapper.lambda().like(EbaseStringUtil.isNotBlank(po.getFormRemark()), FlowForm::getFormRemark,
				po.getFormRemark());
		queryWrapper.lambda().eq(EbaseStringUtil.isNotBlank(po.getFormName()), FlowForm::getFormName,
				po.getFormName());
		if (EbaseStringUtil.isNotBlank(po.getFormTypeCode()) && !po.getFormTypeCode().equalsIgnoreCase("ALL")) {
			queryWrapper.lambda().eq(FlowForm::getFormTypeCode, po.getFormTypeCode());
		}
		if (EbaseStringUtil.isNotBlank(po.getFormStatus()) && !po.getFormStatus().equalsIgnoreCase("ALL")) {
			queryWrapper.lambda().eq(FlowForm::getFormStatus, po.getFormStatus());
		}
		queryWrapper.lambda().eq(EbaseStringUtil.isNotBlank(po.getCustId()), FlowForm::getCustId, po.getCustId());
		return queryWrapper;
	}

	@Override
	public FlowFormVo base2View(FlowForm model) {
		FlowFormVo vo = new FlowFormVo();
		BeanUtils.copyProperties(model, vo);
		return vo;
	}

}
