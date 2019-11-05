package com.snet.tcms.tcode;

public class TroubleCodePropertyManager {

    private static TroubleCodePropertyManager instance = null;
    private final TroubleCodeProperty troubleCodeProperty;

    private TroubleCodePropertyManager() {
        troubleCodeProperty = new TroubleCodeProperty();
    }

    /**
     * EnvManager class 생성
     * @return EnvManager
     */
    private synchronized static TroubleCodePropertyManager getInstance() {
        if(instance == null) {
            instance = new TroubleCodePropertyManager();
        }
        return instance;
    }

    /**
     * Environment Class 생성
     * @return Environment
     */
    public static TroubleCodeProperty getTroubleCodeProperty() {
        return getInstance().troubleCodeProperty;
    }
}
