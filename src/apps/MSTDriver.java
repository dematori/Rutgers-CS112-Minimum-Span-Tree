package apps;

import java.io.IOException;
import java.util.*;

import structures.*;

public class MSTDriver {
	public static void main(String[] args) throws IOException{
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter name of graph file : ");
		String fileName = sc.next();
		Graph graph = new Graph(fileName);
		PartialTreeList treeList = MST.initialize(graph);
		ArrayList<PartialTree.Arc> arcArray = MST.execute(graph, treeList);
		System.out.println("---------------------------------------------------");
		System.out.println("OUTPUT : " + arcArray);
	}
}
