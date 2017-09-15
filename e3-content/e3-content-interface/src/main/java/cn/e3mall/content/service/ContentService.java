package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbContent;

public interface ContentService {

	public E3Result saveContent(TbContent tbContent);

	EasyUIDataGridResult getContentList(Long categoryId, int page, int rows);

	public E3Result updateContent(TbContent tbContent);

	public E3Result deleteContent(Long[] ids);

	List<TbContent> getContentListByCid(long cid);

}
