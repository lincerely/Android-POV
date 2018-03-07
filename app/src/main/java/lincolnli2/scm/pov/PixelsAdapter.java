package lincolnli2.scm.pov;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by lincoln on 26/2/2018.
 */

public class PixelsAdapter extends BaseAdapter {

    private final Context context;
    private final int[] pixels;
    private final int[] palette;

    public PixelsAdapter(Context context, int[] pixels)
    {
        this.context = context;
        this.pixels = pixels;

        palette = context.getResources().getIntArray(R.array.palette);
    }


    @Override
    public int getCount() {
        return pixels.length;
    }

    @Override
    public Object getItem(int position) {
        return pixels[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView dummyTextView = new TextView(context);
        dummyTextView.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle));
        dummyTextView.getBackground().setColorFilter(palette[pixels[position]], PorterDuff.Mode.DARKEN);
        dummyTextView.setText(" ");
        dummyTextView.setWidth(dummyTextView.getHeight());
        return dummyTextView;
    }
}
