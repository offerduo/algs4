public class PercolationStats {
  private Percolation perc;
  private int[]       threshold;
  private final int   N;
  private final int   T;

  public PercolationStats(int N, int T) {
    if (N <= 0 || T <= 0) throw new IllegalArgumentException();

    threshold = new int[T];
    this.N = N;
    this.T = T;

    for (int i = 0; i < T; ++i) {
      perc = new Percolation(N);
      do {
        ++threshold[i];
        int row = 0, col = 0;
        do {
          row = StdRandom.uniform(1, N + 1); // [1, N+1)
          col = StdRandom.uniform(1, N + 1); // [1, N+1)
        } while (perc.isOpen(row, col));
        perc.open(row, col);
      } while (!perc.percolates());
    }
  }

  public double mean() {
    double sum = 0.0;
    for (int i = 0; i < T; ++i) {
      sum += threshold[i];
    }
    return sum / (N * N * T);
  }

  public double stddev() {
    if (T == 1) return Double.NaN;
    double meanVal = mean();
    double sqrtDiffs = 0.0;
    for (int i = 0; i < T; ++i) {
      double diffs = meanVal - (double) threshold[i] / (N * N);
      sqrtDiffs += diffs * diffs;
    }
    return Math.sqrt(sqrtDiffs / (T - 1));
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
