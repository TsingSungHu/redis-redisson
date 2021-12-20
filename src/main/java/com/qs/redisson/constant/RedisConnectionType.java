package com.qs.redisson.constant;

/**
 * Redis连接方式
 * 包含:standalone-单节点部署方式
 * sentinel-哨兵部署方式
 * cluster-集群方式
 * masterslave-主从部署方式
 *
 * @author huqingsong
 */
public enum RedisConnectionType {

    /**  **/
    STANDALONE("standalone", "单节点部署方式"),
    SENTINEL("sentinel", "哨兵部署方式"),
    CLUSTER("cluster", "集群方式"),
    MASTERSLAVE("masterslave", "主从部署方式");

    private final String connectionType;
    private final String connectionDesc;

    RedisConnectionType(String connectionType, String connectionDesc) {
        this.connectionType = connectionType;
        this.connectionDesc = connectionDesc;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public String getConnectionDesc() {
        return connectionDesc;
    }
}
