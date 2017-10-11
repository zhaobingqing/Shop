package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;

/**
 * @author ntjr 内容分类
 */
@Controller
public class ContentCatController {

	@Autowired
	ContentCategoryService contentCategoryService;

	/**
	 * @param id
	 * @return 获取分类列表
	 */
	@RequestMapping(value = "/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentcatList(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
		return contentCategoryService.getContentCatList(id);
	}

	/**
	 * 添加分类节点
	 */
	@RequestMapping(value = "/content/category/create", method = RequestMethod.POST)
	@ResponseBody
	public E3Result addContentcat(@RequestParam(value = "parentId", required = true) Long parentId, @RequestParam(value = "name", required = true) String name) {
		return contentCategoryService.addContentCategory(parentId, name);
	}
	@RequestMapping(value = "/content/category/delete", method = RequestMethod.POST)
	@ResponseBody
	public E3Result deleteContentcat(@RequestParam(value = "id", required = true) Long id) {
		return contentCategoryService.deleteContentcat(id);
	}
	@RequestMapping(value = "/content/category/update", method = RequestMethod.POST)
	@ResponseBody
	public E3Result updateContentcat(@RequestParam(value = "id", required = true) Long id,@RequestParam(value = "name", required = true) String name) {
		return contentCategoryService.updateContentcat(id,name);
	}

}
