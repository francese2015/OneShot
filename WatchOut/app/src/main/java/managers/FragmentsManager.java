package managers;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;

import watchout.FragmentsActivity;
import com.example.cosimoalessandro.watchout.R;

import fragments.MainFragment;

/**
 * Created by CosimoAlessandro on 03/04/2015.
 */
public class FragmentsManager {

    private static FragmentsManager _instance;

    private FragmentManager fragmentsManager;
    private FragmentsActivity mainActivity;
    private MainFragment actualFragment;


    public static FragmentsManager init(FragmentManager fManager, FragmentsActivity activity, MainFragment initialFragment){
        _instance = new FragmentsManager(fManager, activity, initialFragment);

        return _instance;
    }

    public static FragmentsManager getInstance(){
        return _instance;
    }

    private FragmentsManager(FragmentManager fManager, FragmentsActivity activity, MainFragment initialFragment){
        this.mainActivity = activity;
        this.fragmentsManager = fManager;
        this.actualFragment = initialFragment;

        setActualFragment(false);
    }

    public FragmentsActivity getMainActivity(){
        return this.mainActivity;
    }

    public MainFragment getActualFragment(){
        return (MainFragment)fragmentsManager.findFragmentById(R.id.container);
    }

    private void setActualFragment(boolean withPushBack){
        FragmentTransaction transaction = fragmentsManager.beginTransaction();
        transaction.replace(R.id.container, actualFragment);

        if(withPushBack)
            transaction.addToBackStack(null);
        else
            fragmentsManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        transaction.commit();
    }

    public void loadFragment(MainFragment fragment, boolean withPush){
        this.actualFragment = fragment;

        setActualFragment(withPush);

        getMainActivity().setShowBack(withPush);
    }

    public void goBack(){
        if(this.actualFragment != null){
            this.actualFragment.onGoBack();
        }
        fragmentsManager.addOnBackStackChangedListener(new OnBackStackChangedListener() {

            @Override
            public void onBackStackChanged() {
                if(fragmentsManager.getBackStackEntryCount() == 0){
                    mainActivity.setShowBack(false);
                }
            }
        });
        fragmentsManager.popBackStackImmediate();
    }

}

