package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {
	@Autowired
	private TbUserMapper tbUserMapper;

	@Override
	public E3Result checkData(String param, int type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		// 用户名
		if (type == 1)
			criteria.andUsernameEqualTo(param);
		// 手机号
		if (type == 2)
			criteria.andPhoneEqualTo(param);
		// 邮箱
		if (type == 3)
			criteria.andEmailEqualTo(param);
		List<TbUser> result = tbUserMapper.selectByExample(example);
		if (result != null && !result.isEmpty()) {
			return E3Result.ok(false);
		}
		return E3Result.ok(true);
	}

	@Override
	public E3Result register(TbUser user) {
		// 对数据有效应进行校验
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getPhone())) {
			return E3Result.build(400, "用户数据不完整，注册失败");
		}
		if (!(boolean) checkData(user.getUsername(), 1).getData()) {
			return E3Result.build(400, "此用户名已经被占用");
		}
		if (!(boolean) checkData(user.getPhone(), 2).getData()) {
			return E3Result.build(400, "该手机号码已经注册");
		}
		Date date = new Date();
		user.setCreated(date);
		user.setUpdated(date);
		user.setEmail(null);
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		tbUserMapper.insert(user);
		return E3Result.ok();
	}
}
