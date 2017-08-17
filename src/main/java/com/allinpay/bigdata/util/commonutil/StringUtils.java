package com.allinpay.bigdata.util.commonutil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类。
 * 
 * @author wangwch
 * @since 1.0, 2011-01-12
 */
public abstract class StringUtils {

	private StringUtils() {
	}

	/**
	 * 检查指定的字符串是否为空。
	 * <ul>
	 * <li>SysUtils.isEmpty(null) = true</li>
	 * <li>SysUtils.isEmpty("") = true</li>
	 * <li>SysUtils.isEmpty("   ") = true</li>
	 * <li>SysUtils.isEmpty("abc") = false</li>
	 * </ul>
	 * 
	 * @param value 待检查的字符串
	 * @return true/false
	 */
	public static boolean isEmpty(String value) {
		int strLen;
		if (value == null || (strLen = value.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(value.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查指定的字符串列表是否不为空。
	 * 
	 * @param values 字符串列表
	 * @return true/false
	 */
	public static boolean areNotEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isEmpty(value);
			}
		}
		return result;
	}

	/**
	 * 把通用字符编码的字符串转化为汉字编码。
	 * 
	 * @param unicode 通用字符编码的字符串
	 * @return 汉字
	 */
	public static String unicodeToChinese(String unicode) {
		StringBuilder out = new StringBuilder();
		if (!isEmpty(unicode)) {
			for (int i = 0; i < unicode.length(); i++) {
				out.append(unicode.charAt(i));
			}
		}
		return out.toString();
	}
	
    /**
     * 字符串左边补零
     * @param source 原字符串
     * @param len 补零后的总长度
     * @return
     * @since 1.0
     */
    public static String leftAppendZero(String source, int len) {
    	int appendLen = len - source.length();
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < appendLen; i++) {
    		sb.append("0");
    	}
    	sb.append(source);
    	return sb.toString();
    }
    
    /**
     * 数据敏感词处理，保留前四位和后2位，其他用*代替，适用于身份证号，通联卡号，银行卡号
     * @param words 原字符串
     * @return
     * @since 1.0
     */
    public static String desSensitiveWords(String words) {
    	if("".equals(words) || words==null || words.length()<=4){
    		return words;
    	}else if(words.length()<=8){
    		String str = "";
    		for(int i=0;i<words.length()-4;i++){
    			str = str+"*";
    		}
    		String startStr = words.substring(0,4);
    		return startStr+str;
    	}else{
    		String str = "*";
    		for(int i=1;i<words.length()-8;i++){
    			str = str+"*";
    		}
    		String startStr = words.substring(0,4);
    		String endStr = words.substring(words.length()-4,words.length());
    		return startStr+str+endStr;
    	}
    }
    
    /**
     * 数据敏感词处理，保留前3位和后4位，其他用*代替，适用于手机号
     * @param words 原字符串
     * @return
     * @since 1.0
     */
    public static String desSensitiveMobile(String words) {
    	if("".equals(words) || words==null || words.length()<=3){
    		return words;
    	}else if(words.length()<=7){
    		String str = "";
    		for(int i=0;i<words.length()-3;i++){
    			str = str+"*";
    		}
    		String startStr = words.substring(0,3);
    		return startStr+str;
    	}else{
    		String str = "";
    		for(int i=0;i<words.length()-7;i++){
    			str = str+"*";
    		}
    		String startStr = words.substring(0,3);
    		String endStr = words.substring(words.length()-4,words.length());;
    		return startStr+str+endStr;
    	}
    }
    
    /**
     * 判断字符串为空
     * @param str
     * @return
     */
    public static boolean   checkIsEmpty(String str){
    	boolean  res =true;
    	if(str== null || "".equals(str)){
    		res=false;
    	}
    	return res;
    }

	/**  
	*@param 
	
	*@return
	
	*@exception  (方法有异常的话加)
	
	*@author gaosw  
	
	*@Time 2017-2-7上午10:53:11
	
	
	*/
	/**
	 * 验证验证输入汉字
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isChinese(String validateString) {
		String regex = "^[\u4e00-\u9fa5]{1,}$";
		return patternValidator(regex, validateString);
	}

	/**
	 * 正则表达式验证数据
	 * @param regexString  正则字符串
	 * @param validateString 验证字符串
	 * @return 如果validateString 符合 regex的正则表达式格式,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean patternValidator(String regex, String validateString) {
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(validateString);
		return m.matches();
	}

    
}
