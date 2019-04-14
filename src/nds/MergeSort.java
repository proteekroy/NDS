
package nds;

public class MergeSort 
{
	public int obj;
	public int[] helper;
	public int[] order_obj;
	public int[] unique_solution_count;
	public int[] lex_order;
	public double[][] population;
	public int[][] allrank;
	
	public int arrayOfPoints[];
	public int index[];
	public double arrayOfValues[];
	private AbstractNDS nds_obj;
	
	int m1 = -1, m2 = -1;
	
	public void setValues(int[]help, int []lex, double[][] pop, int[][] all, AbstractNDS arg_nds_obj)
	{
		helper = help;
		lex_order = lex;
		population = pop;
		allrank = all;
		nds_obj = arg_nds_obj;
	}
	
	public void setlexorder(int []arg_lex_order)
	{
		lex_order = arg_lex_order;
	}
	
	public void setObj(int objective)
	{
		obj = objective;
	}
	
	public void setNDS(AbstractNDS arg_nds)
	{
		nds_obj = arg_nds;
	}
	
	public void setPop(double [][]arg_pop, int arg_m1, int arg_m2)
	{
		population = arg_pop;
		m1 = arg_m1;
		m2 = arg_m2;
	}
	
	public void setArray(int []points, double[] values, int [] arg_helper)
	{
		arrayOfPoints = points;
		arrayOfValues = values;
		helper = arg_helper;
	}
	
	/*public void setArrayIndex(int []points, int []arg_index, double[] values, int [] arg_helper)
	{
		arrayOfPoints = points;
		index = arg_index;
		arrayOfValues = values;
		helper = arg_helper;
	}*/
	
	public void mergesort_array(int low, int high) 
	{
		// check if low is smaller then high, if not then the array is sorted
		if (low < high) 
		{
		// Get the index of the element which is in the middle
			int middle = low + (high - low) / 2;
			// Sort the left side of the array
			mergesort_array(low, middle);
			// Sort the right side of the array
			mergesort_array(middle + 1, high);
			// Combine them both
			merge_array(low, middle, high);
		 }	  
	}
	
	public void mergesort_lex2d(int low, int high) 
	{
		// check if low is smaller then high, if not then the array is sorted
		if (low < high) 
		{
		// Get the index of the element which is in the middle
			int middle = low + (high - low) / 2;
			// Sort the left side of the array
			mergesort_lex2d(low, middle);
			// Sort the right side of the array
			mergesort_lex2d(middle + 1, high);
			// Combine them both
			merge_lex(low, middle, high);
		 }	  
	}
	
	public void merge_lex(int low, int middle, int high) 
	{

		// Copy both parts into the helper array
		for (int i = low; i <= high; i++) 
		{
			helper[i] = arrayOfPoints[i];
		}
		  
		int i = low;
		int j = middle + 1;
		int k = low;
		// Copy the smallest values from either the left or the right side back
		// to the original array
		while (i <= middle && j <= high) 
		{ 
			if (population[helper[i]][m1] < population[helper[j]][m1]) 
			{
				arrayOfPoints[k] = helper[i];
				i++;
			} 
			else if (population[helper[i]][m1] > population[helper[j]][m1]) 
			{
				arrayOfPoints[k] = helper[j];
				j++;
			}
			else //equal first objective value
			{
				if (population[helper[i]][m2] <= population[helper[j]][m2]) 
				{
					arrayOfPoints[k] = helper[i];
					i++;
				} 
				else
				{
					arrayOfPoints[k] = helper[j];
					j++;
				}
			}
			nds_obj.comparison_sort++;
			k++;
		}
		while(i<=middle)
		{
			arrayOfPoints[k] = helper[i];
			k++;
			i++;
		}
		while(j<=high)
		{
			arrayOfPoints[k] = helper[j];
			k++;
			j++;
		}

	}
	
	
	public void merge_array(int low, int middle, int high) 
	{

		// Copy both parts into the helper array
		for (int i = low; i <= high; i++) 
		{
			helper[i] = arrayOfPoints[i];
		}
		  
		int i = low;
		int j = middle + 1;
		int k = low;
		// Copy the smallest values from either the left or the right side back
		// to the original array
		while (i <= middle && j <= high) 
		{ 
			if (arrayOfValues[helper[i]] <= arrayOfValues[helper[j]]) 
			{
				arrayOfPoints[k] = helper[i];
				i++;
			} 
			else 
			{
				arrayOfPoints[k] = helper[j];
				j++;
			}
			nds_obj.comparison_sort++;
			k++;
		}
		while(i<=middle)
		{
			arrayOfPoints[k] = helper[i];
			k++;
			i++;
		}
		while(j<=high)
		{
			arrayOfPoints[k] = helper[j];
			k++;
			j++;
		}

	}
	
	/**
	 * Merge sort divide step
	 * @param low
	 * @param high
	 */
	public void mergesort(int low, int high) 
	{
		// check if low is smaller then high, if not then the array is sorted
		if (low < high) 
		{
		// Get the index of the element which is in the middle
			int middle = low + (high - low) / 2;
			// Sort the left side of the array
			mergesort(low, middle);
			// Sort the right side of the array
			mergesort(middle + 1, high);
			// Combine them both
			merge(low, middle, high);
		 }	  
	}
	
	/**
	 * Merge sort merging step
	 * @param low
	 * @param middle
	 * @param high
	 */
	public void merge(int low, int middle, int high) 
	{

		// Copy both parts into the helper array
		for (int i = low; i <= high; i++) 
		{
			helper[i] = allrank[obj][i];
		}
		  
		int i = low;
		int j = middle + 1;
		int k = low;
		// Copy the smallest values from either the left or the right side back
		// to the original array
		while (i <= middle && j <= high) 
		{ 
			if (population[helper[i]][obj] < population[helper[j]][obj]) 
			{
				allrank[obj][k] = helper[i];
				i++;
				nds_obj.comparison_sort++;
			} 
			else if(population[helper[i]][obj] > population[helper[j]][obj]) 
			{
				allrank[obj][k] = helper[j];
				j++;
				nds_obj.comparison_sort+=2;
			}
			else //two values are equal
			{
				if(obj==0)
				{
					if(nds_obj.lexicopgraphic_dominate(helper[i],helper[j]))
					{
						allrank[obj][k] = helper[i];
						i++;
					}
					else
					{
						allrank[obj][k] = helper[j];
						j++;
					}
				}
				else
				{
					
					if(lex_order[helper[i]]<lex_order[helper[j]])
					{
						allrank[obj][k] = helper[i];
						i++;
					}
					else
					{
						allrank[obj][k] = helper[j];
						j++;
					}
				}
			}
			k++;
		}
		while(i<=middle)
		{
			allrank[obj][k] = helper[i];
			k++;
			i++;
		}
		while(j<=high)
		{
			allrank[obj][k] = helper[j];
			k++;
			j++;
		}

	}
	
	

} 


