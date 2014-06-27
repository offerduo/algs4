public class PercolationStats {
  private Percolation perc;
  private double[]    threshold;
  private final int   N;
  private final int   T;

  public PercolationStats(int N, int T) {
    if (N <= 0 || T <= 0) throw new IllegalArgumentException();

    threshold = new double[T];
    this.N = N;
    this.T = T;

    for (int i = 0; i < T; ++i) {
      perc = new Percolation(N);
      do {
        threshold[i] += 1.0;
        int row = 0, col = 0;
        do {
          row = StdRandom.uniform(1, N + 1); // [1, N+1)
          col = StdRandom.uniform(1, N + 1); // [1, N+1)
        } while (perc.isOpen(row, col));
        perc.open(row, col);
      } while (!perc.percolates());
      threshold[i] /= (N * N);
    }
  }

  public double mean() {
    return StdStats.mean(threshold);
  }

  public double stddev() {
    return StdStats.stddev(threshold);
  }

  public double confidenceLo() {
    return mean() - 1.96 * stddev() / (Math.sqrt(T));
  }

  public double confidenceHi() {
    return mean() + 1.96 * stddev() / (Math.sqrt(T));
  }

  public static void main(String[] args) {
    int N = Integer.parseInt(args[0]);
    int T = Integer.parseInt(args[1]);

    PercolationStats percStats = new PercolationStats(N, T);
    System.out.println("mean                    = " + percStats.mean());
    System.out.println("stddev                  = " + percStats.stddev());
    System.out.println("95% confidence interval = " + percStats.confidenceLo()
        + ", " + percStats.confidenceHi());
  }
}
