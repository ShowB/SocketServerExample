package com.snet.tcms.common;

@SuppressWarnings("serial")                                         // Serial warmings 안나오게
public class ConversionException extends RuntimeException {              // RuntimeException 상속받는다.

    private String message;
    private String messageCode;
    private String[] messageArgs;
    private static final int DEFAULT_ERROR = 200;
    private int errorCode = DEFAULT_ERROR;

    public ConversionException() {
        super();
    }

    public ConversionException(int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public ConversionException(String messge) {
        this(messge, DEFAULT_ERROR);
    }

    public ConversionException(String messge, int errorCode) {
        super(messge);
        this.errorCode = errorCode;
    }

    public ConversionException(Throwable cause) {
        this(cause, DEFAULT_ERROR);
    }

    public ConversionException(Throwable cause, int errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ConversionException(String message, Throwable cause) {
        this(message, cause, DEFAULT_ERROR);
    }

    public ConversionException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ConversionException(String messageCode, String[] messageArgs) {
        this(messageCode, messageArgs, DEFAULT_ERROR);
    }

    public ConversionException(String messageCode, String[] messageArgs, int errorCode) {
        this.messageCode = messageCode;
        this.messageArgs = messageArgs;
        this.errorCode = errorCode;
    }

    public ConversionException(String messageCode, String[] messageArgs, Throwable cause) {
        this(messageCode, messageArgs, cause, DEFAULT_ERROR);
    }

    public ConversionException(String messageCode, String[] messageArgs, Throwable cause, int errorCode) {
        super(cause);
        this.messageCode = messageCode;
        this.messageArgs = messageArgs;
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message == null ? super.getMessage() : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StackTraceElement[] getStatckTrace() {
        return super.getStackTrace();
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String[] getMessageArgs() {
        return messageArgs;
    }

    public void setMessageArgs(String[] messageArgs) {
        this.messageArgs = messageArgs;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
