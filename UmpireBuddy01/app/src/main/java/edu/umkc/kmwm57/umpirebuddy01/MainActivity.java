package edu.umkc.kmwm57.umpirebuddy01;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import edu.umkc.kmwm57.umpirebuddy01.R;

/* This app uses Android tool bar in AppCompatActivity.
   The tool bar can also be specified as a view element.
   Ref: https://blog.xamarin.com/android-tips-hello-toolbar-goodbye-action-bar/
        https://www.youtube.com/watch?t=49&v=5Be2mJzP-Uw
 */
public class MainActivity extends AppCompatActivity implements OnClickListener {

    static final private String TAG = "Umpire Buddy";

    private int strike_count = 0;
    private int ball_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The following will print to LogCat.
        Log.i(TAG, "Starting onCreate...");
        setContentView(R.layout.activity_main);

        View strikeIncrementButton = findViewById(R.id.strike_button);
        // This class implements the onClickListener interface.
        // Passing 'this' to setOnClickListener means the
        //   onClick method in this class will get called
        //   when the button is clicked.
        strikeIncrementButton.setOnClickListener(this);

        updateStrikeCount();


        View ballIncrementButton = findViewById(R.id.ball_button);
        ballIncrementButton.setOnClickListener(this);
        updateBallCount();

    }

    private void updateStrikeCount() {
        TextView t = (TextView) findViewById(R.id.strike_count_value);
        t.setText(Integer.toString(strike_count));
    }

    private void updateBallCount() {
        TextView t = (TextView) findViewById(R.id.ball_count_value);
        t.setText(Integer.toString(ball_count));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.strike_button:
                // batter is out if strike_count ==3
                strike_count++;
                updateStrikeCount();
                if (strike_count == 3) {
                    // Builder is an inner class so we have to qualify it
                    // with its outer class: AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    //builder.setTitle("Dialog box");
                    builder.setMessage("BATTER OUT!!!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Bummer.", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            strike_count = 0;
                            ball_count = 0;
                            // Note, you have to call update count here because.
                            //   the call builder.show() below is non blocking.
                            updateStrikeCount();
                            updateBallCount();
                        }

                    });
                    builder.show();
                }
                break;
            case R.id.ball_button:
                ball_count++;
                updateBallCount();
                if (ball_count == 4) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    //builder.setTitle("Dialog box");
                    builder.setMessage("Batter walks.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Okay.", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            strike_count = 0;
                            ball_count = 0;
                            // Note, you have to call update count here because.
                            //   the call builder.show() below is non blocking.
                            updateStrikeCount();
                            updateBallCount();

                        }
                    });
                    builder.show();

                }
                break;
        }
    }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }

