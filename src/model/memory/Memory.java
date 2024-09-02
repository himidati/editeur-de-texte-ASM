package model.memory;

import java.util.*;

public class Memory {

	protected List<Byte> data;
	protected Map<Variable, Integer> variables;
	
	public Memory() {
		
		data = new ArrayList<Byte>();
		variables = new HashMap<Variable, Integer>();
	
	}
	
	public void declareVariable( Variable variable ) {
		
		variable.setAddress(Integer.valueOf( data.size() ) );
		variables.put( variable, Integer.valueOf( data.size() ) );
		
	}
	
	public int getVariableAddress( String name ) {
		
		return variables.get( name );
		
	}
	
	public void addByte( Byte b ) {
		
		data.add( b );
		
	}
	
	public void addString( String s ) {
		
		byte bytes[] = s.getBytes();
		for (byte b : bytes) {
			data.add( b );
		}
		
	}
	
	public void addDoubleWord( ) {
		
	}
	
	
}
