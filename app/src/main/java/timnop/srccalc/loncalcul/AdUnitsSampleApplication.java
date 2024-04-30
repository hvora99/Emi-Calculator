package timnop.srccalc.loncalcul;

import android.app.Application;

import com.facebook.ads.AudienceNetworkAds;


public class AdUnitsSampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (AudienceNetworkAds.isInAdsProcess(this)) {
            return;
        } // else execute default application initialization code


        AudienceNetworkInitializeHelper.initialize(this);



    }
}
