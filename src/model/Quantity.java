package model;

import java.util.*;

/**
 * Define a Quantity that stores a certain number of consecutive
 * bits.
 * 
 * @author Jean-Michel Richer
 *
 */
public class Quantity {

	protected static String hexAlphabet[] = { "0", "1", "2", "3", "4", "5", "6", "7",
			"8,", "9", "A", "B", "C", "D", "E", "F"};
	
	// size in number of bits
	protected int size;
	protected BitSet data;
	
	/**
	 * Define a Quantity by giving its size
	 * @param size number of bits stored
	 */
	public Quantity( int size ) {
		
		this.size = size;
		this.data = new BitSet( size );
		
	}
	
	public int getSize() {
		
		return size;
		
	}
	
	public void set( Quantity qty ) {
	
		
	}
	
	/**
	 * Set contents by using an integer
	 * @param value
	 */
	public void set( int value ) {
		
		if (value == 0) {
			
			data.clear();
			
		} else {
			
			int mask = 1;
			for (int i = 0; i < size; ++i, mask <<= 1) {
				if ((value & mask) != 0) {
					data.set( i );
				} else {
					data.clear( i );
				}
			}
			
		}
		
	}
	
	/**
	 * Set contents from binary String
	 * @param s String composed of '0', '1' and '_' that is a separator
	 */
	public void setBin( String s ) throws IllegalArgumentException {
		String alphabet = "01_";
		
		if (s.length() == 0) return ;
		int length = s.length();
		int bit = 0;
		int i = length - 1;
		while (i >= 0) {
			char c = s.charAt( i );
			if (s.indexOf( c ) == -1) {
				throw new IllegalArgumentException("Character " + c + " not allowed");
			}
			if (c == '1') {
				data.set( bit++ );
			} else if (c == '0') {
				data.clear( bit++ );
			} 
			--i;
		}
		
	}
	
	public String toBin() {
		
		String s = "";
		for (int i = 0; i < size; ++i) {
			if (data.get( i ) == true) {
				s = "1" + s;
			} else {
				s = "0" + s;
			}
			if ( ((i + 1) % 4) == 0 ) {
				if (i !=( size-1)) {
					s = "_" + s;
				}
			}
		}
		
		
		return s;
		
	}
	
	public String toHex() {
		
		String s = "";
		
		for (int i = 0; i < size; i += 4) {
			
			int quartet = 0;
			for (int j = 0; (j < 4) && ((i+j) < size); ++j) {
				if (data.get(i + j) == true) {
					quartet |= (1 << j);
				}
			}
			
			s = hexAlphabet[ quartet ] + s;
			
			if ( ((i+4) % 8) == 0 ) {
				if ( (i+4) < (size-1) ) {
					s = "_" + s;
				}
			}
			
		}
		
		return s;
	}
	
	public String toOct() {
		
		String s = "";
		
		for (int i = 0; i < size; i += 3) {
			
			int quartet = 0;
			for (int j = 0; (j < 3) && ((i+j) < size); ++j) {
				if (data.get(i + j) == true) {
					quartet |= (1 << j);
				}
			}
			
			s = hexAlphabet[ quartet ] + s;
			
			if ( ((i+3) % 9) == 0 ) {
				if ( (i+3) < (size-1) ) {
					s = "_" + s;
				}
			}
			
		}
		
		return s;
	}
	
	/**
	 * Extract bits from this Quantity from bits fromIndex (included) to 
	 * bit toIndex (excluded) and put it at atIndex of qty
	 * 
	 * @param fromIndex
	 * @param toIndex
	 * @param qty
	 * @param atIndex
	 */
	public void extractAndSetTo( int fromIndex, int toIndex, Quantity qty, int atIndex ) {
		
		if ((fromIndex < 0) || (fromIndex > size)) {
			throw new IllegalArgumentException("fromIndex is illegal " + fromIndex);
		}
		
		if ((toIndex < 0) || (toIndex > size)) {
			throw new IllegalArgumentException("toIndex is illegal " + toIndex);
		}
		
		if (fromIndex > toIndex) {
			throw new IllegalArgumentException("fromIndex > toIndex");
		}
		
		if ((atIndex < 0) || (atIndex > qty.getSize())) {
			throw new IllegalArgumentException("atIndex is illegal " + atIndex);
		}
		
		int width = toIndex - fromIndex;
		
		if (atIndex + width > qty.getSize()) {
			throw new IllegalArgumentException("size of source is bigger than destination");
		}
		
		
		for (int i = 0; i < width; ++i) {
			qty.data.set( atIndex + i, data.get( i + fromIndex ) );
		}
	}
	
	
	public void setFrom( int atIndex, Quantity qty, int fromIndex, int size ) {
		
	}
	
	
}
