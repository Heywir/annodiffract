package main;

import javax.swing.JFrame;

public class Fenetre extends JFrame{

	JFrame mainWindow = null;
	
	public Fenetre() {
	
		mainWindow = new JFrame("AnnoDiffract");
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		// Window Settings
		
		
		
	}
	
	public static void main(String[] args) {
		
		Fenetre Window = new Fenetre();
		Window.setVisible(true);
		
	}
}
