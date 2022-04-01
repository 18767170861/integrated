package org.wmc.integrated.lfu;

import java.util.*;

public class LFU<K, V> {

    // 总容量
    private int capacity;
    // 所有的node节点
    private Map<K, Node> caches;

    public LFU(int capacity) {
        this.capacity = capacity;
        caches = new LinkedHashMap<>(capacity);
    }

    public V get(K key) {
        Node node = caches.get(key);
        if (Objects.isNull(node)) {
            return null;
        }
        node.setCount(node.getCount() + 1);
        node.setTime(System.nanoTime());
        sort();
        return (V) node.getValue();
    }

    public void put(K key, V value) {
        Node node = caches.get(key);
        // 如果是新元素
        if (Objects.isNull(node)) {
            // 移除count计数最小的那个key\
            if (caches.size() >= capacity) {
                K lastKey = removeLeastCount();
                caches.remove(lastKey);
            }
            // 创建新节点
            node = new Node(key, value, System.nanoTime(), 1);
            caches.put(key, node);
        } else { // 已经存在的元素覆盖旧值
            node.setValue(value);
            node.setTime(System.nanoTime());
            node.setCount(node.getCount() + 1);
        }
        sort();
    }

    private void sort() {
        List<Map.Entry<K, Node>> list = new ArrayList<>(caches.entrySet());
        Collections.sort(list, ((o1, o2) -> o2.getValue().compareTo(o1.getValue())));
        caches.clear();
        for (Map.Entry<K, Node> kNodeEntry : list) {
            caches.put(kNodeEntry.getKey(), kNodeEntry.getValue());
        }
    }

    private K removeLeastCount() {
        return (K) Collections.min(caches.values()).getKey();
    }


    public static void main(String[] args) {
        LFU<Integer, String> lruCache = new LFU<>(5);
        lruCache.put(1, "A");
        lruCache.put(2, "B");
        lruCache.put(3, "C");
        lruCache.put(4, "D");
        lruCache.put(5, "E");
        lruCache.put(6, "F");
        lruCache.get(2);
        lruCache.get(2);
        lruCache.get(3);
        lruCache.get(6);
        //重新put节点3
        lruCache.put(3, "C");
        final Map<Integer, Node> caches = lruCache.caches;
        for (Map.Entry<Integer, Node> nodeEntry : caches.entrySet()) {
            System.out.println(nodeEntry.getValue().getValue() + "...count:" + nodeEntry.getValue().getCount() + "...time:" + nodeEntry.getValue().getTime());
        }

    }
}
