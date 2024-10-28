package com.taskflow.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taskflow.utils.DatabaseConnection;

@WebServlet("/RegisterServlet") 

public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // ghi đè phương thức doPost từ HttpSrvlet để xử lý yêu cầu và phản hồi
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // Lấy dữ liệu từ form
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm-password");

        // Kiểm tra mật khẩu có khớp hay không
        if (!password.equals(confirmPassword)) {
            response.sendRedirect("signup.html?error=passwordsDoNotMatch"); //gửi thông báo mật khẩu không khớp về sign-up
            return;
        }

        // Kết nối tới cơ sở dữ liệu
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);  //gán giá trị cho value ? theo thứ tự
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);
                preparedStatement.executeUpdate(); //cập nhật giá trị(insert)
            }
            response.sendRedirect("signup.html?success=registrationSuccessful"); //đăng nhập thành công vè sign up
        } catch (Exception e) { //nếu cho ngoaij lệ in ra thông báo lỗi
            e.printStackTrace();
            response.sendRedirect("signup.html?error=registrationFailed");
        }
    }
}
