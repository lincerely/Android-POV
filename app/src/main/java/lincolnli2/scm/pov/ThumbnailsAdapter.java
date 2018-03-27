package lincolnli2.scm.pov;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import io.paperdb.Paper;

/**
 * Created by lincoln on 26/2/2018.
 */

public class ThumbnailsAdapter extends BaseAdapter {

    private final Context context;
    private ArrayList<String> savedPixels;
    private ArrayList<String> selectedPixels;

    public ThumbnailsAdapter(Context context) {
        this.context = context;
        this.savedPixels = Paper.book().read(DisplaySavedActivity.EXTRA_SAVED_PIXELS, new ArrayList<String>());
        this.selectedPixels = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return savedPixels.size();
    }

    @Override
    public Object getItem(int position) {
        return savedPixels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ThumbnailView thumbnailView = new ThumbnailView(context);
        thumbnailView.SetPixels(stringToIntArray(savedPixels.get(position)));
        thumbnailView.setMinimumWidth(parent.getWidth() / 3);
        thumbnailView.setMinimumHeight(parent.getWidth() / 3);
        thumbnailView.setForeground(ContextCompat.getDrawable(context, R.drawable.del_selector));
        return thumbnailView;
    }

    private int[] stringToIntArray(String s) {
        char[] chars = s.toCharArray();
        int[] intArray = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            intArray[i] = Character.getNumericValue(chars[i]);
        }
        return intArray;
    }

    public int[] getIntArrayAt(int pos) {
        return stringToIntArray(savedPixels.get(pos));
    }

    public int addToSelection(int pos) {
        selectedPixels.add(savedPixels.get(pos));
        return selectedPixels.size();
    }

    public int removeFromSelection(int pos) {
        selectedPixels.remove(savedPixels.get(pos));
        return selectedPixels.size();
    }

    public void removeSelected() {
        savedPixels.removeAll(selectedPixels);
        Paper.book().write(DisplaySavedActivity.EXTRA_SAVED_PIXELS, savedPixels);
        this.notifyDataSetChanged();
    }
}
