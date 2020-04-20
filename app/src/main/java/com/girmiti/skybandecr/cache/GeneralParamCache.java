package com.girmiti.skybandecr.cache;

import com.girmiti.skybandecr.constant.Constant;

public class GeneralParamCache extends Cache implements Constant {

    private static class LazyHolder {
        private static final GeneralParamCache INSTANCE = new GeneralParamCache();
    }

    private GeneralParamCache() {
        super(SP_NAME_GENERAL_PARAM);

        load();
    }

    private void load() {
        if ( getString(APPLICATION_NAME) == null ) {
            putString(APPLICATION_NAME, "Test ECR Application");
            putString(SOFTWARE_VERSION, "1.0.0");
            putString(PROVIDER_ID, "Girmiti Software Pvt. Ltd");
        }
    }

    public static GeneralParamCache getInstance() {
        return LazyHolder.INSTANCE;
    }
}
