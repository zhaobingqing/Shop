package cn.e3mall.order.pojo;

import java.util.List;

import cn.e3mall.pojo.TbOrder;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;

/**
 * @author ntjr 订单信息
 */
public class OrderInfo extends TbOrder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2291418395035719230L;

	private List<TbOrderItem> orderItems;
	private TbOrderShipping orderShipping;

	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}

	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}

	@Override
	public String toString() {
		return "OrderInfo [orderItems=" + orderItems + ", orderShipping=" + orderShipping + ", getOrderItems()=" + getOrderItems() + ", getOrderShipping()=" + getOrderShipping() + ", getOrderId()=" + getOrderId() + ", getPayment()="
				+ getPayment() + ", getPaymentType()=" + getPaymentType() + ", getPostFee()=" + getPostFee() + ", getStatus()=" + getStatus() + ", getCreateTime()=" + getCreateTime() + ", getUpdateTime()=" + getUpdateTime()
				+ ", getPaymentTime()=" + getPaymentTime() + ", getConsignTime()=" + getConsignTime() + ", getEndTime()=" + getEndTime() + ", getCloseTime()=" + getCloseTime() + ", getShippingName()=" + getShippingName()
				+ ", getShippingCode()=" + getShippingCode() + ", getUserId()=" + getUserId() + ", getBuyerMessage()=" + getBuyerMessage() + ", getBuyerNick()=" + getBuyerNick() + ", getBuyerRate()=" + getBuyerRate() + ", toString()="
				+ super.toString() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
	}

}
