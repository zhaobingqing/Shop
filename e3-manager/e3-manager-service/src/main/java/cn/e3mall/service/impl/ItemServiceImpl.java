package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;

	@Override
	public TbItem getItemById(long itemId) {
		// 按主键查询
		// return tbItemMapper.selectByPrimaryKey(itemId);
		// 按条件查询
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = tbItemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
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

}
