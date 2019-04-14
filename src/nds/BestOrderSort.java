package nds;

public class BestOrderSort extends AbstractNDS 
{

	int m1 = -1;
	int [][]allrank;
	boolean []set;
	LinkedList [][]list;
	LinkedList []F;
	int index[];//two dimension
	int []lex_order;
	
	public int[] comparison_index_count;
	int []temp;
	int initialFront;
	int lastfront;
	boolean done;
	
	public BestOrderSort(int arg_n, int arg_m)
	{
		n = arg_n;
		m = arg_m;
		m1 = m;
		helper = new int[n];
		rank = new int[n];
		allrank = new int[m1][n];
		lex_order = new int[n];
		objseq = new int[n][m];
		mergesort = new MergeSort();
		set = new boolean[n];
		list = new LinkedList[m1][n];
		comparison_index_count = new int[n];
		temp = new int[n];
		done = false;
		F = new LinkedList[n];
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
		
		//if(m == 2)
		{
		//	extended_kung_sort_two_dimension();		
		}
		//else
		{
			best_sort();
		}

		end = System.nanoTime();
		mini_stat();
		
		if(printinfo)
		{
			printInformation();
		}
	}
	
	@Override
	void initialize(double[][] pop)
	{
		if(printinfo)
		{
			System.out.println("\nStarting Best Order Sort");
		}
		population = pop;
		//n = population.length;
		//m = population[0].length;
		
		
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
		
		comparison = 0;
		comparison_sort = 0;
		comparison_rank = 0;
		time = 0;
		totalfront = 0;
		initialFront = 0;
		done = false;
		lastfront = -1;
		
		for(int i=0;i<n;i++)
		{
			comparison_index_count[i] = m;
			set[i] = false;
			rank[i] = -1;
		}
		mergesort.setValues(helper, lex_order, population, allrank, this);
	}
	
	
	private void best_sort()
	{
		int i, j, total=0, front, s, obj;
		boolean dominated;
		Node head;
		
		
		mergesort.setObj(0);
		mergesort.mergesort(0, n-1);//sorts first objective with lexicographic order
		
		for(i=0;i<n;i++)
		{
			lex_order[allrank[0][i]] = i;  
		}
		
		sortingValues();//sorting population according to all objectives
		
		/*
		for(i=0;i<n;i++)
		{
			temp[i] = m-1;
		}
		
		for(i=0;i<n;i++)
		{
			for(j=0;j<m;j++)
			{
				s = allrank[j][i];
				objseq[s][temp[s]] = j;
				temp[s]--;
			}
		}*/
		endTime2 = System.nanoTime();
		
		
		for(j=0;j<m1;j++)
		{
			list[j][0] = new LinkedList();
		}
		
		for(i=0;i<n;i++)//n
		{
			for(obj=0;obj<m1;obj++)
			{
				s = allrank[obj][i];
				//if(s==94)
				//{
				//	System.out.println("s = "+s);
				//}
				/*
				if(m>2)
				{
					comparison_index_count[s]--;
				}*/	
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
				total++;
				
				for(front = initialFront; front <= totalfront; front++)
				{
					if(list[obj][front]==null)
					{
						list[obj][front] = new LinkedList();
						break;
					}
					head = list[obj][front].start;	
					dominated = false;
					while(head!=null)
					{
						//if(dominates_objseq(head.data, s))
						if(dominates(head.data, s))
						{
							dominated = true;
							break;
						}
						head = head.link;
					}
					if(!dominated) //not dominated
					{
						break;
					}
				}
				
				if(front == totalfront)
				{
					list[obj][front] = new LinkedList();
					totalfront++;
				}
				rank[s] = front;
				list[obj][rank[s]].addStart(s);
				if(moeaflag)
				{
					if(F[rank[s]]==null)
					{
						F[rank[s]] = new LinkedList();
					}
					F[rank[s]].add(s);
				}
			}
			if(moeaflag)
			{
				if(total>(n/2) && lastfront==-1)//save the last front to be discovered for moea
				{
					int size = 0;
					for(j=0;j<n;j++)
					{
						size = size + F[j].size;
						if(size>(n/2))
						{
							lastfront = j;
							break;
						}
					}
					lastfront = totalfront;
				}
				if(lastfront==initialFront)
				{
					break;
				}
			}
			if(total==n)
			{
				break;
			}
		}
		totalfront++;
	}

	public int countMaxComparison(double[][] pop)
	{
		population = pop;

		for(int j=0;j<m1;j++)
		{
			for(int i=0;i<n;i++)
			{
				allrank[j][i] = i;
			}
		}
		
		comparison = 0;
		comparison_sort = 0;
		comparison_rank = 0;
		time = 0;
		totalfront = 0;
		initialFront = 0;
		
		for(int i=0;i<n;i++)
		{
			set[i] = false;
		}
		mergesort.setValues(helper, lex_order, population, allrank, this);
		
		int counter = 0;
		int i, total=0, s, obj;

		mergesort.setObj(0);
		mergesort.mergesort(0, n-1);//sorts first objective with lexicographic order
		
		sortingValues();//sorting population according to all objectives
		
		for(i=0;i<n;i++)//n
		{
			for(obj=0;obj<m1;obj++)
			{
				s = allrank[obj][i];
					
				if(set[s])//s is already ranked
				{
					continue;
				}
				set[s] = true;
				total++;
				
			}
			if(total==n)
			{
				counter = i+1;
				break;
			}
		}
		return counter;
	}
	
	@Override
	public boolean dominates_objseq(int p1, int p2) 
	{
		boolean equal = true;
		if(comparison_index_count[p1]==0)
		{
			if(rank[p1]>initialFront)//all solutions discovered later have higher ranks than p1
			{
				initialFront = rank[p1];
			}
			return !isEqual(p1, p2);
		}			
		for(int i=0;i<comparison_index_count[p1];i++)
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
	
	private void sortingValues() 
	{
		//Sorting with respect to objective values
		for (int obj=1;obj<m1;obj++)
		{
			mergesort.setObj(obj);
			mergesort.mergesort(0, n-1);//mergesort
		}
	}
	

	public void extended_kung_sort_two_dimension()
	{
		//Initialization
		int i, low, high, middle, s;
		double key;
		index = new int[n];
		
			
		mergesort.setObj(0);
		mergesort.mergesort(0, n-1);
		
		endTime2 = System.nanoTime();
		
		index[0] = allrank[0][0];//y-value of first rank solution
		rank[allrank[0][0]] = 0; //rank of first solution is already found
		totalfront = 1;
		
		for(i=1;i<n;i++)
		{
			s = allrank[0][i];//take the solution id
			key = population[s][1];//the field we would consider
			
			//-------------Go over all points----------------------//
			low = 0;			
			high = totalfront - 1;
		         
			while(high >= low) 
			{
				middle = low + ((high - low) / 2); //bug fix (low + high) / 2;
				comparison_rank++;
				if(key < population[index[middle]][1]) //it has low rank, numerically
				{
					high = middle - 1;
					
				}
				else if(key > population[index[middle]][1]) //it has high rank, numerically
				{
					low = middle + 1;
				}
				else
				{
					if(population[index[middle]][0]<population[s][0])
					{
						low = middle + 1;
					}
					else//first objective was also same
					{
						low = rank[index[middle]];
						break;
					}
				}
			}
			
			if(low==totalfront)
			{
				totalfront = totalfront+1;
			}
			rank[s] = low;
			index[low] = s;
							
		}
	}
	
	public static void main(String[] args) 
	{
		int i;
		int n = 50000;//200;//50000;//184;
		int m = 10;//10;//10;//3;
		int f = 10;//5;//10;//1;
		int p = 1;
		
		String filename = "Test/fixed_front_"+n+"_"+m+"_"+f+"_"+p+".txt";
		//String filename="cloud_"+n+"_"+m+"_"+p+".txt";
		//String filename = "test.txt";
		//String filename="mixed_"+n+"_"+m+"_"+f+"_"+p+".txt";
		double [][]population = new double[n][m];
		
//		int i,j, k;
//		int n = 15;
//		int m = 5;
//		int f = 1;
//		
//		double [][]population = new double[n][m];
//		
//		population[0][1] = 2;	population[0][2] = 4;	population[0][3] = 3;	population[0][4] = 2;	population[0][0] = 3;	
//		population[1][1] = 4;	population[1][2] = 3;	population[1][3] = 2;	population[1][4] = 5;	population[1][0] = 1;
//		population[2][1] = 1;	population[2][2] = 1;	population[2][3] = 1;	population[2][4] = 1;	population[2][0] = 1;
//		population[3][1] = 1;	population[3][2] = 5;	population[3][3] = 5;	population[3][4] = 3;	population[3][0] = 2;
//		population[4][1] = 1;	population[4][2] = 2;	population[4][3] = 3;	population[4][4] = 1;	population[4][0] = 5;
//		population[5][1] = 3;	population[5][2] = 2;	population[5][3] = 1;	population[5][4] = 1;	population[5][0] = 5;
//		population[6][1] = 3;	population[6][2] = 1;	population[6][3] = 1;	population[6][4] = 1;	population[6][0] = 5;
//		population[7][1] = 2;	population[7][2] = 5;	population[7][3] = 3;	population[7][4] = 2;	population[7][0] = 3;
		
//		for(i=0;i<n;i++)
//		{
//			for(j=0;j<m;j++)
//			{
//				if(i<5)
//				{
//					population[i][j] = 3;
//				}
//				else if(i<10)
//				{
//					population[i][j] = 1;
//				}
//				else
//				{
//					population[i][j] = 2;
//				}
//			}
//			population[i][m-1] = 10;
//		}
		BestOrderSort bos = new BestOrderSort(n,m);
		//DivideAndConquer ddc = new DivideAndConquer(n, m);
		//MMS2 mms2 = new MMS2(n, m);
		MMS mms = new MMS(n, m);
		T_ENS t_ens = new T_ENS(n,m);
		ENS_BS ens_bs = new ENS_BS(n,m);
		//H_BOS h_bos = new H_BOS(n,m);
		T_BOS t_bos = new T_BOS(n,m);
		
		bos.printinfo = true;
		//bos.debug = true;
		t_bos.printinfo = true;
		t_ens.printinfo = true;
		mms.printinfo = true;
		ens_bs.printinfo = true;
		//mms.debug = true;
		bos.read_population(n, m, filename, population);
		
		//bos.sort(population);
		//t_ens.sort(population);
		//ens_bs.sort(population);
		t_bos.sort(population);
		mms.sort(population);
		
		//ddc.sort(population);
		
		int []rank1 = mms.rank;
	
		int []rank2 = bos.rank;
	    
	    
		boolean same = true;
		for(i=0;i<n;i++)
		{
			if(rank1[i]!=rank2[i])
			{
				same = false;
				System.out.println("Not same for this population");
				break;
			}
		}
		
		if(!same)
		{
			System.out.println("Not same for this population");
		}
		
	}
}
