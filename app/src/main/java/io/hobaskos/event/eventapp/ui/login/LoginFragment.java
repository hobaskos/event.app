package io.hobaskos.event.eventapp.ui.login;

/**
 * Created by hansp on 25.02.2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.games.social.Social;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.SocialUserVM;
import io.hobaskos.event.eventapp.data.model.response.Response;
import io.hobaskos.event.eventapp.ui.base.view.fragment.BaseViewStateFragment;
import io.hobaskos.event.eventapp.ui.main.MainActivity;

/**
 * Created by osvold.hans.petter on 15.02.2017.
 */

public class LoginFragment extends BaseViewStateFragment<LoginView, LoginPresenter>
        implements LoginView {

    @Inject
    public LoginPresenter loginPresenter;

    private EditText etEmail, etPassword;
    private Button btnLogin;


    private CallbackManager callbackManager;
    private LoginButton btnFacebook;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        etEmail = (EditText) view.findViewById(R.id.field_email);
        etEmail.setHint(R.string.email);
        etPassword = (EditText) view.findViewById(R.id.field_password);
        etPassword.setHint(R.string.password);

        initRegularLogin();
        initFacebookLogin();

        Button btnSkip = (Button) getView().findViewById(R.id.btn_skip_login);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });
    }

    private void initRegularLogin()
    {
        etEmail = (EditText) getView().findViewById(R.id.field_email);
        etEmail.setHint(R.string.email);

        etPassword = (EditText) getView().findViewById(R.id.field_password);
        etPassword.setHint(R.string.password);

        btnLogin = (Button) getView().findViewById(R.id.btn_login_username);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("LoginFragment", "Login button clicked!");
                LoginVM loginVM = new LoginVM(etEmail.getText().toString(), etPassword.getText().toString());
                presenter.login(loginVM);
            }
        });
    }

    private void initFacebookLogin()
    {
        Log.i("LoginFragment", "FacebookBtn clicked!");
        callbackManager = CallbackManager.Factory.create();

        btnFacebook = (LoginButton) getView().findViewById(R.id.btn_login_facebook);
        btnFacebook.setFragment(this);
        Log.i("LoginActivity", "Just before registerCallback");
        btnFacebook.setReadPermissions("email");
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("LoginActivity", "OnSuccess(token)");
                Log.i("Login Token", loginResult.getAccessToken().getToken());
                Log.i("Login UserId", loginResult.getAccessToken().getUserId());

                GraphRequest mGrapRequest = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                if (response.getError() != null) {

                                } else {
                                    String email = object.optString("email");
                                    String name = object.optString("name");

                                    Log.i("Facebook", email);
                                    Log.i("Facebook", name);
                                }
                            }
                        }
                );

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                mGrapRequest.setParameters(parameters);
                mGrapRequest.executeAsync();

                String token = loginResult.getAccessToken().getToken();
                String userId = loginResult.getAccessToken().getUserId();

                SocialUserVM socialUserVM = new SocialUserVM(userId, token);
                presenter.login(socialUserVM);
            }

            @Override
            public void onCancel() {
                Log.i("LoginActivityOnCancel", "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("LoginActivity", "onError(error)");
                Log.i("Error:", error.toString());

            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_splash;
    }

    @NonNull
    @Override
    public ViewState createViewState() {
        return new LoginViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        showLoginForm();
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return loginPresenter;
    }

    @Override
    public void showLoginForm() {
        LoginViewState loginViewState = (LoginViewState) viewState;
        loginViewState.setShowLoginForm();

        //
    }

    @Override
    public void showError(Response response) {
        LoginViewState loginViewState = (LoginViewState) viewState;
        loginViewState.setShowError();
        loginViewState.setResponse(response);

        Log.i("LoginActivityError", response.getMessage());
        Toast.makeText(getActivity(), R.string.login_failure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(Response response) {
        LoginViewState loginViewState = (LoginViewState) viewState;
        loginViewState.setShowSuccess();
        loginViewState.setResponse(response);

        Log.i("LoginActivitySuccess", response.getMessage());
        Toast.makeText(getActivity(), R.string.login_success, Toast.LENGTH_SHORT).show();

        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
