package com.girmiti.skybandecr.fragment.transaction;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.girmiti.skybandecr.R;

public class TransactionTypeFragment extends Fragment {

    private TransactionTypeViewModel mViewModel;

    public static TransactionTypeFragment newInstance() {
        return new TransactionTypeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.transaction_type_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TransactionTypeViewModel.class);
        // TODO: Use the ViewModel
    }

}
