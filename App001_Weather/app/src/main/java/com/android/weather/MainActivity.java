package com.android.weather;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private Typeface weatherFont;
    private TextView cityField;
    private TextView detailsField;
    private TextView currentTemperatureField;
    private TextView humidity_field;
    private TextView presure_field;
    private TextView weatherIcon;
    private TextView updateField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherFont = Typeface.createFromAsset(getAssets(),"fonts/weathericons-regular-webfont.ttf" );
        cityField = (TextView)findViewById(R.id.city_field);
        detailsField = (TextView)findViewById(R.id.details_field);
        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
        humidity_field = (TextView)findViewById(R.id.humidity_field);
        presure_field = (TextView)findViewById(R.id.pressure_field);
        weatherIcon.setTypeface(weatherFont);
        updateField = (TextView)findViewById(R.id.updated_field);

        Function.placeIdTask asynctask = new Function.placeIdTask(new Function.AsyncResponce() {
            @Override
            public void processFinish(String weather_city, String weather_description, String weather_temperature,
                                      String weather_humidity, String weather_presure, String weather_updateOn,
                                      String weather_iconText, String weather_sunrise) {
                cityField.setText(weather_city);
                updateField.setText(weather_updateOn);
                detailsField.setText(weather_description);
                currentTemperatureField.setText(weather_temperature);
                humidity_field.setText("Humidity" + weather_humidity);
                presure_field.setText("Presure" + weather_presure);
                weatherIcon.setText(Html.fromHtml(weather_iconText));
            }
        });
        asynctask.execute("21.007852", "105.819877");
    }
}
