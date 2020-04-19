package dbmanager.frontend;

import dbmanager.main.Main;

public class Camera
{
	private static final int TEXT_PADDING = 5;
	private static final int MOV_SPEED = Main.window.canvas.getFontMetrics(TableRenderer.FNT).getHeight() + TEXT_PADDING * 2;
	
	private int x = 0, y = 0;
	private int maxX = 0, maxY = 0;
	
	public void moveY(int dir)
	{
		y += MOV_SPEED * dir;
		y = clamp(y, 0, maxY);
	}
	
	public void moveX(int dir)
	{
		x += MOV_SPEED * dir;
		x = clamp(x, 0, maxX);
	}
	
	public void setMaxX(int val)
	{
		maxX = clamp(val, 0, Integer.MAX_VALUE);
	}
	
	public void setMaxY(int val)
	{
		maxY = clamp(val, 0, Integer.MAX_VALUE);
	}
	
	private int clamp(int val, int min, int max)
	{
		if (val <= min) return min;
		else if (val >= max) return max;
		else return val;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
}
