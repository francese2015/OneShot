package fragments;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cosimoalessandro.watchout.R;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.HashMap;
import watchout.FragmentsActivity;
import watchout.User;

public class RankingFragment extends MainFragment implements  AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
    private ListView list;
    private SwipeRefreshLayout swipeLayout;
    private ProgressBar loading;
    private TextView noUsers;
    private boolean flag_loading = false;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<HashMap<String, Object>> maps;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConversationsFragment.
     */
    //TODO: Rename and change types and number of parameters
    public static RankingFragment newInstance(String param1, String param2) {
        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public RankingFragment() {
        // Required empty public constructor
    }
    @Override
    public void onStart() {
        super.onStart();
        this.setPageTitle(getString(R.string.rank_fragment));
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
        View view = inflater.inflate(R.layout.fragment_ranking, null, true);
        swipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        loading = (ProgressBar)view.findViewById(R.id.loading_wall);
        noUsers = (TextView)view.findViewById(R.id.no_users);
        list = (ListView) view.findViewById(R.id.ranking_list);
        list.setOnScrollListener(this);
        fillListView();
        this.initTabBar(view);
      /*  setHasOptionsMenu(true);
*/
        return view;
    }
    /*    @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            super.onCreateOptionsMenu(menu, inflater);
            getFragmentActivity().getMenuInflater().inflate(R.menu.menu_activity_with_fragments, menu);
            SearchManager searchManager = (SearchManager) getFragmentActivity().getSystemService( Context.SEARCH_SERVICE );
            SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getFragmentActivity().getComponentName()));
            searchView.setSubmitButtonEnabled(true);
            searchView.setIconifiedByDefault(true);
            searchView.setOnQueryTextListener(this);
            searchView.setQueryHint("Type here");
        }
        @Override
        public boolean onQueryTextChange(String newText)
        {
            // this is your adapter that will be filtered
            if (TextUtils.isEmpty(newText))
            {
                list.clearTextFilter();
            }
            else
            {
                list.setFilterText(newText.toString());
            }
            return true;
        }
        @Override
        public boolean onQueryTextSubmit(String query) {
            // TODO Auto-generated method stub
            return false;
        }*/
    private void fillListView(){
        if(isNetworkAvailable()) {
            if (getActivity() == null)
                return;
            ParseCloud.callFunctionInBackground("get_ranking", new HashMap<String, Object>(), new FunctionCallback<ArrayList<ParseUser>>() {
                public void done(ArrayList<ParseUser> users, ParseException e) {
                    if (e == null) {
                        loading.setVisibility(View.GONE);
                        if (users.size() == 0) {
                            list.setVisibility(View.GONE);
                            noUsers.setVisibility(View.VISIBLE);
                        } else {
                            noUsers.setVisibility(View.GONE);
                            if (maps == null)
                                maps = new ArrayList<HashMap<String, Object>>();
                            else
                                maps.clear();
                            for (int i = 0; i < users.size(); i++) {
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("id", users.get(i).getObjectId());
                                map.put("user_name", users.get(i).get("name") + " " + users.get(i).get("cognome"));
                                map.put("user_score", users.get(i).get("punteggio"));
                                //map.put("user_avatar", "ciao");
                                Log.d("Nome", users.get(i).get("name").toString());
                                maps.add(map);
                                if (list.getAdapter() != null) {
                                    ((SimpleAdapter) list.getAdapter()).notifyDataSetChanged();
                                } else {
                                    SimpleAdapter adapter = new SimpleAdapter(getFragmentActivity(), maps, R.layout.ranking_row, new String[]{"user_name", "user_score"},
                                            new int[]{R.id.user_name, R.id.user_score});
                                    list.setAdapter(adapter);
                                }
                                list.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        Log.d("Errore query ranking", "Errore");
                    }
                }
            });
        }else Toast.makeText(getFragmentActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getFragmentActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    @Override
    public void onRefresh() {
    }


}