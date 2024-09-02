package model;

import java.util.*;

public class Keywords {

	private HashSet<String> keywords;

	String _keywords = "";
			
	public Keywords() {
		
		keywords = new HashSet<String>();
		String str = "global,extern,section,.data,.text.bss,db,dw,dd,dq,byte,word,dword,qword";
		String array[] = str.split(",", 0);
		for (String s : array) {
			keywords.add( s );
		}
	}
	
	public boolean contains( String s ) {
		return keywords.contains( s );
	}
	
	public String toString() {
		String s = "";
		for (String w : keywords) {
			s += w + "|";
		}
		return s;
	}
}