package fragments;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.cosimoalessandro.watchout.R;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Date;
import java.util.HashMap;

public class TournamentFragment extends MainFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TournamentFragment newInstance(String param1, String param2) {
        TournamentFragment fragment = new TournamentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public TournamentFragment() {
        // Required empty public constructor
    }
    @Override
    public void onStart() {
        super.onStart();
        this.setPageTitle(getString(R.string.tournament_fragment));
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tournament, null, true);
        ParseUser user = ParseUser.getCurrentUser();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", user.getObjectId());
        //Chiamata del metodo "Hello" su Clud di Parse
        ParseCloud.callFunctionInBackground("send_push", params, new FunctionCallback<Date>() {
            public void done(Date result, ParseException e) {
                if (e == null) {
                    // result is "Hello world!"
                    //Toast.makeText(getFragmentActivity(), result, Toast.LENGTH_LONG).show();
                }
            }
        });
        // Inflate the layout for this fragment
        this.initTabBar(view);
        return view;
    }
}