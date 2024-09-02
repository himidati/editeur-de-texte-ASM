package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.*;
import model.*;

public class MainWindow extends JFrame implements ActionListener {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected JMenuBar menuBar;
	protected JMenu menuFile, menuEdit, menuHelp;
	protected JMenuItem menuFileNew, menuFileOpen, menuFileSave, menuFileSaveAs, menuFileQuit;
	protected JMenuItem menuEditUndo, menuEditRedo, menuEditCut, menuEditPaste, menuEditCopy, menuEditFind;
	protected JMenuItem menuHelpAbout;
	
	protected ArrayList<JMenuItem> itemsList;
	
	// Editor with assembly style
	protected JTextPane editor;
	// HTML Documentation
	protected JEditorPane documentation;
	
	protected JDesktopPane desktop;
	//protected JInternalFrame internalFrame;
	
	protected ImageIcon codingIcon, frameIcon;
	
	protected Mnemonics mnemonics;
	protected Registers registers;
	protected Keywords keywords;
	
	/**
	 * Create menu
	 * @return
	 */
	private JMenuBar createMenuBar() {
		
		menuBar = new JMenuBar();
		
		itemsList = new ArrayList<JMenuItem>();
		
		// File menu
		menuFile = new JMenu("File");
		menuFile.setMnemonic( KeyEvent.VK_F );
		menuFile.addActionListener( this );
		menuBar.add( menuFile );
		
		menuFileNew  = new JMenuItem( "New", KeyEvent.VK_N );
		menuFileOpen = new JMenuItem( "Open", KeyEvent.VK_O );
		menuFileSave = new JMenuItem( "Save", KeyEvent.VK_S );
		menuFileSaveAs = new JMenuItem( "Save as", KeyEvent.VK_A );
		menuFileQuit = new JMenuItem( "Quit", KeyEvent.VK_Q );
		menuFile.add( menuFileNew );
		menuFile.add( menuFileOpen );
		menuFile.add( menuFileSave );
		menuFile.add( menuFileSaveAs );
		menuFile.add( menuFileQuit );
		
		itemsList.add( menuFileNew );
		itemsList.add( menuFileOpen );
		itemsList.add( menuFileSave );
		itemsList.add( menuFileSaveAs );
		itemsList.add( menuFileQuit );
		
		// Editor
		menuEdit = new JMenu("Edit");
		menuEdit.setMnemonic( KeyEvent.VK_E );
		menuEdit.addActionListener( this );
		menuBar.add( menuEdit );

		menuEditUndo  = new JMenuItem( "Undo", KeyEvent.VK_Z );
		menuEditRedo  = new JMenuItem( "Redo", KeyEvent.VK_Y );
		menuEditCut  = new JMenuItem( "Cut", KeyEvent.VK_X );
		menuEditPaste = new JMenuItem( "Paste", KeyEvent.VK_V );
		menuEditCopy  = new JMenuItem( "Copy", KeyEvent.VK_C );
		
		menuEdit.add(menuEditUndo);
		menuEdit.add(menuEditRedo);
		menuEdit.add(menuEditCut);
		menuEdit.add(menuEditPaste);
		menuEdit.add(menuEditCopy);

		itemsList.add(menuEditCopy);
		itemsList.add(menuEditCut);
		itemsList.add(menuEditPaste);
		itemsList.add(menuEditUndo);
		itemsList.add(menuEditRedo);


		// Help
		menuHelp = new JMenu("Help");
		menuHelp.setMnemonic( KeyEvent.VK_H );
		menuHelp.addActionListener( this );
		menuBar.add( menuHelp );
		

		menuHelpAbout = new JMenuItem("About");

		menuHelp.add(menuHelpAbout);
		itemsList.add(menuHelpAbout);

		// record all menu items for the action listener
		for (JMenuItem mi : itemsList) {
			mi.addActionListener(this);
		}
		
		return menuBar;
		
	}
	
	/**
	 * Create interface
	 */
	private void createInterface() {
		
		EditorKit editorKit = new StyledEditorKit()
		{
			public Document createDefaultDocument()
			{
				return new AssemblyDocument( keywords, mnemonics, registers );
			}
		};
		
		editor = new JTextPane();
		editor.setEditorKit( editorKit );
		
		getContentPane().setLayout( new BorderLayout() );
		desktop = new JDesktopPane();
		desktop.setVisible(true);
		getContentPane().add( BorderLayout.CENTER, desktop );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		
				
	}
	
	public MainWindow() {

		mnemonics = new model.Mnemonics();
		registers = new model.Registers();
		keywords = new model.Keywords();
		
		System.err.println( keywords );
		
		codingIcon = new ImageIcon("images/binary-code.png", "Coding");
		frameIcon = new ImageIcon("images/web-programming.png");
		
		System.err.println( frameIcon.getIconHeight() );

		setJMenuBar( createMenuBar() );
		createInterface();
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
		setIconImage( frameIcon.getImage() );
		setSize( 640, 480 );
		setTitle("Assembly Editor");
		
		setVisible( true );
		
	}

	public static void main(String args[]) {
		// load icons
		
		MainWindow mainWindow = new MainWindow();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() instanceof JMenu) {
			
		} else if (e.getSource() instanceof JMenuItem) {
			
			JMenuItem mi = (JMenuItem) e.getSource();
			if (mi == menuFileQuit) {
				System.exit(1);
			} else if (mi == menuFileNew) {
				
				JInternalFrame internalFrame = new EditionInternalFrame("new.asm",
						keywords, mnemonics, registers 
					);

	
				internalFrame.setFrameIcon( codingIcon );
				internalFrame.setResizable(true);
				internalFrame.setIconifiable(true);
				internalFrame.setSize(320, 200);
				internalFrame.setVisible(true);
				desktop.add(internalFrame);
			}
			
		}
		
	}
}
