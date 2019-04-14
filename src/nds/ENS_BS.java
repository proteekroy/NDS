package nds;

public class ENS_BS extends AbstractNDS {
	
	LinkedList[] F;
	HeapSort heapsort;
	int[][] allrank;
	
	public ENS_BS(int arg_n, int arg_m)
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
		ens_bs();
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
			System.out.println("\nStarting ENS-BS Sort");
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
	
	private void ens_bs()
	{
		int s, s2, i, k, kmax, kmin; 
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
			s=temp[i];	
			kmin=0;
			kmax=x;//number of fronts been found
			k=(int) Math.floor(((kmax+kmin)/2.0+0.5));//front now checking
			
			while(true)
			{
				head = F[k-1].start;	
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
					if(k==(kmin+1))
					{
						F[k-1].addStart(s);
						rank[s] = k-1;
						break;
					}
					else
					{
						kmax=k;
						k=(int) Math.floor((kmax+kmin)/2.0+0.5);	
					}
				}
				else
				{
					kmin=k;
					if(kmax==kmin+1 && kmax<x)
					{
						F[kmax-1].addStart(s);
						rank[s] = kmax-1;
						break;
					}
					else if(kmin==x)//If the last existing front FL has been checked and pn doses not belong to this front
					{
						x++;
						F[x-1].addStart(s);
						rank[s] = x-1;
						break;
					}
					else
					{
						k=(int) Math.floor((kmax+kmin)/2.0+0.5);
					}
				}//if dominated by one solution of F(k)
			}
		}	
		totalfront = x;
	}
}
