package lincolnli2.scm.pov;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lincoln on 26/2/2018.
 * The custom view for displaying the POV
 */

public class DisplayPOVView extends View {

    int[] pixels;
    int[] palette;
    int x = 0;
    int pixel_width = 10;
    Timer t = null;
    TimerTask timerTask = null;
    Paint p = new Paint();
    long changeRate;

    public DisplayPOVView(Context context) {
        super(context);
        t = new Timer();
        palette = context.getResources().getIntArray(R.array.palette);

    }

    void SetPixels(int[] ps) {
        pixels = ps;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int h = canvas.getHeight();
        int w = canvas.getWidth();
        pixel_width = h / 10;


        for (int y = 0; y < 10; y++) {
            p.setColor(palette[pixels[y * 10 + x]]);
            canvas.drawRect(w / 2 - pixel_width / 2, y * pixel_width, w / 2 + pixel_width / 2, (y + 1) * pixel_width, p);
        }

        super.onDraw(canvas);
    }


    private void updateFrame()
    {
        x++;
        if(x>=10) x=0;

        //update
        DisplayPOVView.this.postInvalidate();
    }

    public void updateChangeRate(long newRate) {
        changeRate = newRate;

        if (timerTask != null) timerTask.cancel();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                updateFrame();
            }
        };

        t.schedule(timerTask, 0, changeRate);
    }
}
