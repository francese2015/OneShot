package watchout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosimoalessandro.watchout.R;
import com.facebook.appevents.AppEventsLogger;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import configuration.Configuration;
import fragments.EditProfileFragment;
import fragments.HomeFragment;
import fragments.MainFragment;
import fragments.UserProfileFragment;
import managers.FragmentsManager;
import utility.RoundedImage;


public class FragmentsActivity extends FragmentActivity implements View.OnClickListener, SimpleAdapter.ViewBinder, AdapterView.OnItemClickListener {

    private TextView pageTitle;
    private ImageButton backButton, plusButton;

    private boolean isAuthFromFacebook=false;

    //Take User Image from FB
    private ImageView user_picture;
    private String id;
    private Bitmap bitmap = null;



    // menu views
    private EditText searchEdit;
    private ListView menuListView;
    private TextView nameUser;
    private ParseUser user = ParseUser.getCurrentUser();


    //  private RoundedImageView menuAvatar;
    private ImageButton writePostButton;
    private ImageButton suggestButton;
    private Button rightButton;

    private DrawerLayout drawerLayout;
    //  private User loggedUser;
    private RoundedImage profilePicture;

    private View headerView;
    // private NotificationsManager notificationsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_fragments);



        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        pageTitle = (TextView)this.findViewById(R.id.page_title);
        pageTitle.setTypeface(Configuration.getDefaultMediumTypeface(this));

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        backButton = (ImageButton)this.findViewById(R.id.back_button);
        backButton.setOnClickListener(this);

        plusButton = (ImageButton)this.findViewById(R.id.plus_button);
        rightButton = (Button)this.findViewById(R.id.right_button);
//        rightButton.setTypeface(Configuration.getDefaultMediumTypeface(this));

        writePostButton = (ImageButton)this.findViewById(R.id.write_post_button);

        //  profilePicture = (RoundedImageView)findViewById(R.id.profile_avatar);
        profilePicture = (RoundedImage)findViewById(R.id.imageView_round);
        profilePicture.setOnClickListener(this);

        suggestButton = (ImageButton)this.findViewById(R.id.suggest_button);
        headerView = findViewById(R.id.header);

        nameUser = (TextView) findViewById(R.id.ilmioprofilo);

        if(user.getUsername().toString().contains("@"))
            isAuthFromFacebook=false;
        else isAuthFromFacebook=true;
        if(isAuthFromFacebook)
            nameUser.setText(user.getString("name"));
        else nameUser.setText(user.getString("name")+ " "+user.getString("cognome"));
        nameUser.setOnClickListener(this);
        // notificationsManager = new NotificationsManager(this);

        initMenu();

        MainFragment fragment = null;
        Activity activity = null;

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            // do stuff with the user

            fragment = new HomeFragment();
            setMenuLocked(false);

        } else {
            // show the signup or login screen


        }
        if(fragment != null)
            FragmentsManager.init(getSupportFragmentManager(), this, fragment);
    }



    public TextView getPageTitleView(){
        return this.pageTitle;
    }

    @Override

    public void onClick(View v) {

        if(v == profilePicture){
            Logger.getLogger("PP").info("vai al profilo");
            FragmentsManager.getInstance().loadFragment(new UserProfileFragment(), true);
            closeMenu();
        }

        if(v == nameUser){
            Logger.getLogger("PP").info("vai al profilo");
            FragmentsManager.getInstance().loadFragment(new UserProfileFragment(), true);
            closeMenu();
        }

        if(v == backButton){
            FragmentsManager.getInstance().goBack();
        }

    }
    public void setMenuLocked(boolean locked){
        if(locked){
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    public void setShowBack(boolean showBack){

        if(!showBack)
            backButton.setVisibility(View.INVISIBLE);
        else
            backButton.setVisibility(View.VISIBLE);
    }

    public ImageButton getBackButton(){
        return backButton;
    }

    private void initMenu(){

        menuListView = (ListView)findViewById(R.id.menu_listview);


        ArrayList<HashMap<String, Object>> maps = new ArrayList<HashMap<String,Object>>();

        for(int menuString : Configuration.menu){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("label", getString(menuString));

            maps.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, maps, R.layout.menu_item, new String[]{"label"}, new int[]{R.id.label});
        adapter.setViewBinder(this);
        menuListView.setAdapter(adapter);
        menuListView.setOnItemClickListener(this);

    }

    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation) {
        /*
        if(view instanceof TextView){
            ((TextView) view).setTypeface(Configuration.getDefaultRegularTypeface(this));
        }*/
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        int menuId = Configuration.menu[position];

        if(menuId == R.string.home) {
            // FragmentsManager.getInstance().loadFragment(new LiveFeedFragment(), false);
        }
        else if(menuId == R.string.menu_modifica){ // modifica informazioni
            if(isAuthFromFacebook)
                Toast.makeText(this, R.string.edit_profile_facebook, Toast.LENGTH_LONG).show();
            else  FragmentsManager.getInstance().loadFragment(new EditProfileFragment(), true);
        }
        else if(menuId == R.string.menu_faq) { // faq
            //  FragmentsManager.getInstance().loadFragment(new FAQFragment(), true);
        }
        else if(menuId == R.string.menu_termini_condizioni) { // termini e condizioni
            // FragmentsManager.getInstance().loadFragment(new TermsAndConditionFragment(), true);
        }
        else if(menuId == R.string.menu_segnala) { // segnala

        }
        else if(menuId == R.string.logout) {
            // logout

            ParseUser.logOut();

            // FLAG_ACTIVITY_CLEAR_TASK only works on API 11, so if the user
            // logs out on older devices, we'll just exit.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                Intent intent = new Intent(FragmentsActivity.this, DispatchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                finish();
            }
        }


        closeMenu();
    }

    public void openMenu(){
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    public void closeMenu(){
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    public ImageButton getPlusButton(){
        return plusButton;
    }

    public ImageButton getWritePostButton(){
        return writePostButton;
    }

    public Button getRightButton(){
        return rightButton;
    }

    public ImageButton getSuggestButton(){
        return this.suggestButton;
    }

    public void hideRightButtons(){
        plusButton.setVisibility(View.INVISIBLE);
        writePostButton.setVisibility(View.INVISIBLE);
        rightButton.setVisibility(View.GONE);
        suggestButton.setVisibility(View.INVISIBLE);
    }



    public void setHeaderVisible(boolean visible){
        if(visible){
            headerView.setVisibility(View.VISIBLE);
        }
        else {
            headerView.setVisibility(View.GONE);
        }
    }



    // Aggiunta Metodi Parse FB Login

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }


    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }


}