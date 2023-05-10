package bookstore.dao;

import java.sql.*;

import bookstore.domain.user;
import bookstore.util.JDBCutil;
import java.sql.SQLException;


public class UserDao {
    // 添加用户
    public boolean register(user user) throws SQLException {
        Connection connection = JDBCutil.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from user where username='" + user.getUsername() + "'");
            if (resultSet.next()) {
                return true;
            } else {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(
                            "insert into user (username,password,gender,email,telephone,introduce,active_code) values(?,?,?,?,?,?,?)");
                    preparedStatement.setString(1, user.getUsername());
                    preparedStatement.setString(2, user.getPassword());
                    preparedStatement.setString(3, user.getGender());
                    preparedStatement.setString(4, user.getEmail());
                    preparedStatement.setString(5, user.getTelephone());
                    preparedStatement.setString(6, user.getIntroduce());
                    preparedStatement.setString(7, user.getActiveCode());

                    preparedStatement.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 根据激活码查找用户
    public boolean findUserByActiveCode(String activeCode) throws SQLException {
        String sql = "select * from user where active_code=?";
        try {
            Connection connection = JDBCutil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 激活用戶
    public void activeUser(String activeCode) throws SQLException {
        String sql = "update user set state=? where active_code=?";
        try {
            Connection connection = JDBCutil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, activeCode);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //根据用户名与密码查找用户
    public user findUserByUsernameAndPassword(String username, String password) throws SQLException {
        String sql = "select * from user where username=? and password=?";
        try {
            Connection connection = JDBCutil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user u = new user();
                u.setActiveCode(rs.getString("active_code"));
                return u;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //根据用户id查找用户
    public user findUserByUserid(int id) throws SQLException {
        String sql = "select * from user where id=?";
        try {
            Connection connection = JDBCutil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                user u = new user();
                u.setActiveCode(rs.getString("active_code"));
                u.setId(rs.getInt("id"));
                u.setEmail(rs.getString("email"));
                u.setGender(rs.getString("gender"));
                u.setTelephone(rs.getString("telephone"));
                u.setIntroduce(rs.getString("introduce"));
                u.setPassword(rs.getString("password"));
                u.setRegistTime(rs.getTimestamp("regist_time"));
                u.setRole(rs.getString("role"));
                u.setUsername(rs.getString("username"));
                u.setState(rs.getInt("state"));
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
