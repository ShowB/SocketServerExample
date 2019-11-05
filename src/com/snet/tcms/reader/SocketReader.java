package com.snet.tcms.reader;

import com.snet.tcms.common.ComUtil;
import com.snet.tcms.common.EnvManager;
import com.snet.tcms.common.Environment;
import com.snet.tcms.converter.DawonAsisConverter;
import com.snet.tcms.dbconn.DbConnManager;
import com.snet.tcms.domain.DawonAsIsTrouble;
import com.snet.tcms.tcode.TroubleCodeProperty;
import com.snet.tcms.tcode.TroubleCodePropertyManager;
import com.snet.tcms.util.TcmsConstant;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SocketReader implements Runnable {
    private Socket child;
    private DataInputStream dis;
    private DataOutputStream dos;

    private final String name;
    private InetAddress ip;

    private TroubleCodeProperty troubleCodeProperty;

    public SocketReader(Socket s, String name) {
        troubleCodeProperty = TroubleCodePropertyManager.getTroubleCodeProperty(); // 고장코드 propertiy 가져오기

        child = s;
        this.name = name;

        try {
            dis = new DataInputStream(child.getInputStream());
            dos = new DataOutputStream(child.getOutputStream());

            ip = child.getInetAddress();

            ComUtil.debug("***** [" + ip + "] (" + name + ") 에서 연결 성공!! *****");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        Environment env = EnvManager.getEnvironment();

        int interval;

        try {
            interval = Integer.parseInt(env.getProperty("RUN_INTERVAL_TIME"));
        } catch (Exception e) {
            interval = 600;
        }

        Date start = new Date();
        long endTime = start.getTime() + (interval * 1000); // 운행파일 생성 주기
        Date end = new Date(endTime);

        StringBuilder runSb = new StringBuilder();
        StringBuilder troubleSb = new StringBuilder();

        int idx = 0;
        byte[] runBytes;
        byte[] troubleBytes;
        int readLength;

        Class clazz = null;
        Method convertRunHistory = null;
        Method convertTroubleHistory = null;
        Object instance = null;

        Connection conn = DbConnManager.getConnection();
        PreparedStatement psmt = null;


        int available;
        int cmCheck;

        int sbCursor;

        DawonAsIsTrouble trouble;

        try {
            do {
                available = dis.available();
                cmCheck = 0;

                if (clazz == null) {
                    if (available % TcmsConstant.DAWON_ASIS_PARSING_BYTE == 0) {
                        clazz = Class.forName(TcmsConstant.DAWON_ASIS_CONVERTER_CLASS_NAME);
                        cmCheck++;
                    }

                    if (available % TcmsConstant.DAWON_TOBE_PARSING_BYTE == 0) {
//                        clazz = Class.forName(TcmsConstant.DAWON_TOBE_CONVERTER_CLASS_NAME);
                        clazz = Class.forName(TcmsConstant.DAWON_ASIS_CONVERTER_CLASS_NAME);
                        cmCheck++;
                    }

                    if (available % TcmsConstant.ROTEM_TOBE_PARSING_BYTE == 0) {
//                        clazz = Class.forName(TcmsConstant.ROTEM_TOBE_CONVERTER_CLASS_NAME);
                        clazz = Class.forName(TcmsConstant.DAWON_ASIS_CONVERTER_CLASS_NAME);
                        cmCheck++;
                    }

                    if (clazz != null) {
                        instance = clazz.newInstance();
                        convertTroubleHistory = clazz.getMethod("convertTroubleHistory", byte[].class, int.class, StringBuilder.class, String.class, DawonAsIsTrouble.class);
                        convertRunHistory = clazz.getMethod("convertRunHistory", byte[].class, int.class, StringBuilder.class);
                    }
                }

                if (cmCheck > 1)
                    clazz = null;

                if (available > 0) {
                    if (clazz == null) {
                        dis.skipBytes(available);
                        ComUtil.debug("*** Byte 수로 데이터 포맷 식별 불가 !!! ... Skip >>> " + available);
                    } else {
                        if (TcmsConstant.DAWON_ASIS_CONVERTER_CLASS_NAME.equals(clazz.getName())) {
                            runBytes = new byte[504];
                            readLength = dis.read(runBytes, 0, 504);

                            troubleBytes = new byte[8];
                            readLength += dis.read(troubleBytes, 0, 8);

                            if (readLength == 512) {
                                System.out.println("다원 ASIS 통합기록 받았다!! ::: " + name + " >>> " + ++idx);

                                for (int i = 1; i <= 3040; i++) {
                                    troubleCodeProperty.getProperty(i);
                                }

                                trouble = new DawonAsIsTrouble();
                                convertTroubleHistory.invoke(instance, troubleBytes, idx, troubleSb, name, trouble);
                                convertRunHistory.invoke(instance, runBytes, idx, runSb);

                                String sb = "INSERT INTO ZTPM_DW.TC_DAWONTRBL (" +
                                        "       FORMNO" +
                                        "     , DRVDT" +
                                        "     , FORMDICT" +
                                        "     , FILENM" +
                                        "     , SEQNO" +
                                        "     , DOWN" +
                                        "     , ALARM" +
                                        "     , TRBLEVTCD" +
                                        "     , TRBLSTATCD" +
                                        "     , TRBLWARNCD" +
                                        "     , TRBLHEVYCD" +
                                        "     , CARID" +
                                        "     , TCMSTRBLCD" +
                                        "     , OCCDT" +
                                        "     , OCCTM" +
                                        "     , EXTDT" +
                                        "     , EXTTM" +
                                        "     , ELAPTM" +
                                        "     , STOCKNO" +
                                        "     , LOADDT" +
                                        ") VALUES (" +
                                        "         ?" +
                                        "       , ?" +
                                        "       , ?" +
                                        "       , ?" +
                                        "       , ?" +
                                        "       , ?" +
                                        "       , ?" +
                                        "       , ?" +
                                        "       , ?" +
                                        "       , ?" +
                                        "       , ?" +
                                        "       , ?" +
                                        "       , ?" +
                                        "       , ?" +
                                        "       , ?" +
                                        "       , ?" +
                                        "       , ?" +
                                        "       , ?" +
                                        "       , ?" +
                                        "       , ?" +
                                        ")";
                                psmt = conn.prepareStatement(sb);

//                                psmt = DbConnManager.getTroubleInsertPsmt();

                                sbCursor = 1;

                                psmt.setString(1, trouble.getFormNo());
                                psmt.setString(2, trouble.getDrvDt());
                                psmt.setString(3, trouble.getFormDict());
                                psmt.setString(4, trouble.getFileNm());
                                psmt.setInt   (5, trouble.getSeqNo());
                                psmt.setString(6, trouble.getDown());
                                psmt.setString(7, trouble.getAlarm());
                                psmt.setString(8, trouble.getTrblEvtCd());
                                psmt.setString(9, trouble.getTrblStatCd());
                                psmt.setString(10, trouble.getTrblWarnCd());
                                psmt.setString(11, trouble.getTrblHevyCd());
                                psmt.setString(12, trouble.getCarId());
                                psmt.setString(13, trouble.getTcmsTrblCd());
                                psmt.setString(14, trouble.getOccDt());
                                psmt.setString(15, trouble.getOccTm());
                                psmt.setString(16, trouble.getExtDt());
                                psmt.setString(17, trouble.getExtTm());
                                psmt.setInt   (18, trouble.getElapTm());
                                psmt.setString(19, trouble.getStockNo());
                                psmt.setString(20, trouble.getLoadDt());

                                System.out.println(psmt.executeUpdate());

                                psmt.close();

                            }
                        }
                    }
                }

                if (new Date().after(end)) { // 설정한 시간이 되면
                    // 파일로 생성하고 String Builder를 비움
                    if (runSb.length() > 0) {
                        String startStr = new SimpleDateFormat("YYMMdd_HHmmss").format(start);

                        File e = new File("D:/sample/run_" + name + "_" + startStr + ".conversion");
                        FileWriter fw = new FileWriter(e);
                        fw.write(runSb.toString());
                        fw.flush();

                        fw.close();
                        ComUtil.debug("*** " + e.getPath() + " 파일 생성 완료!!!");
                    }

                    if (troubleSb.length() > 0) {
                        String startStr = new SimpleDateFormat("YYMMdd_HHmmss").format(start);

                        File e = new File("D:/sample/trouble_" + name + "_" + startStr + ".conversion");
                        FileWriter fw = new FileWriter(e);
                        fw.write(troubleSb.toString());
                        fw.flush();

                        fw.close();
                        ComUtil.debug("*** " + e.getPath() + " 파일 생성 완료!!!");
                    }

                    runSb.setLength(0); // String Builder 초기화
                    troubleSb.setLength(0);

                    start = new Date();
                    endTime = start.getTime() + (interval * 1000);
                    end = new Date(endTime);
                }

                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    ComUtil.error(ComUtil.printStackTraceToString(e));
                }

            } while (child.isConnected());

        } catch (Exception e) {
            ComUtil.error(ComUtil.printStackTraceToString(e));
        } finally {
            ComUtil.debug("***** [" + ip + "] (" + name + ") 와의 연결 종료!!!");

            try {
                if (child != null)
                    child.close();
                if (dis != null)
                    dis.close();
                if (dos != null)
                    dos.close();

            } catch (Exception ex) {
                ComUtil.error(ComUtil.printStackTraceToString(ex));
            }
        }
    }

}