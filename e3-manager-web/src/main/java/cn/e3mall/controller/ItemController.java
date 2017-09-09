package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;

	@RequestMapping(value = "/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable long itemId) {
		return itemService.getItemById(itemId);
	}

	@RequestMapping(value = "/item/list", params = { "page", "rows" }, method = RequestMethod.GET)
	@ResponseBody
	public EasyUIDataGridResult getItemList(@RequestParam int page, @RequestParam int rows) {
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}

}
