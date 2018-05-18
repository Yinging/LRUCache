package demo;

import java.util.Hashtable;
import java.util.Map;

public class LRUCache {

    private Map<Integer, Node> map;
    private final int capacity;
    private int count;
    private Node head;
    private Node tail;

    class Node {
        int key;
        int value;
        Node prev;
        Node next;

        public Node() {}

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
            prev = null;
            next = null;
        }
    }

    public LRUCache(int capacity) {
        map = new Hashtable<>();
        this.capacity = capacity;
        this.count = 0;

        head = new Node();
        head.prev = null;

        tail = new Node();
        tail.next = null;

        head.next = tail;
        tail.prev = head;
    }

    // 将节点添加到第一个非空节点的位置，即头节点（空节点）的下一个节点
    private void addToFirst(Node node) {
        if (node == null) return;
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    // 删除双向链表中的某个节点
    private void deleteNode(Node node) {
        if (node == null) return;
        Node prevNode = node.prev;
        Node nextNode = node.next;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
        node.next = null;
        node.prev = null;

    }

    // 删除节点并把它添加到第一个节点的位置。
    private void deleteAndAddToFirst(Node node) {
        this.deleteNode(node);
        this.addToFirst(node);
    }

    // 删除并返回最后一个节点。
    private Node popTail() {
        Node node = tail.prev;
        if (node == null) return null;
        this.deleteNode(node);
        return node;
    }


    // 得到指定key对应的value，并把访问过的节点放到第一个节点的位置。
    public int get(int key) {
        Node node = map.get(key);
        if (node == null) return -1;

        // 将访问过的节点放到第一个节点的位置。
        this.deleteAndAddToFirst(node);
        return node.value;
    }

    public void set(int key, int value) {
        Node node = map.get(key);
        if (node == null) {
            Node newNode = new Node(key, value);
            map.put(key, newNode);
            this.addToFirst(newNode);

            count++;

            if (count > capacity) {
                // 删除最后一个节点。
                Node delete = this.popTail();
                map.remove(delete.key);
                count--;
            }
        } else {
            // 更新value
            node.value = value;
            this.deleteAndAddToFirst(node);
        }
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
