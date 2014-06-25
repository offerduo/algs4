public class Percolation {
  private WeightedQuickUnionUF ufPerc, ufFull;
  private int                  n;
  private int                  virtualTopSite;   // virtual top site: 0
  private int                  virtualBottomSite; // virtual bottom site: n*n+1
  private boolean[]            isOpenSite;       // status array of sites

  // Create N-by-N grid with all sites blocked
  public Percolation(int N) {
    if (N <= 0) throw new IllegalArgumentException();
    ufPerc = new WeightedQuickUnionUF(N * N + 2);
    ufFull = new WeightedQuickUnionUF(N * N + 1);
    n = N;
    virtualTopSite = 0;
    virtualBottomSite = n * n + 1;
    isOpenSite = new boolean[N * N + 2]; // default to closed/false
    // open up virtual top and bottom sites
    isOpenSite[virtualTopSite] = isOpenSite[virtualBottomSite] = true;
  }

  // mapping function for 2D (i,j) pair to 1D index
  private int xyTo1D(int i, int j) {
    validateXY(i, j);
    return (i - 1) * n + j;
  }

  // Validates 2D representation (i,j) so that 0 < i,j <= n
  private void validateXY(int i, int j) {
    if (i <= 0 || i > n)
      throw new IndexOutOfBoundsException("row index i out of bounds");
    if (j <= 0 || j > n)
      throw new IndexOutOfBoundsException("column index j out of bounds");
  }

  private void unionOpenNeighbor(int id, int neighbor, boolean isVirtualBottom) {
    if (neighbor != Integer.MAX_VALUE && isOpenSite[neighbor]) {
      ufPerc.union(neighbor, id);
      if (!isVirtualBottom) {
        ufFull.union(neighbor, id);
      }
    }
  }

  // If site is open, do nothing
  // Otherwise, do union on all neighboring open sites
  public void open(int i, int j) {
    int id = xyTo1D(i, j);
    if (isOpenSite[id]) return;
    else isOpenSite[id] = true;

    // merge all open neighboring
    int top = (id <= n) ? virtualTopSite : id - n;
    int bottom = (id > n * n - n) ? virtualBottomSite : id + n;
    int left = (id % n == 1) ? Integer.MAX_VALUE : id - 1;
    int right = (id % n == 0) ? Integer.MAX_VALUE : id + 1;

    unionOpenNeighbor(id, top, false);
    unionOpenNeighbor(id, bottom, bottom == virtualBottomSite);
    unionOpenNeighbor(id, left, false);
    unionOpenNeighbor(id, right, false);
  }

  public boolean isOpen(int i, int j) {
    return isOpenSite[xyTo1D(i, j)];
  }

  public boolean isFull(int i, int j) {
    int id = xyTo1D(i, j);
    return isOpenSite[id] && ufFull.connected(virtualTopSite, id);
  }

  public boolean percolates() {
    return ufPerc.connected(virtualTopSite, virtualBottomSite);
  }
}
