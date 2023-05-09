package bookstore.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import bookstore.util.JDBCutil;

import bookstore.domain.collection;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;


public class CollectionDao {
    // 添加收藏品
    public void addCollection(collection c) throws SQLException {
        try {
            Connection connection = JDBCutil.getConnection();
            PreparedStatement prepareStatement = connection.prepareStatement("insert into collection(id,user_id,product_id) values(?,?,?)");
            prepareStatement.setInt(1,c.getId());
            prepareStatement.setInt(2,c.getUser().getId());
            prepareStatement.setString(3,c.getProduct().getId());
            prepareStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    // 根据id查找商品
    public boolean findCollectByProductId(String id) throws SQLException {
        try {
            Connection connection = JDBCutil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from collection where product_id=?");
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        }catch (Exception e){
            e.printStackTrace();
        }
        throw new RuntimeException();
    }
    // 根据id查找商品
    public collection findCollectById(String product_id,int user_id) throws SQLException {
        try{
            Connection connection = JDBCutil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from collection where product_id=? and user_id=?");
            preparedStatement.setString(1,product_id);
            preparedStatement.setInt(2,user_id);
            ResultSet rs = preparedStatement.executeQuery();
            collection c = new collection();
            c.setId(rs.getInt("id"));
        }
        String sql = ;
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        return runner.query(sql, new BeanHandler<Collection>(Collection.class), product_id,user_id);
    }
    //根据user_id查找收藏品
    public List<Collection> findCollectByUserId(final int id) throws SQLException {
        String sql = "select * from collection where user_id=?";
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        return runner.query(sql, new ResultSetHandler<List<Collection>>(){
            public List<Collection> handle(ResultSet rs) throws SQLException{
                List<Collection> collections = new ArrayList<Collection>();
                while(rs.next()){
                    Collection collection = new Collection();
                    Product product = new Product();
                    User user = new User();
                    collection.setId(rs.getInt("id"));
                    product.setId(rs.getString("product_id"));
                    product.setName(rs.getString("product_name"));
                    product.setPrice(rs.getDouble("product_price"));
                    product.setImgurl(rs.getString("product_imgurl"));
                    collection.setProduct(product);
                    user.setId(id);
                    collection.setUser(user);
                    collections.add(collection);
                }
                return collections;
            }
        }, id);
    }
    // 获取数据总条数
    public int findAllCount(int id) throws SQLException {
        String sql = "select count(*) from collection where user_id=?";
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        Long count = (Long) runner.query(sql, new ScalarHandler(),id);
        return count.intValue();
    }
    // 获取当前页数据
    public List<Collection> findByPage(int currentPage, int currentCount,final int id) throws SQLException {
        // 要执行的sql语句
        String sql = null;
        // 参数
        Object[] obj = null;
        sql = "select * from collection  where user_id=? limit ?,?";
        obj = new Object[] { id,(currentPage - 1) * currentCount,
                currentCount, };
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        //return runner.query(sql, new BeanListHandler<Collection>(Collection.class),
        //obj);
        return runner.query(sql, new ResultSetHandler<List<Collection>>(){
            public List<Collection> handle(ResultSet rs) throws SQLException{
                List<Collection> collections = new ArrayList<Collection>();
                while(rs.next()){
                    Collection collection = new Collection();
                    Product product = new Product();
                    User user = new User();
                    collection.setId(rs.getInt("id"));
                    product.setId(rs.getString("product_id"));
                    product.setName(rs.getString("product_name"));
                    product.setPrice(rs.getDouble("product_price"));
                    product.setImgurl(rs.getString("product_imgurl"));
                    collection.setProduct(product);
                    user.setId(id);
                    collection.setUser(user);
                    collections.add(collection);
                }
                return collections;
            }
        }, obj);
    }
    //后台系统，根据id删除收藏品信息
    public void delCollectionById(String id) throws SQLException {
        String sql = "DELETE FROM collection WHERE product_id = ?";
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        runner.update(sql, id);
    }
}
