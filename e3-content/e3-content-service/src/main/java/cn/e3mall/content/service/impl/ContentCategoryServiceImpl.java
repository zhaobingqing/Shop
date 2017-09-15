package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

/**
 * @author ntjr 内容分类管理Service
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	/**
	 * 根据父ID获取子节点
	 */
	@Override
	public List<EasyUITreeNode> getContentCatList(long partentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(partentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> nodeList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent() ? "closed" : "open");
			nodeList.add(node);
		}
		return nodeList;
	}

	/**
	 * 添加节点
	 */
	@Override
	public E3Result addContentCategory(long parentId, String name) {
		Date date = new Date();
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setIsParent(false);
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		contentCategory.setCreated(date);
		contentCategory.setUpdated(date);
		contentCategory.setSortOrder(1);
		contentCategoryMapper.insertSelective(contentCategory);
		// 查询父节点
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parent.getIsParent()) {
			parent.setIsParent(true);
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		return E3Result.ok(contentCategory);
	}

	/*
	 * 删除节点
	 */
	@Override
	public E3Result deleteContentcat(Long id) {
		TbContentCategory cat = contentCategoryMapper.selectByPrimaryKey(id);
		if (cat.getIsParent()) {
			return E3Result.build(-1, "不能删除父节点");
		}
		contentCategoryMapper.deleteByPrimaryKey(id);
		Long parentId = cat.getParentId();
		int childCount = contentCategoryMapper.selectChildCount(parentId);
		if (childCount == 0) {
			TbContentCategory parent = new TbContentCategory();
			parent.setId(parentId);
			parent.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKeySelective(parent);
		}
		return E3Result.ok();
	}

	/*
	 * 重命名
	 */
	@Override
	public E3Result updateContentcat(Long id, String name) {
		TbContentCategory record = new TbContentCategory();
		record.setId(id);
		record.setName(name);
		contentCategoryMapper.updateByPrimaryKeySelective(record);
		return E3Result.ok();
	}

}
