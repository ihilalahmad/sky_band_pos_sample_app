package com.skyband.ecr.ui.fragment.connected;

import androidx.databinding.DataBindingUtil;

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

import com.skyband.ecr.R;
import com.skyband.ecr.databinding.ConnectedFragmentBinding;
import com.skyband.ecr.sdk.logger.Logger;

import java.util.Objects;

public class ConnectedFragment extends Fragment {

    private ConnectedFragmentBinding connectedFragmentBinding;
    private NavController navController;
    private Logger logger = Logger.getNewLogger(ConnectedFragment.class.getName());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        connectedFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.connected_fragment, container, false);

        Objects.requireNonNull(getActivity()).findViewById(R.id.home_logo).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.left).setVisibility(View.INVISIBLE);

        setupListeners();

        return connectedFragmentBinding.getRoot();
    }

    private void setupListeners() {

        final NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build();
        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);

        connectedFragmentBinding.buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_connectedFragment_to_homeFragment, null, options);
            }
        });
    }
}
