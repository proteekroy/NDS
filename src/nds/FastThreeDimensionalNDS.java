package nds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;



public class FastThreeDimensionalNDS extends AbstractNDS {
	
	int m1 = -1;
	int [][]allrank;
	LinkedList []F;
	int index[];//two dimension
	int []lex_order;
	int []temp_rank;
	//LinkedList P;
	
	private final int[] indices;
    //private final int[] swap;
    private final int[] eqComp;
    private final MergeSorter sorter;
    private double[][] input;
    private int[] output;
    MedianLinear med;
	Random random;
	int comparatorObj1, comparatorObj2;
	
	private final TreeSet<Integer> set = new TreeSet<>(new Comparator<Integer>() {
        public int compare(Integer lhs, Integer rhs) {
            int ilhs = lhs, irhs = rhs;
            comparison_rank++;
            int cmp1 = Double.compare(input[ilhs][comparatorObj2], input[irhs][comparatorObj2]);
            if (cmp1 != 0) {
                return cmp1;
            } else {
                if(comparatorObj1!=-1)
            	{
                	return Double.compare(input[ilhs][comparatorObj1], input[irhs][comparatorObj1]);
            	}
                else
                {
                	return cmp1;
                }
            }
        }
    });
	
	public FastThreeDimensionalNDS(int arg_n, int arg_m)
	{
		n = arg_n;
		m = arg_m;
		indices = new int[n];
        eqComp = new int[n];
        //swap = new int[n];
        sorter = new MergeSorter(n);
        rank = new int[n];
		random = new Random();
        med = new MedianLinear();
        comparatorObj1 = -1;
        comparatorObj2 = -1;
        med.setNDS(this);
        mergesort = new MergeSort();
        mergesort.setNDS(this);
	}
	
	@Override
	public void initialize(double[][] pop)
	{
		if(printinfo)
		{
			System.out.println("\nStarting Improved DivideAndConquer Sort");
		}
		population = pop;
		totalfront = 0;
		comparison = 0;
		comparison_sort = 0;
		comparison_rank = 0;
		time = 0;
		for(int i=0;i<n;i++)
		{
			rank[i] = 0;
		}
		input = pop;
        output = rank;
	}
	
	public void sort(double [][] pop) 
	{
		int i;
		//LinkedList P1 = new LinkedList();
		//LinkedList P2 = new LinkedList();
		LinkedList P = new LinkedList();
		LinkedList P1 = new LinkedList();
		LinkedList P2 = new LinkedList();
		
		ArrayList<Integer> objlist = new  ArrayList<Integer>();
		initialize(pop);
		start = System.nanoTime();//starting timer
		endTime2 = start;
        for (i = 0; i < n; i++) 
        {
            indices[i] = i;
        }
        for (i = 0; i < m; i++) 
        {
        	objlist.add(i);
        }
        Arrays.fill(output, 0);
        sorter.lexSort(indices, 0, n, input, eqComp);
        
        for (i = 0; i < n; ++i) 
        {
            P.add(indices[i]);
        }

        NDHelperA(P, objlist);
        /*
        for (i = 0; i < n/2; i++) 
        {
            P1.add(indices[i]);
        }
        
        for (i = n/2; i < n; i++) 
        {
            P2.add(indices[i]);
        }
        sweepA(P1, 0, 1);
        sweepB(P1, P2, 0, 1);//(0, n/2, n/2, n);
        sweepA(P2, 0, 1);//(n/2, n);
        */
        end = System.nanoTime();
        rank = output;
        this.input = null;
        this.output = null;
		mini_stat();
		if(printinfo)
		{
			printInformation();
		}
    }
	
	void printList(LinkedList p)
	{
		Node head = p.start;
		while(head!=null)
		{
			System.out.print(head.data+" ");
			head = head.link;
		}
		System.out.println();
	}

	
	private void updateFront(int target, int source) {
    	comparison_rank++;
        if (eqComp[target] == eqComp[source]) {
            output[target] = output[source];
        } else {
            output[target] = Math.max(output[target], output[source] + 1);
        }
        //output[target] = Math.max(output[target], output[source] + 1);
    }

    private void cleanup(int curr) {
        Iterator<Integer> greaterIterator = set.tailSet(curr, true).iterator();
        while (greaterIterator.hasNext()) {
            if (output[greaterIterator.next()] <= output[curr]) {
                greaterIterator.remove();
            } else {
                break;
            }
        }
    }

    //sweep A algorithm
    private void sweepA(LinkedList P, int m1, int m2) {
    	if(debug)
    	{
    		System.out.println("Inside sweepA, List P:");
    		printList(P);
    	}
        Node head = P.start;
        comparatorObj1 = m1;
        comparatorObj2 = m2;
        
        while(head!=null)
        {
            int curr = head.data;//indices[i];
            Iterator<Integer> lessIterator = set.headSet(curr, true).descendingIterator();
            if (lessIterator.hasNext()) {
                updateFront(curr, lessIterator.next());
            }
            cleanup(curr); 
            set.add(curr);
            head = head.link;
        }
        set.clear();
    }
    
    
	void NDHelperA(LinkedList P, ArrayList<Integer> objlist)
	{
		if(debug)
    	{
			System.out.println("Inside NDHelperA, List P:");
    	}
		if(P.size<2)
		{
			return;
		}
		else if (P.size==2)
		{
			if(dominates(P.start.data, P.start.link.data))
			{
				updateFront(P.start.link.data, P.start.data);
			}
		}
		else if(objlist.size()==2)
		{
			sweepA(P, objlist.get(0), objlist.get(1));
		}
		else if(objlist.size()>2)//m==3 for initial test
		{
			LinkedList P1 = new LinkedList();
			LinkedList P2 = new LinkedList();
			LinkedList P3 = new LinkedList();
			LinkedList P4 = new LinkedList();
			LinkedList M12 = new LinkedList();
			LinkedList M13 = new LinkedList();
			LinkedList M24 = new LinkedList();
			LinkedList M34 = new LinkedList();
			LinkedList M14 = new LinkedList();
			
			splitA(P, P1, P2, P3, P4, M12, M13, M24, M34, M14, objlist.get(objlist.size()-1), objlist.get(objlist.size()-2));
						
			ArrayList<Integer> objlist_copyfor12 = new ArrayList<Integer>();
			ArrayList<Integer> objlist_copyfor13 = new ArrayList<Integer>();
			ArrayList<Integer> objlist_copyfor14 = new ArrayList<Integer>();
			
			objlistHelper(objlist, objlist_copyfor12, objlist_copyfor13, objlist_copyfor14);
			
			NDHelperA(P1, objlist);
			NDHelperB(P1, M12, objlist_copyfor12);	
			NDHelperB(P1, M13, objlist_copyfor13);	
			NDHelperB(P1, M14, objlist_copyfor14);	
			NDHelperB(P1, M24, objlist_copyfor14);
			NDHelperB(P1, M34, objlist_copyfor14);
			
			NDHelperA(M12, objlist_copyfor12);
			NDHelperB(M12, M34, objlist_copyfor14);	
			NDHelperB(M12, M24, objlist_copyfor14);
			NDHelperB(M12, M14, objlist_copyfor14);
			
			NDHelperA(M13, objlist_copyfor13);
			NDHelperB(M13, M34, objlist_copyfor14);	
			NDHelperB(M13, M24, objlist_copyfor14);
			NDHelperB(M13, M14, objlist_copyfor14);
			
			NDHelperB(addToFirstList(P1, M12), P2, objlist_copyfor12);
			NDHelperA(P2, objlist);
			
			
			NDHelperB(addToFirstList(P1, M13), P3, objlist_copyfor13);
			NDHelperA(P3, objlist);
			

			NDHelperA(M14, objlist_copyfor14);
			NDHelperB(addToFirstList(addToFirstList(addToFirstList(P1, M14), M13), M12), P4, objlist_copyfor14);
			
			
			NDHelperB(P2, M24, objlist_copyfor13);	
			NDHelperA(M24, objlist_copyfor13);
			NDHelperB(addToFirstList(P2, M24), P4, objlist_copyfor13);
			
			
			
			NDHelperB(P3, M34, objlist_copyfor12);	
			NDHelperA(M34, objlist_copyfor12);
			NDHelperB(addToFirstList(P3, M34), P4, objlist_copyfor12);
			
			NDHelperA(P4, objlist);
			
			if(P1.size>(1+P.size/2)||P2.size>(1+P.size/2)||P3.size>(1+P.size/2)||P4.size>(1+P.size/2)||M12.size>(1+P.size/2)||
					M13.size>(1+P.size/2)||M24.size>(1+P.size/2)||M34.size>(1+P.size/2)||M14.size>(1+P.size/2))
			{
				try {
					throw new Exception();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
	}
	
	void NDHelperB(LinkedList p1, LinkedList p2, ArrayList<Integer> objlist)
	{
		if(debug)
    	{
			System.out.println("Inside NDHelperB");
			printList(p1);
			printList(p2);
    	}
		//We know the ranks of P1
		if(p1.size==0 || p2.size==0)
		{
			return;
		}
		else if(p1.size==1 || p2.size==1)
		{
			Node head;
			if(p1.size==1)
			{
				head = p2.start;
				while(head!=null)
				{
					if(dominates(p1.start.data, head.data))
					{
						updateFront(head.data, p1.start.data);
					}
					head = head.link;
				}
			}
			else
			{
				head = p1.start;
				while(head!=null)
				{
					if(dominates(head.data, p2.start.data))
					{
						updateFront(p2.start.data, head.data);
					}
					head = head.link;
				}
			}
		}
		else if(objlist.size()==1)
		{
			sweepB1D(p1, p2, objlist.get(0));
		}
		else if(objlist.size()==2)
		{
			sweepB(p1, p2, objlist.get(0), objlist.get(1));
		}
		else
		{
			LinkedList l1, l2, l3, l4, h1, h2, h3, h4;
			l1 = new LinkedList();
			l2 = new LinkedList();
			l3 = new LinkedList();
			l4 = new LinkedList();
			h1 = new LinkedList();
			h2 = new LinkedList();
			h3 = new LinkedList();
			h4 = new LinkedList();
			LinkedList M12l = new LinkedList();
			LinkedList M13l = new LinkedList();
			LinkedList M24l = new LinkedList();
			LinkedList M34l = new LinkedList();
			LinkedList M14l = new LinkedList();
			LinkedList M12h = new LinkedList();
			LinkedList M13h = new LinkedList();
			LinkedList M24h = new LinkedList();
			LinkedList M34h = new LinkedList();
			LinkedList M14h = new LinkedList();
			
			splitB(p1, p2, l1, l2, l3, l4, h1, h2, h3, h4, 
					 M12l, M13l, M24l, M34l, M14l,
					 M12h, M13h, M24h, M34h, M14h,
					 objlist.get(objlist.size()-1), objlist.get(objlist.size()-2));
			
			ArrayList<Integer> objlist_copyfor12 = new ArrayList<Integer>();
			ArrayList<Integer> objlist_copyfor13 = new ArrayList<Integer>();
			ArrayList<Integer> objlist_copyfor14 = new ArrayList<Integer>();
			
			objlistHelper(objlist, objlist_copyfor12, objlist_copyfor13, objlist_copyfor14);
			
			
			NDHelperB(l1, h1, objlist);
			
			NDHelperB(l1, M12h, objlist_copyfor12);
			NDHelperB(l1, M13h, objlist_copyfor13);
			NDHelperB(l1, M14h, objlist_copyfor14);
			NDHelperB(l1, M24h, objlist_copyfor14);
			NDHelperB(l1, M34h, objlist_copyfor14);

			NDHelperB(M12l, M24h, objlist_copyfor14);
			NDHelperB(M12l, M34h, objlist_copyfor14);
			NDHelperB(M12l, M14h, objlist_copyfor14);
			
			NDHelperB(M13l, M24h, objlist_copyfor14);
			NDHelperB(M13l, M34h, objlist_copyfor14);
			NDHelperB(M13l, M14h, objlist_copyfor14);
			
			NDHelperB(addToFirstList(l1, M12l), h2, objlist_copyfor12);
			NDHelperB(addToFirstList(l1, M13l), h3, objlist_copyfor13);
			NDHelperB(addToFirstList(addToFirstList(addToFirstList(l1, M14l), M12l), M13l), h4, objlist_copyfor14);
			
			NDHelperB(l2, h2, objlist);
			NDHelperB(l2, M24h, objlist_copyfor13);
			NDHelperB(M24l, M24h, objlist_copyfor13);
			NDHelperB(addToFirstList(l2, M24l), h4, objlist_copyfor13);
			
			
			NDHelperB(l3, h3, objlist);
			NDHelperB(l3, M34h, objlist_copyfor12);
			NDHelperB(M34l, M34h, objlist_copyfor12);
			NDHelperB(addToFirstList(l3, M34l), h4, objlist_copyfor12);
			
			
			NDHelperB(l4, h4, objlist);
			
			if(l1.size>(1+p1.size/2)||l2.size>(1+p1.size/2)||l3.size>(1+p1.size/2)||l4.size>(1+p1.size))
			{
				try {
					throw new Exception();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	LinkedList addToFirstList(LinkedList p1, LinkedList p2)
	{
		Node head;
		LinkedList p = new LinkedList();
		
		head = p1.start;
		while(head!=null)
		{
			p.add(head.data);
			head = head.link;
		}
		
		
		head = p2.start;
		while(head!=null)
		{
			p.add(head.data);
			head = head.link;
		}
		return p;
	}
	
	void objlistHelper(ArrayList<Integer> objlist, ArrayList<Integer> objlist_copyfor12, ArrayList<Integer> objlist_copyfor13, ArrayList<Integer> objlist_copyfor14)
	{
		for(Integer p : objlist) 
		{
		    objlist_copyfor12.add(p.intValue());
		    objlist_copyfor13.add(p.intValue());
		    objlist_copyfor14.add(p.intValue());
		}
		objlist_copyfor12.remove(objlist_copyfor12.size()-2);//P1 is better than P2 in secondlast objective
		objlist_copyfor13.remove(objlist_copyfor13.size()-1);//P1 is better than P3 in last objective
		objlist_copyfor14.remove(objlist_copyfor14.size()-1);//P1 is better than P4 in both
		objlist_copyfor14.remove(objlist_copyfor14.size()-1);// last and second last objective. If one removed, other becomes last
		
	}
	
	void splitA(LinkedList p, LinkedList p1, LinkedList p2, LinkedList p3, LinkedList p4,
			LinkedList M12, LinkedList M13, LinkedList M24, LinkedList M34, LinkedList M14, 
			 int m1, int m2)
	{
		int i = 0;
		double median1, median2;
		double []values1 = new double[p.size];
		double []values2 = new double[p.size];
		Node head;
		head = p.start;
		while(head!=null)
		{
			values1[i] = population[head.data][m1];
			values2[i] = population[head.data][m2];
			head = head.link;
			i++;
		}
		
		median1 = med.findMedian(values1);
		median2 = med.findMedian(values2);
		
		
		splitHelper(p, p1, p2, p3, p4, M12, M13, M24, M34, M14, median1, median2, m1, m2);
		
		if(debug)
    	{
			System.out.println("Inside split A");
			printList(p);
			System.out.println("median1 = "+median1+", median2 = "+median2);
			printList(p1);
			printList(p2);
			printList(p3);
			printList(p4);
			printList(M12);
			printList(M13);
			printList(M24);
			printList(M34);
			printList(M14);
    	}
	}
	
	private void splitB(LinkedList p1, LinkedList p2, LinkedList l1, LinkedList l2, LinkedList l3, LinkedList l4,
			LinkedList h1, LinkedList h2, LinkedList h3, LinkedList h4, 
			LinkedList M12l, LinkedList M13l, LinkedList M24l, LinkedList M34l, LinkedList M14l,
			LinkedList M12h, LinkedList M13h, LinkedList M24h, LinkedList M34h, LinkedList M14h,
			int m1, int m2) 
	{
		
		int i = 0;
		double median1, median2;
		double []values1 = new double[p1.size];
		double []values2 = new double[p1.size];
		Node head;
		head = p1.start;
		while(head!=null)
		{
			values1[i] = population[head.data][m1];
			values2[i] = population[head.data][m2];
			head = head.link;
			i++;
		}
		median1 = med.findMedian(values1);
		median2 = med.findMedian(values2);
		
		//split p1 and p2 based one median values of p1 
		splitHelper(p1, l1, l2, l3, l4, M12l, M13l, M24l, M34l, M14l, median1, median2, m1, m2);
		splitHelper(p2, h1, h2, h3, h4, M12h, M13h, M24h, M34h, M14h, median1, median2, m1, m2);
		if(debug)
    	{
			System.out.println("Inside splitB");
			printList(p1);
			System.out.println("median1 = "+median1+", median2 = "+median2);
			printList(l1);
			printList(l2);
			printList(l3);
			printList(l4);
			printList(M12l);
			printList(M13l);
			printList(M24l);
			printList(M34l);
			printList(M14l);
			
			
			printList(p2);
			printList(h1);
			printList(h2);
			printList(h3);
			printList(h4);
			printList(M12h);
			printList(M13h);
			printList(M24h);
			printList(M34h);
			printList(M14h);
    	}
		
	}
	
	void splitHelper(LinkedList p, LinkedList p1, LinkedList p2, LinkedList p3, LinkedList p4,
			LinkedList M12, LinkedList M13, LinkedList M24, LinkedList M34, LinkedList M14, 			
			double median1, double median2, int m1, int m2)
	{
		int s;
		Node head = p.start;
		while(head!=null)
		{
			s = head.data;
			if(input[s][m1]<median1)
			{
				if(input[s][m2]<median2)
				{
					p1.add(s);
				}
				else if (input[s][m2]>median2)
				{
					p2.add(s);
				}
				else
				{
					M12.add(s);
				}
			}
			else if(input[s][m1]>median1)
			{
				if(input[s][m2]<median2)
				{
					p3.add(s);
				}
				else if(input[s][m2]>median2)
				{
					p4.add(s);
				}
				else
				{
					M34.add(s);
				}
			}
			else //they are equal to median1
			{
				if(input[s][m2]<median2)
				{
					M13.add(s);
				}
				else if (input[s][m2]>median2)
				{
					M24.add(s);
				}
				else
				{
					M14.add(s);
				}
			}
			head = head.link;
		}	
	}
	
	
	private void sweepB(LinkedList P1, LinkedList P2, int m1, int m2)//(int lFrom, int lUntil, int hFrom, int hUntil) 
    {
    	if(debug)
    	{
    		System.out.println("Inside sweepB, m1 = "+m1+ ", m2 = "+m2+ ", List P1:");
    		printList(P1);
    		System.out.println("List P2:");
    		printList(P2);
    	}
    	Node head1, head2;
    	comparatorObj1 = m1;
    	comparatorObj2 = m2;
    	
    	
    	int i, j;
		int[] indices1 = new int[P1.size];
		//int[] tempindices1 = new int[P1.size];
		//int[] indexOfindices1 = new int[P1.size];
		int[] helper1 = new int[P1.size];
		
		int[] indices2 = new int[P2.size];
		//int[] tempindices2 = new int[P2.size];
		//int[] indexOfindices2 = new int[P2.size];
		int[] helper2 = new int[P2.size];
		
		//for(i=0;i<indices1.length;i++){indexOfindices1[i] = i;}
		//for(i=0;i<indices2.length;i++){indexOfindices2[i] = i;}
		
		
		head1 = P1.start;
		i = 0;
		while(head1!=null)
		{
			indices1[i] = head1.data;
			head1 = head1.link;
			i++;
		}
		i=0;
		head2 = P2.start;
		while(head2!=null)
		{
			indices2[i] = head2.data;
			head2 = head2.link;
			i++;
		}
	
		mergesort.setPop(input, m1, m2);//set pop and 2 objectives
		mergesort.setArray(indices1, null, helper1);
		mergesort.mergesort_lex2d(0, indices1.length-1);
		
		mergesort.setArray(indices2, null, helper2);
		mergesort.mergesort_lex2d(0, indices2.length-1);
		
		//for(i=0;i<indices1.length;i++){tempindices1[i] = indices1[indexOfindices1[i]];}
		//for(i=0;i<indices2.length;i++){tempindices2[i] = indices2[indexOfindices2[i]];}
    	
		
		i = 0;
		j = 0;
		
		while(j<indices2.length)
		{
			while(i<indices1.length)
			{
				if(input[indices1[i]][m1]<input[indices2[j]][m1] || (input[indices1[i]][m1]==input[indices2[j]][m1] && input[indices1[i]][m2]<input[indices2[j]][m2]))//Check this for equality
	            { 
					Iterator<Integer> lessIterator = set.headSet(indices1[i], true).descendingIterator();
	                if (!lessIterator.hasNext() || output[lessIterator.next()] < output[indices1[i]]) {
	                    cleanup(indices1[i]);
	                    set.add(indices1[i]);
	                }
	                i++;
	            }
				else
				{
					//i++;
					break;
				}
			}
			Iterator<Integer> lessIterator = set.headSet(indices2[j], true).descendingIterator();
            if (lessIterator.hasNext()) {
            	int source = lessIterator.next();
                updateFront(indices2[j], source);
            }
			j++;
		}
		set.clear();
		
    	/*
    	head1 = P1.start;
    	head2 = P2.start;
    	
    	while(head2!=null)
        {
            int currH = head2.data;//indices[hi];
            //int eCurrH = eqComp[currH];
            //while (li < lUntil && eqComp[indices[li]] < eCurrH) 
            while(head1!=null && (input[head1.data][m1]< input[head2.data][m1] || (input[head1.data][m1]== input[head2.data][m1] && input[head1.data][m2]< input[head2.data][m2]) ) )//&& eqComp[head1.data] < eCurrH)
            //while(head1!=null && eqComp[head1.data] < eCurrH)
            {
                int curr = head1.data;//indices[li++];
                Iterator<Integer> lessIterator = set.headSet(curr, true).descendingIterator();
                if (!lessIterator.hasNext() || output[lessIterator.next()] < output[curr]) {
                    cleanup(curr);
                    set.add(curr);
                }
                head1 = head1.link;
            }
            Iterator<Integer> lessIterator = set.headSet(currH, true).descendingIterator();
            if (lessIterator.hasNext()) {
            	int source = lessIterator.next();
                updateFront(currH, source);
            }
            head2 = head2.link;
        }
        set.clear();
        */
    }

	void sweepB1D(LinkedList p1, LinkedList p2, int m)
	{
		if(debug)
    	{
			System.out.println("Inside sweep1D, m = "+m);
			printList(p1);
			printList(p2);
    	}
		int i, j;
		int[] indices1 = new int[p1.size];
		int[] tempindices1 = new int[p1.size];
		int[] indexOfindices1 = new int[p1.size];
		int[] helper1 = new int[p1.size];
		double []values1 = new double[p1.size];
		
		int[] indices2 = new int[p2.size];
		int[] tempindices2 = new int[p2.size];
		int[] indexOfindices2 = new int[p2.size];
		int[] helper2 = new int[p2.size];
		double []values2 = new double[p2.size];
		
		for(i=0;i<indices1.length;i++){indexOfindices1[i] = i;}
		for(i=0;i<indices2.length;i++){indexOfindices2[i] = i;}
		
		
		Node head = p1.start;
		i = 0;
		while(head!=null)
		{
			indices1[i] = head.data;
			values1[i] = input[head.data][m];
			head = head.link;
			i++;
		}
		i=0;
		head = p2.start;
		while(head!=null)
		{
			indices2[i] = head.data;
			values2[i] = input[head.data][m];
			head = head.link;
			i++;
		}
		
		mergesort.setArray(indexOfindices1, values1, helper1);
		mergesort.mergesort_array(0, indexOfindices1.length-1);
		
		mergesort.setArray(indexOfindices2, values2, helper2);
		mergesort.mergesort_array(0, indexOfindices2.length-1);
		
		for(i=0;i<indices1.length;i++){tempindices1[i] = indices1[indexOfindices1[i]];}
		for(i=0;i<indices2.length;i++){tempindices2[i] = indices2[indexOfindices2[i]];}
		
		i = 0;
		j = 0;
		int sol = -1;
		
		while(i<tempindices1.length && j<tempindices2.length)
		{
			if(input[tempindices1[i]][m]<input[tempindices2[j]][m])//Check this for equality
            { 
       			if(sol==-1 || output[tempindices1[i]]>output[sol])
       			{
       				sol = tempindices1[i];
       			}
       			i++;
            }
       		else
       		{
       			if(sol!=-1)
       			{
       				updateFront(tempindices2[j], sol);
       			}
        		j++;
        	}
			
		}
		while(j<tempindices2.length)
		{
			if(sol!=-1)
   			{
   				updateFront(tempindices2[j], sol);
   			}
			j++;
		}
               	
            //int eCurrH = eqComp[currH];
            //while(head1!=null && eqComp[head1.data] < eCurrH)
            /*while(head1!=null)//already ranked solutions
            {
                if(input[head1.data][m]<input[head2.data][m])//Check this for equality
                {
                	if(sol==-1 || output[head2.data]>output[sol])
                	{
                		sol = head2.data;
                	}
                	updateFront(head2.data, head1.data);
                	//cleanup(head1.data);
                    //set.add(head1.data);
                }
                head1 = head1.link;
            }
            head2 = head2.link;
             //set.clear()
            */;
	}
	
	
	private class MergeSorter {
        final int[] scratch;
        int[] indices = null;
        int secondIndex = -1;
        double[][] reference = null;
        int[] eqComp = null;

        public MergeSorter(int size) {
            this.scratch = new int[size];
        }

        public void lexSort(int[] indices, int from, int until, double[][] reference, int[] eqComp) {
            this.indices = indices;
            this.reference = reference;
            this.eqComp = eqComp;
            lexSortImpl(from, until, 0, 0);
            this.eqComp = null;
            this.reference = null;
            this.indices = null;
        }

        private int lexSortImpl(int from, int until, int currIndex, int compSoFar) {
            if (from + 1 < until) {
                secondIndex = currIndex;
                sortImpl(from, until);
                secondIndex = -1;

                if (currIndex + 1 == reference[0].length) {
                    eqComp[indices[from]] = compSoFar;
                    for (int i = from + 1; i < until; ++i) {
                        int prev = indices[i - 1], curr = indices[i];
                        if (reference[prev][currIndex] != reference[curr][currIndex]) {
                            ++compSoFar;
                        }
                        eqComp[curr] = compSoFar;
                    }
                    return compSoFar + 1;
                } else {
                    int lastIndex = from;
                    for (int i = from + 1; i < until; ++i) {
                        if (reference[indices[lastIndex]][currIndex] != reference[indices[i]][currIndex]) {
                            compSoFar = lexSortImpl(lastIndex, i, currIndex + 1, compSoFar);
                            lastIndex = i;
                        }
                    }
                    return lexSortImpl(lastIndex, until, currIndex + 1, compSoFar);
                }
            } else {
                eqComp[indices[from]] = compSoFar;
                return compSoFar + 1;
            }
        }

        private void sortImpl(int from, int until) {
            if (from + 1 < until) {
                int mid = (from + until) >>> 1;
                sortImpl(from, mid);
                sortImpl(mid, until);
                int i = from, j = mid, k = 0, kMax = until - from;
                while (k < kMax) {
                	comparison_rank++;
                    if (i == mid || j < until && reference[indices[j]][secondIndex] < reference[indices[i]][secondIndex]) {
                        scratch[k] = indices[j];
                        ++j;
                    } else {
                        scratch[k] = indices[i];
                        ++i;
                    }
                    ++k;
                }
                System.arraycopy(scratch, 0, indices, from, kMax);
            }
        }
    }	
	
	private double medianInSwap(LinkedList p, int dimension) 
	{	
		int []array = new int[p.size];
		int from = 0;
	    int until = p.size;
        int to = until - 1;
        int med = (from + until) >>> 1;
        while (from <= to) {
            double pivot = input[array[from + random.nextInt(to - from + 1)]][dimension];
            int ff = from, tt = to;
            while (ff <= tt) {
            	comparison_rank = comparison_rank+1;
                while (input[array[ff]][dimension] < pivot) ++ff;
                while (input[array[tt]][dimension] > pivot) --tt;
                if (ff <= tt) {
                    int tmp = array[ff];
                    array[ff] = array[tt];
                    array[tt] = tmp;
                    ++ff;
                    --tt;
                }
            }
            if (med <= tt) {
                to = tt;
            } else if (med >= ff) {
                from = ff;
            } else {
                return input[array[med]][dimension];
            }
        }
        return input[array[from]][dimension];
    }

	public static void main(String[] args) 
	{
		
		int n = 5000;//30;//184;
		int m = 5;//4;//10;//3;
		int f = 10;//10;//1;
		int p = 1;
		//String filename="fixed_front_"+n+"_"+m+"_"+f+"_"+p+".txt";
		String filename="cloud_"+n+"_"+m+"_"+p+".txt";
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
//		
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
		T_ENS t_ens = new T_ENS(n,m);
		T_BOS t_bos = new T_BOS(n,m);
		DeductiveSort ds = new DeductiveSort(n,m);
		FastThreeDimensionalNDS ftd = new FastThreeDimensionalNDS(n,m);
		bos.read_population(n, m, filename, population);
		bos.sort(population);
		t_ens.sort(population);
		//ds.sort(population);
		t_bos.sort(population);
		ftd.sort(population);
		
		int []rank1 = bos.rank;
	
		int []rank2 = ftd.rank;
	    int i;
	    
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
		
	}

	

}
