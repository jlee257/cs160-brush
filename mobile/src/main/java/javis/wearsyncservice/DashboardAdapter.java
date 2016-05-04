package javis.wearsyncservice;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by AshleyTheMagnificant on 4/23/16.
 */
public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private static String[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public TextView mTimeOfDay;
        public TextView circularBar;
        public TextView verticalBar;
        public TextView horizontalBar;
        public CardView cardView;

        public ViewHolder(final View v){
            super(v);
            mTextView = (TextView) v.findViewById(R.id.overall_score);
            mTimeOfDay = (TextView) v.findViewById(R.id.time_of_day);
            circularBar = (TextView) v.findViewById(R.id.bar1);
            verticalBar = (TextView) v.findViewById(R.id.bar2);
            horizontalBar = (TextView) v.findViewById(R.id.bar3);
            cardView = (CardView) v.findViewById(R.id.dashboard_score_view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DashboardAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DashboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_score_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.mTextView = (TextView) v.findViewById(R.id.overall_score);
        vh.mTimeOfDay = (TextView) v.findViewById(R.id.time_of_day);
        vh.circularBar = (TextView) v.findViewById(R.id.bar1);
        vh.verticalBar = (TextView) v.findViewById(R.id.bar2);
        vh.horizontalBar = (TextView) v.findViewById(R.id.bar3);
        vh.cardView = (CardView) v.findViewById(R.id.dashboard_score_view);
        return vh;
    }

    //     Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String[] vals = mDataset[position].split("_"); // [timeOfDay_overallscore_circular_vertical_horizontal]
        Log.d("Adapta", "pso = " + position);

        if (position % 2 == 1) { // odd
            holder.cardView.setCardBackgroundColor(holder.cardView.getResources().getColor(R.color.lightblue));
            holder.mTimeOfDay.setText("Evening");
        } else {
            holder.cardView.setCardBackgroundColor(holder.cardView.getResources().getColor(R.color.periwinkle));
            holder.mTimeOfDay.setText("Morning");

        }
        // TODO Time of Day
//        if (Integer.parseInt(vals[0]) == 1) {
        if(vals[0].equals("AM")) {
            holder.mTimeOfDay.setText("Morning");
        } else if(vals[0].equals("PM")){
            holder.mTimeOfDay.setText("Evening");
        }
//        holder.mTimeOfDay ==

        holder.mTextView.setText(String.valueOf((int) Float.parseFloat(vals[1])));
        // bar scores
        float cs = Float.parseFloat(vals[2]);
        float vs = Float.parseFloat(vals[3]);
        float hs = Float.parseFloat(vals[4]);
        int full_width = holder.circularBar.getMaxWidth();

        float new_width1 = full_width * ((float) cs/100f);
        float new_width2 = full_width * ((float) vs/100f);
        float new_width3 = full_width * ((float) hs/100f);

        Log.d("Dash Adap", "new vals =  " + new_width1 + " , " + new_width2 + " , " + new_width3);

        holder.circularBar.setWidth((int) new_width1);
        holder.verticalBar.setWidth((int)new_width2);
        holder.horizontalBar.setWidth((int)new_width3);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}



