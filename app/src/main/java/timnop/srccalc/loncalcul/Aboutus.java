package timnop.srccalc.loncalcul;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;

import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;

import java.util.ArrayList;
import java.util.List;

public class Aboutus extends AppCompatActivity {

    Intent intent;



    private @Nullable AdView bannerAdView;
    Boolean Istablate=false;
    private RelativeLayout Banneradscon;

    private final String TAG = Aboutus.class.getSimpleName();
    private boolean isAdViewAdded;
    private @Nullable
    NativeBannerAd mNativeBannerAd;
    private NativeAdLayout mNativeBannerAdContainer;
    private LinearLayout mAdView;
    private FrameLayout mAdChoicesContainer;

    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("About Us");

        DisplayMetrics metrics = new DisplayMetrics();
        Aboutus.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        Istablate = diagonalInches >= 7.5;

        Banneradscon = (RelativeLayout) findViewById(R.id.mybanner);

        loadBnrAdView();
        Load_Native_Banner();

        interstrial__add();


        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        CardView gotomain = (CardView) findViewById(R.id.gotomain);
        gotomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Aboutus.this,homescreen.class);
                startActivity(intent);
            }
        });


    }


    private void loadBnrAdView() {

        if (bannerAdView != null) {
            bannerAdView.destroy();
            bannerAdView = null;
        }


        // Create a banner's ad view with a unique placement ID (generate your own on the Facebook
        // app settings). Use different ID for each ad placement in your app.
        bannerAdView = new AdView(Aboutus.this, getResources().getString(R.string.banner),
                Istablate ? AdSize.BANNER_HEIGHT_90 : AdSize.BANNER_HEIGHT_50);

        // Reposition the ad and add it to the view hierarchy.
        Banneradscon.addView(bannerAdView);

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

        if (mNativeBannerAd != null) {
            mNativeBannerAd.unregisterView();
            mNativeBannerAd = null;
        }
        if (interstitialAd != null) {
            interstitialAd.destroy();
            interstitialAd = null;
        }
        super.onDestroy();
    }



    private void interstrial__add() {

        interstitialAd = new com.facebook.ads.InterstitialAd(this, getResources().getString(R.string.ins));


        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                Log.e(TAG, "Interstitial ad dismissed.");
                interstitialAd.destroy();
                interstitialAd = null;

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                interstitialAd.destroy();
                interstitialAd = null;
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

        interstitialAd.loadAd();
    }


    public void Load_Native_Banner()
    {
        mNativeBannerAdContainer = findViewById(R.id.nativebanner);
        mNativeBannerAd = new NativeBannerAd(this, getResources().getString(R.string.nb));


        LayoutInflater inflater = LayoutInflater.from(Aboutus.this);
        mAdView = (LinearLayout) inflater.inflate(R.layout.native_banner_ad_unit, mNativeBannerAdContainer, false);

        mAdChoicesContainer = mAdView.findViewById(R.id.ad_choices_container);

        mNativeBannerAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {



            }

            @Override
            public void onAdLoaded(Ad ad) {



                if (mNativeBannerAd == null || mNativeBannerAd != ad) {
                    // Race condition, load() called again before last ad was displayed
                    return;
                }
                if (!isAdViewAdded) {
                    isAdViewAdded = true;
                    mNativeBannerAdContainer.addView(mAdView);
                }
                // Unregister last ad
                mNativeBannerAd.unregisterView();

                AdOptionsView adOptionsView = new AdOptionsView(
                        Aboutus.this,
                        mNativeBannerAd,
                        mNativeBannerAdContainer,
                        AdOptionsView.Orientation.HORIZONTAL,
                        20);
                mAdChoicesContainer.removeAllViews();
                mAdChoicesContainer.addView(adOptionsView);

                inflateAd(mNativeBannerAd, mAdView);

                mNativeBannerAd.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            int i = view.getId();
                            if (i == R.id.native_ad_call_to_action) {
                                Log.d(TAG, "Call to action button clicked");
                            } else if (i == R.id.native_icon_view) {
                                Log.d(TAG, "Main image clicked");
                            } else {
                                Log.d(TAG, "Other ad component clicked");
                            }
                        }
                        return false;
                    }
                });

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        mNativeBannerAd.loadAd(NativeAdBase.MediaCacheFlag.ALL);
    }


    private void inflateAd(NativeBannerAd nativeBannerAd, View adView) {
        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        MediaView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Setting the Text
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());

        // You can use the following to specify the clickable areas.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(
                mNativeBannerAdContainer,
                nativeAdIconView,
                clickableViews);
    }
}
