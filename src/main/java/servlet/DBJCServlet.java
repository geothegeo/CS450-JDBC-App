package servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.util.*;
import java.sql.*;

import java.lang.Math;

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

	private Integer givesAuthor = 0;
	private Integer firstSet = 1;

	private String id = null;
	private String author = null;
	private String title = null;
	private String year = null;
	private String type = null;
	private String sort = null;
	private String offset = "0";

	private String limit = null;
	private String interval = null;
	private String numRows = "0";

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
     response.setContentType("text/html");
     PrintWriter out = response.getWriter();

	  	String limitString = "";
		String sqlString = "SELECT p.PublicationID, Title, Year, Type, Summary, URL, GROUP_CONCAT(Author ORDER BY Author ASC SEPARATOR ', ') AS Authors" +
									" FROM Authors a, Publications p WHERE a.publicationID = p.publicationID";

		String iBAuthor = request.getParameter("bAuthor");
		if ((iBAuthor != null) && (iBAuthor.length() > 0)) {
			givesAuthor = Integer.parseInt(iBAuthor);
		}		

		String iBSet = request.getParameter("bSet");
		if ((iBSet != null) && (iBSet.length() > 0)) {
			firstSet = Integer.parseInt(iBSet);
		}

		String iOffset = request.getParameter("offset");
		if ((iOffset != null) && (iOffset.length() > 0)) {
			offset = iOffset;
		}

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
				givesAuthor = 1;
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
						
			if (givesAuthor == 0)
				sqlString += " GROUP BY Title, Year, Type, Summary, URL, p.PublicationID";

			String iSort = request.getParameter("sort");
			if ((iSort != null) && (iSort.length() > 0)) {
				sort = iSort;	
				if(sort.equals("Author") && (givesAuthor == 0))
					sort = "Authors";
				sqlString += " ORDER BY " + sort;
			}
					
			String iLimit = request.getParameter("limit");
			if ((iLimit != null) && (iLimit.length() > 0)) {
				limit = iLimit;
				String iInterval = request.getParameter("interval");
				if ((iInterval != null) && (iInterval.length() > 0)) {
					interval = iInterval;
				} else {
					interval = limit;
					offset = "-" + limit;
				}
				offset = "" + (Integer.parseInt(offset) + Integer.parseInt(interval));
				limitString += " LIMIT " + offset + ", " + limit;
			}

			String iNumRows = request.getParameter("nRows");
			if ((firstSet == 1) && (iNumRows != null) && (iNumRows.length() > 0)) {
				numRows = iNumRows;	
				firstSet = 0;
			} else {
				try (ResultSet rs = stmt.executeQuery(sqlString)) {
					rs.last();
					numRows = Integer.toString(rs.getRow());	
				} 
				catch (SQLException ex) {
					out.println(ex.getMessage());
				}
			}		
		}

		sqlString += limitString;

	  printHead(out);
	  printBody(out, sqlString);
	  printTail(out);

  }

  @Override
  public void doGet (HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String sqlString = "SELECT p.PublicationID, Title, Year, Type, Summary, URL, GROUP_CONCAT(Author ORDER BY Author ASC SEPARATOR ', ') AS Authors" +
									" FROM Authors a, Publications p WHERE a.publicationID = p.publicationID AND p.PublicationID = '1'" +
									" GROUP BY Title, Year, Type, Summary, URL, p.PublicationID";		
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
		out.println("<h2>DBJC Results Table:</h2>");
		out.println("<p>Please use the back button to go back to the main page and refresh the page before doing another query.</p>");
		out.println("<p>" + sqlString + "</p>");
		out.println("<p>" + numRows + " rows returned.</p>");
		out.println("<p>Displaying results: " + (Integer.parseInt(offset) + 1) + " - ");
		out.println(Math.min(Integer.parseInt(offset) + Integer.parseInt(limit), Integer.parseInt(numRows)) + "</p>");
		if(!((id != null) && (id.length() > 0))) {
			out.println("<form id=\"inputForm\" class=\"form-inline\" method=\"post\" action=\"" + Servlet + "\">");
			if(author != null)
				out.println("<input type=\"hidden\" id=\"author\" name=\"author\" value=\"" + author + "\">");
			if(title != null)	
				out.println("<input type=\"hidden\" id=\"title\" name=\"title\" value=\"" + title + "\">");
			if(year != null)
				out.println("<input type=\"hidden\" id=\"year\" name=\"year\" value=\"" + year + "\">");
			out.println("<input type=\"hidden\" id=\"type\" name=\"type\" value=\"" + type + "\">");
			out.println("<input type=\"hidden\" id=\"sort\" name=\"sort\" value=\"" + sort + "\">");
			
			out.println("<input type=\"hidden\" id=\"offset\" name=\"offset\" value=\"" + offset + "\">");
			out.println("<input type=\"hidden\" id=\"limit\" name=\"limit\" value=\"" + limit + "\">");
			out.println("<input type=\"hidden\" id=\"nRows\" name=\"nRows\" value=\"" + numRows + "\">");

			out.println("<input type=\"hidden\" id=\"bAuthor\" name=\"bAuthor\" value=\"" + Integer.toString(givesAuthor)  + "\">");
			out.println("<input type=\"hidden\" id=\"bSet\" name=\"bSet\" value=\"" + Integer.toString(firstSet) + "\">");

			out.println("<div class=\"container-fluid\"><div class=\"row\">");
			if (Integer.parseInt(offset) == 0) {
				out.println("<div class=\"col-md-1\">");
				out.println("</div>");
			} else {
				out.println("<div class=\"col-md-1\">");
				out.println("<button name=\"interval\" type=\"submit\" class=\"btn btn-primary\" value=\"-" + limit + "\">Previous</button>");
				out.println("</div>");
			}
			if ((Integer.parseInt(offset) + Integer.parseInt(limit)) >= Integer.parseInt(numRows)) {
				out.println("<div class=\"col-md-1 offset-md-10\">");
				out.println("</div>");
			} else {
				out.println("<div class=\"col-md-1 offset-md-10\">");
				out.println("<button name=\"interval\" type=\"submit\" class=\"btn btn-primary\" value=\"+" + limit + "\">Next</button>");
				out.println("</div>");
			}
			out.println("</div></div>");
			out.println("</form>");
		}
		out.println("<p>id: " + id + ", author: " + author + ", title: " + title + ", year " + year + ", type: " + type + ", sort: " + sort 
					+ ", offset: " + offset + ", limit: " + limit + ", numRows: " + numRows + ", interval: " + interval + "</p>");
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
				if(givesAuthor == 0)
					out.println("<td>" + rs.getString("Authors") + "</td>");
				else
					out.println("<td>" + rs.getString("Author") + "</td>");
				out.println("<td>" + rs.getString("year") + "</td>");
				out.println("<td>" + rs.getString("type") + "</td>");
				out.println("<td>" + rs.getString("summary") + "</td>");
				out.println("<td><a href= '" + rs.getString("url") + "'>Click here to access the article</a></td>");
				out.println("</tr>");
			}
			out.println("</tbody></table>");

			// try {
			// 	rs.close();
			// 	stmt.close();
			// } catch(SQLException e) {
			// 	out.println(e.getMessage());
			// }
		} 
		catch (SQLException ex) {
			out.println(ex.getMessage());
		}

	}
}

		    
			    
				   
				   
			       

