package cn.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	@Autowired
	private JedisClient JedisClient;

	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;

	@Value("${ORDER_ID_START}")
	private String ORDER_ID_START;

	@Value("${ORDER_DETAIL_ID_GEN_KEY}")
	private String ORDER_DETAIL_ID_GEN_KEY;

	@Override
	public E3Result createOrder(OrderInfo orderInfo) {
		if (!JedisClient.exists(ORDER_ID_GEN_KEY)) {
			JedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_START);
		}
		String orderId = JedisClient.incr(ORDER_ID_GEN_KEY).toString();
		orderInfo.setOrderId(orderId);
		// 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		// 插入订单
		orderMapper.insert(orderInfo);
		// 订单明细
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			// 明细id
			String odID = JedisClient.incr(ORDER_DETAIL_ID_GEN_KEY).toString();
			tbOrderItem.setId(odID);
			tbOrderItem.setOrderId(orderId);
			// 明细表插入数据
			orderItemMapper.insert(tbOrderItem);
		}
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShippingMapper.insert(orderShipping);
		return E3Result.ok(orderId);
	}

}
