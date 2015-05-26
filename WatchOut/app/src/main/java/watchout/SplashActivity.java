package watchout;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.cosimoalessandro.watchout.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.Timer;
import java.util.TimerTask;

import fragments.HomeFragment;

public class SplashActivity extends Activity {

    private Timer goNextTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .resetViewBeforeLoading(true)
                .delayBeforeLoading(0)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(5000, 5000)
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        ImageLoader.getInstance().init(config);

        goNextAfterDelay(3000);

    }


    private void goNextAfterDelay(int delay){

        goNextTimer = new Timer();
        goNextTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                goNext();
            }
        }, delay);
    }

    @Override
    public void onBackPressed() {
        if(goNextTimer != null)
            goNextTimer.cancel();

        super.onBackPressed();
    }

    private void goNext(){
        this.finish();

        if(true) {
            Intent go = new Intent(this, DispatchActivity.class);

            startActivity(go);
        }
        else
            startActivity(new Intent(this, DispatchActivity.class));

        this.overridePendingTransition(0, 0);
    }



}




