package demo2;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache {

    private Map<Integer, Integer> map;
    private final int CAPACITY;

    public LRUCache(int capacity) {
        CAPACITY = capacity;
        map = new LinkedHashMap<Integer, Integer>(capacity, 0.75f, true) {
            // 这个方法将会在有一条新的映射条目被插入到映射实例之后被put方法和putALL方法调用，
            // 这里覆盖LinkedHashMap中的removeEldestEntry的方法，
            // 使得当LinkedHashMap中的条目数大于这个cache的容量返回true，从而删除LRU条目。
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > CAPACITY;
            }
        };
    }

    // 如果对应的键值对存在，则返回value，否则返回指定的默认值。
    public int get(int key) {
        return map.getOrDefault(key, -1);
    }

    public void set(int key, int value) {
        map.put(key, value);
    }

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2);

        cache.set(1, 1);
        cache.set(2, 2);
        System.out.println(cache.get(1));    // returns 1
        cache.set(3, 3);    // evicts key 2
        System.out.println(cache.get(2));    // returns -1 (not found)
        cache.set(4, 4);    // evicts key 1
        System.out.println(cache.get(1));    // returns -1 (not found)
        System.out.println(cache.get(3));    // returns 3
        System.out.println(cache.get(4));    // returns 4
    }
}
