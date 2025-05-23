package com.example.helloandroid;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.helloandroid.database.AppDatabase;
import com.example.helloandroid.entities.City;
import com.example.helloandroid.entities.User;

public class FormUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.submitBtn).setOnClickListener(v -> {
            String email = ((EditText) findViewById(R.id.emailInput)).getText().toString();
            String cityName = ((EditText) findViewById(R.id.citylInput)).getText().toString();
            String password = ((EditText) findViewById(R.id.passwordInput)).getText().toString();

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(this);

                // 1. Crear y guardar la ciudad
                City city = new City(cityName);
                long cityId = db.cityDao().insert(city); // insert debe devolver long

                // 2. Crear el usuario y asignar cityId
                User user = new User();
                user.email = email;
                user.password = password;
                user.cityId = (int) cityId;
                db.userDao().insert(user);

                runOnUiThread(() -> {
                    setResult(RESULT_OK);
                    finish();
                });
            }).start();
        });
    }
}