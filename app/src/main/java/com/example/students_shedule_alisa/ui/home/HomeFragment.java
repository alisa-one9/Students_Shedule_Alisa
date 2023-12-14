package com.example.students_shedule_alisa.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.students_shedule_alisa.databinding.FragmentHomeBinding;
import com.example.students_shedule_alisa.room.AppDatabase;
import com.example.students_shedule_alisa.room.StudentDao;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AppDatabase appDatabase;
    private StudentDao studentDao;
    private StudentAdapter studentAdapter;
    private NavController navController;
    RecyclerView rv_main_catalog;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv_main_catalog = binding.rvMain;
        studentAdapter = new StudentAdapter();
        rv_main_catalog.setAdapter(studentAdapter);

        appDatabase = Room.databaseBuilder(binding.getRoot().getContext()
                        ,AppDatabase.class,"database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        studentDao = appDatabase.studentDao();
        studentAdapter.setList_main_catalog(studentDao.getAll());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}