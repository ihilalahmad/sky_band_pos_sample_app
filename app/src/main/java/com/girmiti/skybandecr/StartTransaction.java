package com.girmiti.skybandecr;

import com.girmiti.skybandecr.fragment.home.HomeViewModel;
import com.girmiti.skybandecr.listner.TransactionListener;
import com.girmiti.skybandecr.sdk.logger.Logger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class StartTransaction implements Runnable {

    private Logger logger = Logger.getNewLogger(StartTransaction.class.getName());
    private HomeViewModel homeViewModel;
    private TransactionListener transactionListener;

    public StartTransaction(HomeViewModel homeViewModel) {
        this.homeViewModel = homeViewModel;
    }

    public void setTransactionListener(TransactionListener transactionListener) {
        this.transactionListener = transactionListener;
    }

    @Override
    public void run() {
        try {
            byte[] packData = homeViewModel.packData();
            String terminalResponse = homeViewModel.getTerminalResponse(packData);
            homeViewModel.parse(terminalResponse);
            logger.info("Got Response");
            transactionListener.onSuccess();
        } catch (final IOException | NoSuchAlgorithmException e) {
            logger.severe("Exception on transaction", e);
            transactionListener.onError(e.getMessage());
        }
    }
}
