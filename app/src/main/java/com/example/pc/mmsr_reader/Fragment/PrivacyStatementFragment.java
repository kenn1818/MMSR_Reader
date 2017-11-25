package com.example.pc.mmsr_reader.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.pc.mmsr_reader.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrivacyStatementFragment extends Fragment {


    public PrivacyStatementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_privacy_statement, container, false);
        WebView mWebView = (WebView) v.findViewById(R.id.webview);
        mWebView.loadUrl("http://tarucmmsr.pe.hu/privacy_statement.php");

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());

        return v;
    }

}
