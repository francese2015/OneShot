/*
 *  Copyright (c) 2014, Parse, LLC. All rights reserved.
 *
 *  You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 *  copy, modify, and distribute this software in source code or binary form for use
 *  in connection with the web services and APIs provided by Parse.
 *
 *  As with any software that integrates with the Parse platform, your use of
 *  this software is subject to the Parse Terms of Service
 *  [https://www.parse.com/about/terms]. This copyright notice shall be
 *  included in all copies or substantial portions of the software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.parse.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Fragment for the user signup screen.
 */
public class ParseSignupFragment extends ParseLoginFragmentBase implements OnClickListener {
    public static final String USERNAME = "com.parse.ui.ParseSignupFragment.USERNAME";
    public static final String PASSWORD = "com.parse.ui.ParseSignupFragment.PASSWORD";

    //MY Elements
    private EditText cognomeField;
    private RadioButton maleRadioButton, femaleRadioButton;
    private EditText nascitaGiornoEditText, nascitaMeseEditText, nascitaAnnoEditText;


    //PARSE
    private EditText usernameField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private EditText emailField;
    private EditText nameField;
    private Button createAccountButton;
    private ParseOnLoginSuccessListener onLoginSuccessListener;

    private ParseLoginConfig config;
    private int minPasswordLength;

    private static final String LOG_TAG = "ParseSignupFragment";
    private static final int DEFAULT_MIN_PASSWORD_LENGTH = 6;
    private static final String USER_OBJECT_NAME_FIELD = "name";

    public static ParseSignupFragment newInstance(Bundle configOptions, String username, String password) {
        ParseSignupFragment signupFragment = new ParseSignupFragment();
        Bundle args = new Bundle(configOptions);
        args.putString(ParseSignupFragment.USERNAME, username);
        args.putString(ParseSignupFragment.PASSWORD, password);
        signupFragment.setArguments(args);
        return signupFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        config = ParseLoginConfig.fromBundle(args, getActivity());

        minPasswordLength = DEFAULT_MIN_PASSWORD_LENGTH;
        if (config.getParseSignupMinPasswordLength() != null) {
            minPasswordLength = config.getParseSignupMinPasswordLength();
        }


        String username = (String) args.getString(USERNAME);
        String password = (String) args.getString(PASSWORD);

        View v = inflater.inflate(R.layout.com_parse_ui_parse_signup_fragment,
                parent, false);

        ImageView appLogo = (ImageView) v.findViewById(R.id.app_logo);



        nameField = (EditText) v.findViewById(R.id.signup_name_input);
        cognomeField = (EditText) v.findViewById(R.id.cognome);
        usernameField = (EditText) v.findViewById(R.id.signup_username_input);

        maleRadioButton = (RadioButton) v.findViewById(R.id.radio_male);
        femaleRadioButton = (RadioButton) v.findViewById(R.id.radio_female);

        nascitaGiornoEditText = (EditText) v.findViewById(R.id.data_nascita_giorno);
        nascitaMeseEditText = (EditText) v.findViewById(R.id.data_nascita_mese);
        nascitaAnnoEditText = (EditText) v.findViewById(R.id.data_nascita_anno);

        passwordField = (EditText) v.findViewById(R.id.signup_password_input);
        confirmPasswordField = (EditText) v.findViewById(R.id.signup_confirm_password_input);
        emailField = (EditText) v.findViewById(R.id.signup_email_input);
        createAccountButton = (Button) v.findViewById(R.id.create_account);




        usernameField.setText(username);
        passwordField.setText(password);

        if (appLogo != null && config.getAppLogo() != null) {
            appLogo.setImageResource(config.getAppLogo());
        }

        if (config.isParseLoginEmailAsUsername()) {
            usernameField.setHint(R.string.com_parse_ui_email_input_hint);
            usernameField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            if (emailField != null) {
                emailField.setVisibility(View.GONE);
            }
        }

        if (config.getParseSignupSubmitButtonText() != null) {
            createAccountButton.setText(config.getParseSignupSubmitButtonText());
        }
        createAccountButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ParseOnLoginSuccessListener) {
            onLoginSuccessListener = (ParseOnLoginSuccessListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseOnLoginSuccessListener");
        }

        if (activity instanceof ParseOnLoadingListener) {
            onLoadingListener = (ParseOnLoadingListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseOnLoadingListener");
        }
    }



    //Verify Date of Birth
    public static boolean isDateValid(String dateToValidate){
        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dateToValidate);
        } catch (java.text.ParseException e) {
            return false;
        }


        try {
            String[] divided = dateToValidate.split("-");
            int giorno = Integer.parseInt(divided[2]);
            int mese = Integer.parseInt(divided[1]);
            int anno = Integer.parseInt(divided[0]);

            Calendar c = new GregorianCalendar();
            c.setLenient(false);
            c.set(anno, mese - 1, giorno);
            c.getTime();

        }
        catch(Exception e){
            return false;
        }


        return true;
    }


    @Override
    public void onClick(View v) {

        String cognome = null;
        if(cognomeField != null){
            cognome = cognomeField.getText().toString();
        }


        String username = usernameField.getText().toString();

        String sesso = maleRadioButton.isChecked()? "M" : "F";
        String anno = nascitaAnnoEditText.getText().toString();
        String mese = nascitaMeseEditText.getText().toString();
        String giorno = nascitaGiornoEditText.getText().toString();

        String password = passwordField.getText().toString();
        String passwordAgain = confirmPasswordField.getText().toString();


        String birthDate = anno + "-" + mese + "-" + giorno;


        if (giorno.length() != 0 || mese.length() != 0 || anno.length() !=0) {
            if(!isDateValid(birthDate)){
                showToast(R.string.com_parse_ui_no_errore_date_of_birth);
                return;
            }

        }

        String email = null;
        if (config.isParseLoginEmailAsUsername()) {
            email = usernameField.getText().toString();
        } else if (emailField != null) {
            email = emailField.getText().toString();
        }

        String name = null;
        if (nameField != null) {
            name = nameField.getText().toString();
        }

        if (username.length() == 0) {
            if (config.isParseLoginEmailAsUsername()) {
                showToast(R.string.com_parse_ui_no_email_toast);
            } else {
                showToast(R.string.com_parse_ui_no_username_toast);
            }
        } else if (cognome.length() == 0){
            showToast(R.string.com_parse_ui_no_cognome);
        } else if (giorno.length() == 0 || mese.length() == 0 || anno.length() ==0){
            showToast(R.string.com_parse_ui_no_date_of_birth);
        } else if (password.length() == 0) {
            showToast(R.string.com_parse_ui_no_password_toast);
        } else if (password.length() < minPasswordLength) {
            showToast(getResources().getQuantityString(
                    R.plurals.com_parse_ui_password_too_short_toast,
                    minPasswordLength, minPasswordLength));
        } else if (passwordAgain.length() == 0) {
            showToast(R.string.com_parse_ui_reenter_password_toast);
        } else if (!password.equals(passwordAgain)) {
            showToast(R.string.com_parse_ui_mismatch_confirm_password_toast);
            confirmPasswordField.selectAll();
            confirmPasswordField.requestFocus();
        } else if (email != null && email.length() == 0) {
            showToast(R.string.com_parse_ui_no_email_toast);
        } else if (name != null && name.length() == 0) {
            showToast(R.string.com_parse_ui_no_name_toast);
        } else {
            ParseUser user = new ParseUser();

            // Set standard fields
            user.setUsername(username);
            user.put("cognome", cognome);
            user.setEmail(email);
            user.put("sesso", sesso);
            user.put("anno", anno);
            user.put("mese", mese);
            user.put("giorno", giorno);
            user.setPassword(password);


            // Set additional custom fields only if the user filled it out
            if (name.length() != 0) {
                user.put(USER_OBJECT_NAME_FIELD, name);
            }

            loadingStart();
            user.signUpInBackground(new SignUpCallback() {

                @Override
                public void done(ParseException e) {
                    if (isActivityDestroyed()) {
                        return;
                    }

                    if (e == null) {
                        loadingFinish();
                        signupSuccess();
                    } else {
                        loadingFinish();
                        if (e != null) {
                            debugLog(getString(R.string.com_parse_ui_login_warning_parse_signup_failed) +
                                    e.toString());
                            switch (e.getCode()) {
                                case ParseException.INVALID_EMAIL_ADDRESS:
                                    showToast(R.string.com_parse_ui_invalid_email_toast);
                                    break;
                                case ParseException.USERNAME_TAKEN:
                                    showToast(R.string.com_parse_ui_username_taken_toast);
                                    break;
                                case ParseException.EMAIL_TAKEN:
                                    showToast(R.string.com_parse_ui_email_taken_toast);
                                    break;
                                default:
                                    showToast(R.string.com_parse_ui_signup_failed_unknown_toast);
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    protected String getLogTag() {
        return LOG_TAG;
    }

    private void signupSuccess() {

        ParseUser parseUser = ParseUser.getCurrentUser();

        ParseInstallation currentInstall = ParseInstallation.getCurrentInstallation();
        currentInstall.put("userId", parseUser.getObjectId());
        currentInstall.saveInBackground();

        onLoginSuccessListener.onLoginSuccess();
    }
}
