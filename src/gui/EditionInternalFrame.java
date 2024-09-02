package gui;

import java.awt.*;
import javax.swing.*;
import model.*;

public class EditionInternalFrame extends JInternalFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected AssemblyDocument document;
	
	public EditionInternalFrame(String title, Keywords kw, Mnemonics mn, Registers rg) {
		
		setTitle( title );
		setSize(320, 200);
		
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		document = new AssemblyDocument( kw, mn, rg );
		//JEditorPane editorPane = new JEditorPane();
		//editorPane.setDocument( document );
		container.add( BorderLayout.NORTH, new JTextPane( document ) );
		//container.add( BorderLayout.NORTH, editorPane );
	}
	

}
