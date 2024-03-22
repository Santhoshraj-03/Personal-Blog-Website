package fullservletproject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jakarta.servlet.annotation.WebServlet;

@WebServlet("/Registration")
public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		String password = request.getParameter("pass");
//		String dob = request.getParameter("dob");

		Connection connection = null;
		RequestDispatcher requestDispatcher = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root", "Sk324212@");
			PreparedStatement preparedStatement = connection
					.prepareStatement("select * from fullservletwebsite where email=?");
			preparedStatement.setString(1, email);
			PreparedStatement preparedStatement2 = connection
					.prepareStatement("insert into fullservletwebsite(email,fname,lname,password) values(?,?,?,?)");
			preparedStatement2.setString(1, email);
			preparedStatement2.setString(2, fname);
			preparedStatement2.setString(3, lname);
			preparedStatement2.setString(4, password);
//				preparedStatement2.setString(5, dob);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				if (email.equals(resultSet.getString("email"))) {
					request.setAttribute("status", "User Exists");
					requestDispatcher = request.getRequestDispatcher("login.jsp");
				}
			} else {
				int affectedRows = preparedStatement2.executeUpdate();
				if (affectedRows > 0) {
					request.setAttribute("status", "success");
					requestDispatcher = request.getRequestDispatcher("login.jsp");
				} else {
					request.setAttribute("status", "failed");
					requestDispatcher = request.getRequestDispatcher("registration.jsp");
				}
			}
			requestDispatcher.forward(request, response);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
