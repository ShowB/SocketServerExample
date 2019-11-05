package com.snet.tcms.dbconn;

import com.snet.tcms.common.ComUtil;
import com.snet.tcms.common.EnvManager;
import com.snet.tcms.common.Environment;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbConnManager {
    private static Connection conn;
    private static PreparedStatement psmt;

    public synchronized static Connection getConnection() {

        try {
            if (conn != null && !conn.isClosed())
                return conn;

            Environment env = EnvManager.getEnvironment();

            String connectionString = env.getProperty("TROUBLE_CODE_DB_URL");
            String user = env.getProperty("TROUBLE_CODE_DB_USER");
            String password = env.getProperty("TROUBLE_CODE_DB_PWD");

            conn = DriverManager.getConnection(connectionString, user, password);
        } catch (SQLException e) {
            ComUtil.error(e.getMessage());
            getConnection();
        }

        return conn;
    }

    public synchronized static PreparedStatement getTroubleInsertPsmt() {
        try {
            if (psmt != null && !psmt.isClosed())
                return psmt;

            StringBuilder sb = new StringBuilder();

            sb.append("INSERT INTO ZTPM_DW.TC_DAWONTRBL (");
            sb.append("       FORMNO");
            sb.append("     , DRVDT");
            sb.append("     , FORMDICT");
            sb.append("     , FILENM");
            sb.append("     , SEQNO");
            sb.append("     , DOWN");
            sb.append("     , ALARM");
            sb.append("     , TRBLEVTCD");
            sb.append("     , TRBLSTATCD");
            sb.append("     , TRBLWARNCD");
            sb.append("     , TRBLHEVYCD");
            sb.append("     , CARID");
            sb.append("     , TCMSTRBLCD");
            sb.append("     , OCCDT");
            sb.append("     , OCCTM");
            sb.append("     , EXTDT");
            sb.append("     , EXTTM");
            sb.append("     , ELAPTM");
            sb.append("     , STOCKNO");
            sb.append("     , LOADDT");

            sb.append(") VALUES (");
            sb.append("         ?");
            sb.append("       , ?");
            sb.append("       , ?");
            sb.append("       , ?");
            sb.append("       , ?");
            sb.append("       , ?");
            sb.append("       , ?");
            sb.append("       , ?");
            sb.append("       , ?");
            sb.append("       , ?");
            sb.append("       , ?");
            sb.append("       , ?");
            sb.append("       , ?");
            sb.append("       , ?");
            sb.append("       , ?");
            sb.append("       , ?");
            sb.append("       , ?");
            sb.append("       , ?");
            sb.append("       , ?");
            sb.append("       , ?");
            sb.append(")");

            psmt = getConnection().prepareStatement(sb.toString());
        } catch (Exception e) {
            ComUtil.error(e.getMessage());
            getTroubleInsertPsmt();
        }

        return psmt;
    }
}
