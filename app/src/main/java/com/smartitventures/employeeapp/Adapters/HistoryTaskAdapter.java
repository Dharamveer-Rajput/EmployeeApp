package com.smartitventures.employeeapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartitventures.employeeapp.R;
import com.smartitventures.employeeapp.Response.Response.TaskHistroy.HistoryPayload;

import java.util.ArrayList;

/**
 * Created by dharamveer on 21/9/17.
 */

public class HistoryTaskAdapter extends RecyclerView.Adapter<HistoryTaskAdapter.MyViewHolder> {

    private ArrayList<HistoryPayload> historyPayloadArrayList;
    private Context context;

    public HistoryTaskAdapter(Context context,ArrayList<HistoryPayload> historyPayloadArrayList) {
        this.historyPayloadArrayList = historyPayloadArrayList;
        this.context = context;
    }

    @Override
    public HistoryTaskAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_task_row, parent, false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(HistoryTaskAdapter.MyViewHolder holder, int position) {

        HistoryPayload historyPayload = historyPayloadArrayList.get(position);


        holder.tvTitleHis.setText(historyPayload.getTitle());
//        holder.tvDescHis.setText(historyPayload.getDescription());
        holder.tvAssignToHis.setText(historyPayload.getAssignDate());
        holder.tvDeadlineHis.setText(historyPayload.getDeadline());


    }

    @Override
    public int getItemCount() {
        return historyPayloadArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView tvTitleHis,tvDescHis,tvDeadlineHis,tvAssignToHis;

        public MyViewHolder(View itemView) {


            super(itemView);

            tvTitleHis = itemView.findViewById(R.id.tvTitleHis);
            tvAssignToHis = itemView.findViewById(R.id.tvAssignDateHis);
            tvDeadlineHis = itemView.findViewById(R.id.tvDeadlineHis);


        }
    }
}
