package block;

public class Glass
  extends Tile
{
  protected Glass(int id)
  {
    super(id);
    this.tex = 18;
  }
  
  protected int getTexture(int face)
  {
	  return 18;
  }
  
  public boolean blocksLight()
  {
    return false;
  }
  
}

