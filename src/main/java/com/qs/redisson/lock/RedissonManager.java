package com.qs.redisson.lock;

import com.google.common.base.Preconditions;
import com.qs.redisson.constant.RedisConnectionType;
import com.qs.redisson.entity.RedissonProperties;
import com.qs.redisson.strategy.RedissonConfigService;
import com.qs.redisson.strategy.impl.ClusterConfigImpl;
import com.qs.redisson.strategy.impl.MasterslaveConfigImpl;
import com.qs.redisson.strategy.impl.SentineConfigImpl;
import com.qs.redisson.strategy.impl.StandaloneConfigImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;


/**
 * Redisson核心配置，用于提供初始化的redisson实例
 *
 * @author huqingsong
 */
@Slf4j
public class RedissonManager {


    private Redisson redisson;

    public RedissonManager() {

    }

    public RedissonManager(RedissonProperties redissonProperties) {
        try {
            //通过不同部署方式获得不同cofig实体
            Config config = RedissonConfigFactory.getInstance().createConfig(redissonProperties);
            redisson = (Redisson) Redisson.create(config);
        } catch (Exception e) {
            log.error("Redisson init error", e);
            throw new IllegalArgumentException("please input correct configurations," +
                    "connectionType must in standalone/sentinel/cluster/masterslave");
        }
    }

    public Redisson getRedisson() {
        return redisson;
    }

    /**
     * Redisson连接方式配置工厂
     * 双重检查锁
     */
    static class RedissonConfigFactory {

        private RedissonConfigFactory() {
        }

        private static volatile RedissonConfigFactory factory = null;

        public static RedissonConfigFactory getInstance() {
            if (factory == null) {
                synchronized (Object.class) {
                    if (factory == null) {
                        factory = new RedissonConfigFactory();
                    }
                }
            }
            return factory;
        }


        /**
         * 根据连接类型获取对应连接方式的配置,基于策略模式
         *
         * @param redissonProperties redis连接信息
         * @return Config
         */
        Config createConfig(RedissonProperties redissonProperties) {
            Preconditions.checkNotNull(redissonProperties);
            Preconditions.checkNotNull(redissonProperties.getAddress(), "redisson.lock.server.address cannot be NULL!");
            Preconditions.checkNotNull(redissonProperties.getType(), "redisson.lock.server.password cannot be NULL");
            String connectionType = redissonProperties.getType();
            // 声明配置上下文
            RedissonConfigService redissonConfigService;
            if (connectionType.equals(RedisConnectionType.STANDALONE.getConnectionType())) {
                redissonConfigService = new StandaloneConfigImpl();
            } else if (connectionType.equals(RedisConnectionType.SENTINEL.getConnectionType())) {
                redissonConfigService = new SentineConfigImpl();
            } else if (connectionType.equals(RedisConnectionType.CLUSTER.getConnectionType())) {
                redissonConfigService = new ClusterConfigImpl();
            } else if (connectionType.equals(RedisConnectionType.MASTERSLAVE.getConnectionType())) {
                redissonConfigService = new MasterslaveConfigImpl();
            } else {
                throw new IllegalArgumentException("创建Redisson连接Config失败！当前连接方式:" + connectionType);
            }
            return redissonConfigService.createRedissonConfig(redissonProperties);
        }
    }

}

