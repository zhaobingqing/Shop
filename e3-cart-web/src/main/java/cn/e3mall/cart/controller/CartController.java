package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

@Controller
public class CartController {
	private final static Logger logger = LoggerFactory.getLogger(CartController.class);
	@Autowired
	private ItemService itemService;
	// cookie保存时间
	@Value("${COOKIE_CART_EXPIRE}")
	private int COOKIE_CART_EXPIRE;
	// 购物车服务，保存进redis
	@Autowired
	private CartService cartService;

	/**
	 * 加入购物车
	 * 
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/cart/add/{itemId}")
	public String addCart(@PathVariable("itemId") Long itemId, @RequestParam(value = "num", defaultValue = "1") Integer num, HttpServletRequest request, HttpServletResponse response) {
		// 判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");

		// 登录状态购物车保存进redis
		if (user != null) {
			logger.info("登录状态保存购物车");
			cartService.addCart(user.getId(), itemId, num);
			return "cartSuccess";
		}
		logger.info("非登录状态保存购物车");
		// 非登录状态
		List<TbItem> cartList = getCartListFromCookie(request);
		boolean flag = false;
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId.longValue()) {
				flag = true;
				tbItem.setNum(tbItem.getNum() + num);
				break;
			}
		}
		// cookie中不存在，根据商品ID查询商品信息
		if (!flag) {
			TbItem tbItem = itemService.getItemById(itemId);
			tbItem.setNum(num);
			String image = tbItem.getImage();
			if (StringUtils.isNotBlank(image)) {
				tbItem.setImage(image.split(",")[0]);
			}
			cartList.add(tbItem);
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		return "cartSuccess";
	}

	// 从Cookie中取购物车列表
	private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
		String json = CookieUtils.getCookieValue(request, "cart", true);
		if (StringUtils.isBlank(json)) {
			return new ArrayList<TbItem>();
		}
		return JsonUtils.jsonToList(json, TbItem.class);
	}

	/**
	 * 展示购物车列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request, HttpServletResponse response) {
		List<TbItem> cartList = getCartListFromCookie(request);
		// 判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.mergeCart(user.getId(), cartList);
			CookieUtils.deleteCookie(request, response, "cart");
			cartList = cartService.getCartList(user.getId());
		}
		request.setAttribute("cartList", cartList);
		return "cart";
	}

	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartNum(@PathVariable("itemId") Long itemId, @PathVariable("num") Integer num, HttpServletRequest request, HttpServletResponse response) {
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			return cartService.updateCartNum(user.getId(), itemId, num);
		}
		List<TbItem> cartList = getCartListFromCookie(request);
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId.longValue()) {
				tbItem.setNum(num);
				break;
			}
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		return E3Result.ok();
	}

	/**
	 * 删除购物车商品
	 * 
	 * @param itemId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable("itemId") Long itemId, HttpServletRequest request, HttpServletResponse response) {
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		List<TbItem> cartList = getCartListFromCookie(request);
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId.longValue()) {
				cartList.remove(tbItem);
				break;
			}
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		return "redirect:/cart/cart.html";
	}
}
