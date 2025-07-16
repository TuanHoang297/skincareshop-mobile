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

    private TextView tvSkinType, tvMorningSteps, tvNightSteps, tvPrompt;
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
        tvPrompt = view.findViewById(R.id.tvRoutinePrompt); // TextView chứa dòng gợi ý AI chatbox
        progressBar = view.findViewById(R.id.progressRoutine);
        fabSkincareChat = view.findViewById(R.id.fabSkincareChat);

        fabSkincareChat.setOnClickListener(v -> {
            SkincareChatPopup popup = new SkincareChatPopup();
            popup.show(getParentFragmentManager(), "SkincareChatPopup");
        });

        String skinType = SharedPrefManager.getInstance(getContext()).getSkinType();
        if (skinType == null || skinType.isEmpty()) {
            tvPrompt.setVisibility(View.VISIBLE);
            tvSkinType.setVisibility(View.GONE);
            tvMorningSteps.setVisibility(View.GONE);
            tvNightSteps.setVisibility(View.GONE);
        } else {
            tvPrompt.setVisibility(View.GONE);
            tvSkinType.setVisibility(View.VISIBLE);

            String translated = translateSkinType(skinType);
            tvSkinType.setText("Vì da của bạn là: " + translated);

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

                String time = routine.getTimeOfDay() != null ? routine.getTimeOfDay().toLowerCase() : "";

                if (routine.getSteps() != null && !routine.getSteps().isEmpty()) {
                    for (Routine.Step step : routine.getSteps()) {
                        String formatted = "- " + step.getStepName() + ": " + step.getDescription() + "\n";
                        if (time.equals("morning") || time.equals("both")) {
                            morningBuilder.append(formatted);
                        }
                        if (time.equals("evening") || time.equals("both")) {
                            eveningBuilder.append(formatted);
                        }
                    }
                }

                if (morningBuilder.length() > 0) {
                    tvMorningSteps.setVisibility(View.VISIBLE);
                    tvMorningSteps.setText("Sáng:\n" + morningBuilder.toString());
                } else {
                    tvMorningSteps.setVisibility(View.GONE);
                }

                if (eveningBuilder.length() > 0) {
                    tvNightSteps.setVisibility(View.VISIBLE);
                    tvNightSteps.setText("Tối:\n" + eveningBuilder.toString());
                } else {
                    tvNightSteps.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Lỗi: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String translateSkinType(String skinType) {
        switch (skinType.toLowerCase()) {
            case "oily":
                return "da dầu";
            case "dry":
                return "da khô";
            case "normal":
                return "da thường";
            case "combination":
                return "da hỗn hợp";
            default:
                return skinType;
        }
    }
}
