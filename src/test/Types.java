package test;

import java.util.*;


class Variable {
	protected String name;
	protected int address;
	protected int size;
	
	public Variable( String name, int size ) {
		this.name = name;
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}
	
}

class Memory {
	protected List<BitSet> array;
	protected Map<String, Variable> variables;
	
	public Memory() {
		array = new ArrayList<BitSet>();
		variables = new HashMap<String, Variable>();
	}
	
	public void declareVariable( Variable var ) {
		variables.put( var.getName(), var );
	}
	
	public void declareByte( Variable var, short value ) {
		var.setAddress( array.size() );
		variables.put( var.getName(), var );
		BitSet bs = new BitSet(8);
		for (int i = 0; i < 8; ++i) {
			if ((value & (1 << i)) != 0) {
				bs.set( i );
			}
		}
		array.add( bs );
		
	}
	
	public void declareDoubleWord( Variable var, long value ) {
		var.setAddress( array.size() );
		variables.put( var.getName(), var );
		BitSet bs;
		bs = new BitSet(8);
		for (int i = 0; i < 8; ++i) {
			if ((value & (1 << i)) != 0) {
				bs.set( i );
			}
		}
		array.add( bs );
		
		bs = new BitSet(8);
		for (int i = 0; i < 8; ++i) {
			if ((value & (1 << (i+8))) != 0) {
				bs.set( i );
			}
		}
		array.add( bs );
		
		bs = new BitSet(8);
		for (int i = 0; i < 8; ++i) {
			if ((value & (1 << (i+16))) != 0) {
				bs.set( i );
			}
		}
		array.add( bs );
		
		bs = new BitSet(8);
		for (int i = 0; i < 8; ++i) {
			if ((value & (1 << (i+24))) != 0) {
				bs.set( i );
			}
		}
		array.add( bs );
		
	}
	
	public void declareString( Variable var, String s ) {
		var.setAddress( array.size() );
		variables.put( var.getName(), var );
		BitSet bs;
		for (int k = 0; k < s.length(); ++k) {
			bs = new BitSet(8);
			short value = (short) s.charAt(k);
			for (int i = 0; i < 8; ++i) {
				if ((value & (1 << i)) != 0) {
					bs.set( i );
				}
			}
			array.add( bs );
		}
	}
	
	public String toString() {
		String s = "";
		return s;
	}
	
	
}

public class Types {

	public static void main(String args[]) {
		
		byte b[] = Long.toHexString( -1 ).getBytes();
		String s = Long.toBinaryString( Long.valueOf( "4294967296" ) );
		
		System.out.println( s );
		
		BitSet bs = new BitSet( s.length() );
		
		for (int i = 0; i < s.length(); ++i) {
			int index = s.length() - 1 - i;
			System.out.println( index + " " + s.charAt(index));
			if (s.charAt(index) == '1') {
				bs.set(i);
			}
		}
		
		System.out.println( bs.toString() );
		
		int powers[] = { 1, 2, 4, 8, 16, 32, 64, 128};
		
		long l = 0;
		
		for (int i = 64-8; i >= 0; i-=8) {
			int bb = 0;
			for (int j=0; j<8; ++j) {
				if (bs.get(i+j) == true) {
					bb |= powers[ j ]; 
				}
			}
			
			l = l * 256 + bb;
			System.out.println( i +": " + bb + ", " + l);
		}
		
		System.out.println(l);
		
		Memory mem = new Memory();
		Variable var1 = new Variable( "db", 1 );
		Variable var2 = new Variable( "dw", 4 );
		Variable var3 = new Variable( "ds", 5 );
		
		mem.declareByte(var1, (short) 255);
		mem.declareDoubleWord(var2,  (long) 1294967291 );
		mem.declareString(var3, "hello");
		
		System.out.println( mem );
	}
}
