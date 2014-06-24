public class Percolation {
  private WeightedQuickUnionUF uf, ufNoBottom;
  private int                  n;
  private int                  TOPSITE;       // virtual top site: 0
  private int                  BOTTOMSITE;    // virtual bottom site: n*n+1
  private boolean[]            isOpenSite;

  // mapping function for 2D (i,j) pair to 1D index
  private int xyTo1D(int i, int j) {
    validateXY(i, j);
    return (i - 1) * n + j; // 0 is for special use: virtual top site
  }

  // Validates 2D representation (i,j) so that 0 < i,j <= n
  private void validateXY(int i, int j) {
    if (i <= 0 || i > n)
      throw new IndexOutOfBoundsException("row index i out of bounds");
    if (j <= 0 || j > n)
      throw new IndexOutOfBoundsException("column index i out of bounds");
  }

  private void unionOpenNeighbor(int id, int neighbor, boolean isBottom) {
    if (neighbor != Integer.MAX_VALUE && isOpenSite[neighbor]) {
      uf.union(neighbor, id);
      if (!isBottom)
        ufNoBottom.union(neighbor, id);
    }
  }

  // Create N-by-N grid with all sites blocked
  public Percolation(int N) {
    uf = new WeightedQuickUnionUF(N * N + 2);
    ufNoBottom = new WeightedQuickUnionUF(N * N + 1);
    isOpenSite = new boolean[N * N + 2]; // default to closed/false
    isOpenSite[0] = isOpenSite[N * N + 1] = true;
    n = N;
    TOPSITE = 0;
    BOTTOMSITE = n * n + 1;
  }

  // If site is open, do nothing
  // Otherwise, do union on all neighboring open sites
  public void open(int i, int j) {
    int id = xyTo1D(i, j);
    if (isOpenSite[id])
      return;
    else
      isOpenSite[id] = true;

    // merge all open neighboring
    int top = (id <= n) ? TOPSITE : id - n;
    int bottom = (id > n * n - n) ? BOTTOMSITE : id + n;
    int left = (id % n == 1) ? Integer.MAX_VALUE : id - 1;
    int right = (id % n == 0) ? Integer.MAX_VALUE : id + 1;

    unionOpenNeighbor(id, top, false);
    unionOpenNeighbor(id, bottom, bottom == BOTTOMSITE);
    unionOpenNeighbor(id, left, false);
    unionOpenNeighbor(id, right, false);
  }

  public boolean isOpen(int i, int j) {
    return isOpenSite[xyTo1D(i, j)];
  }

  public boolean isFull(int i, int j) {
    int id = xyTo1D(i, j);
    return isOpenSite[id] && ufNoBottom.connected(TOPSITE, id);
  }

  public boolean percolates() {
    return uf.connected(TOPSITE, BOTTOMSITE);
  }
}
