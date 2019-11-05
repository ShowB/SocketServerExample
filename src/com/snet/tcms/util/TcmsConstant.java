package com.snet.tcms.util;

public class TcmsConstant {
    public final static String DAWON_HISTORY_TYPE_RUN = "RUN";
    public final static String DAWON_HISTORY_TYPE_TROUBLE = "TROUBLE";

    public final static String DAWON_RUN_FILE_PREFIX = "l";
    public final static String DAWON_TROUBLE_FILE_PREFIX = "t";

    public final static String COMPLETE_FILE_PREFIX = "complete_";
    public final static String ERROR_FILE_PREFIX = "error_";
    public final static String ENTER_VALUE = "\r\n";
    public final static String COLUMN_SEPARATOR = "|";

    public final static int DAWON_ASIS_PARSING_BYTE = 512;
    public final static int DAWON_TOBE_PARSING_BYTE = 1416;
    public final static int ROTEM_TOBE_PARSING_BYTE = 1408;

    public final static String DAWON_ASIS_CONVERTER_CLASS_NAME = "com.snet.tcms.converter.DawonAsisConverter";
    public final static String DAWON_TOBE_CONVERTER_CLASS_NAME = "com.snet.tcms.converter.DawonTobeConverter";
    public final static String ROTEM_TOBE_CONVERTER_CLASS_NAME = "com.snet.tcms.converter.RotemTobeConverter";
}
