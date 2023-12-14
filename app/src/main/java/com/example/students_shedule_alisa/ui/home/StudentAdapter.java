package com.example.students_shedule_alisa.ui.home;

import static android.Manifest.permission.CALL_PHONE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.students_shedule_alisa.R;
import com.example.students_shedule_alisa.databinding.ItemCardBinding;
import com.example.students_shedule_alisa.models.Student;
import com.example.students_shedule_alisa.room.AppDatabase;
import com.example.students_shedule_alisa.room.StudentDao;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    List<Student> list_main_catalog = new ArrayList<>();
    StudentDao studentDao;
    Context context;
    NavController navController;
    Student newStudent;

    public void setList_main_catalog(List<Student> list_main_catalog) {
        this.list_main_catalog = list_main_catalog;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCardBinding itemCardBinding = ItemCardBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ViewHolder viewHolder = new ViewHolder(itemCardBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.ViewHolder holder, int position) {
        studentDao = Room.databaseBuilder(
                        holder.binding.getRoot().getContext(),
                        AppDatabase.class, "database")
                .allowMainThreadQueries()
                .build()
                .studentDao();

        Student student = list_main_catalog.get(position);


        holder.binding.nameSurnameCard.setText(student.getName_surname());
        holder.binding.telNumberCard.setText(student.getTel_number());


        holder.binding.imageCard.setImageBitmap(BitmapFactory.decodeByteArray(
                student.getImage(),
                0, student.getImage().length));

        newStudent = student;

        holder.binding.dropdownMenu.setOnClickListener(v1 -> {

            PopupMenu popup = new PopupMenu(holder.binding.getRoot().getContext(),
                    holder.binding.dropdownMenu);
            popup.getMenuInflater().inflate(R.menu.card_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getTitle().toString()) {
                        case "call":
                            if (ContextCompat.checkSelfPermission(
                                    holder.binding.getRoot().getContext(), android.Manifest.permission.CALL_PHONE) !=
                                    PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions((Activity) holder.binding.getRoot().getContext(),
                                        new String[]{android.Manifest.permission.CALL_PHONE}, 0);
                            } else {
                                String number_student = holder.binding.telNumberCard.getText().toString().trim();
                                Uri call = Uri.parse("tel:" + number_student);

                                Intent intent = new Intent(Intent.ACTION_DIAL, call);
                                holder.binding.getRoot().getContext().startActivity(intent);
                            }
                            break;
                        case "message":
                            Toast.makeText(holder.binding.getRoot().getContext(), "message !", Toast.LENGTH_SHORT).show();
                            break;

                        case "delete":
                            Toast.makeText(holder.binding.getRoot().getContext(), "delete", Toast.LENGTH_SHORT).show();
                            break;

                    }
                    return true;
                }
            });
            popup.show();


        });

    }

    @Override
    public int getItemCount() {
        return list_main_catalog.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCardBinding binding;

        public ViewHolder(@NonNull ItemCardBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;


        }
    }
}
