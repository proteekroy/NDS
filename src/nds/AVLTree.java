package nds;

public class AVLTree {
 
    public AVLTreeNode root;
    public boolean dominated = false;
	public int current_solution = -1;
	private AbstractNDS nds;
	public int size = 0;
	
    public AVLTree(AbstractNDS arg_nds)
    {
    	nds = arg_nds;
    }
 
    public void insert(double key, int val) {
        if (root == null)
            root = new AVLTreeNode(key, val, null);
        else {
        	AVLTreeNode n = root;
        	AVLTreeNode parent;
            while (true) {
                //if (n.key == key)
                //    return false;
 
                parent = n;
 
                boolean goLeft =  key <= n.key ;
                n = goLeft ? n.left : n.right;
 
                if (n == null) {
                    if (goLeft) {
                        parent.left = new AVLTreeNode(key, val, parent);
                    } else {
                        parent.right = new AVLTreeNode(key, val, parent);
                    }
                    rebalance(parent);
                    break;
                }
            }
        }
        size++;
        
        return;
    }
 
    public void delete(double delKey) {
        if (root == null)
            return;
        AVLTreeNode n = root;
        AVLTreeNode parent = root;
        AVLTreeNode delNode = null;
        AVLTreeNode child = root;
 
        while (child != null) {
            parent = n;
            n = child;
            child = delKey >= n.key ? n.right : n.left;
            if (delKey == n.key)
                delNode = n;
        }
 
        if (delNode != null) {
            delNode.key = n.key;
 
            child = n.left != null ? n.left : n.right;
 
            if (root.key == delKey) {
                root = child;
            } else {
                if (parent.left == n) {
                    parent.left = child;
                } else {
                    parent.right = child;
                }
                rebalance(parent);
            }
        }
        size--;
        
        return;
    }
 
    private void rebalance(AVLTreeNode n) {
        setBalance(n);
 
        if (n.balance == -2) {
            if (height(n.left.left) >= height(n.left.right))
                n = rotateRight(n);
            else
                n = rotateLeftThenRight(n);
 
        } else if (n.balance == 2) {
            if (height(n.right.right) >= height(n.right.left))
                n = rotateLeft(n);
            else
                n = rotateRightThenLeft(n);
        }
 
        if (n.parent != null) {
            rebalance(n.parent);
        } else {
            root = n;
        }
    }
 
    private AVLTreeNode rotateLeft(AVLTreeNode a) 
    {
        AVLTreeNode b = a.right;
        b.parent = a.parent;
 
        a.right = b.left;
 
        if (a.right != null)
            a.right.parent = a;
 
        b.left = a;
        a.parent = b;
 
        if (b.parent != null) {
            if (b.parent.right == a) {
                b.parent.right = b;
            } else {
                b.parent.left = b;
            }
        }
 
        setBalance(a, b);
 
        return b;
    }
 
    private AVLTreeNode rotateRight(AVLTreeNode a) {
 
        AVLTreeNode b = a.left;
        b.parent = a.parent;
 
        a.left = b.right;
 
        if (a.left != null)
            a.left.parent = a;
 
        b.right = a;
        a.parent = b;
 
        if (b.parent != null) {
            if (b.parent.right == a) {
                b.parent.right = b;
            } else {
                b.parent.left = b;
            }
        }
 
        setBalance(a, b);
 
        return b;
    }
 
    private AVLTreeNode rotateLeftThenRight(AVLTreeNode n) {
        n.left = rotateLeft(n.left);
        return rotateRight(n);
    }
 
    private AVLTreeNode rotateRightThenLeft(AVLTreeNode n) {
        n.right = rotateRight(n.right);
        return rotateLeft(n);
    }
 
    private int height(AVLTreeNode n) {
        if (n == null)
            return -1;
        return 1 + Math.max(height(n.left), height(n.right));
    }
 
    private void setBalance(AVLTreeNode... nodes) {
        for (AVLTreeNode n : nodes)
            n.balance = height(n.right) - height(n.left);
    }
 
    public void printBalance() {
        printBalance(root);
    }
 
    private void printBalance(AVLTreeNode n) {
        if (n != null) {
            printBalance(n.left);
            System.out.printf("%s ", n.balance);
            printBalance(n.right);
        }
    }
    
    //return the node which is equal to the key, if exists, otherwise return null
    public AVLTreeNode search(AVLTreeNode node, double key) 
	{
    	if(node==null)
    	{
    		return null;
    	}
    	else if (node.key == key)
	    {
	    	return node;//(new AVLNode(key, val));	
	    }
    	else if (key <= node.key) // strictly better, so no comparison is needed
	    {
	    	return search(node.left, key);
	    }
	    else //if (key >= AVLTreeNode.key)
	    {
	    	return search(node.right, key);
	    }
	}
	

	public void compare(AVLTreeNode AVLTreeNode) 
	{
		if (AVLTreeNode == null)//no points to compare
			return;
		 
		if(AVLTreeNode.flag==0)
		{
			if(!dominated)
			{
				compare(AVLTreeNode.left);
			}
		}
		else
		{
			//go to right first
			if(AVLTreeNode.right!=null)
	  		{
	  			AVLTreeNode.right.flag = 1;
	  		}
			
			compare(AVLTreeNode.right);
		  	//after coming back, check dominance
 
		  	if(dominated)
		  	{
		  		return;
		  	}
		  	else
		  	{
		  		dominated = nds.dominates_objseq(AVLTreeNode.value, current_solution);
		  		if(!dominated)
				{
		  			if(AVLTreeNode.left!=null)
		  			{
		  				AVLTreeNode.left.flag = 1;
		  			}
		  			compare(AVLTreeNode.left);
				}
		  		else
		  		{
		  			return;
		  		}
		  	 }
		 }
		 return;
	}
	
	

	/*
    public static void main(String[] args) {
        AVLtree tree = new AVLtree();
 
        System.out.println("Inserting values 1 to 10");
        for (int i = 1; i < 10; i++)
            tree.insert(i, i);
 
        System.out.print("Printing balance: ");
        tree.printBalance();
    }*/
}