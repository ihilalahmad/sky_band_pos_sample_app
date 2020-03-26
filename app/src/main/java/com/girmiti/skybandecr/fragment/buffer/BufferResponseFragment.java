package com.girmiti.skybandecr.fragment.buffer;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.girmiti.skybandecr.R;
import com.girmiti.skybandecr.databinding.BufferResponseFragmentBinding;

public class BufferResponseFragment extends Fragment {

    private BufferResponseViewModel mViewModel;
    protected NavController navController;
    private BufferResponseFragmentBinding bufferResponseFragmentBinding;

    private Button okButton;

    public static BufferResponseFragment newInstance() {
        return new BufferResponseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        bufferResponseFragmentBinding= DataBindingUtil.inflate(inflater,R.layout.buffer_response_fragment,container,false);
        setupListeners();
        return bufferResponseFragmentBinding.getRoot();
    }

    private void setupListeners() {

        navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        okButton=bufferResponseFragmentBinding.okButton;

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.action_bufferResponseFragment_to_homeFragment);

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(BufferResponseViewModel.class);
        // TODO: Use the ViewModel
    }

}
