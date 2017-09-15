package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;

public interface ContentCategoryService {
	
	List<EasyUITreeNode> getContentCatList(long partentId);
	
	E3Result addContentCategory(long parentId,String name);

	E3Result deleteContentcat(Long id);

	E3Result updateContentcat(Long id, String name);

}
