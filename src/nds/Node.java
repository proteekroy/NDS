package nds;

public class Node
{
	public int data;
    public Node link;
        
    //  Constructor
    public Node(int d, Node n)
    {
    	data = d;
        link = n;
    }
}