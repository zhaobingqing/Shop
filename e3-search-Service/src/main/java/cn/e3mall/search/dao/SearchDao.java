package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;

@Repository
public class SearchDao {

	@Autowired
	private SolrServer solrServer;

	public SearchResult search(SolrQuery query) throws Exception {
		SearchResult result = new SearchResult();
		QueryResponse queryResponse = solrServer.query(query);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		result.setRecordCount(solrDocumentList.getNumFound());
		// 取高亮显示
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		List<SearchItem> searchItems = new ArrayList<>();
		for (SolrDocument solrDocument : solrDocumentList) {
			String title = null;
			List<String> list = highlighting.get((String) solrDocument.get("id")).get("item_title");
			if (list != null && !list.isEmpty()) {
				title = list.get(0);
			}
			if (!StringUtils.isNotBlank(title)) {
				title = (String) solrDocument.get("item_title");
			}
			SearchItem searchItem = new SearchItem();
			searchItem.setId((String) solrDocument.get("id"));
			searchItem.setTitle(title);
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setPrice((long) solrDocument.get("item_price"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			searchItems.add(searchItem);
		}
		result.setItemList(searchItems);
		return result;
	}

}
