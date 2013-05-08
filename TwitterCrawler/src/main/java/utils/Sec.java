package utils;

public final class Sec {
	
	private int val;
	
	public Sec(int val) {
		this.val = val;
	}
	
	public void decrement() {
		val --;
	}
	
	public boolean isZero() {
		return val == 0;
	}
	
	public int getVal() {
		return val;
	}
}
