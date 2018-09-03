package com.example.selcuk.connectfourv20;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class CircularList<E> {

    public static void main(String args[]) {
        String text = "6X7";
        String[] arr = text.split("X");
        System.out.println(Integer.valueOf(arr[1]));
    }

    private Node<E> head = null;
    private Node<E> tail = null;
    private int size = 0;

    public ListIterator<E> listIterator() {
        return new CircularListIterator(0);
    }

    public ListIterator<E> listIterator(int i) {
        return new CircularListIterator(i);
    }

    public Iterator<E> iterator() {
        return new CircularListIterator(0);
    }

    private class Node<E> {
        private Node<E> next = null;
        public Node<E> prev = null;
        public E data;


        public Node(E item) {
            this.data = item;
        }
    }

    public CircularList() {
    }

    private class CircularListIterator implements ListIterator<E> {

        private Node<E> nextItem;

        private Node<E> lastItemReturned;

        private int index = 0;

        public CircularListIterator(int localIndex) {
            if(localIndex > size || localIndex < 0)
                throw new IndexOutOfBoundsException("Invalid index " + localIndex);
            lastItemReturned = null;

            nextItem = head;
            for(index = 0; index < localIndex; index++) {
                System.out.println("init iter: " + nextItem.data);
                nextItem = nextItem.next;
            }

        }

        @Override
        public void add(E e) {
            if(head == null) {
                head = new Node<>(e);
                tail = head;
            } else if(nextItem == head) { // Insert at head
                Node<E> newNode = new Node<>(e);
                newNode.next = head;
                head.prev = newNode;
                head = newNode;
            } else if(nextItem == null) { // Insert at tail
                Node<E> newNode = new Node<>(e);
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
                tail.next = head;
                head.prev = tail;
            } else {
                Node<E> newNode = new Node<>(e);

                newNode.prev = nextItem.prev;
                nextItem.prev.next = newNode;

                newNode.next = nextItem;
                nextItem.prev = newNode;
            }

            size++;
            index++;
            lastItemReturned = null;
        }

        @Override
        public boolean hasNext() {
            if(nextItem != null && nextItem.next != null)
                return true;
            return false;
        }

        @Override
        public E next() {
            if(!hasNext())
                throw new NoSuchElementException();

            if(index == 0)
                nextItem = head;
            else {
                lastItemReturned = nextItem;
                nextItem = nextItem.next;
            }
            index++;
            return nextItem.data;
        }

        @Override
        public boolean hasPrevious() {
            if((nextItem == null && size != 0) ||
                    nextItem.prev != null) // if iterator is in the last element, controls it.
                return true;
            return false;
        }

        @Override
        public E previous() {
            if(!hasPrevious())
                throw new NoSuchElementException();
            if(nextItem == null)
                nextItem = tail;
            else
                nextItem = nextItem.prev;

            lastItemReturned = nextItem;
            index--;

            return lastItemReturned.data;
        }

        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index-1;
        }

        @Override
        public void remove() {

        }

        @Override
        public void set(E e) {

        }

    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }
}
