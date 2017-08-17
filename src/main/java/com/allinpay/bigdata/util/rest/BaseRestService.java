package com.allinpay.bigdata.util.rest;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseRestService {

	@Autowired
	private MessageSource messageSource;
	
	/**
	 *
	 * @param code 获取i18n国际化信息
	 * @return 无效传入参数返回
	 */
	protected String getMessage(String code) {
		return messageSource.getMessage(code, null, Locale.SIMPLIFIED_CHINESE);
	}

	/**
	 * 获取i18n国际化信息
	 * @param param 错误码
	 * @param param 错误信息，如卡号
	 * @return 无效传入参数 param
	 */
	protected String getMessage(String code, String param) {
		String[] params = new String[] { param };
		return messageSource.getMessage(code, params, Locale.SIMPLIFIED_CHINESE);
	}

	/**
	 * 获取i18n国际化信息
	 * @param param 错误码
	 * @param params 错误信息，多个参数值
	 * @return 无效传入参数 param
	 */
	protected String getMessage(String code, Object[] params) {
		return messageSource.getMessage(code, params, Locale.SIMPLIFIED_CHINESE);
	}
	
}
