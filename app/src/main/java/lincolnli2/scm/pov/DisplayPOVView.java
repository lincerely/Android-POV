package lincolnli2.scm.pov;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;

/**
 * Created by lincoln on 26/2/2018.
 * The custom view for displaying the POV
 */

public class DisplayPOVView extends View {

    int[] pixels;
    int[] palette;
    int x = 0;
    int pixel_width = 10;
    Paint p = new Paint();
    long averageFrameRate;
    long nextFrame;

    //Use handler instead of timertask
    //ref:https://stackoverflow.com/questions/20330355/timertask-or-handler
    Handler handler = new Handler();

    Runnable updateFrame = new Runnable() {
        @Override
        public void run() {
            x++;
            if (x >= 10) x = 0;

            //cal next delay
            long offset = x / 10 * 360;
            long nextFrame = (long) (averageFrameRate * (1.01 + Math.cos(offset)) / 2);

            //update
            DisplayPOVView.this.postInvalidate();

            //next update
            handler.postDelayed(updateFrame, nextFrame);
        }
    };

    public DisplayPOVView(Context context) {
        super(context);
        palette = context.getResources().getIntArray(R.array.palette);
        handler.post(updateFrame);
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

    public void updateAverageFrameRate(long newRate) {
        averageFrameRate = newRate;
        x = 9;

        if (handler != null) handler.removeCallbacksAndMessages(null);
        handler.postDelayed(updateFrame, nextFrame);

    }
}
