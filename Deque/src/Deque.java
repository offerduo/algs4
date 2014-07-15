import java.util.Iterator;

public class Deque<T> implements Iterable<T> {
  private class DoublyLinkedNode {
    T                item;
    DoublyLinkedNode prev;
    DoublyLinkedNode next;
  }

  private DoublyLinkedNode first, last;

  private class ListIterator implements Iterator<T> {

    @Override
    public boolean hasNext() {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public T next() {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public void remove() {
      // TODO Auto-generated method stub

    }

  }

  @Override
  public Iterator<T> iterator() {
    // TODO Auto-generated method stub
    return new ListIterator();
  }

}
