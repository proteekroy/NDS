package nds;

public class HeapSort {

	
	AbstractNDS  nds_obj;
	int left, right, smallest, largest;
	int [][] allrank;
	int [] lex_order;
	int obj;
	
	public void setObj(int arg_obj)
	{
		obj = arg_obj;
	}
	
	public void setAllrankObj(int[][] arg_allrank, int arg_obj, AbstractNDS arg_nds_obj)
	{
		allrank = arg_allrank;
		obj = arg_obj;
		nds_obj = arg_nds_obj;
	}
	public void setlexorder(int []arg_lex_order)
	{
		lex_order = arg_lex_order;
	}
	public void sort(int n)
	{
		int k;
		int n1 = n-1;

		buildheapMax(n1);
		
		while(n1>0)
		{	
			//exchange n1 and 0-th position
			k = allrank[obj][0];
			allrank[obj][0] = allrank[obj][n1];
			allrank[obj][n1] = k;
		
			n1--;
			siftdown_maxheap(0, n1);//find the place of the 0-th element
		}

	}

	public void buildheapMax(int size) //make max heap with n elements
	{
		for(int i=(int) (Math.ceil(size/2.0)-1);i>=0;i--)
		{
			siftdown_maxheap(i, size);
		}
	}
	
	public void buildheapMin(int size) //make max heap with n elements
	{
		for(int i=(int) (Math.ceil(size/2.0)-1);i>=0;i--)
		{
			siftdown_minheap(i, size);
		}
	}
	
//	public void siftdown_minheap(int root, int heapsize)
//	{
//		
//		
//		left = 2*root+1;
//		right = 2*root+2;
//		smallest = root;
//		
//		while(left <= heapsize)
//		{
//			nds_obj.comparison_sort++;
//			if(nds_obj.population[allrank[obj][left]][obj] < nds_obj.population[allrank[obj][root]][obj])
//			{
//				smallest = left;
//			}
//			else if(nds_obj.population[allrank[obj][root]][obj] == nds_obj.population[allrank[obj][left]][obj])
//			{
//				nds_obj.comparison_sort++;
//				/*if(!nds_obj.lexicopgraphic_dominate(allrank[obj][root], allrank[obj][left]))
//				{
//					smallest = left;
//				}*/
//				if(lex_order[allrank[obj][left]] < lex_order[allrank[obj][root]])
//				{
//					smallest = left;
//				}
//			}
//			
//			if(right <= heapsize)
//			{
//				nds_obj.comparison_sort++;
//				if (nds_obj.population[allrank[obj][right]][obj] < nds_obj.population[allrank[obj][smallest]][obj])
//				{
//					smallest=right;
//				}
//				else if(nds_obj.population[allrank[obj][right]][obj] == nds_obj.population[allrank[obj][smallest]][obj])
//				{
//					nds_obj.comparison_sort++;
//					/*if(nds_obj.lexicopgraphic_dominate(allrank[obj][right],allrank[obj][smallest]))
//					{
//						smallest=right;
//					}*/
//					if(lex_order[allrank[obj][right]]< lex_order[allrank[obj][smallest]])
//					{
//						smallest = right;
//					}
//				}
//				
//			}
//			if(smallest!=root)
//			{
//				int k = allrank[obj][root];
//				allrank[obj][root] = allrank[obj][smallest];
//				allrank[obj][smallest] = k;	
//				root = smallest;
//				left = 2*root+1;
//				right = 2*root+2;
//			}
//			else
//				break;
//		}		
//		
//	}
	public void siftdown_minheap(int root, int heapsize)
	{
		left = 2*root+1;
		right = 2*root+2;
		smallest = root;
		
		while(left <= heapsize)
		{
			if(right <= heapsize)
			{
				if(nds_obj.population[allrank[obj][left]][obj] < nds_obj.population[allrank[obj][right]][obj])
				{
					if(nds_obj.population[allrank[obj][left]][obj] < nds_obj.population[allrank[obj][root]][obj])
					{
						//nds_obj.comparison_sort = nds_obj.comparison_sort+2;
						int k = allrank[obj][root];
						allrank[obj][root] = allrank[obj][left];
						allrank[obj][left] = k;	
						root = left;
						left = 2*root+1;
						right = 2*root+2;
					}
					else if(nds_obj.population[allrank[obj][left]][obj] == nds_obj.population[allrank[obj][root]][obj])
					{
						if(lex_order[allrank[obj][left]] < lex_order[allrank[obj][root]])
						{
							//nds_obj.comparison_sort = nds_obj.comparison_sort+3;
							int k = allrank[obj][root];
							allrank[obj][root] = allrank[obj][left];
							allrank[obj][left] = k;	
							root = left;
							left = 2*root+1;
							right = 2*root+2;
						}
						else
							break;
					}
					else
						break;
				}
				else if(nds_obj.population[allrank[obj][left]][obj] == nds_obj.population[allrank[obj][right]][obj])
				{
					if(lex_order[allrank[obj][left]] < lex_order[allrank[obj][right]])
					{
						if(nds_obj.population[allrank[obj][left]][obj] < nds_obj.population[allrank[obj][root]][obj])
						{
							//nds_obj.comparison_sort = nds_obj.comparison_sort+4;
							int k = allrank[obj][root];
							allrank[obj][root] = allrank[obj][left];
							allrank[obj][left] = k;	
							root = left;
							left = 2*root+1;
							right = 2*root+2;
						}
						else if(nds_obj.population[allrank[obj][left]][obj] == nds_obj.population[allrank[obj][root]][obj])
						{
							if(lex_order[allrank[obj][left]] < lex_order[allrank[obj][root]])
							{
								//nds_obj.comparison_sort = nds_obj.comparison_sort+4;
								int k = allrank[obj][root];
								allrank[obj][root] = allrank[obj][left];
								allrank[obj][left] = k;	
								root = left;
								left = 2*root+1;
								right = 2*root+2;
							}
							else
								break;
						}
						else
							break;
					}
					else
					{
						if(nds_obj.population[allrank[obj][right]][obj] < nds_obj.population[allrank[obj][root]][obj])
						{
							//nds_obj.comparison_sort = nds_obj.comparison_sort+3;
							int k = allrank[obj][root];
							allrank[obj][root] = allrank[obj][right];
							allrank[obj][right] = k;	
							root = right;
							left = 2*root+1;
							right = 2*root+2;
						}
						else if(nds_obj.population[allrank[obj][right]][obj] == nds_obj.population[allrank[obj][root]][obj])
						{
							if(lex_order[allrank[obj][right]] < lex_order[allrank[obj][root]])
							{
								//nds_obj.comparison_sort = nds_obj.comparison_sort+5;
								int k = allrank[obj][root];
								allrank[obj][root] = allrank[obj][right];
								allrank[obj][right] = k;	
								root = right;
								left = 2*root+1;
								right = 2*root+2;
							}
							else
								break;
						}
						else
							break;
					}
				}
				else //right smaller
				{
					if(nds_obj.population[allrank[obj][right]][obj] < nds_obj.population[allrank[obj][root]][obj])
					{
						//nds_obj.comparison_sort = nds_obj.comparison_sort+2;
						int k = allrank[obj][root];
						allrank[obj][root] = allrank[obj][right];
						allrank[obj][right] = k;	
						root = right;
						left = 2*root+1;
						right = 2*root+2;
					}
					else if(nds_obj.population[allrank[obj][right]][obj] == nds_obj.population[allrank[obj][root]][obj])
					{
						if(lex_order[allrank[obj][right]] < lex_order[allrank[obj][root]])
						{
							//nds_obj.comparison_sort = nds_obj.comparison_sort+4;
							int k = allrank[obj][root];
							allrank[obj][root] = allrank[obj][right];
							allrank[obj][right] = k;	
							root = right;
							left = 2*root+1;
							right = 2*root+2;
						}
						else
							break;
					}
					else
						break;
				}
			}
			else
			{
				if(nds_obj.population[allrank[obj][left]][obj] < nds_obj.population[allrank[obj][root]][obj])
				{
					//nds_obj.comparison_sort = nds_obj.comparison_sort+2;
					int k = allrank[obj][root];
					allrank[obj][root] = allrank[obj][left];
					allrank[obj][left] = k;	
					root = left;
					left = 2*root+1;
					right = 2*root+2;
				}
				else if(nds_obj.population[allrank[obj][left]][obj] == nds_obj.population[allrank[obj][root]][obj])
				{
					if(lex_order[allrank[obj][left]] < lex_order[allrank[obj][root]])
					{
						//nds_obj.comparison_sort = nds_obj.comparison_sort+3;
						int k = allrank[obj][root];
						allrank[obj][root] = allrank[obj][left];
						allrank[obj][left] = k;	
						root = left;
						left = 2*root+1;
						right = 2*root+2;
					}
					else
						break;
				}
				else
					break;
			}
		}		
		
	}
	
	
	public void siftdown_maxheap(int root, int heapsize)
	{
		
		int k;
		left = 2*root+1;
		right = 2*root+2;
		largest = root;
		
		while(left <= heapsize)
		{
			nds_obj.comparison_sort++;
			if(nds_obj.population[allrank[obj][left]][obj] > nds_obj.population[allrank[obj][root]][obj])
			{
				largest = left;
			}
			else if(nds_obj.population[allrank[obj][root]][obj] == nds_obj.population[allrank[obj][left]][obj])
			{
				nds_obj.comparison_sort++;
				if(nds_obj.lexicopgraphic_dominate(allrank[obj][root], allrank[obj][left]))
				{
					largest = left;
				}
			}
			if(right <= heapsize)
			{
				nds_obj.comparison_sort++;
				if (nds_obj.population[allrank[obj][right]][obj] > nds_obj.population[allrank[obj][largest]][obj])
				{
					largest = right;
				}
				else if(nds_obj.population[allrank[obj][right]][obj] == nds_obj.population[allrank[obj][largest]][obj])
				{
					nds_obj.comparison_sort++;
					if(nds_obj.lexicopgraphic_dominate(allrank[obj][largest], allrank[obj][right]))
					{
						largest = right;
					}				
				}
			}
			if(largest!=root)
			{
				k = allrank[obj][root];
				allrank[obj][root] = allrank[obj][largest];
				allrank[obj][largest] = k;	
				root = largest;
				left = 2*root+1;
				right = 2*root+2;
			}
			else
				break;
		}		
		
	}
	
	public static void main(String [] args)
	{
		HeapSort heapsort = new HeapSort();
				
		BestOrderSort bos  = new BestOrderSort(10, 2);
		heapsort.nds_obj.population = new double[][]{{5,1},{8,1},{7,1},{7,1},{6,1},{1,1},{4,1},{6,1},{5,1},{3,1}};
		int [][] allrank = new int[][]{{0,1,2,3,4,5,6,7,8,9}};
		heapsort.setAllrankObj(allrank, 0, bos);
		heapsort.sort(10);
		
		for(int i=0;i<heapsort.allrank[0].length;i++)
    	{
    		System.out.print(heapsort.nds_obj.population[allrank[0][i]][0]+" ");
    	}
	}
}
