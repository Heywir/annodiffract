package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Fenetre extends JFrame implements ActionListener{

	JFrame mainWindow = null;
	JPanel mainPanel = null;
	JMenuBar mainMenuBar = null;
	JMenu menuFile = null;
	JMenuItem menuItemOuvrir = null;
	
	public Fenetre() {
		
		// Composants
	
		mainWindow = new JFrame("AnnoDiffract");
		mainPanel = new JPanel();
		mainMenuBar = new JMenuBar();
		menuFile = new JMenu("Fichier");
		menuItemOuvrir = new JMenuItem("Ouvrir");
		
		// Layout
		
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);
		
		// Window Settings
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		// MenuBar
		
		mainMenuBar.add(menuFile);
		menuFile.add(menuItemOuvrir);
		
		// Listeners
		
		menuItemOuvrir.addActionListener(this);
		
		// Ajouts
		
		this.add(mainMenuBar, BorderLayout.NORTH);
		this.add(mainPanel, BorderLayout.CENTER);
		
	}
	
	public static void main(String[] args) {
		
		Fenetre window = new Fenetre();
		window.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
