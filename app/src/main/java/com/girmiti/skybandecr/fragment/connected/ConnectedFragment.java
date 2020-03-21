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

import com.girmiti.skybandecr.R;
import com.girmiti.skybandecr.databinding.ConnectedFragmentBinding;

public class ConnectedFragment extends Fragment {

     private ConnectedViewModel mViewModel;
     private ConnectedFragmentBinding connectedFragmentBinding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        connectedFragmentBinding= DataBindingUtil.inflate(inflater,R.layout.connected_fragment, container, false);

        mViewModel = ViewModelProviders.of(this).get(ConnectedViewModel.class);
        return connectedFragmentBinding.getRoot();
    }


}
