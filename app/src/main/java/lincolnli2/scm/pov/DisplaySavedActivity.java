package lincolnli2.scm.pov;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

//
// Display previous saved patterns
//

public class DisplaySavedActivity extends AppCompatActivity {

    public final static String EXTRA_SAVED_PIXELS = "saved";
    ThumbnailsAdapter adapter;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_saved);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.saved_pattern);
        ab.setSubtitle(R.string.hold_hint);

        gridView = findViewById(R.id.savedThumbnails);
        adapter = new ThumbnailsAdapter(this.getApplicationContext());
        gridView.setAdapter(adapter);

        gridView.setEmptyView(findViewById(R.id.placeholder));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LoadAndOpen(position);
            }
        });

        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);

        gridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    //add
                    mode.setSubtitle("Selected: " + adapter.addToSelection(position));
                } else {
                    //remove
                    mode.setSubtitle("Selected: " + adapter.removeFromSelection(position));
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.menu_multi_del, menu);
                mode.setTitle("Delete");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.del_menu_btn:
                        Remove();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mode.setSubtitle("Hold to delete");
            }
        });
    }

    void LoadAndOpen(int pos) {
        //open it in main activity
        // ref:https://stackoverflow.com/questions/10582314/how-to-get-back-the-result-from-child-activity-to-parent-in-android
        Intent data = new Intent();
        data.putExtra(MainActivity.EXTRA_PIXELS, adapter.getIntArrayAt(pos));
        setResult(RESULT_OK, data);
        finish();
    }

    void Remove() {
        adapter.removeSelected();
    }
}
