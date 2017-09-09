package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;

/**
 * @author ntjr 商品管理Service
 *
 */
public interface ItemService {

	TbItem getItemById(long itemId);

	EasyUIDataGridResult getItemList(int page, int rows);

}
