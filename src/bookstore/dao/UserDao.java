package bookstore.dao;

import java.sql.*;

import bookstore.domain.user;
import bookstore.util.JDBCutil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
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
                    preparedStatement.setInt(1, user.getId());
                    preparedStatement.setString(2, user.getUsername());
                    preparedStatement.setString(3, user.getPassword());
                    preparedStatement.setString(4, user.getGender());
                    preparedStatement.setString(5, user.getEmail());
                    preparedStatement.setString(6, user.getTelephone());
                    preparedStatement.setString(7, user.getIntroduce());
                    preparedStatement.setString(8, user.getActiveCode());

                    preparedStatement.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    // 根据激活码查找用户
    public boolean findUserByActiveCode(String activeCode) throws SQLException {
        String sql = "select * from user where active_code=?";
        try{
            Connection connection = JDBCutil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        }
    }

    // 激活用戶
    public void activeUser(String activeCode) throws SQLException {
        String sql = "update user set state=? where active_code=?";
        try{
            Connection connection = JDBCutil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,1);
            preparedStatement.setString(2,activeCode);
            preparedStatement.executeUpdate();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //根据用户名与密码查找用户
    public user findUserByUsernameAndPassword(String username, String password) throws SQLException {
        String sql="select * from user where username=? and password=?";
        try{
            Connection connection = JDBCutil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);

        }
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        return runner.query(sql, new BeanHandler<User>(User.class),username,password);
    }

    //根据用户id查找用户
    public User findUserByUserid(int id) throws SQLException {
        String sql="select * from user where id=?";
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        return runner.query(sql, new BeanHandler<User>(User.class),id);
    }

}
