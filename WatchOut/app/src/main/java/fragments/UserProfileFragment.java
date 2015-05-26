package fragments;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.UserManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosimoalessandro.watchout.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.Parse;
import com.parse.ParseUser;

import configuration.Configuration;
import fragments.MainFragment;
import managers.FragmentsManager;
import utility.RoundedImage;


public class UserProfileFragment extends MainFragment implements OnScrollListener,  OnRefreshListener {

    private ListView feedListView;
    private ProgressBar loading;
    private ParseUser loggedUser;

    private int id_post=0;

    private ParseUser utente;

    private ArrayList<HashMap<String, Object>> maps;
    private Button followButton;
    private LinearLayout quartiereContainer;

    private ProgressDialog following;
    private ProgressDialog saving;
    private ProgressDialog loader;
    private TextView userStatus;
    private LinearLayout writePost, photoPost;

    private EditText changeStatusEditText;
    private boolean flag_loading = false;

    private SwipeRefreshLayout swipeLayout;
    private View headerView;

    private LinearLayout followingsButton, followersButton;

    private ImageButton changeCoverButton;
    private ImageView userCover;
    private String nome,cognome;
    private boolean isForCover = false, isAuthFromFacebook=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile,null, true);

        utente=ParseUser.getCurrentUser();
        if(utente.getUsername().toString().contains("@"))
            isAuthFromFacebook=false;
        else isAuthFromFacebook=true;
        if (isAuthFromFacebook){
            nome=utente.get("name").toString().trim();
        }else {
            nome = utente.get("name").toString().trim();
            cognome = utente.get("cognome").toString().trim();
        }


        feedListView = (ListView)view.findViewById(R.id.feed_listview);
        feedListView.setOnScrollListener(this);


        swipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);

        loading = (ProgressBar)view.findViewById(R.id.loading_wall);

        this.initTabBar(view);


        return view;
    }

    private void addHeaderView(){
        View row = null;
        if(headerView == null){
            LayoutInflater inflater = getFragmentActivity().getLayoutInflater();
            row = inflater.inflate(R.layout.user_profile_header, null);
            headerView = row;
        }
        else {
            row = headerView;
        }

        followButton = (Button)row.findViewById(R.id.follow_button);
        ParseUser u = utente;

        writePost = (LinearLayout)row.findViewById(R.id.scrivibutton);
        writePost.setOnClickListener(this);

        photoPost = (LinearLayout)row.findViewById(R.id.fotobutton);
        photoPost.setOnClickListener(this);

        followersButton = (LinearLayout)row.findViewById(R.id.followers_button);
        followersButton.setOnClickListener(this);

        followingsButton = (LinearLayout)row.findViewById(R.id.followings_button);
        followingsButton.setOnClickListener(this);

        userCover = (ImageView)row.findViewById(R.id.user_cover);

        //  if(u.cover != null)
        //      ImageLoader.getInstance().displayImage(u.cover, userCover);

        userCover.setOnClickListener(this);

        changeCoverButton = (ImageButton)row.findViewById(R.id.change_cover_button);

    /*    if(u.id == loggedUser.id){
            changeCoverButton.setOnClickListener(this);
            changeCoverButton.setVisibility(View.VISIBLE);
        }

*/
        TextView userStatus = (TextView)row.findViewById(R.id.user_status);
        //      userStatus.setTypeface(Configuration.getDefaultRegularTypeface(getActivity()));

        quartiereContainer = (LinearLayout)row.findViewById(R.id.quartiere);
        //   RoundedImage profileAvatar = (RoundedImage)row.findViewById(R.id.profile_avatar);
        TextView quartiereUtente = (TextView)row.findViewById(R.id.quartiere_utente);
        quartiereUtente.setTypeface(Configuration.getDefaultMediumTypeface(getActivity()));
        quartiereContainer.setOnClickListener(this);


        TextView scriviText = (TextView)row.findViewById(R.id.scrivi_text);
        scriviText.setTypeface(Configuration.getDefaultMediumTypeface(getActivity()));

        TextView fotoText = (TextView)row.findViewById(R.id.foto_text);
        fotoText.setTypeface(Configuration.getDefaultMediumTypeface(getActivity()));


        TextView followingNum = (TextView)row.findViewById(R.id.following_num);

        TextView followingText = (TextView)row.findViewById(R.id.following_text);
        followingText.setTypeface(Configuration.getDefaultRegularTypeface(getActivity()));


        TextView followerNum = (TextView)row.findViewById(R.id.follower_num);
        followerNum.setTypeface(Configuration.getDefaultMediumTypeface(getActivity()));

        TextView followerText = (TextView)row.findViewById(R.id.follower_text);
        followerText.setTypeface(Configuration.getDefaultRegularTypeface(getActivity()));


 /*
        followButton.setOnClickListener(this);

     followingNum.setText(u.following + "");
        followerNum.setText(u.followers + "");

        if(u.description == null || u.description.length() == 0){
            userStatus.setVisibility(View.INVISIBLE);
        }
        else {
            userStatus.setText(u.description);
            userStatus.setVisibility(View.VISIBLE);
        }

        ArrayList<Quartiere> quartieri = DataManager.getQuartieri(getActivity());
        Quartiere userQuartiere = null;
        for(Quartiere q : quartieri){
            if(q.id == u.quartiere){
                userQuartiere = q;
                break;
            }
        }


        if(userQuartiere != null)
            quartiereUtente.setText(userQuartiere.nome);

        if(u.picture != null){
            ImageLoader.getInstance().displayImage(u.picture, profileAvatar);
        }

        if(feedListView.getHeaderViewsCount() == 0)
            feedListView.addHeaderView(row);

  */
    }


    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0) {
            if(flag_loading == false) {
                flag_loading = true;
                //              wsManager.getUserWall(id_utente, loggedUser.id, id_post, 0);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if(FragmentsManager.getInstance().getActualFragment() != this)
            return;

        if(utente == null)
            this.setPageTitle("");
        else {
            if (isAuthFromFacebook){
                this.setPageTitle(nome);
            }
            else
                this.setPageTitle(nome + " " + cognome);
        }
        FragmentsManager.getInstance().getMainActivity().getPageTitleView().setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        FragmentsManager.getInstance().getMainActivity().getPageTitleView().setOnClickListener(null);
    }

    private void fillListView(){
        if(getActivity() == null)
            return;

        loading.setVisibility(View.GONE);

        if(maps == null)
            maps = new ArrayList<HashMap<String, Object>>();
        else
            maps.clear();

        /*   for(Post post : posts){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("id", post.id);
            map.put("autore", post.autore);
            map.put("user_name", post.nome_autore + " " + post.cognome_autore);
            map.put("user_location", post.place.nome);
            map.put("feed_text", post.content);
            map.put("user_picture", post.avatar_autore);
            map.put("photo", post.photo);
            map.put("num_likes", post.numLikes);
            map.put("num_comments", post.numComments);
            map.put("has_like", post.hasLiked);
            map.put("post_data", post);

            map.put("hideUserTag", post.on_utente == utente.id);

            maps.add(map);

        } */

        if(feedListView.getAdapter() != null){
            ((SimpleAdapter)(((HeaderViewListAdapter)feedListView.getAdapter()).getWrappedAdapter())).notifyDataSetChanged();
        }
        else {
            if(getActivity() == null)
                return;

    /*        FeedSimpleAdapter adapter = new FeedSimpleAdapter(getFragmentActivity(), maps, R.layout.feed_item,
                    new String[]{ "user_name", "user_location", "feed_text", "user_picture", "photo", "num_likes", "num_comments", "has_like" },
                    new int[]{R.id.user_name, R.id.user_location, R.id.feed_text, R.id.user_avatar, R.id.post_photo, R.id.num_likes_container, R.id.num_comment_container, R.id.like_button }, this, true);
            feedListView.setAdapter(adapter);
     */

        }


        feedListView.setVisibility(View.VISIBLE);
    }


    private void loadUserData(){
        if (isAuthFromFacebook){
            this.setPageTitle(nome);
        }
        else
            this.setPageTitle(nome + " " + cognome);

        addHeaderView();
    }

    @Override
    public void onRefresh() {
        //    wsManager.getUserProfile(loggedUser.id, id_utente);
    }

}