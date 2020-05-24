package block;

import level.Level;
import level.Tesselator;
import phys.AABB;
import java.util.Random;

public class Bush
  extends Tile 
{
  protected Bush(int id)
  {
    super(id);
    this.tex = 15;
  }
  
  public void tick(Level level, int x, int y, int z, Random random)
  {
    int below = level.getTile(x, y - 1, z);
    if ((!level.isLit(x, y, z)) || ((below != Tile.dirt.id) && (below != Tile.grass.id))) {
      level.setTile(x, y, z, 0);
    }
  }
  
  public void render(Tesselator t, Level level, int layer, int x, int y, int z)
  {
    if ((level.isLit(x, y, z) ^ layer != 1)) {
      return;
    }
    int tex = getTexture(15);
    float u0 = tex % 16 / 16.0F;
    float u1 = u0 + 0.0624375F;
    float v0 = tex / 16 / 16.0F;
    float v1 = v0 + 0.0624375F;
    
    int rots = 2;
    t.color(1.0F, 1.0F, 1.0F);
    for (int r = 0; r < rots; r++)
    {
      float xa = (float)(Math.sin(r * 3.141592653589793D / rots + 0.7853981633974483D) * 0.5D);
      float za = (float)(Math.cos(r * 3.141592653589793D / rots + 0.7853981633974483D) * 0.5D);
      float x0 = x + 0.5F - xa;
      float x1 = x + 0.5F + xa;
      float y0 = y + 0.0F;
      float y1 = y + 1.0F;
      float z0 = z + 0.5F - za;
      float z1 = z + 0.5F + za;
      
      t.vertexUV(x0, y1, z0, u1, v0);
      t.vertexUV(x1, y1, z1, u0, v0);
      t.vertexUV(x1, y0, z1, u0, v1);
      t.vertexUV(x0, y0, z0, u1, v1);
      
      t.vertexUV(x1, y1, z1, u0, v0);
      t.vertexUV(x0, y1, z0, u1, v0);
      t.vertexUV(x0, y0, z0, u1, v1);
      t.vertexUV(x1, y0, z1, u0, v1);
    }
  }
  
  public AABB getAABB(int x, int y, int z)
  {
    return null;
  }
  
  public boolean blocksLight()
  {
    return false;
  }
  
  public boolean isSolid()
  {
    return false;
  }
}
