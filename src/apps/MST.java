package apps;

import structures.*;
import java.util.ArrayList;

public class MST {

	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
		PartialTreeList treeList = new PartialTreeList();                                       
		for (int i = 0; i < graph.vertices.length; i++) {
			MinHeap<PartialTree.Arc> singleVertexHeap = new MinHeap<PartialTree.Arc>();
			String currentVertex = graph.vertices[i].name;
			for (Vertex.Neighbor nbr = graph.vertices[i].neighbors; nbr != null; nbr = nbr.next) {
				PartialTree.Arc temp = new PartialTree.Arc(graph.vertices[i], nbr.vertex, nbr.weight);
				if(currentVertex.equals(graph.vertices[i].name) || currentVertex.equals(nbr.vertex.name)){
					singleVertexHeap.insert(temp);
				}
			}
			PartialTree singleVertex = new PartialTree(singleVertexHeap.getMin().v1);
			while(!singleVertexHeap.isEmpty()){
				singleVertex.getArcs().insert(singleVertexHeap.deleteMin());
			}
			treeList.append(singleVertex);
		}
		return treeList;
	}

	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * 
	 * @param graph Graph for which MST is to be found
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<PartialTree.Arc> execute(Graph graph, PartialTreeList ptlist) {
		ArrayList<PartialTree.Arc> returnList = new ArrayList<PartialTree.Arc>(); 
		while(ptlist.size() > 1){
			PartialTree currentTree = ptlist.remove();
			Vertex search = currentTree.getArcs().getMin().v2;
			PartialTree connectTree = ptlist.removeTreeContaining(search);
			PartialTreeList newOutput = combineVertices(currentTree, connectTree);
			returnList.add(newOutput.remove().getArcs().deleteMin());
			currentTree = newOutput.remove();
			ptlist.append(currentTree);	
		}
		return returnList;
	}

	/**
	 * Combines the two trees with the common vertex and returns a PartialTreeList
	 * that contains two items. First is the arc that is used for the MST and the
	 * second is the list of the combined vertices
	 * @param currentTree tree for the first PartialTree
	 * @param connectTree tree for the second PartialTree
	 * @return
	 */
	private static PartialTreeList combineVertices(PartialTree currentTree, PartialTree connectTree){
		MinHeap<PartialTree.Arc> currentArcs = currentTree.getArcs();
		MinHeap<PartialTree.Arc> connectArcs = connectTree.getArcs();
		MinHeap<PartialTree.Arc> combineArcs = new MinHeap<PartialTree.Arc>();
		Vertex root = currentTree.getRoot();
		Vertex parent = connectTree.getRoot();
		PartialTreeList returnList = new PartialTreeList();
		PartialTree tempTree1 = new PartialTree(root);
		tempTree1.getRoot().parent = parent;
		PartialTree.Arc returnArc = null;
		while(!currentArcs.isEmpty() && !connectArcs.isEmpty()){
			if(currentArcs.getMin().equals(connectArcs.getMin())&& (returnArc == null)){
				returnArc = currentArcs.deleteMin();
				connectArcs.deleteMin();
			}else if(currentArcs.getMin().weight > connectArcs.getMin().weight){
				combineArcs.insert(connectArcs.deleteMin());
			}else if(currentArcs.getMin().weight < connectArcs.getMin().weight){
				combineArcs.insert(currentArcs.deleteMin());
			}else{
				combineArcs.insert(connectArcs.deleteMin());
			}
		}
		while(!currentArcs.isEmpty() || !connectArcs.isEmpty()){
			if(currentArcs.isEmpty()){
				combineArcs.insert(connectArcs.deleteMin());
			}else{
				combineArcs.insert(currentArcs.deleteMin());
			}
		}
		tempTree1.getArcs().insert(returnArc);
		returnList.append(tempTree1);
		tempTree1 = new PartialTree(root);
		while(!combineArcs.isEmpty()){
			tempTree1.getArcs().insert(combineArcs.deleteMin());
		}
		returnList.append(tempTree1);
		return returnList;
	}
}
