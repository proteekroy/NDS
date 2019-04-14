package nds;


public class H_BOS extends AbstractNDS 
{

	int m1 = -1;
	int [][]allrank;
	boolean []set;
	LinkedList [][]list;
	int index[];//two dimension
	int []lex_order;
	public int[] comparison_index_count;
	int []temp;
	public SplayTree tree;
	
	public H_BOS(int arg_n, int arg_m)
	{
		n = arg_n;
		m = arg_m;
		//m1 = (int) Math.min(m, Math.max(1, Math.ceil(Math.log10(n)/Math.log10(2))));
		m1 = m;
		helper = new int[n];
		rank = new int[n];
		allrank = new int[m1][n];
		lex_order = new int[n];
		objseq = new int[n][m];
		mergesort = new MergeSort();
		heapsort = new HeapSort();
		tree = new SplayTree();
		set = new boolean[n];
		list = new LinkedList[m1][n];
		temp = new int[n];

		/*for(int j=0;j<m1;j++)
		{
			for(int i=0;i<n;i++)
			{
				list[j][i] = new LinkedList();
			}
		}*/
	}
	
	@Override
	public void sort(double [][] population) 
	{
		initialize(population);	
		start = System.nanoTime();//starting timer
		heap_bos();
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
			System.out.println("\nStarting H-BOS");
		}
		population = pop;		
		totalfront = 0;
		for(int j=0;j<m1;j++)
		{
			for(int i=0;i<n;i++)
			{
				allrank[j][i] = i;
				if(list[j][i]!=null)
				{
					list[j][i].delete();
				}
			}
		}
		tree.delete();
		tree.insert(new Integer(0));
		comparison = 0;
		comparison_sort = 0;
		comparison_rank = 0;
		time = 0;
		
		for(int i=0;i<n;i++)
		{
			set[i] = false;
			temp[i] = 0;
			rank[i] = -1;
		}
		mergesort.setValues(helper, lex_order, population, allrank, this);
		heapsort.setAllrankObj(allrank, 0, this);
	}
	
	
	private void heap_bos()
	{
		int i, j, total=0, s, obj;
				
		mergesort.setObj(0);
		mergesort.mergesort(0, n-1);//sorts first objective with lexicographic order
		
		for(i=0;i<n;i++)
		{
			lex_order[allrank[0][i]] = i;  
		}
		
		buildheapAllMinHeap(m1);
		
		for(i=0;i<n;i++)
		{
			for(j=0;j<m;j++)
			{
				if(j<m1)
				{
					s = allrank[j][i];
					objseq[s][m1-1-temp[s]] = j;
					temp[s]++;
				}
				else
				{
					objseq[i][j] = j;
				}
			}
		}
		endTime2 = System.nanoTime();
		
		for(j=0;j<m1;j++)
		{
			list[j][0] = new LinkedList();
		}
		
		for(i=0;i<n;i++)//n
		{
			for(obj=0;obj<m1;obj++)
			{
				if(obj>0)//first objective is already sorted, so no need for heap
				{
					s = allrank[obj][0];//Take the smallest element
					allrank[obj][0] = allrank[obj][n-i-1];
					heapsort.setObj(obj);
					heapsort.siftdown_minheap(0, n-i-1);
				}
				else
				{
					s = allrank[0][i];
				}
					
				if(set[s])//s is already ranked
				{
					/*if(list[obj][rank[s]]==null)
					{
						list[obj][rank[s]] = new LinkedList();	
					}*/
					list[obj][rank[s]].addStart(s);
					continue;
				}
				set[s] = true;
				total++;
				find_rank_splay(s, obj);
			}
			
			if(total==n)
			{
				break;
			}
		}
		totalfront++;
	}

	public void find_rank_splay(int s, int obj)
	{
		int i, k;
		Node head, prev;
		boolean dominated;
		
		BinaryNode binNode = tree.getRoot();

		while(true)
		{
			k = (int) binNode.key;
			dominated = false;

			head = list[obj][k].start;
			prev = head;
			while(head!=null)
			{
				if(dominates_objseq(head.data,s))
				{
					dominated = true;
					if(prev!=head)
					{
						list[obj][k].bringToFront(prev, head);
					}
					break;
				}
				prev = head;
				head = head.link;
			}
			
			if(!dominated)//if not dominated
			{
				if(binNode.left==null)//leftmost node of a sub-tree
				{
					list[obj][k].add(s);
					rank[s] = k;
					tree.find(new Integer(Math.max(0, k-1)));
					break;
				}
				else
				{
					binNode = binNode.left;
				}
			}
			else//if dominated
			{
				if(binNode.right!=null)
				{
					binNode = binNode.right;
				}
				else if(k<totalfront)//this is a rightmost node of a sub-tree
				{	
					k = k+1;
					rank[s] = k;		
					/*if(list[obj][k]==null)
					{
						list[obj][k] = new LinkedList();
					}*/
						
					list[obj][rank[s]].add(s);
					tree.find(new Integer(k));
					break;
				}
				else //if(k==BestOrderSort.totalfront)//rightmost node of whole tree
				{
					totalfront++;
					for(i=0;i<m1;i++)
					{
						//if(list[i][totalfront]==null)
						{
							list[i][totalfront] = new LinkedList();
						}
					}
					
					list[obj][totalfront].add(s);
					rank[s]=  totalfront;
					tree.insert(new Integer(rank[s]));//new front will be added in splay tree
					break;
				}
				
			}//if
			

		}//while(true)
	}
	
	@Override
	public boolean dominates_objseq(int p1, int p2) 
	{
		boolean equal = true;
				
		for(int i=0;i<m;i++)
		{
			comparison_rank++;
			if(population[p1][objseq[p1][i]] > population[p2][objseq[p1][i]])
			{
				return false;
			}
			else if(population[p1][objseq[p1][i]] < population[p2][objseq[p1][i]])
			{
				equal = false;
			}
		}
	
		if(equal)//both solutions are equal
			return false;
		else //dominates
			return true;
	
	}
	
	
	
	
}
