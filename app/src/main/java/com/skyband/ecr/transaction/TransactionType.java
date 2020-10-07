package com.skyband.ecr.transaction;

import lombok.Getter;
import lombok.Setter;

public enum TransactionType {

    PURCHASE("Purchase"),
    PURCHASE_CASHBACK("Purchase CashBack"),
    REFUND("Refund"),
    PRE_AUTHORISATION("Pre Authorisation"),
    PRE_AUTH_COMPLETION("Pre Auth Completion"),
    PRE_AUTH_EXTENSION("Pre Auth Extension"),
    PRE_AUTH_VOID("Pre Auth Void"),
    ADVICE("Advice"),
    CASH_ADVANCE("Cash Advance"),
    REVERSAL("Reversal"),
    RECONCILIATION("Reconciliation"),
    PARAMETER_DOWNLOAD("Parameter Download"),
    SET_SETTINGS("SET Settings"),
    GET_SETTINGS("GET Settings"),
    SET_TERMINAL_LANGUAGE("Set Terminal Language"),
    TERMINAL_STATUS("Terminal Status"),
    PREVIOUS_TRANSACTION_DETAILS("Previous Transaction Details"),
    REGISTER("Register"),
    START_SESSION("Start Session"),
    END_SESSION("End Session"),
    BILL_PAYMENT("Bill Payment"),
    RUNNING_TOTAL("Running Total"),
    PRINT_SUMMARY_REPORT("Print Summary Report"),
    REPEAT("Repeat"),
    CHECK_STATUS("Check Status");

    @Getter
    @Setter
    private String transactionType;

    TransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
