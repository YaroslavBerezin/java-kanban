package taskTracker.manager.history;

import taskTracker.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> map = new HashMap<>();

    private Node head;
    private Node tail;
    private int size = 0;

    @Override
    public void add(Task task) {
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }

    // Правильно ли я понимаю, что мапить в данном контексте - создавать и инициализировать данные,
    // связанные с новым таском (строки 38-40)? Я раз 10 перечитал замечание, только потом понял, в чем проблема)
    private void linkLast(Task task) {
        int id = task.getId();

        if (map.containsKey(id)) {
            remove(id);
        }

        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;

        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }

        map.put(task.getId(), newNode);
        size++;
    }

    // дико извиняюсь за прошлую работу, от такого ТЗ голова кругом...
    private void removeNode(int id) {
        if (map.containsKey(id)) {
            Node currentNode = map.get(id);
            Node prev = currentNode.prev;
            Node next = currentNode.next;

            if (prev == null) {
                head = currentNode.next;
            } else {
                prev.next = next;
            }

            if (next == null) {
                tail = currentNode.prev;
            } else {
                next.prev = prev;
            }

            map.remove(id);
            size--;
        }
    }

    private List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        Node headNode = head;

        while (headNode != null) {
            list.add(headNode.data);
            headNode = headNode.next;
        }

        return list;
    }

    public int size() {
        return this.size;
    }

    private class Node {
        private Task data;
        private Node next;
        private Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}
