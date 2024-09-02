package model.memory;

public class Variable {

	/** name of variable */
	protected String name;
	/** size in bytes */
	protected int size;
	/** address in memory */
	protected int address;
	
	public Variable( String name, int size ) {
		
		this.name = name;
		this.size = size;
		
		// not defined yet
		address = -1;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}
	
	
}
