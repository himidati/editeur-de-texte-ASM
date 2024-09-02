package test;

import java.awt.*;
import javax.swing.*;

public class MainWindow extends JFrame {

	protected JTextPane textPane;
	
	public MainWindow() {
		getContentPane().setLayout( new BorderLayout() );
		textPane = new JTextPane();
		getContentPane().add( BorderLayout.CENTER, textPane );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setSize( 640, 480 );
		setTitle("Editor");
		setVisible( true );
	}
	
	public static void main(String args[]) {
		
		MainWindow main = new MainWindow();
		
	}
	
}
