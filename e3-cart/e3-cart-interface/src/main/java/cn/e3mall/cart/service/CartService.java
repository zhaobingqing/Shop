package cn.e3mall.cart.service;

import java.util.List;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;

public interface CartService {

	/**
	 * redis中缓存购物车信息
	 * 
	 * @param userId
	 *            用户ID
	 * @param itemId
	 *            商品ID
	 * @param num
	 *            商品数量
	 * @return
	 */
	E3Result addCart(Long userId, Long itemId, Integer num);

	E3Result mergeCart(Long userId, List<TbItem> itemList);

	List<TbItem> getCartList(Long userId);

	E3Result updateCartNum(Long userId, Long itemId, Integer num);

	E3Result deleteCartItem(Long userId, Long itemId);
	
	E3Result clearCartItem(Long userId);

}
