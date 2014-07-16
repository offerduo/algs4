import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
  private class Node {
    private Item item;
    private Node next, prev;
  }

  private int N;
  private Node pre, post; // sentinels before/after first/last nodes

  public Deque() {
    pre = new Node();
    post = new Node();
    pre.next = post;
    post.prev = pre;
    N = 0;
  }

  public boolean isEmpty() {
    return N == 0;
  }

  public int size() {
    return N;
  }

  public void addFirst(Item item) {
    if (item == null) throw new NullPointerException();
    Node newNode = new Node();
    newNode.item = item;
    newNode.prev = pre;
    newNode.next = pre.next;
    newNode.prev.next = newNode;
    newNode.next.prev = newNode;
    ++N;
  }

  public void addLast(Item item) {
    if (item == null) throw new NullPointerException();
    Node newNode = new Node();
    newNode.item = item;
    newNode.next = post;
    newNode.prev = post.prev;
    newNode.next.prev = newNode;
    newNode.prev.next = newNode;
    ++N;
  }

  public Item removeFirst() {
    if (isEmpty()) throw new java.util.NoSuchElementException();
    Node ret = pre.next;
    ret.prev.next = ret.next;
    ret.next.prev = ret.prev;
    ret.prev = ret.next = null;
    --N;

    return ret.item;
  }

  public Item removeLast() {
    if (isEmpty()) throw new java.util.NoSuchElementException();
    Node ret = post.prev;
    ret.next.prev = ret.prev;
    ret.prev.next = ret.next;
    ret.next = ret.prev = null;
    --N;

    return ret.item;
  }

  private class FrontIterator implements Iterator<Item> {
    private Node current;

    public FrontIterator() {
      current = pre.next;
    }

    public boolean hasNext() {
      return current != post;
    }

    public Item next() {
      if (!hasNext()) throw new java.util.NoSuchElementException();
      current = current.next;
      return current.prev.item;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  public Iterator<Item> iterator() {
    return new FrontIterator();
  }

  public static void main(String[] args) {
    Deque<Integer> myDeque = new Deque<Integer>();
    myDeque.addLast(new Integer(1));
    myDeque.removeFirst();
    myDeque.addLast(new Integer(3));
    myDeque.removeLast();
    myDeque.addLast(new Integer(4));

    for (Integer integer : myDeque) {
      System.out.println(integer);
    }
    System.out.println();

    System.out.print(myDeque.removeLast());

  }
}
