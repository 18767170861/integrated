package org.wmc.integrated.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.wmc.integrated.zookeeper.ZookeeperUtil;

import java.util.List;

public class CuratorCrud<T> {
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(2000, 5);//重试策略
    //工厂创建连接
    CuratorFramework cf= CuratorFrameworkFactory.builder()
            .connectString(ZookeeperUtil.connectString)
            .sessionTimeoutMs(ZookeeperUtil.sessionTimeout)
            .retryPolicy(new ExponentialBackoffRetry(2000, 5))
            .build();

    //CuratorFramework cf=CuratorFrameworkFactory.newClient(ZookeeperUtil.connectString,new RetryOneTime(1));

    public CuratorCrud() {
        cf.start();//链接
    }

    /***
     * 递归建立节点 路径、数据内容
     * @param path
     * @param data
     */
    public void create(String path,byte[] data){
        System.out.println("递归建立节点 路径、数据内容:" + path);
        try {
            cf.create().creatingParentsIfNeeded().forPath(path,data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * 删除节点
     * deletingChildrenIfNeeded 是否删除子节点 递归
     * @param path
     */
    public void delete(String path){
        try {
            cf.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * 获取资源
     * @param path
     * @return
     */
    public byte[] getData(String path){
        try {
            return cf.getData().forPath(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * 更新资源
     * @param path
     * @param bytes
     */
    public void setData(String path,byte bytes[]){
        try {
            cf.setData().forPath(path,bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * 节点是否存在
     * @param path
     */
    public Stat checkExists(String path){
        try {
            return cf.checkExists().forPath(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * 获取子节点
     * @param path
     */
    public List<String> getChildren(String path){
        try {
            return cf.getChildren().forPath(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}