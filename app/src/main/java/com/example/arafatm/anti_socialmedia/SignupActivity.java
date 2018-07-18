package com.example.arafatm.anti_socialmedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText fullNameInput;
    private EditText emailInput;
    private EditText passwordInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final String username = usernameInput.getText().toString();
        final String fullname = fullNameInput.getText().toString();
        final String password = passwordInput.getText().toString();
        final String email = emailInput.getText().toString();

        signUp(username, password, email, fullname);

    }

    private void signUp(String username, String password, String email, String fullname){
        ParseUser user = new ParseUser();

        user.setUsername(username);
        user.setFullname(fullname);
        user.setPassword(password);
        user.setEmail(email);


        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("LoginActivity", "Sign up successful!");


                    final Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_LONG).show();

                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_LONG).show();
                    Log.e("LoginActivity", "Sign up failure.");
                    e.printStackTrace();
                }
            }
        });
    }


}
