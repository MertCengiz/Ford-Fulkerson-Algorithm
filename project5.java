import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class project5 {
	public static void main (String[] args) {
		try {
			File theFile = new File(args[0]); // The input file is taken as the first argument.
			Scanner theReader = new Scanner (theFile); // It will be read by the scanner.
			
			int whichLine = 1;// This is the line counter. Operations are done according to the line number.	
			int numberOfCities = 0; // This is the number of cities.
			HashMap<String, Node> allNodes = new HashMap<>(); // This enables to reach the node with its string code.
			String beginPoint = "B"; // This is the imaginary beginning point that all regions are connected to.
			String kingsLanding = "KL"; // This is the target that we are looking for.
			
			while (theReader.hasNextLine()) {
				String data = theReader.nextLine(); // This reveals that the input will be taken line by line.
				String[] splittedData = data.split("\\s+"); // Since the letters in the beginning and numbers between blanks
				// has its specific meaning, the data is splitted and each element between blanks are assigned to its variable.
			
				if (whichLine == 1) {  // In the first line, the total number of cities is given as a string.
					numberOfCities = Integer.parseInt(splittedData[0]); // It is converted to an integer.
					for (int i = 0; i < numberOfCities; i++) { // Then, city objects are created, then they are put to a hashmap so that 
						String point = "c" + Integer.toString(i); // reaching to the nodes with their string values will be easier.
						Node node = new Node (point); 
						allNodes.put(point, node);
					}
					Node kingsNode = new Node(kingsLanding); // The beginning and ending points have also have objects, they are created
					allNodes.put(kingsLanding, kingsNode); // and put into the hashmap.
					Node beginNode = new Node (beginPoint);
					allNodes.put(beginPoint, beginNode);
				}
				
				else if (whichLine == 2) { // In the second line, region objects are created and the integer values are said to be
					for (int i = 0; i < splittedData.length; i++) { // the capacity between the beginning node (B) and the regions.
						String point = "r" + Integer.toString(i); // They are set accordingly. The setting is done by putting the 
						Node node = new Node (point); // adjacent node and the capacity to the adjacency list of the beginning node.
						allNodes.put(point, node); 
						allNodes.get(beginPoint).getNeighbourNodes().put(node, Integer.parseInt(splittedData[i]));
					}
				}
				
				else if (whichLine >= 3) { // After the first second line, capacities except between the beginning node (B) and the regions are
					Node parentNode = allNodes.get(splittedData[0]); // set. The setting is done by putting the adjacent node and the capacity
					for (int i = 1; i < splittedData.length; i = i + 2) { // to the adjacency list of the parent node. 
						Node adjacentNode = allNodes.get(splittedData[i]);
						parentNode.getNeighbourNodes().put(adjacentNode, Integer.parseInt(splittedData[i+1]));
					}
				}
				
				whichLine++; // In every line, this counter is raised by one.
			}			
			theReader.close(); // After reading everything, the reader is closed.
			
			boolean isPossible = true; // This boolean shows whether it is possible to find a path to the target (King's Landing).
			while(isPossible) {	// To find every possible path, the algorithm is put inside of a while loop.
				// Here is the beginning of BFS.	
				HashSet<Node> visitedNodes = new HashSet<Node>(); // All visited nodes are put to a set to make checks.
				Queue<Node> nodeQueue = new LinkedList<Node>(); // In BFS, queues are used so a queue is created.
				Node beginNode = allNodes.get(beginPoint); // Beginning node ("B") is called and added to the queue o start
				nodeQueue.add(beginNode); // breadth-first-search. It is also added to the visited nodes' set since it is
				visitedNodes.add(beginNode); // visited at all.
				while (nodeQueue.size() > 0) { // BFS happens when queue is not empty.
					Node iterNode = nodeQueue.poll(); // The first element is taken from the queue.
					if (iterNode.getStringCode().equals(kingsLanding)) // If we have reached the target node, there is no necessity to
						break; // continue so that the while loop is broken.
					for (Entry<Node, Integer> neighbours: iterNode.getNeighbourNodes().entrySet()) { // Every adjacent node is searched, 
						Node neighbourNode = neighbours.getKey(); // they are added to the queue if not visited, then they are made 
						int remainingCapacity = neighbours.getValue(); // visited by adding them to the visited nodes' hashset.
						if (visitedNodes.contains(neighbourNode) == false && remainingCapacity > 0) {
							nodeQueue.add(neighbourNode);
							neighbourNode.setParentNode(iterNode);
							visitedNodes.add(neighbourNode);
						}
					}
				}
				// Here is the end of BFS.
				Node targetNode = allNodes.get(kingsLanding); // In order to differentiate whether the loop above is broken or ended due to the
				if (visitedNodes.contains(targetNode) == false) { // empty queue, visited nodes' set is checked. If there is no target node, 
					isPossible = false; // this means that finding a path to the target is not possible and this part of the loop ends by
					break; // breaking the outer loop.
				}
				// Finding the path.
				ArrayList<Node> myPath = new ArrayList<Node>(); // The created path will be put into this arraylist.
				while (targetNode.getStringCode().equals(beginPoint) == false) { // The path is found by iterating parents from the 
					myPath.add(0, targetNode); // target node until the parent becomes the beginning node ("B").
					targetNode = targetNode.getParentNode();
				}
				myPath.add(0, targetNode); // The beginning node is also added and path is completed now.
				// Finding the bottleneck. 
				int bottleneck = 2147483647; // Bottleneck is found by iterating the path and finding the smallest value.
				for (int i = 0; i < myPath.size() - 1; i++) {
					int way = myPath.get(i).getNeighbourNodes().get(myPath.get(i+1));
					if (way < bottleneck)
						bottleneck = way;
				}
				// Applying Ford-Fulkerson Method
				for (int i = 0; i < myPath.size() - 1; i++) { // It iterates over the path and reduces the capacities by the flow.
					int flow = myPath.get(i).getNeighbourNodes().get(myPath.get(i+1)) - bottleneck;	// Also, in the reversed way,
					myPath.get(i).getNeighbourNodes().put(myPath.get(i+1), flow); // residual edges are created by the value of
					if (myPath.get(i+1).getNeighbourNodes().get(myPath.get(i)) == null) // the bottleneck if it doesn't exist. 
						myPath.get(i+1).getNeighbourNodes().put(myPath.get(i), bottleneck); // If exists, the old flow is updated.
					else if (myPath.get(i+1).getNeighbourNodes().get(myPath.get(i)) != null) { 
						int oldBottleneck = myPath.get(i+1).getNeighbourNodes().get(myPath.get(i));
						oldBottleneck += bottleneck;
						myPath.get(i+1).getNeighbourNodes().put(myPath.get(i), oldBottleneck);						
					}
				}
			}
			
			int maxFlow = 0; // Max flow is found by adding the flow of residual edges starting from the target node.
			HashMap<Node, Integer> kingsMap = allNodes.get(kingsLanding).getNeighbourNodes();
			for (Entry<Node, Integer> nearCities: kingsMap.entrySet()) {
				int flow = nearCities.getValue();
				maxFlow += flow;
			}
						
			// Beginning of BFS to find min-cut.
			ArrayList<Edge> zeroEdges = new ArrayList<Edge>(); // In this BFS, zero edges will be stored to find min-cut after BFS.
			HashSet<Node> visitedNodes = new HashSet<Node>(); // Similarly, visited nodes are stored in a set.
			Queue<Node> nodeQueue = new LinkedList<Node>(); // Also, a queue is created.
			Node beginNode = allNodes.get(beginPoint); // Beginning node is get and added to the queue.
			nodeQueue.add(beginNode); // Then, it is set as visited.
			visitedNodes.add(beginNode); // Then, the loop starts.
			while (nodeQueue.size() > 0) { // This loop is very similar to the previous BFS loop.
				Node iterNode = nodeQueue.poll();
				if (iterNode.getStringCode().equals(kingsLanding))
					break;
				for (Entry<Node, Integer> neighbours: iterNode.getNeighbourNodes().entrySet()) {
					Node neighbourNode = neighbours.getKey();
					int remainingCapacity = neighbours.getValue();
					if (visitedNodes.contains(neighbourNode) == false && remainingCapacity > 0) {
						nodeQueue.add(neighbourNode);
						neighbourNode.setParentNode(iterNode);
						visitedNodes.add(neighbourNode);
					}
					if (remainingCapacity == 0) { // The only difference is if there is zero capacity remaining in an edge,
						Edge theEdge = new Edge (iterNode, neighbourNode); // that edge is added to the array list if it   
						boolean exists = false; // isn't already in the list. It is done by comparing every edge and checking 
						for (int i = 0; i < zeroEdges.size(); i++) { // if exists. If it doesn't exist, it is added to the array list.
							if (zeroEdges.get(i).compareTo(theEdge) == 0) {
								exists = true;
								break;
							}
						}
						if (exists == false)
							zeroEdges.add(theEdge);
					}
				}
			}
			// End of BFS to find min-cut.
			
			FileWriter theWriter = new FileWriter(args[1]); // Then, writer is taken as the second argument.
			theWriter.write(Integer.toString(maxFlow) + "\n");	// First, the max flow is written.		 
			for (int i = 0; i < zeroEdges.size(); i++) { // Then if the ending node of the zero edges are still zero, this mean that there is
				if (visitedNodes.contains(zeroEdges.get(i).getEndNode()) == false) { // no way to go and it is a min-cut. So they are written				
					if (zeroEdges.get(i).getBeginNode().getStringCode().equals(beginPoint) == false) // according to the fact that they are
						theWriter.write(zeroEdges.get(i).getBeginNode().getStringCode() + " " + zeroEdges.get(i).getEndNode().getStringCode() + "\n");
					else if (zeroEdges.get(i).getBeginNode().getStringCode().equals(beginPoint)) // passsing through the beginning node ("B"). In this
						theWriter.write(zeroEdges.get(i).getEndNode().getStringCode() + "\n"); // case, "B" is not written.
				}
			}						
			theWriter.close(); // After everything, the writer is closed.
		}
		catch (FileNotFoundException e){
			e.printStackTrace();  // This is called if there is a problem in reading. 
		}
		catch (IOException e) {
			e.printStackTrace(); // This is called if there is a problem in writing. 
		}
	}
}
