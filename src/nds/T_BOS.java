package nds;


public class T_BOS extends AbstractNDS{
	
	int []lex_order;
	int [][]allrank;
	boolean []set;
	int []temp;
	
	boolean found, dominated, skip;
	NDTree last_nondominated_element;
	int nondominated_obj_index;
	public NDTree [] NDT_specific;
	public LinkedList [] P;
	public LinkedList [] list_bospp;
	public HashTree [] hashlist;
	LinkedList []temp_list2;
	boolean addnode;
	
	public T_BOS(int arg_n, int arg_m)
	{
		n = arg_n;
		m = arg_m;
		//m1 = (int) Math.min(m, Math.max(1, Math.ceil(Math.log10(n)/Math.log10(2))));
		//m1 = Math.min(m, m1);
		m1 = m;
		helper = new int[n];
		rank = new int[n];
		allrank = new int[m1][n];
		lex_order = new int[n];
		objseq = new int[n][m];
		set = new boolean[n];
		list_bospp = new LinkedList[m1];
		NDT_specific = new NDTree[m1];
		P = new LinkedList[m1];
		temp = new int[n];
		hashlist = new HashTree[n];
		temp_list2 = new LinkedList[m1];
		
		for(int i=0;i<n;i++)//return specific ranks, others are zero
		{
			hashlist[i] = new HashTree();
		}
		if(heapflag)
		{
			heapsort = new HeapSort();
		}
		for(int j=0;j<m1;j++)
		{
			P[j] = new LinkedList();
			list_bospp[j] = new LinkedList();
			temp_list2[j] = new LinkedList();
		}
	}
	
	@Override
	public void sort(double [][] population) 
	{
		initialize(population);	
		start = System.nanoTime();//starting timer
		t_bos2();
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
			System.out.println("\nStarting T-BOS Sort");
		}
		m1 = Math.min(m, m1);
		
		allrank = new int[m1][n];
		list_bospp = new LinkedList[m1];
		NDT_specific = new NDTree[m1];
		P = new LinkedList[m1];
		
		for(int j=0;j<m1;j++)
		{
			if(P[j]==null)
			{
				P[j] = new LinkedList();
			}
			else
			{
				P[j].delete();
			}
			if(list_bospp[j]==null)
			{
				list_bospp[j] = new LinkedList();
			}
			else
			{
				list_bospp[j].delete();
			}
		}
		
		population = pop;
		totalfront = 0;
		comparison = 0;
		comparison_sort = 0;
		comparison_rank = 0;
		time = 0;
		int i, j;
		
		/*for(j=0;j<m1;j++)
		{
			P[j].delete();
			list_bospp[j].delete();
		}*/
		
		for(j=0;j<m1;j++)
		{
			for(i=0;i<n;i++)
			{
				allrank[j][i] = i;
			}
		}
		
		for(i=0;i<n;i++)
		{
			set[i] = false;
			rank[i] = -1;
		}
		
		mergesort = new MergeSort();
		mergesort.setValues(helper, lex_order, population, allrank, this);
		if(heapflag)
		{
			heapsort.setAllrankObj(allrank, 0, this);
		}
	}
	
	public void t_bos()
	{
		int i = 0, j, s, total = 0, obj;
		Node head[], prev[];
		int bos_max_rank = n;
		
		head = new Node[m1];
		prev = new Node[m1];
		
		boolean []processed = new boolean[n];
		
		
		mergesort.setObj(0);
		mergesort.mergesort(0, n-1);//sorts first objective with lexicographic order
		
		for(i=0;i<n;i++)
		{
			lex_order[allrank[0][i]] = i;  
		}
		
		sortingValues();//sorting population according to all objectives
	
		//Step 2: Find maximum possible rank
		
		int []temp = new int[n];//initially zero
		total = 0;

		for(i=0;i<n;i++)
		{
			for(j=0;j<m;j++)
			{
				if(j<m1)
				{
					s = allrank[j][i];
					if(!set[s])
					{
						set[s] = true;
						total++;
					}
					if(total<=n)
					{
						P[j].add(s);
					}
					objseq[s][m1-1-temp[s]] = j;
					temp[s]++;
//					if(temp[s]<=0.3*m1)//heuristic, sum best 30% partial ranks 
//					{
//						sum_rank[s] = sum_rank[s] + i;
//					}
//					else if(temp[s]==1)//sum-rank is now second best rank
//					{
//						BOS.sum_rank[s] = temp[s];
//					}
				}
				else
				{
					objseq[i][j] = j;
				}				
			}
		}
		for(i=0;i<n;i++)
		{
			set[i] = false;
		}
		endTime2 = System.nanoTime();
		
		total = 0;
		totalfront = 0;

		
		while(total<n)//not all solutions are ranked
		{
			//ranked_obj.start = null;
			for(obj=0;obj<m1;obj++)//initialize
			{
				head[obj] = P[obj].start;
				prev[obj] = null;
				list_bospp[obj].delete();
				NDT_specific[obj] = null;//.delete();
				temp_list2[obj].start = null;
			}
			for(i=0;i<n;i++)
			{
				processed[i] = false;
				hashlist[i].start = null;
			}
			obj = 0;
			
			while(obj<m1)
			{
				if(head[obj]==null)
				{
					break;
				}

				if(set[head[obj].data])//s is already ranked
				{
					list_bospp[obj].addStart(head[obj].data);
					head[obj] = P[obj].delete(prev[obj], head[obj]);
				}
				else if(!processed[head[obj].data])
				{
					processed[head[obj].data] = true;
								
					//----------------find rank-----------------------------------------------------------------------------------
					//BOS.NDT_specific[obj] = find_rank_specific(head[obj].data, obj, BOS.bos_max_rank);
					//if(head[obj].data==18 || head[obj].data ==11)
					{
					//	System.out.println("head[obj].data = "+head[obj].data);
					}
					dominated = find_rank_hash(head[obj].data, obj, temp_list2[obj]);   
					//System.out.println("head[obj].data = "+head[obj].data+", dominated = "+dominated);
					
					if(!dominated)//if rank found or duplicate
					{
						//ranked_obj.add(head[obj].data);
						total++;
						set[head[obj].data] = true;	
						head[obj] = P[obj].delete(prev[obj], head[obj]);
					}
					else
					{
						prev[obj] = head[obj];
						head[obj] = head[obj].link;
					}
					
				}
				else
				{
					prev[obj] = head[obj];
					head[obj] = head[obj].link;
				}
				
				obj++;
				
				if(obj>=m1)
				{	
					obj = 0;
				
				}
				//System.gc();
			}
			
			/*Node head2;
			HashNode hashhead;
			head2 = ranked_obj.start;
			while(head2!=null)
			{
				hashhead = BOS.hashlist[head2.data].start;
				System.out.print("sol = "+head2.data+", child--> ");
				while(hashhead!=null)
				{
					System.out.print(hashhead.data+" ");
					hashhead = hashhead.link;
				}
				System.out.println();
				
				if(BOS.hashlist[head2.data].size>0)
				{
					avg_child = avg_child+BOS.hashlist[head2.data].size;
					parent_count++;
				}
				head2 = head2.link;
			}*/
			
			totalfront = totalfront + 1;
			
			if(totalfront>bos_max_rank)
			{	
				break; 
			}
		}
		/*System.out.println("Average solution comparison = "+(avg_sol_comp*1.0/BOS.n));
		
		if(parent_count>0)
		{
			avg_child = avg_child/parent_count;
		}
		System.out.println("Average number of child = "+(avg_child));*/
	}	
	public void t_bos2()
	{
		int i = 0, j, s, total = 0, obj;
		Node head[], prev[];
			
		head = new Node[m1];
		prev = new Node[m1];
			
		boolean []processed = new boolean[n];
			
			
		mergesort.setObj(0);
		mergesort.mergesort(0, n-1);//sorts first objective with lexicographic order
			
			
		for(i=0;i<n;i++)
		{
			lex_order[allrank[0][i]] = i;  
		}
			
		if(!heapflag)
		{
			sortingValues();//sorting population according to all objectives
		}
		else
		{
			buildheapAllMinHeap(m1);
		}
		int []temp = new int[n];//initially zero
		total = 0;

		for(i=0;i<n;i++)
		{
			for(j=0;j<m;j++)
			{
				if(j<m1)
				{
					if(!heapflag)
					{
						s = allrank[j][i];
					}
					else
					{
						if(j>0)//first objective is already sorted, so no need for heap
						{
							s = allrank[j][0];//Take the smallest element
							allrank[j][0] = allrank[j][n-i-1];
							heapsort.setObj(j);
							heapsort.siftdown_minheap(0, n-i-1);
						}
						else
						{
							s = allrank[0][i];
						}
					}
					if(!set[s])
					{
						set[s] = true;
						total++;
					}
					if(total<=n)
					{
						P[j].add(s);
					}
					objseq[s][m1-1-temp[s]] = j;
					temp[s]++;
					
				}
				else
				{
					objseq[i][j] = j;
				}		
			}
		}
			
		for(i=0;i<n;i++)
		{
			temp[i] = 0;
			set[i] = false;
		}
		endTime2 = System.nanoTime();
			
		total = 0;
		totalfront = 0;
			
		LinkedList templist = new LinkedList();
		
		while(total<n)//not all solutions are ranked
		{
			//System.out.println("total = "+total);
			while(templist.start!=null)
			{
				processed[templist.start.data] = false;
				hashlist[templist.start.data].start = null;
				temp[templist.start.data] = 0;
				templist.start = templist.start.link;
			}
				
			for(obj=0;obj<m1;obj++)//initialize
			{
				head[obj] = P[obj].start;
				prev[obj] = null;
				list_bospp[obj].delete();
				NDT_specific[obj] = null;//.delete();
				temp_list2[obj].start = null;
			}
			
			templist.delete();
			obj = 0;
				
			while(obj<m1)
			{		
				if(head[obj]==null)
				{
					break;
				}
					
				temp[head[obj].data]++;
				
				if(set[head[obj].data])//s is already ranked
				{
					if(rank[head[obj].data]==totalfront)//no need of this step
					{
						list_bospp[obj].addStart(head[obj].data);//do this step after next if statement						
							
						if(temp[head[obj].data]==m)//data is in current front and every new data from now is dominated
						{		
							if(head[obj].link!=null)
							{
								if(!isEqual(head[obj].link.data, head[obj].data))
								{
									head[obj] = P[obj].delete(prev[obj], head[obj]);
									break;
								}
							}
						}
					}	
					head[obj] = P[obj].delete(prev[obj], head[obj]);
				}
				else if(!processed[head[obj].data]) //data is not processed
				{
					processed[head[obj].data] = true;
									
					//----------------find rank-----------------------------------------------------------------------------------
					
					find_rank(head[obj].data, obj);
					//find_rank_hash(head[obj].data, obj, temp_list2[obj]);   
						
					if(!dominated)//if rank found or duplicate
					{
						total++;
						set[head[obj].data] = true;	
						head[obj] = P[obj].delete(prev[obj], head[obj]);
					}
					else
					{
						templist.addStart(head[obj].data);
						prev[obj] = head[obj];
						head[obj] = head[obj].link;
					}
						
				}
				else //data is processed, skip to next one
				{
					prev[obj] = head[obj];
					head[obj] = head[obj].link;
				}
					
				obj++;
				if(obj>=m1)
				{	
					obj = 0;
				}
			}
			
			if(moeaflag)
			{
				if(total>(n/2))
				{
					break;
				}
			}
			totalfront = totalfront + 1;
			if(totalfront>n)
			{	
				break; 
			}

		}		
	}
	
	public boolean find_rank_hash(int s, int obj, LinkedList list)
	{
		Node head, prev;
		dominated = false;
		int startSol;
		//int count = 0;	
		//compare with all the repeated solution, probably AVL would be better than list, but not sure 
		head = list_bospp[obj].start;
		prev = head;
		while(head!=null)
		{
			//count++;
			if(dominates_objseq(head.data, s))
			{
				dominated = true;
				if(prev!=head)
				{
					list_bospp[obj].bringToFront(prev, head);
				}
				break;
			}
			prev = head;
			head = head.link;
		}
			
		if(dominated)// This is dominated by the repeated solutions
		{
			//avg_sol_comp = avg_sol_comp+count;
			return true;
		}
		//compare with unique solutions
		if(list.start==null)
		{
			list.add(s);
			rank[s] = totalfront;
			return false;
		}
		else
		{
			startSol = list.start.data;
			
			/*for(int i=0;i<BOS.n;i++)
			{
				checked[i] = false;
			}*/
			//dominated  = find_through_hashtree_dfs_nonrecursive(s, obj, startSol);
			//dominated  = find_through_hashtree_bfs_reorder(s, obj, startSol);
			dominated  = find_through_hashtree_bfs(s, obj, startSol);
			//hashnode_location.start = null;
			//
			//addnode = true;dominated  = find_through_hashtree_dfs(s, obj, startSol);
		}	
		if(dominated)// This is dominated by the repeated solutions
		{
			return true;
		}	
		else //if(!dominated)//if not dominated, this is the rank
		{
			rank[s] = totalfront;
			return false;
		}
	}
	
	public boolean find_through_hashtree_dfs_nonrecursive(int s, int obj, int startSol)
	{
		LinkedList l1 = new LinkedList();
		long []divisor = new long[1];
		boolean addnode = true;
		boolean addcheck;
		
		HashNode hashhead = null;
		HashNode hashnode_location = null;
		
		int count = 0;
		l1.add(startSol);

		while(l1.size>0)
		{
			startSol = l1.pop();
			
			count++;
			dominated = dominates_list(startSol, s, divisor);
			
			if(dominated)
			{
				//avg_sol_comp = avg_sol_comp+count;
				return true;
			}
			
			hashhead = hashlist[startSol].start;
			addcheck = true;
			hashnode_location = null;
			
			while(hashhead != null)
			{
				if(hashhead.key >= divisor[0])
				{
					if((hashhead.key%divisor[0])==0)//subset or equal
					{
						if(hashhead.key==divisor[0])//cannot add here, go to depth 
						{
							addcheck = false;
							l1.addStart(hashhead.data);
						}
						else
						{
							l1.addStart(hashhead.data);
						}
					}
					if(addnode)//find the last location
					{
						hashnode_location = hashhead;//the last element where key is less
					}
				}
				//else, //don't need to consider
				
				hashhead = hashhead.link;
			}//while(hashhead!=null)
			
			if(addcheck)
			{
				addnode = false;//node found, no update needed
			}
		}
		
		hashlist[startSol].add_middle(s, divisor[0], hashnode_location);
		
		//avg_sol_comp = avg_sol_comp + count;
		
		return dominated;
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
		
		int count = 0;
		l1.add(startSol);
		head = l1.start;
		
		while(head!=null)
		{
			startSol = head.data;
			/*if(!checked[startSol])
			{
				checked[startSol] = true;
			}
			else
			{
				System.out.println("Problem, startSol = "+startSol);
				//checked[s] = true;
			}*/
			count++;
			dominated = dominates_list(startSol, s, divisor);
			
			if(dominated)
			{
				//avg_sol_comp = avg_sol_comp+count;
				return true;
			}
			
			hashhead = hashlist[startSol].start;
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
//				else if((hashhead.key%divisor[0])==0)//subset or equal
//				{
//					addcheck = false;
//					l1.add(hashhead.data);
//					if(hashhead.key==divisor[0])//cannot add here, go to depth 
//					{
//						addcheck = false;
//						l1.add(hashhead.data);
//					}
//					else
//					{
//						l1.add(hashhead.data);
//					}
//				}
				hashhead = hashhead.link;
			}//while(hashhead!=null)
			
			if(addcheck)
			{
				addnode = false;//node found, no update needed
			}
			head = head.link;
		}
		
		hashlist[startSol].add_middle(s, divisor[0], hashnode_location);
		
		//avg_sol_comp = avg_sol_comp+count;
		
		return dominated;
	}
	
	public boolean find_through_hashtree_dfs(int s, int obj, int startSol)
	{
		long []divisor = new long[1];
		HashNode hashhead = null;

		dominated = dominates_list(startSol, s, divisor);
		
		if(dominated)
		{
			return true;
		}
		
		hashhead = hashlist[startSol].start;
		
		boolean temp_boolean = true;
		
		while(hashhead!=null)
		{
			if((hashhead.key%divisor[0])==0)//subset or equal, cannot be added as a child of this node
			{
				temp_boolean = false;
				if(hashhead.key==divisor[0])//cannot add here, go to depth 
				{
					//System.out.print(hashhead.data+" ");
					dominated = find_through_hashtree_dfs(s, obj, hashhead.data);
				}
				else
				{
					dominated = find_through_hashtree_dfs(s, obj, hashhead.data);//don't add
				}
				if(dominated)
				{
					return true;
				}
			}
			hashhead = hashhead.link;
		}
		
		if(temp_boolean)
		{
			if(addnode)
			{
				hashlist[startSol].add_middle(s, divisor[0], hashlist[startSol].start);
				addnode = false;
			}
		}
		
		//no key exactly matches this, or no element at all
		//and, adding that node is valid.
		return dominated;
	}
	
	public boolean find_through_hashtree_bfs_reorder(int s, int obj, int startSol)
	{
		LinkedList l1 = new LinkedList();
		long []divisor = new long[1];
		boolean addnode = true;
		boolean addcheck;
		
		Node head = null;
		HashNode hashhead = null;
		HashNode hashnode_location = null;
		
		//int count = 0;
		l1.add(startSol);
		head = l1.start;
		
		while(head!=null)
		{
			startSol = head.data;
			
			//count++;
			dominated = dominates_list(startSol, s, divisor);
			
			if(dominated)
			{
				//avg_sol_comp = avg_sol_comp+count;
				return true;
			}
			
			hashhead = hashlist[startSol].start;
			addcheck = true;
			hashnode_location = null;
			
			while(hashhead != null)
			{
				if(hashhead.key >= divisor[0])//don't need to consider
				{
					if((hashhead.key%divisor[0])==0)//subset or equal
					{
						if(hashhead.key==divisor[0])//cannot add here, go to depth 
						{
							addcheck = false;
							l1.add(hashhead.data);
						}
						else
						{
							l1.add(hashhead.data);
						}
					}
					if(addnode)//find the last location
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
		
		hashlist[startSol].add_middle(s, divisor[0], hashnode_location);
		
		//avg_sol_comp = avg_sol_comp+count;
		
		return dominated;
	}
	
	
	public void find_rank(int s, int obj)
	{
		Node head, prev;
		dominated = false;
			
		//compare with all the repeated solution, probably AVL would be better than list, but not sure 
		head = list_bospp[obj].start;
		prev = head;
		while(head!=null)
		{
			if(dominates_objseq(head.data, s))
			{
				dominated = true;
				if(prev!=head)
				{
					list_bospp[obj].bringToFront(prev, head);
				}
				break;
			}
			prev = head;
			head = head.link;
		}
			
		if(dominated)// This is dominated by the repeated solutions
		{
			return;
		}
		
		//compare with unique solutions
		skip = false;
		found = false;
		
		update_tree(s, obj);
		
		
		if(dominated)
		{
			return;
		}
		else
		{
			if(skip)//No need to add this element
			{
				return;
			}
			rank[s] = totalfront;
				
			if(NDT_specific[obj]==null)
			{
				NDT_specific[obj] = new NDTree(objseq[0].length);
				NDT_specific[obj].setData(s);
			}
			else
			{
				last_nondominated_element.children[nondominated_obj_index] = new NDTree(objseq[0].length);
				last_nondominated_element.children[nondominated_obj_index].setData(s);
			}
		}
		return;
	}
	
	public void update_tree(int p, int obj) 
	{
		if(NDT_specific[obj]==null)
		{
			return;
		}
		else
		{
			check_tree(p, NDT_specific[obj], true);
		}
	}
	
	private void check_tree(int p, NDTree T, boolean add_pos) 
	{
		if(T==null)
		{
			return;//this is needed as the base case of recursive call
		}
		
		int i, m=-1;
		boolean less = false, equal = true;
		
		for(i=0;i<objseq[0].length;i++)
		{
			if(population[p][objseq[T.data][i]]<population[T.data][objseq[T.data][i]])//compare with root
			{
				less = true;
				m = i; //save index
				comparison_rank++;
				break;
			}
			else if(population[p][objseq[T.data][i]]==population[T.data][objseq[T.data][i]])//compare with root
			{
				if(i==(objseq[0].length-1)&& equal)
				{
					rank[p] = rank[T.data];
					skip = true;
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
	
	public static void main(String[] args) 
	{
		int i, j;
		int n = 10000;//184;
		int m = 200;//3;
		//int f = 10;//1;
		int p = 1;
		//String filename="fixed_front_"+n+"_"+m+"_"+f+"_"+p+".txt";
		//String filename="C:/Proteek's Folder/Research/Data/BOSBB data/Cloud Dataset/cloud_"+n+"_"+m+"_"+p+".txt";
		//String filename="mixed_"+n+"_"+m+"_"+f+"_"+p+".txt";
		
		MyFileIO io = new MyFileIO();
		long warmup = 10000000;
		for(int r = 0; r < warmup; r++) 
		{
		    // do test
		}
		for(n=50000;n<=50000;n=n+1000)
		{
			double [][]population = new double[n][m];
			BestOrderSort bos = new BestOrderSort(n,m);
			T_BOS t_bos = new T_BOS(n,m);
			
			double [][] comparison = new double[10][200];
			double [][] time = new double[10][200];
			
			for(p=2;p<=10;p++)
			{
				String filename="C:/Proteek's Folder/Research/Data/BOSBB data/Cloud Dataset/cloud_"+n+"_"+m+"_"+p+".txt";
				System.out.println(filename);
				bos.read_population(n, m, filename, population);
				for(i=1;i<=200;i++)
				{
					t_bos.m1 = i;
					t_bos.sort(population);
					time[p-1][i-1] = t_bos.time;
					comparison[p-1][i-1] = t_bos.comparison;
				}
				io.write_1d_double(comparison[p-1], "optimum_m_comparison_"+n+".txt", true); 
				io.write_1d_double(time[p-1], "optimum_m_time_"+n+".txt", true); 
			}
			System.gc();
		}
		System.out.println("Done");
		
	}
}
