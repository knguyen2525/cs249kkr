import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.io.PrintWriter;
import java.io.File;

public class GenPairs {
	private ArrayList<ArrayList<Integer>> paperToAuthor = new ArrayList<ArrayList<Integer>>();
	private ArrayList<ArrayList<Integer>> paperToVenue = new ArrayList<ArrayList<Integer>>();
	private ArrayList<ArrayList<Integer>> paperToCites = new ArrayList<ArrayList<Integer>>();
	private ArrayList<ArrayList<Integer>> authorToPaper = new ArrayList<ArrayList<Integer>>();
	private ArrayList<ArrayList<Integer>> venueToPaper = new ArrayList<ArrayList<Integer>>();
	private ArrayList<ArrayList<Integer>> citesToPaper = new ArrayList<ArrayList<Integer>>();
	//0 is paper, 1 is author, 2 is venue, 3 is cite
	private Random rand = new Random(System.currentTimeMillis());

	public GenPairs() {
		populate(paperToAuthor, "../matrices/papersToAuthors.csv");
		populate(paperToVenue, "../matrices/papersToVenues.csv");
		populate(paperToCites, "../matrices/papersToCites.csv");
		populate(authorToPaper, "../matrices/authorsToPapers.csv");
		populate(venueToPaper, "../matrices/venuesToPapers.csv");
		populate(citesToPaper, "../matrices/citesToPapers.csv");
	}

	private void addToList(ArrayList<ArrayList<Integer>> xToY, String line) {
		String[] lineSplit = line.split(",");
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for(int i=0; i<lineSplit.length; i++) {
			temp.add(Integer.parseInt(lineSplit[i]));
		}
		xToY.add(temp);
	}

	public void populate(ArrayList<ArrayList<Integer>> xToY, String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	addToList(xToY, line);
		    }
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}

	//Generates positive pairs
	//Symmetric
	public Pair generatePair(int startType, int s){
		int halfPathLength = (rand.nextInt(4) + 1);
		ArrayList<Integer> currTypePath = new ArrayList<Integer>(halfPathLength);
		ArrayList<Integer> currIndexPath = new ArrayList<Integer>(halfPathLength);

		ArrayList<Integer> fullTypePath = new ArrayList<Integer>();
		ArrayList<Integer> fullIndexPath = new ArrayList<Integer>();

		int currType = startType;
		int currIndex = s;
		currTypePath.add(currType);
		currIndexPath.add(currIndex);

		fullTypePath.add(currType);
		fullIndexPath.add(currIndex);
		//might be easier if i make this all recursive
		while(halfPathLength != 0) {
			int nextType = -1;
			int nextIndex = -1;

			while(nextIndex == -1) {
				if(currType == 0) {
					nextType = rand.nextInt(3) + 1;
					if(paperToAuthor.get(currIndex).get(0) == -1 && paperToVenue.get(currIndex).get(0) == -1 && paperToCites.get(currIndex).get(0) == -1) {
						return null;
					}
					if(nextType == 1) {
						//get next author
						ArrayList<Integer> tempList = paperToAuthor.get(currIndex);
						if(tempList.size() != 0 && tempList.get(0) != -1)
							nextIndex = tempList.get(rand.nextInt(tempList.size()));
					}
					else if(nextType == 2) {
						//get next venue
						ArrayList<Integer> tempList = paperToVenue.get(currIndex);
						if(tempList.size() != 0 && tempList.get(0) != -1)
							nextIndex = tempList.get(rand.nextInt(tempList.size()));
					}
					else {
						//get next cite
						ArrayList<Integer> tempList = paperToCites.get(currIndex);
						if(tempList.size() != 0 && tempList.get(0) != -1)
							nextIndex = tempList.get(rand.nextInt(tempList.size()));
					}
				}
				else if(currType == 1) {
					nextType = 0;
					ArrayList<Integer> tempList = authorToPaper.get(currIndex);
					nextIndex = tempList.get(rand.nextInt(tempList.size()));

				}
				else if(currType == 2) {
					nextType = 0;
					ArrayList<Integer> tempList = venueToPaper.get(currIndex);
					nextIndex = tempList.get(rand.nextInt(tempList.size()));
				}
				else {
					nextType = 0;
					ArrayList<Integer> tempList = citesToPaper.get(currIndex);
					nextIndex = tempList.get(rand.nextInt(tempList.size()));
				}
			}

			currType = nextType;
			currIndex = nextIndex;

			currTypePath.add(currType);
			currIndexPath.add(currIndex);
			fullTypePath.add(currType);
			fullIndexPath.add(currIndex);

			halfPathLength--;
		}

		currType = currTypePath.get(currTypePath.size() - 1);
		currIndex = currIndexPath.get(currIndexPath.size() - 1);
		currTypePath.remove(currTypePath.size() - 1);
		currIndexPath.remove(currIndexPath.size() - 1);

		while(currTypePath.size() != 0) {
			int nextIndex = -1;
			int nextType = currTypePath.get(currTypePath.size() - 1);
			while(nextIndex == -1) {
				if(currType == 0) {
					if(nextType == 1) {
						//get next author
						ArrayList<Integer> tempList = paperToAuthor.get(currIndex);
						if(tempList.get(0) == -1) return null;
						if(tempList.size() != 0)
							nextIndex = tempList.get(rand.nextInt(tempList.size()));
					}
					else if(nextType == 2) {
						//get next venue
						ArrayList<Integer> tempList = paperToVenue.get(currIndex);
						if(tempList.get(0) == -1) return null;
						if(tempList.size() != 0)
							nextIndex = tempList.get(rand.nextInt(tempList.size()));
					}
					else {
						//get next cite
						ArrayList<Integer> tempList = paperToCites.get(currIndex);
						if(tempList.get(0) == -1) return null;
						if(tempList.size() != 0)
							nextIndex = tempList.get(rand.nextInt(tempList.size()));
					}
				}
				else if(currType == 1) {
					//we're at author
					ArrayList<Integer> tempList = authorToPaper.get(currIndex);
					nextIndex = tempList.get(rand.nextInt(tempList.size()));

				}
				else if(currType == 2) {
					//we're at venue
					ArrayList<Integer> tempList = venueToPaper.get(currIndex);
					nextIndex = tempList.get(rand.nextInt(tempList.size()));
				}
				else {
					//we're at cites
					ArrayList<Integer> tempList = citesToPaper.get(currIndex);
					nextIndex = tempList.get(rand.nextInt(tempList.size()));
				}
			}

			currType = nextType;
			currIndex = nextIndex;

			fullTypePath.add(currType);
			fullIndexPath.add(currIndex);
			currTypePath.remove(currTypePath.size() - 1);
			currIndexPath.remove(currIndexPath.size() - 1);
		}
		int endType = currType;
		int t = currIndex;

		System.out.print("fullIndexPath: ");
		printList(fullIndexPath);
		System.out.print("fullTypePath: ");
		printList(fullTypePath);

		Pair p = new Pair(s, t, startType, endType, 1, fullIndexPath, fullTypePath);
		return p;
	}

	public Pair generatePairNonSym(int startType, int s){
		int fullPathLength = (rand.nextInt(6) + 1);
		ArrayList<Integer> fullTypePath = new ArrayList<Integer>();
		ArrayList<Integer> fullIndexPath = new ArrayList<Integer>();

		int currType = startType;
		int currIndex = s;

		fullTypePath.add(currType);
		fullIndexPath.add(currIndex);

		while(fullPathLength != 0) {
			int nextType = -1;
			int nextIndex = -1;

			while(nextIndex == -1) {
				if(currType == 0) {
					nextType = rand.nextInt(3) + 1;
					if(paperToAuthor.get(currIndex).get(0) == -1 && paperToVenue.get(currIndex).get(0) == -1 && paperToCites.get(currIndex).get(0) == -1) {
						return null;
					}
					if(nextType == 1) {
						//get next author
						ArrayList<Integer> tempList = paperToAuthor.get(currIndex);
						if(tempList.size() != 0 && tempList.get(0) != -1)
							nextIndex = tempList.get(rand.nextInt(tempList.size()));
					}
					else if(nextType == 2) {
						//get next venue
						ArrayList<Integer> tempList = paperToVenue.get(currIndex);
						if(tempList.size() != 0 && tempList.get(0) != -1)
							nextIndex = tempList.get(rand.nextInt(tempList.size()));
					}
					else {
						//get next cite
						ArrayList<Integer> tempList = paperToCites.get(currIndex);
						if(tempList.size() != 0 && tempList.get(0) != -1)
							nextIndex = tempList.get(rand.nextInt(tempList.size()));
					}
				}
				else if(currType == 1) {
					nextType = 0;
					ArrayList<Integer> tempList = authorToPaper.get(currIndex);
					nextIndex = tempList.get(rand.nextInt(tempList.size()));

				}
				else if(currType == 2) {
					nextType = 0;
					ArrayList<Integer> tempList = venueToPaper.get(currIndex);
					nextIndex = tempList.get(rand.nextInt(tempList.size()));
				}
				else {
					nextType = 0;
					ArrayList<Integer> tempList = citesToPaper.get(currIndex);
					nextIndex = tempList.get(rand.nextInt(tempList.size()));
				}
			}

			currType = nextType;
			currIndex = nextIndex;

			fullTypePath.add(currType);
			fullIndexPath.add(currIndex);

			fullPathLength--;
		}

		int endType = currType;
		int t = currIndex;

		System.out.print("fullIndexPath: ");
		printList(fullIndexPath);
		System.out.print("fullTypePath: ");
		printList(fullTypePath);

		Pair p = new Pair(s, t, startType, endType, 1, fullIndexPath, fullTypePath);
		return p;
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

	private int getRandomElement(int type) {
		//need to make sure the next one has more than 1 element?
		if(type == 0) {
			return rand.nextInt(paperToAuthor.size());
		}
		else if(type == 1) {
			return rand.nextInt(authorToPaper.size());
		}
		else if(type == 2) {
			return rand.nextInt(venueToPaper.size());
		}
		else {
			return rand.nextInt(citesToPaper.size());
		}
	}

	public Pair getNegPair(Pair p) {
		return new Pair(getRandomElement(p.typeLeft), getRandomElement(p.typeRight), p.typeLeft, p.typeRight, -1, null, null);
	}

	public static void main(String args[]) {
		Random rand = new Random();
		GenPairs gp = new GenPairs();
		ArrayList<Pair> pairsNonSym = new ArrayList<Pair>();
		ArrayList<Pair> pairs = new ArrayList<Pair>();
		int i=0;
		int bound = 100;

		//get nonsym pairs
		while(i < bound) {
			int startType = rand.nextInt(4);
			int s = gp.getRandomElement(startType);
			Pair p = gp.generatePairNonSym(startType, s);
			if(p != null && p.left != p.right) {
				pairsNonSym.add(p);
				i++;
			}
		}
		i=0;

		//get sym pairs
		while(i < bound) {
			int startType = rand.nextInt(4);
			int s = gp.getRandomElement(startType);
			Pair p = gp.generatePair(startType, s);
			if(p != null && p.left != p.right) {
				pairs.add(p);
				i++;
			}
		}
		System.out.println(pairsNonSym.size());
		//get nonsym neg pairs
		ArrayList<Pair> negPairsNonSym = new ArrayList<Pair>();
		for(i=0; i<pairsNonSym.size(); i++) {
			Pair np = gp.getNegPair(pairsNonSym.get(i));
			negPairsNonSym.add(np);
		}

		//get sym neg pairs
		ArrayList<Pair> negPairs = new ArrayList<Pair>();
		for(i=0; i<pairs.size(); i++) {
			Pair np = gp.getNegPair(pairs.get(i));
			negPairs.add(np);
		}

		try{
	    PrintWriter writer = new PrintWriter("positivePairsNonSym.csv", "UTF-8");
			for(i=0; i<pairsNonSym.size(); i++) {
				Pair temp = pairsNonSym.get(i);
				writer.println(temp.left + "," + temp.right + "," + temp.typeLeft + "," + temp.typeRight + "," + temp.sign);
			}
	    writer.close();

			writer = new PrintWriter("positivePairPathsNonSym.csv", "UTF-8");
			for(i=0; i<pairsNonSym.size(); i++) {
				Pair temp = pairsNonSym.get(i);
				writer.println(temp.indexPathToString() + " " + temp.typePathToString());
			}
			writer.close();

			writer = new PrintWriter("negativePairsNonSym.csv", "UTF-8");
			for(i=0; i<negPairsNonSym.size(); i++) {
				Pair temp = negPairsNonSym.get(i);
				writer.println(temp.left + "," + temp.right + "," + temp.typeLeft + "," + temp.typeRight + "," + temp.sign);
			}
			writer.close();

/////////////
			writer = new PrintWriter("positivePairs.csv", "UTF-8");
			for(i=0; i<pairs.size(); i++) {
				Pair temp = pairs.get(i);
				writer.println(temp.left + "," + temp.right + "," + temp.typeLeft + "," + temp.typeRight + "," + temp.sign);
			}
			writer.close();

			writer = new PrintWriter("positivePairPaths.csv", "UTF-8");
			for(i=0; i<pairs.size(); i++) {
				Pair temp = pairs.get(i);
				writer.println(temp.indexPathToString() + " " + temp.typePathToString());
			}
			writer.close();

			writer = new PrintWriter("negativePairs.csv", "UTF-8");
			for(i=0; i<negPairs.size(); i++) {
				Pair temp = negPairs.get(i);
				writer.println(temp.left + "," + temp.right + "," + temp.typeLeft + "," + temp.typeRight + "," + temp.sign);
			}
			writer.close();
		} catch (IOException e) {
		   // do something
		}
	}
}
