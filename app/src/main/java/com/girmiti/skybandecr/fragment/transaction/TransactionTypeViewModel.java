package com.girmiti.skybandecr.fragment.transaction;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.girmiti.skybandecr.databinding.TransactionTypeFragmentBinding;

public class TransactionTypeViewModel extends ViewModel {

    TransactionTypeFragmentBinding transactionTypeFragmentBinding;

    public void resetVisibilityOfViews(TransactionTypeFragmentBinding transactionTypeFragmentBinding) {

        this.transactionTypeFragmentBinding=transactionTypeFragmentBinding;

        this.transactionTypeFragmentBinding.payAmt.setVisibility(View.GONE);
        this.transactionTypeFragmentBinding.payAmtTv.setVisibility(View.GONE);

        this.transactionTypeFragmentBinding.refundAmt.setVisibility(View.GONE);
        this.transactionTypeFragmentBinding.refundAmtTv.setVisibility(View.GONE);

        this.transactionTypeFragmentBinding.authAmt.setVisibility(View.GONE);
        this.transactionTypeFragmentBinding.authAmtTv.setVisibility(View.GONE);

        this.transactionTypeFragmentBinding.cashAdvanceAmt.setVisibility(View.GONE);
        this.transactionTypeFragmentBinding.cashAdvanceAmtTv.setVisibility(View.GONE);

        this.transactionTypeFragmentBinding.cashBackAmt.setVisibility(View.GONE);
        this.transactionTypeFragmentBinding.cashBackAmtTv.setVisibility(View.GONE);

        this.transactionTypeFragmentBinding.ecrRefNo.setVisibility(View.GONE);
        this.transactionTypeFragmentBinding.ecrRefNoTv.setVisibility(View.GONE);

        this.transactionTypeFragmentBinding.origApproveCode.setVisibility(View.GONE);
        this.transactionTypeFragmentBinding.origApproveCodeTv.setVisibility(View.GONE);

        this.transactionTypeFragmentBinding.origRefundDate.setVisibility(View.GONE);
        this.transactionTypeFragmentBinding.origRefundDateTv.setVisibility(View.GONE);

        this.transactionTypeFragmentBinding.samaKeyIndex.setVisibility(View.GONE);
        this.transactionTypeFragmentBinding.samaKeyIndexTv.setVisibility(View.GONE);

        this.transactionTypeFragmentBinding.trsmId.setVisibility(View.GONE);
        this.transactionTypeFragmentBinding.trsmIdTv.setVisibility(View.GONE);

        this.transactionTypeFragmentBinding.origTransactionAmt.setVisibility(View.GONE);
        this.transactionTypeFragmentBinding.origTransactionAmtTv.setVisibility(View.GONE);

        this.transactionTypeFragmentBinding.origTransactionDate.setVisibility(View.GONE);
        this.transactionTypeFragmentBinding.origTransactionDateTv.setVisibility(View.GONE);

        this.transactionTypeFragmentBinding.vendorKeyIndex.setVisibility(View.GONE);
        this.transactionTypeFragmentBinding.vendorKeyIndexTv.setVisibility(View.GONE);

        this.transactionTypeFragmentBinding.vendorId.setVisibility(View.GONE);
        this.transactionTypeFragmentBinding.vendorIdTv.setVisibility(View.GONE);

        this.transactionTypeFragmentBinding.rrnNoEditText.setVisibility(View.GONE);
        this.transactionTypeFragmentBinding.rrnNoTextView.setVisibility(View.GONE);

        this.transactionTypeFragmentBinding.vendorTerminalType.setVisibility(View.GONE);
        this.transactionTypeFragmentBinding.vendorTerminalTypeTv.setVisibility(View.GONE);
    }

    public void getVisibilityOfViews(String selectedItem) {

    }

}
