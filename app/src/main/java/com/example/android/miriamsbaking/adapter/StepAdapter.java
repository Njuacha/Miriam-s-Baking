package com.example.android.miriamsbaking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.miriamsbaking.R;
import com.example.android.miriamsbaking.database.StepWithId;
import com.example.android.miriamsbaking.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder>{

    private Context mContext;
    private List<Step> mSteps;
    private StepListener mListener;

    public StepAdapter(Context context,StepListener listener){
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.step,parent,false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        String shrtDesciptn = mSteps.get(position).getShortDescription();

        if ( shrtDesciptn != null){
            holder.tv_step.setText(shrtDesciptn);
        }else{
            holder.tv_step.setText(R.string.no_availabe_short_descriptn);
        }

    }

    @Override
    public int getItemCount() {
        if(mSteps == null) return 0;
        else return mSteps.size();
    }

    public void setSteps(List<Step> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

    public interface StepListener{
        void onStepClicked(int stepIndex);
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.tv_step)
        TextView tv_step;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onStepClicked(mSteps.get(getAdapterPosition()).getId());
        }
    }
}
