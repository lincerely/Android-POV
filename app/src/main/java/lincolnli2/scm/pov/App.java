package lincolnli2.scm.pov;

import android.app.Application;

import io.paperdb.Paper;

/**
 * Created by lincoln on 27/2/2018.
 * Override onCreate to enable Paper
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(getApplicationContext());
    }
}
