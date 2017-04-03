package nyc.c4q.ashiquechowdhury.auxx.chooseroomandlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import nyc.c4q.ashiquechowdhury.auxx.R;
import nyc.c4q.ashiquechowdhury.auxx.util.SingleLineTextView;

public class LoginFragment extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LoginFragment.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private String enteredUsername;
    private String enteredPassword;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, new ChooseRoomFragment()).commit();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }
                updateUi(user);
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        CardView auxSignInButton = (CardView) view.findViewById(R.id.aux_signin_button);
        CardView googleSignInButton = (CardView) view.findViewById(R.id.google_signin_button);
        SingleLineTextView singleLineTextView = (SingleLineTextView) view.findViewById(R.id.aux_login_tv);
        Typeface skinnyMarkerFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/skinny_marker.ttf");
        singleLineTextView.setTypeface(skinnyMarkerFont);

        usernameEditText = (EditText) view.findViewById(R.id.username_login_edittext);
        passwordEditText = (EditText) view.findViewById(R.id.password_login_edittext);
        LinearLayout signUpHereButton = (LinearLayout) view.findViewById(R.id.sign_up_here_button);


        passwordEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    enteredUsername = getUsernameText();
                    enteredPassword = getPasswordText();
                    signInWithAuxEmailAndPassword(enteredUsername, enteredPassword);
                    return true;
                }
                return false;
            }
        });

        auxSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredUsername = getUsernameText();
                enteredPassword = getPasswordText();
//                handleLogin(enteredUsername, enteredPassword);
                signInWithAuxEmailAndPassword(enteredUsername, enteredPassword);
            }
        });

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

        signUpHereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction
                        .replace(R.id.main_container, new SignUpFragment())
                        .addToBackStack(TAG)
                        .commit();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }

    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Signing in with Google");
        progressDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(view.getContext(), "Sign in with Google failed.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(view.getContext(), "Sign in with Google succeeded!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.cancel();
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient)
                .setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                updateUi(null);
                            }
                        }
                );
    }

    private void handleLogin(String enteredUsername, String enteredPassword) {
        if (enteredUsername.isEmpty()) {
            Toast.makeText(getActivity(), "Enter a username", Toast.LENGTH_SHORT).show();
        } else if (enteredPassword.isEmpty()) {
            Toast.makeText(getActivity(), "Enter a password", Toast.LENGTH_SHORT).show();
        } else {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, new ChooseRoomFragment()).commit();
        }
    }

    private void signInWithAuxEmailAndPassword(String email, String password) {
        if (!validateForm()) {
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Signing in");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(view.getContext(), "Sign in failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(view.getContext(), "Sign in Successful", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.cancel();
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = getUsernameText();
        if (TextUtils.isEmpty(email)) {
            usernameEditText.setError("Required.");
            valid = false;
        } else {
            usernameEditText.setError(null);
        }

        String password = getPasswordText();
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Required.");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        return valid;
    }

    private void updateUi(FirebaseUser user) {
        // TODO
    }

    private String getUsernameText() {
        return usernameEditText.getText().toString();
    }

    private String getPasswordText() {
        return passwordEditText.getText().toString();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
