package com.dragonball.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.GridLayout;
import android.graphics.drawable.GradientDrawable;

public class MainActivity extends Activity {

    TextView display;
    String current = "";
    String operator = "";
    double first = 0;

    int orange = Color.rgb(255, 145, 0);
    int dark = Color.rgb(20, 20, 28);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(18, 25, 18, 18);
        root.setBackgroundColor(dark);

        TextView title = new TextView(this);
        title.setText("🐉 DRAGON BALL");
        title.setTextSize(25);
        title.setTextColor(orange);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("ULTIMATE CALCULATOR");
        subtitle.setTextColor(Color.LTGRAY);
        subtitle.setTextSize(12);
        subtitle.setGravity(Gravity.CENTER);
        root.addView(subtitle);

        display = new TextView(this);
        display.setText("0");
        display.setTextColor(Color.WHITE);
        display.setTextSize(38);
        display.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        display.setPadding(15, 20, 15, 20);

        LinearLayout.LayoutParams dp =
            new LinearLayout.LayoutParams(-1, 0, 1);

        dp.setMargins(0, 25, 0, 20);
        root.addView(display, dp);

        String[][] keys = {
            {"C", "DEL", "%", "÷"},
            {"7", "8", "9", "×"},
            {"4", "5", "6", "−"},
            {"1", "2", "3", "+"},
            {"00", "0", ".", "="}
        };

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(4);
        grid.setRowCount(5);

        for (String[] row : keys) {
            for (String key : row) {
                Button button = new Button(this);
                button.setText(key);
                button.setTextSize(20);
                button.setTypeface(null, Typeface.BOLD);
                button.setTextColor(Color.WHITE);

                GradientDrawable shape = new GradientDrawable();
                shape.setCornerRadius(25);

                if (key.matches("[0-9.]+")) {
                    shape.setColor(Color.rgb(45, 45, 58));
                } else {
                    shape.setColor(orange);
                    button.setTextColor(Color.BLACK);
                }

                button.setBackground(shape);

                GridLayout.LayoutParams params =
                    new GridLayout.LayoutParams();

                params.width = 0;
                params.height = 0;
                params.columnSpec = GridLayout.spec(
                    GridLayout.UNDEFINED, 1f);
                params.rowSpec = GridLayout.spec(
                    GridLayout.UNDEFINED, 1f);

                params.setMargins(5, 5, 5, 5);
                grid.addView(button, params);

                button.setOnClickListener(v -> press(key));
            }
        }

        root.addView(grid, new LinearLayout.LayoutParams(-1, 0, 3));

        setContentView(root);
    }

    void press(String key) {

        if (key.equals("C")) {
            current = "";
            operator = "";
            first = 0;
            display.setText("0");
            return;
        }

        if (key.equals("DEL")) {
            if (!current.isEmpty()) {
                current = current.substring(0, current.length() - 1);
            }
            display.setText(current.isEmpty() ? "0" : current);
            return;
        }

        if (key.equals("=")) {
            calculate();
            return;
        }

        if (key.equals("%")) {
            if (!current.isEmpty()) {
                double value = Double.parseDouble(current) / 100;
                current = String.valueOf(value);
                display.setText(current);
            }
            return;
        }

        if ("+−×÷".contains(key)) {
            if (!current.isEmpty()) {
                first = Double.parseDouble(current);
                operator = key;
                current = "";
            }
            return;
        }

        if (key.equals(".")) {
            if (current.contains(".")) return;
            if (current.isEmpty()) current = "0";
        }

        current += key;
        display.setText(current);
    }

    void calculate() {
        if (current.isEmpty() || operator.isEmpty()) return;

        double second = Double.parseDouble(current);
        double result = 0;

        switch (operator) {
            case "+": result = first + second; break;
            case "−": result = first - second; break;
            case "×": result = first * second; break;
            case "÷":
                if (second == 0) {
                    display.setText("Error");
                    current = "";
                    operator = "";
                    return;
                }
                result = first / second;
                break;
        }

        current = String.valueOf(result);
        display.setText(current);
        operator = "";
    }
          }
