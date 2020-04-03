package com.girmiti.skybandecr.listner;

public interface TransactionListener {

    void onSuccess();
    void onError(String errorMessage);
}
