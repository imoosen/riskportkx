package com.allinpay.bigdata.api.kexin;

import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.allinpay.bigdata.util.commonutil.CommonParams;
import com.allinpay.bigdata.util.commonutil.ErrorCode;
import com.allinpay.bigdata.util.kexin.KexinClient;
import com.allinpay.bigdata.util.rest.BaseRestService;

/**
 * 
 * 功能：可信不良记录查询0001
 * 
 * @author Yuanxy
 * 
 *         创建日期：2017-6-9上午11:34:50
 */
@Slf4j
@Component
public class BadrecordQueryService extends BaseRestService {

	@Autowired
	private CommonParams commonParams;
	
	public JSONObject badRecordQuery(String idCard, String name) {

		JSONObject res = new JSONObject();

		// 身份证不能为空
		if (StringUtils.isBlank(idCard)) {
			res.put("code", ErrorCode.IDCARD_NULL_CODE_2001);
			res.put("msg", getMessage(ErrorCode.IDCARD_NULL_CODE_2001));
			return res;
		}

		// 姓名不能为空
		if (StringUtils.isBlank(name)) {
			res.put("code", ErrorCode.NAME_NULL_CODE_2002);
			res.put("msg", getMessage(ErrorCode.NAME_NULL_CODE_2002));
			return res;
		}
		String result = "";

		try {
			// 不良记录查询
			KexinClient client = new KexinClient(commonParams.getKexinAKey(),
					commonParams.getKexinSKey(), commonParams.getKexinbrokenUrl());
			Map<String, String> map = new HashMap<String, String>();
			map.put("idcard", idCard);
			map.put("name", URLEncoder.encode(name, "utf-8"));
			map.put("user_ip", commonParams.getUserIP());
			try {
				result = client.getVerification(map);
				log.info("kexin0001 result：{}", result);
				res.put("data", result);
				if (result == null || result.isEmpty()) {
					res.put("code", ErrorCode.FAIL_CODE_0001);
					res.put("msg", getMessage(ErrorCode.FAIL_CODE_0001));
				} else {
					JSONObject Jsonresult = JSONObject.parseObject(result);
					if (Jsonresult.getString("status") == null) {
						res.put("code", ErrorCode.FAIL_CODE_0001);
						res.put("msg", getMessage(ErrorCode.FAIL_CODE_0001));
					} else {
						if (Jsonresult.containsKey("status")) {
							if ("true".equals(Jsonresult.getString("status"))) {
								// 0 无错误 203 参数错误 500 900 服务异常
								String error_code = Jsonresult.getString("error_code");
								res.put("code", ErrorCode.SUCCESS_CODE_0000);
								res.put("msg",getMessage(ErrorCode.SUCCESS_CODE_0000));
								if (!"0".equals(error_code)) {
									res.put("errorreason", error_code + ","
											+ Jsonresult.getString("error_msg"));
								}
							} else {
								res.put("code", ErrorCode.FAIL_CODE_0001);
								res.put("msg",getMessage(ErrorCode.FAIL_CODE_0001));
								res.put("errorreason",Jsonresult.getString("error"));
							}
						} else {
							res.put("code", ErrorCode.FAIL_CODE_0001);
							res.put("msg", getMessage(ErrorCode.FAIL_CODE_0001));
						}
					}
				}
			} catch (ConnectTimeoutException e) {
				res.put("code", ErrorCode.FAIL_CODE_0001);
				res.put("msg", getMessage(ErrorCode.FAIL_CODE_0001));
				log.info("kexin 0001 failed：{}", e.toString());
			} catch (SocketTimeoutException e) {
				res.put("code", ErrorCode.FAIL_CODE_0001);
				res.put("msg", getMessage(ErrorCode.FAIL_CODE_0001));
				log.info("kexin 0001 failed：{}", e.toString());
			} catch (Exception e) {
				res.put("code", ErrorCode.FAIL_CODE_0001);
				res.put("msg", getMessage(ErrorCode.FAIL_CODE_0001));
				log.info("kexin 0001 failed：{}", e.toString());
			}
		} catch (UnsupportedEncodingException e) {
			res.put("code", ErrorCode.FAIL_CODE_0001);
			res.put("msg", getMessage(ErrorCode.FAIL_CODE_0001));
			log.info("kexin 0001 failed，name Encode error：{}", e.toString());
		}

		return res;

	}
}
