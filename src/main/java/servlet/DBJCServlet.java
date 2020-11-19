package servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.util.*;
import java.sql.*;

/*require mysql dependency in your pom.xml
 <dependencies>
...

    <dependency>
    	<groupId>mysql</groupId>
    	<artifactId>mysql-connector-java</artifactId>
    	<version>8.0.15</version>
    </dependency>
...
*/

@WebServlet(name = "DBJC", urlPatterns = {"/search"})
public class DBJCServlet extends HttpServlet {
  static String Domain  = "https://cs450-jdbc.herokuapp.com/";
  static String Path    = "";
  static String Servlet = "search";

    private Statement stmt;
    private Connection conn;

    private String URL="jdbc:mysql://p2jxdb.mysql.database.azure.com:3306/papers?useSSL=true&requireSSL=false&serverTimezone=UTC";
    private String username="db_user@p2jxdb";
    private String password="zksTu8%d";
   
	static String Style1 = "https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css";
	static String Style2 = "https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js";
	static String Style3 = "https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js";

	static String BJS1 = "https://code.jquery.com/jquery-3.3.1.slim.min.js";
	static String BJS2 = "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js";
	static String BJS3 = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js";



   public void init (ServletConfig config) throws ServletException {
		super.init(config);
		try{
			conn=DriverManager.getConnection(URL,username,password);
			stmt=conn.createStatement();
		}
		catch (Exception e){
			e.printStackTrace();
			conn=null;
		}
   }

   public void destroy(){
		try{
			stmt.close();
			conn.close();
		}
			catch (Exception e){
			System.err.println("problem closing the database");
		}
   }

	@Override
  public void doPost (HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException
  {


	   Boolean givesId = false;

	  	String id = "";
		String author = "";
		String title = "";
		String year = "";
		String type = "";
		String sort = "";
		String limit = "";

   	String iId = request.getParameter("pubId");
		if ((iId != null) && (iId.length() > 0)) {
			id = iId;
			givesId = true;
		}			
		else {
			String iAuthor = request.getParameter("author");
			if ((iAuthor != null) && (iAuthor.length() > 0))
				author = iAuthor;
			String iTitle = request.getParameter("title");
			if ((iTitle != null) && (iTitle.length() > 0))
				title = iTitle;
			String iYear = request.getParameter("year");
			if ((iYear != null) && (iYear.length() > 0))
				year = iYear;			
			String iType = request.getParameter("type");
			if ((iType != null) && (iType.length() > 0))
				type = iType;			
			String iSort = request.getParameter("sort");
			if ((iSort != null) && (iSort.length() > 0))
				sort = iSort;		
			String iLimit = request.getParameter("limit");
			if ((iLimit != null) && (iLimit.length() > 0))
				limit = iLimit;
		}

     response.setContentType("text/html");
     PrintWriter out = response.getWriter();

     out.println("<html>");
     out.println("");
     out.println("<head>");
     out.println("<title>JBDC Results Table</title>");
     out.println(" <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
     out.println(" <link rel=\"stylesheet\" href=\"" + Style1 + "\">");
     out.println(" <link rel=\"stylesheet\" href=\"" + Style2 + "\">");
     out.println(" <link rel=\"stylesheet\" href=\"" + Style3 + "\">");

     out.println(" <script src=\"" + BJS1 + "\"></script>");
     out.println(" <script src=\"" + BJS2 + "\"></script>");
     out.println(" <script src=\"" + BJS3 + "\"></script>");
     out.println("</head>");

	  out.println("<body>");
	  out.println("<p>");
	  out.println("Results Table.");
	  out.println("</p>");

	  out.println(printTable("SELECT * FROM authors"));

	  out.println("</body>");
     out.println("");
     out.println("</html>");

  }

  @Override
  public void doGet (HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("<html>");
		out.println("");
		out.println("<head>");
		out.println("<title>JBDC Results Table</title>");
		out.println(" <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
		out.println(" <link rel=\"stylesheet\" href=\"" + Style1 + "\">");
		out.println(" <link rel=\"stylesheet\" href=\"" + Style2 + "\">");
		out.println(" <link rel=\"stylesheet\" href=\"" + Style3 + "\">");

		out.println(" <script src=\"" + BJS1 + "\"></script>");
		out.println(" <script src=\"" + BJS2 + "\"></script>");
		out.println(" <script src=\"" + BJS3 + "\"></script>");
		out.println("</head>");

		out.println("<body>");
		out.println("<p>");
		out.println("Use the back button to go back to the main page.");
		out.println("</p>");

		out.println(printTable("SELECT * FROM authors"));

		out.println("</body>");
		out.println("");
		out.println("</html>");

		// try{
		// rs.close();
		// stmt.close();
		// } catch(SQLException e) {
		// 	System.out.println(e.getMessage());
		// }
	}

	private String printTable(String sqlString) {
		
		String result = "";
		// String sql = "SELECT * FROM authors";
		try (ResultSet rs = stmt.executeQuery(sqlString)) {
			while (rs.next()) {
				result = rs.getString("publicationID") + "\t" + 
							rs.getString("author") + "\n";
			}
		} 
		catch (SQLException ex) {
			result = ex.getMessage();
		}

		return result;

	}
}

		    
			    
				   
				   
			       

