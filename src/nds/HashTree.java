package nds;


public class HashTree {
	
	protected HashNode start;
	protected HashNode end;
	protected int size;
	
	public HashTree()// Constructor
	{
		start = null;
		end = null;
		size = 0;
	}
	
	//insert an element in the beginning 
	public void addStart(int val, long key)
	{
		if(start==null)
		{
			start = new HashNode(val, key, start);
			end = start;
		}
		else
		{
			start = new HashNode(val, key, start);
		}
		size++;
	}
	
	//insert an element in the end
	public void add(int val, long key)
    {
    	if(start == null) 
        {
    		start = new HashNode(val, key, start);
            end = start;
        }
        else 
        {
            end.link = new HashNode(val, key, null);
            end = end.link;
        }
    	size++;
    }
	
	//insert an element in the beginning 
	public void addStart(HashNode node)
	{
		if(start==null)
		{
			start = node;
			end = start;
		}
		else
		{
			node.link = start;
			start = node;
		}
		size++;
	}
	//insert an element in the end
	public void add(HashNode node)
    {
    	if(start == null) 
        {
    		start = node;
            end = start;
        }
        else 
        {
            end.link = node;
            end = end.link;
        }
    	size++;
    }
	
	public void add_middle(int val, long key, HashNode current)//add node after current node
    {
		if(current!=null)
		{
			HashNode temp = new HashNode(val, key, current.link);
			current.link = temp;
		}
		else
		{
			addStart(val, key);
		}
		size++;
    }
	
	public void bringToFront(HashNode prev, HashNode current)
	{
		if(current==end)
		{
			end = prev;
		}
		prev.link = current.link;
		current.link = start;
		start = current;
	}
	
	public void moveOneStep(HashNode prev_prev, HashNode prev, HashNode current)
	{
		if(current==end)
		{
			end = prev;
		}
		prev.link = current.link;
		current.link = prev;
		prev_prev.link = current;
	}
	
	public void delete()
	{
		start = null;
		end = null;
		size = 0;
	}
	
	public HashNode delete(HashNode prev, HashNode current)//delete this node and return next node
    {
		if(prev == null)
		{
			if(start==end)//only one element
			{
				start = current.link;
				end = start;//start and end both are changed
			}
			else
			{
				start = current.link;//only start is changed
			}
			
			current.link = null;
			size--;
			return start;
		}
		else
		{
			if(current==end)//last element
			{
				end = prev;
			}
			prev.link = current.link;
			current.link = null;
			size--;
			return prev.link;
		}
    }
	
}
