package com.girmiti.skybandecr.transaction;

import com.girmiti.skybandecr.ui.fragment.home.HomeViewModel;
import com.girmiti.skybandecr.transaction.listener.TransactionListener;
import com.girmiti.skybandecr.sdk.logger.Logger;

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
            String terminalResponse = homeViewModel.getTerminalResponse();
            logger.debug("Terminal response>>> " + terminalResponse);
            homeViewModel.setParseResponse(terminalResponse);
            logger.info("Response parsed");
            transactionListener.onSuccess();
        } catch (final Exception e) {
            logger.severe("Exception on transaction", e);
            transactionListener.onError(e);
        }
    }
}
