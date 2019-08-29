package dbhistory.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Properties;

/**
 * 执行命令工具
 */
public class ShellUtil {

    /**
     * 获取运行命令主机的操作系统
     */
    public static boolean systemWindows() {
        boolean windows = false;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            windows = true;
        } else if (os.toLowerCase().startsWith("linux") || os.toLowerCase().startsWith("mac")) {
            windows = windows;
        }
        return windows;
    }

    /**
     * @param mysqlDumpCommand mysqldump命令
     * @throws IOException
     * @throws InterruptedException
     */
    public static void exeDump(String mysqlDumpCommand) throws IOException, InterruptedException {
        try {
            if (systemWindows()) {
                Process process = Runtime.getRuntime().exec(new String[]{"cmd", "/c", mysqlDumpCommand});
                process.waitFor();
            } else {
                Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", mysqlDumpCommand});
                process.waitFor();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 数据备份文件导入
     *
     * @param shellList shell命令
     */
    public static void exeLoad(List<String> shellList) {
        try {
            Process process;
            if (systemWindows()) {
                process = Runtime.getRuntime().exec(new String[]{"cmd", "/c", shellList.get(0)});
            } else {
                process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", shellList.get(0)});
            }
            OutputStream os = process.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os);
            writer.write(shellList.get(1) + "\r\n" + shellList.get(2));
            writer.flush();
            writer.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
