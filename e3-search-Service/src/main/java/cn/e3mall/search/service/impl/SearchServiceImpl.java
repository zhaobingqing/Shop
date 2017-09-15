package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {
	@Autowired
	private SearchDao searchDao;

	@Override
	public SearchResult search(String keyword, int page, int rows) throws Exception {
		if (page <= 0) {
			page = 1;
		}
		SearchResult searchResult = null;
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery(keyword);
		solrQuery.setStart((page - 1) * rows);
		solrQuery.setRows(rows);
		solrQuery.set("df", "item_title");
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<span style='color:red'");
		solrQuery.setHighlightSimplePost("</span>");
		searchResult = searchDao.search(solrQuery);
		long recordCount = searchResult.getRecordCount();
		int totalPage = (int) (recordCount / rows);
		if (recordCount % rows > 0) {
			totalPage++;
		}
		searchResult.setTotalPages(totalPage);
		return searchResult;
	}
}
