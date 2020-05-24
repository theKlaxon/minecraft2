package level;

import block.Tile;
import phys.AABB;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Level
{
  private static final int TILE_UPDATE_INTERVAL = 400;
  public final int width;
  public final int height;
  public final int depth;
  private byte[] blocks;
  private int[] lightDepths;
  private ArrayList<LevelListener> levelListeners = new ArrayList();
  private Random random = new Random();
  
  public Level(int w, int h, int d)
  {
    this.width = w;
    this.height = h;
    this.depth = d;
    this.blocks = new byte[w * h * d];
    this.lightDepths = new int[w * h];
    
    boolean mapLoaded = load();
    if (!mapLoaded) {
      generateMap();
    }
    calcLightDepths(0, 0, w, h);
  }
  
  private void generateMap()
  {
    int w = this.width;
    int h = this.height;
    int d = this.depth;
    int[] heightmap1 = new PerlinNoiseFilter(0).read(w, h);
    int[] heightmap2 = new PerlinNoiseFilter(0).read(w, h);
    int[] cf = new PerlinNoiseFilter(1).read(w, h);
    int[] rockMap = new PerlinNoiseFilter(1).read(w, h);
    for (int x = 0; x < w; x++) {
      for (int y = 0; y < d; y++) {
        for (int z = 0; z < h; z++)
        {
          int dh1 = heightmap1[(x + z * this.width)];
          int dh2 = heightmap2[(x + z * this.width)];
          int cfh = cf[(x + z * this.width)];
          if (cfh < 128) {
            dh2 = dh1;
          }
          int dh = dh1;
          if (dh2 > dh) {
            dh = dh2;
          } else {
            dh2 = dh1;
          }
          dh = dh / 8 + d / 3;
          
          int rh = rockMap[(x + z * this.width)] / 8 + d / 3;
          if (rh > dh - 2) {
            rh = dh - 2;
          }
          int i = (y * this.height + z) * this.width + x;
          int id = 0;
          if (y == dh) {
            id = Tile.grass.id;
          }
          if (y < dh) {
            id = Tile.dirt.id;
          }
          if (y <= rh) {
            id = Tile.rock.id;
          }
          this.blocks[i] = ((byte)id);
        }
      }
    }
  }
  
  public boolean load()
  {
    try
    {
      DataInputStream dis = new DataInputStream(new GZIPInputStream(new FileInputStream(new File("level.dat"))));
      dis.readFully(this.blocks);
      calcLightDepths(0, 0, this.width, this.height);
      for (int i = 0; i < this.levelListeners.size(); i++) {
        ((LevelListener)this.levelListeners.get(i)).allChanged();
      }
      dis.close();
      return true;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  public void save()
  {
    try
    {
      DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(new File("level.dat"))));
      dos.write(this.blocks);
      dos.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public void calcLightDepths(int x0, int y0, int x1, int y1)
  {
    for (int x = x0; x < x0 + x1; x++) {
      for (int z = y0; z < y0 + y1; z++)
      {
        int oldDepth = this.lightDepths[(x + z * this.width)];
        int y = this.depth - 1;
        while ((y > 0) && (!isLightBlocker(x, y, z))) {
          y--;
        }
        this.lightDepths[(x + z * this.width)] = y;
        if (oldDepth != y)
        {
          int yl0 = oldDepth < y ? oldDepth : y;
          int yl1 = oldDepth > y ? oldDepth : y;
          for (int i = 0; i < this.levelListeners.size(); i++) {
            ((LevelListener)this.levelListeners.get(i)).lightColumnChanged(x, z, yl0, yl1);
          }
        }
      }
    }
  }
  
  public void addListener(LevelListener levelListener)
  {
    this.levelListeners.add(levelListener);
  }
  
  public void removeListener(LevelListener levelListener)
  {
    this.levelListeners.remove(levelListener);
  }
  
  public boolean isLightBlocker(int x, int y, int z)
  {
    Tile tile = Tile.tiles[getTile(x, y, z)];
    if (tile == null) {
      return false;
    }
    return tile.blocksLight();
  }
  
  public ArrayList<AABB> getCubes(AABB aABB)
  {
    ArrayList<AABB> aABBs = new ArrayList();
    int x0 = (int)aABB.x0;
    int x1 = (int)(aABB.x1 + 1.0F);
    int y0 = (int)aABB.y0;
    int y1 = (int)(aABB.y1 + 1.0F);
    int z0 = (int)aABB.z0;
    int z1 = (int)(aABB.z1 + 1.0F);
    if (x0 < 0) {
      x0 = 0;
    }
    if (y0 < 0) {
      y0 = 0;
    }
    if (z0 < 0) {
      z0 = 0;
    }
    if (x1 > this.width) {
      x1 = this.width;
    }
    if (y1 > this.depth) {
      y1 = this.depth;
    }
    if (z1 > this.height) {
      z1 = this.height;
    }
    for (int x = x0; x < x1; x++) {
      for (int y = y0; y < y1; y++) {
        for (int z = z0; z < z1; z++)
        {
          Tile tile = Tile.tiles[getTile(x, y, z)];
          if (tile != null)
          {
            AABB aabb = tile.getAABB(x, y, z);
            if (aabb != null) {
              aABBs.add(aabb);
            }
          }
        }
      }
    }
    return aABBs;
  }
  
  public boolean setTile(int x, int y, int z, int type)
  {
    if ((x < 0) || (y < 0) || (z < 0) || (x >= this.width) || (y >= this.depth) || (z >= this.height)) {
      return false;
    }
    if (type == this.blocks[((y * this.height + z) * this.width + x)]) {
      return false;
    }
    this.blocks[((y * this.height + z) * this.width + x)] = ((byte)type);
    calcLightDepths(x, z, 1, 1);
    for (int i = 0; i < this.levelListeners.size(); i++) {
      ((LevelListener)this.levelListeners.get(i)).tileChanged(x, y, z);
    }
    return true;
  }
  
  public boolean isLit(int x, int y, int z)
  {
    if ((x < 0) || (y < 0) || (z < 0) || (x >= this.width) || (y >= this.depth) || (z >= this.height)) {
      return true;
    }
    return y >= this.lightDepths[(x + z * this.width)];
  }
  
  public int getTile(int x, int y, int z)
  {
    if ((x < 0) || (y < 0) || (z < 0) || (x >= this.width) || (y >= this.depth) || (z >= this.height)) {
      return 0;
    }
    return this.blocks[((y * this.height + z) * this.width + x)];
  }
  
  public boolean isSolidTile(int x, int y, int z)
  {
    Tile tile = Tile.tiles[getTile(x, y, z)];
    if (tile == null) {
      return false;
    }
    return tile.isSolid();
  }
  
  int unprocessed = 0;
  
  public void tick()
  {
    this.unprocessed += this.width * this.height * this.depth;
    int ticks = this.unprocessed / 400;
    this.unprocessed -= ticks * 400;
    for (int i = 0; i < ticks; i++)
    {
      int x = this.random.nextInt(this.width);
      int y = this.random.nextInt(this.depth);
      int z = this.random.nextInt(this.height);
      Tile tile = Tile.tiles[getTile(x, y, z)];
      if (tile != null) {
        tile.tick(this, x, y, z, this.random);
      }
    }
  }
}
