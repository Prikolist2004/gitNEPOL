package com.example.git1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;

    private TextView mTextViewSecond;
    private TextView mTextView;

    private TextInputEditText takeName;
    private final static String PHOTO_URL = "https://avatars.githubusercontent.com/u/66577?v=4" ;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        takeName = (TextInputEditText) findViewById(R.id.takeName);

        mTextView = (TextView) findViewById(R.id.textViewMain);
        mTextViewSecond = (TextView) findViewById(R.id.textViewContributions);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
    }
    public void onClickPr1(View view) {
        GitHubServicePr1 gitHubServicePr1 = GitHubServicePr1.retrofit.create(GitHubServicePr1.class);
        final Call<List<Contributor>> call = gitHubServicePr1.repoContributors("square", "picasso");

        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                final TextView textViewLogins = (TextView) findViewById(R.id.textViewMain);
                final TextView textViewContributions = (TextView) findViewById(R.id.textViewContributions);
                final ImageView imageView = (ImageView) findViewById(R.id.image);

                StringBuilder loginsText = new StringBuilder();
                StringBuilder contributionsText = new StringBuilder();
                StringBuilder Img = new StringBuilder();
                List<Contributor> contributors = response.body();
                for (Contributor contributor : contributors) {
                    loginsText.append(contributor.getLogin()).append("\n");
                    contributionsText.append(contributor.getContributions()).append("\n");
                    Picasso.get()
                            .load(contributor.getPhoto())
                            .resize(200, 150)
                            .into(imageView);
                }
                textViewLogins.setText(loginsText.toString());
                textViewContributions.setText(contributionsText.toString());

            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable throwable) {
                final TextView textView = (TextView) findViewById(R.id.textViewMain);
                textView.setText("Что-то пошло не так: " + throwable.getMessage());
            }
        });
    }
    public void onClickPr2(View view) {
        mTextView.setText("");
        mTextViewSecond.setText("");
        mProgressBar.setVisibility(View.VISIBLE);

        GitHubServicePr2 gitHubService = GitHubServicePr2.retrofit.create(GitHubServicePr2.class);
        final Call<User> call =
                gitHubService.getUser(takeName.getText().toString());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // response.isSuccessfull() is true if the response code is 2xx
                if (response.isSuccessful()) {
                    User user = response.body();

                    // Получаем json из github-сервера и конвертируем его в удобный вид
                    mTextView.setText("Аккаунт Github: " + user.getName() +
                            "\nСайт: " + user.getBlog() +
                            "\nКомпания: " + user.getCompany());

                    mProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    int statusCode = response.code();

                    // handle request errors yourself
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mTextView.setText(errorBody.string());
                        mProgressBar.setVisibility(View.INVISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                mTextView.setText("Что-то пошло не так: " + throwable.getMessage());
            }
        });
    }
    public void onClickPr3(View view){
        mTextView.setText("");
        mTextViewSecond.setText("");
        mProgressBar.setVisibility(View.VISIBLE);
        GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);

        final Call<List<Repos>> call = gitHubService.getRepos(takeName.getText().toString());

        call.enqueue(new Callback<List<Repos>>() {
                         @Override
                         public void onResponse(Call<List<Repos>> call, Response<List<Repos>> response) {
                             // response.isSuccessfull() is true if the response code is 2xx
                             if (response.isSuccessful()) {
                                 // Выводим массив имён
                                 for (int i = 0; i < response.body().size(); i++) {
                                     // Выводим имена по отдельности
                                     mTextView.append(response.body().get(i).getName() + "\n");
                                 }

                                 mProgressBar.setVisibility(View.INVISIBLE);
                             } else {
                                 int statusCode = response.code();
                                 // Обрабатываем ошибку
                                 ResponseBody errorBody = response.errorBody();
                                 try {
                                     mTextView.setText(errorBody.string());
                                     mProgressBar.setVisibility(View.INVISIBLE);
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<List<Repos>> call, Throwable throwable) {
                             mTextView.setText("Что-то пошло не так: " + throwable.getMessage());
                         }
                     }

        );
    }
    public void onClickPr4(View view) {
        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setText("");
        mTextViewSecond.setText("");

        GitHubServicePr4 gitHubService = GitHubServicePr4.retrofit.create(GitHubServicePr4.class);
        // часть слова
        final Call<GitResult> call =
                gitHubService.getUsers(takeName.getText().toString());

        call.enqueue(new Callback<GitResult>() {
            @Override
            public void onResponse(Call<GitResult> call, Response<GitResult> response) {
                // response.isSuccessful() is true if the response code is 2xx
                if (response.isSuccessful()) {
                    GitResult result = response.body();

                    // Получаем json из github-сервера и конвертируем его в удобный вид
                    // Покажем только первого пользователя
                    String user = "Аккаунт Github: " + result.getItems().get(0).getLogin();
                    mTextView.setText(user);
                    Log.i("Git", String.valueOf(result.getItems().size()));

                    mProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    int statusCode = response.code();

                    // handle request errors yourself
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mTextView.setText(errorBody.string());
                        mProgressBar.setVisibility(View.INVISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GitResult> call, Throwable throwable) {
                mTextView.setText("Что-то пошло не так: " + throwable.getMessage());
            }
        });
    }
}