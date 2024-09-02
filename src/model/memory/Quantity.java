package model.memory;

public class Quantity {

	// size in bytes
	protected int size;
	protected short array[];
	
	public Quantity( int size ) {
		this.size = size;
		array = new short[ size ];
	}
	
	public static Quantity declareByte( short value ) {
		Quantity q = new Quantity( 1 );
		q.array[ 0 ] = value;
		return q;
	}
	
	public static Quantity declareDoubleWord( long value ) {
		Quantity q = new Quantity( 4 );
		for (int i = 0; i < 4; ++i) {
			q.array[ i ] = (short) ((value >> (8*i)) & 255);
		}
		return q;
	}
	
	public static Quantity declareFloat( long value ) {
		Quantity q = new Quantity( 4 );
		for (int i = 0; i < 4; ++i) {
			q.array[ i ] = (short) ((value >> (8*i)) & 255);
		}
		return q;
	}
	
	
}
