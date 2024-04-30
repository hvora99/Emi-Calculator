package timnop.srccalc.loncalcul;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.MediaView;
import com.facebook.ads.MediaViewListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    private boolean isAdViewAdded;
    private @Nullable
    NativeBannerAd mNativeBannerAd;
    private NativeAdLayout mNativeBannerAdContainer;
    private LinearLayout mAdView;
    private FrameLayout mAdChoicesContainer;

    private @Nullable
    AdView bannerAdView;
    Boolean Istablate=false;
    private final String TAG = MainActivity.class.getSimpleName();



    final Context context = this;


    Dialog dialog;
    private @Nullable
    NativeAdLayout nativeAdLayout;
    private @Nullable LinearLayout adChoicesContainer;
    private @Nullable NativeAd nativeAd;

    private @Nullable AdOptionsView adOptionsView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);


        loadBnrAdView();

        Load_Native_Banner();

        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(35000);
        rotate.setInterpolator(new LinearInterpolator());

        ImageView image= (ImageView) findViewById(R.id.imageView);

        image.startAnimation(rotate);

        CardView st = (CardView)findViewById(R.id.cricketscore);
        st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent ( MainActivity.this, homescreen.class );
                startActivity ( i );
            }
        });

        CardView about = (CardView) findViewById(R.id.aboutus);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent ( MainActivity.this, Aboutus.class );
                startActivity ( i );
            }
        });

        CardView rateus_my = (CardView) findViewById(R.id.rate);
        rateus_my.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }
            }

        });

        CardView shareus = (CardView) findViewById(R.id.share);
        shareus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "Download Lone Score Application   \n"+"Install This Application\n\n"+ "http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                startActivity(Intent.createChooser(intent, "choose one"));
            }
        });

        CardView privacy = (CardView) findViewById(R.id.privacy);
        privacy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {

                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://loancalcemimortgageguide.blogspot.com/")));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://loancalcemimortgageguide.blogspot.com/")));

                }
            }

        });
    }



    public void onBackPressed() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.exitdialouge);

        Button yes = (Button) dialog.findViewById(R.id.btn_yes);
        final Button no = (Button) dialog.findViewById(R.id.btn_no);

        Load_Native_Ads();

//
        dialog.setCanceledOnTouchOutside(true);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.finish();
                dialog.dismiss();
            }
        });


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void Load_Native_Ads()
    {

        nativeAdLayout = dialog.findViewById(R.id.native_ad_container);

        adChoicesContainer = dialog.findViewById(R.id.ad_choices_container);

        nativeAd = new NativeAd(MainActivity.this, getResources().getString(R.string.nv));

        // Set a listener to get notified when the ad was loaded.
        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

                if (nativeAd == null || nativeAd != ad) {
                    // Race condition, load() called again before last ad was displayed
                    return;
                }

                if (nativeAdLayout == null) {
                    return;
                }

                // Unregister last ad
                nativeAd.unregisterView();



                if (adChoicesContainer != null) {
                    adOptionsView = new AdOptionsView(MainActivity.this, nativeAd, nativeAdLayout);
                    adChoicesContainer.removeAllViews();
                    adChoicesContainer.addView(adOptionsView, 0);
                }

                inflateAd(nativeAd, nativeAdLayout);

                // Registering a touch listener to log which ad component receives the touch event.
                // We always return false from onTouch so that we don't swallow the touch event (which
                // would prevent click events from reaching the NativeAd control).
                // The touch listener could be used to do animations.
                nativeAd.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            int i = view.getId();
                            if (i == R.id.native_ad_call_to_action) {
                                Log.d(TAG, "Call to action button clicked");
                            } else if (i == R.id.native_ad_media) {
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

        // When testing on a device, add its hashed ID to force test ads.
        // The hash ID is printed to log cat when running on a device and loading an ad.
        // AdSettings.addTestDevice("THE HASHED ID AS PRINTED TO LOG CAT");

        // Initiate a request to load an ad.
        nativeAd.loadAd();


    }



    private void inflateAd(NativeAd nativeAd, View adView) {
        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        nativeAdMedia.setListener(new MediaViewListener() {
            @Override
            public void onPlay(MediaView mediaView) {

            }

            @Override
            public void onVolumeChange(MediaView mediaView, float v) {

            }

            @Override
            public void onPause(MediaView mediaView) {

            }

            @Override
            public void onComplete(MediaView mediaView) {

            }

            @Override
            public void onEnterFullscreen(MediaView mediaView) {

            }

            @Override
            public void onExitFullscreen(MediaView mediaView) {

            }

            @Override
            public void onFullscreenBackground(MediaView mediaView) {

            }

            @Override
            public void onFullscreenForeground(MediaView mediaView) {

            }
        });

        // Setting the Text
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());


        // You can use the following to specify the clickable areas.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdIcon);
        clickableViews.add(nativeAdMedia);
        clickableViews.add(nativeAdCallToAction);
        nativeAd.registerViewForInteraction(
                nativeAdLayout,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);

        // Optional: tag views
        NativeAdBase.NativeComponentTag.tagView(nativeAdIcon, NativeAdBase.NativeComponentTag.AD_ICON);
        NativeAdBase.NativeComponentTag.tagView(nativeAdTitle, NativeAdBase.NativeComponentTag.AD_TITLE);
        NativeAdBase.NativeComponentTag.tagView(nativeAdBody, NativeAdBase.NativeComponentTag.AD_BODY);
        NativeAdBase.NativeComponentTag.tagView(nativeAdSocialContext, NativeAdBase.NativeComponentTag.AD_SOCIAL_CONTEXT);
        NativeAdBase.NativeComponentTag.tagView(nativeAdCallToAction, NativeAdBase.NativeComponentTag.AD_CALL_TO_ACTION);
    }




    public void Load_Native_Banner()
    {
        mNativeBannerAdContainer = findViewById(R.id.nativebanner);
        mNativeBannerAd = new NativeBannerAd(this, getResources().getString(R.string.nb));


        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
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
                        MainActivity.this,
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



    private void loadBnrAdView() {

        DisplayMetrics metrics = new DisplayMetrics();
        MainActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

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
        bannerAdView = new AdView(MainActivity.this, getResources().getString(R.string.banner),
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

        if (mNativeBannerAd != null) {
            mNativeBannerAd.destroy();
            mNativeBannerAd = null;
        }
        super.onDestroy();
    }
}
