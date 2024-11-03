package Controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.*;


public class SqlGateWayServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

//         get a connection
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        String sqlStatement = request.getParameter("sqlStatement");
        String sqlResult = "";
        try {
//            mysql
//            Class.forName("com.mysql.jdbc.Driver");
//            // get a connection
//            String dbURL = "jdbc:mysql://localhost:3306/usertest";
//            String username = "root";
//            String password = "huydeptrai1";
//            Connection connection = DriverManager.getConnection(
//                    dbURL, username, password);
//            postgrestSql
            // Load the PostgreSQL JDBC driver
//            Class.forName("org.postgresql.Driver");
//
//// Get a connection to the PostgreSQL database
//            String dbURL = "jdbc:postgresql://dpg-crvru2tds78s738bivrg-a.singapore-postgres.render.com:5432/pg17";
//            String username = "pg17_user"; // Adjust if needed
//            String password = "8a5VBRYEERYztwqYgf3tNE3SaXsds5JE"; // Your PostgreSQL password
////            String dbURL = System.getenv("DATABASE_URL");
////            String username = System.getenv("DATABASE_USER");
////            String password = System.getenv("DATABASE_PASSWORD");
//            Connection connection = DriverManager.getConnection(dbURL, username, password);


            // create a statement
            Statement statement = connection.createStatement();

            // parse the SQL string
            sqlStatement = sqlStatement.trim();
            if (sqlStatement.length() >= 6) {
                String sqlType = sqlStatement.substring(0, 6);
                if (sqlType.equalsIgnoreCase("select")) {
                    // create the HTML for the result set
                    ResultSet resultSet
                            = statement.executeQuery(sqlStatement);
                    sqlResult = SQLUtil.getHtmlTable(resultSet);
                    resultSet.close();
                } else {
                    int i = statement.executeUpdate(sqlStatement);
                    if (i == 0) { // a DDL statement
                        sqlResult =
                                "<p>The statement executed successfully.</p>";
                    } else { // an INSERT, UPDATE, or DELETE statement
                        sqlResult =
                                "<p>The statement executed successfully.<br>"
                                        + i + " row(s) affected.</p>";
                    }
                }
            }
            statement.close();
            connection.close();
//        } catch (ClassNotFoundException e) {
//            sqlResult = "<p>Error loading the database driver: <br>"
//                    + e.getMessage() + "</p>";
        } catch (SQLException e) {
            sqlResult = "<p>Error executing the SQL statement: <br>"
                    + e.getMessage() + "</p>";
        }
     finally {
        pool.freeConnection(connection);
    }

        HttpSession session = request.getSession();
        session.setAttribute("sqlResult", sqlResult);
        session.setAttribute("sqlStatement", sqlStatement);

        String url = "/index.jsp";
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }
}
