package dbmanager.frontend;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

import dbmanager.main.Main;

public class TableRenderer implements Runnable
{
	// LADIES AND GENTLEMEN I PRESENT TO YOU THE MOST INEFFICENT RENDERER IN HISTORY!
	
	private Thread thread;
	private boolean isRunning = false;
	private HashMap<String, List<String>> table = new HashMap<String, List<String>>();
	private BufferedImage tableImg = null;
	private List<TableImg> tableImgs = new ArrayList<TableImg>();
	
	public static final Font FNT = new Font("Sans-Serif", Font.PLAIN, 16);
	
	public TableRenderer() 
	{
		start();
	}
	
	private void start()
	{
		thread = new Thread(this, "Renderer-Thread");
		isRunning = true;
		if (System.getProperty("os.name").contains("Mac")) thread.run();
		else thread.start();
	}
	
	private void graphicsRender()
	{
		BufferStrategy bs = Main.window.canvas.getBufferStrategy();
		if (bs == null) { Main.window.canvas.createBufferStrategy(2); return; }
		Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// -- RENDER -- //
		
		g2d.setFont(FNT);
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, 500, 400);
		
		int fontHeight = g2d.getFontMetrics().getHeight();
		int x = 0;
		int textPadding = 5;
		
		g2d.translate(-Main.camera.getX(), -Main.camera.getY());
		try {
			for (TableImg img : tableImgs) img.render(g2d);
		} catch (ConcurrentModificationException e) {  }
		g2d.translate(0, Main.camera.getY());
		
		for (String column : table.keySet())
		{
			int maxWidth = getMaxColumnWidth(column);
			
			g2d.setColor(Color.red);
			g2d.fillRect(x, 0, maxWidth + textPadding * 2, fontHeight + textPadding * 2);
			g2d.setColor(Color.black);
			g2d.drawString(column.split("/")[0], x + textPadding, fontHeight + textPadding);
			
			x += maxWidth + textPadding * 2;
			
			Main.camera.setMaxY((table.get(column).size() * (fontHeight + textPadding * 2)) - Main.VIEWPORT_HEIGHT);
		}
		
		Main.camera.setMaxX(x - Main.VIEWPORT_WIDTH);
		
		// ------------ //
		
		g2d.dispose();
		bs.show();
	}
	
	@Override
	public void run() 
	{	
		long initialTime = System.nanoTime();
		final double timeF = 1000000000 / 30.0f;
		double deltaF = 0;
		long timer = System.currentTimeMillis();

		while (isRunning) 
		{	
			long currentTime = System.nanoTime();
			deltaF += (currentTime - initialTime) / timeF;
			initialTime = currentTime;

			if (deltaF >= 1)
			{
				if (Main.camera != null) graphicsRender();
				deltaF--;
			}

			if (System.currentTimeMillis() - timer > 1000) 
			{
				timer += 1000;
			}
		}
		
		stop();
	}
	
	public void stop()
	{
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void end()
	{
		isRunning = false;
	}
	
	public int getMaxColumnWidth(String column)
	{
		int maxWidth = Main.window.canvas.getFontMetrics(FNT).stringWidth(column.split("/")[0]);
			
		for (String value : table.get(column))
		{
			int width = Main.window.canvas.getFontMetrics(FNT).stringWidth(value);
			if (width > maxWidth) maxWidth = width;
		}
		
		return maxWidth;
	}
	
	private int getMaximumHeight(int fontHeight, int textPadding)
	{
		int height = table.get(table.keySet().iterator().next()).size() * (fontHeight + textPadding * 2);
		return height;
	}
	
	private int getMaximumWidth(int textPadding)
	{
		int width = 0;
		
		for (String column : table.keySet())
		{
			width += getMaxColumnWidth(column) + textPadding * 2;
		}
		
		return width;
	}
	
	private void createTableImg()
	{
		int fontHeight = Main.window.canvas.getFontMetrics(FNT).getHeight();
		int textPadding = 5;
		int x = 0;
		int height = (textPadding * 2 + fontHeight);
		
		tableImg = new BufferedImage(getMaximumWidth(textPadding), getMaximumHeight(fontHeight, textPadding), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = tableImg.createGraphics();
		g2d.setFont(FNT);
		g2d.setColor(Color.white);
		for (String column : table.keySet())
		{
			int maxWidth = getMaxColumnWidth(column);
			
			for (int i = 0; i < table.get(column).size(); i ++)
			{
				String value = table.get(column).get(i);
				g2d.drawRect(x, i * height, maxWidth + textPadding * 2, height);
				g2d.drawString(value, x + textPadding, (i * height) + (textPadding + fontHeight));
			}
			
			x += maxWidth + textPadding * 2;
		}
		
		g2d.dispose();
	}
	
	public void splitImages()
	{
		tableImgs.clear();
		
		float xTimes = ((float) tableImg.getWidth()) / ((float) Main.VIEWPORT_WIDTH);
		xTimes = ((xTimes - ((int) xTimes)) > 0) ? (((int) xTimes) + 1) : xTimes;
		
		float yTimes = ((float) tableImg.getHeight()) / ((float) Main.VIEWPORT_HEIGHT);
		yTimes = ((yTimes - ((int) yTimes)) > 0) ? (((int) yTimes) + 1) : yTimes;
		
		for (int i = 0; i < xTimes; i ++)
		{
			for (int j = 0; j < yTimes; j ++)
			{
				BufferedImage img = new BufferedImage(Main.VIEWPORT_WIDTH, Main.VIEWPORT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = img.createGraphics();
				g2d.translate(-(i * Main.VIEWPORT_WIDTH), -(j * Main.VIEWPORT_HEIGHT));
				g2d.drawImage(tableImg, 0, 0, null);
				g2d.dispose();
				tableImgs.add(new TableImg(i * Main.VIEWPORT_WIDTH, j * Main.VIEWPORT_HEIGHT, img));
			}
		}
	}
	
	public void setTable(HashMap<String, List<String>> table)
	{
		Main.camera.setX(0);
		Main.camera.setY(0);
		this.table = table;
		createTableImg();
		splitImages();
	}
}
