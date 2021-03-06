package com.cse6324.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cse6324.adapter.NoteListAdapter;
import com.cse6324.bean.NoteBean;
import com.cse6324.bean.UserBean;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.phms.ConfirmLockActivity;
import com.cse6324.phms.ContactActivity;
import com.cse6324.phms.EditVitalSignsActivity;
import com.cse6324.phms.LoginActivity;
import com.cse6324.phms.MainActivity;
import com.cse6324.phms.NoticeActivity;
import com.cse6324.phms.R;
import com.cse6324.phms.SettingActivity;
import com.cse6324.service.MyApplication;
import com.cse6324.util.UserUtil;
import com.wooplr.spotlight.SpotlightView;

import org.w3c.dom.Text;

import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.toolsfinal.StringUtils;
import okhttp3.Headers;
import okhttp3.Response;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Jarvis on 2017/2/11.
 */

public class ProfileFragment extends Fragment implements View.OnClickListener {

    ImageView ivNotification,ivEdit;
    TextView tvName, tvEmail, tvAge, tvGender, tvHeight, tvWeight, tvBp, tvBsl, tvChol;

    RelativeLayout rlEditVitalSigns;

    LinearLayout layoutContact, layoutSetting, layoutNotice;

    String age, gender, height, weight, bp, bsl, chol;

    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {

            if (response == null || response.length() == 0) {
                Toast.makeText(getContext(), "Connect fail", Toast.LENGTH_SHORT).show();
            } else if (!response.equals("0")) {
                ivNotification.setVisibility(View.VISIBLE);
            } else
                ivNotification.setVisibility(View.GONE);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, null);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");

        tvName = (TextView) v.findViewById(R.id.tv_name);
        tvEmail = (TextView) v.findViewById(R.id.tv_email);
        tvAge = (TextView) v.findViewById(R.id.tv_age);
        tvGender = (TextView) v.findViewById(R.id.tv_gender);
        tvHeight = (TextView) v.findViewById(R.id.tv_height);
        tvWeight = (TextView) v.findViewById(R.id.tv_weight);
        tvBp = (TextView) v.findViewById(R.id.tv_bp);
        tvBsl = (TextView) v.findViewById(R.id.tv_bsl);
        tvChol = (TextView) v.findViewById(R.id.tv_chol);

        ivEdit = (ImageView) v.findViewById(R.id.iv_edit);

        ivNotification = (ImageView) v.findViewById(R.id.iv_notification);

        setInfo();

        rlEditVitalSigns = (RelativeLayout) v.findViewById(R.id.editVitalSigns);
        rlEditVitalSigns.setOnClickListener(this);

        layoutContact = (LinearLayout) v.findViewById(R.id.layout_contact);
        layoutContact.setOnClickListener(this);

        layoutSetting = (LinearLayout) v.findViewById(R.id.layout_setting);
        layoutSetting.setOnClickListener(this);

        layoutNotice = (LinearLayout) v.findViewById(R.id.layout_notice);
        layoutNotice.setOnClickListener(this);

        initShowCase();

        return v;
    }

    public void setInfo() {
        tvName.setText(MyApplication.getPreferences(UserUtil.UNAME));
        tvEmail.setText(MyApplication.getPreferences(UserUtil.EMAIL));

        age = MyApplication.getPreferences(UserUtil.AGE);
        gender = MyApplication.getPreferences(UserUtil.GENDER);
        height = MyApplication.getPreferences(UserUtil.HEIGHT);
        weight = MyApplication.getPreferences(UserUtil.WEIGHT);
        bp = MyApplication.getPreferences(UserUtil.BP);
        bsl = MyApplication.getPreferences(UserUtil.BSL);
        chol = MyApplication.getPreferences(UserUtil.CHOL);

        tvAge.setText(isEmpty(age));
        tvHeight.setText(isEmpty(height));
        tvWeight.setText(isEmpty(weight));
        tvBp.setText(isEmpty(bp));
        tvBsl.setText(isEmpty(bsl));
        tvChol.setText(isEmpty(chol));

        if (gender.isEmpty()) {
            gender = "N/A";
        } else if (gender.equals("1"))
            gender = "Male";
        else
            gender = "Female";

        tvGender.setText(gender);

        new HttpUtil(HttpUtil.NORMAL_PARAMS)
                .add("uid", MyApplication.getPreferences(UserUtil.UID))
                .add("token", MyApplication.getPreferences(UserUtil.TOKEN))
                .get(Constant.URL_GETNEWNOTICE, callback);
    }

    @Override
    public void onClick(View view) {

        Intent intent;
        switch (view.getId()) {
            case R.id.editVitalSigns:
                if(!StringUtils.isEmpty(MyApplication.getPreferences(UserUtil.LOCK))){
                    intent = new Intent(getActivity(), ConfirmLockActivity.class);
                    startActivity(intent);
                }else {
                    intent = new Intent(getActivity(), EditVitalSignsActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.layout_contact:
                intent = new Intent(getActivity(), ContactActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.layout_setting:
                intent = new Intent(getActivity(), SettingActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.layout_notice:
                intent = new Intent(getActivity(), NoticeActivity.class);
                getContext().startActivity(intent);
                break;
        }
    }

    public void initShowCase(){
        new SpotlightView.Builder(getActivity())
                .introAnimationDuration(400)
                .performClick(true)
                .fadeinTextDuration(400)
                .headingTvColor(Color.parseColor("#ffffff"))
                .headingTvSize(32)
                .headingTvText("Vital Signs")
                .subHeadingTvColor(Color.parseColor("#ffffff"))
                .subHeadingTvSize(16)
                .subHeadingTvText("Save or edit your own vital signs")
                .maskColor(Color.parseColor("#dc000000"))
                .target(ivEdit)
                .lineAnimDuration(400)
                .lineAndArcColor(Color.parseColor("#ffffff"))
                .dismissOnTouch(true)
                .dismissOnBackPress(true)
                .enableDismissAfterShown(true)
                .usageId("vitalsigns") //UNIQUE ID
                .enableRevealAnimation(true)
                .show();
    }

    public String isEmpty(String str) {
        if (str.isEmpty())
            return "N/A";
        else
            return str;
    }

    @Override
    public void onResume() {
        super.onResume();
        setInfo();
    }
}
