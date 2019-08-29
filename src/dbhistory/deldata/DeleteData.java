package dbhistory.deldata;

import dbhistory.model.DBConfig;
import dbhistory.util.JdbcUtil;
import dbhistory.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 删除数据
 */
public class DeleteData {
    public static final String PROPERTIES = ".properties";

    /**
     * 删除数据
     *
     * @param dbConfig    数据源配置
     * @param historyTime 删除的时间点
     */
    public static void deleteData(DBConfig dbConfig, String historyTime) {
        Map read = null;
        try {
            read = PropertiesUtil.read(dbConfig.getProductCode() + PROPERTIES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String delSql = (String) read.get("delSql");
        delSql = delSql.replace("$", getHistoryTime(historyTime));
        List<String> sqlList = Arrays.asList(delSql.split("#"));
        String mysqlUrl =
                "jdbc:mysql://" + dbConfig.getHost() + ":" + dbConfig.getPort() + "/" + dbConfig.getDatabase();
        Connection connection = null;
        Statement statement = null;
        try {
            connection = JdbcUtil.connected(mysqlUrl, dbConfig.getUser(), dbConfig.getPassword());
            statement = connection.createStatement();
            List<String> tableNames = new ArrayList<>();
            for (String sql : sqlList) {
                sql = sql + ";";
                String tableName = "";
                if (sql.indexOf("where") > -1) {
                    tableName = sql.substring(sql.indexOf("from") + 4, sql.indexOf("where"));
                } else {
                    tableName = sql.substring(sql.indexOf("from") + 4);
                }
                tableNames.add(tableName);
                System.out.println(sql);
            }
            int[] ints = statement.executeBatch();
            for (int i = 0; i < ints.length; i++) {
                System.out.println(tableNames.get(i) + "清理" + ints[i] + "行");
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 获取时间毫秒值
     *
     * @param historyTime 输入格式"2018-01-01"
     */
    private static String getHistoryTime(String historyTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(historyTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(date.getTime());
    }
}
