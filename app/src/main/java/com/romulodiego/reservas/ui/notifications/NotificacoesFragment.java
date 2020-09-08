package com.romulodiego.reservas.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.romulodiego.reservas.R;

public class NotificacoesFragment extends Fragment {

    private NotificacoesViewModel notificacoesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificacoesViewModel =
                ViewModelProviders.of(this).get(NotificacoesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notificacoes, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificacoesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}