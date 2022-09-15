package aid.distaid.aidclockdiff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private NumberPicker hourUp;
    private NumberPicker minuteUp;
    private NumberPicker hourBottom;
    private NumberPicker minuteBottom;

    private TextView resultText;

    private FloatingActionButton themeButton;

    private Locale locale;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locale = Locale.getDefault();
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        prepareTheme();
        getWidgets();
        setNumerics();
    }

    private void getWidgets() {
        hourUp = findViewById(R.id.hourUp);
        minuteUp = findViewById(R.id.minuteUp);
        hourBottom = findViewById(R.id.hourBottom);
        minuteBottom = findViewById(R.id.minuteBottom);

        resultText = findViewById(R.id.resultText);

        themeButton = findViewById(R.id.themeButton);
    }

    private void setNumerics() {
        hourUp.setMaxValue(23);
        hourBottom.setMaxValue(23);
        hourUp.setMinValue(0);
        hourBottom.setMinValue(0);

        minuteUp.setMaxValue(59);
        minuteBottom.setMaxValue(59);
        minuteUp.setMinValue(0);
        minuteBottom.setMinValue(0);

        hourUp.setOnValueChangedListener(this::calculateTime);
        hourBottom.setOnValueChangedListener(this::calculateTime);
        minuteUp.setOnValueChangedListener(this::calculateTime);
        minuteBottom.setOnValueChangedListener(this::calculateTime);

        themeButton.setOnClickListener(this::changeTheme);
    }

    private void calculateTime(NumberPicker picker, int i, int i1) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", locale);

        try {
            Date dateUp = format.parse(String.format(locale, "%d:%d", hourUp.getValue(), minuteUp.getValue()));
            Date dateBottom = format.parse(String.format(locale, "%d:%d", hourBottom.getValue(), minuteBottom.getValue()));

            if (dateUp != null && dateBottom != null) {
                long difference = Math.abs(dateBottom.getTime() - dateUp.getTime());
                String rh = String.format(locale, "%d", difference / (60 * 1000) / 60);
                String rm = String.format(locale, "%d", difference / (60 * 1000) % 60);

                if (rh.length() == 1) {
                    rh = "0" + rh;
                }
                if (rm.length() == 1) {
                    rm = "0" + rm;
                }

                resultText.setText(String.format(locale, "%s:%s", rh, rm));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void changeTheme(View view) {
        int dark = AppCompatDelegate.getDefaultNightMode();
        if (dark == AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            sp.edit().putString("app_theme", "dark_theme").apply();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            sp.edit().putString("app_theme", "light_theme").apply();
        }
    }

    private void prepareTheme() {
        switch(sp.getString("app_theme", "light_theme")) {
            case "dark_theme":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "light_theme":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
    }
}