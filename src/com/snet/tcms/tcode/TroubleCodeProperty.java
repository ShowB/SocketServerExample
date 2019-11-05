package com.snet.tcms.tcode;


import com.snet.tcms.common.ComUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class TroubleCodeProperty {

    private Properties properties;

    /**
     * 클래스 생성자이다. 클래스가 인스턴스화 될때 default setting된 파일을 읽는다.
     */
    TroubleCodeProperty() {
        loadProperty();
    }

    /**
     * 고장코드 파일을 읽는다.
     */
    public void loadProperty() {
        if (properties != null) {
            return;
        }

        FileInputStream fis = null;

        String resource = ComUtil.WorkingDirectory + File.separator + "config" + File.separator + "troublecode.properties";
        properties = new Properties();

        try {
            fis = new FileInputStream(resource);
            properties.load(fis);
        } catch (Exception e) {
            ComUtil.error(e.getMessage());

            try {
                if (fis != null)
                    fis.close();
            } catch (Exception e2) {
                ComUtil.error(e2.getMessage());
                loadProperty();
            }

            loadProperty();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (Exception e) {
                ComUtil.error(e.getMessage());
            }
        }
    }

    /**
     * 로템 TCMS 고장코드값에 해당하는 정보를 retrun한다.
     * @param key 요청 정보의 식별값
     * @return String 요청 정보
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * 로템 TCMS 고장코드값에 해당하는 정보를 retrun한다.
     * @param key 요청 정보의 식별값
     * @return String 요청 정보
     */
    public String getProperty(Integer key) {
        if (key == null)
            return "";

        String keyStr = String.valueOf(key);
        return getProperty(keyStr);
    }

    public Properties getProperties() {
        return this.properties;
    }

}
