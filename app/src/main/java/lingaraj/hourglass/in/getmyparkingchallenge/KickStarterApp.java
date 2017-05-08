package lingaraj.hourglass.in.getmyparkingchallenge;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;

/**
 * Created by lingaraj on 5/7/17.
 */

public class KickStarterApp extends Application {

    public static final String TAG = "APP_KICKSTARTER";
    private RestAdapter cloudAdapter;
    private ConnectivityManager mConnectivity;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * The method to check if network is available or not
     * @return boolean
     */
    public boolean isNetworkAvailable() {
        if (mConnectivity == null) {
            mConnectivity = ((ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE));
        }
        return mConnectivity.getActiveNetworkInfo() != null && mConnectivity.getActiveNetworkInfo().isConnected();
    }
    private void setCloudAdapter()
    {
        Log.d(TAG, "set cloud adapter");
        OkHttpClient okHttpClient = getUnsafeOkHttpClient();
        //OkHttpClient okHttpClient = new OkHttpClient();
        cloudAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setEndpoint(Constants.rest_server_url)
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("CLOUD_API_REQUESTS"))
                .build();
        return;

    }

    private  OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    synchronized public RestAdapter getCloudAdapter()
    {
        Log.d(TAG, "get cloud adapter");
        if(cloudAdapter == null) {
            Log.d(TAG, "new cloud adapter");
            setCloudAdapter();
        }
        return cloudAdapter;
    }


}
