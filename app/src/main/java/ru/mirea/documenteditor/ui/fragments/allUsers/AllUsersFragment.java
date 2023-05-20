package ru.mirea.documenteditor.ui.fragments.allUsers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import ru.mirea.documenteditor.data.adapter.UsersAdapter;
import ru.mirea.documenteditor.data.model.api.user.UserInfo;
import ru.mirea.documenteditor.databinding.FragmentAllUsersBinding;
import ru.mirea.documenteditor.ui.activities.userInfoId.UserInfoIdActivity;

public class AllUsersFragment extends Fragment {

    private AllUsersViewModel allUsersViewModel;
    private SwipeRefreshLayout rlUsers;
    private RecyclerView rvUsers;
    private Context context;
    private FragmentAllUsersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allUsersViewModel = new ViewModelProvider(this).get(AllUsersViewModel.class);

        binding = FragmentAllUsersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rlUsers = binding.rlUsers;
        rvUsers = binding.rvUsers;
        context = getContext();

        rlUsers.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(context, "Обновляем пользователей", Toast.LENGTH_SHORT).show();
                allUsersViewModel.queryUsers();
            }
        });

        // Initialize users
        allUsersViewModel.getArrayUserInfo().observe(getViewLifecycleOwner(), this::setUpRecycleView);

        if (savedInstanceState != null) {
            allUsersViewModel.getArrayUserInfo().setValue(
                    savedInstanceState.getParcelableArrayList("arrayUserInfo")
            );
        } else {
            allUsersViewModel.queryUsers();
        }
        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("arrayUserInfo", allUsersViewModel.getArrayUserInfo().getValue());
    }

    private void setUpRecycleView(ArrayList<UserInfo> arrayUserInfo) {
        if (rvUsers.getAdapter() == null) {
            UsersAdapter adapter = new UsersAdapter(arrayUserInfo);
            adapter.setOnItemClickListener(new UsersAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Integer id = arrayUserInfo.get(position).getId();
                    String name = arrayUserInfo.get(position).getUsername();
                    Toast.makeText(context, "Загружаем профиль " + name, Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(context, UserInfoIdActivity.class);
                    myIntent.putExtra("id", id);
                    context.startActivity(myIntent);
                }
            });
            rvUsers.setAdapter(adapter);
            rvUsers.setLayoutManager(new LinearLayoutManager(context));
            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
            rvUsers.addItemDecoration(itemDecoration);
        } else {
            UsersAdapter adapter = (UsersAdapter) rvUsers.getAdapter();
            adapter.swap(arrayUserInfo);
        }
        rlUsers.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}