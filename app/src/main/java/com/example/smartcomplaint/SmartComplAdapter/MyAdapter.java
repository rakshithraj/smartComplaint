package com.example.smartcomplaint.SmartComplAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.smartcomplaint.R;
import com.example.smartcomplaint.ComplaintDetailedActivity;
import com.example.smartcomplaint.dao.ComplaintInfo;

import java.util.ArrayList;

/**
 * Created by Rakshith on 10/13/2015.
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    Activity mActivity;
    private ArrayList<ComplaintInfo> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tvComplaint;
        Button btStatus;

        public ViewHolder(View v) {
            super(v);
            tvComplaint = (TextView) v.findViewById(R.id.tvComplaint);
            btStatus = (Button) v.findViewById(R.id.btStatus);
            view = v;
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }

    public MyAdapter(ArrayList<ComplaintInfo> myDataset, Activity activity) {
        mDataset = myDataset;
        mActivity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.complaint_row, parent, false);

            vh = new ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).tvComplaint.setText(mDataset.get(position).getMessage());
            switch (mDataset.get(position).getStatus()) {
                case "0":
                    ;
                    ((ViewHolder) holder).btStatus.setBackgroundColor(((ViewHolder) holder).btStatus.getContext().getResources().getColor(R.color.red));
                    break;
                case "1":
                    ((ViewHolder) holder).btStatus.setBackgroundColor(((ViewHolder) holder).btStatus.getContext().getResources().getColor(R.color.orange));
                    break;
                case "2":
                    ((ViewHolder) holder).btStatus.setBackgroundColor(((ViewHolder) holder).btStatus.getContext().getResources().getColor(R.color.green));
                    break;
            }
            ((ViewHolder) holder).view.setOnClickListener(new onItemClickListner(position, mDataset.get(position)));
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private class onItemClickListner implements View.OnClickListener {
        int position;
        ComplaintInfo complaintInfo;

        public onItemClickListner(int position, ComplaintInfo complaintInfo) {
            this.position = position;
            this.complaintInfo = complaintInfo;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("message", complaintInfo.getMessage());
            bundle.putString("location", complaintInfo.getMyLocation());
            bundle.putString("image", complaintInfo.getImage());
            Intent intent = new Intent(mActivity, ComplaintDetailedActivity.class);
            intent.putExtras(bundle);
            mActivity.startActivity(intent);
        }
    }
}