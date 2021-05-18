package com.gachon.dawaga;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gachon.dawaga.util.Firestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoLoginButton).setOnClickListener(onClickListener);
    }

/*    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }*/

   /* @Override
    public void onBackPressed(){
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }*/

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.signUpButton:
                    signUp();
                    break;

                case R.id.gotoLoginButton:
                    startLoginActivity();
                    break;
            }
        }
    };

    private void signUp(){
        EditText e_text = (EditText)findViewById(R.id.emailEditText);
        EditText p_text = (EditText)findViewById(R.id.passwordEditText);
        EditText p_ch_text = (EditText)findViewById(R.id.passwordCheckEditText);

        String email = e_text.getText().toString();
        String password = p_text.getText().toString();
        String passwordCheck = p_ch_text.getText().toString();

        if (email.length()>0 && password.length()>0 && passwordCheck.length()>0) {

            if (password.equals(passwordCheck)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    // DB 생성 작업
                                    createNewUserDatabase(task.getResult().getUser());
                                } else {
                                    // If sign in fails, display a message to the user.
                                    if (task.getException() != null) {
                                        startToast(task.getException().toString());
                                        Log.d(TAG, "파이어베이스 연동 실패: "+task.getException().toString());
                                    }
                                }
                            }
                        });
            } else {
                startToast("비밀번호가 일치하지 않습니다.");
            }
        }else {
            startToast("이메일 또는 비밀번호를 입력해 주세요.");
        }
    }

    /**
     * 새로운 사용자의 DB 정보를 생성한다
     * @author Taehyun Park
     */
    private void createNewUserDatabase(FirebaseUser user) {
        // 새 유저 정보 작성
        Firestore.writeNewUser(user.getUid(), user.getEmail(), user.getEmail())
                .addOnCompleteListener(documentTask -> {
                    // 성공했다면
                    if(documentTask.isSuccessful()) {
                        startToast("회원가입에 성공하였습니다.");
                    }
                    // 실패했다면
                    else {
                        // 에러 메시지 띄우고 로그아웃
                        Toast.makeText(getApplicationContext(), R.string.login_error, Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                    }
                });
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void startLoginActivity(){
        Intent intent = new Intent (this, LoginActivity.class);
        startActivity(intent);
    }
}

