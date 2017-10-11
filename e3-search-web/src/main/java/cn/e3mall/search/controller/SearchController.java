package cn.e3mall.search.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private  final static Logger logger = LoggerFactory.getLogger(SearchController.class);
	
	@Autowired
	SearchService searchService;
	@Value("${SEARCH_RESULT_ROWS}")
	private Integer SEARCH_RESULT_ROWS;

	@RequestMapping(value = "/search")
	public String searchIndex(String keyword, @RequestParam(defaultValue = "1") Integer page, Model model) throws Exception {
		keyword = new String(keyword.getBytes("ISO-8859-1"), "UTF-8");
		logger.info("keyWords="+keyword);
		SearchResult search = searchService.search(keyword, page, SEARCH_RESULT_ROWS);
		logger.info("SearchResult="+search.toString());
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages", search.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("recourdCount", search.getRecordCount());
		model.addAttribute("itemList", search.getItemList());
		return "search";
	}

}
