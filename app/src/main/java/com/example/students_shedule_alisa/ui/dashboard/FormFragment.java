package com.example.students_shedule_alisa.ui.dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;


import com.example.students_shedule_alisa.MainActivity;

import com.example.students_shedule_alisa.R;
import com.example.students_shedule_alisa.databinding.FragmentFormBinding;
import com.example.students_shedule_alisa.models.Student;
import com.example.students_shedule_alisa.room.AppDatabase;
import com.example.students_shedule_alisa.room.StudentDao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FormFragment extends Fragment {

    private FragmentFormBinding binding;

    private AppDatabase appDataBase;
    private StudentDao studentDao;
    private ActivityResultLauncher<String> content_l;
    private Bitmap bitmap_imageSudent;
    private boolean isImgSelected = false;
    NavController navController;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentFormBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnLoadFoto.setOnClickListener(v1 -> {
            FormFragment.this.content_l.launch("image/*");
        });

        content_l = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        try {
                            bitmap_imageSudent = MediaStore
                                    .Images
                                    .Media
                                    .getBitmap(getContext()
                                            .getContentResolver(), result);
                            binding.imageFromForm.setImageBitmap(bitmap_imageSudent);
                            isImgSelected = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            isImgSelected = false;
                        }
                    }
                });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSave.setOnClickListener(v2-> {

            String nameSurnameStunent = binding.editNameSurname.getText().toString();
            String telStudent = binding.editTelNubmer.getText().toString();


            if (nameSurnameStunent.isEmpty() || telStudent.isEmpty() ) {
                Toast.makeText(getContext(), "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show();
            } else {
                if (isImgSelected) {
                    ByteArrayOutputStream baos_imageStudent = new ByteArrayOutputStream();
                    bitmap_imageSudent.compress(Bitmap.CompressFormat.PNG, 100, baos_imageStudent);

                    byte[] imageStudent= baos_imageStudent.toByteArray();

                    Student student = new Student(
                            nameSurnameStunent, telStudent,  imageStudent);

                    this.appDataBase = Room.databaseBuilder(
                                    binding.getRoot().getContext(),
                                    AppDatabase.class, "database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                    studentDao = appDataBase.studentDao();
                    studentDao.insert(student);

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {
                    Toast.makeText(getContext(), "Выберите из галереи телефона фото студента "
                           , Toast.LENGTH_LONG).show();
                }
            }
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_home);

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}