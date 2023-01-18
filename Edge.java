
public class Edge implements Comparable<Edge>{
	private final Node beginNode;
	private final Node endNode;
	
	public Edge (Node beginNode, Node endNode) {
		this.beginNode = beginNode;
		this.endNode = endNode;
	}

	public Node getBeginNode() {
		return beginNode;
	}

	public Node getEndNode() {
		return endNode;
	}

	@Override
	public int compareTo(Edge o) {
		// TODO Auto-generated method stub
		if (beginNode.getStringCode().equals(o.getBeginNode().getStringCode()) && endNode.getStringCode().equals(o.getEndNode().getStringCode()))
			return 0;
		return -1;
	}
	
	
}
