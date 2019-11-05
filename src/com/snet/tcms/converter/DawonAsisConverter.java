package com.snet.tcms.converter;

import com.snet.tcms.common.ComUtil;
import com.snet.tcms.domain.DawonAsIsTrouble;
import com.snet.tcms.util.TcmsConstant;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.snet.tcms.util.TcmsUtil.*;
import static com.snet.tcms.util.TcmsUtil.byteArrayToBin;

public class DawonAsisConverter {

    public void test() {
        System.out.println("TEST!!!!!!!!!!!");
    }

    public void convertTroubleHistory(byte[] bytes, int idx, StringBuilder sb, String name, DawonAsIsTrouble trouble) {
        StringBuilder row = new StringBuilder();

        int checkRow = -1;
        try {
            //1.운행일자
//            String runDate = new File(folderPath).getName();
//
//            runDate = "20" + runDate.substring(1);
//            String runYear = runDate.substring(0, 4);

//            String formNoFull = new File(new File(new File(folderPath).getParent()).getParent()).getName();
            //2. 편성번호
//            int formNo = Integer.parseInt(formNoFull.substring(0, 1) + formNoFull.substring(2, 4));//폴더 구조가 맞지 않을경우 임의적으로 에러를 발생하기 위함.
            //3. 진행방향
//            String fromDict = formNoFull.substring(1, 2);

            row.append("206").append(TcmsConstant.COLUMN_SEPARATOR);   // 편성번호
            trouble.setFormNo("206");

            row.append("20191030").append(TcmsConstant.COLUMN_SEPARATOR);  // 운행일자
            trouble.setDrvDt("20191030");

            row.append("0").append(TcmsConstant.COLUMN_SEPARATOR); // 진행방향
            trouble.setFormDict("0");

            row.append(name).append(TcmsConstant.COLUMN_SEPARATOR); // 원파일명
            trouble.setFileNm(name);

            row.append(idx).append(TcmsConstant.COLUMN_SEPARATOR);      // 자체 시퀀스
            trouble.setSeqNo(idx);


//            row.append(formNo).append(DawonConstant.COLUMN_SEPARATOR); //편성번호
//            row.append(runDate).append(DawonConstant.COLUMN_SEPARATOR); //운행일자
//            row.append(fromDict).append(DawonConstant.COLUMN_SEPARATOR); //진행방향
//            row.append(fileName).append(DawonConstant.COLUMN_SEPARATOR); //원파일명
//            row.append(idx).append(DawonConstant.COLUMN_SEPARATOR); //seq

            if (idx == checkRow) {
                for (int i = 0; i <= 7; i++) {
                    StringBuilder binary = new StringBuilder();
                    for (int j = 0; j <= 7; j++) {
                        binary.insert(0, byteArrayToBin(bytes[i], j));
                    }
                    ComUtil.debug(i + " :: " + binary);
                }
                ComUtil.debug("=======================================");
            }

            row.append(oneBinaryToDecimal(bytes, 0, 0)).append(TcmsConstant.COLUMN_SEPARATOR); //down
            trouble.setDown(String.valueOf(oneBinaryToDecimal(bytes, 0, 0)));

            row.append(oneBinaryToDecimal(bytes, 0, 1)).append(TcmsConstant.COLUMN_SEPARATOR); //alarm
            trouble.setAlarm(String.valueOf(oneBinaryToDecimal(bytes, 0, 1)));

            //this.sbuf.append(binaryToDecimal(rbff, 0, 2, 5) + DawonConstant.COLUMN_SEPARATOR);//type
            row.append(oneBinaryToDecimal(bytes, 0, 2)).append(TcmsConstant.COLUMN_SEPARATOR); //type (이벤트)
            trouble.setTrblEvtCd(String.valueOf(oneBinaryToDecimal(bytes, 0, 2)));

            row.append(oneBinaryToDecimal(bytes, 0, 3)).append(TcmsConstant.COLUMN_SEPARATOR); //type (상태)
            trouble.setTrblStatCd(String.valueOf(oneBinaryToDecimal(bytes, 0, 3)));

            row.append(oneBinaryToDecimal(bytes, 0, 4)).append(TcmsConstant.COLUMN_SEPARATOR); //type (경고장)
            trouble.setTrblWarnCd(String.valueOf(oneBinaryToDecimal(bytes, 0, 4)));

            row.append(oneBinaryToDecimal(bytes, 0, 5)).append(TcmsConstant.COLUMN_SEPARATOR); //type (중고장)
            trouble.setTrblHevyCd(String.valueOf(oneBinaryToDecimal(bytes, 0, 5)));


            StringBuilder cidBinary = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                if (i < 2) {
                    cidBinary.insert(0, byteArrayToBin(bytes[0], i + 6));
                } else {
                    cidBinary.insert(0, byteArrayToBin(bytes[1], i - 2));
                }
            }
            row.append(Integer.parseInt(cidBinary.toString(), 2)).append(TcmsConstant.COLUMN_SEPARATOR); //cid
            trouble.setCarId(String.valueOf(Integer.parseInt(cidBinary.toString(), 2)));

            //code
            StringBuilder codeBinary = new StringBuilder();
            for (int i = 0; i < 11; i++) {
                if (i < 6) {
                    codeBinary.insert(0, byteArrayToBin(bytes[1], i + 2));
                } else {
                    codeBinary.insert(0, byteArrayToBin(bytes[2], i - 6));
                }
            }
            if (idx == checkRow) {
                ComUtil.debug("codeBinary ::::::::: " + codeBinary);
                ComUtil.debug("code ::::::::: " + Integer.parseInt(codeBinary.toString(), 2));
            }
            int code = Integer.parseInt(codeBinary.toString(), 2);
            row.append(code).append(TcmsConstant.COLUMN_SEPARATOR); //code
            trouble.setTcmsTrblCd(String.valueOf(code));


//            int year = Integer.parseInt(runYear);
            StringBuilder monBinary = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                if (i < 3) {
                    monBinary.insert(0, byteArrayToBin(bytes[2], i + 5));
                } else {
                    monBinary.insert(0, byteArrayToBin(bytes[3], i - 3));
                }
            }
            //COMUtil.debug("monBinary :: " + monBinary);
            int mon = Integer.parseInt(monBinary.toString(), 2);
            //this.sbuf.append(Integer.parseInt(monBinary, 2) + separator);//mon
            int day = binaryToDecimal(bytes, 3, 1, 5);
            //this.sbuf.append(binaryToDecimal(rbff, 3, 1, 5) + separator);//day


            StringBuilder uHourBinary = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                if (i < 2) {
                    uHourBinary.insert(0, byteArrayToBin(bytes[3], i + 6));
                } else {
                    uHourBinary.insert(0, byteArrayToBin(bytes[4], i - 2));
                }
            }
            int uhour = Integer.parseInt(uHourBinary.toString(), 2);
            //this.sbuf.append(Integer.parseInt(uHourBinary, 2) + separator);//uhour
            if (idx == checkRow) {
                ComUtil.debug("uHourBinary ::::::::: " + uHourBinary);
                ComUtil.debug("uhour ::::::::: " + Integer.parseInt(uHourBinary.toString(), 2));
            }

            //년도가 변경되었을때 (ex) 2018년 12월 31일에 파일이 생성되었는데 발생일시는 2019년 01월 01일 0시에 발생)
//            String folderDate = runDate.substring(4);
//            if (folderDate.equals("1231")) {
//                if (mon == 1 && day == 1) {
//                    year = year + 1;
//                }
//            }
//            if (folderDate.equals("0101")) {
//                if (mon == 12 && day == 31) {
//                    year = year - 1;
//                }
//            }
            //년도가 변경되었을때 (ex) 2018년 12월 31일에 파일이 생성되었는데 발생일시는 2019년 01월 01일 0시에 발생)

            StringBuilder uMinBinary = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                if (i < 5) {
                    uMinBinary.insert(0, byteArrayToBin(bytes[4], i + 3));
                } else {
                    uMinBinary.insert(0, byteArrayToBin(bytes[5], i - 5));
                }
            }
            int umin = Integer.parseInt(uMinBinary.toString(), 2);
            //this.sbuf.append(Integer.parseInt(uMinBinary, 2) + separator);//umin
            if (idx == checkRow) {
                ComUtil.debug("uMinBinary ::::::::: " + uMinBinary);
                ComUtil.debug("umin ::::::::: " + Integer.parseInt(uMinBinary.toString(), 2));
            }

            int usec = binaryToDecimal(bytes, 5, 1, 6);
            //this.sbuf.append(binaryToDecimal(rbff, 5, 1, 6) + separator);//usec


            int year = Integer.parseInt(new SimpleDateFormat("YYYY").format(new Date()));
            String uDate = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, mon, day, uhour, umin, usec);
            //this.sbuf.append(uDate + separator);
            try {
                row.append(uDate, 0, 10).append(TcmsConstant.COLUMN_SEPARATOR);
                trouble.setOccDt(uDate.substring(0, 10));

                row.append(uDate.substring(uDate.length() - 8)).append(TcmsConstant.COLUMN_SEPARATOR);
                trouble.setOccTm(uDate.substring(uDate.length() - 8));
            } catch (Exception ex) {
                row.append(TcmsConstant.COLUMN_SEPARATOR);
                row.append(TcmsConstant.COLUMN_SEPARATOR);

                trouble.setOccDt(null);
                trouble.setOccTm(null);
            }

            StringBuilder dHourBinary = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                if (i < 1) {
                    dHourBinary.insert(0, byteArrayToBin(bytes[5], i + 7));
                } else {
                    dHourBinary.insert(0, byteArrayToBin(bytes[6], i - 1));
                }
            }
            int dhour = Integer.parseInt(dHourBinary.toString(), 2);
            //this.sbuf.append(Integer.parseInt(dHourBinary, 2) + separator);//dhour
            if (idx == checkRow) {
                ComUtil.debug("dHourBinary ::::::::: " + dHourBinary);
                ComUtil.debug("dhour ::::::::: " + Integer.parseInt(dHourBinary.toString(), 2));
            }

            StringBuilder dMinBinary = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                if (i < 4) {
                    dMinBinary.insert(0, byteArrayToBin(bytes[6], i + 4));
                } else {
                    dMinBinary.insert(0, byteArrayToBin(bytes[7], i - 4));
                }
            }
            int dmin = Integer.parseInt(dMinBinary.toString(), 2);
            //this.sbuf.append(Integer.parseInt(dMinBinary, 2) + separator);//dmin
            if (idx == checkRow) {
                ComUtil.debug("dMinBinary ::::::::: " + dMinBinary);
                ComUtil.debug("dmin ::::::::: " + Integer.parseInt(dMinBinary.toString(), 2));
            }

            int dsec = binaryToDecimal(bytes, 7, 2, 7);
            //this.sbuf.append(binaryToDecimal(rbff, 7, 2, 7) + separator);//dsec

            String dDate = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, mon, day, dhour, dmin, dsec);
            if (dDate.contains("00:00:00")) {
                row.append(TcmsConstant.COLUMN_SEPARATOR);
                trouble.setExtDt(null);
                trouble.setExtTm(null);
            } else {
                //this.sbuf.append(dDate);
                try {
                    row.append(dDate, 0, 10).append(TcmsConstant.COLUMN_SEPARATOR);
                    trouble.setExtDt(dDate.substring(0, 10));
                    row.append(dDate.substring(dDate.length() - 8));
                    trouble.setExtTm(dDate.substring(dDate.length() - 8));
                } catch (Exception ex) {
                    row.append(TcmsConstant.COLUMN_SEPARATOR);
                    trouble.setExtDt(null);
                    trouble.setExtTm(null);
                }
            }

            trouble.setElapTm(0);
            trouble.setStockNo(trouble.getFormNo().substring(0, 1) + trouble.getCarId() + trouble.getFormNo().substring(1));
            trouble.setLoadDt(new SimpleDateFormat("YYYYMMdd HH:mm:ss").format(new Date()));

            //COMUtil.debug(this.sbuf.toString());
            row.append(TcmsConstant.ENTER_VALUE);
            //if(code >=300 && code <=346){
            sb.append(row.toString());
            //}
        } catch (Exception e) {
            ComUtil.error(ComUtil.printStackTraceToString(e));
        }
    }

    public void convertRunHistory(byte[] bytes, int idx, StringBuilder sb) {
        int debugRow = -1;
        int sequence = 0;

        StringBuffer row = new StringBuffer();
        try {
            // 1.운행일자

//            String formNoFull = new File(new File(new File(folderPath).getParent()).getParent()).getName();
            // 2. 편성번호
            // 폴더 구조가 맞지 않을경우 임의적으로 에러를 발생하기 위함.
//            int formNo = Integer.parseInt(formNoFull.substring(0, 1) + formNoFull.substring(2, 4));

            // 3. 진행방향 (10량 차량의 경우: 0=정방향, 9=역방향)
//            String fromDict = formNoFull.substring(1, 2);

            row.append("2006").append(TcmsConstant.COLUMN_SEPARATOR);   // 편성번호
            row.append("20191030").append(TcmsConstant.COLUMN_SEPARATOR);  // 운행일자
            row.append("0").append(TcmsConstant.COLUMN_SEPARATOR); // 진행방향
            row.append("socket").append(TcmsConstant.COLUMN_SEPARATOR); // 원파일명
            row.append(idx).append(TcmsConstant.COLUMN_SEPARATOR);      // 자체 시퀀스

            int currentColumnSize = ((row.toString()).split("[" + TcmsConstant.COLUMN_SEPARATOR + "]")).length;
            if (idx == debugRow)
                ComUtil.debug("PK AFTER :::" + currentColumnSize);

            // 시퀀스
            sequence = multiByteToDecimal(bytes, 0, 1);
//            if (idx == 1) startSequence = sequence;
//            else endSequence = sequence;

            row.append(sequence).append(TcmsConstant.COLUMN_SEPARATOR);
            // 날짜
            // row.append(makeDate(rbff, idx)).append(DawonConstant.COLUMN_SEPARATOR);
            String date = makeDate(bytes);
            try {
                row.append(date, 0, 10).append(TcmsConstant.COLUMN_SEPARATOR);
                row.append(date, date.length() - 8, date.length()).append(TcmsConstant.COLUMN_SEPARATOR);
            } catch (Exception ex) {
                row.append(TcmsConstant.COLUMN_SEPARATOR);
                row.append(TcmsConstant.COLUMN_SEPARATOR);
            }

            // P/B PWM
            row.append(binaryToDecimal(bytes, 30, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);

            if (idx == debugRow)
                ComUtil.debug("P/B PWM AFTER :::" + currentColumnSize);


            // 0호차
            ZeroTrain(bytes, row);
            // 09호차
            NineTrain(bytes, row);
            // TC DO
            for (int i = 102; i <= 105; i++) {
                for (int j = 0; j <= 7; j++) {
                    if (j != 3 && j != 7)
                        row.append(oneBinaryToDecimal(bytes, i, j)).append(TcmsConstant.COLUMN_SEPARATOR);

                }
            }
            if (idx == debugRow)
                ComUtil.debug("TC DO AFTER :::" + currentColumnSize);


            // TC AI
            row.append(multiByteToDecimalLong(bytes, 114, 115)).append(TcmsConstant.COLUMN_SEPARATOR);
            row.append(multiByteToDecimalLong(bytes, 116, 117)).append(TcmsConstant.COLUMN_SEPARATOR);
            row.append(multiByteToDecimalLong(bytes, 126, 127)).append(TcmsConstant.COLUMN_SEPARATOR);
            row.append(multiByteToDecimalLong(bytes, 128, 129)).append(TcmsConstant.COLUMN_SEPARATOR);
            row.append(multiByteToDecimalLong(bytes, 138, 139)).append(TcmsConstant.COLUMN_SEPARATOR);
            row.append(multiByteToDecimalLong(bytes, 140, 141)).append(TcmsConstant.COLUMN_SEPARATOR);
            row.append(multiByteToDecimalLong(bytes, 150, 151)).append(TcmsConstant.COLUMN_SEPARATOR);
            row.append(multiByteToDecimalLong(bytes, 152, 153)).append(TcmsConstant.COLUMN_SEPARATOR);

            if (idx == debugRow)
                ComUtil.debug("TC AI AFTER :::" + currentColumnSize);

            // CC DI
            ccDi(bytes, row);

            if (idx == debugRow)
                ComUtil.debug("CC DI AFTER :::" + currentColumnSize);

            // CC DO
            for (int i = 218; i <= 225; i++) {
                for (int j = 0; j <= 4; j++) {
                    row.append(oneBinaryToDecimal(bytes, i, j)).append(TcmsConstant.COLUMN_SEPARATOR);
                }
            }

            if (idx == debugRow)
                ComUtil.debug("CC DO AFTER :::" + currentColumnSize);


            // ATO
            Ato(bytes, idx, debugRow, row);

            if (idx == debugRow)
                ComUtil.debug("ATO AFTER :::" + currentColumnSize);


            // TCMS
            // row.append(multiByteToDecimalLong(rbff, 244, 245) +
            // separator);//TRAIN No.
            String trainNo = binaryToHex(bytes, 244, 4, 7)
                    + binaryToHex(bytes, 244, 0, 3)
                    + binaryToHex(bytes, 245, 4, 7)
                    + binaryToHex(bytes, 245, 0, 3);
            row.append(trainNo).append(TcmsConstant.COLUMN_SEPARATOR); // TRAIN No.
            if (idx == debugRow)
                ComUtil.debug(" 			244 trainNo :: " + trainNo);


            row.append(multiByteToDecimalLong(bytes, 246, 247)).append(TcmsConstant.COLUMN_SEPARATOR);
            row.append(multiByteToDecimalLong(bytes, 252, 254)).append(TcmsConstant.COLUMN_SEPARATOR);

            try {
                row.append(multiByteToDecimalLong(bytes, 256, 263)).append(TcmsConstant.COLUMN_SEPARATOR);
            } catch (Exception e) {
                // COMUtil.debug(e.getMessage() + "8byte 값이 너무커서 -1로 처리" +
                // folderPath + File.separator + fileName);
                row.append("-1").append(TcmsConstant.COLUMN_SEPARATOR);
            }

            row.append(multiByteToDecimalLong(bytes, 264, 265)).append(TcmsConstant.COLUMN_SEPARATOR);

            // SIV
            Siv(bytes, row);

            if (idx == debugRow)
                ComUtil.debug("SIV AFTER :::" + currentColumnSize);


            // ECU
            Ecu(bytes, row);

            if (idx == debugRow)
                ComUtil.debug("ECU AFTER :::" + currentColumnSize);


            // VVVF
            for (int i = 371; i <= 375; i++) {
                for (int j = 0; j <= 7; j++) {
                    row.append(oneBinaryToDecimal(bytes, i, j)).append(TcmsConstant.COLUMN_SEPARATOR);
                }
            }

            if (idx == debugRow)
                ComUtil.debug("VVVF AFTER :::" + currentColumnSize);


            // SIV
            try {
                row.append(multiByteToDecimalLong(bytes, 378, 385)).append(TcmsConstant.COLUMN_SEPARATOR);
            } catch (Exception e) {
                // COMUtil.debug(e.getMessage() + "8byte 값이 너무커서 -1로 처리" +
                // folderPath + File.separator + fileName);
                row.append("-1").append(TcmsConstant.COLUMN_SEPARATOR);
            }

            try {
                row.append(multiByteToDecimalLong(bytes, 386, 393)).append(TcmsConstant.COLUMN_SEPARATOR);
            } catch (Exception e) {
                // COMUtil.debug(e.getMessage() + "8byte 값이 너무커서 -1로 처리" +
                // folderPath + File.separator + fileName);
                row.append("-1").append(TcmsConstant.COLUMN_SEPARATOR);
            }

            try {
                row.append(multiByteToDecimalLong(bytes, 394, 401)).append(TcmsConstant.COLUMN_SEPARATOR);
            } catch (Exception e) {
                // COMUtil.debug(e.getMessage() + "8byte 값이 너무커서 -1로 처리" +
                // folderPath + File.separator + fileName);
                row.append("-1").append(TcmsConstant.COLUMN_SEPARATOR);
            }

            // VVVF
            try {
                row.append(multiByteToDecimalLong(bytes, 402, 409)).append(TcmsConstant.COLUMN_SEPARATOR);
            } catch (Exception e) {
                // COMUtil.debug(e.getMessage() + "8byte 값이 너무커서 -1로 처리" +
                // folderPath + File.separator + fileName);
                row.append("-1").append(TcmsConstant.COLUMN_SEPARATOR);
            }

            try {
                row.append(multiByteToDecimalLong(bytes, 410, 417)).append(TcmsConstant.COLUMN_SEPARATOR);
            } catch (Exception e) {
                // COMUtil.debug(e.getMessage() + "8byte 값이 너무커서 -1로 처리" +
                // folderPath + File.separator + fileName);
                row.append("-1").append(TcmsConstant.COLUMN_SEPARATOR);
            }

            try {
                row.append(multiByteToDecimalLong(bytes, 418, 425)).append(TcmsConstant.COLUMN_SEPARATOR);
            } catch (Exception e) {
                // COMUtil.debug(e.getMessage() + "8byte 값이 너무커서 -1로 처리" +
                // folderPath + File.separator + fileName);
                row.append("-1").append(TcmsConstant.COLUMN_SEPARATOR);
            }

            try {
                row.append(multiByteToDecimalLong(bytes, 426, 433)).append(TcmsConstant.COLUMN_SEPARATOR);
            } catch (Exception e) {
                // COMUtil.debug(e.getMessage() + "8byte 값이 너무커서 -1로 처리" +
                // folderPath + File.separator + fileName);
                row.append("-1").append(TcmsConstant.COLUMN_SEPARATOR);
            }

            try {
                row.append(multiByteToDecimalLong(bytes, 434, 441)).append(TcmsConstant.COLUMN_SEPARATOR);
            } catch (Exception e) {
                // COMUtil.debug(e.getMessage() + "8byte 값이 너무커서 -1로 처리" +
                // folderPath + File.separator + fileName);
                row.append("-1").append(TcmsConstant.COLUMN_SEPARATOR);
            }

            try {
                row.append(multiByteToDecimalLong(bytes, 442, 449)).append(TcmsConstant.COLUMN_SEPARATOR);
            } catch (Exception e) {
                // COMUtil.debug(e.getMessage() + "8byte 값이 너무커서 -1로 처리" +
                // folderPath + File.separator + fileName);
                row.append("-1").append(TcmsConstant.COLUMN_SEPARATOR);
            }

            try {
                row.append(multiByteToDecimalLong(bytes, 450, 457)
                ).append(TcmsConstant.COLUMN_SEPARATOR);
            } catch (Exception e) {
                // COMUtil.debug(e.getMessage() + "8byte 값이 너무커서 -1로 처리" +
                // folderPath + File.separator + fileName);
                row.append("-1").append(TcmsConstant.COLUMN_SEPARATOR);
            }

            try {
                row.append(multiByteToDecimalLong(bytes, 458, 465)
                ).append(TcmsConstant.COLUMN_SEPARATOR);
            } catch (Exception e) {
                // COMUtil.debug(e.getMessage() + "8byte 값이 너무커서 -1로 처리" +
                // folderPath + File.separator + fileName);
                row.append("-1").append(TcmsConstant.COLUMN_SEPARATOR);
            }

            try {
                row.append(multiByteToDecimalLong(bytes, 466, 473)
                ).append(TcmsConstant.COLUMN_SEPARATOR);
            } catch (Exception e) {
                // COMUtil.debug(e.getMessage() + "8byte 값이 너무커서 -1로 처리" +
                // folderPath + File.separator + fileName);
                row.append("-1").append(TcmsConstant.COLUMN_SEPARATOR);
            }

            try {
                row.append(multiByteToDecimalLong(bytes, 474, 481));
            } catch (Exception e) {
                // COMUtil.debug(e.getMessage() + "8byte 값이 너무커서 -1로 처리" +
                // folderPath + File.separator + fileName);
                row.append("-1");
            }

            row.append(TcmsConstant.ENTER_VALUE);

            sb.append(row.toString());
        } catch (NumberFormatException ne) {
//            ComUtil.error("err :: " + folderPath + File.separator + fileName);
            ComUtil.error(ComUtil.printStackTraceToString(ne));
        } catch (Exception e) {
//            ComUtil.error("err :: " + folderPath + File.separator + fileName);
            ComUtil.error("[" + sequence + "]"
                    + ComUtil.printStackTraceToString(e));
        }

    }

    /**
     * siv 정보 생성
     * @param rbff rbff
     * @param row row
     */
    private void Siv(byte[] rbff, StringBuffer row) {
        row.append(binaryToDecimal(rbff, 266, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 267, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 268, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        // row.append(multiByteToDecimal(rbff, 269, 270) +
        // separator);//출력전류
        StringBuilder binary = new StringBuilder();
        StringBuilder binary2 = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            if (i < 4) {
                binary.insert(0, byteArrayToBin(rbff[270], i + 4));
            } else {
                binary2.insert(0, byteArrayToBin(rbff[269], i - 4));
            }
        }
        binary.append(binary2);
        // COMUtil.debug("270 :: ["+sequence+"]"+ Long.parseLong(binary, 2) );
        row.append(Long.parseLong(binary.toString(), 2)).append(TcmsConstant.COLUMN_SEPARATOR);// 출력전류

        row.append(binaryToDecimal(rbff, 271, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 272, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 273, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        // row.append(multiByteToDecimal(rbff, 274, 275) +
        // separator);//출력전류
        binary = new StringBuilder();
        binary2 = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            if (i < 4) {
                binary.insert(0, byteArrayToBin(rbff[275], i + 4));
            } else {
                binary2.insert(0, byteArrayToBin(rbff[274], i - 4));
            }
        }
        binary.append(binary2);
        // COMUtil.debug("275 :: ["+sequence+"]"+ Long.parseLong(binary, 2) );
        row.append(Long.parseLong(binary.toString(), 2)).append(TcmsConstant.COLUMN_SEPARATOR);// 출력전류

        row.append(binaryToDecimal(rbff, 276, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 277, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 278, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        // row.append(multiByteToDecimal(rbff, 279, 280) +
        // separator);//출력전류
        binary = new StringBuilder();
        binary2 = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            if (i < 4) {
                binary.insert(0, byteArrayToBin(rbff[280], i + 4));
            } else {
                binary2.insert(0, byteArrayToBin(rbff[279], i - 4));
            }
        }
        binary.append(binary2);
        // COMUtil.debug("280 :: ["+sequence+"]"+ Long.parseLong(binary, 2) );
        row.append(Long.parseLong(binary.toString(), 2)).append(TcmsConstant.COLUMN_SEPARATOR);// 출력전류

        row.append(binaryToDecimal(rbff, 281, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 282, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 283, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        // row.append(multiByteToDecimal(rbff, 284, 285) +
        // separator);//APBR
        binary = new StringBuilder();
        for (int i = 0; i <= 7; i++) {
            binary.insert(0, byteArrayToBin(rbff[284], i));
        }
        for (int i = 0; i <= 7; i++) {
            binary.insert(0, byteArrayToBin(rbff[285], i));
        }
        row.append(signedBinaryToDecimal(binary.toString())).append(TcmsConstant.COLUMN_SEPARATOR);// APBR
        row.append(multiByteToDecimal(rbff, 286, 287)).append(TcmsConstant.COLUMN_SEPARATOR);

        row.append(binaryToDecimal(rbff, 288, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 289, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 290, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        // row.append(multiByteToDecimal(rbff, 291, 292) +
        // separator);//APBR
        binary = new StringBuilder();
        for (int i = 0; i <= 7; i++) {
            binary.insert(0, byteArrayToBin(rbff[291], i));
        }
        for (int i = 0; i <= 7; i++) {
            binary.insert(0, byteArrayToBin(rbff[292], i));
        }
        row.append(signedBinaryToDecimal(binary.toString())).append(TcmsConstant.COLUMN_SEPARATOR);// APBR
        row.append(multiByteToDecimal(rbff, 293, 294)).append(TcmsConstant.COLUMN_SEPARATOR);

        row.append(binaryToDecimal(rbff, 295, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 296, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 297, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        // row.append(multiByteToDecimal(rbff, 298, 299) +
        // separator);//APBR
        binary = new StringBuilder();
        for (int i = 0; i <= 7; i++) {
            binary.insert(0, byteArrayToBin(rbff[298], i));
        }
        for (int i = 0; i <= 7; i++) {
            binary.insert(0, byteArrayToBin(rbff[299], i));
        }
        row.append(signedBinaryToDecimal(binary.toString())).append(TcmsConstant.COLUMN_SEPARATOR);// APBR
        row.append(multiByteToDecimal(rbff, 300, 301)).append(TcmsConstant.COLUMN_SEPARATOR);

        row.append(binaryToDecimal(rbff, 302, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 303, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 304, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        // row.append(multiByteToDecimal(rbff, 305, 306) +
        // separator);//APBR
        binary = new StringBuilder();
        for (int i = 0; i <= 7; i++) {
            binary.insert(0, byteArrayToBin(rbff[305], i));
        }
        for (int i = 0; i <= 7; i++) {
            binary.insert(0, byteArrayToBin(rbff[306], i));
        }
        row.append(signedBinaryToDecimal(binary.toString())).append(TcmsConstant.COLUMN_SEPARATOR);// APBR
        row.append(multiByteToDecimal(rbff, 307, 308)).append(TcmsConstant.COLUMN_SEPARATOR);

        row.append(binaryToDecimal(rbff, 309, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 310, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 311, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        // row.append(multiByteToDecimal(rbff, 312, 313) +
        // separator);//APBR
        binary = new StringBuilder();
        for (int i = 0; i <= 7; i++) {
            binary.insert(0, byteArrayToBin(rbff[312], i));
        }
        for (int i = 0; i <= 7; i++) {
            binary.insert(0, byteArrayToBin(rbff[313], i));
        }
        row.append(signedBinaryToDecimal(binary.toString())).append(TcmsConstant.COLUMN_SEPARATOR);// APBR
        row.append(multiByteToDecimal(rbff, 314, 315)).append(TcmsConstant.COLUMN_SEPARATOR);

        row.append(binaryToDecimal(rbff, 316, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 317, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 318, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        // row.append(multiByteToDecimal(rbff, 319, 320) +
        // separator);//열차번호
        String trainNo = binaryToHex(rbff, 320, 4, 7)
                + binaryToHex(rbff, 320, 0, 3) + binaryToHex(rbff, 319, 4, 7)
                + binaryToHex(rbff, 319, 0, 3);
        row.append(trainNo).append(TcmsConstant.COLUMN_SEPARATOR);// 열차번호
    }

    /**
     * ecu정보 생성
     * @param rbff rbff
     * @param row row
     */
    private void Ecu(byte[] rbff, StringBuffer row) {
        // 0호차
        row.append(binaryToDecimal(rbff, 321, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 322, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 323, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 324, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        for (int i = 0; i <= 7; i++) {
            if (i != 5 && i != 6)
                row.append(oneBinaryToDecimal(rbff, 325, i)).append(TcmsConstant.COLUMN_SEPARATOR);
        }

        row.append(binaryToDecimal(rbff, 326, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 327, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 328, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 329, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        for (int i = 0; i <= 7; i++) {
            if (i != 5 && i != 6)
                row.append(oneBinaryToDecimal(rbff, 330, i)).append(TcmsConstant.COLUMN_SEPARATOR);
        }

        row.append(binaryToDecimal(rbff, 331, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 332, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 333, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 334, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        for (int i = 0; i <= 7; i++) {
            if (i != 5 && i != 6)
                row.append(oneBinaryToDecimal(rbff, 335, i)).append(TcmsConstant.COLUMN_SEPARATOR);
        }

        row.append(binaryToDecimal(rbff, 336, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 337, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 338, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 339, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);

        for (int i = 0; i <= 7; i++) {
            if (i != 5 && i != 6)
                row.append(oneBinaryToDecimal(rbff, 340, i)).append(TcmsConstant.COLUMN_SEPARATOR);
        }

        row.append(binaryToDecimal(rbff, 341, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 342, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 343, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 344, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);

        for (int i = 0; i <= 7; i++) {
            if (i != 5 && i != 6)
                row.append(oneBinaryToDecimal(rbff, 345, i)).append(TcmsConstant.COLUMN_SEPARATOR);
        }

        row.append(binaryToDecimal(rbff, 346, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 347, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 348, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 349, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);

        for (int i = 0; i <= 7; i++) {
            if (i != 5 && i != 6)
                row.append(oneBinaryToDecimal(rbff, 350, i)).append(TcmsConstant.COLUMN_SEPARATOR);
        }

        row.append(binaryToDecimal(rbff, 351, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 352, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 353, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 354, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);

        for (int i = 0; i <= 7; i++) {
            if (i != 5 && i != 6)
                row.append(oneBinaryToDecimal(rbff, 355, i)).append(TcmsConstant.COLUMN_SEPARATOR);
        }

        row.append(binaryToDecimal(rbff, 356, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 357, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 358, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 359, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);

        for (int i = 0; i <= 7; i++) {
            if (i != 5 && i != 6)
                row.append(oneBinaryToDecimal(rbff, 360, i)).append(TcmsConstant.COLUMN_SEPARATOR);
        }

        row.append(binaryToDecimal(rbff, 361, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 362, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 363, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 364, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);

        for (int i = 0; i <= 7; i++) {
            if (i != 5 && i != 6)
                row.append(oneBinaryToDecimal(rbff, 365, i)).append(TcmsConstant.COLUMN_SEPARATOR);
        }

        row.append(binaryToDecimal(rbff, 366, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 367, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 368, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 369, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);

        for (int i = 0; i <= 7; i++) {
            if (i != 5 && i != 6)
                row.append(oneBinaryToDecimal(rbff, 370, i)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
    }

    /**
     * 0화 정보 생성
     *
     * @param rbff rbff
     * @param row row
     */
    private void ZeroTrain(byte[] rbff, StringBuffer row) {
        // 0호차_LIU1
        for (int i = 38; i <= 39; i++) {
            for (int j = 0; j <= 6; j++) {
                row.append(oneBinaryToDecimal(rbff, i, j)).append(TcmsConstant.COLUMN_SEPARATOR);
            }
        }
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 40, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        row.append(oneBinaryToDecimal(rbff, 41, 2)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 41, 3)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 41, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 42, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        row.append(oneBinaryToDecimal(rbff, 43, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 43, 5)).append(TcmsConstant.COLUMN_SEPARATOR);
        for (int i = 46; i <= 47; i++) {
            for (int j = 0; j <= 7; j++) {
                row.append(oneBinaryToDecimal(rbff, i, j)).append(TcmsConstant.COLUMN_SEPARATOR);
            }
        }
        row.append(oneBinaryToDecimal(rbff, 48, 0)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 48, 1)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 48, 2)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 48, 3)).append(TcmsConstant.COLUMN_SEPARATOR);
        for (int i = 49; i <= 50; i++) {
            for (int j = 0; j <= 7; j++) {
                row.append(oneBinaryToDecimal(rbff, i, j)).append(TcmsConstant.COLUMN_SEPARATOR);
            }
        }
        row.append(binaryToDecimal(rbff, 51, 0, 3)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 51, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 51, 5)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 51, 6)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 51, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 52, 0)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 52, 1)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 52, 2)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 52, 3)).append(TcmsConstant.COLUMN_SEPARATOR);

        // 0호차_LIU2
        for (int i = 54; i <= 55; i++) {
            for (int j = 0; j <= 6; j++) {
                row.append(oneBinaryToDecimal(rbff, i, j)).append(TcmsConstant.COLUMN_SEPARATOR);
            }
        }
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 56, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        row.append(oneBinaryToDecimal(rbff, 57, 2)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 57, 3)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 57, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 58, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        row.append(oneBinaryToDecimal(rbff, 59, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 59, 5)).append(TcmsConstant.COLUMN_SEPARATOR);
        for (int i = 62; i <= 63; i++) {
            for (int j = 0; j <= 7; j++) {
                row.append(oneBinaryToDecimal(rbff, i, j)).append(TcmsConstant.COLUMN_SEPARATOR);
            }
        }
        row.append(oneBinaryToDecimal(rbff, 64, 0)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 64, 1)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 64, 2)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 64, 3)).append(TcmsConstant.COLUMN_SEPARATOR);
        for (int i = 65; i <= 66; i++) {
            for (int j = 0; j <= 7; j++) {
                row.append(oneBinaryToDecimal(rbff, i, j)).append(TcmsConstant.COLUMN_SEPARATOR);
            }
        }
        row.append(binaryToDecimal(rbff, 67, 0, 3)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 67, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 67, 5)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 67, 6)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 67, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 68, 0)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 68, 1)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 68, 2)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 68, 3)).append(TcmsConstant.COLUMN_SEPARATOR);

    }

    /**
     * 9호차 정보 생성
     *
     * @param rbff rbff
     * @param row row
     */
    private void NineTrain(byte[] rbff, StringBuffer row) {
        // 9호차_LIU1
        for (int i = 70; i <= 71; i++) {
            for (int j = 0; j <= 6; j++) {
                row.append(oneBinaryToDecimal(rbff, i, j)).append(TcmsConstant.COLUMN_SEPARATOR);
            }
        }
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 72, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        row.append(oneBinaryToDecimal(rbff, 73, 2)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 73, 3)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 73, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 74, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        row.append(oneBinaryToDecimal(rbff, 75, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 75, 5)).append(TcmsConstant.COLUMN_SEPARATOR);
        for (int i = 78; i <= 79; i++) {
            for (int j = 0; j <= 7; j++) {
                row.append(oneBinaryToDecimal(rbff, i, j)).append(TcmsConstant.COLUMN_SEPARATOR);
            }
        }
        row.append(oneBinaryToDecimal(rbff, 80, 0)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 80, 1)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 80, 2)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 80, 3)).append(TcmsConstant.COLUMN_SEPARATOR);
        for (int i = 81; i <= 82; i++) {
            for (int j = 0; j <= 7; j++) {
                row.append(oneBinaryToDecimal(rbff, i, j)).append(TcmsConstant.COLUMN_SEPARATOR);
            }
        }
        row.append(binaryToDecimal(rbff, 83, 0, 3)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 83, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 83, 5)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 83, 6)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 83, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 84, 0)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 84, 1)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 84, 2)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 84, 3)).append(TcmsConstant.COLUMN_SEPARATOR);

        // 9호차_LIU2
        for (int i = 86; i <= 87; i++) {
            for (int j = 0; j <= 6; j++) {
                row.append(oneBinaryToDecimal(rbff, i, j)).append(TcmsConstant.COLUMN_SEPARATOR);
            }
        }
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 88, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        row.append(oneBinaryToDecimal(rbff, 89, 2)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 89, 3)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 89, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 90, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        row.append(oneBinaryToDecimal(rbff, 91, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 91, 5)).append(TcmsConstant.COLUMN_SEPARATOR);
        for (int i = 94; i <= 95; i++) {
            for (int j = 0; j <= 7; j++) {
                row.append(oneBinaryToDecimal(rbff, i, j)).append(TcmsConstant.COLUMN_SEPARATOR);
            }
        }
        row.append(oneBinaryToDecimal(rbff, 96, 0)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 96, 1)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 96, 2)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 96, 3)).append(TcmsConstant.COLUMN_SEPARATOR);
        for (int i = 97; i <= 98; i++) {
            for (int j = 0; j <= 7; j++) {
                row.append(oneBinaryToDecimal(rbff, i, j)).append(TcmsConstant.COLUMN_SEPARATOR);
            }
        }
        row.append(binaryToDecimal(rbff, 99, 0, 3)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 99, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 99, 5)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 99, 6)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 99, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 100, 0)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 100, 1)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 100, 2)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 100, 3)).append(TcmsConstant.COLUMN_SEPARATOR);
    }

    /**
     * CC DI정보 생성
     *
     * @param rbff rbff
     * @param row row
     */
    private void ccDi(byte[] rbff, StringBuffer row) {
        // 1호차
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 154, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 155, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 156, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 2; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 157, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 158, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        row.append(oneBinaryToDecimal(rbff, 159, 0)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 159, 1)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 159, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 159, 5)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToHex(rbff, 160, 4, 7))
                .append(binaryToHex(rbff, 160, 0, 3)).append(TcmsConstant.COLUMN_SEPARATOR);

        // 2호차
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 162, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 163, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 164, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 2; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 165, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 166, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        row.append(oneBinaryToDecimal(rbff, 167, 0)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 167, 1)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 167, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 167, 5)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToHex(rbff, 168, 4, 7))
                .append(binaryToHex(rbff, 168, 0, 3)).append(TcmsConstant.COLUMN_SEPARATOR);

        // 3호차
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 170, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 171, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 172, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 2; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 173, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 174, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        row.append(oneBinaryToDecimal(rbff, 175, 0)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 175, 1)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 175, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 175, 5)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToHex(rbff, 176, 4, 7))
                .append(binaryToHex(rbff, 176, 0, 3))
                .append(TcmsConstant.COLUMN_SEPARATOR);

        // 4호차
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 178, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 179, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 180, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 2; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 181, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 182, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        row.append(oneBinaryToDecimal(rbff, 183, 0)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 183, 1)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 183, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 183, 5)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToHex(rbff, 184, 4, 7))
                .append(binaryToHex(rbff, 184, 0, 3))
                .append(TcmsConstant.COLUMN_SEPARATOR);

        // 5호차
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 186, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 187, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 188, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 2; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 189, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 190, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        row.append(oneBinaryToDecimal(rbff, 191, 0)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 191, 1)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 191, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 191, 5)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToHex(rbff, 192, 4, 7))
                .append(binaryToHex(rbff, 192, 0, 3))
                .append(TcmsConstant.COLUMN_SEPARATOR);

        // 6호차
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 194, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 195, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 196, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 2; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 197, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 198, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        row.append(oneBinaryToDecimal(rbff, 199, 0)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 199, 1)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 199, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 199, 5)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToHex(rbff, 200, 4, 7))
                .append(binaryToHex(rbff, 200, 0, 3))
                .append(TcmsConstant.COLUMN_SEPARATOR);

        // 7호차
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 202, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 203, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 204, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 2; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 205, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 206, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        row.append(oneBinaryToDecimal(rbff, 207, 0)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 207, 1)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 207, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 207, 5)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToHex(rbff, 208, 4, 7))
                .append(binaryToHex(rbff, 208, 0, 3))
                .append(TcmsConstant.COLUMN_SEPARATOR);

        // 8호차
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 210, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 211, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 212, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 2; j <= 7; j++) {
            row.append(oneBinaryToDecimal(rbff, 213, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int j = 0; j <= 6; j++) {
            row.append(oneBinaryToDecimal(rbff, 214, j)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        row.append(oneBinaryToDecimal(rbff, 215, 0)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 215, 1)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 215, 4)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(oneBinaryToDecimal(rbff, 215, 5)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToHex(rbff, 216, 4, 7))
                .append(binaryToHex(rbff, 216, 0, 3))
                .append(TcmsConstant.COLUMN_SEPARATOR);

    }

    /**
     * ATO 생성
     *
     * @param rbff rbff
     * @param idx idx
     * @param debugRow debug용 row index
     * @param row row
     */
    private void Ato(byte[] rbff, int idx, int debugRow, StringBuffer row) {
        for (int i = 0; i <= 5; i++) {
            row.append(oneBinaryToDecimal(rbff, 228, i)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        row.append(binaryToDecimal(rbff, 230, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 232, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        for (int i = 0; i <= 2; i++) {
            row.append(oneBinaryToDecimal(rbff, 233, i)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        // row.append(multiByteToDecimal(rbff, 234, 235) +
        // separator);//TRAIN NO
        String trainNo = binaryToHex(rbff, 235, 4, 7)
                + binaryToHex(rbff, 235, 0, 3) + binaryToHex(rbff, 234, 4, 7)
                + binaryToHex(rbff, 234, 0, 3);
        row.append(trainNo).append(TcmsConstant.COLUMN_SEPARATOR);// TRAIN No.
        if (idx == debugRow) {
            System.out.println(" 			234 trainNo :: " + trainNo);
        }

        for (int i = 0; i <= 3; i++) {
            row.append(oneBinaryToDecimal(rbff, 236, i)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        for (int i = 0; i <= 4; i++) {
            row.append(oneBinaryToDecimal(rbff, 237, i)).append(TcmsConstant.COLUMN_SEPARATOR);
        }
        row.append(oneBinaryToDecimal(rbff, 237, 7)).append(TcmsConstant.COLUMN_SEPARATOR);

        // row.append(binaryToDecimal(rbff, 238, 0, 7)).append(DawonConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 238, 0, 3)).append(TcmsConstant.COLUMN_SEPARATOR); // 차륜경
        row.append(binaryToDecimal(rbff, 239, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 240, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        row.append(binaryToDecimal(rbff, 241, 0, 7)).append(TcmsConstant.COLUMN_SEPARATOR);
        for (int i = 0; i <= 4; i++) {
            row.append(oneBinaryToDecimal(rbff, 242, i)).append(TcmsConstant.COLUMN_SEPARATOR);
        }

    }

    /**
     * 날짜 생성
     *
     * @param rbff rbff
     * @return string
     */
    private String makeDate(byte[] rbff) {
        StringBuilder yearBinary = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            yearBinary.insert(0, byteArrayToBin(rbff[2], i));
        }
        int year = Integer.parseInt(yearBinary.toString(), 2); // 10진수값 변경
        year = 2010 + year;
        // COMUtil.debug("yearBinary ::::::::::" + yearBinary);
        // COMUtil.debug("year ::::::::::" + year);

        StringBuilder monBinary = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (i < 2) {
                monBinary.insert(0, byteArrayToBin(rbff[2], i + 6));
            } else {
                monBinary.insert(0, byteArrayToBin(rbff[3], i - 2));
            }
        }
        int mon = Integer.parseInt(monBinary.toString(), 2); // 10진수값 변경
        // COMUtil.debug("monBinary ::::::::::" + monBinary);
        // COMUtil.debug("mon ::::::::::" + mon);

        StringBuilder dayBinary = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            dayBinary.insert(0, byteArrayToBin(rbff[3], i + 2));
        }
        int day = Integer.parseInt(dayBinary.toString(), 2); // 10진수값 변경
        // COMUtil.debug("dayBinary ::::::::::" + dayBinary);
        // COMUtil.debug("day ::::::::::" + day);

        StringBuilder hourBinary = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i < 1) {
                hourBinary.insert(0, byteArrayToBin(rbff[3], i + 7));
            } else {
                hourBinary.insert(0, byteArrayToBin(rbff[4], i - 1));
            }
        }
        int hour = Integer.parseInt(hourBinary.toString(), 2); // 10진수값 변경
        // COMUtil.debug("hourBinary ::::::::::" + hourBinary);
        // COMUtil.debug("hour ::::::::::" + hour);

        StringBuilder minBinary = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            if (i < 4) {
                minBinary.insert(0, byteArrayToBin(rbff[4], i + 4));
            } else {
                minBinary.insert(0, byteArrayToBin(rbff[5], i - 4));
            }
        }
        int min = Integer.parseInt(minBinary.toString(), 2); // 10진수값 변경
        // COMUtil.debug("minBinary ::::::::::" + minBinary);
        // COMUtil.debug("min ::::::::::" + min);

        StringBuilder secBinary = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            secBinary.insert(0, byteArrayToBin(rbff[5], i + 2));
        }
        int sec = Integer.parseInt(secBinary.toString(), 2); // 10진수값 변경
        // COMUtil.debug("secBinary ::::::::::" + secBinary);
        // COMUtil.debug("sec ::::::::::" + sec);

//        String str = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, mon, day, hour, min, sec);
        // COMUtil.debug(str);

        return String.format("%04d-%02d-%02d %02d:%02d:%02d", year, mon, day, hour, min, sec);
    }
}
