package fpt.edu.vn.skincareshop.ui.routine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.models.Routine;
import fpt.edu.vn.skincareshop.services.RoutineService;
import fpt.edu.vn.skincareshop.ui.components.SkincareChatPopup;
import fpt.edu.vn.skincareshop.utils.SharedPrefManager;

public class RoutineFragment extends Fragment {

    private TextView tvSkinType, tvMorningSteps, tvNightSteps;
    private ProgressBar progressBar;
    private FloatingActionButton fabSkincareChat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine, container, false);

        tvSkinType = view.findViewById(R.id.tvSkinType);
        tvMorningSteps = view.findViewById(R.id.tvRoutineMorning);
        tvNightSteps = view.findViewById(R.id.tvRoutineNight);
        progressBar = view.findViewById(R.id.progressRoutine);
        fabSkincareChat = view.findViewById(R.id.fabSkincareChat);

        fabSkincareChat.setOnClickListener(v -> {
            SkincareChatPopup popup = new SkincareChatPopup();
            popup.show(getParentFragmentManager(), "SkincareChatPopup");
        });

        String skinType = SharedPrefManager.getInstance(getContext()).getSkinType();
        if (skinType == null || skinType.isEmpty()) {
            Toast.makeText(getContext(), "Chưa xác định loại da", Toast.LENGTH_SHORT).show();
        } else {
            tvSkinType.setText("Loại da: " + skinType);
            loadRoutine(skinType);
        }

        return view;
    }

    private void loadRoutine(String skinType) {
        progressBar.setVisibility(View.VISIBLE);

        RoutineService.getRoutineBySkinType(getContext(), skinType, new RoutineService.RoutineCallback() {
            @Override
            public void onSuccess(Routine routine) {
                progressBar.setVisibility(View.GONE);

                StringBuilder morningBuilder = new StringBuilder();
                StringBuilder eveningBuilder = new StringBuilder();

                if (routine.getSteps() != null && routine.getTimeOfDay() != null) {
                    List<Routine.Step> steps = routine.getSteps();
                    String timeOfDay = routine.getTimeOfDay().toLowerCase();

                    for (Routine.Step step : steps) {
                        String formatted = "- " + step.getStepName() + ": " + step.getDescription() + "\n";

                        if (timeOfDay.equals("morning") || timeOfDay.equals("both")) {
                            morningBuilder.append(formatted);
                        }
                        if (timeOfDay.equals("evening") || timeOfDay.equals("both")) {
                            eveningBuilder.append(formatted);
                        }
                    }
                }

                tvMorningSteps.setText("Sáng:\n" + morningBuilder.toString());
                tvNightSteps.setText("Tối:\n" + eveningBuilder.toString());
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Lỗi: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
