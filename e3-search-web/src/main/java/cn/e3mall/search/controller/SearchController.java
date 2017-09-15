package cn.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;

@Controller
public class SearchController {
	@Autowired
	SearchService searchService;
	@Value("${SEARCH_RESULT_ROWS}")
	private Integer SEARCH_RESULT_ROWS;

	@RequestMapping(value = "/search")
	public String searchIndex(String keyword, @RequestParam(defaultValue = "1") Integer page, Model model) throws Exception {
		keyword = new String(keyword.getBytes("ISO-8859-1"), "UTF-8");
		SearchResult search = searchService.search(keyword, page, SEARCH_RESULT_ROWS);
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages", search.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("recourdCount", search.getRecordCount());
		model.addAttribute("itemList", search.getItemList());
		return "search";
	}

}
