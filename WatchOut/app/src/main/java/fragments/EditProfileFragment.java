package fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cosimoalessandro.watchout.R;
import com.parse.GetCallback;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseSession;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import configuration.Configuration;
import managers.FragmentsManager;

/**
 * Created by CosimoAlessandro on 03/05/2015.
 */
public class EditProfileFragment extends MainFragment implements View.OnClickListener {
    private EditText nameEditText, surnameEditText, nascitaGiornoEditText, nascitaMeseEditText, nascitaAnnoEditText, descrizioneEditText,
            passwordEditText, confirmPasswordEditText;
    private RadioButton maleRadioButton, femaleRadioButton;
    private Button registratiButton, alreadyAccountButton, termsButton, privacyButton;
    private ProgressDialog loading;
    ParseUser user;
    private boolean isAuthFromFacebook;

    /* valori temporanei per l'init dei dati*/
    private String tNome, tCognome, tSesso, tGiorno, tMese, tAnno, tPass, tPassok;

    // validate date
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


    /*
        private RoundedImageView profilePicture;
        private FKDevicePhotoLoader devicePhotoLoader;*/
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        user = ParseUser.getCurrentUser();
        if(user.getUsername().toString().contains("@"))
            isAuthFromFacebook=false;
        else isAuthFromFacebook=true;


        View view = inflater.inflate(R.layout.fragment_edit_profile, null, true);
        TextView labelSesso = (TextView)view.findViewById(R.id.label_sesso);
        labelSesso.setTypeface(Configuration.getDefaultRegularTypeface(getFragmentActivity()));
        TextView labelDataNascita = (TextView)view.findViewById(R.id.label_data_nascita);
        labelDataNascita.setTypeface(Configuration.getDefaultRegularTypeface(getFragmentActivity()));
 /*       profilePicture = (RoundedImageView)view.findViewById(R.id.profile_picture);
        profilePicture.setOnClickListener(this);*/
        nameEditText = (EditText)view.findViewById(R.id.name);
        nameEditText.setTypeface(Configuration.getDefaultRegularTypeface(getFragmentActivity()));
        surnameEditText = (EditText)view.findViewById(R.id.surname);
        surnameEditText.setTypeface(Configuration.getDefaultRegularTypeface(getFragmentActivity()));
        nascitaGiornoEditText = (EditText)view.findViewById(R.id.data_nascita_giorno);
        nascitaGiornoEditText.setTypeface(Configuration.getDefaultRegularTypeface(getFragmentActivity()));
        nascitaMeseEditText = (EditText)view.findViewById(R.id.data_nascita_mese);
        nascitaMeseEditText.setTypeface(Configuration.getDefaultRegularTypeface(getFragmentActivity()));
        nascitaAnnoEditText = (EditText)view.findViewById(R.id.data_nascita_anno);
        nascitaAnnoEditText.setTypeface(Configuration.getDefaultRegularTypeface(getFragmentActivity()));
        // nascitaAnnoEditText.setOnEditorActionListener(this);
        passwordEditText = (EditText)view.findViewById(R.id.password);
        passwordEditText.setTypeface(Configuration.getDefaultRegularTypeface(getFragmentActivity()));
        confirmPasswordEditText = (EditText)view.findViewById(R.id.confirm_password);
        confirmPasswordEditText.setTypeface(Configuration.getDefaultRegularTypeface(getFragmentActivity()));
        maleRadioButton = (RadioButton)view.findViewById(R.id.radio_male);
        maleRadioButton.setTypeface(Configuration.getDefaultRegularTypeface(getFragmentActivity()));
        femaleRadioButton = (RadioButton)view.findViewById(R.id.radio_female);
        femaleRadioButton.setTypeface(Configuration.getDefaultRegularTypeface(getFragmentActivity()));
        registratiButton = (Button)view.findViewById(R.id.button_registrati);
        registratiButton.setTypeface(Configuration.getDefaultRegularTypeface(getFragmentActivity()));
        registratiButton.setOnClickListener(this);


        if(isAuthFromFacebook) {
            registratiButton.setVisibility(View.INVISIBLE);
            maleRadioButton.setVisibility(View.INVISIBLE);
            femaleRadioButton.setVisibility(View.INVISIBLE);
            confirmPasswordEditText.setVisibility(View.INVISIBLE);

            passwordEditText.setVisibility(View.INVISIBLE);
            nascitaGiornoEditText.setVisibility(View.INVISIBLE);
            nascitaMeseEditText.setVisibility(View.INVISIBLE);
            nascitaAnnoEditText.setVisibility(View.INVISIBLE);
            labelDataNascita.setVisibility(View.INVISIBLE);
            labelSesso.setText(R.string.edit_profile_facebook);

        }
        initData();
        return view;
    }
    private void initData(){


        if(!isAuthFromFacebook){

            tNome=user.get("name").toString().trim();
            tCognome=user.get("cognome").toString().trim();
            tAnno=user.get("anno").toString().trim();
            tGiorno=user.get("giorno").toString().trim();
            tMese=user.get("mese").toString().trim();
            tSesso=user.get("sesso").toString();
            nameEditText.setText(tNome);
            surnameEditText.setText(tCognome);
            nascitaGiornoEditText.setText(tGiorno);
            nascitaMeseEditText.setText(tMese);
            nascitaAnnoEditText.setText(tAnno);

            if(tSesso.equals("M")){
                maleRadioButton.toggle();
            }else{
                femaleRadioButton.toggle();
            }

        }
        else
        {
            tNome=user.get("name").toString().trim();
            String[] splitta = tNome.split(" ");
            tNome=splitta[0];
            for(int i=1; i < splitta.length-1;i++){
                tNome=tNome+" "+splitta[i];
            }
            tCognome=splitta[splitta.length-1];

            nameEditText.setText(tNome);
            surnameEditText.setText(tCognome);
        }




/*        if(loggedUser.picture != null){
            ImageLoader.getInstance().displayImage(loggedUser.picture, profilePicture);
        }*/
/*        nameEditText.setText(loggedUser.nome);
        surnameEditText.setText(loggedUser.cognome);
        if(loggedUser.gender.equalsIgnoreCase("F")){
            maleRadioButton.setChecked(false);
            femaleRadioButton.setChecked(true);
        }
        else {
            maleRadioButton.setChecked(true);
            femaleRadioButton.setChecked(false);
        }
        String[] splittedDate = loggedUser.data_nascita.split("-");
        nascitaGiornoEditText.setText(splittedDate[2]);
        nascitaMeseEditText.setText(splittedDate[1]);
        nascitaAnnoEditText.setText(splittedDate[0]);
        descrizioneEditText.setText(loggedUser.description);*/
    }
    @Override
    public void onStart() {
        super.onStart();
        if(FragmentsManager.getInstance().getActualFragment() != this)
            return;
        this.setPageTitle(getString(R.string.edit_profile));
    }
    @Override
    public void onClick(View v) {
        if(v == registratiButton){ // go feed
            sendRegistration();
        }
/*        else if(v == profilePicture){
//            selectProfilePicture();
        }*/
    }
    /*    private void selectProfilePicture(){
            AlertDialog.Builder builder = new AlertDialog.Builder(getFragmentActivity());
            builder.setTitle(R.string.profile_picture);
            builder.setItems(new String[]{ getString(R.string.profile_picture_shot),  getString(R.string.profile_picture_gallery) }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == 0){
                        pictureFromShot();
                    }
                    else {
                        pictureFromGallery();
                    }
                }
            });
            builder.create().show();
        }*/
/*    private void pictureFromShot(){
        devicePhotoLoader = new FKDevicePhotoLoader(getFragmentActivity(), this);
        devicePhotoLoader.startTakePhoto(profilePicture.getWidth(), profilePicture.getHeight());
    }*/
/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        devicePhotoLoader.onActivityResult(requestCode, resultCode, data);
    }
    private void pictureFromGallery(){
        devicePhotoLoader = new FKDevicePhotoLoader(getFragmentActivity(), this);
        devicePhotoLoader.startPhotoFromGallery(profilePicture.getWidth(), profilePicture.getHeight());
    }*/


    private void sendRegistration(){
        String nome = nameEditText.getText().toString().trim();
        String cognome = surnameEditText.getText().toString().trim();
        String sesso = maleRadioButton.isChecked()? "M" : "F";
        String anno = nascitaAnnoEditText.getText().toString().trim();
        String mese = nascitaMeseEditText.getText().toString().trim();
        String giorno = nascitaGiornoEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String ripetiPassword = confirmPasswordEditText.getText().toString().trim();
        // check
        if(nome.length() == 0 || cognome.length() == 0 || sesso.length() == 0 || anno.length() == 0 || mese.length() == 0 ||
                giorno.length() == 0 || (password.length() > 0 && ripetiPassword.length() == 0)){
            Toast.makeText(getFragmentActivity(), R.string.all_fields_required, Toast.LENGTH_LONG).show();
            return;
        }

        String birthDate = anno + "-" + mese + "-" + giorno;

        if(!isDateValid(birthDate)){
            Toast.makeText(getFragmentActivity(), R.string.invalid_birth_date, Toast.LENGTH_LONG).show();
            return;
        }



        /*if(!CommonUtils.isDateValid(birthDate)){
            Toast.makeText(getFragmentActivity(), R.string.invalid_birth_date, Toast.LENGTH_LONG).show();
            return;
        }*/

        if(password.length() > 0 && !password.equals(ripetiPassword)){
            Toast.makeText(getFragmentActivity(), R.string.password_not_same, Toast.LENGTH_LONG).show();
            return;
        }


        // loading = ProgressDialog.show(getFragmentActivity(), null, getString(R.string.loading));

        user.put("name",nome);
        user.put("cognome",cognome);
        user.put("sesso",sesso);
        user.put("anno",anno);
        user.put("giorno",giorno);
        user.put("mese",mese);

        if(password.length() > 5)
            user.put("password",password);
        else if (password.length()>0 && password.length() < 6)
            Toast.makeText(getFragmentActivity(), R.string.invalid_password,Toast.LENGTH_LONG).show();

        user.saveInBackground();
        Toast.makeText(getFragmentActivity(), R.string.edit_profile_success,Toast.LENGTH_LONG).show();

        // call ws
/*        WSManager wsManager = new WSManager();
        wsManager.setListener(this);
        wsManager.editProfile(loggedUser.id, password, nome, cognome, sesso, birthDate, selectedQuartiere, descrizione);*/
    }

/*    @Override
    public void onPhotoLoaded(FKDevicePhotoLoader loader, Bitmap bitmap) {
        profilePicture.setImageBitmap(bitmap);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        loading = ProgressDialog.show(getFragmentActivity(), null, getString(R.string.loading));
        // send bitmap to server
        WSManager wsManager = new WSManager();
        wsManager.setListener(this);
        wsManager.saveProfilePicture(loggedUser.id, byteArray);
    }*/
/*    @Override
    public void onPhotoLoadError(FKDevicePhotoLoader loader) {
        Toast.makeText(getFragmentActivity(), R.string.generic_error, Toast.LENGTH_LONG).show();
    }*/
}