package com.test.redis;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class TestPing {
	
	@Test
	public void testPingRedis() {
		Jedis jedis = new Jedis("192.168.1.116", 6379);
		System.out.println(jedis.ping());
	}
}
