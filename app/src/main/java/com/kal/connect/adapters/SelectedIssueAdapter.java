package com.kal.connect.adapters;

import android.content.Context;
//import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kal.connect.R;
import com.kal.connect.models.IssuesModel;
import com.kal.connect.modules.dashboard.BookAppointment.HomeFragment;
import com.robertlevonyan.views.chip.Chip;
import com.robertlevonyan.views.chip.OnCloseClickListener;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class SelectedIssueAdapter extends RecyclerView.Adapter<SelectedIssueAdapter.IssuesViewHolder>{
    private List<IssuesModel> horizontalIssuesist;
    Context context;
    HomeFragment homeFragment;

    public SelectedIssueAdapter(List<IssuesModel> horizontalIssuesist, Context context, HomeFragment homeFragment){
        this.horizontalIssuesist= horizontalIssuesist;
        this.context = context;
        this.homeFragment = homeFragment;
    }

    @Override
    public IssuesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_issue_item, parent, false);
        IssuesViewHolder isv = new IssuesViewHolder(groceryProductView);
        return isv;
    }

    @Override
    public void onBindViewHolder(IssuesViewHolder holder, final int position) {
//        holder.imageView.setImageResource(horizontalIssuesist.get(position)());
        holder.selectedIssueChip.setChipText(horizontalIssuesist.get(position).getTitle());

        if(homeFragment ==null)
            return;
        holder.selectedIssueChip.setOnCloseClickListener(new OnCloseClickListener() {
            @Override
            public void onCloseClick(View v) {
                homeFragment.removeSelectedIssue(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return horizontalIssuesist.size();
    }

    public class IssuesViewHolder extends RecyclerView.ViewHolder {
        Chip selectedIssueChip;
        public IssuesViewHolder(View view) {
            super(view);
            selectedIssueChip = (Chip) view.findViewById(R.id.selected_issue_chip);
            if(homeFragment !=null){
                selectedIssueChip.setClosable(true);
            }

        }
    }
}