package cn.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {
	@Autowired
	private JedisClient jedisClient;

	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	@Override
	public E3Result getUserByToken(String token) {
		String json = jedisClient.get("SESSION:" + token);
		if (StringUtils.isBlank(json)) {
			return E3Result.build(201, "token过期");
		}
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
		return E3Result.ok(user);
	}
}
