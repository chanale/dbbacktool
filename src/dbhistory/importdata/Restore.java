package dbhistory.importdata;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import dbhistory.model.DBConfig;
import dbhistory.util.ShellUtil;

/**
 * 导入数据
 */
public class Restore {

    public static final String MYSQL = "mysql";

    /**
     * 执行导入
     *
     * @throws InterruptedException
     * @throws IOException
     */
    public static boolean loadExcute(DBConfig dbConfig, String filePath) throws IOException, InterruptedException {
        System.out.println("------------导入命令-----------");
        boolean result = false;
        String connectMysql = getLoadStr(dbConfig);
        String useDatabase = " use " + dbConfig.getTargetDataBase();
        String sourceShell = " source " + filePath;
        List<String> shellList = new ArrayList<>();
        shellList.add(connectMysql);
        shellList.add(useDatabase);
        shellList.add(sourceShell);
        System.out.println("connectMysql:  " + connectMysql);
        System.out.println("useDatabase:  " + useDatabase);
        System.out.println("sourceShell:  " + sourceShell);
        ShellUtil.exeLoad(shellList);
        result = true;
        return result;
    }

    /**
     * 获取导入数据命令
     *
     * @param dbConfig 数据库配置文件
     * @return 导入命令
     */
    public static String getLoadStr(DBConfig dbConfig) {
        StringBuffer shell = new StringBuffer();
        shell.append(MYSQL)
                .append(" -f")
                .append(" -u").append(dbConfig.getUser())
                .append(" -p").append(dbConfig.getPassword())
                .append(" -h").append(dbConfig.getHost())
                .append(" -P").append(dbConfig.getPort());
        return shell.toString();
    }

}


