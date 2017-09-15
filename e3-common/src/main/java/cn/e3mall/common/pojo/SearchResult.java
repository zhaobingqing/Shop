package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5681099062531575064L;

	/**
	 * 总记录数
	 */
	private long recordCount;
	/**
	 * 总页数
	 */
	private int totalPages;
	/**
	 * 记录
	 */
	private List<SearchItem> itemList;

	public long getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List<SearchItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}

}
