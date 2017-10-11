package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	public final static Logger logger = LoggerFactory.getLogger(ItemCatServiceImpl.class);

	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;

	// 商品数据在Redis中缓存的前缀
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	// 商品数据在Redis中过期的时间
	@Value("${ITEM_INFO_EXPIRE}")
	private Integer ITEM_INFO_EXPIRE;

	@Override
	public TbItem getItemById(long itemId) {
		// 查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE + itemId + ":BASE");
			if (StringUtils.isNotBlank(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			logger.info("获取缓存失败。。。", e);
		}

		// 按主键查询
		// return tbItemMapper.selectByPrimaryKey(itemId);
		// 按条件查询
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = tbItemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			// 添加缓存
			try {
				jedisClient.set(REDIS_ITEM_PRE + itemId + ":BASE", JsonUtils.objectToJson(list.get(0)));
				jedisClient.expire(REDIS_ITEM_PRE + itemId + ":BASE", ITEM_INFO_EXPIRE);
			} catch (Exception e) {
				logger.info("添加缓存失败。。。", e);
			}
			return list.get(0);
		}
		return null;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		if (page <= 0) {
			page = 1;
		}
		PageHelper.startPage(page, rows);
		TbItemExample example = new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(example);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);
		return result;
	}

	@Override
	public E3Result addItem(TbItem item, String desc) {
		Date date = new Date();
		long id = IDUtils.genItemId();
		item.setId(id);
		item.setStatus((byte) 1);
		item.setUpdated(date);
		item.setCreated(date);
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(id);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(date);
		itemDesc.setUpdated(date);
		tbItemMapper.insert(item);
		tbItemDescMapper.insert(itemDesc);
		jmsTemplate.send(topicDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage();
				textMessage.setLongProperty("itemId", id);
				return textMessage;
			}
		});
		return E3Result.ok();
	}

	@Override
	public E3Result deleteItem(String ids) {
		if (!StringUtils.isNotBlank(ids)) {
			return E3Result.build(-1, "请选择删除的商品");
		}

		TbItem tbItem = new TbItem();
		tbItem.setStatus((byte) 3);
		if (ids.contains(",")) {
			String[] split = ids.split(",");
			for (String id : split) {
				tbItem.setId(Long.valueOf(id));
				tbItemMapper.updateByPrimaryKeySelective(tbItem);
			}

		} else {
			tbItem.setId(Long.valueOf(ids));
			tbItemMapper.updateByPrimaryKeySelective(tbItem);
		}
		return E3Result.ok();
	}

	@Override
	public E3Result instockItem(String ids) {
		if (!StringUtils.isNotBlank(ids)) {
			return E3Result.build(-1, "请选择下架的商品");
		}
		TbItem tbItem = new TbItem();
		tbItem.setStatus((byte) 2);
		if (ids.contains(",")) {
			String[] split = ids.split(",");
			for (String id : split) {
				tbItem.setId(Long.valueOf(id));
				tbItemMapper.updateByPrimaryKeySelective(tbItem);
			}

		} else {
			tbItem.setId(Long.valueOf(ids));
			tbItemMapper.updateByPrimaryKeySelective(tbItem);
		}
		return E3Result.ok();
	}

	@Override
	public E3Result reshelfItem(String ids) {
		if (!StringUtils.isNotBlank(ids)) {
			return E3Result.build(-1, "请选择下架的商品");
		}
		TbItem tbItem = new TbItem();
		tbItem.setStatus((byte) 1);
		if (ids.contains(",")) {
			String[] split = ids.split(",");
			for (String id : split) {
				tbItem.setId(Long.valueOf(id));
				tbItemMapper.updateByPrimaryKeySelective(tbItem);
			}
		} else {
			tbItem.setId(Long.valueOf(ids));
			tbItemMapper.updateByPrimaryKeySelective(tbItem);
		}
		return E3Result.ok();
	}

	@Override
	public E3Result queryItemDesc(long id) {
		TbItemDesc desc = tbItemDescMapper.selectByPrimaryKey(id);
		return E3Result.ok(desc);
	}

	@Override
	public TbItemDesc getItemDescById(Long itemId) {
		// 查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE + itemId + ":DESC");
			if (StringUtils.isNotBlank(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			logger.info("获取缓存失败。。。", e);
		}
		TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		try {
			jedisClient.set(REDIS_ITEM_PRE + itemId + ":DESC", JsonUtils.objectToJson(tbItemDesc));
			jedisClient.expire(REDIS_ITEM_PRE + itemId + ":DESC", ITEM_INFO_EXPIRE);
		} catch (Exception e) {
			logger.info("添加缓存失败。。。", e);
		}
		return tbItemDesc;
	}

}
