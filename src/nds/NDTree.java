package nds;


public class NDTree {
	
	protected NDTree [] children;
	int data;
	
	public NDTree(int m)
	{
		children = new NDTree[m];	
	}
	
	public void setData(int data)
	{
		this.data = data;
	}
	
	public void delete()
	{
		for(int i=0;i<children.length;i++)
		{
			children[i] = null;
		}
	}
}
