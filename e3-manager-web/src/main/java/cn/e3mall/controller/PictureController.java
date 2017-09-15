package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;

@Controller
public class PictureController {

	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	//响应结果才ContextType=text/plain;charset=UTF-8
	@RequestMapping(value = "/pic/upload", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
	@ResponseBody
	public String picUpload(MultipartFile uploadFile) {
		Map<String, Object> map = new HashMap<>();
		try {
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			String extName = uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf(".") + 1);
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			url = IMAGE_SERVER_URL + url;
			map.put("error", 0);
			map.put("url", url);
		} catch (Exception e) {
			map.clear();
			map.put("error", 1);
			map.put("message", e.getMessage());
		}
		return JsonUtils.objectToJson(map);
	}
}
