package test;

import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;

public class AssemblyDocument extends DefaultStyledDocument {
	
	private DefaultStyledDocument doc;
    private Element rootElement;
    
    public AssemblyDocument() {
    	doc = this;
    	rootElement = doc.getDefaultRootElement();
        putProperty( DefaultEditorKit.EndOfLineStringProperty, "\n" );
    }
}
