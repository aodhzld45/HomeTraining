package projectfinal.code.hometraining.Exercise_Settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import projectfinal.code.hometraining.ExerciseFirst.Login.Login;
import projectfinal.code.hometraining.Exercise_Intent.LoginDataGS;
import projectfinal.code.hometraining.Exercise_Settings.Bluetooth.BluetoothMain;
import projectfinal.code.hometraining.Exercise_Settings.CheckUserRequest.CheckUserRequest;
import projectfinal.code.hometraining.Exercise_Settings.DeleteUserRequest.DeleteUserRequest;
import projectfinal.code.hometraining.Exercise_Settings.KakaoMaps.KakaoMaps;
import projectfinal.code.hometraining.Exercise_Settings.Maps.googleMapsGyms;
import projectfinal.code.hometraining.Exercise_Settings.setting.Setting;
import projectfinal.code.hometraining.Exercise_Settings.userchange.UserChange;
import projectfinal.code.hometraining.R;
import projectfinal.code.hometraining.handler.BackPressCloseHandler;

public class Exercise_Settings extends Fragment implements View.OnClickListener {
    private Button BTN_settings_userchange, BTN_settings_setting, BTN_settings_logout, BTN_settings_exit, BTN_settings_maps
            ,BTN_settings_bluetooth;
    private TextView TV_settings_name, TV_settings_address, TV_settings_phone;
    private View bottomview;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Setting Setting;
    private BackPressCloseHandler settingclose;
    private String response_delete_id, response_delete_pw;
    //LoginDataGS??? getter,setter??? ???????????? ??? ??????????????? ??? ????????? ?????? ??????
    //???????????? final??? ???????????? ???????????? ???????????? ??????
    //????????? final??? ????????? ?????? ????????? ???????????? ???????????? ?????? ??? ?????? ?????? ??????
    // ??? ?????? ???????????? ??? ????????? ??????/ ???????????? ???????????? ???
    //final LoginDataGS loginDataGS = new LoginDataGS();

    //LoginDataGS??? ???????????? instance?????? ???
    String loginIdData = LoginDataGS.getInstance().getLogin_ID();
    String loginPwData = LoginDataGS.getInstance().getLogin_PW();
    //life cycle??? ?????? ????????????????????? ?????? ????????? ????????? ?????? ??????????????? ?????? ????????????


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bottomview = inflater.inflate(R.layout.exercise_settings,container,false);
        return bottomview;
    }

    //life cycle??? ?????? onCreateView return??? ??? onViewCreate?????? ????????? ??? ????????? ??????
    // ??? oncreateview?????? view???(?????????) ????????? ??? onviewcreate?????? ??????????????? ?????? ??????.

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BTN_settings_userchange = (Button)view.findViewById(R.id.BTN_settings_userchange);
        BTN_settings_setting = (Button)view.findViewById(R.id.BTN_settings_setting);
        BTN_settings_logout = (Button)view.findViewById(R.id.BTN_settings_logout);
        BTN_settings_exit = (Button)view.findViewById(R.id.BTN_settings_exit);
        BTN_settings_maps = (Button)view.findViewById(R.id.BTN_settings_maps);
        BTN_settings_bluetooth = (Button)view.findViewById(R.id.BTN_settings_bluetooth);

        BTN_settings_maps.setOnClickListener(this);
        BTN_settings_userchange.setOnClickListener(this);
        BTN_settings_setting.setOnClickListener(this);
        BTN_settings_logout.setOnClickListener(this);
        BTN_settings_exit.setOnClickListener(this);
        BTN_settings_bluetooth.setOnClickListener(this);

        TV_settings_name= view.findViewById(R.id.TV_settings_name);



        if ((loginIdData!=null)){
            TV_settings_name.setText(loginIdData+"2???");
        }else{
            TV_settings_name.setText("2???");
        }


        if ((loginIdData!=null)&&(loginPwData!=null)){
        }else{
            BTN_settings_exit.setText("????????? ???????????? ????????????");
        }

        TV_settings_phone = view.findViewById(R.id.TV_settings_phone);
        if (loginIdData!=null){
            TV_settings_name.setText("??????: "+loginIdData);
        }else{
            TV_settings_name.setText("?????????");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //???????????? ?????? ?????????
            case R.id.BTN_settings_userchange:

                if ((loginIdData != null) && (loginPwData != null)){
                     AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                     LayoutInflater factory = LayoutInflater.from(getActivity());
                     View view = factory.inflate(R.layout.alert_userchange, null);

                    final EditText ET_userchange_ID = view.findViewById(R.id.ET_alert_userchange_ID);
                    final EditText ET_userchange_PW = view.findViewById(R.id.ET_alert_userchange_PW);


                         dialog.setView(view)
                        .setNeutralButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String U_id = ET_userchange_ID.getText().toString();
                        String U_pw = ET_userchange_PW.getText().toString();

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");

                                    if (success) {
                                        String U_id = jsonObject.getString("U_id");
                                        String U_pw = jsonObject.getString("U_pw");

                                        Intent pwchange = new Intent(getActivity(), UserChange.class);
                                        pwchange.putExtra("U_id", U_id);
                                        pwchange.putExtra("U_pw", U_pw);
                                        startActivity(pwchange);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        CheckUserRequest checkUserRequest = new CheckUserRequest(U_id, U_pw, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(getActivity());
                        queue.add(checkUserRequest);
                    }
                }).create().show();
        }else{
                    Toast.makeText(getActivity(),"????????? ??? ??????????????? ???????????????.",Toast.LENGTH_SHORT).show();
                }
               break;

                //???????????? ?????? ?????????
            case R.id.BTN_settings_setting:
                getActivity();
                fm= getFragmentManager();
                ft=fm.beginTransaction();
                ft.replace(R.id.Main_Frame, new Setting());
                ft.addToBackStack(null);
                ft.commit();
                break;

                //???????????? ?????? ?????????
            case R.id.BTN_settings_bluetooth:
                Intent bluetoothIntent = new Intent(getActivity(),BluetoothMain.class);
                startActivity(bluetoothIntent);

            //???????????? ?????? ?????????
            case R.id.BTN_settings_logout:
                if ((loginIdData!=null) &&(loginPwData!=null)) {
                    Intent logout = new Intent(getActivity(), Login.class);
                    logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logout);
                    //?????? ???????????? ??????
                    getActivity().finish();
                    /*settingclose.*/
                }else{
                    Toast.makeText(getActivity(),"????????? ??? ??????????????? ???????????????.",Toast.LENGTH_SHORT).show();
                }
                break;



            //?????? ????????? ?????? ?????????
            case R.id.BTN_settings_maps:
                if ((loginIdData!=null)&&(loginPwData!=null)) {
                    Intent maps = new Intent(getActivity(), KakaoMaps.class);
                    //Intent maps = new Intent(getActivity(), googleMapsGyms.class);
                    startActivity(maps);
                }else{
                    Toast.makeText(getActivity(),"????????? ??? ??????????????? ???????????????.",Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.BTN_settings_exit:

                if ((loginIdData!=null)&&(loginPwData!=null)){

                    AlertDialog.Builder deletedialog = new AlertDialog.Builder(getActivity());
                    LayoutInflater factory = LayoutInflater.from(getActivity());
                    View viewdelete = factory.inflate(R.layout.alert_userchange,null);

                    final EditText ET_userdelete_ID = viewdelete.findViewById(R.id.ET_alert_userchange_ID);
                    final EditText ET_userdelete_PW = viewdelete.findViewById(R.id.ET_alert_userchange_PW);

                    deletedialog.setView(viewdelete)
                            .setNeutralButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String delete_id = ET_userdelete_ID.getText().toString();
                            String delete_pw = ET_userdelete_PW.getText().toString();

                            Response.Listener<String> delete_responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject deletejsonObject = new JSONObject(response);
                                        boolean success = deletejsonObject.getBoolean("success");

                                        if (success){
                                            response_delete_id = deletejsonObject.getString("U_id");
                                            response_delete_pw = deletejsonObject.getString("U_pw");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            };

                            CheckUserRequest checkUserRequest = new CheckUserRequest(delete_id,delete_pw,delete_responseListener);
                            DeleteUserRequest deleteUserRequest = new DeleteUserRequest(delete_id,delete_responseListener);
                            RequestQueue queue = Volley.newRequestQueue(getActivity());
                            queue.add(checkUserRequest);
                            // ????????? ?????? ?????? id??? pw??? ?????? ??? ???????????? ?????? php???????????? id,pw?????? ?????? ?????? ???????????? ?????? ??????
                            if ((loginIdData.equals(response_delete_id)) && (loginPwData.equals(response_delete_pw))) {
                                queue.add(deleteUserRequest);
                                Intent deleteuser = new Intent(getActivity(),Login.class);
                                deleteuser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                deleteuser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(deleteuser);
                                Toast.makeText(getActivity(),"??????????????? ?????????????????????.",Toast.LENGTH_SHORT).show();
                            }else{
                               Toast.makeText(getActivity(),"???????????? ?????? ???????????? ???????????????",Toast.LENGTH_SHORT).show();
                           }
                        }
                    }).create().show();
                }else{
                    Intent deleteuser = new Intent(getActivity(),Login.class);
                    deleteuser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    deleteuser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(deleteuser);
                }
                break;
        }
    }
}
