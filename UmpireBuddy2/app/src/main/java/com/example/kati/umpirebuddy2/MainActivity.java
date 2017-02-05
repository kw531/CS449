package com.example.kati.umpirebuddy2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements OnClickListener{

    private int strike_count = 0;
    private int ball_count = 0;
    private int out_count = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Main menu creator
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        inflater.inflate(R.menu.reset, menu);
        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // Context menu creation
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.actions_textview,menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readOuts(); // Read in the shared preferences
        updateOutCount(); // Update to show the lifetime "out" count

        View strikeIncrementButton = findViewById(R.id.btnStrike);
        strikeIncrementButton.setOnClickListener(this);

        View ballIncrementButton = findViewById(R.id.btnBall);
        ballIncrementButton.setOnClickListener(this);

        // Context menu listening
        Button btn1=(Button)strikeIncrementButton;
        registerForContextMenu(btn1);
        Button btn2=(Button)ballIncrementButton;
        registerForContextMenu(btn2);

        resetBalls(); // Strikes/Balls aren't saved, when created just reset both
    }

    private void updateStrikeCount() {
        TextView t = (TextView) findViewById(R.id.strikeCount);
        t.setText(Integer.toString(strike_count));
    }

    private void updateBallCount() {
        TextView t = (TextView) findViewById(R.id.ballCount);
        t.setText(Integer.toString(ball_count));
    }

    private void updateOutCount() {
        TextView t = (TextView) findViewById(R.id.outCount);
        t.setText(Integer.toString(out_count));
    }

    private void addStrike(){
        // Strike button has been clicked
        strike_count++;
        updateStrikeCount();

        if (strike_count == 3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Too many strikes");
            builder.setMessage("BATTER OUT!!!");
            addOuts(); //Update out total outs.

            builder.setCancelable(false); //Cancel button
            builder.setPositiveButton("Bummer.", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //The OK Button
                    resetBalls();
                }
            });
            builder.show();
        }
    }

    private void addBall(){
        // Ball button has been clicked
        ball_count++;
        updateBallCount();
        if (ball_count == 4) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Too many balls");
            builder.setMessage("Batter walks.");
            builder.setCancelable(false);
            builder.setPositiveButton("Okay.", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {resetBalls();
                }
            });
            builder.show();

        }
    }

    private void resetBalls() {
        // Reset the ball and strike count to zero and update
        strike_count = 0;
        ball_count = 0;
        updateStrikeCount();
        updateBallCount();
    }

    public void viewAbout() {
        //Unnecessary for the scope of this project, but will be useful in the future
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }

    private void addOuts() {
        //Saving the outs in the shared pref folder.
        out_count++;
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("totalOuts", out_count);
        editor.commit();
        updateOutCount();
    }

    private void readOuts() {
        // Reads in the shared preference data
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        out_count = prefs.getInt("totalOuts", 0); //0 is the default value
    }

    @Override
    public void onClick(View v) {
        // Strike or Ball button has been clicked
        switch (v.getId()) {
            case R.id.btnStrike:
                addStrike();
                break;
            case R.id.btnBall:
                addBall();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection in the main menu
        switch (item.getItemId()) {
            case R.id.action_about:
                viewAbout();
                return true;
            case R.id.action_reset:
                resetBalls();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Context menu button has been clicked
        switch (item.getItemId()) {
            case R.id.context_ball:
                addBall();
                return true;
            case R.id.context_strike:
                addStrike();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
