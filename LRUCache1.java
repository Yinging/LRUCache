package demo1;

import java.util.Hashtable;
import java.util.Map;

public class LRUCache {

    private Map<Integer, Node> map;
    private int count;
    private final int capacity;
    private Node head, tail;

    // Node节点存储数据
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
        this.count = 0;
        this.capacity = capacity;
        this.map = new Hashtable<>();

        head = tail = null;
    }

    // 把节点插入到头节点的位置上，使用一个head指针指向头节点
    private void addToHead(Node node) {
        if (node == null) return;
        else {
            if (head == null && tail == null) {
                head = node;
                tail = node;
            } else {
                node.prev = null;
                node.next = head;
                head.prev = node;
                head = node;
            }
        }
    }

    // 从链表中删除一个节点
    private void deleteNode(Node node) {
        if (node == null) return;
        else {
            Node prevNode = node.prev;
            Node nextNode = node.next;

            if (prevNode == null && nextNode != null) {
                head = nextNode;
                node.next = null;
                nextNode.prev = null;
            } else if (prevNode != null && nextNode == null) {
                tail = prevNode;
                node.prev = null;
                prevNode.next = null;
            } else if (prevNode == null && nextNode == null){
                node.next = null;
                node.prev = null;
                head = null;
                tail = null;
            } else {
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
            }
        }
    }

    // 从链表中把这个node节点移动到头节点的位置
    private void moveToHead(Node node) {
        this.deleteNode(node);
        this.addToHead(node);
    }

    // 删除最后一个节点并返回
    private Node deleteTail() {
        if (tail == null) return null;
        Node node = tail;
        this.deleteNode(node);
        return node;
    }

    public int get(int key) {
        Node node = map.get(key);
        if (node == null) {
            return -1;
        }
        // 将访问过的节点移动到头节点的位置
        this.moveToHead(node);
        return node.value;
    }

    public void set(int key, int value) {
        Node node = map.get(key);
        if (node == null) {
            Node newNode = new Node(key, value);

            this.map.put(key, newNode);
            this.addToHead(newNode);

            count++;

            if (count > capacity) {
                Node tail = this.deleteTail();
                if (tail == null) return;
                this.map.remove(tail.key);
                count--;
            }
        } else {
            node.value = value;
            this.moveToHead(node);
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
