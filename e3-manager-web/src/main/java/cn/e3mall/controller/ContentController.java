package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

@Controller
public class ContentController {
	@Autowired
	ContentService contentService;

	@RequestMapping(value = "/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentList(Long categoryId, int page, int rows) {
		return contentService.getContentList(categoryId, page, rows);
	}

	@RequestMapping(value = "/content/save", method = RequestMethod.POST)
	@ResponseBody
	public E3Result saveContent(TbContent tbContent) {
		return contentService.saveContent(tbContent);
	}

	@RequestMapping(value = "/rest/content/edit", method = RequestMethod.POST)
	@ResponseBody
	public E3Result editContent(TbContent tbContent) {
		return contentService.updateContent(tbContent);
	}

	@RequestMapping(value = "/content/delete", method = RequestMethod.POST)
	@ResponseBody
	public E3Result deleteContent(Long[] ids) {
		return contentService.deleteContent(ids);
	}

}
