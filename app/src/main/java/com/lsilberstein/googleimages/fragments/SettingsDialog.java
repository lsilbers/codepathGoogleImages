package com.lsilberstein.googleimages.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.lsilberstein.googleimages.R;

/**
 * Created by lsilberstein on 10/31/15.
 */
public class SettingsDialog extends DialogFragment {

    private Spinner spSize;
    private Spinner spColour;
    private Spinner spType;
    private Spinner spSite;

    public SettingsDialog() {
    }

    public static SettingsDialog newInstance() {
        return new SettingsDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get the spinners
        spSize = (Spinner) view.findViewById(R.id.spSize);
        spColour = (Spinner) view.findViewById(R.id.spColour);
        spType = (Spinner) view.findViewById(R.id.spType);
        spSite = (Spinner) view.findViewById(R.id.spSite);

        Button cancel = (Button) view.findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Button save = (Button) view.findViewById(R.id.btnSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String size = spSize.getSelectedItem().toString();
                String colour = spColour.getSelectedItem().toString();
                String type = spType.getSelectedItem().toString();
                String site = spSite.getSelectedItem().toString();
                SettingsDialogListener listener = (SettingsDialogListener) getActivity();
                listener.onFiltersSaved(size, colour, type, site);
                dismiss();
            }
        });
    }

    public interface SettingsDialogListener {
        void onFiltersSaved(String size, String colour, String type, String site);
    }
}
