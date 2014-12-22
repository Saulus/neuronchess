/**
 * 
 */

/**
 * @author Paul
 *
 */

public class Position {
	public byte h;
	public byte v;
	
	public Position (byte h, byte v) {
		this.v=v;
		this.h=h;
	}
	
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other.getClass() != getClass())
			return false;

		if (h != ((Position)other).h)
			return false;
		if (v != ((Position)other).v)
			return false;
		return true;
	} 
}

