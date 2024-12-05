package org.example;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter

public class InMemoryHistoryManagerImpl implements HistoryManager {

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        if (table.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node removedNode = table.remove(id);

        if (removedNode != null) {
            this.removeNode(removedNode);
        }
    }

    private final Map<Integer, Node> table = new HashMap<>();
    private Node head;
    private Node tail;

    private void linkLast(Task task) {
        Node node = new Node();
        node.setTask(task);


        if (table.containsKey(task.getId())) {
            removeNode(table.get(task.getId()));
        }

        if (head == null) {
            head = node;
            tail = node;

            node.setPrev(null);
            node.setNext(null);
        } else {
            node.setPrev(tail);
            node.setNext(null);
            tail.setNext(node);
            tail = node;
        }

        table.put(task.getId(), node);

    }

    private Node getNode(int id) {
        return table.get(id);
    }

    private List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        Node element = head;

        while (element != null) {
            list.add(element.getTask());
            element = element.getNext();
        }

        return list;
    }

    private void removeNode(Node node) {
        if (node != null) {
            table.remove(node.getTask().getId());

            Node nodeNext = node.getNext();
            Node nodePrev = node.getPrev();

            if (head == node) {
                head = node.getNext();
            }

            if (tail == node) {
                tail = node.getPrev();
            }

            if (nodeNext != null) {
                nodeNext.setPrev(nodePrev);
            }
            if (nodePrev != null) {
                nodePrev.setNext(nodeNext);
            }
        }

    }


}

@Getter
@Setter
class Node {
    private Task task;
    private Node next;
    private Node prev;
}

