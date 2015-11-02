package com.example.smartcomplaint;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.example.smartcomplaint.SmartComplAdapter.MyAdapter;
import com.example.smartcomplaint.SmartComplaintInterface.ServerResponse;
import com.example.smartcomplaint.dao.ComplaintInfo;
import com.example.smartcomplaint.dao.ComplaintInfoList;
import com.example.smartcomplaint.utility.EndlessRecyclerOnScrollListener;
import com.example.smartcomplaint.utility.smartcomplaintConfig;
import com.example.smartcomplaint.webservice.ConnectWebService;
import com.google.gson.Gson;

import java.util.ArrayList;

public final class AllStatusFragment extends Fragment implements ServerResponse {
    private static final String KEY_CONTENT = "AllStatusFragment:Content";
    RecyclerView mRecyclerView;
    ProgressBar progressBar;
    Context context;
    ArrayList<ComplaintInfo> myDataset = new ArrayList<ComplaintInfo>();
    MyAdapter mAdapter;
    private Handler handler;
    ComplaintInfo complaintInfo;
    View view;
    int department;
    String dep = "";
    int start = 0;
    public boolean loading = false;

    public static AllStatusFragment newInstance(int department) {
        AllStatusFragment fragment = new AllStatusFragment();
        fragment.setDepartment(department);

        return fragment;
    }

    // private String mContent = "???";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }*/


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.all_status, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        if (myDataset.size() <= 0) {
            if(!getLoading())
            setData();
        } else
            progressBar.setVisibility(View.GONE);

    }

    private void setData() {
        String url = smartcomplaintConfig.ALL_STATUS_URL + "&tablename=" + dep + "&start=" + start + "&end=" + (start + 10);

        ConnectWebService connectWebService=new ConnectWebService();
        connectWebService.setOnServerResponse(this);
        connectWebService.stringGetRequest(url, this.getActivity());


        setLoading(true);
    }

    private void initialize() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        handler = new Handler();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(myDataset,this.getActivity());
        mRecyclerView.setAdapter(mAdapter);

        //mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                myDataset.add(null);
                mAdapter.notifyItemInserted(myDataset.size());
                if(!getLoading())
                setData();
                //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();

                System.out.println("load");
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //   outState.putString(KEY_CONTENT, mContent);
    }

    public void setDepartment(int department) {
        this.department = department;

        switch (department) {
            case AllStatusActivity.MNPO:
                dep = "mnpotable";
                break;
            case AllStatusActivity.MESCOM:
                dep = "mescomtable";
                break;
            case AllStatusActivity.TRAFFIC:
                dep = "traffictable";
                break;
            case AllStatusActivity.POLICE:
                dep = "policetable";
                break;
            //mnpotable
        }
    }

    @Override
    public void onServerResponse(String result) {
        if (myDataset.size() > 0) {
            myDataset.remove(myDataset.size() - 1);
            mAdapter.notifyItemRemoved(myDataset.size());
        }

        progressBar.setVisibility(View.GONE);

        if (result != null) {
            Gson gson = new Gson();
            ComplaintInfoList complaintInfoList = gson.fromJson(result, ComplaintInfoList.class);

                myDataset.addAll(complaintInfoList.getComplaints());
                start = myDataset.size();

            mAdapter.notifyDataSetChanged();

            setLoading(false);
        }

    }

    @Override
    public void onServerError(String result) {

    }

    @Override
    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    @Override
    public boolean getLoading() {
        return loading;
    }
}
