package cn.e3mall.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.mapper.TbItemMapper;

/**
 * @author ntjr
 * 监听添加商品消息，同步索引库
 *
 */
public class ItemAddMessageListener implements MessageListener {
	private static final Logger logger = LoggerFactory.getLogger(ItemAddMessageListener.class);
	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private SolrServer solrServer;
	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			Long itemId = textMessage.getLongProperty("itemId");
			logger.info("onMessage:itemId=" + itemId);
			//等待添加商品事物提交之后，再向数据库查询，否则存在，查询不到数据的情况，因为添加商品的事物还没提交
			Thread.sleep(200);
			SearchItem searchItem = tbItemMapper.getItemById(itemId);
			logger.info("searchItem=" + searchItem.toString());
			SolrInputDocument document = new SolrInputDocument();
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			solrServer.add(document);
			solrServer.commit();
		} catch (Exception e) {
			logger.info("索引库添加商品异常", e);
			e.printStackTrace();
		}

	}

}
