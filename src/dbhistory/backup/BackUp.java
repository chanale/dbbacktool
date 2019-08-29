package dbhistory.backup;

import dbhistory.model.DBConfig;
import dbhistory.util.ShellUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 导出数据
 */
public class BackUp {

    public static final String MYSQLDUMP = "mysqldump";

    /**
     * 执行备份
     *
     * @throws InterruptedException
     * @throws IOException
     */
    public static String backupExcute(DBConfig config) throws IOException, InterruptedException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String now = sdf.format(new Date());
        String backupFilePath = config.getDatabase() + now;
        backupFilePath = backupFilePath + ".sql";
        String backupSql = getBackupDataStr(config, backupFilePath);
        System.out.println(backupSql);
        ShellUtil.exeDump(backupSql);
        return backupFilePath;
    }

    /**
     * 只备份表(无drop)和数据,不导出表结构的脚本
     */
    public static String getBackupDataStr(DBConfig dbConfig,
                                          String backupFilePath) {
        StringBuffer shell = new StringBuffer();
        shell.append(MYSQLDUMP)
//                .append(" -d ")//加d参数只导表结构
                .append(" --skip-add-drop-table ")
                .append(" --skip-extended-insert ")
                .append(" -u").append(dbConfig.getUser())
                .append(" -p").append(dbConfig.getPassword())
                .append(" -h").append(dbConfig.getHost())
                .append(" ").append(dbConfig.getDatabase())
                .append(" --default-character-set=utf8")
                .append(" > ")
                .append(" " + backupFilePath);
        return shell.toString();
    }

}
