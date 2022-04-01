package org.wmc.integrated.zookeeper;

public class ZookeeperUtil {

    /** zookeeper服务器地址 多个地址“,”隔开 */
    public static final String connectString = "192.168.110.100:2181";
    /** 定义session失效时间 */
    public static final int sessionTimeout = 5000;
    public static final String path = "/root";
}
