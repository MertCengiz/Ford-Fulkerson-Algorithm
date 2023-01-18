import java.util.*;

public class Node {
	private String stringCode;
	private Node parentNode;
	private HashMap<Node ,Integer> neighbourNodes = new HashMap<Node, Integer> ();
	
	public Node(String stringCode) {
		this.stringCode = stringCode;
	}

	public String getStringCode() {
		return stringCode;
	}

	public void setStringCode(String stringCode) {
		this.stringCode = stringCode;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	public HashMap<Node, Integer> getNeighbourNodes() {
		return neighbourNodes;
	}

	public void setNeighbourNodes(HashMap<Node, Integer> neighbourNodes) {
		this.neighbourNodes = neighbourNodes;
	}
	
	
}
