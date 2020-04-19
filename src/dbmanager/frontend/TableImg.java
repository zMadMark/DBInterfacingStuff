package dbmanager.frontend;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import dbmanager.main.Main;

public class TableImg 
{
	private int x, y;
	private BufferedImage img;
	
	public TableImg(int x, int y, BufferedImage img) 
	{
		this.x = x;
		this.y = y;
		this.img = img;
	}
	
	public void render(Graphics2D g2d)
	{
		if 
		(
				(
					x + img.getWidth() >= Main.camera.getX() && 
					x <= Main.camera.getX() + Main.VIEWPORT_WIDTH
				)
				&&
				(
					y + img.getHeight() >= Main.camera.getY() &&
					y <= Main.camera.getY() + Main.VIEWPORT_HEIGHT
				)
		) 
		{
			g2d.drawImage(img, x, y, null);
		}
	}

	public int getX() 
	{
		return x;
	}
	
	public int getY() 
	{
		return y;
	}
	
	public BufferedImage getImg() 
	{
		return img;
	}
}
