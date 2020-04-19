package dbmanager.main;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import dbmanager.framework.DBManagerSQL;
import dbmanager.frontend.Camera;
import dbmanager.frontend.TableRenderer;
import dbmanager.frontend.Window;
import framework.ModuleManager;

public class Main 
{
	public static TableRenderer renderer;
	public static Window window;
	public static DBManagerSQL sql;
	public static Camera camera = null;
	public static ModuleManager mManager;
	
	public static final int VIEWPORT_WIDTH = 500, VIEWPORT_HEIGHT = 400;
	
	public static void main(String[] args) throws URISyntaxException, IOException 
	{
		System.setProperty("sun.java2d.opengl", "true");
		
		mManager = new ModuleManager("modules");
		window = new Window();
		renderer = new TableRenderer();
		camera = new Camera();
		sql = new DBManagerSQL();
	}
	
	public static void exit()
	{
		if (!sql.terminate()) 
		{
			JOptionPane.showMessageDialog(window, "Impossibile terminare la connessione. Perfavore riprovi.", "Errore", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		renderer.end();
		window.finalDispose();
		System.exit(0);
	}
}
