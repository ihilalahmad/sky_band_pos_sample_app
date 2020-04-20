package com.girmiti.skybandecr.transaction.listener;

public interface TransactionListener {

    void onSuccess();

    void onError(Exception e);
}
