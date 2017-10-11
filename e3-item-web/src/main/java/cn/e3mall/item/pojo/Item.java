package cn.e3mall.item.pojo;

import org.apache.commons.lang3.StringUtils;

import cn.e3mall.pojo.TbItem;

public class Item extends TbItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3065295651656404305L;

	public Item(TbItem item) {
		this.setId(item.getId());
		this.setTitle(item.getTitle());
		this.setSellPoint(item.getSellPoint());
		this.setPrice(item.getPrice());
		this.setNum(item.getNum());
		this.setBarcode(item.getBarcode());
		this.setImage(item.getImage());
		this.setCid(item.getCid());
		this.setStatus(item.getStatus());
		this.setCreated(item.getCreated());
		this.setUpdated(item.getUpdated());
	}

	public String[] getImages() {
		String image = this.getImage();
		if (StringUtils.isNotBlank(image)) {
			String[] strings = image.split(",");
			return strings;
		}
		return null;
	}
}
