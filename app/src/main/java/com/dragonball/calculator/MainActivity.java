package com.dragonball.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.widget.GridLayout;

public class MainActivity extends Activity {

    TextView display;
    String current = "";
    double firstNumber = 0;
    String operator = "";
    boolean newNumber = true;

    int orange = Color.rgb(255, 135, 20);
    int blue = Color.rgb(20, 110, 220);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(12, 12, 12, 12);
        root.setBackgroundColor(Color.rgb(12, 15, 30));

        // خلفية Dragon Ball
        FrameLayout header = new FrameLayout(this);

        ImageView background = new ImageView(this);
        int headerId = getResources().getIdentifier(
                "dragon_header", "drawable", getPackageName());

        if (headerId != 0) {
            background.setImageResource(headerId);
        }

        background.setScaleType(ImageView.ScaleType.CENTER_CROP);
        header.addView(background,
                new FrameLayout.LayoutParams(-1, -1));

        TextView title = new TextView(this);
        title.setText("DRAGON BALL");
        title.setTextColor(Color.WHITE);
        title.setTextSize(28);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setShadowLayer(12, 0, 0, orange);

        header.addView(title,
                new FrameLayout.LayoutParams(-1, -1));

        root.addView(header,
                new LinearLayout.LayoutParams(-1, 150));

        // شاشة الآلة الحاسبة
        display = new TextView(this);
        display.setText("0");
        display.setTextSize(34);
        display.setTextColor(Color.WHITE);
        display.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        display.setPadding(15, 10, 15, 10);
        display.setBackground(makeBackground(
                Color.rgb(20, 25, 45), orange));

        LinearLayout.LayoutParams displayParams =
                new LinearLayout.LayoutParams(-1, 85);

        displayParams.setMargins(0, 15, 0, 15);
        root.addView(display, displayParams);

        // لوحة الأزرار
        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(4);
        grid.setRowCount(4);

        String[] buttons = {
                "7", "8", "9", "÷",
                "4", "5", "6", "×",
                "1", "2", "3", "-",
                "C", "0", "=", "+"
        };

        for (String text : buttons) {

            FrameLayout button = new FrameLayout(this);

            int color = isOperator(text)
                    ? blue : orange;

            button.setBackground(makeBackground(
                    Color.rgb(25, 30, 50), color));

            GridLayout.LayoutParams params =
                    new GridLayout.LayoutParams();

            params.width = 0;
            params.height = 0;
            params.columnSpec =
                    GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.rowSpec =
                    GridLayout.spec(GridLayout.UNDEFINED, 1f);

            params.setMargins(5, 5, 5, 5);

            ImageView character = new ImageView(this);

            if (text.matches("[0-9]")) {

                int imageId = getResources().getIdentifier(
                        "char_" + text,
                        "drawable",
                        getPackageName());

                if (imageId != 0) {
                    character.setImageResource(imageId);
                }

                character.setScaleType(
                        ImageView.ScaleType.CENTER_CROP);

                button.addView(character,
                        new FrameLayout.LayoutParams(-1, -1));
            }

            TextView label = new TextView(this);
            label.setText(text);
            label.setTextSize(25);
            label.setTextColor(Color.WHITE);
            label.setTypeface(null, Typeface.BOLD);
            label.setGravity(Gravity.CENTER);
            label.setShadowLayer(8, 2, 2, Color.BLACK);

            FrameLayout.LayoutParams labelParams =
                    new FrameLayout.LayoutParams(-1, -1);

            button.addView(label, labelParams);

            button.setElevation(8);

            button.setOnClickListener(v -> {

                v.animate()
                        .scaleX(0.90f)
                        .scaleY(0.90f)
                        .setDuration(80)
                        .withEndAction(() ->
                                v.animate()
                                        .scaleX(1f)
                                        .scaleY(1f)
                                        .setDuration(100)
                                        .start())
                        .start();

                handleClick(text);
            });

            grid.addView(button, params);
        }

        root.addView(grid,
                new LinearLayout.LayoutParams(-1, 0, 1));

        TextView footer = new TextView(this);
        footer.setText("POWERED BY DRAGON BALL ⚡");
        footer.setTextColor(orange);
        footer.setGravity(Gravity.CENTER);
        footer.setTextSize(13);
        footer.setPadding(0, 10, 0, 5);

        root.addView(footer);

        setContentView(root);
    }

    private GradientDrawable makeBackground(
            int backgroundColor, int borderColor) {

        GradientDrawable shape = new GradientDrawable();

        shape.setColor(backgroundColor);
        shape.setCornerRadius(18);
        shape.setStroke(3, borderColor);

        return shape;
    }

    private boolean isOperator(String text) {
        return text.equals("+")
                || text.equals("-")
                || text.equals("×")
                || text.equals("÷")
                || text.equals("=");
                }

    private void handleClick(String value) {

        if (value.matches("[0-9]")) {

            if (newNumber) {
                current = value;
                newNumber = false;
            } else {
                current += value;
            }

            display.setText(current);
        }

        else if (value.equals("C")) {

            current = "";
            operator = "";
            firstNumber = 0;
            newNumber = true;
            display.setText("0");
        }

        else if (value.equals("=")) {

            if (!operator.isEmpty() && !current.isEmpty()) {

                double secondNumber =
                        Double.parseDouble(current);

                double result = 0;

                switch (operator) {
                    case "+":
                        result = firstNumber + secondNumber;
                        break;

                    case "-":
                        result = firstNumber - secondNumber;
                        break;

                    case "×":
                        result = firstNumber * secondNumber;
                        break;

                    case "÷":
                        if (secondNumber == 0) {
                            display.setText("لا يمكن القسمة على صفر");
                            current = "";
                            operator = "";
                            newNumber = true;
                            return;
                        }
                        result = firstNumber / secondNumber;
                        break;
                }

                current = formatResult(result);
                display.setText(current);

                operator = "";
                newNumber = true;
            }
        }

        else if (isOperator(value)) {

            if (!current.isEmpty()) {
                firstNumber = Double.parseDouble(current);
                operator = value;
                newNumber = true;
            }
        }
    }

    private String formatResult(double result) {

        if (result == (long) result) {
            return String.valueOf((long) result);
        }

        return String.valueOf(result);
    }
                                       }
