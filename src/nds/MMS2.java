package nds;
 
import java.util.Random;
 
import development.MyFileIO;
 
public class MMS2 extends AbstractNDS{
     
    int []lex_order;
    int [][]allrank;
    //int [][]allrankCopy;
    boolean []set;
    int []temp;
    LinkedList [][]list;
    LinkedList []F;
    //LinkedList extra_sol;
    int [] extra_solutions;
    int [] extra_solutions_index;
    int extra_solution_counter;
    double []max_val;
     
    QuickSort quicksort;
     
    boolean found, dominated, skip;
    NDTree last_nondominated_element;
    int nondominated_obj_index;
    public NDTree [][] NDT_specific;
    public int[] comparison_index_count;
    int initialFront, lastfront;
    public SplayTree tree;
    int [] frontSizeCount;
     
    boolean self_adjusing_list_points_flag = true;
    boolean self_adjusing_list_obj_flag = true;
    boolean ndtree_flag = true;
     
     
     
    public MMS2(int arg_n, int arg_m)
    {
        n = arg_n;
        m = arg_m;
        //m1 = (int) Math.min(m, Math.max(1, Math.ceil(Math.log10(n)/Math.log10(2))));
        //m1 =  Math.max(2, Math.min(m, (int)Math.sqrt(n)));
        m1 = m;
        //m1 = m;
        helper = new int[n];
        rank = new int[n];
        //allrank = new int[m1][n];
        //allrankCopy = new int[m1][n];
        lex_order = new int[n];
        objseq = new int[n][m];
        treeobjseq = new int[n][m];
        set = new boolean[n];
        frontSizeCount = new int[n];
        NDT_specific = new NDTree[m1][n];
        list = new LinkedList[m1][n];
        comparison_index_count = new int[n];
        temp = new int[n];
        F = new LinkedList[n];
        tree = new SplayTree();
        quicksort = new QuickSort();
        rand = new Random();
        heapsort = new HeapSort();
    }
     
    @Override
    public void sort(double [][] population) 
    {
        initialize(population); 
        start = System.nanoTime();//starting timer
        mms();
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
            System.out.println("\nStarting MMS2 Sort");
        }
        //m1 = Math.min(m, m1);
         
        allrank = new int[m1][n];
        //allrankCopy = new int[m1][n];
        NDT_specific = new NDTree[m1][n];
        tree.delete();
        tree.insert(new Integer(0));        
         
        population = pop;
        totalfront = 0;
        comparison = 0;
        comparison_sort = 0;
        comparison_rank = 0;
        initialFront = 0;
        time = 0;
        lastfront = n;
        extra_solution_counter = 0;
        int i, j;
         
        for(j=0;j<m1;j++)
        {
            for(i=0;i<n;i++)
            {
                allrank[j][i] = i;
                objseq[i][j] = j;
                treeobjseq[i][j] = j;
                 
                if(list[j][i]!=null)
                {
                    list[j][i].delete();
                }
                 
            }
        }
         
        for(i=0;i<n;i++)
        {
            comparison_index_count[i] = m;
            set[i] = false;
            rank[i] = -1;
        }
         
        extra_solutions = new int[n];
        extra_solutions_index = new int[n];
        max_val = new double[m1];
        quicksort = new QuickSort();
        mergesort = new MergeSort();
        mergesort.setValues(helper, lex_order, population, allrank, this);
        heapsort.setAllrankObj(allrank, 0, this);
    }
     
     
    public void search_solution(int i, int j, int arg_max)
    {
        if(population[allrank[j][i]][j]<=max_val[j])
        {
            //if(!set[allrank[j][i]])
            if(i>=arg_max)
            {
                extra_solutions[extra_solution_counter] = allrank[j][i];
                extra_solutions_index[extra_solution_counter] = i;
                extra_solution_counter++;
                //set[allrank[j][i]] = true;
            }
            //Take children of this solution
            if((2*i+1)<n)
            {
                search_solution(2*i+1, j, arg_max);
                if((2*i+2)<n)
                {
                    search_solution(2*i+2, j, arg_max);
                }
            }
            
        }
        return;
    }
     
    public void mms()
    {
        int i = 0, j, s, total = 0, obj, t = n, t2,  search_temp, index_start, index_end;
        //max_val = new double[m1];
         
        //mergesort.setObj(0);
        //mergesort.mergesort(0, n-1);//sorts first objective with lexicographic order
        quicksort.set(population, allrank[0], this);
        quicksort.sort(0, 0, n-1);
             
        for(i=0;i<n;i++)
        {
            lex_order[allrank[0][i]] = i;  
        }
             
        buildheapAllMinHeap(m1);
        for(j=0;j<m1;j++)
        {
            max_val[j] = Double.MIN_VALUE;
        }
         
        total = 0;
         
        for(i=0;i<n;i++)
        {
            for(j=0;j<m1;j++)
            {
                s = allrank[j][i];
                if(population[s][j] > max_val[j])//better than worst point
                {
                    max_val[j] = population[s][j];
                }
                if(!set[s])
                {
                    set[s] = true;
                    total++;
                    //if(total==n)
                    //  break;
                }
            }
            if(total==n)
                break;
        }
        total = Math.min(i+1, n-1);
        //t2 = (int)Math.max(Math.floor((i-1)*1.0/2), 0);
        //t = -1;//Math.min(2*total, n);
        //index_start = Math.min(i+1, n-1);
		//index_end = Math.min(2*(i+1)-1, n);
		index_start = (int)Math.max(Math.floor((i-1)*1.0/2), 0);
		index_end = Math.min(i+1, n);
		t = index_start;
         
		/*
		for(j=0;j<m;j++)
        {
        	for(i=0;i<n;i++)
            {
                set[i] = false;
            }
        	for(i=0;i<n;i++) 
            {
        		s = allrank[j][i];
        		if(s==31686)
        		{
        			System.out.println(j);
        		}
            	if(!set[s])
            	{
            		set[s]=true;
            	}
            	
            }
        }*/
		for(j=0;j<m;j++)
        {
        	for(i=0;i<n;i++)
            {
                set[i] = false;
            }
        	for(i=0;i<n;i++) 
            {
        		s = allrank[j][i];
        		if(s==31686)
        		{
        			System.out.println(j);
        		}
            	if(!set[s])
            	{
            		set[s]=true;
            	}
            	else
            	{
            		System.out.println("Problem, s  = "+s);
            	}
            	
            }
        }
		
        for(j=0;j<m1;j++)
        {
            /*
            for(i=total;i<t2;i++)
            {
                s = allrank[j][i];
                if(population[s][j] <= max_val[j])
                {
                    //max_val[j] = population[s][j];
                    //total = i;
                    //t2 = Math.min(2*i, n);
                    t2 = i;
                }
            }
            t2 = i;
            t = Math.min(t2, t);
            */
            extra_solution_counter = 0;
            for(i=index_start;i<index_end;i++)
            {
                search_solution(i, j, index_end);
            }
            for(i=0;i<extra_solution_counter;i++)
            {
//            	if(extra_solutions_index[i]<index_end)
//            	{	
//            		continue;
//            	}
                search_temp = allrank[j][index_start+i];
                allrank[j][index_start+i] = extra_solutions[i];
                allrank[j][extra_solutions_index[i]] = search_temp; 
            }
            for(i=0;i<n;i++)
            {
                set[i] = false;
            }
//            for(i=0;i<n;i++) 
//            {
//        		s = allrank[j][i];
//            	if(!set[s])
//            	{
//            		set[s]=true;
//            	}
//            	else
//            	{
//            		System.out.println("Problem, s  = "+s);
//            	}
//            	
//            }
            t = Math.max(t, index_start+extra_solution_counter);
            if(t==n)
            {   break;}
             
        }
        
        for(j=0;j<m;j++)
        {
        	for(i=0;i<n;i++)
            {
                set[i] = false;
            }
        	for(i=0;i<n;i++) 
            {
        		s = allrank[j][i];
//        		if(s==31686)
//        		{
//        			System.out.println(j);
//        		}
            	if(!set[s])
            	{
            		set[s]=true;
            	}
            	else
            	{
            		System.out.println("Problem, s  = "+s);
            	}
            	
            }
        }
        //total = Math.min(n, 2*(i+1));
        //long endHeap = System.nanoTime();
        sortingValues(t);//sorting population according to all objectives
 
        int []temp = new int[n];//initially zero
         
        for(i=0;i<n;i++)
        {
            for(j=0;j<m;j++)
            {
                if(j<m1)
                {
                    s = allrank[j][i];
                    //search_temp = objseq[s][m1-1-temp[s]];
                    objseq[s][m1-1-temp[s]] = j;
                    //objseq[s][j] = search_temp;
                             
                    //search_temp = treeobjseq[s][m1-1-temp[s]];
                    treeobjseq[s][m1-1-temp[s]] = j;
                    //treeobjseq[s][j] = search_temp;
                    temp[s]++;
                }
                /*else
                {
                    objseq[i][j] = j;
                    treeobjseq[i][j] = j;
                }   */ 
            }
        }
        /*
        for(i=0;i<n;i++)
        {
            for(j=0;j<m;j++)
            {
                if(j<m1)
                {
                    s = allrank[j][i];
                    if(temp[s]<m)
                    {
                    objseq[s][m1-1-temp[s]] = j;
                    treeobjseq[s][m1-1-temp[s]] = j;
                    temp[s]++;
                    }
                }
                else
                {
                    objseq[i][j] = j;
                    treeobjseq[i][j] = j;
                }       
            }
        }*/
         
        for(i=0;i<n;i++)
        {
            set[i] = false;
            //temp[i] = 0;
        }
        endTime2 = System.nanoTime();
             
        //total = 0;
        totalfront = 0;
         
        for(j=0;j<m1;j++)
        {
            list[j][0] = new LinkedList();
        }
        //long endSort = System.nanoTime();
        total = 0;
        for(i=0;i<t;i++)//n
        {
            for(obj=0;obj<m1;obj++)
            {
                s = allrank[obj][i];
 
                if(set[s])//s is already ranked
                {
                    if(list[obj][rank[s]]==null)
                    {
                        list[obj][rank[s]] = new LinkedList();  
                    }
                    list[obj][rank[s]].addStart(s);
                    continue;
                }
                set[s] = true;
                total++;
                //find_rank_basic(s, obj);
                find_rank_splay(s, obj);
                //find_rank_binary(s, obj);
                //find_rank_sequential(s, obj);
                //if(total==n)
                {
                //  break;
                }
            }
            if(total==n)
            {
                break;
            }
        }
        totalfront++;
        //double heap_time = (endHeap - start)*1.0/1000000.0;
        //double sort_time = (endSort - endHeap)*1.0/1000000.0;
        //System.out.println("Heap Time = "+heap_time);
        //System.out.println("Sort Time = "+sort_time);
 
    }
     
    public void find_rank_binary(int s, int obj)
    {
        int i, low, high, middle = 0;
        low = 0;            
        high = totalfront;
         
        while(high >= low) 
        {
            middle = low + ((high - low) / 2); //bug fix (low + high) / 2;
             
            find_rank(s, obj, middle);
            //find_basic(s, obj, middle);
             
            if(!dominated) //it has low rank, numerically
            {
                high = middle - 1;
            }
            else //it has high rank, numerically
            {
                low = middle + 1;
            }
        }
         
        if(dominated)
        {
            middle++;
        }
        if(middle>totalfront)
        {
            totalfront++;
            for(i=0;i<m1;i++)
            {
                list[i][totalfront] = new LinkedList();
            }
        }
        rank[s] = middle;
         
        if(NDT_specific[obj][rank[s]]==null)
        {
            NDT_specific[obj][rank[s]] = new NDTree(treeobjseq[0].length);
            NDT_specific[obj][rank[s]].setData(s);
        }
        else if(!skip)//skip is not true i.e. there is no point with similar objective value, then we got a place for this
        {
            last_nondominated_element.children[nondominated_obj_index] = new NDTree(treeobjseq[0].length);
            last_nondominated_element.children[nondominated_obj_index].setData(s);
        }
         
    }
         
    public void find_rank_splay(int s, int obj)
    {
        int i, k;
        boolean prev_skip = false;
        BinaryNode binNode = tree.getRoot();
 
        while(true)
        {
            k = (int) binNode.key;
 
            find_rank(s, obj, k);//go through ND-tree and lists
             
            if(!dominated)//if not dominated
            {
                if(binNode.left==null)//leftmost node of a sub-tree
                {
                    rank[s] = k;
                    tree.find(new Integer(Math.max(0, k-1)));
                    break;
                }
                else
                {
                    binNode = binNode.left;
                }
                prev_skip = skip;
            }
            else//if dominated
            {
                if(binNode.right!=null)
                {
                    prev_skip = false;
                    binNode = binNode.right;
                }
                else if(k<totalfront)//this is a rightmost node of a sub-tree
                {   
                    k = k+1;
                    rank[s] = k;
                    /*if(list[obj][k]==null)
                    {
                        list[obj][k] = new LinkedList();
                    }*/
                    skip = prev_skip;
                    tree.find(new Integer(k));
                    break;
                }
                else //if(k==BestOrderSort.totalfront)//rightmost node of whole tree
                {
                    totalfront++;
                    for(i=0;i<m1;i++)
                    {
                        list[i][totalfront] = new LinkedList();
                    }
                     
                    rank[s] =  totalfront;
                    tree.insert(new Integer(rank[s]));//new front will be added in splay tree
                    break;
                }
            }//if
        }//while(true)
         
        //add to tree
        if(NDT_specific[obj][rank[s]]==null)
        {
            NDT_specific[obj][rank[s]] = new NDTree(treeobjseq[0].length);
            NDT_specific[obj][rank[s]].setData(s);
        }
        else if(!skip)//skip is not true i.e. there is no point with similar objective value, then we got a place for this
        {
            last_nondominated_element.children[nondominated_obj_index] = new NDTree(treeobjseq[0].length);
            last_nondominated_element.children[nondominated_obj_index].setData(s);
        }
    }
     
    public void find_rank_sequential(int s, int obj)
    {
        int i, k;   
        for(k = 0; k <= totalfront; k++)
        {
            find_rank(s, obj, k);//go through ND-tree and lists
             
            if(!dominated) //not dominated
            {
                break;
            }
        }
        if(dominated)
        {
            if(k > totalfront)
            {
                totalfront++;
                for(i=0;i<m1;i++)
                {
                    list[i][totalfront] = new LinkedList();
                }
                 
            }
        }
        rank[s] = k;
         
        if(NDT_specific[obj][rank[s]]==null)
        {
            NDT_specific[obj][rank[s]] = new NDTree(treeobjseq[0].length);
            NDT_specific[obj][rank[s]].setData(s);
        }
        else if(!skip)//skip is not true i.e. there is no point with similar objective value, then we got a place for this
        {
            last_nondominated_element.children[nondominated_obj_index] = new NDTree(treeobjseq[0].length);
            last_nondominated_element.children[nondominated_obj_index].setData(s);
        }
    }
     
    public void find_rank_basic(int s, int obj)
    {
        int i, k;   
        for(k = 0; k <= totalfront; k++)
        {
            find_basic(s, obj, k);//go through ND-tree and lists
             
            if(!dominated) //not dominated
            {
                break;
            }
        }
        if(dominated)
        {
            if(k > totalfront)
            {
                totalfront++;
                for(i=0;i<m1;i++)
                {
                    list[i][totalfront] = new LinkedList();
                }
                 
            }
        }
        rank[s] = k;
        list[obj][rank[s]].add(s);
    }
     
    public void find_basic(int s, int obj, int frontno)
    {
        Node head, prev;
        dominated = false;
        //compare with all the solution found early     
        head = list[obj][frontno].start;
        prev = head;
         
        while(head!=null)
        {
//          if(dominates(head.data, s))
//          {
//              dominated = true;
//              break;
//          }
            //Basic+Self-Adjusting
            //if(dominates_objseq_adaptive(head.data, s))
            //if(dominates(head.data, s))   
            if(dominates_objseq(head.data, s))
            {
                dominated = true;
                //if(rand.nextDouble()<0.8)
                {
                    if(prev!=head)
                    {
                        list[obj][frontno].bringToFront(prev, head);
                    }
                }
                break;
            }
            prev = head;
            head = head.link;
        }
        return;
    }
     
     
     
    public void find_rank(int s, int obj, int frontno)
    {
        Node head, prev;
        dominated = false;
        //compare with all the repeated solution, probably AVL would be better than list, but not sure 
        head = list[obj][frontno].start;
        prev = head;
        while(head!=null)
        {
            //if(dominates(head.data, s))
            if(dominates_objseq(head.data, s))
            //if(dominates_objseq_adaptive(head.data, s))
            {
                dominated = true;
                //if(rand.nextDouble()<0.2)
                {
                    if(prev!=head)
                    {
                        list[obj][frontno].bringToFront(prev, head);
                    }
                }
                 
                break;
            }
            prev = head;
            head = head.link;
        }
         
        //compare with unique solutions
        skip = false;
        found = false;
                 
        update_tree(s, obj, frontno);
                 
        if(dominated)//dominated by unique solutions
        {
            return;
        }
        else
        {
            if(skip)//No need to add this element
            {
                return;
            }
        }
        return;
    }
     
    public void update_tree(int p, int obj, int frontno) 
    {
        if(NDT_specific[obj]==null)
        {
            return;
        }
        else
        {
            check_tree(p, NDT_specific[obj][frontno], true);
        }
    }
     
    private void check_tree(int p, NDTree T, boolean add_pos) 
    {
        if(T==null)
        {
            return;//this is needed as the base case of recursive call
        }
         
        int i, m=-1;
        boolean less = false, equal = true;
         
        for(i=0;i<treeobjseq[0].length;i++)
        {
            if(population[p][treeobjseq[T.data][i]]<population[T.data][treeobjseq[T.data][i]])//compare with root
            {
                less = true;
                m = i; //save index
                comparison_rank++;
                break;
            }
            else if(population[p][treeobjseq[T.data][i]]==population[T.data][treeobjseq[T.data][i]])//compare with root
            {
                if(i==(treeobjseq[0].length-1)&& equal)
                {
                    rank[p] = rank[T.data];
                    skip = true;
                    return;
                }
            }
            else
            {
                equal = false;
            }
        }
        if(less==false)
        {
            dominated = true;
            return;
        }
        else
        {
            for(i=0;i<=m;i++)
            {
                check_tree(p, T.children[i], (i==m)&&add_pos);
                if(found || dominated || skip)
                {
                    return;//p is dominated by a solution in the branch of the tree
                }
            }
             
            if(T.children[m]==null && add_pos)
            {
                last_nondominated_element = T;
                nondominated_obj_index = m;
                found = true;
            }
             
            return;
        }
 
    }
     
     
    private void sortingValues(int high) 
    {
        //Sorting with respect to objective values
        for (int obj=1;obj<m1;obj++)
        {
            mergesort.setObj(obj);
            mergesort.mergesort(0, high-1);//mergesort
        }
    }
     
    public static void main(String[] args) 
    {
        MyFileIO io = new MyFileIO();
        int N = 50000;
        int M = 10;
        int K = 1;
        int p, m;
        //double time = 0, comparison = 0;
        MMS2 mms2;
        MMS mms;
         
        double time[][];
        double  comparison[][];
        int[] obj;
        int end_m = 9;
        int end_p = 1;
        obj = new int[]{1000,200,300,400,500,600,700,800,900,1000};
         
         
        m = 0;
         
        //mms2
         
        time = new double[10][10];
        comparison = new double[10][10];
        //
        //for(m=0;m<end_m;m=m+1)
        {
            M = obj[m];
            double [][]population = new double[N][M];
            mms2 = new MMS2(N, M);
            for(p=1;p<=end_p;p++)
            {
                System.out.println("M = "+M+", N = "+N+", K = "+K+", p = "+p);
                io.read_data(1, population, N, M, K, p, 1, "");//cloud data
                mms2.sort(population);
                time[m][p-1] = time[m][p-1] + mms2.time;
                comparison[m][p-1] = comparison[m][p-1] + mms2.comparison;
            }
            System.out.print("\nTime:\n");
            for(p=0;p<end_p;p++)
            {
                System.out.print(time[m][p]+"\t");
            }
            System.out.println();
            System.out.print("\nComparison:\n");
            for(p=0;p<end_p;p++)
            {
                System.out.print(comparison[m][p]+"\t");
            }
            System.out.println();
            System.gc();
        }
         
         
        //mms
        time = new double[10][10];
        comparison = new double[10][10];
        //
        //for(m=0;m<end_m;m=m+1)
        {
            M = obj[m];
            double [][]population = new double[N][M];
            mms = new MMS(N, M);
            for(p=1;p<=end_p;p++)
            {
                System.out.println("M = "+M+", N = "+N+", K = "+K+", p = "+p);
                io.read_data(1, population, N, M, K, p, 1, "");//cloud data
                mms.sort(population);
                time[m][p-1] = time[m][p-1] + mms.time;
                comparison[m][p-1] = comparison[m][p-1] + mms.comparison;
            }
            System.out.print("\nTime:\n");
            for(p=0;p<end_p;p++)
            {
                System.out.print(time[m][p]+"\t");
            }
            System.out.println();
            System.out.print("\nComparison:\n");
            for(p=0;p<end_p;p++)
            {
                System.out.print(comparison[m][p]+"\t");
            }
            System.out.println();
            System.gc();
        }
         
        System.out.println("Done");
    }
}