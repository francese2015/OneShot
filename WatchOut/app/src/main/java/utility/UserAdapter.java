package utility;


import java.util.Arrays;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cosimoalessandro.watchout.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import watchout.User;

/*
 * The FavoriteMealAdapter is an extension of ParseQueryAdapter
 * that has a custom layout for favorite meals, including a
 * bigger preview image, the meal's rating, and a "favorite"
 * star.
 */

public class UserAdapter extends ParseQueryAdapter<User> {

    public UserAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<User>() {
            public ParseQuery<User> create() {
                // Here we can configure a ParseQuery to display
                // only top-rated meals.
                ParseQuery query = new ParseQuery("User");
                query.whereContainedIn("punteggio", Arrays.asList("0", "8"));
                query.orderByDescending("punteggio");
                return query;
            }
        });
    }

    @Override
    public View getItemView(User user, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.rank_user_item, null);
        }

        super.getItemView(user, v, parent);

       /* ParseImageView mealImage = (ParseImageView) v.findViewById(R.id.icon);
        ParseFile photoFile = user.getParseFile("photo");
        if (photoFile != null) {
            mealImage.setParseFile(photoFile);
            mealImage.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    // nothing to do
                }
            });
        }*/

        TextView titleTextView = (TextView) v.findViewById(R.id.text1);
        titleTextView.setText(user.getName());
        TextView ratingTextView = (TextView) v.findViewById(R.id.favorite_meal_rating);
        ratingTextView.setText(user.getPunteggio());
        return v;
    }

}

