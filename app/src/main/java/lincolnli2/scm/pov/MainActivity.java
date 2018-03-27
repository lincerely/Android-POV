package lincolnli2.scm.pov;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import io.paperdb.Paper;

/*
 * Launcher avtivity.
 * Use to edit + save + load patterns
 */

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_PIXELS = "pixels";
    static final int PICK_SAVED = 1;  // The request code
    GridView gridView;
    SeekBar seekColor;
    int[] pixels;
    ArrayList<String> savedPixels;
    int[] palette;
    int brush = 1;
    PixelsAdapter pixelsAdapter;
    int[] defaultPixels =
            {
                    0,0,0,0,0,0,0,0,0,0,
                    0,0,8,0,0,0,0,8,0,0,
                    0,0,8,0,0,0,0,8,0,0,
                    0,0,8,0,0,0,0,8,0,0,
                    0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,
                    0,0,8,0,0,0,0,8,0,0,
                    0,0,8,8,8,8,8,8,0,0,
                    0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load from storage
        if (pixels == null)
            pixels = Paper.book().read(EXTRA_PIXELS, defaultPixels);

        savedPixels = Paper.book().read(DisplaySavedActivity.EXTRA_SAVED_PIXELS,
                new ArrayList<>(Arrays.asList(pixelsToString(defaultPixels))));

        //
        // set up pixels editor
        //
        gridView = findViewById(R.id.gridPreview);
        pixelsAdapter = new PixelsAdapter(this, pixels);
        gridView.setAdapter(pixelsAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pixels[position] = brush;
                pixelsAdapter.notifyDataSetChanged();
            }
        });

        //
        // set up color selector
        //
        palette = getResources().getIntArray(R.array.palette);

        seekColor = findViewById(R.id.seekColor);
        seekColor.setMax(palette.length-1);
        seekColor.setBackgroundColor(palette[brush]);
        seekColor.setProgress(brush);

        seekColor.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        seekBar.setBackgroundColor(palette[progress]);
                        brush = progress;

                    }

                    @Override public void onStartTrackingTouch(SeekBar seekBar){}
                    @Override public void onStopTrackingTouch(SeekBar seekBar) {}
                }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        SavePixels();
    }

    //
    // Menu
    //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                ClearPixels();
                return true;
            case R.id.menu_info:
                About();
                return true;

            case R.id.menu_saved:
                OpenSaved();
                return true;

            case R.id.menu_saving:
                Save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_SAVED) {
            pixels = data.getExtras().getIntArray(EXTRA_PIXELS);
            pixelsAdapter = new PixelsAdapter(this, pixels);
            gridView.setAdapter(pixelsAdapter);
        }
    }

    public void Display(View view)
    {
        Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
        intent.putExtra(EXTRA_PIXELS, pixels);
        startActivity(intent);
    }

    private void SavePixels()
    {
        Paper.book().write(EXTRA_PIXELS, pixels);
    }

    private void ClearPixels()
    {
        for(int i=0; i< pixels.length; i++)
        {
            pixels[i] = 0;
        }
        pixelsAdapter.notifyDataSetChanged();
    }

    private void About()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_about, null));

        AlertDialog alertDialog = builder.create();
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void OpenSaved() {
        Intent intent = new Intent(MainActivity.this, DisplaySavedActivity.class);
        startActivityForResult(intent, PICK_SAVED);
    }

    private void Save() {
        //save the current pattern to the paper database
        String strPixels = pixelsToString(pixels);

        if (savedPixels.contains(strPixels))
            Toast.makeText(MainActivity.this, "Duplicate pattern found, save cancelled", Toast.LENGTH_SHORT).show();
        else {
            savedPixels.add(strPixels);
            Paper.book().write(DisplaySavedActivity.EXTRA_SAVED_PIXELS, savedPixels);
            Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        }
    }

    // pack int array into a single String
    private String pixelsToString(int[] ints) {
        return Arrays.toString(ints)
                .replace("[", "")
                .replace("]", "")
                .replace(",", "")
                .replace(" ", "");
    }
}
