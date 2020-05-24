package block;

import java.util.ArrayList;

import block.Tile;
import level.Level;
import level.Tesselator;

public class Tree {

	  private int paintTexture = 1;
	
	private ArrayList<Log> logs = new ArrayList();
	Log e;
	
	public Tree() {
	
	
	}

	public void setPos(float x, float y, float z)
	{
	  this.x = x;
	  this.y = y;
	  this.z = z;
	}
	
}
