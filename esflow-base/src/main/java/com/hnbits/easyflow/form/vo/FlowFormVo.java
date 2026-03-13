package com.hnbits.easyflow.form.vo;
 
import com.hnbits.easyflow.form.model.FlowForm;
import com.hnbits.easyjava.common.annotation.DictConvert;
import com.hnbits.easyjava.system.SystemDictCodeConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data 
@EqualsAndHashCode(callSuper = true)
public class FlowFormVo extends FlowForm {

	private static final long serialVersionUID = 2756642863261167769L;

	@DictConvert(code = SystemDictCodeConstant.DICT_ENABLE_CODE, refField = "formStatus")
	private String formStatusDesc;

	private String formTypeName;

	private String formTypeId;

}
