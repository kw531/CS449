package com.example.kati.umpirebuddy2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.R.attr.textSize;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private int strike_count = 0;
    private int ball_count = 0;
    private int out_count = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        inflater.inflate(R.menu.reset, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readOuts();
        updateOutCount();

        View strikeIncrementButton = findViewById(R.id.btnStrike);
        strikeIncrementButton.setOnClickListener(this);

        View ballIncrementButton = findViewById(R.id.btnBall);
        ballIncrementButton.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStrike:
                strike_count++;
                updateStrikeCount();
                if (strike_count == 3) {
                    // Builder is an inner class so we have to qualify it
                    // with its outer class: AlertDialog
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
                break;
            case R.id.btnBall:
                ball_count++;
                updateBallCount();
                if (ball_count == 4) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Too many balls");
                    builder.setMessage("Batter walks.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Okay.", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            resetBalls();

                        }
                    });
                    builder.show();

                }
                break;
        }
    }

    public void resetBalls() {
        strike_count = 0;
        ball_count = 0;
        updateStrikeCount();
        updateBallCount();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
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

    public void viewAbout() { //Unnecessary for the scope of this project, but will be useful in the future
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }

    public void addOuts() {
        //Saving the outs in the shared pref folder.
        out_count++;
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("totalOuts", out_count);
        editor.commit();
        updateOutCount();
    }

    public void readOuts() {
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        out_count = prefs.getInt("totalOuts", 0); //0 is the default value
    }

}
