package com.snet.tcms.main;

import com.snet.tcms.common.ComUtil;
import com.snet.tcms.common.EnvManager;
import com.snet.tcms.common.Environment;
import com.snet.tcms.converter.DawonAsisConverter;
import com.snet.tcms.dbconn.DbConnManager;
import com.snet.tcms.reader.SocketReader;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.HashMap;

public class SocketServer {
    private int idx = 0;

    private SocketServer(int port) {
        makeTroubleCodeFile();

        SocketReader sr;
        Thread t;

        try {
            ServerSocket server = new ServerSocket(port); //서버소켓 생성

            ComUtil.debug("***** 소켓 (" + server.getInetAddress() + ":" + server.getLocalPort() + ") 생성 완료. 클라이언트 접속 대기 중... *****");

            HashMap<String, DataOutputStream> hm = new HashMap<>();

            while (true) {
                Socket client = server.accept(); // 클라이언트에서 접속 요청이 옴

                String name = "socket" + ++idx;

                if (client != null) {
                    sr = new SocketReader(client, name);
                    t = new Thread(sr); // 새로운 스레드를 생성하여 데이터 수신 처리
                    t.start();
                }
            }
        } catch (Exception e) {
            ComUtil.error(ComUtil.printStackTraceToString(e));
        }
    }


    public static void main(String[] args) {
        Environment env = EnvManager.getEnvironment();
        int port;
        try {
            port = Integer.parseInt(env.getProperty("SOCKET_PORT"));
        } catch (Exception e) {
            ComUtil.debug("*** 포트 정보가 설정되지 않아, 기본 포트(6000)로 연결합니다...");
            port = 6000;
        }

        new SocketServer(port);
    }

    private void makeTroubleCodeFile() {
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            ComUtil.error(e.getMessage());
        }

        Connection connection = DbConnManager.getConnection();

        if (connection == null)
            makeTroubleCodeFile();

        ResultSet resultSet = null;
        PreparedStatement pstmt = null;

        StringBuilder query = new StringBuilder();
        StringBuilder result = new StringBuilder();

        try {
            query.append("SELECT USRDEF2");
            query.append("     , USRDEF1");
            query.append("  FROM ZTPM_DW.ZT_COMCODE");
            query.append(" WHERE 1 = 1");
            query.append("   AND CDTYPE = 'TC002'");
            query.append("   AND USEYN = 'Y'");
            query.append(" ORDER BY TO_INT(USRDEF2)");

            pstmt = connection.prepareStatement(query.toString());

            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                result.append(resultSet.getString("USRDEF2"));
                result.append("=");
                result.append(resultSet.getString("USRDEF1"));
                result.append("\n");
            }

        } catch (SQLException e) {
            ComUtil.error(e.getMessage());

            makeTroubleCodeFile(); // properties 파일이 없으면 에러코드 변환이 안되기 때문에 반드시 있어야 함..
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();

                if (pstmt != null)
                    pstmt.close();
            } catch (Exception e) {
                ComUtil.error(e.getMessage());
            }

        }

        if (result.length() > 0) {
            File file = new File(ComUtil.WorkingDirectory + File.separator + "config" + File.separator + "troublecode.properties");
            FileWriter fw = null;
            try {
                fw = new FileWriter(file);
                fw.write(result.toString());
                fw.flush();
                fw.close();

                ComUtil.debug("파일 생성완료....");
            } catch (IOException e) {
                ComUtil.error(e.getMessage());
                try {
                    if (fw != null)
                        fw.close();
                } catch (Exception e2) {
                    ComUtil.error(e2.getMessage());
                }

                makeTroubleCodeFile(); // properties 파일이 없으면 에러코드 변환이 안되기 때문에 반드시 있어야 함..
            } finally {
                if (fw != null) {
                    try {
                        fw.close();
                    } catch (IOException e) {
                        ComUtil.error(e.getMessage());
                    }
                }
            }
        }
    }

}