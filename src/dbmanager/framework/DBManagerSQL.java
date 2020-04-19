package dbmanager.framework;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import dbmanager.main.Main;
import framework.SQLInterface;

public class DBManagerSQL extends SQLInterface
{
	public void connect()
	{
		try {
			
			if(super.connect
			(
					Main.window.serverIp.getText(), 
					Main.window.serverPort.getText(), 
					Main.window.databaseName.getText(), 
					Main.window.username.getText(), 
					String.valueOf(Main.window.password.getPassword())
			)) JOptionPane.showMessageDialog(Main.window, "Connessione con il server MySql riuscita", "Info", JOptionPane.INFORMATION_MESSAGE);;
		
		} catch (Exception e) {
		
			e.printStackTrace();
			Main.window.log.append("\n[ERR] " + e.getMessage());
			JOptionPane.showMessageDialog(Main.window, "Tentativo di connessione al server fallito", "Errore", JOptionPane.ERROR_MESSAGE);
			
		}
	}
	
	public void execute(String sqlCode, int runNum)
	{
		try {
			
			Main.window.log.append("########## RUN " + runNum + "##########\n\n");
			Main.window.log.append(super.execute(sqlCode));
			Main.window.log.append("\n\n\n\n");
			
			Main.renderer.setTable(getTable());
		
		} catch (SQLException e) {
			
			e.printStackTrace();
			Main.window.log.append("\n[ERR] " + e.getMessage());
			JOptionPane.showMessageDialog(Main.window, "Esecuzione del comando fallita", "Errore", JOptionPane.ERROR_MESSAGE);
			
		}
	}
}
