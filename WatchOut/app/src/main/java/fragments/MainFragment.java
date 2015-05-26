package fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.view.View.OnClickListener;

import managers.FragmentsManager;
import com.example.cosimoalessandro.watchout.R;


public class MainFragment extends Fragment implements OnClickListener {

    private RelativeLayout tabBar;

    private ImageButton menuButton;
    private ImageButton clanButton;
    private ImageButton homeButton;
    private ImageButton tournamentButton;
    private ImageButton rankingButton;

   // private BadgeView notificationsBadge, messagesBadge;

    private FrameLayout messagesButtonContainer, notificationsButtonContainer, notificationsFrame, messagesFrame;


    protected void initTabBar(View view){

        tabBar = (RelativeLayout)view.findViewById(R.id.tab_bar);

        menuButton = (ImageButton)view.findViewById(R.id.menu_button);
        menuButton.setOnClickListener(this);

        clanButton = (ImageButton)view.findViewById(R.id.clan_button);
        clanButton.setOnClickListener(this);

        homeButton = (ImageButton)view.findViewById(R.id.home_button);
        homeButton.setOnClickListener(this);

        tournamentButton = (ImageButton)view.findViewById(R.id.tournament_button);
        tournamentButton.setOnClickListener(this);

        rankingButton = (ImageButton)view.findViewById(R.id.ranking_button);
        rankingButton.setOnClickListener(this);

       /* messagesButtonContainer = (FrameLayout)view.findViewById(R.id.messages_button_container);
        messagesButtonContainer.setOnClickListener(this);

        notificationsButtonContainer = (FrameLayout)view.findViewById(R.id.notification_button_container);
        notificationsButtonContainer.setOnClickListener(this);


        notificationsFrame = (FrameLayout)view.findViewById(R.id.notifications_frame_container);
        notificationsFrame.setOnClickListener(this);*/
/*
        notificationsBadge = new BadgeView(getActivity(), notificationsFrame);
        notificationsBadge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        notificationsBadge.setOnClickListener(this);
*/
        /*messagesFrame = (FrameLayout)view.findViewById(R.id.messages_frame_container);
        messagesFrame.setOnClickListener(this);*/
/*
        messagesBadge = new BadgeView(getActivity(), messagesFrame);
        messagesBadge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        messagesBadge.setBadgeMargin(10);
        messagesBadge.setOnClickListener(this);
*/
        initButtons();
    }

    /*
    public void setBadgeValues(int num_notifications, int num_messages){
        if(notificationsBadge != null){
            if(num_notifications > 0){
                notificationsBadge.setText("" + num_notifications);
                notificationsBadge.show();
            }
            else {
                notificationsBadge.hide();
            }
        }

        if(messagesBadge != null){
            if(num_messages > 0){
                messagesBadge.setText("" + num_messages);
                messagesBadge.show();
            }
            else {
                messagesBadge.hide();
            }
        }
    }
*/
    protected void setPageTitle(String pageTitle){
        FragmentsManager.getInstance().getMainActivity().getPageTitleView().setText(pageTitle);
    }

    private void initButtons(){

        if(FragmentsManager.getInstance().getActualFragment() instanceof ClanFragment)
            clanButton.setImageResource(R.drawable.icon_clan_selected);
        else
            clanButton.setImageResource(R.drawable.icon_clan);


        if(FragmentsManager.getInstance().getActualFragment() instanceof HomeFragment)
            homeButton.setImageResource(R.drawable.icon_home_selected);
        else
            homeButton.setImageResource(R.drawable.icon_home);

        if(FragmentsManager.getInstance().getActualFragment() instanceof TournamentFragment)
            tournamentButton.setImageResource(R.drawable.icon_tournament_selected);
        else
            tournamentButton.setImageResource(R.drawable.icon_tournament);


        if(FragmentsManager.getInstance().getActualFragment() instanceof RankingFragment){
            rankingButton.setImageResource(R.drawable.icon_ranking_selected);
        }
        else {
            rankingButton.setImageResource(R.drawable.icon_ranking);
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // manage back button

        if(FragmentsManager.getInstance().getActualFragment() instanceof HomeFragment){
            FragmentsManager.getInstance().getMainActivity().setShowBack(false);
        }

    }

    @Override
    public void onClick(View v) {
        if(v == menuButton){
            FragmentsManager.getInstance().getMainActivity().openMenu();
        }
        else if(v == clanButton){
            if(!(FragmentsManager.getInstance().getActualFragment() instanceof ClanFragment)){
                FragmentsManager.getInstance().loadFragment(new ClanFragment(), false);
            }
        }
        else if(v == homeButton){
            if(!(FragmentsManager.getInstance().getActualFragment() instanceof HomeFragment)){
                FragmentsManager.getInstance().loadFragment(new HomeFragment(), false);
            }
        }
        else if(v == tournamentButton){
            if(!(FragmentsManager.getInstance().getActualFragment() instanceof TournamentFragment)){
                FragmentsManager.getInstance().loadFragment(new TournamentFragment(), false);
            }
        }
        else if(v == rankingButton){
            if(!(FragmentsManager.getInstance().getActualFragment() instanceof RankingFragment)){
                FragmentsManager.getInstance().loadFragment(new RankingFragment(), false);
            }
        }
    }

    public void onGoBack(){}

    public FragmentActivity getFragmentActivity(){
        return getActivity();
//		if(super.getActivity() == null)
//			return FragmentsManager.getInstance().getMainActivity();
//		else
//			return super.getActivity();
    }

}
