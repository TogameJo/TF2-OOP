package com.taskflow.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet") 

public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database"; // Thay 'your_database' bằng tên cơ sở dữ liệu của bạn
    private static final String DB_USER = "your_username"; // Thay 'your_username' bằng tên người dùng của bạn
    private static final String DB_PASSWORD = "your_password"; // Thay 'your_password' bằng mật khẩu của bạn

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // Lấy dữ liệu từ form
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm-password");

        // Kiểm tra mật khẩu có khớp hay không
        if (!password.equals(confirmPassword)) {
            response.sendRedirect("signup.jsp?error=passwordsDoNotMatch"); //gửi thông báo lỗi về sign-up
            return;
        }

        // Kết nối tới cơ sở dữ liệu
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);
                preparedStatement.executeUpdate();
            }
            response.sendRedirect("signup.jsp?success=registrationSuccessful");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("signup.jsp?error=registrationFailed");
        }
    }
}
