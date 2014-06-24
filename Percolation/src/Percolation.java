public class Percolation {
  private int[] sites;        // with virtual bottom site
  private int[] sitesNoBottom; // without virtual bottom site
  private int   n;
  private int   TOPSITE;      // virtual top site
  private int   BOTTOMSITE;   // virtual bottom site
  private int   BLOCK;        // large enough to indicate this site is blocked

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

  // Returns component id for the given site
  private int find(int id, boolean hasBottom) {
    int[] site = hasBottom ? sites : sitesNoBottom;
    while (site[id] != id) {
      if (site[id] == BLOCK)
        return BLOCK;
      id = site[id];
    }
    return id;
  }

  private int find(int id) {
    return find(id, true);
  }

  // Return component id for neighboring open sites
  private int[] getNeighborRoot(int id, boolean hasBottom) {
    int top = (id <= n) ? find(TOPSITE, hasBottom)
                       : find(id - n, hasBottom);
    int bottom = 0;
    if (id > n * n - n) {
      bottom = hasBottom ? find(BOTTOMSITE, hasBottom) : BLOCK;
    } else {
      bottom = find(id + n, hasBottom);
    }
    int left = (id % n == 1) ? BLOCK : find(id - 1, hasBottom);
    int right = (id % n == 0) ? BLOCK : find(id + 1, hasBottom);

    return new int[] { top, bottom, left, right };
  }

  // Return minimal component id among all open neighbor sites
  private int getMinRoot(int[] neighbors) {
    int ret = BLOCK;
    for (int neighbor : neighbors) {
      if (neighbor == BLOCK)
        continue;
      if (neighbor < ret)
        ret = neighbor;
    }
    return ret;
  }
  
  private void updateSite(int id, boolean hasBottom) {
    int[] site = hasBottom ? sites : sitesNoBottom;
    int[] neighbors = getNeighborRoot(id, hasBottom);
    int minRoot = getMinRoot(neighbors);
    minRoot = (minRoot < id) ? minRoot : id;
    site[id] = minRoot;
    for (int neighbor : neighbors) {
      if (neighbor != BLOCK)
        site[neighbor] = minRoot;
    }
  }
  
  private void updateSite(int id) {
    updateSite(id, true);
  }

  // Create N-by-N grid with all sites blocked
  public Percolation(int N) {
    sites = new int[N * N + 2]; // two virtual sites
    sitesNoBottom = new int[N * N + 1]; // only virtual top site
    n = N;
    TOPSITE = 0;
    BOTTOMSITE = n * n + 1;
    BLOCK = n * n + 2;

    // Initialize all sites to be blocked
    sites[TOPSITE] = sitesNoBottom[TOPSITE] = TOPSITE;
    sites[BOTTOMSITE] = BOTTOMSITE;
    for (int i = 1; i <= n * n; ++i) {
      sites[i] = sitesNoBottom[i] = BLOCK;
    }
  }

  // If site is open, do nothing
  // Otherwise, do union on all neighboring open sites
  public void open(int i, int j) {
    if (isOpen(i, j))
      return;

    int id = xyTo1D(i, j);
    sites[id] = id; // mark current site as open

    // merge all open neighboring
    updateSite(id);
    updateSite(id, false);
  }

  public boolean isOpen(int i, int j) {
    int id = xyTo1D(i, j);
    return sites[id] != BLOCK;
  }

  public boolean isFull(int i, int j) {
    int id = xyTo1D(i, j);
    return isOpen(i, j) && find(id, false) == TOPSITE;
  }

  public boolean percolates() {
    return find(BOTTOMSITE) == TOPSITE;
  }
}
