package com.example.tickoff;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends Fragment implements DataToFragments{

    private AppCompatImageView profileUserPicture;
    private AppCompatTextView profileUserFullname;
    private AppCompatTextView profileUserEmail;
    private AppCompatTextView profileUserUsername;
    private AppCompatTextView profileUserLevel;
    private AppCompatTextView profileUserBirthdate;
    private AppCompatTextView profileUserRegdate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        init(view);

        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setdata(this);
        }

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
    }

    @Override
    public void sendData(Response response) {
        JSONObject res;
        JSONObject data = null;
        try {
            res = new JSONObject(response.getContent());
            data = new JSONObject(res.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (data != null){
            try {
                //TODO: kép, szint, születési dátum!
                String fullName = data.getString("first_name") + " " + data.getString("last_name");
                profileUserFullname.setText(fullName);
                profileUserEmail.setText(data.getString("email"));
                profileUserUsername.setText(data.getString("username"));
                profileUserRegdate.setText(data.getString("register_date"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}