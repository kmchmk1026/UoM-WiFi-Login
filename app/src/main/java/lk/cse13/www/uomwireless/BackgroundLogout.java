package lk.cse13.www.uomwireless;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import lk.cse13.www.uomwireless.Views.MainActivity;


public class BackgroundLogout extends AsyncTask<String, Void, String> {
    public BackgroundLogout() {
        MainActivity.loginButton.setEnabled(false);
    }

    @Override
    protected String doInBackground(String[] params) {

        if (Operations.isConnectedToUoMWireless()) {
            return this.logout("https://wlan.uom.lk/logout.html");
        }
        else if (Operations.isConnectedToOtherSSID()) {
//            return "You cannot logout from " + Operations.getOtherSSID();
            String otherServerURL = Operations.getOtherServer();

            if(otherServerURL.equals(""))
            {
                return "Add login URL in settings";
            }

            if(otherServerURL.contains("login.htm")){
                otherServerURL = otherServerURL.replace("login.htm", "logout.htm");
            }
            else{
                if(!otherServerURL.substring(otherServerURL.length() - 1).equals("/")){
                    otherServerURL += "/";
                }
                otherServerURL += "logout.html";
            }
            return this.logout(otherServerURL);
        }
        else {
            return "Connect to UoM Wireless/"+ Operations.getOtherSSID() +" first";
        }
    }

    @Override
    protected void onPostExecute(String message) {
        Operations.toast(message);
        if (message.equals("Logged out")) {
            MainActivity.loginButton.setText("Click here to login");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                MainActivity.loginButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            }
            MainActivity.loggedIn = false;
            Operations.showNotification("You are NOT logged in!");
        }
        MainActivity.loginButton.setEnabled(true);
    }


    private String logout(String serverURL) {
        String responseString = "Couldn't log out";
        try {

            MyHttpClient httpClient = new MyHttpClient();

            HttpPost httpPost = new HttpPost(serverURL);

            List<NameValuePair> para = new ArrayList<>();

            para.add(new BasicNameValuePair("userStatus", "1"));
            para.add(new BasicNameValuePair("err_flag", "0"));
            para.add(new BasicNameValuePair("err_msg", ""));
            httpPost.setEntity(new UrlEncodedFormEntity(para));
            HttpResponse response = httpClient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                if (!Operations.isConnectedToInternet()) {
                    responseString = "Logged out";
                }
            }
            return responseString;
        } catch (Exception e) {
            return responseString;
        }
    }

}

