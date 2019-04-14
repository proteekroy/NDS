package nds;

import java.util.Random;

public class T_ENS extends AbstractNDS {
	
	
	public int[][] allrank;
	public LinkedList P;
	public boolean found, dominated, skip;
	public NDTree [] F;
	Random rand;
	
	public T_ENS(int arg_n, int arg_m)
	{
		n = arg_n;
		m = arg_m;
		rank = new int[n];
		comparison = 0;
		time = 0;
		P = new LinkedList();
		allrank = new int[1][n];
		helper = new int[n];
		mergesort = new MergeSort();
		F = new NDTree[n];
		rand = new Random();
		objseq = new int[n][m-1];
		
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<m-1;j++)
			{
				objseq[i][j] = j+1;
				
			}
			shuffleArray(objseq[i]);
		}
	}
	
	@Override
	public void sort(double [][] population) 
	{
		initialize(population);	
		start = System.nanoTime();//starting timer
		t_ens();
		end = System.nanoTime();
		mini_stat();
		if(printinfo)
		{
			printInformation();
		}
	}
	
	@Override
	public void initialize(double[][] pop)
	{
		if(printinfo)
		{
			System.out.println("\nStarting T-ENS Sort");
		}
		population = pop;
		totalfront = 0;
		comparison = 0;
		comparison_sort = 0;
		comparison_rank = 0;
		time = 0;
		
		for(int i=0;i<n;i++)
		{
			allrank[0][i] = i;
			F[i] = null;
			rank[i] = -1;
		}
		if(P!=null)
		{
			P.delete();
		}
		mergesort.setValues(helper, null, population, allrank, this);
	}
	
	private void t_ens()
	{
		int i, k;
		mergesort.setObj(0);
		mergesort.mergesort(0, n-1);//sorts first objective with lexicographic order
		
		for(i=0;i<n;i++)
		{
			P.add(allrank[0][i]);
		}
		endTime2 = System.nanoTime();
		
		Node head, prev;
		
		k = 0;
		
		while(P.size>0) //Population not empty
		{
			head = P.start;
			prev = null;
			while(head!=null)
			{
				found = false;
				skip = false;
				F[k] = update_tree(head.data, F[k], objseq);
				
				if(found || skip)
				{
					rank[head.data] = k;
					head = P.delete(prev, head);
				}
				else
				{
					prev = head;
					head = head.link;
				}
			}
			if(moeaflag)
			{
				if(P.size< (n/2.0))
				{
					break;
				}
			}
			k = k+1;
			totalfront = k;
			
			
		}
	}
	
	
	public NDTree update_tree(int p, NDTree T, int[][] objseq) 
	{
		
		if(T==null)
		{		
			T = new NDTree(objseq[0].length);
			T.setData(p);//p is used as the root of the tree
			found = true;
			return T;//means it is deleted from P
		}
		else
		{
			dominated = false;
			
			NDTree tempTree = check_tree(p, T, objseq, true);
			if(tempTree==null)
			{	return T; }
			else
			{	return tempTree; }
		}
	}
	
	
	private NDTree check_tree(int p, NDTree T, int[][] objseq, boolean add_pos) 
	{
		if(T==null)
		{
			return null;//this is needed as the base case of recursive call
		}
		
		int i, m=-1;
		boolean less = false;
		
		for(i=0;i<objseq[0].length;i++)
		{
			if(population[p][objseq[T.data][i]]<population[T.data][objseq[T.data][i]])//compare with root
			{
				less = true;
				m = i; //save index
				comparison_rank++;
				break;
			}
			else if(isEqual(p, T.data))
			{
				rank[p] = rank[T.data];
				skip = true;
				return null;
			}
		}
		
		if(less==false)
		{
			dominated = true;
			return null;
		}
		else
		{
			for(i=0;i<=m;i++)
			{
				check_tree(p, T.children[i], objseq, (i==m)&&add_pos);
				if(dominated || skip)
				{
					return null;//p is dominated by a solution in the branch of the tree
				}
			}
			
			if(T.children[m]==null && add_pos)
			{
				T.children[m] = new NDTree(objseq[0].length);
				T.children[m].setData(p);
				found = true;
			}
			
			return T;
		}
	}
	
	public void shuffleArray(int[] ar)
	{
		int index, a;
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	    	index = rand.nextInt(i + 1);
	    	a = ar[index];
	    	ar[index] = ar[i];
	    	ar[i] = a;
	    }
	}
}
