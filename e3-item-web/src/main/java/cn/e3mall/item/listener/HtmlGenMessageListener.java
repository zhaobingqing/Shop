package cn.e3mall.item.listener;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author ntjr 监听添加商品消息，创建静态页面
 *
 */
public class HtmlGenMessageListener implements MessageListener {
	private static final Logger logger = LoggerFactory.getLogger(HtmlGenMessageListener.class);
	@Autowired
	ItemService itemService;
	@Autowired
	FreeMarkerConfigurer freeMarkerConfigurer;
	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;
	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			Long itemId = textMessage.getLongProperty("itemId");
			//等待添加商品事物提交
			Thread.sleep(1000);
			TbItem tbItem = itemService.getItemById(itemId);
			TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
			Item item = new Item(tbItem);
			//创建一个数据集，把商品数据封装
			Map<String,Object> data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", tbItemDesc);
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			Writer out = new FileWriter(HTML_GEN_PATH+itemId+".html");
			template.process(data, out);
			out.close();
			logger.info("生成商品详情页面页面。。。。。。"+itemId);
		} catch (Exception e) {
			logger.info("生成商品详情页面异常", e);
			e.printStackTrace();
		}
	}
}
