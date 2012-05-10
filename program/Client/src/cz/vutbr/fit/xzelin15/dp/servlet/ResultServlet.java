package cz.vutbr.fit.xzelin15.dp.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResultServlet extends HttpServlet 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8458448798054196024L;
	
	
	private ServletContext context;
	
	/**
	 * 
	 */
	public void init(ServletConfig config)  throws ServletException 
	{ 
		super.init(config); 
		context = config.getServletContext();
    } 
	
	
			     
	
	/**
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		processRequest(request, response);
    }
	
	/**
	 * 
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		processRequest(request, response);
    }
	
	
	protected void processRequest(HttpServletRequest request,HttpServletResponse response) 
		    throws ServletException, java.io.IOException 
    {
		ResultReader reader = new ResultReader();
		
		String result = reader.readResult();
		
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print(result);
		
		
		//context.getRequestDispatcher("/index.jsp").forward(request, response);	
		
		
    }
    
}
	