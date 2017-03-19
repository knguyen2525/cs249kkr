import java.util.ArrayList;

public class Pair {
	public int left;
	public int right;
	public int typeLeft;
	public int typeRight;
	public int sign;
	public ArrayList<Integer> fullIndexPath = null;
	public ArrayList<Integer> fullTypePath = null;

	public Pair(int left_, int right_, int typeLeft_, int typeRight_, int sign_, ArrayList<Integer> fullIndexPath_, ArrayList<Integer> fullTypePath_) {
		left = left_;
		right = right_;
		typeLeft = typeLeft_;
		typeRight = typeRight_;
		sign = sign_;
		fullIndexPath = fullIndexPath_;
		fullTypePath = fullTypePath_;
	}

	public void printPair() {
		System.out.println("(" + left + "," + right + ")" + " (" + typeLeft + "," + typeRight + ")");
		printList(fullIndexPath);
		printList(fullTypePath);
	}

	public String indexPathToString() {
		String result = "(";
		for(int i=0; i<fullIndexPath.size(); i++) {
			if(i+1 == fullIndexPath.size())
				result = result + fullIndexPath.get(i) + ")";
			else
				result = result + fullIndexPath.get(i) + ", ";
		}
		return result;
	}

	public String typePathToString() {
		String result = "(";
		for(int i=0; i<fullTypePath.size(); i++) {
			if(i+1 == fullTypePath.size())
				result = result + fullTypePath.get(i) + ")";
			else
				result = result + fullTypePath.get(i) + ", ";
		}
		return result;
	}

	public void printList(ArrayList<Integer> list) {
		System.out.print("(");
		for(int i=0; i<list.size(); i++) {
			if(i+1 == list.size())
				System.out.print(list.get(i));
			else
			System.out.print(list.get(i) + ", ");
		}
		System.out.print(")");
		System.out.print("\n");
	}
}
