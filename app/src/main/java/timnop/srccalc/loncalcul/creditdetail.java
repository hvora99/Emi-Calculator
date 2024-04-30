package timnop.srccalc.loncalcul;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
 import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
 import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.CacheFlag;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAdListener;

import java.util.EnumSet;

public class creditdetail extends AppCompatActivity {

    String title;
    String description;
    String dialoginformation;
    int image;


    private @Nullable
    AdView bannerAdView;
    Boolean Istablate=false;



    InterstitialAd interstitialAd;


    private @Nullable AdView rectangleAdView;
    private static final String TAG = creditdetail.class.getSimpleName();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditdetail);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Credit Detail");


        loadBnrAdView();

        Load_Rectangular_Banner();


        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView tvTitleLabel = (TextView)findViewById(R.id.tvTitleLabel);
        TextView tvDescLabel = (TextView)findViewById(R.id.tvDescLabel);
        Button btn = (Button)findViewById(R.id.button1);


        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            title = extras.getString("province");
            tvTitleLabel.setText(title);
            btn.setText("More about "+ title);
            dialoginformation = extras.getString("dialogmsg");

            image = extras.getInt("prvImg");

            description = extras.getString("provincedesclabel");
            tvDescLabel.setText(description);

        }

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Load_ins();
            }
        });
    }


    public void Load_ins()
    {

        if (interstitialAd != null) {
            interstitialAd.destroy();
            interstitialAd = null;
        }
        interstitialAd = new InterstitialAd(creditdetail.this, getResources().getString(R.string.ins1));
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                interstitialAd.destroy();
                interstitialAd = null;
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(creditdetail.this);
                dlgAlert.setMessage(dialoginformation);
                dlgAlert.setTitle(title);
                dlgAlert.setNegativeButton("close", null);
                dlgAlert.create().show();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                interstitialAd.destroy();
                interstitialAd = null;
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(creditdetail.this);
                dlgAlert.setMessage(dialoginformation);
                dlgAlert.setTitle(title);
                dlgAlert.setNegativeButton("close", null);
                dlgAlert.create().show();
            }

            @Override
            public void onAdLoaded(Ad ad) {

                if (interstitialAd == null || !interstitialAd.isAdLoaded()) {
                    // Ad not ready to show.

                } else {
                    // Ad was loaded, show it!
                    interstitialAd.show();

                }

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

        // Load a new interstitial.
        interstitialAd.loadAd(EnumSet.of(CacheFlag.VIDEO));

    }
    public void Load_Rectangular_Banner()
    {

        RelativeLayout rectangleAdContainer = (RelativeLayout) findViewById(R.id.rectangleAdContainer);
        if (rectangleAdView != null) {
            rectangleAdView.destroy();
            rectangleAdView = null;
        }

        // Update progress message


        // Create a banner's ad view with a unique placement ID (generate your own on the Facebook
        // app settings). Use different ID for each ad placement in your app.
        rectangleAdView = new AdView(this, getResources().getString(R.string.mediumrec1),
                AdSize.RECTANGLE_HEIGHT_250);

        // Reposition the ad and add it to the view hierarchy.
        rectangleAdContainer.addView(rectangleAdView);

        // Set a listener to get notified on changes or when the user interact with the ad.
        rectangleAdView.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

        // Initiate a request to load an ad.
        rectangleAdView.loadAd();



    }

    private void loadBnrAdView() {

        DisplayMetrics metrics = new DisplayMetrics();
        creditdetail.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        Istablate = diagonalInches >= 7.5;

        RelativeLayout banneradscon = (RelativeLayout) findViewById(R.id.mybanner);

        if (bannerAdView != null) {
            bannerAdView.destroy();
            bannerAdView = null;
        }



        // Create a banner's ad view with a unique placement ID (generate your own on the Facebook
        // app settings). Use different ID for each ad placement in your app.
        bannerAdView = new AdView(creditdetail.this, getResources().getString(R.string.banner1),
                Istablate ? AdSize.BANNER_HEIGHT_90 : AdSize.BANNER_HEIGHT_50);

        // Reposition the ad and add it to the view hierarchy.
        banneradscon.addView(bannerAdView);

        // Set a listener to get notified on changes or when the user interact with the ad.

        // Initiate a request to load an ad.
        bannerAdView.loadAd();


    }

    @Override
    public void onDestroy() {
        if (bannerAdView != null) {
            bannerAdView.destroy();
            bannerAdView = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
