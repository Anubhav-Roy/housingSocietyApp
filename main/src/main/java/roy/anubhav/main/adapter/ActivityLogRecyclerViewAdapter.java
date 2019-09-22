package roy.anubhav.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import roy.anubhav.core.db.entity.ActivityLog;
import roy.anubhav.main.R;

import static roy.anubhav.core.utils.CommonUtils.formatDate;

public class ActivityLogRecyclerViewAdapter extends  RecyclerView.Adapter<ActivityLogRecyclerViewAdapter.ViewHolder> {

    private List<ActivityLog> logs ;
    private Context mContext;

    public ActivityLogRecyclerViewAdapter(Context mContext){
        this.mContext = mContext;
    }

    public void setData( List<ActivityLog> logs ){
        this.logs = logs;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityLog log = logs.get(position);

        if(log!=null){
            holder.description.setText(logs.get(position).activityDescription);

            holder.timestamp.setText(formatDate(mContext,logs.get(position).timestamp));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_log_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(logs!=null)
            return logs.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView description , timestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.description);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
    }

}
