package model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import model.*;

/* ==================================================================
 * CLASS
 *	Representation of a Register defined by
 *  - its name, for example "eax"
 *	- an array of bits
 *	- bits that are modified by this register (startIndex, stopIndex
 *		and size = stopIndex - startIndex + 1)
 *	- information to print the register (base = 2, 8, 10, 16, and
 *		format = BYTE, WORD, ...)
 * ==================================================================
 */
public class Register {

	public final static int BIT = 0;
	public final static int BYTE = 1;
	public final static int WORD = 2;
	public final static int DOUBLE_WORD = 4;
	public final static int QUAD_WORD = 8;
	public final static int FLOAT = 16;
	public final static int DOUBLE = 32;
	
	protected String name;
	protected Quantity quantity;
	protected int startIndex, stopIndex, size;
	protected int base;
	protected int format;
	
	
	public Register(String name, Quantity quantity, int startIndex, int stopIndex) {
		this.name = name;
		this.quantity = quantity;
		this.startIndex = startIndex;
		this.stopIndex = stopIndex;
		this.size = (stopIndex - startIndex + 1);
		this.base = 10;
		this.format = BYTE;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getSize() {
		return this.size;
	}

	public int getBase() {
		return this.base;
	}
	
	public void setBase( int b ) {
		this.base = b;
	}
	
	
}