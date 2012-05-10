package cz.vutbr.fit.xzelin15.dp.servlet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;


public class ResultReader {
	
	
	public String readResult() 
	{
        File file = new File("/tmp/txresult.txt");
        StringBuffer contents = new StringBuffer();
        BufferedReader reader = null;

        try 
        {
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            // repeat until all lines is read
            while ((text = reader.readLine()) != null) 
            {
                contents.append(text)
                        .append(System.getProperty(
                                "line.separator"));
            }
        } 
        
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        } 
        
        catch (IOException e) 
        {
            e.printStackTrace();
        } 
        
        finally 
        {
            try 
            {
                if (reader != null) 
                {
                    reader.close();
                }
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }

        // show file contents here
        return contents.toString();
    }
}
