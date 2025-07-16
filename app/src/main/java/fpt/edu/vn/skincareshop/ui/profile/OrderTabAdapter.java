package fpt.edu.vn.skincareshop.ui.profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OrderTabAdapter extends FragmentStateAdapter {

    public OrderTabAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String status = "pending"; // mặc định
        switch (position) {
            case 0:
                status = "pending";
                break;
            case 1:
                status = "confirmed";
                break;
            case 2:
                status = "shipped";
                break;
            case 3:
                status = "delivered";
                break;
            case 4:
                status = "cancelled";
                break;
            case 5:
                status = "refunded";
                break;
        }
        return OrderTabFragment.newInstance(status);
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
