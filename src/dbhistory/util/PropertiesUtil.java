package dbhistory.util;

import com.alibaba.fastjson.JSONObject;
import com.sun.security.ntlm.Client;
import dbhistory.model.DBConfig;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取配置
 */
public class PropertiesUtil {

    /**
     * 根据配置文件和产品名称转为model
     *
     * @param propertiesFile 配置文件名称
     * @param productCode    产品名称
     */
    public static DBConfig getDBConfig(String propertiesFile, String productCode) throws Exception {
        try {
            Map dbConfigs = read(propertiesFile);
            String product = (String) dbConfigs.get(productCode);
            DBConfig config = JSONObject.parseObject(product, DBConfig.class);
            return config;
        } catch (IOException e) {
            throw new Exception(e);
        }
    }


    /**
     * 根据配置文件名称读取并转为map
     */
    public static Map read(String propertiesFile) throws Exception {
        try {
            InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFile);
            Properties p = new Properties();
            p.load(inStream);
            Map<Object, Object> map = properties2map(p);
            return map;
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    /**
     * 配置文件读取转map
     */
    public static Map properties2map(Properties prop) {
        Map<Object, Object> map = new HashMap<Object, Object>();
        Enumeration enu = prop.keys();
        while (enu.hasMoreElements()) {
            Object obj = enu.nextElement();
            Object objv = prop.get(obj);
            map.put(obj, objv);
        }
        return map;
    }

}
