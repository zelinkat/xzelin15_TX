package cz.vutbr.fit.xzelin15.dp.consumer;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class ResultWriter {
	
	public void writeResult(String result)
	{
		try
		{
	  	  // Create file 
	  	  FileWriter fstream = new FileWriter("/tmp/txresult.txt");
	  	  BufferedWriter out = new BufferedWriter(fstream);
	  	  out.write(result);
	  	  //Close the output stream
	  	  out.close();
	  	}
		catch (Exception e)
		{//Catch exception if any
	  		  System.err.println("Error: " + e.getMessage());
	  	}
	}
}
