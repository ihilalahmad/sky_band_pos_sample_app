package com.skyband.ecr.cache;

import com.skyband.ecr.constant.Constant;

public class GeneralParamCache extends Cache  {

    private static class LazyHolder {
        private static final GeneralParamCache INSTANCE = new GeneralParamCache();
    }

    private GeneralParamCache() {
        super(SP_NAME_GENERAL_PARAM);

        load();
    }

    private void load() {
        if ( getString(Constant.APPLICATION_NAME) == null ) {
            putString(Constant.APPLICATION_NAME, "Test ECR Application");
            putString(Constant.SOFTWARE_VERSION, "1.0.0");
            putString(Constant.PROVIDER_ID, "Girmiti Software Pvt. Ltd");
        }
    }

    public static GeneralParamCache getInstance() {
        return LazyHolder.INSTANCE;
    }
}
