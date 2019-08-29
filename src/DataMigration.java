import dbhistory.backup.BackUp;
import dbhistory.deldata.DeleteData;
import dbhistory.importdata.Restore;
import dbhistory.model.DBConfig;
import dbhistory.util.PropertiesUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataMigration {

    public static final String DBCONFIGS = "product.properties";

    /**
     * @param args
     * @author lizhzg
     * 执行数据库备份还原的主函数
     */
    public static void main(String[] args) {
        Map<String, String> productMap = new HashMap();
        try {
            productMap = PropertiesUtil.read(DBCONFIGS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<DBConfig> dbList = getDBList(productMap);
        System.out.println("产品及对应编码如下");
        for (DBConfig dbConfig : dbList) {
            System.out.printf("%1s-%1s  ", dbConfig.getCourseName(), dbConfig.getProductCode());
        }

        System.out.println();
        System.out.println("请正确输入要归档的产品编码(如V财请输入fin):");
        Scanner scanner = new Scanner(System.in);
        String productCode = scanner.next();
        while (!productMap.containsKey(productCode.toLowerCase())) {
            System.out.println("请先正确输入要归档的产品编码:");
            productCode = scanner.next();
        }

        System.out.println("请正确输入要归档的时间(格式如2018-12-31):");
        String regex = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])" +
                "-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((" +
                "([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)(" +
                "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-" +
                "(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((" +
                "([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29) ";
        String historyTime = scanner.next();
        while (!historyTime.matches(regex)) {
            System.out.println("请先正确输入要归档的时间格式:");
            historyTime = scanner.next();
        }

        //执行dump
        String backupFilePath = null;
        DBConfig dbConfig = null;
        try {
            dbConfig = PropertiesUtil.getDBConfig(DBCONFIGS, productCode);
            System.out.println("开始备份");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long start = System.currentTimeMillis();
            backupFilePath = BackUp.backupExcute(dbConfig);
            long end = System.currentTimeMillis();
            String runingTime = getDifferenceTime(start, end);
            System.out.println("数据库备份命令执行完成，用时：" + runingTime);
        } catch (Exception e) {
            System.out.println("数据库备份命令执行失败" + e);
        }

        //执行导入
        boolean result = false;
        try {
            result = Restore.loadExcute(dbConfig, backupFilePath);
        } catch (Exception e) {
            System.out.println("数据库导入命令执行失败" + e);
        }
        //删除
        if (result && backupFilePath != null && new File(backupFilePath).exists()) {
            long start = System.currentTimeMillis();
            DeleteData.deleteData(dbConfig, historyTime);
            long end = System.currentTimeMillis();
            String runingTime = getDifferenceTime(start, end);
            System.out.println("删除数据完成，用时：" + runingTime);
        }
        System.out.println("数据处理完毕！");
    }

    /**
     * 获取所有产品的配置文件
     */
    private static List<DBConfig> getDBList(Map<String, String> productMap) {
        List<DBConfig> dbList = new ArrayList<>();
        List<String> productCodeList = new ArrayList<>();
        for (String productCode : productMap.keySet()) {
            productCodeList.add(productCode);
        }
        try {
            for (String productCode : productCodeList) {
                dbList.add(PropertiesUtil.getDBConfig(DBCONFIGS, productCode));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbList;
    }

    /**
     * 时间差
     */
    public static String getDifferenceTime(long strTime1, long strTime2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long ms = 0;
        long diff;
        if (strTime1 < strTime2) {
            diff = strTime2 - strTime1;
        } else {
            diff = strTime1 - strTime2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        ms = (diff - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - sec * 1000);
        return day + "天" + hour + "小时" + min + "分" + sec + "秒" + ms + "毫秒";
    }
}
