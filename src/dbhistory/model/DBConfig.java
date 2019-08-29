package dbhistory.model;

/**
 * 数据库配置信息
 */
public class DBConfig {

    private String host;
    private String port;
    private String database;
    private String user;
    private String password;
    private String targetDataBase; //备份数据存放数据库
    private String courseName;//课程名称
    private String productCode;//课程编码

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTargetDataBase() {
        return targetDataBase;
    }

    public void setTargetDataBase(String targetDataBase) {
        this.targetDataBase = targetDataBase;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "DBConfig{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", database='" + database + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
