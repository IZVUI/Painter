package com.example.painter.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.painter.DAO.DB;
import com.example.painter.R;
import com.example.painter.model.User;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class RegistrationActivity extends AppCompatActivity {

    private EditText login, password, password_confirm;
    private Button back, register;
    private static boolean isIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
    }

    private void init() {
        login = (EditText) findViewById(R.id.login_field_reg);
        password = (EditText) findViewById(R.id.password_reg);
        password_confirm = (EditText) findViewById(R.id.password_confirm);

        back = (Button) findViewById(R.id.back_button);
        register = (Button) findViewById(R.id.register_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String log = login.getText().toString();
                String psw = password.getText().toString();
                String psw_confirm = password_confirm.getText().toString();
                if(log.isEmpty()
                        || psw.isEmpty()
                        || psw_confirm.isEmpty())
                    showToastMessage("Please fill \"Password\" and \"Login\" fields!");
                else {
                    if(!psw_confirm
                        .equals( psw)) {
                    showToastMessage("Passwords are not same!");
                } else
                    DB.getDB().userDao().getUserByLogin(log)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DisposableMaybeObserver<User>() {
                                @Override
                                public void onSuccess(User user) {
                                    showToastMessage("Login is exist!");
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {
                                    Completable.fromRunnable(new Runnable() {
                                        @Override
                                        public void run() {
                                            User newUser = new User(log, psw);
                                            newUser.setId(DB.getDB().userDao().insert(newUser));
                                            HomeActivity.user = newUser;
                                        }
                                    })
                                            .subscribeOn(Schedulers.io())
                                            .subscribe();
                                    goToHome();
                                }
                            });
                }
            }
        });
    }

    private void showToastMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(1, 0, 0);
        toast.show();
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}