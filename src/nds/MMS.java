package nds;

import java.util.Random;


public class MMS extends AbstractNDS{
	
	int []lex_order;
	int [][]allrank;
	int [][]allrankCopy;
	boolean objseq_set [][];
	int count[];
	boolean []set;
	int []temp;
	LinkedList [][]list;
	LinkedList []F;
	QuickSort quicksort;
	public int[][] objpos;
	
	boolean found, dominated, skip;
	NDTree last_nondominated_element;
	int nondominated_obj_index;
	public NDTree [][] NDT_specific;
	int initialFront, lastfront;
	public SplayTree tree;
	
	public HashTree [] hashtree;
	public int [][] hashtreeHead;
	LinkedList []temp_list2;
	boolean addnode;
	
	boolean self_adjusing_list_points_flag = true;
	boolean self_adjusing_list_obj_flag = true;
	boolean ndtree_flag = true;
	
	
	
	public MMS(int arg_n, int arg_m)
	{
		n = arg_n;
		m = arg_m;
		//m1 = (int) Math.min(m, Math.max(1, Math.ceil(Math.log10(n)/Math.log10(2))));
		//m1 =  Math.max(2, Math.min(m, (int)Math.sqrt(n)));
		m1 = m;
		//m1 = m;
		helper = new int[n];
		rank = new int[n];
		count = new int[n];

		lex_order = new int[n];
		objseq = new int[n][m];
		treeobjseq = new int[n][m];
		objpos = new int[n][m];
		objseq_set = new boolean[n][m]; 
		set = new boolean[n];
		list = new LinkedList[m1][n];
		temp = new int[n];
		F = new LinkedList[n];
		tree = new SplayTree();
		rand = new Random();
		
		hashtree = new HashTree[n];
		hashtreeHead = new int[n][m];
		temp_list2 = new LinkedList[m1];
	}
	
	@Override
	public void sort(double [][] population) 
	{
		initialize(population);	
		start = System.nanoTime();//starting timer
		mms();
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
			System.out.println("\nStarting MMS Sort");
		}
		//m1 = Math.min(m, m1);
		
		allrank = new int[m1][n];
		allrankCopy = new int[m1][n];
		NDT_specific = new NDTree[m1][n];
		tree.delete();
		tree.insert(new Integer(0));		
		
		population = pop;
		totalfront = 0;
		comparison = 0;
		comparison_sort = 0;
		comparison_rank = 0;
		initialFront = 0;
		time = 0;
		lastfront = n;
		
		int i, j;
		
		for(j=0;j<m1;j++)
		{
			for(i=0;i<n;i++)
			{
				allrank[j][i] = i;
				
				if(list[j][i]!=null)
				{
					list[j][i].delete();
				}
				objseq_set[i][j] = false;
				treeobjseq[i][j] = j;
				objseq[i][j] = j;
				objpos[i][j] = j;
			}
			temp_list2[j] = new LinkedList();
		}
		for(j=m1;j<m;j++)
		{
			for(i=0;i<n;i++)
			{
				objseq_set[i][j] = false;
				treeobjseq[i][j] = j;
				objseq[i][j] = j;
				objpos[i][j] = j;
			}
		}
		
		for(i=0;i<n;i++)
		{
			set[i] = false;
			rank[i] = -1;
			count[i] = 0;
			hashtree[i] = new HashTree();
		}
		
		quicksort = new QuickSort();
		mergesort = new MergeSort();
		mergesort.setValues(helper, lex_order, population, allrank, this);
		if(heapflag)
		{
			heapsort = new HeapSort();
			heapsort.setAllrankObj(allrank, 0, this);
		}
	}
	
	
	public void mms()
	{
		int i = 0, j, s, total = 0, obj, save_index=-1, save_val;
		
		mergesort.setObj(0);
		mergesort.mergesort(0, n-1);//sorts first objective with lexicographic order
		//quicksort.set(population, allrank[0], this);
		//quicksort.sort(0, 0, n-1);
		
		for(i=0;i<n;i++)
		{
			lex_order[allrank[0][i]] = i;  
		}
			
		if(!heapflag)
		{
			mergesort.setlexorder(lex_order);
			sortingValues();//sorting population according to all objectives
		}
		else
		{
			heapsort.setlexorder(lex_order);
			//buildheapAllMaxHeap(m1);
			buildheapAllMinHeap(m1);
		}
		
		endTime2 = System.nanoTime();
		System.out.println("Time for constructiheap = "+ (endTime2 - start)*1.0/1000000.0 +" ms");
		
		int []temp = new int[n];//initially zero
		total = 0;

		if(heapflag)
		{
			
			long start3 = System.nanoTime();			
			
			for(i=0;i<n;i++)
			{
				allrankCopy[0][i] = allrank[0][i];
				objseq[allrank[0][i]][m-1-temp[allrank[0][i]]] = 0;
				treeobjseq[allrank[0][i]][m-1-temp[allrank[0][i]]] = 0;
				temp[allrank[0][i]]++;
				objseq_set[allrank[0][i]][0] = true;
				
				if(!set[allrank[0][i]])
				{
					set[allrank[0][i]] = true;
					total++;
				}
				
				for(j=1;j<m1;j++)
				{
					allrankCopy[j][i] = allrank[j][0];//Take the smallest element	
					allrank[j][0] = allrank[j][n-i-1];
					heapsort.setObj(j);
					heapsort.siftdown_minheap(0, n-i-2);
					objseq[allrankCopy[j][i]][m-1-temp[allrankCopy[j][i]]] = j;
					treeobjseq[allrankCopy[j][i]][m-1-temp[allrankCopy[j][i]]] = j;
					temp[allrankCopy[j][i]]++;
					objseq_set[allrankCopy[j][i]][j] = true;
					
					if(!set[allrankCopy[j][i]])
					{
						set[allrankCopy[j][i]] = true;
						total++;
					}
				}//for(j = 0;j<m1;j++)
				if(total>=n)
				{
					save_index = i+1;
					break;
				}
			}
			
			
			long end3 = System.nanoTime();
			time = (end3 - start3)*1.0/1000000.0;
			System.out.println("Time to scan = "+ time+" ms");
			System.out.println("Save index = "+ save_index);
			
			
			//random assignment of objectives
			for(i=0;i<n;i++)
			{
				for(j=0;j<m;j++)
				{	
					if(objseq_set[i][j] == false)
					{
						objseq[i][count[i]] = j;
						treeobjseq[i][count[i]] = j;
						count[i]++;
					}
				}
			}
			
			
			for(i=0;i<n;i++)
			{
				set[i] = false;
			}
			total = 0;
			totalfront = 0;
			
			for(j=0;j<m1;j++)
			{
				list[j][0] = new LinkedList();
			}
			
			for(i=0;i<save_index;i++)//n
			{
				for(obj=0;obj<m1;obj++)
				{
					s = allrankCopy[obj][i];
					//s = allrank[obj][i];
					if(set[s])//s is already ranked
					{
						if(list[obj][rank[s]]==null)
						{
							list[obj][rank[s]] = new LinkedList();	
						}
						list[obj][rank[s]].addStart(s);
						continue;
					}
					set[s] = true;
					find_rank_binary(s, obj);
				}
			}
			totalfront++;
			
			/*
			for(i=0;i<n;i++)//n
			{
				for(obj=0;obj<m1;obj++)
				{
					s = allrank[obj][0];
					
					if(obj>0)//first objective is already sorted, so no need for heap
					{
						allrank[obj][0] = allrank[obj][n-i-1];
						//allrank[obj][n-i-1] = s;//all rank is in reverse order
						//allrank[obj][0] = save_val;
						heapsort.setObj(obj);
						heapsort.siftdown_minheap(0, n-i-2);
						
					}
					
					
//					save_val = objseq[s][temp[s]];
//					objseq[s][temp[s]] = obj;
//					objseq[s][objpos[s][obj]] = save_val;
//					objpos[s][save_val] = objpos[s][obj];
										
					if(set[s])//s is already ranked
					{
						if(list[obj][rank[s]]==null)
						{
							list[obj][rank[s]] = new LinkedList();	
						}
						list[obj][rank[s]].addStart(s);
						continue;
					}
					total++;
					set[s] = true;
					find_rank_binary(s, obj);
				}
				if(total>=n)
				{
					break;
				}
			}
			totalfront++;
			
			*/
			
		}
		else
		{
			long start3 = System.nanoTime();
			for(i=0;i<n;i++)
			{
				for(j=0;j<m1;j++)
				{
					//if(j<m1)
					{
						s = allrank[j][i];
						allrankCopy[j][i] = s;
						objseq[s][m-1-temp[s]] = j;
						treeobjseq[s][m-1-temp[s]] = j;
						temp[s]++;
						objseq_set[s][j] = true;
						if(!set[s])
						{
							set[s] = true;
							total++;
							if(total==n)
							{
								save_index = i+1;
							}
						}
					}
				}
				if(total==n)
				{
					break;
				}
			}
			long end3 = System.nanoTime();
			time = (end3 - start3)*1.0/1000000.0;
			System.out.println("Time to scan in sort = "+ time+" ms");
			//random assignment of objectives
			for(i=0;i<n;i++)
			{
				for(j=0;j<m;j++)
				{	
					if(objseq_set[i][j] == false)
					{
						objseq[i][count[i]] = j;
						treeobjseq[i][count[i]] = j;
						count[i]++;
					}
				}
			}
			
			for(i=0;i<n;i++)
			{
				set[i] = false;
			}
			total = 0;
			totalfront = 0;
			
			for(j=0;j<m1;j++)
			{
				list[j][0] = new LinkedList();
			}
			
			for(i=0;i<save_index;i++)//n
			{
				for(obj=0;obj<m1;obj++)
				{
					s = allrankCopy[obj][i];
					if(set[s])//s is already ranked
					{
						if(list[obj][rank[s]]==null)
						{
							list[obj][rank[s]] = new LinkedList();	
						}
						list[obj][rank[s]].addStart(s);
						continue;
					}
					set[s] = true;
					find_rank_binary(s, obj);
				}
			}
			totalfront++;

		}
		
		/*
		for(i=0;i<n;i++)
		{
			set[i] = false;
		}
		//endTime2 = System.nanoTime();
			
		total = 0;
		totalfront = 0;
		
		for(j=0;j<m1;j++)
		{
			list[j][0] = new LinkedList();
		}
		
		for(i=0;i<save_index;i++)//n
		//for(i=0;i<n;i++)//n
		{
			for(obj=0;obj<m1;obj++)
			{
				//s = allrank[obj][i];
				s = allrankCopy[obj][i];
//				if(s==6||s==2)
//				{
//					System.out.println("s = "+s);
//				}
				if(set[s])//s is already ranked
				{
					if(list[obj][rank[s]]==null)
					{
						list[obj][rank[s]] = new LinkedList();	
					}
					list[obj][rank[s]].addStart(s);
					continue;
				}
				set[s] = true;
				//total++;
				//find_rank_basic(s, obj);
//				if(rank_search_option==1)
//				{
//					find_rank_splay(s, obj);
//				}
//				else if(rank_search_option==2)
//				{
//					find_rank_sequential(s, obj);
//				}
//				else if(rank_search_option==3)
//				{
					find_rank_binary(s, obj);
//				}
//				else
//				{
//					find_rank_splay_hash(s, obj);
//				}
			    //if(total==n)
			    //{
				//	break;
			    //}
			}
			//if(total==n)
			//{
			//	break;
			//}
		}
		//System.out.println("i = "+i);
		
		totalfront++;
		*/

	}
	
	public void find_rank_binary(int s, int obj)
	{
		/*
		int i, low, high, middle = 0;
		low = 0;			
		high = totalfront;
		dominated = false;
		while(high >= low) 
		{
			middle = low + ((high - low) / 2); //bug fix (low + high) / 2;
			
			find_rank(s, obj, middle);
			//find_basic(s, obj, middle);
			
			if(!dominated) //it has low rank, numerically
			{
				high = middle - 1;
			}
			else //it has high rank, numerically
			{
				low = middle + 1;
			}
		}
		
		if(dominated)
		{
			middle++;
		}
		
		if(middle>totalfront)
		{
			totalfront++;
			for(i=0;i<m1;i++)
			{
				list[i][totalfront] = new LinkedList();
			}
		}
		
		if(!skip)
		{
			rank[s] = middle;
		}
			if(NDT_specific[obj][rank[s]]==null)
			{
				NDT_specific[obj][rank[s]] = new NDTree(treeobjseq[0].length);
				NDT_specific[obj][rank[s]].setData(s);
			}
			else if(!skip)//skip is not true i.e. there is no point with similar objective value, then we got a place for this
			{
				last_nondominated_element.children[nondominated_obj_index] = new NDTree(treeobjseq[0].length);
				last_nondominated_element.children[nondominated_obj_index].setData(s);
			}
		
		*/
		int kmin=0, kmax = totalfront+1;//number of fronts been found
		int x = kmax, i;
		int k=(int)((kmax+kmin)/2.0+0.5);
		skip = false;
		//System.out.println("Starting with k = "+k+", kmax = "+kmax+", kmin = "+kmin);
		while(true)
		{
			find_rank(s, obj, k-1);
			if(skip)
			{
				break;
			}
			
			if(!dominated)
			{
				if(k==(kmin+1))
				{
					//System.out.println("k==(kmin+1), Adding "+s2+" to front = "+(k-1)+", j = "+j);
					//list[obj][k-1].addStart(s);
					rank[s]=k-1;
					break;
				}
				else
				{
					kmax=k;
					k=(int) ((kmax+kmin)/2.0+0.5);
					//System.out.println("Not Dominated, next  k = "+k);
				}
			}
			else
			{
				kmin=k;
				if(kmax==kmin+1 && kmax<x)
				{
					//System.out.println("kmax==kmin+1 && kmax<x, Adding "+s2+" to front = "+(kmax-1)+", j = "+j);
					//list[obj][kmax-1].addStart(s);
					rank[s]=kmax-1;
					break;
				}
				else if(kmin==x)//If the last existing front FL has been checked and pn doses not belong to this front
				{
					rank[s] = x;
					totalfront++;
					for(i=0;i<m1;i++)
					{
						list[i][totalfront] = new LinkedList();
					}
					
					//list[obj][x-1].addStart(s);
					
					//System.out.println("kmin==x, Adding "+s2+" to front = "+(x-1)+", j = "+j);
					break;
				}
				else
				{
					k=(int)((kmax+kmin)/2.0+0.5);
					//System.out.println("Dominated, next  k = "+k);
				}
			}//if dominated by one solution of F(k)
		}
	
		if(NDT_specific[obj][rank[s]]==null)
		{
			NDT_specific[obj][rank[s]] = new NDTree(treeobjseq[0].length);
			NDT_specific[obj][rank[s]].setData(s);
		}
		else if(!skip)//skip is not true i.e. there is no point with similar objective value, then we got a place for this
		{
			last_nondominated_element.children[nondominated_obj_index] = new NDTree(treeobjseq[0].length);
			last_nondominated_element.children[nondominated_obj_index].setData(s);
		}
	}
	
	public void find_rank_sequential(int s, int obj)
	{
		int k;	
		//dominated = false;
		for(k = 0; k <= totalfront; k++)
		{
			find_rank(s, obj, k);//go through ND-tree and lists
			//find_rank_nd_tree(s, obj, k);//go through only ND-tree
			//find_basic(s, obj, k);//go through ND-tree and lists
			
			if(!dominated) //not dominated
			{
				break;
			}
		}
		if(dominated)
		{
			if(k > totalfront)
			{
				totalfront++;
				for(int i=0;i<m1;i++)
				{
					list[i][totalfront] = new LinkedList();
				}
				
			}
		}
		rank[s] = k;

		if(NDT_specific[obj][rank[s]]==null)
		{
			NDT_specific[obj][rank[s]] = new NDTree(treeobjseq[0].length);
			NDT_specific[obj][rank[s]].setData(s);
		}
		else if(!skip)//skip is not true i.e. there is no point with similar objective value, then we got a place for this
		{
			last_nondominated_element.children[nondominated_obj_index] = new NDTree(treeobjseq[0].length);
			last_nondominated_element.children[nondominated_obj_index].setData(s);
		}
		
	}
		
	public void find_rank_splay(int s, int obj)
	{
		int i, k;
		boolean prev_skip = false;
		BinaryNode binNode = tree.getRoot();
		
		while(true)
		{
			k = (int) binNode.key;
			if(improvement_option==1)
			{
				find_rank(s, obj, k);//go through ND-tree and lists
			}
			else if(improvement_option==2)//nd-tree, no self-adjusting list
			{
				find_rank_nd_tree(s, obj, k);
			}
			else if(improvement_option==3)//self-adjusting list
			{
				find_rank_self_adjusting(s, obj, k);
			}
			else if (improvement_option==4)//no improvement
			{
				find_basic(s, obj, k);
			}
			else
			{
				find_rank(s, obj, k);//go through ND-tree and lists
			}
			
			if(!dominated)//if not dominated
			{
				if(binNode.left==null)//leftmost node of a sub-tree
				{
					rank[s] = k;
					tree.find(new Integer(Math.max(0, k-1)));
					break;
				}
				else
				{
					binNode = binNode.left;
				}
				prev_skip = skip;
			}
			else//if dominated
			{
				if(binNode.right!=null)
				{
					prev_skip = false;
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
					skip = prev_skip;
					tree.find(new Integer(k));
					break;
				}
				else //if(k==BestOrderSort.totalfront)//rightmost node of whole tree
				{
					totalfront++;
					for(i=0;i<m1;i++)
					{
						list[i][totalfront] = new LinkedList();
					}
					
					rank[s] =  totalfront;
					tree.insert(new Integer(rank[s]));//new front will be added in splay tree
					break;
				}
			}//if
		}//while(true)
		
		if(improvement_option==1 && improvement_option==2)
		{
			//add to tree
			if(NDT_specific[obj][rank[s]]==null)
			{
				NDT_specific[obj][rank[s]] = new NDTree(treeobjseq[0].length);
				NDT_specific[obj][rank[s]].setData(s);
			}
			else if(!skip)//skip is not true i.e. there is no point with similar objective value, then we got a place for this
			{
				last_nondominated_element.children[nondominated_obj_index] = new NDTree(treeobjseq[0].length);
				last_nondominated_element.children[nondominated_obj_index].setData(s);
			}
		}
	}
	
	public void find_rank_splay_hash(int s, int obj)
	{
		int i, k;
		boolean prev_skip = false;
		BinaryNode binNode = tree.getRoot();
		if(s==45926)
		{
			System.out.println("s = "+s);
		}
		while(true)
		{
			k = (int) binNode.key;

			find_rank_hash(s, obj, k);
			
			if(!dominated)//if not dominated
			{
				if(binNode.left==null)//leftmost node of a sub-tree
				{
					rank[s] = k;
					tree.find(new Integer(Math.max(0, k-1)));
					break;
				}
				else
				{
					binNode = binNode.left;
				}
				prev_skip = skip;
			}
			else//if dominated
			{
				if(binNode.right!=null)
				{
					prev_skip = false;
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
					skip = prev_skip;
					tree.find(new Integer(k));
					break;
				}
				else //if(k==BestOrderSort.totalfront)//rightmost node of whole tree
				{
					totalfront++;
					for(i=0;i<m1;i++)
					{
						list[i][totalfront] = new LinkedList();
					}
					
					rank[s] =  totalfront;
					tree.insert(new Integer(rank[s]));//new front will be added in splay tree
					break;
				}
			}//if
		}//while(true)
	}
	
	public void find_rank_hash(int s, int obj, int frontno)
	{
		Node head, prev;
		dominated = false;
		//compare with all the repeated solution, probably AVL would be better than list, but not sure 
		head = list[obj][frontno].start;
		prev = head;
		while(head!=null)
		{
			//if(dominates(head.data, s))
			if(dominates_objseq(head.data, s))
			//if(dominates_objseq_adaptive(head.data, s))
			{
				dominated = true;
				//if(rand.nextDouble()<0.2)
				{
					if(prev!=head)
					{
						list[obj][frontno].bringToFront(prev, head);
					}
				}
				
				break;
			}
			prev = head;
			head = head.link;
		}
		
		//compare with unique solutions
		if(dominated)//dominated by unique solutions
		{
			return;
		}
		else
		{
			if(hashtreeHead[obj][frontno]==0)
			{
				hashtreeHead[obj][frontno] = s+1;
			}
			else
			{
				dominated  = find_through_hashtree_bfs(s, obj, hashtreeHead[obj][frontno]-1);
			}
			return;
		}	
	}
	
	
	
	public void find_rank_basic(int s, int obj)
	{
		int i, k;	
		dominated  = false;
		for(k = 0; k <= totalfront; k++)
		{
			find_basic(s, obj, k);//go through ND-tree and lists
			
			if(!dominated) //not dominated
			{
				break;
			}
		}
		if(dominated)
		{
			if(k > totalfront)
			{
				totalfront++;
				for(i=0;i<m1;i++)
				{
					list[i][totalfront] = new LinkedList();
				}
				
			}
		}
		rank[s] = k;
		list[obj][k].add(s);
	}
	
	
	
	public void find_basic(int s, int obj, int frontno)//no use of self-adjusting list (only plain list)
	{
		Node head;
		dominated = false;
		//compare with all the solution found early 	
		head = list[obj][frontno].start;
		//prev = head;
		
		while(head!=null)
		{
			//Basic
			//if(dominates_objseq_adaptive(head.data, s))
			//if(dominates(head.data, s))	
			if(dominates_objseq(head.data, s))
			{
				dominated = true;
				break;
			}
			head = head.link;
		}
		return;
	}
	
	public void find_rank_self_adjusting(int s, int obj, int frontno)//no use of self-adjusting list (only plain list)
	{
		Node head, prev;
		dominated = false;
		//compare with all the solution found early 	
		head = list[obj][frontno].start;
		prev = head;
		
		while(head!=null)
		{
			//Basic+Self-Adjusting
			//if(dominates_objseq_adaptive(head.data, s))
			//if(dominates(head.data, s))	
			if(dominates_objseq(head.data, s))
			{
				dominated = true;
				//if(rand.nextDouble()<0.8)
				{
					if(prev!=head)
					{
						list[obj][frontno].bringToFront(prev, head);
					}
				}
				break;
			}
			prev = head;
			head = head.link;
		}
		return;
	}
	
	public void find_rank_nd_tree(int s, int obj, int frontno)//use nd-tree but not self-adjusting list
	{
		Node head;
		dominated = false;
		//compare with all the repeated solution, probably AVL would be better than list, but not sure 
		head = list[obj][frontno].start;

		while(head!=null)
		{
			//if(dominates(head.data, s))
			if(dominates_objseq(head.data, s))
			//if(dominates_objseq_adaptive(head.data, s))
			{
				dominated = true;				
				break;
			}
			head = head.link;
		}
		
		//compare with unique solutions
		if(!dominated)//dominated by unique solutions
		{
			skip = false;
			if(NDT_specific[obj]!=null)
			{
				found = false;
				check_tree(s, NDT_specific[obj][frontno], true);
			}
		}
	}
	
	
	public void find_rank(int s, int obj, int frontno)
	{
		Node head = list[obj][frontno].start;
		Node prev = head, prev_prev = null;
		dominated = false;
		//compare with all the repeated solution, probably AVL would be better than list, but not sure 
		//head = prev;
		//prev = head;
		
		while(head!=null)
		{
			//if(dominates(head.data, s))
			if(dominates_objseq(head.data, s))
			//if(dominates_objseq_adaptive(head.data, s))
			{
				dominated = true;
				//if(rand.nextDouble()<0.2)
				{
					if(prev!=head)
					{
						list[obj][frontno].bringToFront(prev, head);
						/*
						if(prev_prev!=prev)
						{
							list[obj][frontno].moveOneStep(prev_prev, prev, head);
						}*/
					}
				}
				
				break;
			}
			prev_prev = prev;
			prev = head;
			head = head.link;
		}
		
		//compare with unique solutions
		if(!dominated)//dominated by unique solutions
		{
			if(NDT_specific[obj]!=null)
			{
				found = false;
				check_tree(s, NDT_specific[obj][frontno], true);
			}
		}
		//return;
	}
	
	private void check_tree(int p, NDTree T, boolean add_pos) 
	{
		if(T==null)
		{
			return;//this is needed as the base case of recursive call
		}
		
		int i, m=-1;
		boolean less = false, equal = true;
		
		for(i=0;i<treeobjseq[0].length;i++)
		{
			if(population[p][treeobjseq[T.data][i]]<population[T.data][treeobjseq[T.data][i]])//compare with root
			{
				less = true;
				m = i; //save index
				comparison_rank++;
				break;
			}
			else if(population[p][treeobjseq[T.data][i]]==population[T.data][treeobjseq[T.data][i]])//compare with root
			{
				if(i==(treeobjseq[0].length-1)&& equal)
				{
					rank[p] = rank[T.data];
					skip = true;
//					
//					if(p==79)
//						System.out.println(p);
					return;
				}
			}
			else
			{
				equal = false;
			}
		}
		if(less==false)
		{
			dominated = true;
			return;
		}
		else
		{
			for(i=0;i<=m;i++)
			{
				check_tree(p, T.children[i], (i==m)&&add_pos);
				if(found || dominated || skip)
				{
					return;//p is dominated by a solution in the branch of the tree
				}
			}
			
			if(T.children[m]==null && add_pos)
			{
				last_nondominated_element = T;
				nondominated_obj_index = m;
				found = true;
			}
			
			return;
		}

	}
	
	
	private void sortingValues() 
	{
		//Sorting with respect to objective values
		for (int obj=1;obj<m1;obj++)
		{
			mergesort.setObj(obj);
			mergesort.mergesort(0, n-1);//mergesort
		}
	}
	
	public boolean find_through_hashtree_bfs(int s, int obj, int startSol)
	{
		LinkedList l1 = new LinkedList();
		long []divisor = new long[1];
		boolean addnode = true;
		boolean addcheck;
		
		Node head = null;
		HashNode hashhead = null;
		HashNode hashnode_location = null;

		l1.add(startSol);
		head = l1.start;
		
		while(head!=null)
		{
			startSol = head.data;
			
			dominated = dominates_list(startSol, s, divisor);
			
			if(dominated)
			{
				return true;
			}
			
			hashhead = hashtree[startSol].start;
			addcheck = true;
			hashnode_location = null;
			
			while(hashhead != null)
			{
				if((hashhead.key%divisor[0])==0)
				{
					addcheck = false;
					l1.add(hashhead.data);
				}
				else //if(hashhead.key < divisor[0])//don't need to consider
				{
					if(addnode)
					{
						hashnode_location = hashhead;//the last element where key is less
					}
				}
				hashhead = hashhead.link;
			}//while(hashhead!=null)
			
			if(addcheck)
			{
				addnode = false;//node found, no update needed
			}
			head = head.link;
		}
		
		hashtree[startSol].add_middle(s, divisor[0], hashnode_location);
		
		return dominated;
	}
	
	public static void main(String[] args) 
	{
		MyFileIO io = new MyFileIO();
		int N = 100000;
		int M = 10;
		int K = 100;
		int p, m;
		//double time = 0, comparison = 0;
		
		MMS mms;
	    
		double [][] time = new double[10][10];
		double [][] comparison = new double[10][10];
		int[] obj = new int[]{10,20,30,40,50,60,70,80,90,100};
		//int []obj = new int[]{100,200,300,400,500,600,700,800,900,1000};
//		int []obj = new int[20];
//		for(int i=5, j = 0;i<=100;i=i+5, j++)
//		{
//			obj[j] = i;
//		}
		//int[] num = new int[]{1000,2000,3000,4000,5000};
		int [][]front = new int[10][10];
		int end_m = 1;
		int end_p = 1;
		m=0;
		//for(m=0;m<end_m;m=m+1)
		{
			//System.out.println("m = "+m);
			//double [][]population = new double[N][obj[m]];
			//M = obj[m];
			double [][]population = new double[N][M];
			mms = new MMS(N, M);
			//T_BOS t_bos = new T_BOS(N, M);
			for(p=1;p<=end_p;p++)
			{
				System.out.println("M = "+M+", N = "+N+", K = "+10);
				io.read_data(0, population, N, M, K, p, 1, "");//cloud data
				//io.read_data(0, population, N, M, num[m], p, 1, "");
				//io.read_data(0, population, N, M, 10, p, 1, "");
				mms.sort(population);
				//t_bos.sort(population);
				time[m][p-1] = time[m][p-1] + mms.time;
				comparison[m][p-1] = comparison[m][p-1] + mms.comparison;
				front[m][p-1] = mms.totalfront;
			}
			System.gc();
			//System.out.print("\nTime:\t"+time[m][p-1]+"\t");
			//System.out.print("\nComparison\t"+comparison[m][p-1]+"\t");
		}
		
		System.out.print("\nTime:\t\t");
		for(m=0;m<end_m;m=m+1)
		{
			for(p=0;p<end_p;p++)
			{
				System.out.print(time[m][p]+"\t");
			}
			System.out.println();
		}
		
		
		System.out.print("\nComparison:\t");
		for(m=0;m<end_m;m=m+1)
		{
			for(p=0;p<end_p;p++)
			{
				System.out.print(comparison[m][p]+"\t");
			}
			System.out.println();
		}
		//MyFileIO io = new MyFileIO();
		//io.write_2d_double(comparison, "temp_comparison.txt", true);
		//io.write_2d_double(time, "temp_time.txt", true);
		
//		System.out.print("\nFront:\t");
//		for(m=0;m<end_m;m=m+1)
//		{
//			System.out.print(front[m][0]+"\n");
//		}
		System.out.println("Done");
	}
}
