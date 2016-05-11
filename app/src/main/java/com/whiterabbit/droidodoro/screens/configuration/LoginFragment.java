package com.whiterabbit.droidodoro.screens.configuration;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.whiterabbit.droidodoro.BuildConfig;
import com.whiterabbit.droidodoro.R;


public class LoginFragment extends DialogFragment {

    private WebView webViewOauth;
    private ProgressBar progressBar;
    private boolean mAuthFound;

    public static LoginFragment newInstance() {
        LoginFragment f = new LoginFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthFound = false;
        setRetainInstance(true);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (!mAuthFound) {
            notifyAuthenticationFailed();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (!mAuthFound) {
            notifyAuthenticationFailed();
        }
    }

    private class MyWebViewClient extends WebViewClient {

        private boolean parseUrl(String url) {
            // the redirect url is something like
            // https://trello.com/1/token/com.whiterabbit#token=TOKEN
            Uri uri = Uri.parse(url);
            String fragment = uri.getFragment();
            if (fragment != null && fragment.startsWith("token")) {
                String token = fragment.split("=")[1];
                if (token != null) {
                    mAuthFound = true;
                    ConfigurationFragment target = (ConfigurationFragment) getTargetFragment();
                    target.onTokenFound(token);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            progressBar.setVisibility(View.INVISIBLE);
            if (parseUrl(url)) {
                dismiss();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            notifyAuthenticationFailed();
        }
    }

    private void notifyAuthenticationFailed() {
        ConfigurationFragment target = (ConfigurationFragment) getTargetFragment();
        target.onLoginError(getString(R.string.authentication_failed));
    }

    @Override
    public void onViewCreated(View arg0, Bundle arg1) {
        super.onViewCreated(arg0, arg1);
        //load the url of the oAuth mLogin page
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("trello.com")
                .appendPath("1")
                .appendPath("authorize")
                .appendQueryParameter("expiration", "never")
                .appendQueryParameter("name", "Droidodoro")
                .appendQueryParameter("key", BuildConfig.TRELLO_API_KEY)
                .appendQueryParameter("callback_method", "fragment")
                .appendQueryParameter("return_url", "com.whiterabbit")
                .appendQueryParameter("scope", "read,write");
        String url = builder.build().toString();
        webViewOauth.loadUrl(url);
        //set the web client
        webViewOauth.setWebViewClient(new MyWebViewClient());
        //activates JavaScript (just in case)
        WebSettings webSettings = webViewOauth.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_dialog, container, false);
        webViewOauth = (WebView) v.findViewById(R.id.web_login);
        progressBar = (ProgressBar) v.findViewById(R.id.web_login_progress);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return v;
    }
}