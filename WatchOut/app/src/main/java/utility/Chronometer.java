package utility;
/**
 * Created by CosimoAlessandro on 10/05/2015.
 */
/*
 * The Android chronometer widget revised so as to count milliseconds
 */
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import java.text.DecimalFormat;
public class Chronometer extends TextView {
    @SuppressWarnings("unused")
    private static final String TAG = "Chronometer";
    public interface OnChronometerTickListener {
        void onChronometerTick(Chronometer chronometer);
    }
    private long mBase;
    private boolean mVisible;
    private boolean mStarted;
    private boolean mRunning;
    private OnChronometerTickListener mOnChronometerTickListener;
    private static final int TICK_WHAT = 2;
    private long timeElapsed;
    public Chronometer(Context context) {
        this (context, null, 0);
    }
    public Chronometer(Context context, AttributeSet attrs) {
        this (context, attrs, 0);
    }
    public Chronometer(Context context, AttributeSet attrs, int defStyle) {
        super (context, attrs, defStyle);
        init();
    }
    private void init() {
        mBase = SystemClock.elapsedRealtime();
        updateText(mBase);
    }

    public void setBase(long base) {
        mBase = base;
        dispatchChronometerTick();
        updateText(base);
    }

    public long getBase() {
        return mBase;
    }

    public void setOnChronometerTickListener(
            OnChronometerTickListener listener) {
        mOnChronometerTickListener = listener;
    }
    public OnChronometerTickListener getOnChronometerTickListener() {
        return mOnChronometerTickListener;
    }
    public void start() {
        // mBase = SystemClock.elapsedRealtime();
        mStarted = true;
        updateRunning();
    }
    public void stop() {
        mStarted = false;
        updateRunning();
    }
    public void setStarted(boolean started) {
        mStarted = started;
        updateRunning();
    }
    @Override
    protected void onDetachedFromWindow() {
        super .onDetachedFromWindow();
        mVisible = false;
        updateRunning();
    }
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super .onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        updateRunning();
    }
    private synchronized void updateText(long now) {
        timeElapsed =  now - mBase;
        //Log.d("timeElapsed", Long.toString(timeElapsed));
        DecimalFormat df = new DecimalFormat("00");
        int hours = (int)(timeElapsed / (3600 * 1000));
        int remaining = (int)(timeElapsed % (3600 * 1000));
        int minutes = (int)(remaining / (60 * 1000));
        remaining = (int)(remaining % (60 * 1000));
        int seconds = (int) (timeElapsed - hours * 3600000 - minutes * 60000) / 1000;
        remaining = (int)(remaining % (1000));
        int milliseconds = (int)(((int)timeElapsed % 1000) / 10);
        String text = "";
        /*if (hours > 0) {
            text += df.format(hours) + ":";
        }*/
        // text += df.format(minutes) + ":";
        text += df.format(seconds) + ":";
        text += Integer.toString(milliseconds);
        setText(text);
    }

    private void updateRunning() {
        boolean running = mVisible && mStarted;
        if (running != mRunning) {
            if (running) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                mHandler.sendMessageDelayed(Message.obtain(mHandler,
                        TICK_WHAT), 100);
            } else {
                mHandler.removeMessages(TICK_WHAT);
            }
            mRunning = running;
        }
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (mRunning) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                sendMessageDelayed(Message.obtain(this , TICK_WHAT),
                        100);
            }
        }
    };
    void dispatchChronometerTick() {
        if (mOnChronometerTickListener != null) {
            mOnChronometerTickListener.onChronometerTick(this);
        }
    }
    public long getTimeElapsed() {
        return timeElapsed;
    }
}