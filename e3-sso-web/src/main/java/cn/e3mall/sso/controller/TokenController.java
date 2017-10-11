package cn.e3mall.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.sso.service.TokenService;

@Controller
public class TokenController {
	private final static Logger logger = LoggerFactory.getLogger(TokenController.class);

	@Autowired
	private TokenService tokenService;

	/**
	 * @param token
	 * @param callback 使用jsonP
	 * @return 
	 * jsonP请求方法1，可兼容spring4.1之前版本
	 */
	/*@RequestMapping("/user/token/{token}")
	@ResponseBody
	public String getUserByToken(@PathVariable("token") String token, String callback) {
		E3Result result = tokenService.getUserByToken(token);
		logger.info("获取缓存用户：" + token);
		//响应结果之前判断是不是jsonp请求
		if (StringUtils.isNotBlank(callback)) {
			return callback + "(" + JsonUtils.objectToJson(result) + ");";
		}
		return JsonUtils.objectToJson(result);
	}*/
	
	/**
	 * @param token
	 * @param callback
	 * @return jsonP请求 只能适应于Spring 4.1之后版本
	 */
	@RequestMapping("/user/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable("token") String token, String callback) {
		E3Result result = tokenService.getUserByToken(token);
		logger.info("获取缓存用户：" + token);
		//响应结果之前判断是不是jsonp请求
		if (StringUtils.isNotBlank(callback)) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return result;
	}
}
