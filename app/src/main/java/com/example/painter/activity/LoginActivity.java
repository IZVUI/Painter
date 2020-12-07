package com.example.painter.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.painter.DAO.AppDatabase;
import com.example.painter.DAO.DB;
import com.example.painter.R;
import com.example.painter.model.User;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private EditText password, login;
    private Button signIn, logIn, logInAsGuest;
    private LoginActivity such = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DB.setDB(Room
                .databaseBuilder(getApplicationContext(),
                        AppDatabase.class,
                        "painterDatabase")
                .fallbackToDestructiveMigration()
                .build());

        initActivity();

    }

    private void initActivity() {
        password = (EditText) findViewById(R.id.password);
        login = (EditText) findViewById(R.id.login_field);

        signIn = (Button) findViewById(R.id.sign_in);
        logIn = (Button) findViewById(R.id.log_in_button);
        logInAsGuest = (Button) findViewById(R.id.log_in_as_guest);


        logInAsGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.user = new User();


                DB.getDB().userDao().getUserByLogin("Guest")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableMaybeObserver<User>() {
                            @Override
                            public void onSuccess(User user) {
                                    HomeActivity.user = user;
                                    goToHome();
                                }
                            @Override
                            public void onError(Throwable e) {
                                showToastMessage("Something wrong!!!");
                            }

                            @Override
                            public void onComplete() {
                                Completable.fromRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        User newUser = new User("Guest", "");
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
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login.getText().toString().isEmpty()
                        || password.getText().toString().isEmpty())
                    showToastMessage("Please fill \"Password\" and \"Login\" fields!");
                else {
                    /* TODO check user in DB*/

                    final User check = null;


                    DB.getDB().userDao().getUserByLogin(login.getText().toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DisposableMaybeObserver<User>() {
                                @Override
                                public void onSuccess(User user) {
                                    String log = login.getText().toString();
                                    String psw = password.getText().toString();
                                    if(!user.getLogin().equals(log)
                                            || !user.getPassword().equals(psw))
                                        showToastMessage("Wrong Login or Password!!!");
                                    else {
                                       // HomeActivity.user = new User(login.getText().toString(), password.getText().toString());
                                        HomeActivity.user = user;
                                        goToHome();
                                    }

                                }

                                @Override
                                public void onError(Throwable e) {
                                    showToastMessage("Wrong Login or Password!!!");
                                }

                                @Override
                                public void onComplete() {
                                    showToastMessage("Wrong Login or Password!!!");
                                }
                            });
                }
            }
        });

        //TODO Add registration
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegistration();
            }
        });
    }

    private void showToastMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(1, 0, 0);
        toast.show();
    }

    private void goToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void goToRegistration() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}