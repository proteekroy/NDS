

package nds;


public class LinkedList {
	

	public Node start;
	public Node end;
	public int size;
	
	public LinkedList()// Constructor
	{
		start = null;
		end = null;
		size = 0;
	}
	
	//  Function to insert an element at the beginning 
	public void addStart(int val)
	{
		if(start==null)
		{
			start = new Node(val, start);
			end = start;
		}
		else
		{
			start = new Node(val, start);
		}
		size++;
	}
	
	
	public void add(int val)
    {
    	if(start == null) 
        {
    		start = new Node(val, start);
            end = start;
        }
        else 
        {
            end.link = new Node(val, null);
            end = end.link;
        }
    	size++;
    }
	
	
	public int pop()
	{
		Node temp = start;
		start = start.link;
		temp.link = null;
		size--;
		return temp.data;
	}
	
	
	
	public void bringToFront(Node prev, Node current)
	{
//		if(current==end)
//		{
//			end = prev;
//		}
		prev.link = current.link;
		current.link = start;
		start = current;
	}
	
	public void moveOneStep(Node prev_prev, Node prev, Node current)
	{
//		if(current==end)
//		{
//			end = prev;
//		}
		prev.link = current.link;
		current.link = prev;
		if(prev_prev!=prev)
		{
			prev_prev.link = current;
		}
	}
	
	public void delete()
	{
		start = null;
		end = null;
		size = 0;
	}
	
	public Node delete(Node prev, Node current)//delete this node and return next node
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
