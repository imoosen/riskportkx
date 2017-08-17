/*
 *
 */

package com.allinpay.bigdata.controller;
import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.allinpay.bigdata.api.kexin.BadrecordQueryService;
import com.allinpay.bigdata.service.KafkaManager;
import com.allinpay.bigdata.service.MsgDistributionManager;


@Controller
@RequestMapping("/")
public class OlogController {
	//默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null; 
	
	//forward paths
	protected static final String LIST_JSON = "open/Olog/listJson";
	protected static final String LIST= "open/Olog/list";
	protected static final String CREATE = "open/Olog/create";
	protected static final String EDIT = "open/Olog/edit";
	protected static final String VIEW = "open/Olog/view";
	
//	@Autowired
//    private KafkaTemplate<String,String> kafkaTemplate;
	
	@Autowired
	private KafkaManager kafkaManager;
	@Autowired
	private MsgDistributionManager msgDistributionManager;
	@Autowired
	private BadrecordQueryService badrecordQueryService;
	
	@RequestMapping(value="/", method={RequestMethod.GET})
	@ResponseBody
	public String list(ServletRequest request) {
//		kafkaTemplate.send("suanhua", "2222222222222");
//		kafkaTemplate.send("testTopic", "3333333333333");
		//kafkaManager.produce("suanhua","key1","partKey1", "333333333");
		String msg = "{\"channelId\": \"suanhua\",\"funcId\": \"001\",\"interfaceId\": \"1003\",\"interfaceName\": \"姓名、身份证号、银行卡、手机号比对\",\"operatorId\": \"操作员ID\",\"orgId\": \"机构ID\",\"param\": {"+
		"\"bankNo\": \"6225758317934379\",\"idCard\": \"610122198908072818\",\"name\": \"汪洋\",\"phoneNo\": \"13468779136\"},\"pkId\": \"B\",\"redisKey\": \"E30766E354F41D03C\","+
	"\"seralNo\": \"0123JLDSFKL\",\"sessionId\": \"session_id_SN72N23\"}";
		//msgDistributionManager.messageHandler("", msg);
		//badrecordQueryService.badRecordQuery("410526199107232011","袁晓洋");
		return LIST;
	}
	
	
//	@Autowired
//	private OlogServiceI ologService;
//	
//	
//	@ModelAttribute("preloadOlog")
//	public Olog preload(
//			@RequestParam(value = "id", required = false) java.lang.Long id
//		){
//		if (id != null) {
//			Olog olog = ologService.selectByPrimaryKey(id);
//			return olog;
//		}
//		return null;
//	}
//	
//	@RequestMapping(value="/list", method={RequestMethod.GET})
//	public String list(ServletRequest request) {
//		return LIST;
//	}
//	
//	@RequestMapping(value="/list", method={RequestMethod.POST})
//	public @ResponseBody PageInfo<Olog> list(ServletRequest request
//			,@RequestParam(defaultValue="1") Integer current
//			,@RequestParam(defaultValue="10") Integer size) {
//		Example example = DynamicExample.bySearchFilter(request, Olog.class);
//		example.orderBy("id");
//		PageHelper.startPage(current, size);
//		return new PageInfo<>(ologService.selectByExample(example));
//	}
//	
//	
//	@RequestMapping(value="/create", method=RequestMethod.GET)
//	public String create(ServletRequest request) {
//		return CREATE;
//	}
//
//	@RequestMapping(value="/create", method=RequestMethod.POST)
//	public @ResponseBody String create(@Valid Olog olog) {
//		ologService.insertSelective(olog);
//		
//		return AjaxObject.newOk("添加成功！").toString();
//	}
//	
//	@RequestMapping(value="/update/{id}", method=RequestMethod.GET)
//	public String preUpdate(@PathVariable java.lang.Long id, Map<String, Object> map) {
//		Olog olog = ologService.selectByPrimaryKey(id);
//		map.put("olog", olog);
//		return EDIT;
//	}
//	
//	@RequestMapping(value="/update", method=RequestMethod.POST)
//	public @ResponseBody String update(@Valid @ModelAttribute("preloadOlog")Olog olog) {
//		ologService.updateByPrimaryKeySelective(olog);
//
//		return AjaxObject.newOk("修改成功！").toString();
//	}
//
//	@RequestMapping(value="/view/{id}", method={RequestMethod.GET})
//	public String view(@PathVariable java.lang.Long id, Map<String, Object> map) {
//		Olog olog = ologService.selectByPrimaryKey(id);
//		map.put("olog", olog);
//		return VIEW;
//	}
//
//	
//	@RequestMapping(value="/delete/{id}", method=RequestMethod.POST)
//	public @ResponseBody String delete(@PathVariable java.lang.Long id) {
//		
//		ologService.deleteByPrimaryKey(id);
//		return AjaxObject.newOk("删除成功！").setCallbackType("").toString();
//	}
//	
//	@RequestMapping(value="/delete", method=RequestMethod.POST)
//	public @ResponseBody String deleteMany(java.lang.Long[] items) {
//		for (int i = 0; i < items.length; i++) {
//			ologService.deleteByPrimaryKey(items[i]);
//		}
//		return AjaxObject.newOk("删除成功！").setCallbackType("").toString();
//	}
}
