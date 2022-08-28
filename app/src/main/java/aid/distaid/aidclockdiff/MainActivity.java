package aid.distaid.aidclockdiff;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TextView;

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

    private Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locale = Locale.getDefault();

        getWidgets();
        setNumerics();
    }

    private void getWidgets() {
        hourUp = findViewById(R.id.hourUp);
        minuteUp = findViewById(R.id.minuteUp);
        hourBottom = findViewById(R.id.hourBottom);
        minuteBottom = findViewById(R.id.minuteBottom);

        resultText = findViewById(R.id.resultText);
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
    }

    private void calculateTime(NumberPicker picker, int i, int i1) {
        String hourUpStringValue = String.format(locale, "%d", hourUp.getValue());
        String hourBottomStringValue = String.format(locale, "%d", hourBottom.getValue());
        String minuteUpStringValue = String.format(locale, "%d", minuteUp.getValue());
        String minuteBottomStringValue = String.format(locale, "%d", minuteBottom.getValue());

        SimpleDateFormat format = new SimpleDateFormat("HH:mm", locale);

        try {
            Date dateUp = format.parse(String.format(locale, "%s:%s", hourUpStringValue, minuteUpStringValue));
            Date dateBottom = format.parse(String.format(locale, "%s:%s", hourBottomStringValue, minuteBottomStringValue));

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

                resultText.setText(String.format("%s:%s", rh, rm));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}