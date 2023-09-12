package ru.forblitz.statistics.service;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.yandex.mobile.ads.banner.BannerAdEventListener;
import com.yandex.mobile.ads.banner.BannerAdSize;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdError;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestConfiguration;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.interstitial.InterstitialAd;
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener;
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener;
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader;

import java.util.HashMap;
import java.util.Objects;

import ru.forblitz.statistics.utils.InterfaceUtils;

/**
 * The {@link AdService} class provides methods to handle advertisements in an
 * application.
 */
public class AdService {

    private final Activity activity;

    /**
     * This is a map, where the key is the banner ID, and the value is the
     * date of its last update.
     */
    private final HashMap<Integer, Long> banners = new HashMap<>();

    /**
     * Timestamp of the last impression of an {@link InterstitialAd}
     */
    private long dateOfTheLastImpression = System.currentTimeMillis();

    private final HashMap<String, String> adUnitIds;

    public AdService(Activity activity, HashMap<String, String> adUnitIds) {
        this.activity = activity;
        this.adUnitIds = adUnitIds;
    }

    /**
     * Sets the {@link BannerAdView} with the specified width and returns the updated {@link BannerAdView}.
     *
     * @param width the width of the banner ad view
     * @param adView the BannerAdView to be set
     * @return the updated {@link BannerAdView}
     */
    public BannerAdView setBanner(int width, BannerAdView adView) {

        ConstraintLayout.LayoutParams adViewLayoutParams = new ConstraintLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        adView.setLayoutParams(adViewLayoutParams);
        adView.setGravity(Gravity.BOTTOM);

        if (!banners.containsKey(adView.getId())) {
            banners.put(adView.getId(), 0L);
            adView.setAdUnitId(Objects.requireNonNull(adUnitIds.get("banner")));
            adView.setAdSize(BannerAdSize.stickySize(activity, InterfaceUtils.pxToDp(activity, width)));
        }

        // Создание объекта таргетирования рекламы.
        final AdRequest adRequest = new AdRequest.Builder().build();

        // Регистрация слушателя для отслеживания событий, происходящих в баннерной рекламе.
        adView.setBannerAdEventListener(new BannerAdEventListener() {

            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
                Log.e(adRequestError.toString(), adRequestError.getDescription());
            }

            @Override
            public void onAdClicked() {

            }

            @Override
            public void onLeftApplication() {

            }

            @Override
            public void onReturnedToApplication() {

            }

            @Override
            public void onImpression(@Nullable ImpressionData impressionData) {

            }
        });

        // Загрузка объявления.
        if (System.currentTimeMillis() - Objects.requireNonNull(banners.get(adView.getId())) >= 30000) {
            banners.replace(adView.getId(), System.currentTimeMillis());
            adView.loadAd(adRequest);
        }
        return adView;

    }

    /**
     * Shows an {@link InterstitialAd} and executes the specified runnable afterwards.
     *
     * @param runnable {@link Runnable} to be executed after the ad is shown or if ad loading fails
     */
    public void showInterstitial(Runnable runnable) {

        if (System.currentTimeMillis() - dateOfTheLastImpression >= 30000) {

            InterstitialAdLoader loader = new InterstitialAdLoader(activity);
            loader.setAdLoadListener(new InterstitialAdLoadListener() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                    interstitialAd.setAdEventListener(new InterstitialAdEventListener() {
                        @Override
                        public void onAdShown() {
                            runnable.run();
                            dateOfTheLastImpression = System.currentTimeMillis();
                        }

                        @Override
                        public void onAdFailedToShow(@NonNull AdError adError) {
                            Log.e(adError.toString(), adError.getDescription());
                            runnable.run();
                        }

                        @Override
                        public void onAdDismissed() { }

                        @Override
                        public void onAdClicked() { }

                        @Override
                        public void onAdImpression(@Nullable ImpressionData impressionData) { }

                    });

                    interstitialAd.show(activity);

                }

                @Override
                public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
                    Log.e(adRequestError.toString(), adRequestError.getDescription());
                    runnable.run();
                }

            });

            loader.loadAd(
                    new AdRequestConfiguration.Builder(Objects.requireNonNull(adUnitIds.get("interstitial"))).build()
            );

        } else {
            runnable.run();
        }

    }

}
