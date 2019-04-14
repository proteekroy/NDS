package nds;



public class CornerSort extends AbstractNDS{
	
	LinkedList unranked, unmarked;
	
	public CornerSort(int arg_n, int arg_m)
	{
		n = arg_n;
		m = arg_m;
		rank = new int[n];
		comparison = 0;
		time = 0;
		unranked = new LinkedList();
		unmarked = new LinkedList();
	}
	
	@Override
	public void sort(double [][] population) 
	{	
		initialize(population);		
		start = System.nanoTime();//starting timer
		endTime2 = start;
		corner();
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
			System.out.println("\nStarting Corner Sort");
		}
		population = pop;
		totalfront = 0;
		comparison = 0;
		comparison_sort = 0;
		comparison_rank = 0;
		time = 0;
		if(unranked!=null)
		{
			unranked.delete();
		}
		if(unmarked!=null)
		{
			unmarked.delete();
		}
		for(int i=0;i<n;i++)
		{
			rank[i] = -1;
		}
	}
	
	private void corner()
	{
		int i, q; 
		
		Node head, prev;
		
		for(i=0;i<n;i++)
		{
			//unranked.add(i);//unranked is just a container
			unmarked.addStart(i);//initially all are unmarked
		}
		
		int x=0;//set first front
		int f;//set number sorted to 0
		f = 0;
		
		while(unmarked.size>0)//do until all the solutions are ranked
		{
			f=0;
			while(unmarked.size>0)//do until all solutions are marked
			{
				q = find_best(f);//best solution in objective f
				rank[q] = x;
			    
				head = unmarked.start;
				prev = head;
				while(head!=null)//check if best solution q dominates any other unmarked solution
				{
					if(this.dominates(q, head.data))//then delete
					{
						unranked.addStart(head.data);//container
						if(prev == head)//1st element
						{
							head = unmarked.start=head.link;
							prev.link = null;
							prev = head;
						}
						else
						{
							prev.link = head.link;
							head.link = null;
							head = prev.link;
						}
						unmarked.size--;
					}
					else
					{
						prev = head;
						head = head.link;
					}		
				}
				f = (f+1)%m;
				
			}
			
			x++;
			prev = head = unranked.start;
			
			while(head != null) //for all previously unranked(dominated) solutions will be added for next phase
			{
				unmarked.addStart(head.data);
				head = head.link;
				prev.link = null;
				prev = head;
			}
			unranked.start = null;//delete all from unranked because it is just a container
			if(moeaflag)
			{
				if(unmarked.size<(n/2))
				{
					break;
				}
			}
		}
		totalfront = x;
	}
	
	private int find_best(int f) 
	{
		Node head, prev, save_prev;
		head = unmarked.start;
		save_prev = prev=head;
		int q = head.data;
		head = head.link;
		while(head!=null)
		{
			if(lexicopgraphic_dominate(head.data,q,f))
			{
				q=head.data;
				save_prev=prev;
			}
			prev=head;
			head=head.link;
		}
		//delete as it will be ranked
		if(save_prev==unmarked.start)//1st element
		{
			if(q==unmarked.start.data)//delete the first or there is only one element
				unmarked.start=save_prev.link;//just link to the second
			else //delete second
				save_prev.link=save_prev.link.link;//save_prev.link.link may be null if only two elements
		}
		else
		{
			save_prev.link=save_prev.link.link;		
		}
		unmarked.size--;
		return q;
	}
	
	private boolean lexicopgraphic_dominate(int p1, int p2, int f)
	{
		int i,k;
		
		for(i=f,k=0;  k<m;  k++, i=(i+1)%m)
		{
			comparison_rank++;
			if(population[p1][i] == population[p2][i])
				continue;
			else if(population[p1][i] < population[p2][i])
			{
				return true;
			}
			else
			{
				return false;
			}
		}
			
		return true;
	}

}
