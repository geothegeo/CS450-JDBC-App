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

	private String id = null;
	private String author = null;
	private String title = null;
	private String year = null;
	private String type = null;
	private String sort = null;
	private String offset = "0";
	private String limit = null;
	private String interval = null;

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
		String sqlString = "SELECT p.PublicationID, Title, GROUP_CONCAT(DISTINCT Author ORDER BY Author ASC SEPARATOR ', ') AS Authors, " +
									"Year, Type, Summary, URL FROM Authors a, Publications p WHERE a.publicationID = p.publicationID";

   	String iId = request.getParameter("pubId");
		if ((iId != null) && (iId.length() > 0)) {
			id = iId;
			sqlString += " AND p.PublicationID = '" + id + "'";
			sqlString += " GROUP BY Title, Year, Type, Summary, URL, p.PublicationID";
		}			
		else {
			String iAuthor = request.getParameter("author");
			if ((iAuthor != null) && (iAuthor.length() > 0)) {
				author = iAuthor;
				sqlString = "SELECT p.PublicationID, Title, Author, Year, Type, Summary, URL " +
								"FROM Authors a, Publications p WHERE a.publicationID = p.publicationID";
				sqlString += " AND Author LIKE '%" + author + "%'";
			}
				
			String iTitle = request.getParameter("title");
			if ((iTitle != null) && (iTitle.length() > 0)) {
				title = iTitle;
				sqlString += " AND Title LIKE '%" + title + "%'";
			}
				
			String iYear = request.getParameter("year");
			if ((iYear != null) && (iYear.length() > 0)) {
				year = iYear;	
				sqlString += " AND Year = '" + year + "'";
			}

			String iType = request.getParameter("type");
			if ((iType != null) && (iType.length() > 0)) {
				type = iType;	
				sqlString += " AND Type = '" + type + "'";
			}
						
			sqlString += " GROUP BY Title, Year, Type, Summary, URL, p.PublicationID";

			String iSort = request.getParameter("sort");
			if ((iSort != null) && (iSort.length() > 0)) {
				sort = iSort;	
				sqlString += " ORDER BY " + sort + "";
			}
					
			String iLimit = request.getParameter("limit");
			if ((iLimit != null) && (iLimit.length() > 0)) {
				limit = iLimit;
				String iInterval = request.getParameter("interval");
				if ((iInterval != null) && (iInterval.length() > 0)) {
					interval = iInterval;
					offset = Integer.parseInt(offset) + Integer.parseInt(interval));
				}
				sqlString += " LIMIT " + offset + ", " + limit + "";
			}
				
		}

     response.setContentType("text/html");
     PrintWriter out = response.getWriter();

	  printHead(out);
	  printBody(out, sqlString);
	  printTail(out);

  }

  @Override
  public void doGet (HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String sqlString = "SELECT p.PublicationID, Title, Year, Type, Summary, URL, GROUP_CONCAT(DISTINCT Author ORDER BY Author ASC SEPARATOR ', ') AS Authors" +
									" FROM Authors a, Publications p WHERE a.publicationID = p.publicationID GROUP BY Title, Year, Type, Summary, URL, p.PublicationID";

		printHead(out);
		printBody(out, sqlString);
		printTail(out);
	}

   private void printHead (PrintWriter out) {
		out.println("<html>");
		out.println("");
		out.println("<head>");
		out.println("<title>JBDC Results Table</title>");
		out.println(" <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
		out.println(" <link rel=\"stylesheet\" href=\"" + Style1 + "\">");
		out.println(" <link rel=\"stylesheet\" href=\"" + Style2 + "\">");
		out.println(" <link rel=\"stylesheet\" href=\"" + Style3 + "\">");
		out.println("</head>");
	}

	private void printBody (PrintWriter out, String sqlString) {
		out.println("<body>");
		out.println("<p>");
		out.println("Use the back button to go back to the main page.");
		out.println("</p>");
		out.println("<form id=\"inputForm\" class=\"form-inline\" method=\"post\" action=\"" + Servlet + "\" onsubmit=\"return configForm()\"");
		out.println("<input type=\"hidden\" id=\"pubId\" name=\"pubId\" value=\"" + id + "\">");
		out.println("<input type=\"hidden\" id=\"author\" name=\"author\" value=\"" + author + "\">");
		out.println("<input type=\"hidden\" id=\"title\" name=\"title\" value=\"" + title + "\">");
		out.println("<input type=\"hidden\" id=\"year\" name=\"year\" value=\"" + year + "\">");
		out.println("<input type=\"hidden\" id=\"type\" name=\"type\" value=\"" + type + "\">");
		out.println("<input type=\"hidden\" id=\"sort\" name=\"sort\" value=\"" + sort + "\">");
		out.println("<input type=\"hidden\" id=\"offset\" name=\"offset\" value=\"" + offset + "\">");
		out.println("<input type=\"hidden\" id=\"limit\" name=\"limit\" value=\"" + limit + "\">");
		out.println("<div class=\"container-fluid\"><div class=\"row\">");
		if (Integer.parseInt(offset) == 0) {
			out.println("<div class=\"col-md-1\">");
			out.println("</div>");
		} else {
			out.println("<div class=\"col-md-1\">");
			out.println("<button name=\"interval\" type=\"submit\" class=\"btn btn-primary\" value=\"-" + limit + "\">Previous</button>");
			out.println("</div>");
		}
		if (Integer.parseInt(offset) + Integer.parseInt(limit) > 90) {
			out.println("<div class=\"col-md-1 offset-md-10\">");
			out.println("</div>");
		} else {
			out.println("<div class=\"col-md-1 offset-md-10\">");
			out.println("<button name=\"interval\" type=\"submit\" class=\"btn btn-primary\" value=\"+" + limit + "\">Next</button>");
			out.println("</div>");
		}
    	out.println("</div></div>");
    	out.println("</form>");
		printTable(out, sqlString);
		out.println("</body>");
	}

	private void printTail (PrintWriter out) {
		out.println("");
		out.println(" <script src=\"" + BJS1 + "\"></script>");
		out.println(" <script src=\"" + BJS2 + "\"></script>");
		out.println(" <script src=\"" + BJS3 + "\"></script>");
		out.println("</html>");
	}


	private void printTable(PrintWriter out, String sqlString) {
		
		try (ResultSet rs = stmt.executeQuery(sqlString)) {
			out.println("<table class=\"table\">");
			out.println("<thead><tr>");
			out.println("<th scope=\"col\">ID</th>");
			out.println("<th scope=\"col\">Title</th>");
			out.println("<th scope=\"col\">Author(s)</th>");
			out.println("<th scope=\"col\">Year</th>");
			out.println("<th scope=\"col\">Type</th>");
			out.println("<th scope=\"col\">Summary</th>");
			out.println("<th scope=\"col\">URL</th>");
			out.println("</tr></thead>");

			out.println("<tbody>");
			while (rs.next()) {
			
				out.println("<tr>");
				out.println("<td>" + rs.getString("publicationID") + "</td>");
				out.println("<td>" + rs.getString("title") + "</td>");
				out.println("<td>" + rs.getString("Authors") + "</td>");
				out.println("<td>" + rs.getString("year") + "</td>");
				out.println("<td>" + rs.getString("type") + "</td>");
				out.println("<td>" + rs.getString("summary") + "</td>");
				out.println("<td><a href= '" + rs.getString("url") + "'>Click here to access the article</a></td>");
				out.println("</tr>");
			}
			out.println("</tbody></table>");

		} 
		catch (SQLException ex) {
			out.println(ex.getMessage());
		}

		// try {
		// 	rs.close();
		// 	stmt.close();
		// } catch(SQLException e) {
		// 	System.out.println(e.getMessage());
		// }

	}
}

		    
			    
				   
				   
			       

