package nds;

public class DeductiveSort extends AbstractNDS {
	
	boolean[] D;
	boolean[] sorted;
	
	public DeductiveSort(int arg_n, int arg_m)
	{
		n = arg_n;
		m = arg_m;
		rank = new int[n];
		D = new boolean[n];//dominated flag initialize
		sorted = new boolean[n];//check if it is sorted
		
	}
	
	@Override
	public void sort(double [][] population) 
	{
		initialize(population);
		start = System.nanoTime();//starting timer
		endTime2 = start;
		deductive();
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
			System.out.println("\nStarting Deductive Sort");
		}
		population = pop;
		totalfront = 0;
		comparison = 0;
		comparison_sort = 0;
		comparison_rank = 0;
		time = 0;
		for(int i=0;i<n;i++)
		{
			D[i] = false;
			sorted[i] = false;
			rank[i] = -1;
		}
	}
	
	private void deductive()
	{
		
		int i,j,d;
		int x = 0;//set first front
		int f = 0;//set number sorted to 0
		
		while(f<n)//while not all are sorted
		{
			 for(i=0;i<n;i++)//iterate through solutions
			 {
				if(!D[i] && !sorted[i])
				{
					for(j=i+1;j<n;j++)//from next solution to end
					{
						if(!D[j] && !sorted[j])
						{
							d = this.dominates_bothway(i, j);//compute relation
							if(d==1) //if i dominates j 
							{
								D[j]=true;
							}
							else if(d==3)//if j dominates i 
							{
								D[i] = true;
								break;//break the inner for loop
							}
						}
					}
					if(!D[i])
					{
						rank[i] = x;
						sorted[i] = true;
						f++;
					}
				}
			 }
			 if(moeaflag)
			 {
				 if(f>(n/2))
				 {
					 break;
				 }
			 }
			 
			 x++;
			 
			 for(i=0;i<n;i++)
			 {
				 D[i] = false;
			 }
			 
			
		}
		totalfront = x;
	}
	

}
