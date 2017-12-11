package com.b.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wangbil on 10/13/2016.
 */
public class JedisSentinelDemo {
    public static void main(String args[]){
        Set<String> sentinels = new HashSet<String>();
        sentinels.add("<IP>:<PORT>");
        sentinels.add("<IP>:<PORT>");
        JedisSentinelPool poolStats = new JedisSentinelPool("<NAME1>", sentinels);
        JedisSentinelPool poolTokens = new JedisSentinelPool("<NAME2>", sentinels);
        Jedis jedisA = poolStats.getResource();
        Jedis jedisB = poolTokens.getResource();
        System.out.println("jedisA key:"+jedisA.ping());
        System.out.println("jedisB key:"+jedisB.ping());
    }
}
