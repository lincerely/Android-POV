package lincolnli2.scm.pov;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.Window;
import android.widget.Toast;

/**
 * The full screen display activity the show the POV view and get input to control framerate
 */
public class DisplayActivity extends AppCompatActivity {

    private long changeRate = 25;
    private DisplayPOV pov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pov = new DisplayPOV(this.getApplicationContext());
        Intent intent = getIntent();
        pov.SetPixels(intent.getIntArrayExtra(MainActivity.EXTRA_PIXELS));

        setContentView(pov);
        pov.updateChangeRate(changeRate);
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

    void NotifyChange()
    {
        Toast.makeText(getApplicationContext(), Long.toString(changeRate), Toast.LENGTH_SHORT).show();
    }




}
