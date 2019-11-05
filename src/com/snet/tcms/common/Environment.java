package com.snet.tcms.common;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Environment {
    // 시스템 환경파일
    //usage : -Dbpm.api.config.file=c:/_bpm/wfenvironment.properties    
    public static final String DEFAULT_PROPERTIES = getDefaultProperties();

    private String propertyFile = DEFAULT_PROPERTIES;

    private Properties properties = new Properties();

    private static Context ic = null;

    public static Context getInitialContext() {
        if (ic == null) {
            synchronized (DEFAULT_PROPERTIES) {
                if (ic == null) {
                    try {
                        ic = new InitialContext();
                    } catch (Exception e) {
                        ComUtil.debug(e.toString());
                        ic = null;
                    }
                }
            }
        }

        return ic;
    }

    private static String getDefaultProperties() {
        String rtn = null;
        String configfile = System.getProperty("train.instance");
        if ((null == configfile) || ("".equals(configfile))) {
        	String WorkingDirectory = System.getProperty("user.dir");
            rtn = WorkingDirectory + File.separator + "config" + File.separator + "environment.properties";
        } else {
            rtn = configfile;
        }

        return rtn;
    }

    /**
     * 클래스 생성자이다. 클래스가 인스턴스화 될때 default setting된 파일을 읽는다.
     * @see com.hanabank.mca.bpm.controller.base.common.Object#Object()
     */
    Environment() {
        loadProperty();
    }

    /**
     * 클래스 인스턴스 생성시 요청한 Resource파일을 읽는다.
     * @param propertyFile 환경정보 파일
     */
    Environment(String propertyFile) {
        this.propertyFile = propertyFile;
        loadProperty();
    }

    /**
     * 환경정보 파일을 읽는다.
     */
    public void loadProperty() {
        FileInputStream fis =null;
        InputStream fiss = null;
        try {
        	if(propertyFile.equals("environment.properties")){
        		fiss = getClass().getClassLoader().getResourceAsStream(propertyFile);
        		properties.load(fiss);
        	}else{
        		fis = new FileInputStream(propertyFile);
        		properties.load(fis);
        	}
            
            
        } catch (Exception e) {
        	ComUtil.debug("[Environment : loadProperty()] "
                    + propertyFile
                    + " 파일을 읽을 수 없습니다.");
        	ComUtil.debug("[Environment : loadProperty()] "
                    + propertyFile
                    + " 파일이 CLASSPATH에 있는지 확인하세요.");
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (fiss != null)
                	fiss.close();                
            } catch (IOException e) {
            	ComUtil.debug("[Environment : loadProperty()] InputStream close중 에러 발생.");
            }
        }
    }

    /**
     * 환경정보 식별값에 해당하는 정보를 retrun한다.
     * @param key 요청 정보의 식별값
     * @return String 요청 정보
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public Properties getProperties() {
        return this.properties;
    }

    public boolean reload() {
        try {
            loadProperty();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}