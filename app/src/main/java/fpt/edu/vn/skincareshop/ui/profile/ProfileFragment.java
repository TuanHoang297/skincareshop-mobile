package fpt.edu.vn.skincareshop.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.ui.auth.LoginActivity;
import fpt.edu.vn.skincareshop.utils.SharedPrefManager;

public class ProfileFragment extends Fragment {

    private TextView tvUsername, tvUserId, tvSkinType;
    private Button btnLogout;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvUsername = view.findViewById(R.id.tvProfileUsername);
        tvUserId = view.findViewById(R.id.tvProfileUserId);
        tvSkinType = view.findViewById(R.id.tvProfileSkinType);
        btnLogout = view.findViewById(R.id.btnLogout);
        tabLayout = view.findViewById(R.id.tabLayoutOrders);
        viewPager = view.findViewById(R.id.viewPagerOrders);

        SharedPrefManager pref = SharedPrefManager.getInstance(requireContext());

        tvUsername.setText("Tên: " + pref.getUsername());
        tvUserId.setText("Mã người dùng: " + pref.getUserId());
        String skinType = pref.getSkinType();
        tvSkinType.setText("Loại da: " + (skinType != null ? skinType : "Chưa xác định"));

        btnLogout.setOnClickListener(v -> {
            pref.clear();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        });

        setupOrderTabs();

        return view;
    }

    private void setupOrderTabs() {
        OrderTabAdapter orderTabAdapter = new OrderTabAdapter(this);
        viewPager.setAdapter(orderTabAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Chờ xác nhận");
                    break;
                case 1:
                    tab.setText("Đã xác nhận");
                    break;
                case 2:
                    tab.setText("Đang giao");
                    break;
                case 3:
                    tab.setText("Đã giao");
                    break;
                case 4:
                    tab.setText("Đã huỷ");
                    break;
                case 5:
                    tab.setText("Đã hoàn tiền");
                    break;
            }
        }).attach();
    }
}
