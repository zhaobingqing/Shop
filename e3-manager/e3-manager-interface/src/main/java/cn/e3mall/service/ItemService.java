package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;

/**
 * @author ntjr 商品管理Service
 *
 */
public interface ItemService {

	TbItem getItemById(long itemId);

	EasyUIDataGridResult getItemList(int page, int rows);

	E3Result addItem(TbItem item, String desc);
	
	/**
	 * @param ids
	 * @return
	 * 删除-3
	 */
	E3Result deleteItem(String ids);
	
	/**
	 * @param ids
	 * @return
	 * 下架-2
	 */
	E3Result instockItem(String ids);
	
	/**
	 * @param dis
	 * @return
	 * 上架 1
	 */
	E3Result reshelfItem(String dis);

	
	/**
	 * @param id
	 * @return
	 * 获取商品描述
	 */
	E3Result queryItemDesc(long id);
	
}
