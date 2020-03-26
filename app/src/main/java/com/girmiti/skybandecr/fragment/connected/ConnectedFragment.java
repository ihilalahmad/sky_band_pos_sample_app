package com.girmiti.skybandecr.fragment.connected;

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
import android.widget.Button;

import com.girmiti.skybandecr.R;
import com.girmiti.skybandecr.databinding.ConnectedFragmentBinding;

public class ConnectedFragment extends Fragment {

    private ConnectedViewModel mViewModel;
    private ConnectedFragmentBinding connectedFragmentBinding;

    private Button okButton;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        connectedFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.connected_fragment, container, false);
        mViewModel = ViewModelProviders.of(this).get(ConnectedViewModel.class);

        getActivity().findViewById(R.id.home_logo).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.left).setVisibility(View.INVISIBLE);

        setupListeners();
        return connectedFragmentBinding.getRoot();
    }

    private void setupListeners() {

        okButton = connectedFragmentBinding.buttonOk;
        navController=Navigation.findNavController(getActivity(),R.id.nav_host_fragment);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_connectedFragment2_to_transactionTypeFragment);
            }
        });


    }
}
