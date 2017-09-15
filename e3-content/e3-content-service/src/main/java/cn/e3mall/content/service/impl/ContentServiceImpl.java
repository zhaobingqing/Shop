package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;

	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;

	@Override
	public E3Result saveContent(TbContent tbContent) {
		Date date = new Date();
		tbContent.setCreated(date);
		tbContent.setUpdated(date);
		contentMapper.insert(tbContent);
		// 缓存同步，删除缓存中的数据
		try {
			jedisClient.hdel(CONTENT_LIST, String.valueOf(tbContent.getCategoryId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return E3Result.ok();
	}

	@Override
	public EasyUIDataGridResult getContentList(Long categoryId, int page, int rows) {
		if (page <= 0) {
			page = 1;
		}
		List<TbContent> list = null;
		PageHelper.startPage(page, rows);
		TbContentExample example = new TbContentExample();
		if (categoryId != 0) {
			list = contentMapper.selectByContent(categoryId);
		} else {
			list = contentMapper.selectByExampleWithBLOBs(example);
		}

		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);
		return result;
	}

	@Override
	public E3Result updateContent(TbContent tbContent) {
		Date date = new Date();
		tbContent.setUpdated(date);
		contentMapper.updateByPrimaryKeySelective(tbContent);
		// 缓存同步，删除缓存中的数据
		try {
			jedisClient.hdel(CONTENT_LIST, String.valueOf(tbContent.getCategoryId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return E3Result.ok();
	}

	@Override
	public E3Result deleteContent(Long[] ids) {
		for (Long id : ids) {
			TbContent content = contentMapper.selectByPrimaryKey(id);
			contentMapper.deleteByPrimaryKey(id);
			try {
				jedisClient.hdel(CONTENT_LIST, String.valueOf(content.getCategoryId()));
			} catch (Exception e) {
			e.printStackTrace();
			}
		}
		return E3Result.ok();
	}

	@Override
	public List<TbContent> getContentListByCid(long cid) {
		List<TbContent> list = null;
		// 先读取缓存
		try {
			String json = jedisClient.hget(CONTENT_LIST, String.valueOf(cid));
			if (StringUtils.isNotBlank(json)) {
				list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		list = contentMapper.selectByExampleWithBLOBs(example);
		try {
			// 添加缓存
			jedisClient.hset(CONTENT_LIST, String.valueOf(cid), JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
