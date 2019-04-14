package nds;

public class ENS_SS extends AbstractNDS {
	
	LinkedList[] F;
	HeapSort heapsort;
	int[][] allrank;
	
	public ENS_SS(int arg_n, int arg_m)
	{
		n = arg_n;
		m = arg_m;
		rank = new int[n];
		F = new LinkedList[n];
		heapsort = new HeapSort();
		allrank = new int[1][n];
	}
	
	@Override
	public void sort(double [][] population) 
	{
		initialize(population);
		start = System.nanoTime();//starting timer
		ens_ss();
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
			System.out.println("\nStarting ENS-SS Sort");
		}
		population = pop;
		totalfront = 0;
		comparison = 0;
		comparison_sort = 0;
		comparison_rank = 0;
		time = 0;
		
		for(int i=0;i<n;i++)
		{
			if(F[i]==null)
			{
				F[i]=new LinkedList();
			}
			else
			{
				F[i].delete();
			}
			allrank[0][i] = i;
			rank[i] = -1;
		}
	}
	
	private void ens_ss()
	{
		int s,s2,i,k; 
		int x;//set first front
		boolean dominated;
		//- initialize fronts (already done)
		Node head;
		
		heapsort.setAllrankObj(allrank, 0, this);
		heapsort.sort(n);
		endTime2 = System.nanoTime();
		
		int [] temp = allrank[0];
		
		x = 1;//current list size is 0, means list 0 is the last list.
		F[0].addStart(temp[0]);
		rank[temp[0]] = 0;
		
		for(i=1;i<n;i++)
		{
			s = temp[i];
			k = 0;//front now checking
			while(true)
			{
				//compare with each solution of F(k)
				head = F[k].start;
				dominated = false;

				while(head!=null)
				{
					s2 = head.data;
					dominated = dominates(s2,s);
					if(dominated)
					{
						break;
					}	
					head = head.link;
				}
				if(!dominated)
				{
					F[k].addStart(s);
					rank[s] = k;
					break;//break from while loop
				}
				else
				{
					k++;
					if(k==x)//k reaches a new front which is not yet initialized;
					{
						F[k].addStart(s);
						rank[s] = k;
						x++;//increase list size, but don't need to allocate
						break;//break from while(true) loop
					}
				}//if dominated by one solution of F(k)
			}	
		}
		totalfront = x;
	}
}
