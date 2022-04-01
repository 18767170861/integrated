package org.wmc.integrated.lfu;

import lombok.Data;

@Data
public class Node implements Comparable<Node> {

    // 键
    private Object key;
    // 值
    private Object value;
    // 访问时间
    private long time;
    // 访问次数
    private int count;

    public Node(Object key, Object value, long time, int count) {
        this.key = key;
        this.value = value;
        this.time = time;
        this.count = count;
    }

    @Override
    public int compareTo(Node o) {
        int compare = Integer.compare(this.count, o.getCount());
        // 在数目相同的情况下比较时间
        if (compare == 0) {
            return Long.compare(this.time, o.getTime());
        }
        return compare;
    }

}
