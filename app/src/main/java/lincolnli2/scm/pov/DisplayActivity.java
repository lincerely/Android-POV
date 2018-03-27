package lincolnli2.scm.pov;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.squareup.seismic.ShakeDetector;

/**
 * The full screen display activity the show the POV view and get input to control framerate
 */
public class DisplayActivity extends AppCompatActivity implements ShakeDetector.Listener {

    private static long MAX_SESSION_DURATION = 2000;
    private long changeRate = 25;
    private DisplayPOVView pov;
    private long lastShake;
    private long averageShake;
    private long sumShakeDuration;
    private long numShakeSessions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pov = new DisplayPOVView(this.getApplicationContext());
        Intent intent = getIntent();
        pov.SetPixels(intent.getIntArrayExtra(MainActivity.EXTRA_PIXELS));

        setContentView(pov);
        pov.updateChangeRate(changeRate);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ( pov != null && keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if(changeRate > 5) {
                changeRate -= 5;
                pov.updateChangeRate(changeRate);
                NotifyChange();
            }
            return true;

        } else if( pov != null && keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {
            if(changeRate < 50)
            {
                changeRate += 5;
                pov.updateChangeRate(changeRate);
                NotifyChange();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void hearShake() {
        Log.d("Shake Test", "hearShake");
        long curTime = System.currentTimeMillis();
        long diff = curTime - lastShake;
        lastShake = curTime;

        if (lastShake == 0 || diff >= MAX_SESSION_DURATION) {
            return;
        }

        sumShakeDuration += diff;
        numShakeSessions++;
        averageShake = sumShakeDuration / numShakeSessions;
        pov.updateChangeRate(averageShake / 12);

    }

    void NotifyChange()
    {
        Toast.makeText(getApplicationContext(), Long.toString(changeRate), Toast.LENGTH_SHORT).show();
    }

}