package nyc.c4q.ashiquechowdhury.auxx.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.List;

import nyc.c4q.ashiquechowdhury.auxx.R;

public class JoinRoomDialog extends DialogFragment {
    private EditText roomNameEditText;
    private EditText roomPasswordEditText;
    private NiceSpinner spinner;
    private SubmitRoomNameListener roomNameSubmitListener;
    private boolean roomIsPublic = true;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_room_name_password, null);

        roomNameEditText = (EditText) view.findViewById(R.id.room_name);
        roomPasswordEditText = (EditText) view.findViewById(R.id.room_password);
        spinner = (NiceSpinner) view.findViewById(R.id.create_room_spinner);
        List<String> roomChoice = Arrays.asList("Public", "Private");
        spinner.attachDataSource(roomChoice);

        roomPasswordEditText.setVisibility(View.INVISIBLE);
        spinner.attachDataSource(roomChoice);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                roomIsPublic = !roomIsPublic;
                if(!roomIsPublic)
                    roomPasswordEditText.setVisibility(View.VISIBLE);
                else
                    roomPasswordEditText.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        builder.setView(view)
                .setPositiveButton(R.string.join_room_prompt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String input = String.valueOf(roomNameEditText.getText());
                        if (roomIsPublic) {
                            roomNameSubmitListener.onJoinRoomPrompt(input);
                        } else {
                            String password = String.valueOf(roomPasswordEditText.getText());
                            roomNameSubmitListener.onJoinRoomPrompt(input, password);
                        }
                    }
                });
        return builder.create();
    }

    public void setSubmitListener(SubmitRoomNameListener submitListener) {
        this.roomNameSubmitListener = submitListener;
    }

    public interface SubmitRoomNameListener {
        void onJoinRoomPrompt(String roomNameInput);
        void onJoinRoomPrompt(String roomNameInput, String passwordInput);
    }
}
