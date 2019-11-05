package com.snet.tcms.common;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ComUtil {
    public static String WorkingDirectory = System.getProperty("user.dir");
    public final static String FILE_SEPARATOR = System.getProperty("file.separator");


    public static void debug(String szMsg) {
        System.out.println(szMsg);

        PrintWriter pw = null;
        try {
            String szData = "";
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();

            String szDbgFile = "";
            Environment env = EnvManager.getEnvironment();
            szDbgFile = WorkingDirectory + FILE_SEPARATOR + env.getProperty("LOG_DIR") + FILE_SEPARATOR + "DEBUG_" + "LOG_" + sdf1.format(date) + ".log";
            //System.out.println("szDbgFile :: " + szDbgFile);

            FileOutputStream dbgStream = new FileOutputStream(szDbgFile, true);
            pw = new PrintWriter(dbgStream);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
            szData += "[" + sdf.format(new Date()) + "]";
            szData += szMsg;
            pw.println(szData);


        } catch (IOException ioe) {
            ioe.printStackTrace();

        } finally {
            if (pw != null)
                pw.close();
        }
    }

    public static void error(String szMsg) {
        System.out.println(szMsg);

        PrintWriter pw = null;
        try {
            String szData = "";
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();

            String szDbgFile = "";
            Environment env = EnvManager.getEnvironment();
            szDbgFile = WorkingDirectory + FILE_SEPARATOR + env.getProperty("LOG_DIR") + FILE_SEPARATOR + "ERROR_" + "LOG_" + sdf1.format(date) + ".log";
            //System.out.println("szDbgFile :: " + szDbgFile);

            FileOutputStream dbgStream = new FileOutputStream(szDbgFile, true);
            pw = new PrintWriter(dbgStream);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
            szData += "[" + sdf.format(new Date()) + "]";
            szData += szMsg;
            pw.println(szData);


        } catch (IOException ioe) {
            ioe.printStackTrace();

        } finally {
            if (pw != null)
                pw.close();
        }
    }

    public static String printStackTraceToString(Exception e) {
        ByteArrayOutputStream ex_out = new ByteArrayOutputStream();
        PrintStream ex_pinrtStream = new PrintStream(ex_out);
        e.printStackTrace(ex_pinrtStream);
        String stackTraceString = ex_out.toString(); // 찍은 값을 가져오고.

        try {
            ex_out.close();
        } catch (Exception ex) {
        }
        ex_pinrtStream.close();

        return stackTraceString;
    }
}
