
public class Pair {
	public int left;
	public int right;
	public int typeLeft;
	public int typeRight;
	public int sign;

	public Pair(int left_, int right_, int typeLeft_, int typeRight_, int sign_) {
		left = left_;
		right = right_;
		typeLeft = typeLeft_;
		typeRight = typeRight_;
		sign = sign_;
	}

	public void printPair() {
		System.out.println("(" + left + "," + right + ")" + " (" + typeLeft + "," + typeRight + ")");
	}
}
