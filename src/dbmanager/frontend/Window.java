package dbmanager.frontend;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.jgoodies.forms.factories.DefaultComponentFactory;

import dbmanager.main.Main;
import framework.Module;

public class Window extends JFrame
{
	private static final long serialVersionUID = 3894510086087199629L;
	public JTextField serverPort;
	public JTextField serverIp;
	public JTextField databaseName;
	public JTextField username;
	public JPasswordField password;
	public JTextArea log;
	public Canvas canvas;
	public JTabbedPane tabbedPane;
	
	private int runNum = 0;
	
	private static final String LEFT = "Left";
    private Action left = new AbstractAction(LEFT) 
    {
		private static final long serialVersionUID = -5153555536863934127L;

		@Override
        public void actionPerformed(ActionEvent e) {
            Main.camera.moveX(-1);
		}
    };
    
    private static final String RIGHT = "Right";
    private Action right = new AbstractAction(RIGHT) 
    {
		private static final long serialVersionUID = 8329159783301808606L;

		@Override
        public void actionPerformed(ActionEvent e) {
			Main.camera.moveX(1);
        }
    };
    
    private static final String UP = "Up";
    private Action up = new AbstractAction(UP) 
    {
		private static final long serialVersionUID = 8470426066740840946L;

		@Override
        public void actionPerformed(ActionEvent e) {
			Main.camera.moveY(-1);
        }
    };
    
    private static final String DOWN = "Down";
    private Action down = new AbstractAction(DOWN) 
    {
		private static final long serialVersionUID = 5001534955852638262L;

		@Override
        public void actionPerformed(ActionEvent e) {
			Main.camera.moveY(1);
        }
    };

	public Window() 
	{
		setResizable(false);
		setTitle("DBManager");
		setSize(500, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		Panel settingsTab = new Panel();
		tabbedPane.addTab("Connessione", null, settingsTab, null);
		settingsTab.setLayout(null);
		
		JButton connectBtn = new JButton("CONNETTI");
		connectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				connectBtn.setEnabled(false);
				connectBtn.setText("CONNESSIONE IN CORSO...");
				Main.sql.connect();
				connectBtn.setText("RICONNETTI");
				connectBtn.setEnabled(true);
			}
		});
		connectBtn.setBounds(12, 265, 471, 71);
		settingsTab.add(connectBtn);
		
		serverPort = new JTextField();
		serverPort.setHorizontalAlignment(SwingConstants.CENTER);
		serverPort.setText("3306");
		serverPort.setBounds(12, 152, 228, 19);
		settingsTab.add(serverPort);
		serverPort.setColumns(10);
		
		JLabel lblServerPort = new JLabel("Porta del server");
		lblServerPort.setBounds(12, 125, 197, 15);
		settingsTab.add(lblServerPort);
		
		serverIp = new JTextField();
		serverIp.setHorizontalAlignment(SwingConstants.CENTER);
		serverIp.setText("localhost");
		serverIp.setBounds(255, 152, 228, 19);
		settingsTab.add(serverIp);
		serverIp.setColumns(10);
		
		JLabel lblServerIp = new JLabel("Indirizzo del server");
		lblServerIp.setBounds(255, 125, 228, 15);
		settingsTab.add(lblServerIp);
		
		databaseName = new JTextField();
		databaseName.setFont(new Font("Dialog", Font.PLAIN, 24));
		databaseName.setHorizontalAlignment(SwingConstants.CENTER);
		databaseName.setBounds(12, 39, 471, 59);
		settingsTab.add(databaseName);
		databaseName.setColumns(10);
		
		JLabel lblDatabaseName = new JLabel("Nome del database");
		lblDatabaseName.setBounds(12, 12, 228, 15);
		settingsTab.add(lblDatabaseName);
		
		username = new JTextField();
		username.setHorizontalAlignment(SwingConstants.CENTER);
		username.setText("root");
		username.setBounds(12, 226, 228, 19);
		settingsTab.add(username);
		username.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(12, 199, 228, 15);
		settingsTab.add(lblUsername);
		
		password = new JPasswordField();
		password.setHorizontalAlignment(SwingConstants.CENTER);
		password.setBounds(255, 226, 228, 19);
		settingsTab.add(password);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(255, 199, 228, 15);
		settingsTab.add(lblPassword);
		
		JPanel modulesTab = new JPanel();
		tabbedPane.addTab("Moduli", null, modulesTab, null);
		modulesTab.setLayout(null);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(12, 180, 471, 13);
		modulesTab.add(separator);
		
		JLabel lblSelectModule = new JLabel("Seleziona un modulo");
		lblSelectModule.setBounds(12, 193, 471, 15);
		modulesTab.add(lblSelectModule);
		
		JComboBox<Module> moduleSelector = new JComboBox<Module>();
		moduleSelector.setFont(new Font("Dialog", Font.BOLD, 14));
		moduleSelector.setBounds(12, 211, 471, 36);
		for (Module module : Main.mManager.getModules()) moduleSelector.addItem(module);
		modulesTab.add(moduleSelector);
		
		JButton executeModuleBtn = new JButton("ESEGUI");
		executeModuleBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				Module toRun = (Module) moduleSelector.getSelectedItem();
				List<String> vars = toRun.getVarNames();
				for (String var : vars) 
				{ 
					String input = JOptionPane.showInputDialog(Main.window, "Inserisci il valore per la variabile '" + var + "'", "Inizializzazione", JOptionPane.PLAIN_MESSAGE);
					toRun.setVariable(var, input);
				}
				Main.sql.execute(toRun.getSQLCode(), runNum ++);
			}
		});
		executeModuleBtn.setBounds(12, 259, 471, 77);
		modulesTab.add(executeModuleBtn);
		
		JLabel lblModuleInfo = DefaultComponentFactory.getInstance().createTitle("INFORMAZIONI SUL MODULO");
		lblModuleInfo.setFont(new Font("Dialog", Font.BOLD, 16));
		lblModuleInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblModuleInfo.setBounds(12, 12, 471, 15);
		modulesTab.add(lblModuleInfo);
		
		JLabel lblAuthor = new JLabel("Autore:");
		lblAuthor.setBounds(12, 39, 461, 15);
		modulesTab.add(lblAuthor);
		
		JLabel lblVersion = new JLabel("Versione:");
		lblVersion.setBounds(12, 66, 471, 15);
		modulesTab.add(lblVersion);
		
		JLabel lblDescription = new JLabel("Descrizione:");
		lblDescription.setVerticalAlignment(SwingConstants.TOP);
		lblDescription.setBounds(12, 93, 471, 15);
		modulesTab.add(lblDescription);
		
		JPanel commandsTab = new JPanel();
		tabbedPane.addTab("Comandi", null, commandsTab, null);
		commandsTab.setLayout(null);
		
		JButton sendCommandBtn = new JButton("Invia comandi");
		sendCommandBtn.setBounds(12, 272, 471, 64);
		commandsTab.add(sendCommandBtn);
		
		JScrollPane commandsScrollPane = new JScrollPane();
		commandsScrollPane.setBounds(12, 12, 471, 248);
		commandsTab.add(commandsScrollPane);
		
		JTextArea commands = new JTextArea();
		commandsScrollPane.setViewportView(commands);
		
		sendCommandBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Main.sql.execute(commands.getText().replace("\n", " "), runNum ++);
			}
		});
		
		JPanel viewerTab = new JPanel();
		tabbedPane.addTab("Visualizzatore", null, viewerTab, null);
		viewerTab.setLayout(new BorderLayout(0, 0));
		
		canvas = new Canvas();
		viewerTab.add(canvas, BorderLayout.CENTER);
		
		JPanel logTab = new JPanel();
		tabbedPane.addTab("Log", null, logTab, null);
		logTab.setLayout(new BorderLayout(0, 0));
		
		JScrollPane logScrollPane = new JScrollPane();
		logTab.add(logScrollPane, BorderLayout.CENTER);
		
		log = new JTextArea();
		logScrollPane.setViewportView(log);
		
		lblAuthor.setText("Autore: " + ((Module) moduleSelector.getSelectedItem()).getAuthor());
		lblVersion.setText("Versione: " + ((Module) moduleSelector.getSelectedItem()).getVersion());
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 113, 471, 55);
		modulesTab.add(scrollPane);
		
		JTextArea descriptionBox = new JTextArea();
		descriptionBox.setBackground(UIManager.getColor("Panel.background"));
		descriptionBox.setEditable(false);
		descriptionBox.setLineWrap(true);
		scrollPane.setViewportView(descriptionBox);
		descriptionBox.setText(((Module) moduleSelector.getSelectedItem()).getDescription());
		
		moduleSelector.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) 
			{
				Module selected = ((Module) e.getItem());
				lblAuthor.setText("Autore: " + selected.getAuthor());
				lblVersion.setText("Versione: " + selected.getVersion());
				descriptionBox.setText(((Module) moduleSelector.getSelectedItem()).getDescription());
			}
		});
		
		// INPUT SHIT THAT I'M TOO LAZY TO MAKE MORE EFFICIENT // IT JUST WORKS DON'T YOU DARE FUCKING TOUCH IT, K?
		
		tabbedPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), LEFT);
		viewerTab.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), LEFT);
		tabbedPane.getActionMap().put(LEFT, left);
		viewerTab.getActionMap().put(LEFT, left);
		
		tabbedPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), RIGHT);
		viewerTab.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), RIGHT);
		tabbedPane.getActionMap().put(RIGHT, right);
		viewerTab.getActionMap().put(RIGHT, right);
		
		tabbedPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), UP);
		viewerTab.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), UP);
		tabbedPane.getActionMap().put(UP, up);
		viewerTab.getActionMap().put(UP, up);
		
		tabbedPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), DOWN);
		viewerTab.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), DOWN);
		tabbedPane.getActionMap().put(DOWN, down);
		viewerTab.getActionMap().put(DOWN, down);
		
		this.requestFocus();
		
		setVisible(true);
	}

	@Override
	public void dispose() 
	{
		Main.exit();
	}
	
	public void finalDispose()
	{
		super.dispose();
	}
}
