package fpt.edu.vn.skincareshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.models.Routine;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder> {

    private Context context;
    private List<Routine.Step> stepList;

    public RoutineAdapter(Context context, List<Routine.Step> stepList) {
        this.context = context;
        this.stepList = stepList;
    }

    @NonNull
    @Override
    public RoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_routine_step, parent, false);
        return new RoutineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineViewHolder holder, int position) {
        Routine.Step step = stepList.get(position);
        holder.tvStepTitle.setText("Bước " + step.getStepNumber() + ": " + step.getStepName());
        holder.tvStepDescription.setText(step.getDescription());
    }

    @Override
    public int getItemCount() {
        return stepList != null ? stepList.size() : 0;
    }

    static class RoutineViewHolder extends RecyclerView.ViewHolder {
        TextView tvStepTitle, tvStepDescription;

        public RoutineViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStepTitle = itemView.findViewById(R.id.tvStepTitle);
            tvStepDescription = itemView.findViewById(R.id.tvStepDescription);
        }
    }
}
