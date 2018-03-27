package lincolnli2.scm.pov;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by lincoln on 26/2/2018.
 * The custom view for previewing the pixels
 */

public class ThumbnailView extends View {

    int[] pixels;
    int[] palette;

    int pixel_width = 10;

    Paint p = new Paint();

    public ThumbnailView(Context context) {
        super(context);
        palette = context.getResources().getIntArray(R.array.palette);

    }

    // For initializing the pixel array
    void SetPixels(int[] ps) {
        pixels = ps;
        ThumbnailView.this.postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int h = canvas.getHeight();
        int w = canvas.getWidth();
        pixel_width = h < w ? h / 10 : w / 10;

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                p.setColor(palette[pixels[y * 10 + x]]);
                canvas.drawRect(
                        pixel_width * x,
                        pixel_width * y,
                        pixel_width * (x + 1),
                        (y + 1) * pixel_width,
                        p);
            }
        }

        super.onDraw(canvas);
    }
}