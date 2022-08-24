package com.foo;

import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Test {

    public static void main(String[] args) throws SQLException, IOException {
        //createOrder(1,1);
        //createOrder(2,2);
        //testQuery(2, 2);
        testQuery();
    }

    public static void testQuery() throws IOException, SQLException {
        URL url = Test.class.getClassLoader().getResource("sharding-jdbc.yaml");
        String filePath;
        if (url == null) {
            throw new IOException("文件不存在");
        }
        filePath = url.getPath();
        File ymlFile = new File(filePath);
        DataSource dataSource = YamlShardingSphereDataSourceFactory.createDataSource(ymlFile);
        String sql = "SELECT count(*) FROM t_order o";
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createOrder(Integer orderId, Integer userId) throws IOException, SQLException {
        URL url = Test.class.getClassLoader().getResource("sharding-jdbc.yaml");
        if (url == null) {
            throw new IOException("文件不存在");
        }
        String filePath = url.getPath();
        File ymlFile = new File(filePath);
        DataSource dataSource = YamlShardingSphereDataSourceFactory.createDataSource(ymlFile);
        String sql = "insert into t_order(order_id, user_id) values(?, ?)";
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, userId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
