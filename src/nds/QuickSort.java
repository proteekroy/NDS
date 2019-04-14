package nds;

import java.util.Random;

public class QuickSort {
	
	static MedianLinear medlin;
	
	
	static double [][]population;
	static int []arr;
	static int []less_pivot;
	static int []equal_pivot;
	static int []greater_pivot;
	static double []pivot_find_array;
	static int n;
	static int m;
	double pivot;
	Random rand;
	int randIndex;
	
	void set(double [][]pop, int[] arg_arr, AbstractNDS	nds)
	{
		n = pop.length;
		m = pop[0].length;
		less_pivot = new int[n];
		equal_pivot = new int[n];
		greater_pivot = new int[n];
		pivot_find_array = new double [n];
		arr = arg_arr;
		population = pop;
		medlin = new MedianLinear();
		medlin.setNDS(nds);
		rand = new Random();
	}
	
	/* This function partitions a[] in three parts
	   a) a[l..i] contains all elements smaller than pivot
	   b) a[i+1..j-1] contains all occurrences of pivot
	   c) a[j..r] contains all elements greater than pivot */
	
	void quick_partition(int dim, int l, int r, int[]pi)
	{
		
		int temp;
		if(r-l<1)
		{
			return;
		}
		else if (r-l==1) 
		{
			if(population[arr[l]][dim]>population[arr[r]][dim])
			{
				temp = arr[l];
		        arr[l] = arr[r];
		        arr[r] = temp;    
			}
			pi[0] = r;
			pi[1] = r;
			
			return;
		}
		
		int k1, k2, k3, k4, i, j, p, q, index1, index2;
	    i = l-1;
	    j = r;
	    p = l-1;
	    q = r;
	    k1 = 0;
	    k2 = 0;
	    k3 = 0;
	    k4 = 0;
	    
	    //find pivot and put it on the rightmost corner
	    randIndex = (r-l)/2;//(int) (l + (rand.nextInt(r-l)));
		pivot = population[arr[randIndex]][dim];// medlin.findMedian(pivot_find_array, 0, high-low);
	    
	    /*
		index2 = 0;
	    for(int index=l;index<=r;index++)
		{
			pivot_find_array[index2] = population[arr[index]][dim];
			index2++;
		}
	    pivot = medlin.findMedian(pivot_find_array, 0, r-l);
	    
	    for(int index=l;index<=r;index++)
		{
	    	if(pivot==population[arr[index]][dim])
	    	{
	    		randIndex = index;
	    	}
		}*/
		
		
		temp = arr[randIndex];
		arr[randIndex] = arr[r];
		arr[r] = temp;
	    
	    //int v = arr[r];//pivot
	    
	 
	    while (true)
	    {
	        // From left, find the first element greater than
	        // or equal to v. This loop will definitely terminate
	        // as v is last element
	        //while (arr[++i] < v);
	    	
	    	while(population[arr[(++i)]][dim] < pivot)
	    	{
	    		k1++;
	    	}
	    		
	        // From right, find the first element smaller than or
	        // equal to v
	        //while (v < arr[--j])
	        //    if (j == l)
	        //        break;
	    	while(pivot < population[arr[(--j)]][dim])
	    	{	
	    		k2++;
	    		if(j==l)
	    			break;
	    	}
	        // If i and j cross, then we are done
	        if (i >= j) 
	        	break;
	 
	        // Swap, so that smaller goes on left greater goes on right
	        //swap(arr[i], arr[j]);
	        //if(population[arr[i]][dim]>pivot)//swap only when current i-th point is greater because it could be equal
	        {
		        temp = arr[i];
		        arr[i] = arr[j];
		        arr[j] = temp;
		        k1++;
	        }
	        // Move all same left occurrence of pivot to beginning of
	        // array and keep count using p
//	        if (a[i] == v)
//	        {
//	            p++;
//	            swap(a[p], a[i]);
//	        }
	        if (population[arr[i]][dim] == pivot)
	        {
	            p++;
	            temp = arr[p];
	            arr[p] = arr[i];
	            arr[i] = temp;
	            k3++;
	        }
	 
	        // Move all same right occurrence of pivot to end of array
	        // and keep count using q
//	        if (a[j] == v)
//	        {
//	            q--;
//	            swap(a[j], a[q]);
//	        }
	        if (population[arr[j]][dim] == pivot)
	        {
	            q--;
	            temp = arr[j];
	            arr[j] = arr[q];
	            arr[q] = temp;
	            k4++;
	        }
	    }

	    
	    // Move pivot element to its correct index
	    //swap(a[i], a[r]);
	    //if(population[arr[i]][dim]>pivot)
	    {
		    temp = arr[i];
		    arr[i] = arr[r];
		    arr[r] = temp;
		    k3++;
	    }
	    
	    // Move all left same occurrences from beginning
	    // to adjacent to arr[i]
	    //if(l>p)
	    {
		    j = i-1;
		    for (int k = l; k < p; k++, j--)
		    {
		        //swap(a[k], a[j]);
		    	temp = arr[k];
		    	arr[k] = arr[j];
		    	arr[j] = temp;
		    }
	    }
	    // Move all right same occurrences from end
	    // to adjacent to arr[i]
	    //if(r-1>q)
	    {
		    i = i+1;
		    for (int k = r-1; k > q; k--, i++)
		    {
		        //swap(a[i], a[k]);
		    	temp = arr[i];
		    	arr[i] = arr[k];
		    	arr[k] = temp;
		    }
	    }
	    
	   // pi[0] = j;
	   // pi[1] = i-1;
	    if(k1>0)
	    {
	    	pi[0]= j;
	    	pi[1] = j+k3+k4; 
	    }
	    else
	    {
	    	pi[0]= -1;
	    	pi[1] = k3+k4; 
	    }
	    
//	    if(k1>0)
//		{pi[0] = l + Math.max(0, k1-1);}
//		else
//		{pi[0] = l-1;}
//		pi[1] = l + Math.max(0, k1+k3+k4-1);
	}
	
	void partition(int dim, int low, int high, int []pi)
	{
		int i, k1=0, k2=0, k3=0, j = 0;
		
		//pivot_find_array = new double [high-low+1];
		//find pivot in linear time
		
		/*for(i=low;i<=high;i++)
		{
			pivot_find_array[j] = population[arr[i]][dim];
			j++;
		}*/
		randIndex = (int) (low + (rand.nextInt(high-low)));
		pivot = population[arr[randIndex]][dim];// medlin.findMedian(pivot_find_array, 0, high-low);
		
		//partition using pivot
		for(i=low;i<=high;i++)
		{
			if(population[arr[i]][dim]<pivot)
			{
				less_pivot[k1] =  arr[i];
				k1++;
			}
			else if(population[arr[i]][dim]>pivot)
			{
				greater_pivot[k2] = arr[i];
				k2++;
			}
			else
			{
				equal_pivot[k3] = arr[i];
				k3++;
			}
		}
		
		if(k1>0)
		{pi[0] = low + Math.max(0, k1-1);}
		else
		{pi[0] = low-1;}
		pi[1] = low + Math.max(0, k1+k3-1);
		
		i = 0;
		j = low;
		
		if(k1>0)
		{
			while(i<k1)//copy lesser valued elements
			{
				arr[j] = less_pivot[i];
				j++;
				i++;
			}
		}
		i = 0;
		while(i<k3)//copy equal valued elements
		{
			arr[j] = equal_pivot[i];
			j++;
			i++;
		}
		
		if(k2>0)
		{
			i=0;
			while(i<k2)//copy greater valued elements
			{
				arr[j] = greater_pivot[i];
				j++;
				i++;
			}
		}	
	    return;	
		
	}
	
	
	static void partition2(int dim, int low, int high, int []pi, double pivot)
	{
		int temp;
		
		
		int i, j = 0, i1, i2, j1, j2, k=0;
		
		//randIndex = (int) (low + (rand.nextInt(high-low)));
		//int randIndex = (high-low)/2;
		
		
		i1 = low;
		i2 = i1;
		j1 = high;
		j2 = high;
		
		//partition using pivot
		while (true)
		{	    	
	    	while(population[arr[i2]][dim] <= pivot)
	    	{
	    		if(population[arr[i2]][dim] < pivot)
	    		{
	    			arr[i1] = arr[i2];
	    			i1++;
	    			i2++;
	    		}
	    		else if(population[arr[i2]][dim] == pivot)
	    		{
		        	equal_pivot[k] = arr[i2];
		        	i2++;
		        	k++;
		        }
	    		if(i2>j2)
    				break;
	    	}
	    	if(i2>j2)
				break;
	    	
	    	while(pivot <=  population[arr[j2]][dim] )
	    	{	
	    		if(pivot < population[arr[j2]][dim] )
	    		{
		    		arr[j1] = arr[j2];
		    		j1--;
		    		j2--;
		    		
	    		}
	    		else if(population[arr[j2]][dim]  == pivot)
		        {
		        	equal_pivot[k] = arr[j2];
		        	j2--;
		        	k++;
		        }
	    		if(j2<i2)
	    			break;
	    	}
	        if (i2 >= j2) 
	        	break;
	        
	        
    	    temp = arr[i2];
		    arr[i2] = arr[j2];
		    arr[j2] = temp;
	        

	        if (i2 > j2) 
	        	break;
	    }
		
		i=i1;
		temp = i1+k;
		
		while(i<temp)//copy lesser valued elements
		{
			arr[i] = equal_pivot[j];
			i++;
			j++;
		}
		
		if(i1>low)//at least one
		{
			pi[0] = Math.max(0, i1-1);
		}
		else
		{
			pi[0] = low-1;
		}
		
		pi[1] = temp-1;//low + Math.max(0, i1+k-1);
		
		
	    return;	
		
	}
	
	
	/* The main function that implements QuickSort()
    arr[] --> Array to be sorted,
    low  --> Starting index,
    high  --> Ending index */
	void sort(int dim, int low, int high)
	{
		
		if (low < high)
		{
			if(high-low<2)
			{
				if(population[arr[high]][dim]<population[arr[low]][dim])
				{
					int temp = arr[low];
				    arr[low] = arr[high];
				    arr[high] = temp;
				}
				return;
			}
			/* pi is partitioning index, arr[pi] is 
            now at right place */
			int []pi = new int[2];
			int randIndex = (int) (low + (rand.nextInt(high-low)));
			//double pivot = population[arr[low+(int)Math.round((high-low)/2)]][dim];// medlin.findMedian(pivot_find_array, 0, high-low);
			double pivot = population[arr[randIndex]][dim];// medlin.findMedian(pivot_find_array, 0, high-low);
			
			
			partition2(dim, low, high, pi, pivot);
			//partition(dim, low, high, pi);			
			//quick_partition(dim, low, high, pi);
			
			//solutions lower than pivot
			if(low<pi[0])
			{
				sort(dim, low, pi[0]);//lower than pivot
			}
			
			//solutions equal to pivot
			if(dim< (m-1))// && (pi[0]+1) < pi[1])// && (pi[0]+1)<pi[1])
			{
				sort(dim+1, pi[0]+1, pi[1]);
			}
			//solutions with higher than pivot
			sort(dim, pi[1]+1, high);//higher than pivot
			
		}
	}
	
	/* A utility function to print array of size n */
	static void printArray()
    {
    	int n = population.length;
        for (int i=0; i<n; ++i)
        {
        	System.out.print(arr[i]+" --| ");
        	for(int j=0;j<m;j++)
        	{
        		System.out.print(population[arr[i]][j]+"\t");
        	}
        	System.out.println();
        }
        System.out.println();
    }
 
	static void printArray(int [] arr)
    {
    	int n = arr.length;
        for (int i=0; i<n; i++)
        {
       		System.out.print(arr[i]+"\t");
        }
        System.out.println();
    }
	
	
	static void int_partition(int []arr, int low, int high, int []pi, int pivot)
	{
		int i, j = 0, i1, i2, j1, j2, k=0, temp1;
		
		//randIndex = (int) (low + (rand.nextInt(high-low)));
		//randIndex = (high-low)/2;
		//pivot = arr[randIndex];// medlin.findMedian(pivot_find_array, 0, high-low);
		
		i1 = low;
		i2 = i1;
		j1 = high;
		j2 = high;
		
		//partition using pivot
		while (true)
		{	    	
	    	while(arr[i2] <= pivot)
	    	{
	    		if(arr[i2] < pivot)
	    		{
	    			arr[i1] = arr[i2];
	    			i1++;
	    			i2++;
	    		}
	    		else if(arr[i2] == pivot)
	    		{
		        	equal_pivot[k] = arr[i2];
		        	i2++;
		        	k++;
		        }
	    		if(i2>j2)
    				break;
	    	}
	    	if(i2>j2)
				break;
	    	
	    	while(pivot <= arr[j2])
	    	{	
	    		if(pivot < arr[j2])
	    		{
		    		arr[j1] = arr[j2];
		    		j1--;
		    		j2--;
		    		
	    		}
	    		else if(arr[j2] == pivot)
		        {
		        	equal_pivot[k] = arr[j2];
		        	j2--;
		        	k++;
		        }
	    		if(j2<i2)
	    			break;
	    	}
	        if (i2 >= j2) 
	        	break;
	        
	        
    	    temp1 = arr[i2];
		    arr[i2] = arr[j2];
		    arr[j2] = temp1;
	        
	        
	        
	        if (i2 > j2) 
	        	break;
	    }
		
		i=i1;
		temp1 = i1+k;
		
		while(i<temp1)//copy lesser valued elements
		{
			arr[i] = equal_pivot[j];
			i++;
			j++;
		}
		
		if(i1>low)//at least one
		{
			pi[0] = Math.max(0, i1-1);
		}
		else
		{
			pi[0] = low-1;
		}
		
		pi[1] = temp1-1;//low + Math.max(0, i1+k-1);
		
		
	    return;	
		
	}
    // Driver program
    public static void main(String args[])
    {
    	System.gc();
    	double [][]population = new double[10][5];
    	int []arr = new int[population.length];
    	int i;
		
		for(i=0;i<population.length;i++)
		{
			arr[i] = i;
		}
    	
    	QuickSort ob = new QuickSort();
    	AbstractNDS	nds = new MMS(n, m);
    	ob.set(population, arr, nds);
    	
    	population[0][0] = 3;	population[0][1] = 2;	population[0][2] = 5;	population[0][3] = 3;	population[0][4] = 2;	
    	population[1][0] = 1;   population[1][1] = 4;	population[1][2] = 3;	population[1][3] = 2;	population[1][4] = 5;	
    	population[2][0] = 1;   population[2][1] = 1;	population[2][2] = 1;	population[2][3] = 1;	population[2][4] = 1;	
    	population[3][0] = 2;   population[3][1] = 1;	population[3][2] = 5;	population[3][3] = 5;	population[3][4] = 3;	
    	population[4][0] = 5;   population[4][1] = 1;	population[4][2] = 2;	population[4][3] = 3;	population[4][4] = 1;	
    	population[5][0] = 5;   population[5][1] = 3;	population[5][2] = 2;	population[5][3] = 1;	population[5][4] = 1;	
    	population[6][0] = 5;   population[6][1] = 3;	population[6][2] = 1;	population[6][3] = 1;	population[6][4] = 1;	
    	population[7][0] = 3;   population[7][1] = 2;	population[7][2] = 4;	population[7][3] = 5;	population[7][4] = 2;	
    	population[8][4] = 2;
    	population[9][4] = 1;
        
    	/*
    	arr[0] = 3;
    	arr[1] = 1;
    	arr[2] = 1;
    	arr[3] = 2;
    	arr[4] = 5;
    	arr[5] = 5;
    	arr[6] = 5;
    	arr[7] = 3;
    	arr[8] = 2;
    	arr[9] = 1;
    	
    	int pi [] = new int[2];
    	int_partition(arr, 0, 9, pi, 1);
    	System.out.println(pi[0]);
    	System.out.println(pi[1]);
    	printArray(arr);
    	*/
    	
        ob.sort(0, 0, n-1);
        System.out.println("Sorted array");
        printArray();
        
    }

}
