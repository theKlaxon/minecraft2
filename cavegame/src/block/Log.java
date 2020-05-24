package block;

public class Log
extends Tile
{
protected Log(int id)
{
  super(id);
  this.tex = 21;
}

protected int getTexture(int face)
{
    if (face == 1) {
        return 21;
      }
      if (face == 0) {
        return 21;
      }
      return 20;
}

}

