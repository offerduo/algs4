import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
  private Item[] q;
  private int    N     = 0;
  private int    first = 0;
  private int    last  = 0;

  public RandomizedQueue() {
    q = (Item[]) new Object[2];
  }

  private void resize(int max) {
    assert max >= N;
    Item[] temp = (Item[]) new Object[max];
    for (int i = 0; i < N; i++) {
      temp[i] = q[(first + i) % q.length];
    }
    q = temp;
    first = 0;
    last = N;
  }

  public boolean isEmpty() {
    return N == 0;
  }

  public int size() {
    return N;
  }

  public void enqueue(Item item) {
    if (N == q.length) resize(2 * q.length);
    q[last++] = item;
    if (last == q.length) last = 0;
    N++;
  }

  public Item dequeue() {
    if (isEmpty()) throw new java.util.NoSuchElementException();
    Item item = q[first];
    q[first] = null;
    N--;
    first++;
    if (first == q.length) first = 0;
    if (N > 0 && N == q.length / 4) resize(q.length / 2);
    return item;
  }

  public Item sample() {
    return q[StdRandom.uniform(N)];
  }

  private class RandomIterator implements Iterator<Item> {
    private int[] randomSequence;
    private int   current;

    private void shuffle(int[] a) {
      int N = a.length;
      for (int i = 0; i < N; i++) {
        int r = StdRandom.uniform(i, N); // [i, N-1]
        int swap = a[r];
        a[r] = a[i];
        a[i] = swap;
      }
    }

    public RandomIterator() {
      randomSequence = new int[N];
      for (int i = 0; i < N; ++i) {
        randomSequence[i] = i;
      }
      shuffle(randomSequence);
      current = 0;
    }

    @Override
    public boolean hasNext() {
      return current != N;
    }

    public Item next() {
      if (!hasNext()) throw new java.util.NoSuchElementException();
      return q[randomSequence[current++]];
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  public Iterator<Item> iterator() {
    return new RandomIterator();
  }

  public void dump() {
    for (int i = 0; i < N; ++i)
      System.out.println(q[i]);
  }

  public static void main(String[] args) {
    RandomizedQueue<Integer> myQueue = new RandomizedQueue<Integer>();
    for (int i = 0; i < 10; ++i) {
      myQueue.enqueue(new Integer(i));
    }
    myQueue.dump();

    System.out.println();

    for (Integer integer : myQueue) {
      System.out.println(integer);
    }
  }
}