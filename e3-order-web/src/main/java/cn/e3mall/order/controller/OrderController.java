package cn.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;

@Controller
public class OrderController {
	private final static Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/order/order-cart")
	public String showOrderCart(HttpServletRequest request, ModelMap modelMap) {
		TbUser user = (TbUser) request.getAttribute("user");
		List<TbItem> cartList = cartService.getCartList(user.getId());
		modelMap.addAttribute("cartList", cartList);
		return "order-cart";
	}

	@RequestMapping(value = "/order/create", method = RequestMethod.POST)
	public String createOrder(HttpServletRequest request, OrderInfo orderInfo) {
		TbUser user = (TbUser) request.getAttribute("user");
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		E3Result result = orderService.createOrder(orderInfo);
		if (result.getStatus() == 200) {
			// 清空购物车
			cartService.clearCartItem(user.getId());
		}
		logger.info("===OrderInfo==" + orderInfo.toString());
		request.setAttribute("orderId", result.getData());
		request.setAttribute("payment", orderInfo.getPayment());
		return "success";
	}

}
