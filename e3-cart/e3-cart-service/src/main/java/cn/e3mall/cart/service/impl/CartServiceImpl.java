package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;

/**
 * 购物车处理服务
 */
@Service
public class CartServiceImpl implements CartService {
	private final static Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
	@Autowired
	private JedisClient jedisClient;

	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;

	@Autowired
	private TbItemMapper itemMapper;

	@Override
	public E3Result addCart(Long userId, Long itemId, Integer num) {
		logger.info("保存购物车数据进入redis");
		boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
		TbItem tbItem = null;
		if (hexists) {
			String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
			tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			tbItem.setNum(tbItem.getNum() + num);
		} else {
			tbItem = itemMapper.selectByPrimaryKey(itemId);
			tbItem.setNum(num);
			String image = tbItem.getImage();
			if (StringUtils.isNotBlank(image)) {
				tbItem.setImage(image.split(",")[0]);
			}
		}
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}

	@Override
	public E3Result mergeCart(Long userId, List<TbItem> itemList) {
		// 遍历列表
		for (TbItem tbItem : itemList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		return E3Result.ok();
	}

	@Override
	public List<TbItem> getCartList(Long userId) {
		List<String> json = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
		List<TbItem> list = new ArrayList<>();
		for (String str : json) {
			TbItem tbItem = JsonUtils.jsonToPojo(str, TbItem.class);
			list.add(tbItem);
		}
		return list;
	}

	@Override
	public E3Result updateCartNum(Long userId, Long itemId, Integer num) {
		String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
		TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
		tbItem.setNum(num);
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}

	@Override
	public E3Result deleteCartItem(Long userId, Long itemId) {
		jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
		return E3Result.ok();
	}

	@Override
	public E3Result clearCartItem(Long userId) {
		jedisClient.del(REDIS_CART_PRE + ":" + userId);
		return E3Result.ok();
	}

}
