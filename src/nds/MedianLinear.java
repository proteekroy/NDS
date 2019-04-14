package nds;

import java.util.Arrays;

public class MedianLinear 
{
	AbstractNDS nds;
	
	
	public void setNDS(AbstractNDS arg_nds)
	{
		nds = arg_nds;
	}
	
	
	public double findMedian(double arr[], int low, int high)
    {
    	double median;
    	
    	if(high-low==0)
    	{
    		median = arr[low];
    	}
    	else if((high-low)==1)
    	{
    		if(arr[low]<=arr[high])
    			median =  arr[low];
    		else
    			median =  arr[high];
    	}
    	else
    	{
    		//if(arr.length==3)
    		{
    		//	System.out.println("Hello");
    		}
	        median = findMedianUtil(arr,(high-low)/2 + 1, low, high);
	        //System.out.println("Median = " + median);
    	}
    	if(nds.debug)
    	{
	    	System.out.println();
	    	for (int i = 0; i < arr.length; ++i) 
	        {
	            System.out.print(arr[i]+" ");
	        }
	    	System.out.println();
	    	System.out.print("median = "+median);
	    	System.out.println();
    	}
    	return median;
    }
	
    public double findMedian(double arr[])
    {
    	double median;
    	
    	
    	if(arr.length==1)
    	{
    		median = arr[0];
    	}
    	else if(arr.length==2)
    	{
    		if(arr[0]<=arr[1])
    			median =  arr[0];
    		else
    			median =  arr[1];
    	}
    	else
    	{
    		//if(arr.length==3)
    		{
    		//	System.out.println("Hello");
    		}
	        median = findMedianUtil(arr,(arr.length)/2 + 1,0,arr.length-1);
	        //System.out.println("Median = " + median);
    	}
    	if(nds.debug)
    	{
	    	System.out.println();
	    	for (int i = 0; i < arr.length; ++i) 
	        {
	            System.out.print(arr[i]+" ");
	        }
	    	System.out.println();
	    	System.out.print("median = "+median);
	    	System.out.println();
    	}
    	return median;
    }
    
    private double findMedianUtil(double arr[], int k, int low, int high)
    {
        // Uncomment this if you want to print the current subArray being processed/searched
        //printArray(arr, low, high);
        
        if(low == high)
        {
            return arr[low];
        }
        
        // sort the mth largest element in the given array
        int m = partition(arr, low, high);
        
        // Adjust position relative to the current subarray being processed
        int length = m - low + 1;
        
        // If mth element is the median, return it
        if(length == k)
        {
            return arr[m];
        }
        
        // If mth element is greater than median, search in the left subarray
        if(length > k)
        {
            return findMedianUtil(arr, k, low, m-1);
        }
        // otherwise search in the right subArray
        else
        {
            return findMedianUtil(arr, k-length, m+1, high);
        }
        
    }
    
    
    private int partition(double arr[],int low, int high)
    {
        // Get pivotvalue by finding median of medians
        double pivotValue = getPivotValue(arr, low, high);        
        
        // Find the sorted position for pivotVale and return it's index
        while(low < high)
        {
            while(arr[low] < pivotValue)
            {
                low ++;
            }
            
            while(arr[high] > pivotValue)
            {
                high--;
            }
            
            if(arr[low] == arr[high])
            {
                low ++;
            }
            else if(low < high)
            {
                double temp = arr[low];
                arr[low] = arr[high];
                arr[high] = temp;
            }
                
        }
        return high;
    }
    
    // Find pivot value, such the it is always 'closer' to the actual median
    private double getPivotValue(double arr[],int low,int high)
    {
        // If number of elements in the array are small, return the actual median
        if(high-low+1 <= 9)
        {
            Arrays.sort(arr);
            return arr[arr.length/2];
        }
        
        //Otherwise divide the array into subarray of 5 elements each, and recursively find the median
        
        // Array to hold '5 element' subArray, last subArray may have less than 5 elements
        double temp[] = null;
        
        // Array to hold the medians of all '5-element SubArrays'
        double medians[] = new double[(int)Math.ceil((double)(high-low+1)/5)];
        int medianIndex = 0;
        
        while(low <= high)
        {
            // get size of the next element, it can be less than 5
            temp = new double[Math.min(5,high-low+1)];
            
            // copy next 5 (or less) elements, in the subarray
            for(int j=0;j<temp.length && low <= high;j++)
            {
                temp[j] = arr[low];
                low++;
            }
            
            // sort subArray
            Arrays.sort(temp);
            
            // find mean and store it in median Array
            medians[medianIndex] = temp[temp.length/2];
            medianIndex++;
        }
        
        // Call recursively to find median of medians
        return getPivotValue(medians,0,medians.length-1);
    }
    
    public static void main(String args[])
    {
        int nElements = 100;
        double arr[] = new double[nElements];
        
        for(int i=0;i<nElements;i++)
        {
            arr[i] = (2000*Math.random());
        }
        
        System.out.print("Array");
        printArray(arr, 0, nElements);
        MedianLinear med = new MedianLinear();
        
        med.findMedian(arr);
        
        java.util.Arrays.sort(arr);
        System.out.print(" Sorted Array - ");
        printArray(arr, 0, nElements);
        System.out.println("Median using sorting - " + arr[(arr.length)/2]);
    }
    
     private static void printArray(double arr[],int low, int high)
    {
        System.out.print("[  ");
        for(int i=low;i<high;i++)
        {
            System.out.print(arr[i] + "  ");
            if(i%30==0)
            {
            	System.out.println();
            }
        }
        System.out.println("]");
    }
    
    
}