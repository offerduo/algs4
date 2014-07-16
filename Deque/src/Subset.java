import java.util.Iterator;

public class Subset {
  public static void main(String[] args) {
    RandomizedQueue<String> myQueue = new RandomizedQueue<String>();
    int k = Integer.parseInt(args[0]);
    while (!StdIn.isEmpty()) {
      myQueue.enqueue(StdIn.readString());
    }
    Iterator<String> iter = myQueue.iterator();
    for (int i = 0; i < k && iter.hasNext(); ++i) {
      System.out.println(iter.next());
    }
  }
}