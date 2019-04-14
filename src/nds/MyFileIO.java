package nds;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class MyFileIO {

	double[][] population;
	
	
	
	public void read_data(int test_type,double[][] population,
			int n, int m, int f, int p, int g, String problem) 
	{
		switch(test_type)
		{
			case 0://fixed front data, vary front
				read_fixed_front(population, n, m, f, p); 
				break;
			case 1://mixed data, vary objective
				read_mixed_data(population, n, m, f, p); 
				break;
			case 2://cloud data, vary objective 
				read_cloud_data(population, n, m, f, p); 
				break;
			case 3://cloud data, vary population 
				read_cloud_data(population, n, m, f, p); 
				break;
			case 4://fixed front data, vary objective
				read_fixed_front(population, n, m, f, p); 
				break;
			case 5://moea data
				read_moea_data(population, n, m, f, p, g, problem); 
				break;
			case 6://real world data
				read_real_data(population, n, m, f, p, g, problem); 
				break;
			case 7:
				read_worst_data(population, n, m, f, p);
				break;
			default://random data
				read_random_data(population, n, m, 1);
				break;
		}
	}
	
	
	public void write_data(int test_type, int n, int m, 
			int front, double []mean_comparison, double[] mean_time) 
	{
		File file1, file2;
		switch(test_type)
		{
			case 0://fixed front data, vary front
				file1 = new File("fixed_front_data_const_ratio_vary_front_comparison.txt");
				file2 = new File("fixed_front_data_const_ratio_vary_front_time.txt");
				//file1 = new File("internal_fixed_front_comparison.txt");
				//file2 = new File("internal_fixed_front_time.txt");
				break;
			case 1://cloud data, vary objective 
				file1 = new File("mixed_data_comparison_vary_population.txt");
				file2 = new File("mixed_data_time_vary_population.txt");
				break;
			case 2://cloud data, small vary objective
				file1 = new File("cloud_data_comparison_vary_population_mms.txt");
				file2 = new File("cloud_data_time_vary_population_mms.txt");
				//file1 = new File("fixed_front_data_comparison_vary_objective.txt");
				//file2 = new File("fixed_front_data_time_vary_objective.txt");
				//file1 = new File("internal_fixed_front_comparison.txt");
				//file2 = new File("internal_fixed_front_time.txt");
				break;
				
			case 3://cloud data, vary population 
				file1 = new File("cloud_data_comparison_vary_population.txt");
				file2 = new File("cloud_data_time_vary_population.txt");
				//file1 = new File("large_scale_comparison.txt");
				//file2 = new File("large_scale_time.txt");
				//file1 = new File("internal_cloud_comparison.txt");
				//file2 = new File("internal_cloud_time.txt");
				break;
			case 4://mixed data, vary objective and front
				file1 = new File("mixed_data_comparison_vary_objective.txt");
				file2 = new File("mixed_data_time_vary_objective.txt");
				//file1 = new File("internal_mixed_comparison.txt");
				//file2 = new File("internal_mixed_time.txt");
				break;
			case 5://moea data
				file1 = new File("moea_data_comparison.txt"); 
				file2 = new File("moea_data_time.txt");  
				break;
			case 6://real world data
				file1 = new File("real_data_comparison.txt"); 
				file2 = new File("real_data_time.txt");  
				break;
			case 7://worst case data
				file1 = new File("worst_data_comparison.txt"); 
				file2 = new File("worst_data_time.txt");  
				break;	
			default://random data
				System.out.println("Invalid paramters");
				file1 = new File("cloud_data_comparison_vs_obj_test.txt");
				file2 = new File("cloud_data_time_vs_obj_test.txt");
				break;
		}
		
		
		try 
		{
			BufferedWriter output1 = new BufferedWriter(new FileWriter(file1,true));//true=append
			BufferedWriter output2 = new BufferedWriter(new FileWriter(file2,true));//true=append
			
			//write comparison and runtime
			output1.write(n+"\t"+m+"\t"+front+"\t");
			output2.write(n+"\t"+m+"\t"+front+"\t");
			
			for(int i=0;i<mean_comparison.length;i++)
			{
				if(mean_comparison[i]!=0) output1.write(mean_comparison[i] +"\t");
				if(mean_time[i]!=0) output2.write(mean_time[i]+"\t");
			}
			output1.write("\n");
			output1.flush();
			output1.close();
			output2.write("\n");
			output2.flush();
			output2.close();
			
	    } 
		catch ( IOException e ) 
	    {
	       e.printStackTrace();
	    }
	}
	
	public void read_fixed_front(double[][] population, int n, int m, int f, int p) 
	{
		this.population = population;
		String filename="C:/Proteek's Folder/Research/Data/BOSBB data/Fixed Front/fixed_front_"+n+"_"+m+"_"+f+"_"+p+".txt";//fixed_front_500_5_5_1.txt
		//String filename="C:/Proteek's Folder/Research/Data/Heap data/Fixed Front/fixed_front_"+n+"_"+m+"_"+f+"_"+p+".txt";//fixed_front_500_5_5_1.txt
		//String filename="fixed_front_"+n+"_"+m+"_"+f+"_"+p+".txt";//fixed_front_500_5_5_1.txt
		//System.out.println(filename);
		read_population(n, filename);
	}
	public void read_moea_data(double[][] population, int n, int m, int f, int p,int g, String problem) 
	{
		this.population=population;
		String filename="C:/Proteek's Folder/Research/Data/BOSBB data/MOEA Dataset/run"+p+"/"+problem+"_"+n+"_"+m+"_gen_"+g+".txt";//'dtlz1_200_2_gen_1.txt';
		//String filename="C:/Users/COIN8/Dropbox/Michigan State Projects/MOEAFramework-2.1/Nondominated Test Data/run"+p+"/"+problem+"_"+n+"_"+m+"_gen_"+g+".txt";//'dtlz1_200_2_gen_1.txt';
		read_population(n, filename);
	}
	public void read_real_data(double[][] population, int n, int m, int f, int p,int g, String problem) 
	{
		this.population=population;
		String filename="C:/Proteek's Folder/Research/Data/BOSBB data/MOEA Dataset/run"+p+"/water_424_5_gen_"+g+".txt";//'water_424_5_gen_1.txt';
		read_population(n, filename);
	}
	public void read_cloud_data(double[][] population, int n, int m, int f, int p) 
	{
		this.population=population;
		String filename="C:/Proteek's Folder/Research/Data/BOSBB data/Cloud Dataset/cloud_"+n+"_"+m+"_"+p+".txt";//'cloud_500_5_1.txt';
		//String filename="C:/Proteek's Folder/Research/Data/Heap data/Cloud Data/cloud_"+n+"_"+m+"_"+p+".txt";//'cloud_500_5_1.txt';
		
		//String filename="cloud_"+n+"_"+m+"_"+p+".txt";//'cloud_500_5_1.txt';
		read_population(n, filename);
	}
	public void read_mixed_data(double[][] population,int n, int m, int f, int p) 
	{
		this.population = population;
		String filename="C:/Proteek's Folder/Research/Data/BOSBB data/Mixed Dataset/mixed_"+n+"_"+m+"_1_"+p+".txt";//'mixed_500_5_1_1.txt';
		read_population(n,filename);
	}
	public void read_worst_data(double[][] population, int n, int m, int f, int p) 
	{
		this.population = population;
		String filename="C:/Proteek's Folder/Research/Data/BOSBB data/Worst Case Dataset/worst_case_"+n+"_"+m+"_"+p+".txt";//'worst_case_50000_100_1.txt';
		read_population(n,filename);
	}
	
	public void read_random_data(double[][] population,
			int n, int m, int r)
	{
		double value,max=1,min=0;
		int i,j;
		String filename="Random/random_"+n+"_"+m+"_"+r+".txt";
		File file = new File(filename);
		try 
		{
			BufferedWriter output = new BufferedWriter(new FileWriter(file,true));
		
			Random rand = new Random();
			
			for(i=0;i<n;i++)
			{
				for(j=0;j<m;j++)
				{
					value = rand.nextDouble() * (max - min) + min;
					population[i][j] =value;
					output.write(population[i][j]+" ");
				}
				output.write("\n");
			}
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void read_population(int n, String filename) 
	{
		int i=0,j;
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String strLine;
			while ((strLine = br.readLine()) != null)   
			{
				
				String[] tokens = strLine.split("\\s");
				strLine=strLine.trim();
				if(i==n)
				{
					break;
				}
				for(j=0;j<population[0].length;j++)
				{
					//System.out.print(tokens[j]+" ");
					try{
					population[i][j]=Double.parseDouble(tokens[j]);
					}
					catch (Exception e)
					{
						System.err.println("Error: " + e.getMessage());
					}
				}
				//System.out.println();
				i++;
			}
			br.close();
		}
		catch (IOException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public void print_population(double [][] population)
	{
		int i,j;
		for(i=0;i<population.length;i++)
		{
			System.out.print("["+(i+1)+"]  ");
			for(j=0;j<population[0].length;j++)
			{
				System.out.print(population[i][j]+" ");
			}
			System.out.println();
		}
	}
	public void write_population(double [][] population)
	{
		int i,j;
		File file = new File("population.txt");
		try 
		{
			BufferedWriter output = new BufferedWriter(new FileWriter(file,false));//true=append
			for(i=0;i<population.length;i++)
			{
				for(j=0;j<population[0].length;j++)
				//for(j=0;j<5;j++)
				{
					output.write(population[i][j]+" ");
				}
				output.write("\n");
			}	
			output.close();
	    } 
		catch ( IOException e ) 
	    {
	       e.printStackTrace();
	    }
	}
	
	public void write_2d_double(double [][] population, String filename)
	{
		int i,j;
		File file = new File(filename);
		try 
		{
			BufferedWriter output = new BufferedWriter(new FileWriter(file,false));//true=append
			for(i=0;i<population.length;i++)
			{
				for(j=0;j<population[0].length;j++)
				{
					output.write(population[i][j]+" ");
				}
				output.write("\n");
			}	
			output.close();
	    } 
		catch ( IOException e ) 
	    {
	       e.printStackTrace();
	    }
	}
	
	public void write_2d_double(double [][] population, String filename, boolean append)
	{
		int i,j;
		File file = new File(filename);
		try 
		{
			BufferedWriter output = new BufferedWriter(new FileWriter(file,append));//true=append
			for(i=0;i<population.length;i++)
			{
				for(j=0;j<population[0].length;j++)
				{
					output.write(population[i][j]+" ");
				}
				output.write("\n");
			}	
			output.close();
	    } 
		catch ( IOException e ) 
	    {
	       e.printStackTrace();
	    }
	}
	
	public void write_1d_double(double [] population, String filename, boolean append)
	{
		int i;
		File file = new File(filename);
		try 
		{
			BufferedWriter output = new BufferedWriter(new FileWriter(file,append));//true=append
			for(i=0;i<population.length;i++)
			{
				output.write(population[i]+" ");
			}
			output.write("\n");
			output.flush();
			output.close();
	    } 
		catch ( IOException e ) 
	    {
	       e.printStackTrace();
	    }
	}
	
	
	public static void write_front(int n, int m, int front)
	{
		File file = new File("front.txt");
		try 
		{
			BufferedWriter output = new BufferedWriter(new FileWriter(file,true));//true=append	
			
			//write front
			output.write(n+"\t"+m+"\t"+front+"\n");
			output.close();
			
	    } 
		catch ( IOException e ) 
	    {
	       e.printStackTrace();
	    }
	}
	
	
	
	public void write_mean(int option, int n, int m, int front, double []mean)
	{
		File file;
		if(option==1)
		{
			//file = new File("fixed_front_data_comparison_vs_front.txt");
			//file = new File("fixed_front_data_comparison_vs_obj.txt");
			//file = new File("cloud_data_comparison_vs_population_heap.txt");
			//file = new File("cloud_data_comparison_vs_obj_heap_small.txt");
			//file = new File("mixed_data_comparison.txt");  
			//file = new File("moea_data_comparison.txt"); 
			file = new File("worst_data_comparison.txt"); 
			//file = new File("bos_mixed_data_comparison_vs_obj.txt"); 
		}
		else
		{
			//file = new File("fixed_front_data_time_vs_front.txt");
			//file = new File("fixed_front_data_time_vs_obj.txt");
			//file = new File("cloud_data_time_vs_population_heap.txt");
			//file = new File("cloud_data_time_vs_obj_heap_small.txt");
			//file = new File("mixed_data_time.txt");
			//file = new File("moea_data_time.txt");
			file = new File("worst_data_time.txt"); 
			//file = new File("bos_mixed_data_time_vs_obj.txt"); 
		}
		try 
		{
			BufferedWriter output = new BufferedWriter(new FileWriter(file,true));//true=append
			
			//write runtime
			output.write(n+"\t"+m+"\t"+front+"\t");
			
			for(int i=0;i<mean.length;i++)
			{
				if(mean[i]!=0) output.write(mean[i]+"\t");
			}
			output.write("\n");
			output.flush();
			output.close();
			
	    } 
		catch ( IOException e ) 
	    {
	       e.printStackTrace();
	    }
	}
	public void write_problem(String problem)
	{
		File file1 = new File("moea_data_comparison_bbos.txt");  
		File file2 = new File("moea_data_time_bbos.txt");
		try 
		{
			BufferedWriter output1 = new BufferedWriter(new FileWriter(file1,true));//true=append
			BufferedWriter output2 = new BufferedWriter(new FileWriter(file2,true));//true=append
			output1.write(problem+"\n");
			output2.write(problem+"\n");
			output1.flush();
			output2.flush();
			output1.close();
			output2.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public double mean(double[] m) 
	{
	    double sum = 0;
	    for (int i = 0; i < m.length; i++) {
	        sum += m[i];
	    }
	    return sum / m.length;
	}
	public double variance(double[] m, int mean) 
	{
	    double sum = 0;
	    for (int i = 0; i < m.length; i++) {
	        sum += Math.pow(m[i]-mean,2);
	    }
	    return sum / m.length;
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
				
				String[] tokens = strLine.split(" ");
				strLine=strLine.trim();
				if(i==n)
				{
					break;
				}
				for(j=0;j<m;j++)
				{
					//System.out.print(tokens[j]+" ");
					try{
					population[i][j]=Double.parseDouble(tokens[j]);
					}
					catch (Exception e)
					{
						System.err.println("Error: " + e.getMessage());
					}
				}
				//System.out.println();
				i++;
			}
			br.close();
		}
		catch (IOException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
	}
}
