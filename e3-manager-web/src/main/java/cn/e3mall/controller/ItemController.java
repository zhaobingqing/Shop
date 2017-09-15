package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
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

	@RequestMapping(value = "/item/save", method = RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem item, String desc) {
		return itemService.addItem(item, desc);
	}

	@RequestMapping(value = "/rest/item/delete", method = RequestMethod.POST)
	@ResponseBody
	public E3Result deleteItem(String ids) {
		return itemService.deleteItem(ids);
	}

	@RequestMapping(value = "/rest/item/instock", method = RequestMethod.POST)
	@ResponseBody
	public E3Result instockItem(String ids) {
		return itemService.instockItem(ids);
	}

	@RequestMapping(value = "/rest/item/reshelf", method = RequestMethod.POST)
	@ResponseBody
	public E3Result reshelfItem(String ids) {
		return itemService.reshelfItem(ids);
	}

	@RequestMapping(value = "/rest/item/query/item/desc/{id}")
	@ResponseBody
	public E3Result queryItemDesc(@PathVariable("id") long id) {
		E3Result result = itemService.queryItemDesc(id);
		return result;
	}

}
