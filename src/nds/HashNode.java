package nds;

public class HashNode {
	
	
	public int data;
    public HashNode link;
    public long key;
    
    //Constructor
    public HashNode(int d, long k, HashNode n)
    {
    	data = d;
    	key = k;
        link = n;
    }

}
