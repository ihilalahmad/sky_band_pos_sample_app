package com.girmiti.skybandecr.fragment.transaction;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.girmiti.skybandecr.R;
import com.girmiti.skybandecr.databinding.TransactionTypeFragmentBinding;

public class TransactionTypeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private TransactionTypeViewModel transactionTypeViewModel;
    private TransactionTypeFragmentBinding transactionTypeFragmentBinding;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        transactionTypeFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.transaction_type_fragment, container, false);
       transactionTypeViewModel = ViewModelProviders.of(this).get(TransactionTypeViewModel.class);

        getActivity().findViewById(R.id.home_logo).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.left).setVisibility(View.VISIBLE);

        setupListeners();

        return transactionTypeFragmentBinding.getRoot();
    }

    private void setupListeners() {

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.transaction_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transactionTypeFragmentBinding.transactionSpinner.setAdapter(adapter);

        transactionTypeFragmentBinding.transactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedItem = transactionTypeFragmentBinding.transactionSpinner.getSelectedItem().toString();

                if (selectedItem.equals("purchase")) {
                    navController.navigate(R.id.action_transactionTypeFragment_to_bufferResponseFragment);
                }


            }
        });

        transactionTypeFragmentBinding.transactionSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }


    private void getVisibilityOfViews(String selectedItem) {

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final String selectedItem = parent.getItemAtPosition(position).toString();
        transactionTypeViewModel.resetVisibilityOfViews(transactionTypeFragmentBinding);
        getVisibilityOfViews(selectedItem);

        if (selectedItem.equals("purchase")) {
            transactionTypeFragmentBinding.payAmt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
