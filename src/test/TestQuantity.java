package test;

import model.*;

public class TestQuantity {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Quantity octet = new Quantity(8);
		Quantity word  = new Quantity(16);
		
		octet.set(129);
		word.setBin("1010_1111_0000_0011");
		
		System.out.println( octet.toBin() );
		System.out.println( word.toBin() );
		System.out.println( word.toHex() );
		
		word.extractAndSetTo(0, 8, octet, 0);
		System.out.println( octet.toBin() );
		
		
		
	}

}
