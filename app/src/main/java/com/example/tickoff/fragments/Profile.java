package com.example.tickoff.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tickoff.DataChangeDialog;
import com.example.tickoff.DataToFragments;
import com.example.tickoff.PswChangeDialog;
import com.example.tickoff.R;
import com.example.tickoff.RequestTask;
import com.example.tickoff.Response;
import com.example.tickoff.UnixDateConverter;
import com.example.tickoff.UsernameChangeDialog;
import com.example.tickoff.activities.MainActivity;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends Fragment implements DataToFragments {

    private AppCompatImageView profileUserPicture;
    private AppCompatTextView profileUserFullname;
    private AppCompatTextView profileUserEmail;
    private AppCompatTextView profileUserUsername;
    private AppCompatTextView profileUserLevel;
    private AppCompatTextView profileUserBirthdate;
    private AppCompatTextView profileUserRegdate;
    private MaterialButton profileUserChange;
    private MaterialButton profileDataChange;
    private MaterialButton profilePswChange;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        init(view);

        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setdata(this);
        }

        profileUserChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsernameChangeDialog usernameChangeDialog = new UsernameChangeDialog();
                usernameChangeDialog.show(getActivity().getSupportFragmentManager(), "UsernameChange");
            }
        });

        profilePswChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PswChangeDialog pswChangeDialog = new PswChangeDialog();
                pswChangeDialog.show(getActivity().getSupportFragmentManager(), "PswChange");
            }
        });

        profileDataChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataChangeDialog dataChangeDialog = new DataChangeDialog();
                dataChangeDialog.show(getActivity().getSupportFragmentManager(), "DataChange");
            }
        });

        return view;
    }

    private void init(View view) {
        profileUserPicture = view.findViewById(R.id.profile_user_picture);
        profileUserFullname = view.findViewById(R.id.profile_user_fullname);
        profileUserEmail = view.findViewById(R.id.profile_user_email);
        profileUserUsername = view.findViewById(R.id.profile_user_username);
        profileUserLevel = view.findViewById(R.id.profile_user_level);
        profileUserBirthdate = view.findViewById(R.id.profile_user_birthdate);
        profileUserRegdate = view.findViewById(R.id.profile_user_regdate);
        profileUserChange = view.findViewById(R.id.profile_user_change);
        profileDataChange = view.findViewById(R.id.profile_data_change);
        profilePswChange = view.findViewById(R.id.profile_psw_change);
    }

    @Override
    public void sendData(Response response) {
        JSONObject res;
        JSONObject data = null;
        Object responseObject;
        SharedPreferences login = getContext().getSharedPreferences("TickOff", Context.MODE_PRIVATE);
        try {
            res = new JSONObject(response.getContent());
            responseObject = res.get("data");
            Log.d("profil", responseObject.toString());
            if (responseObject instanceof JSONObject){
                JSONObject profilData = new JSONObject(responseObject.toString());
                String fullName = profilData.getString("first_name") + " " + profilData.getString("last_name");
                profileUserFullname.setText(fullName);
                profileUserEmail.setText(profilData.getString("email"));
                profileUserUsername.setText(profilData.getString("username"));
                profileUserBirthdate.setText(UnixDateConverter.toDateString(profilData.getLong("born_date")));
                profileUserRegdate.setText(UnixDateConverter.toDateString(profilData.getLong("register_date")));
                login.edit().putString("email_or_username", profilData.getString("username")).apply();
            }
        }catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void getProfileData(){
        RequestTask refresh = new RequestTask(getContext(), "profile-data", "GET");
        refresh.execute();
    }
}