package gui;

import java.awt.*;
import java.util.*;
import java.util.regex.*;

import javax.swing.text.*;

import model.Keywords;
import model.Mnemonics;
import model.Registers;
import model.*;

public class AssemblyDocument extends DefaultStyledDocument {

	private DefaultStyledDocument doc;
    private Element rootElement;

    private MutableAttributeSet normalStyle;
    // for global section word dword ...
    private MutableAttributeSet keywordStyle;
    // for mov, add, sub, push, ...
    private MutableAttributeSet mnemonicStyle;
    private MutableAttributeSet commentStyle;
    private MutableAttributeSet quoteStyle;
    // for eax, ebx, al, ah, st0, ...
    private MutableAttributeSet registerStyle;
    private MutableAttributeSet constantStyle;
    
    private MutableAttributeSet labelStyle;
    
    // list of keywords
    Keywords keywords;
    // list of mnemonics
    Mnemonics mnemonics;
    // list of registers
    Registers registers;
    
    int lastLineProcessed = -1;
    
    
    public AssemblyDocument(Keywords kw, Mnemonics mn, Registers rg) {
    	doc = this;
    	rootElement = doc.getDefaultRootElement();
        putProperty( DefaultEditorKit.EndOfLineStringProperty, "\n" );
        
        keywords = kw;
        mnemonics = mn;
        registers = rg;

        Color normalColor = new Color(0, 0, 0);
        Color commentColor = new Color(138, 129, 121);
        Color keywordColor = new Color(74,102,141);
        Color registerColor = new Color(90,129,60);
        Color mnemonicColor = new Color(74,102,141);
        Color quotedColor = new Color(204,60,34);
        Color constantColor = new Color(204,60,34);
        Color labelColor = new Color( 27, 254, 229 );
        
        normalStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(normalStyle, normalColor);
        StyleConstants.setFontFamily(normalStyle, "Monospace");
    	
        commentStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(commentStyle, commentColor);
        StyleConstants.setItalic(commentStyle, true);

        keywordStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(keywordStyle, keywordColor);
        StyleConstants.setBold(keywordStyle, true);
	
        quoteStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(quoteStyle, quotedColor);

        registerStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(registerStyle, registerColor);
        StyleConstants.setBold(registerStyle, true);

        mnemonicStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(mnemonicStyle, mnemonicColor);
        StyleConstants.setBold(mnemonicStyle, true);

        constantStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(constantStyle, constantColor);
        StyleConstants.setBold(constantStyle, true);
  
        labelStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(labelStyle, labelColor);
        StyleConstants.setBold(labelStyle, true);
       
        
        TabStop[] tabs = new TabStop[4];
        tabs[0] = new TabStop(10, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE);
        tabs[1] = new TabStop(20, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE);
        tabs[2] = new TabStop(30, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE);
        TabSet tabset = new TabSet(tabs);
        
        StyleContext sc = StyleContext.getDefaultStyleContext();
        sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.TabSet, tabset);
        
    }
    
    /*
     *  Override to apply syntax highlighting after the document has been updated
     */
    public void insertString(int offset, String str, AttributeSet a) 
    		throws BadLocationException{
     
        super.insertString(offset, str, a);
        processChangedLines(offset, str.length());
        
    }
    
    /*
     *  Override to apply syntax highlighting after the document has been updated
     */
    public void remove(int offset, int length) 
    		throws BadLocationException {
    	
        super.remove(offset, length);
        processChangedLines(offset, 0);
        
    }

    /*
     *  Determine how many lines have been changed,
     *  then apply highlighting to each line
     */
    public void processChangedLines(int offset, int length)
        throws BadLocationException {
    	
        String content = doc.getText(0, doc.getLength());

        //  The lines affected by the latest document update

        int startLine = rootElement.getElementIndex(offset);
        int endLine = rootElement.getElementIndex(offset + length);

        if (startLine > endLine)
            startLine = endLine;

        //  Make sure all comment lines prior to the start line are commented
        //  and determine if the start line is still in a multi line comment

        
        //  Do the actual highlighting

        for (int i = startLine; i <= endLine; i++)
        {
            applyHighlighting(content, i);
        }

        highlightLinesAfter(content, endLine);

    }

    
    /*
     *  Highlight lines to start or end delimiter
     */
    private void highlightLinesAfter(String content, int line)
        throws BadLocationException {
    	
        int offset = rootElement.getElement( line ).getEndOffset();

        //  Start/End delimiter not found, nothing to do

        int startDelimiter = indexOf( content, getStartDelimiter(), offset );
        int endDelimiter = indexOf( content, getEndDelimiter(), offset );

        if (startDelimiter < 0)
            startDelimiter = content.length();

        if (endDelimiter < 0)
            endDelimiter = content.length();

        int delimiter = Math.min(startDelimiter, endDelimiter);

        if (delimiter < offset)
            return;

        //  Start/End delimiter found, reapply highlighting

        int endLine = rootElement.getElementIndex( delimiter );

        for (int i = line + 1; i <= endLine; i++)
        {
            Element branch = rootElement.getElement( i );
            Element leaf = doc.getCharacterElement( branch.getStartOffset() );
            AttributeSet as = leaf.getAttributes();

            if ( as.isEqual(commentStyle) )
            {
                applyHighlighting(content, i);
            }
        }
    }

    /*
     *  Parse the line to determine the appropriate highlighting
     */
    private void applyHighlighting(String content, int line)
        throws BadLocationException
    {
    	
        lastLineProcessed = line;

        int startOffset = rootElement.getElement( line ).getStartOffset();
        int endOffset = rootElement.getElement( line ).getEndOffset() - 1;

        int lineLength = endOffset - startOffset;
        int contentLength = content.length();

        if (endOffset >= contentLength)
            endOffset = contentLength - 1;

        //  set normal attributes for the line

        doc.setCharacterAttributes(startOffset, lineLength, normalStyle, true);

        //  check for single line comment

        int index = content.indexOf(getSingleLineDelimiter(), startOffset);

        if ( (index > -1) && (index < endOffset) )
        {
            doc.setCharacterAttributes(index, endOffset - index + 1, commentStyle, false);
            endOffset = index - 1;
        }

        //  check for tokens

        checkForTokens(content, startOffset, endOffset);
    }
    

    /*
     *  Parse the line for tokens to highlight
     */
    private void checkForTokens(String content, int startOffset, int endOffset) {

    	System.err.println("checkForTokens " + startOffset + " " + endOffset);
    	
        while (startOffset <= endOffset)
        {
            //  skip the delimiters to find the start of a new token

            while ( isDelimiter( content.substring(startOffset, startOffset + 1) ) )
            {
                if (startOffset < endOffset)
                    startOffset++;
                else
                    return;
            }

            //  Extract and process the entire token

            if ( isQuoteDelimiter( content.substring(startOffset, startOffset + 1) ) )
                startOffset = getQuoteToken(content, startOffset, endOffset);
            else
                startOffset = getOtherToken(content, startOffset, endOffset);
        }
        
    }
    
   /**
    *
    */
   private int getQuoteToken(String content, int startOffset, int endOffset) {
	   
       String quoteDelimiter = content.substring(startOffset, startOffset + 1);
       String escapeString = getEscapeString(quoteDelimiter);

       int index;
       int endOfQuote = startOffset;

       //  skip over the escape quotes in this quote

       index = content.indexOf(escapeString, endOfQuote + 1);

       while ( (index > -1) && (index < endOffset) )
       {
           endOfQuote = index + 1;
           index = content.indexOf(escapeString, endOfQuote);
       }

       // now find the matching delimiter

       index = content.indexOf(quoteDelimiter, endOfQuote + 1);

       if ( (index < 0) || (index > endOffset) )
           endOfQuote = endOffset;
       else
           endOfQuote = index;

       doc.setCharacterAttributes(startOffset, endOfQuote - startOffset + 1, quoteStyle, false);

       return endOfQuote + 1;
       
   }

   /*
    *
    */
   private int getOtherToken(String content, int startOffset, int endOffset)
   {
       int endOfToken = startOffset + 1;

       while ( endOfToken <= endOffset )
       {
           if ( isDelimiter( content.substring(endOfToken, endOfToken + 1) ) )
               break;

           endOfToken++;
       }

       String token = content.substring(startOffset, endOfToken);
       
       System.err.println("otherToken [" + token + "]");

       if ( isKeyword( token ) ) {
           doc.setCharacterAttributes(startOffset, endOfToken - startOffset, keywordStyle, false);
        
       } else if ( isMnemonic( token ) ) {
           doc.setCharacterAttributes(startOffset, endOfToken - startOffset, mnemonicStyle, false);
       
       } else if ( isRegister( token ) ) {
           doc.setCharacterAttributes(startOffset, endOfToken - startOffset, registerStyle, false);
           
       } else if ( isConstant( token )) {
    	   doc.setCharacterAttributes(startOffset, endOfToken - startOffset, constantStyle, false);
    	   
       } else if ( isLabel( token )) {
    	   doc.setCharacterAttributes(startOffset, endOfToken - startOffset, labelStyle, false);
    	   
       }
       
       
       return endOfToken + 1;
       
   }
   
   /*
    *  Assume the needle will be found at the start/end of the line
    */
   private int indexOf(String content, String needle, int offset)
   {
       int index;

       while ( (index = content.indexOf(needle, offset)) != -1 )
       {
           String text = getLine( content, index ).trim();

           if (text.startsWith(needle) || text.endsWith(needle))
               break;
           else
               offset = index + 1;
       }

       return index;
   }

 
   private String getLine(String content, int offset) {
	   
       int line = rootElement.getElementIndex( offset );
       Element lineElement = rootElement.getElement( line );
       int start = lineElement.getStartOffset();
       int end = lineElement.getEndOffset();
       return content.substring(start, end - 1);
       
   }

   /*
    *  Override for other languages
    */
   protected boolean isDelimiter(String character) {
	   
       String operands = ",;{}()[]+-/%<=>!&|^~*";

       if (Character.isWhitespace( character.charAt(0) )
       ||  operands.indexOf(character) != -1 )
           return true;
       else
           return false;
   }
   
   
   /*
    *  Override for other languages
    */
   protected boolean isQuoteDelimiter(String character)
   {
       String quoteDelimiters = "\"'";

       if (quoteDelimiters.indexOf(character) < 0)
           return false;
       else
           return true;
   }

   /*
    *  Override for other languages
    */
   protected boolean isKeyword(String token) {
	   
       return keywords.contains( token );
       
   }

   protected boolean isMnemonic(String token) {
	   
       return mnemonics.contains( token );
       
   }
   
   protected boolean isRegister(String token) {
	   
       return registers.contains( token );
       
   }
   
   protected boolean isConstant(String token) {
	   
	   String regex = "[0-9]+";
	   Pattern pattern = Pattern.compile(regex);
	   Matcher matcher = pattern.matcher(token);
       return matcher.find();
       
   }
   
   protected boolean isLabel(String token) {
	   return token.startsWith(".") || token.endsWith(":");
   }

   /*
    *  Override for other languages
    */
   protected String getStartDelimiter() {
       
	   return "/*";
	   
   }

   /*
    *  Override for other languages
    */
   protected String getEndDelimiter() {
       
	   return "*/";
	   
   }

   /*
    *  Override for other languages
    */
   protected String getSingleLineDelimiter() {
       
	   return ";";
       
   }

   /*
    *  Override for other languages
    */
   protected String getEscapeString(String quoteDelimiter) {
	   
       return "\\" + quoteDelimiter;
       
   }
    
}
