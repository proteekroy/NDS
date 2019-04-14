package nds;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;


public abstract class AbstractNDS{
	
	//=======================Input======================================//
	public double population[][];
	public int n;
	public int m;
	boolean debug = false;//true;
	boolean printinfo = false;
	boolean moeaflag = false;//true;
	public boolean heapflag = false;//true=use heap, false=use sort
	public int rank_search_option = 2;//1=splay, 2=sequential, 3=binary, 4=hash
	public int improvement_option = 4;//1=use all, 2 = use nd-tree only, 3 = use  self-adjusting list, 4 = use none
	
	//=======================Output Information==========================//
	public long start, end, endTime2;
	public double time, sorting_time, domination_check_time;
	public double comparison, comparison_sort, comparison_rank;
	public int totalfront = 0;
	public int rank[];
	public int [][] objseq;
	public int [][] treeobjseq;
	public int obj_temp;
	
	//====================Specific Classes===============================//
	public MergeSort mergesort;
	HeapSort heapsort;
	public int [] helper;
	public int max_rank;
	public int m1;
	public Random rand;
	
	//====================Other Info===============================//
	public int []prime = new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541}; 
	

	
	abstract void initialize(double[][] pop);
	public abstract void sort(double[][] pop);
	
	public void mini_stat()
	{
		time = (end - start)*1.0/1000000.0;
		sorting_time = (endTime2 - start)*1.0/1000000.0;
		domination_check_time = (end - endTime2)*1.0/1000000.0;
		comparison = comparison_sort + comparison_rank;
		totalfront = 0;
		for(int i=0;i<n;i++)
		{
			if(rank[i]>totalfront)
			{
				totalfront = rank[i];
			}
			
		}
		totalfront++;
	}
	
	public boolean dominates(int p1, int  p2)//one way domination check
	{
		boolean equal = true;
		
		for(int i=0;i<m;i++)
		{
			comparison_rank++;
			if(population[p1][i] > population[p2][i])
			{
				return false;
			}
			else if(equal && population[p1][i] < population[p2][i])
			{
				equal = false;
			}
		}
		
		if(equal)//both solutions are equal
			return false;
		else //dominates
			return true;
	}
	
	
	
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
	
	public boolean dominates_objseq_adaptive(int p1, int p2) 
	{
		boolean equal = true;
		
		for(int i=0;i<m;i++)
		{
			comparison_rank++;
			if(population[p1][objseq[p1][i]] > population[p2][objseq[p1][i]])
			{
				if(i>0)
				{
					obj_temp = objseq[p1][i-1];
					objseq[p1][i-1] = objseq[p1][i];
					objseq[p1][i] = obj_temp;
				}
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
	
	public int dominates_bothway(int a, int b) 
	{
		int k;
		boolean a_dom_b=false;
		boolean b_dom_a=false;
		//comparison_dominated++;
		for(k=0;k<m;k++)//M objectives
		{
			comparison_rank++;
			if(population[a][k] > population[b][k])
			{
				b_dom_a=true;
				break;
			}
			else if(population[a][k] < population[b][k])
			{
				a_dom_b = true;
				break;
			}
		}
		for(k=k+1;k<m;k++)//M objectives
		{
			comparison_rank++;
			if(population[a][k] > population[b][k])
			{
				if(a_dom_b)
					return 2;
			}
			else if(population[a][k] < population[b][k])
			{
				if(b_dom_a)
					return 2;
			}
		}
		
		if(!a_dom_b && !b_dom_a)
			return 2;
		if(a_dom_b)
			return 1;
		else
			return 3;
	}
	
	public boolean dominates_list(int p1, int p2, long []divisor) 
	{
		boolean equal = true;
		boolean dominated = true;
		divisor[0] = 1;
		for(int i=0;i<m;i++)
		{
			comparison_rank++;
			if(population[p1][objseq[p1][i]] > population[p2][objseq[p1][i]])
			{
				dominated = false;
				if(i<m1)
				{
					divisor[0] = divisor[0]*prime[objseq[p1][i]];
				}
				//return false;
			}
			else if(population[p1][objseq[p1][i]] < population[p2][objseq[p1][i]])
			{
				equal = false;
			}
		}
	
		if(equal)//both solutions are equal
			return false;
		else //dominates
			return dominated;
	
	}
	
	public boolean dominates(int p1, int  p2, int k)//one way domination check
	{
		boolean equal = true;
		
		for(int i=0;i<k;i++)
		{
			comparison_rank++;
			if(population[p1][i] > population[p2][i])
			{
				return false;
			}
			else if(equal && population[p1][i] < population[p2][i])
			{
				equal = false;
			}
		}
		
		if(equal)//both solutions are equal
			return false;
		else //dominates
			return true;
	}
	
	public void printInformation()
	{
		int i;
		Node head;
		
		totalfront = 0;
		
		for(i=0;i<n;i++)
		{
			if(rank[i]>totalfront)
			{
				totalfront = rank[i];
			}	
		}
			
		totalfront++;
		int count[] = new int[totalfront];
		
		for(i=0;i<n;i++)
		{
			if(rank[i]>=0)
			{
				count[rank[i]]++;
			}
		}

		
		for(i=0;i<totalfront;i++)
		{
			System.out.println("Number of element in front ["+(i+1)+"] is "+count[i]);
		}
		
		if(debug)
		{
			LinkedList[] F=new LinkedList[totalfront];
			for(i=0;i<totalfront;i++)
			{
				F[i] = new LinkedList();
			}
			for(i=0;i<n;i++)
			{
				if(rank[i]>=0)
				{
					F[rank[i]].addStart(i);
				}
			}
			for(i=0;i<totalfront;i++)
			{
				if(F[i]==null)
					break;
				if(F[i].start==null)
					break;
				head = F[i].start;
				
				int j, k1 = 0;
				if(F[i].start!=null)
				{
					head = F[i].start;
					System.out.print(" --> ");
					while(head!=null) 
					{
						k1++;
						j =  head.data;
						System.out.print(" "+(j));
						head = head.link;
						if(k1%100==0)
							System.out.println();
					}
					System.out.println();
				}
			}
		}
		System.out.println("Number of fronts = "+totalfront);
		System.out.println("Total Number of objectives is "+ m);
		System.out.println("Total Number of elements is "+n);
		System.out.println("Sorting time  = "+sorting_time);
		System.out.println("Domination check time  = "+domination_check_time);
		System.out.println("Running time = "+time);
		System.out.println("Number of Comparisons for  sorting = "+comparison_sort);
		System.out.println("Number of Comparisons for  domination check = "+comparison_rank);
		System.out.println("Total Comparison = "+comparison);
		
	}
	
	
	public boolean lexicopgraphic_dominate(int p1, int p2)
	{
		for(int i=0;i<m;i++)
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
	
	
	public void read_population(int n, int m, String filename,double [][] population) 
	{
		int i=0,j;
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String strLine;
			while ((strLine = br.readLine()) != null)   
			{
				
				String[] tokens = strLine.split("\\s+");
				strLine=strLine.trim();
				if(i==n)
				{
					break;
				}
				for(j=0;j<m;j++)
				{
					try{
					population[i][j]=Double.parseDouble(tokens[j]);
					}
					catch (Exception e)
					{
						System.err.println("Error: " + e.getMessage());
					}
				}
				i++;
			}
			br.close();
		}
		catch (IOException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public boolean isEqual(int p1, int  p2)
	{
		int i;
				
		for(i=0;i<m;i++)
		{
			comparison_rank++;
			if(population[p1][i] == population[p2][i])
			{
				continue;
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	
	public void buildheapAllMinHeap(int m1)
	{
		for (int obj=1;obj<m1;obj++)
		{
			heapsort.setObj(obj);
			heapsort.buildheapMin(n-1);
		}
	}
	
	public void buildheapAllMaxHeap(int m1)
	{
		for (int obj=1;obj<m1;obj++)
		{
			heapsort.setObj(obj);
			heapsort.buildheapMax(n-1);
		}
	}
}
