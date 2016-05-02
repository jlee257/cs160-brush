package javis.wearsyncservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Calendar;

public class DashboardWeek extends SlidingMenuActivity {

    final String SETTINGS_FILE = "BRUSH_SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.dashboard_week);
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

//        SharedPreferences data = getPreferences(0); //for storing data
//        SharedPreferences.Editor editor = data.edit(); //to edit data

        SharedPreferences.Editor editor = getSharedPreferences("SCORES", MODE_PRIVATE).edit();
        SharedPreferences data = getSharedPreferences("SCORES", MODE_PRIVATE);



        String day_score = data.getString("score_day", "0_TBD_0_0_0");

        //if day of the week is Sunday, reset the data for the week
        Calendar c = Calendar.getInstance();
        int val = c.get(Calendar.DAY_OF_WEEK);
        String day = days[val-1];
        if (day.equals("Sunday")) {
            resetData(data);
        }
//        Log.d("LISTENER", ""+ data.getFloat(day, 0f));


        //dummy data, get real data
//        int r = data.getInt(day, 0);
//        data.getInt("Monday", 0);
//        data.getInt("Tuesday", 0);
//        data.getInt("Wednesday", 0);
//        data.getInt("Thursday", 0);
//        data.getInt("Friday", 0);
//        data.getInt("Saturday", 0);

//        editor.apply();

        if (savedInstanceState!=null)
        {
            savedInstanceState.putString("TITLE", "DASHBOARD");
            savedInstanceState.putInt("LAYOUT", R.layout.dashboard_week);
            savedInstanceState.putInt("LAYOUT_ID", R.id.dashboard_week_rel);
            super.onCreate(savedInstanceState);
        }
        else
        {
            Bundle b = new Bundle();
            b.putString("TITLE", "DASHBOARD");
            b.putInt("LAYOUT", R.layout.dashboard_week);
            b.putInt("LAYOUT_ID", R.id.dashboard_week_rel); //id of top level Relative/Linear etc Layout
            super.onCreate(b);
        }

        Bitmap bmp = getImageBitmap(this, "profile","BMP");;
        ImageView img = (ImageView) findViewById(R.id.cropped_final);
        img.setImageDrawable(new RoundedAvatarDrawable(bmp));

        SharedPreferences getSettings = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
        String name = getSettings.getString("FIRST_NAME", "No Name");
        Log.d("Settingszzzs Saved", name);
        if (name.equals(""))
        {
            name = "Your child";
        }
        TextView mainNameView = (TextView) findViewById(R.id.NameWeek);
        mainNameView.setText(name);

        //this is beginning of plot stuff
        XYPlot mySimpleXYPlot;

        //initialized plot reference
        mySimpleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
//        mySimpleXYPlot.setBorderStyle(Plot.BorderStyle.NONE, null, null); //makes legend white instead of gray
        mySimpleXYPlot.setPlotMargins(0, 0, 0, 0);
        mySimpleXYPlot.setPlotPadding(0, 0, 0, 0);
        mySimpleXYPlot.setGridPadding(0, 10, 5, 0);
        mySimpleXYPlot.setBackgroundColor(getResources().getColor(R.color.purple)); //doesn't seem to work


        //create array of y-values to plot
        Number[] scores = {data.getFloat("Sunday", 0f), data.getFloat("Monday", 0f), data.getFloat("Tuesday", 0f), data.getFloat("Wednesday", 0f), data.getFloat("Thursday", 0f), data.getFloat("Friday", 0), data.getFloat("Saturday", 0f)};
//        Number[] numbers = {20, 50, 80, 60, 75, 80, 90}; //index corresponds to x-value
        //figure out how to write to memory

        mySimpleXYPlot.getGraphWidget().getBackgroundPaint().setColor(Color.rgb(155, 24, 155));// sets axis colors
        mySimpleXYPlot.getGraphWidget().getGridBackgroundPaint().setColor(Color.rgb(155, 24, 155)); //sets the graph background color

        // Domain
        mySimpleXYPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, scores.length);
        mySimpleXYPlot.setDomainValueFormat(new DecimalFormat("0"));
        mySimpleXYPlot.setDomainStepValue(1);

        //Range
        mySimpleXYPlot.setRangeBoundaries(0, 100, BoundaryMode.FIXED);
        mySimpleXYPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, scores.length);
        mySimpleXYPlot.setRangeStepValue(10);
        //mySimpleXYPlot.setRangeStep(XYStepMode.SUBDIVIDE, values.length);
        mySimpleXYPlot.setRangeValueFormat(new DecimalFormat("0"));

        //Remove legend
        mySimpleXYPlot.getLayoutManager().remove(mySimpleXYPlot.getLegendWidget());
//        mySimpleXYPlot.getLayoutManager().remove(mySimpleXYPlot.getDomainLabelWidget());
//        mySimpleXYPlot.getLayoutManager().remove(mySimpleXYPlot.getRangeLabelWidget());
//        mySimpleXYPlot.getLayoutManager().remove(mySimpleXYPlot.getTitleWidget());

        // Turn the above arrays into XYSeries':
//        XYSeries series1 = new SimpleXYSeries(
//                Arrays.asList(days),
//                Arrays.asList(numbers),
//                "Series1");                             // Set the display title of the series

        //turn above numbers to XYSeries
        XYSeries series = new SimpleXYSeries(Arrays.asList(scores), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series");

        //create a formatter to use for drawing a series using LineAndPointRenderer

        LineAndPointFormatter seriesFormat = new LineAndPointFormatter(
                Color.WHITE,                   //line color
                Color.WHITE,                   //point color
                null,                                   //fill color (none)
                null);  //text color

        //add a new series to the xy plot
        mySimpleXYPlot.addSeries(series, seriesFormat);

        //customize x-axis labels
        mySimpleXYPlot.getGraphWidget().setDomainValueFormat(new GraphXLabelFormat());

        //remove grid lines (this code from a google groups conversation, by Sergii Nezdolii)
        mySimpleXYPlot.getGraphWidget().getDomainGridLinePaint().setColor(Color.TRANSPARENT);
        mySimpleXYPlot.getGraphWidget().getRangeGridLinePaint().setColor(Color.TRANSPARENT);





        //reduce the number of range labels
//        mySimpleXYPlot.setTicksPerRangeLabel(3);
//        mySimpleXYPlot.setTicksPerDomainLabel(3);
        //this is end of plot stuff

    }

    //this code gotten from stackOverflow user Aegonis

    class GraphXLabelFormat extends Format {

        String[] LABELS = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; //set these to be days

        @Override
        public StringBuffer format(Object object, StringBuffer buffer, FieldPosition field) {
            int parsedInt = Math.round(Float.parseFloat(object.toString()));
            String labelString = LABELS[parsedInt];
            buffer.append(labelString);
            return buffer;
        }

        @Override
        public Object parseObject(String string, ParsePosition position) {
            return java.util.Arrays.asList(LABELS).indexOf(string);
        }
    }

    public void resetData(SharedPreferences data) {
        SharedPreferences.Editor editor = data.edit();
        editor.putInt("Sun", 0);
        editor.putInt("Mon", 0);
        editor.putInt("Tue", 0);
        editor.putInt("Wed", 0);
        editor.putInt("Thu", 0);
        editor.putInt("Fri", 0);
        editor.putInt("Sat", 0);
        editor.apply();
    }

    public Bitmap getImageBitmap(Context context,String name,String extension){
        name=name+"."+extension;
        try{
            FileInputStream fis = context.openFileInput(name);
            Bitmap b = BitmapFactory.decodeStream(fis);
            fis.close();
            return b;
        }
        catch(Exception e){
        }
        //return null;
        //If there is no selected image displays the fox icon
        Bitmap Icon = BitmapFactory.decodeResource(getResources(), R.drawable.foxicon);
        return Icon;
    }

    public void toDashboardDay(View v) {
        Intent next = new Intent(this, DashboardDay.class);
        startActivity(next);
    }

    public void toDashboardWeek(View v) {
        Intent next = new Intent(this, DashboardWeek.class);
        startActivity(next);
    }
}
