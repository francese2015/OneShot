package watchout;

import android.content.Intent;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Damiano on 09/05/15.
 */
public class User extends ParseUser {


    // A default constructor
    public User() {}

    public String getName(){
        return getName();
    }

    public String getEmail(){
        return getEmail();
    }

    public String getPunteggio(){
        return getString("punteggio");
    }


}
