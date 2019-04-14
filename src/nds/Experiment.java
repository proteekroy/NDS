package nds;

public class Experiment {
	
	static int test_type=0;
	static int start_m, increment_m, end_m;//different size of dimensions
	static int start_n, increment_n, end_n;//different size of population
	static int start_k, increment_k, end_k;//different size of fronts
	static int start_p, increment_p, end_p;//different samples
	static int start_g, increment_g, end_g;//different generation for moea data
	static int start_prob, increment_prob, end_prob;//different problems
	/*static String [] problem_name={"water","car_side_impact","dtlz1","dtlz1","dtlz1","dtlz1","dtlz1",
									"dtlz2","dtlz2","dtlz2","dtlz2","dtlz2",
									"dtlz3","dtlz3","dtlz3","dtlz3","dtlz3",
									"dtlz4","dtlz4","dtlz4","dtlz4","dtlz4"};//moea test problems
	*/
	static String [] problem_name={"dtlz1","dtlz2","dtlz3","dtlz4", "carside"};//moea test problems
	static double [][] population;
	/*static int []dim = {5, 3,
							3, 5, 8, 12, 15, 
							3, 5, 8, 12, 15,
							3, 5, 8, 12, 15,
							3, 5, 8, 12, 15};
	*/
	static int []dim = {10, 3, 10, 3, 3};
	static int []popsize = {2000, 200, 2000, 200, 200};
	static int []gen_size = {100, 250, 10, 600, 1000};	

	static boolean fileread=true, debug=false, printinfo=false;
	static boolean fixedfront=true, mixed=false;
	static int n = 100;
	static int m = 2;
	static int front=0;
	static MyFileIO io = new MyFileIO();
	
	static int numberOfNDSOptions = 9; 
	static AbstractNDS []nds_algorithm = new AbstractNDS[numberOfNDSOptions];
	static double[] time  = new double[numberOfNDSOptions];
	static double[] comparison  = new double[numberOfNDSOptions];
	static double[][] time_array; 
	static double[][] comparison_array;
	
	static double[] mean_time = new double[numberOfNDSOptions];
	static double[ ] mean_comparison = new double[numberOfNDSOptions];
	static double []comparison_h1 = new double[200]; 
	static double []time_h1 = new double[200];
	static double []comparison_h2 = new double[200]; 
	static double []time_h2 = new double[200];
	
	static double[][] time_each_gen = new double[9][1500];
	
	public static void main(String[] args) 
	{
		System.out.println("Start Comparison of Non-dominated Sorting Algorithms");
		run_experiment();	
	}
	
	public static void run_experiment()//population size N and objective number M is fixed
	{
		
		int i, j, k, p, g, prob_no,N,M,K;
		 
		
		System.out.println(Runtime.getRuntime().totalMemory()+"\t"+Runtime.getRuntime().maxMemory()+"\t"+Runtime.getRuntime().freeMemory());
		//Warmup
		int warmup = 1000000;
		for(int r = 0; r < warmup; r++) 
		{
		    // do test
		}
		
		/*
		start_m = 30; increment_m = 5; end_m = 30;
		start_n = 10000; increment_n = 1; end_n = 10000;
		start_k = 1; increment_k = 1; end_k = 1;
		start_p = 1; increment_p = 1; end_p = 30;
		start_g = 1; increment_g = 1; end_g = 1;
		start_prob = 1; increment_prob = 1; end_prob = 1;
		*/
		test_type = 3;
		//for(test_type = 0;test_type<=1;test_type=test_type+1)
		{
			get_runtime_parameter();
			
			for(prob_no=start_prob;prob_no<=end_prob;prob_no=prob_no+increment_prob)
			{
				for(i=start_n;i<=end_n;i=i+increment_n)
				{
//					if(test_type==0)
//					{
//						N = i* 1000;
//					}
//					else
					{
						N = i;
					}
					//N = popsize[prob_no];//i;
					//M = dim[prob_no];
					//end_g = gen_size[prob_no];
					//if(test_type==4)
					//{
						//io.write_problem(problem_name[prob_no]);
					//}				
					for(j=start_m;j<=end_m;j=j+increment_m)
					{	
						
						if(test_type==5)//MOEA data
						{	M = dim[j];	}
						else
						{	M=j;  }
//						nds_algorithm[0] = new DivideAndConquer(N, M);
//		            	nds_algorithm[1] = new DeductiveSort(N, M);
//		            	nds_algorithm[2] = new CornerSort(N, M);
//		            	nds_algorithm[3] = new ENS_SS(N, M);
//		            	nds_algorithm[4] = new ENS_BS(N, M);
		            	nds_algorithm[0] = new T_ENS(N, M);
		            	nds_algorithm[1] = new BestOrderSort(N, M);
		            	nds_algorithm[2] = new MMS(N, M); nds_algorithm[2].heapflag = true;
		            	
		            	
						//System.out.println("i = "+i+", j = "+j);
						//population = new double[N][M];
						
						for(k=start_k; k<=end_k; k=k+increment_k)
						{
							if(test_type==0)
							{
								N = k*1000;
							}
//							nds_algorithm[0] = new DivideAndConquer(N, M);
//			            	nds_algorithm[1] = new DeductiveSort(N, M);
//			            	nds_algorithm[2] = new CornerSort(N, M);
//			            	nds_algorithm[3] = new ENS_SS(N, M);
//			            	nds_algorithm[4] = new ENS_BS(N, M);
			            	//nds_algorithm[0] = new T_ENS(N, M);
			            	//nds_algorithm[1] = new BestOrderSort(N, M);
			            	//nds_algorithm[2] = new MMS(N, M); nds_algorithm[2].heapflag = true;
							//nds_algorithm[0] = new MMS(N, M); nds_algorithm[0].rank_search_option = 1;
							//nds_algorithm[1] = new MMS(N, M); nds_algorithm[1].rank_search_option = 2;
							//nds_algorithm[2] = new MMS(N, M); nds_algorithm[2].rank_search_option = 3;
//							nds_algorithm[0] = new MMS(N, M); nds_algorithm[0].improvement_option = 4;
//			            	nds_algorithm[1] = new MMS(N, M); nds_algorithm[1].improvement_option = 3;
//			            	nds_algorithm[2] = new MMS(N, M); nds_algorithm[2].improvement_option = 2;
//			            	nds_algorithm[3] = new MMS(N, M); nds_algorithm[3].improvement_option = 1;
			            	
							population = new double[N][M];
							K = k;
							init();
							
							for(p=start_p; p<=end_p; p=p+increment_p)
							{
								System.out.println("N = "+N+", M = "+M+", p = "+p+", K = "+K+", prob_no = "+prob_no+", name : "+problem_name[prob_no]);
								
								for(g=start_g; g<=end_g; g=g+increment_g)//for MOEA data
								{
									//System.out.println("i = "+i+", j = "+j+", p = "+p+", k = "+k+", prob_no = "+prob_no+", g = "+g);
									io.read_data(test_type, population, N, M, K, p, g, problem_name[prob_no]);
									//nds_algorithm[0].sort(population);
									//for(int index=0; index<nds_algorithm.length; index++)
									for(int index=0; index<3; index++)
									{
										nds_algorithm[index].sort(population);
										time_each_gen[index][g] = nds_algorithm[index].time;
										time[index] = time[index] + nds_algorithm[index].time;
										comparison[index] = comparison[index] + nds_algorithm[index].comparison;
									}
									/*
									nds_algorithm[8].heapflag = false;
									for(int param_h=5;param_h<=100;param_h = param_h+5)
									{
										nds_algorithm[8].m1 = param_h;
										nds_algorithm[8].sort(population);										
										time_h1[param_h] = time_h1[param_h] + nds_algorithm[8].time;
										comparison_h1[param_h] = comparison_h1[param_h] + nds_algorithm[8].comparison;	
									}
									front = front + nds_algorithm[8].totalfront;
									*/
									
									/*
									nds_algorithm[8].heapflag = true;
									for(int param_h=5;param_h<=100;param_h = param_h+5)
									{
										nds_algorithm[8].m1 = param_h;
										nds_algorithm[8].sort(population);										
										time_h2[param_h] = time_h2[param_h] + nds_algorithm[8].time;
										comparison_h2[param_h] = comparison_h2[param_h] + nds_algorithm[8].comparison;	
									}*/
									front = front + nds_algorithm[0].totalfront;
									
									
								}//generation #g
								System.gc();
							}//population #p
							
							front = (int) (front/(end_p*end_g));
							//io.write_data(test_type, N, M, front, comparison_h1, time_h1); 
							//io.write_data(test_type, N, M, front, comparison_h2, time_h2); 
							io.write_data(test_type, N, M, front, comparison, time); 
							//io.write_data(test_type, N, M, front, comparison_h, time_h); 
							//io.write_2d_double(time_each_gen, "time_each_gen.txt");
							//printComparisonTime(N, M, front, comparison, time); //print time
						}//front #k
					}//obbjective #j
				}//problem name
				System.gc();
			}//population size #i
		}//testtype#
		System.out.println("Done");
	}
	
	public static void init()
	{
		
		front = 0;
		for(int i=0;i<time.length;i++)
		{
			time[i] = 0;
			comparison[i] = 0;
		}	
		for(int i=0;i<time_h1.length;i++)
		{
			time_h1[i] = 0;
			comparison_h1[i] = 0;
			time_h2[i] = 0;
			comparison_h2[i] = 0;
		}
	}
	
	
	public static void printComparisonTime(int n, int m, int  front, double[] mean_comparison, double[] mean_time)
	{
		System.out.println("\nTotal Number of elements is "+n);
		System.out.println("Total Number of objectives is "+ m);
		System.out.println("Number of fronts = "+front);
		System.out.println("Number of Comparisons (Avg) :\n");
		
		if(mean_comparison[0]!=0) System.out.println("Divide&Conquer algorithm = "+mean_comparison[0]);
		if(mean_comparison[1]!=0) System.out.println("Deductive sort = "+mean_comparison[1]);
		if(mean_comparison[2]!=0) System.out.println("Corner sort = "+mean_comparison[2]);
		if(mean_comparison[3]!=0) System.out.println("ENS-SS sort = "+mean_comparison[3]);
		if(mean_comparison[4]!=0) System.out.println("ENS-BS sort = "+mean_comparison[4]);
		if(mean_comparison[5]!=0) System.out.println("T-ENS = "+mean_comparison[5]);
		if(mean_comparison[6]!=0) System.out.println("BOS algorithm = "+mean_comparison[6]);
		if(mean_comparison[7]!=0) System.out.println("H-BOS Sort = "+mean_comparison[7]);
		if(mean_comparison[8]!=0) System.out.println("T-BOS = "+mean_comparison[8]);
		
		
		System.out.println("Total time (Avg) :\n");
		if(mean_time[0]!=0) System.out.println("Divide&Conquer algorithm  = "+mean_time[0]);
		if(mean_time[1]!=0) System.out.println("Deductive sort = "+mean_time[1]);
		if(mean_time[2]!=0) System.out.println("Corner sort = "+mean_time[2]);
		if(mean_time[3]!=0) System.out.println("ENS-SS sort = "+mean_time[3]);
		if(mean_time[4]!=0) System.out.println("ENS-BS sort = "+mean_time[4]);
		if(mean_time[5]!=0) System.out.println("T-ENS = "+mean_time[5]);
		if(mean_time[6]!=0) System.out.println("BOS algorithm = "+mean_time[6]);
		if(mean_time[7]!=0) System.out.println("H-BOS Sort = "+mean_time[7]);
		if(mean_time[8]!=0) System.out.println("T-BOS = "+mean_time[8]);
	}
	
	
	public static void get_runtime_parameter()
	{
		switch(test_type)
		{
			case 0://constant ratio data, vary front and population, fixed objective
				start_m = 10; increment_m = 1; end_m = 10;
				start_n = 1; increment_n = 1; end_n = 1;
				start_k = 10; increment_k = 10; end_k = 100;
				start_p = 1; increment_p = 1; end_p = 10;
				start_g = 1; increment_g = 1; end_g = 1;
				start_prob = 0; increment_prob = 1; end_prob = 0;
				break;
				
			case 1://cloud data, vary objective 
				start_m = 10; increment_m = 10; end_m = 100;
				start_n = 100000; increment_n = 1; end_n = 100000;
				start_k = 1; increment_k = 1; end_k = 1;
				start_p = 1; increment_p = 1; end_p = 10;
				start_g = 1; increment_g = 1; end_g = 1;
				start_prob = 0; increment_prob = 1; end_prob = 0;
				break;
				
			case 2://mixed data
				start_m = 10; increment_m = 1; end_m = 10;
				start_n = 5000; increment_n = 5000; end_n = 50000;
				start_k = 1; increment_k = 1; end_k = 1;
				start_p = 1; increment_p = 1; end_p = 10;
				start_g = 1; increment_g = 1; end_g = 1;
				start_prob = 0; increment_prob = 1; end_prob = 0;
				break;
			
			case 3://cloud data, vary population 
				start_m = 100; increment_m = 10; end_m = 100;
				start_n = 10000; increment_n = 10000; end_n = 200000;
				start_k = 1; increment_k = 1; end_k = 1;
				start_p = 1; increment_p = 1; end_p = 10;
				start_g = 1; increment_g = 1; end_g = 1;
				start_prob = 0; increment_prob = 1; end_prob = 0;
				break;
				
			case 4://fixed front data, vary objective
				start_m = 100; increment_m = 10; end_m = 100;
				start_n = 100000; increment_n = 10000; end_n = 100000;
				start_k = 10; increment_k = 1; end_k = 10;
				start_p = 1; increment_p = 1; end_p = 10;
				start_g = 1; increment_g = 1; end_g = 1;
				start_prob = 0; increment_prob = 1; end_prob = 0;
				break;
			
			case 5://moea data
				start_m = 1; increment_m = 1; end_m = 1;
				start_n = 1; increment_n = 1; end_n = 1;
				start_k = 1; increment_k = 1; end_k = 1;
				start_p = 1; increment_p = 1; end_p = 1;
				start_g = 1; increment_g = 1; end_g = 1500;
				start_prob = 2; increment_prob = 1; end_prob = 2;
				break;
				
			case 6://real world data
				start_m = 5; increment_m = 1; end_m = 5;
				start_n = 4000; increment_n = 1; end_n = 4000;
				start_k = 1; increment_k = 1; end_k = 1;
				start_p = 1; increment_p = 1; end_p = 1;//30;
				start_g = 1; increment_g = 1; end_g = 600;//1000;//2500;
				start_prob = 0; increment_prob = 1; end_prob = 0;
				break;
				
			case 7://worst case data
				start_m = 100; increment_m = 5; end_m = 100;
				start_n = 50000; increment_n = 10000; end_n = 50000;
				start_k = 1; increment_k = 1; end_k = 1;
				start_p = 1; increment_p = 1; end_p = 1;//30;
				start_g = 1; increment_g = 1; end_g = 1;
				start_prob = 0; increment_prob = 1; end_prob = 0;
				break;
		
			default:
				System.out.println("Invalid paramters");
				System.exit(0);
				
		}
		
	}

}
