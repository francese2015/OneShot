package watchout;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.PushService;

/**
 * Created by Damiano on 30/04/15.
 */
public class Watchout extends Application {

    public void onCreate(){
        super.onCreate();


        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        //Initialize Parse
        Parse.initialize(this, "UhIJUG93RLGxsFfqZ2gYHwnIeDINOeyNF6DZE1Go", "FzD8kIh3k4tjGGkYOPCdYjN5vPml0UY7AsyjBs2o");

        //Initialize Facebook
        ParseFacebookUtils.initialize(this);

        //Push Notification
        ParseInstallation.getCurrentInstallation().saveInBackground();



    }
}
