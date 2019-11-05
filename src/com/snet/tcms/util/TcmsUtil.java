package com.snet.tcms.util;

import com.snet.tcms.common.ComUtil;
import com.snet.tcms.common.EnvManager;
import com.snet.tcms.common.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TcmsUtil {

    /**
     * 컨버전 대상 파일을 찾음
     *
     * @param source source
     * @param filePrefix filePrefix
     * @return List<File>
     * @throws Exception Exception
     */
    public static List<File> getTargetFiles(String source, String filePrefix) throws Exception {
        return getTargetFilesHierarchy(source, filePrefix, new ArrayList<File>());
    }

    /**
     * 하위 디렉토리를 모두 돌면서 컨버전 대상 파일을 찾음
     *
     * @param source source
     * @param filePrefix filePrefix
     * @param files files
     * @return List<File>
     * @throws Exception Exception
     */
    private static List<File> getTargetFilesHierarchy(String source, String filePrefix, List<File> files) throws Exception {
        File rootFoldor = new File(source);

        if (!rootFoldor.exists()) return files;

        File[] fileList = rootFoldor.listFiles();

        if (fileList == null) return files;

        for (File e : fileList) {
            if (e.isFile()) {
                if (e.getName().startsWith(filePrefix)) files.add(e);
            } else {
                // Directory일 경우, 자기 자신 함수 재귀 호출
                getTargetFilesHierarchy(e.getCanonicalPath(), filePrefix, files);
            }
        }

        return files;
    }

    /**
     * 디렉토리 생성
     *
     * @param source source
     */
    public static void makeDirectory(String source) {
        new File(source).mkdirs();
    }

    /**
     * 파일명 초기화
     * complete_, error_ 등을 제거함
     *
     * @param type type
     */
    public static void initFileName(String type) {
        Environment env = EnvManager.getEnvironment();

        ComUtil.debug("::::::::::: 파일명 초기화 시작 :::::::::::");

        if (TcmsConstant.DAWON_HISTORY_TYPE_RUN.equals(type))
            initFileNameHierarchy(env.getProperty("DAWON_RUN_SOURCE_FOLDER"), TcmsConstant.DAWON_RUN_FILE_PREFIX);
        else
            initFileNameHierarchy(env.getProperty("DAWON_TROUBLE_SOURCE_FOLDER"), TcmsConstant.DAWON_TROUBLE_FILE_PREFIX);

        ComUtil.debug("::::::::::: 파일명 초기화 완료 :::::::::::");
    }

    /**
     * 하위 디렉토리를 모두 돌면서 파일명 초기화
     *
     * @param sourceFolder sourceFolder
     * @param filePrefix filePrefix
     */
    private static void initFileNameHierarchy(String sourceFolder, String filePrefix) {
        ComUtil.debug("Initialize files in :: " + sourceFolder);

        try {
            File rootFoldor = new File(sourceFolder);
            if (rootFoldor.exists() && rootFoldor.isDirectory()) {
                File[] fileList = rootFoldor.listFiles();

                if (fileList == null) return;

                for (File e : fileList) {
                    if (e.isFile()) {
                        if (e.getName().contains(TcmsConstant.COMPLETE_FILE_PREFIX + filePrefix)) {
                            // 컨버전 완료 후 파일명에 complete_ 붙은 것을 다시 제거
                            e.renameTo(new File(e.getParent()
                                    + File.separator
                                    + e.getName().replaceAll(TcmsConstant.COMPLETE_FILE_PREFIX, "")));
                        }

                        if (e.getName().contains(TcmsConstant.ERROR_FILE_PREFIX + filePrefix)) {
                            // 컨버전 에러 발생 후 파일명에 error_ 붙은 것을 다시 제거
                            e.renameTo(new File(e.getParent()
                                    + File.separator
                                    + e.getName().replaceAll(TcmsConstant.ERROR_FILE_PREFIX, "")));
                        }
                    } else {
                        // directory일 경우 해당 경로로 다시 함수 재귀 호출
                        initFileNameHierarchy(e.getCanonicalPath(), filePrefix);
                    }
                }
            } else {
                ComUtil.debug("해당 경로는 폴더가 아닙니다.");
            }
        } catch (Exception e) {
            ComUtil.error(e.getMessage());
        }

    }

    /**
     * 2진수값을 10진수로 변경
     *
     * @param rbff rbff
     * @param row row
     * @param col col
     * @return int
     */
    public static int oneBinaryToDecimal(byte[] rbff, int row, int col) {
        String binary = "";
        binary = byteArrayToBin(rbff[row], col) + binary;

        return Integer.parseInt(binary, 2); // 10진수값 변경
    }

    /**
     * 특정영역의 2진수값을 10진수로 변경
     *
     * @param rbff rbff
     * @param row row
     * @param startCol startCol
     * @param endCol endCol
     * @return int
     */
    public static int binaryToDecimal(byte[] rbff, int row, int startCol, int endCol) {
        StringBuilder binary = new StringBuilder();
        for (int i = startCol; i <= endCol; i++) {
            binary.insert(0, byteArrayToBin(rbff[row], i));
        }
        return Integer.parseInt(binary.toString(), 2); // 10진수값 변경
    }

    /**
     * 멀티Row의 2진수값을 10진수로 변경
     *
     * @param rbff rbff
     * @param startRow startRow
     * @param endRow endRow
     * @return int
     */
    public static int multiByteToDecimal(byte[] rbff, int startRow, int endRow) {
        StringBuilder binary = new StringBuilder();
        for (int i = startRow; i <= endRow; i++) {
            for (int j = 0; j <= 7; j++) {
                binary.insert(0, byteArrayToBin(rbff[i], j));
            }
        }
//        int decimal = Integer.parseInt(binary, 2); // 10진수값 변경
        // COMUtil.debug("multiByteToDecimal binary ::::::::::" + binary);
        // COMUtil.debug("multiByteToDecimal decimal ::::::::::" + decimal);
        return Integer.parseInt(binary.toString(), 2); // 10진수값 변경
    }

    /**
     * 멀티Row의 2진수값을 10진수로 변경
     *
     * @param rbff rbff
     * @param startRow startRow
     * @param endRow endRow
     * @return Long
     */
    public static Long multiByteToDecimalLong(byte[] rbff, int startRow, int endRow) {
        StringBuilder binary = new StringBuilder();
        for (int i = startRow; i <= endRow; i++) {
            for (int j = 0; j <= 7; j++) {
                binary.insert(0, byteArrayToBin(rbff[i], j));
            }
        }
//        Long decimal = Long.parseLong(binary, 2); // 10진수값 변경

        /*
         * int decimal = 0; for (int i = 0; i < binary.length(); i++){ decimal
         * += Math.pow(2, ((binary.length() - 1) - i)) *
         * Integer.parseInt(Character.toString(binary.charAt(i))); }
         */
        // COMUtil.debug("multiByteToDecimal decimal ::::::::::" + decimal);
        return Long.parseLong(binary.toString(), 2); // 10진수값 변경
    }

    /**
     * 특정영역의 2진수값을 Hexadecimal로 변경
     *
     * @param rbff rbff
     * @param row row
     * @param startCol startCol
     * @param endCol endCol
     * @return String
     */
    public static String binaryToHex(byte[] rbff, int row, int startCol, int endCol) {
        StringBuilder binary = new StringBuilder();
        for (int i = startCol; i <= endCol; i++) {
            binary.insert(0, byteArrayToBin(rbff[row], i));
        }
//        String hexa = Long.toHexString(Long.parseLong(binary, 2)); // HEX 변경
        // COMUtil.debug("binaryToDecimal binary :::::::: " + binary);
        // COMUtil.debug("binaryToDecimal hexa :::::::: " + hexa);
        return Long.toHexString(Long.parseLong(binary.toString(), 2)); // HEX 변경
    }

    /**
     * 멀티Row의 2진수값을 Hexadecimal로 변경
     *
     * @param rbff rbff
     * @param startRow startRow
     * @param endRow endRow
     * @return String
     */
    public static String multiByteToHex(byte[] rbff, int startRow, int endRow) {
        StringBuilder binary = new StringBuilder();
        for (int i = startRow; i <= endRow; i++) {
            for (int j = 0; j <= 7; j++) {
                binary.insert(0, byteArrayToBin(rbff[i], j));
            }
        }
//        String hexa = Long.toHexString(Long.parseLong(binary, 2)); // HEX 변경
        // COMUtil.debug("multiByteToDecimal binary ::::::::::" + binary);
        // COMUtil.debug("multiByteToDecimal hexa ::::::::::" + hexa);
        return Long.toHexString(Long.parseLong(binary.toString(), 2)); // HEX 변경
    }

    /**
     * 2진수값을 sign decimal로 변경
     *
     * @param binary binary
     * @return long
     */
    public static long signedBinaryToDecimal(String binary) {

        long[] obj = new long[64];// 8byte
        int objI = 0;
        for (int z = 63; z >= 0; z--) {
            obj[objI++] = (long) Math.pow(2, 63 - z);
        }
        long decimal = 0;
        int[] arryBinary = new int[binary.length()];
        for (int i = 0; i < binary.length(); i++) {
            arryBinary[i] = Integer.parseInt(binary.charAt(i) + "");
        }

        int k;

        for (int i = 1; i < binary.length(); i++) {
            k = (int) Math.pow(2, (binary.length() - 1) - i);

            decimal = decimal + (k * arryBinary[i]);
        }

        if (arryBinary[0] == 1) {
            decimal = obj[binary.length() - 1] - decimal;
            decimal = decimal * (-1);
        }

        return decimal;
    }

    /**
     *
     * @param data data
     * @param idx idx
     * @return int
     */
    public static int byteArrayToBin(byte data, int idx) {
        Integer[] obj = {1, 2, 4, 8, 16, 32, 64, 128};
        int result = obj[idx] & data;
        return result == 0 ? 0 : 1;
    }

}