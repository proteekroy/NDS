package nds;



public class AVLTreeNode {
	
	public double key;
    public int value;
    public int balance;
    public int flag;
    public AVLTreeNode left, right, parent;

    AVLTreeNode(double k, int v, AVLTreeNode p) 
    {
        key = k;
        parent = p;
        value = v;
        flag = 0;
    }

}
