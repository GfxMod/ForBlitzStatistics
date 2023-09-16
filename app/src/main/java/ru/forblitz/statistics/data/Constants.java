package ru.forblitz.statistics.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.forblitz.statistics.R;

public class Constants {

    public static final String forBlitzBaseUrl = "https://forblitz.ru/";

    public static int TABS_COUNT = 4;

    public static String stringResourcesDescriptionPostfix = "_description";

    public static long okHttpTimeout = 90;

    public static class MainViewFlipperItems {
        public static final int ENTER_NICKNAME = 0;
        public static final int LOADING = 1;
        public static final int STATISTICS = 2;
        public static final int SETTINGS = 3;
    }

    public static class StatisticsViewFlipperItems {
        public static final int STATISTICS = 0;
        public static final int FALSE = 1;
    }

    public static class ClanViewFlipperItems {
        public static final int IS_A_MEMBER = 0;
        public static final int NOT_IS_A_MEMBER = 1;
    }

    public static class ClanInfoViewFlipperItems {
        public static final int INFO = 0;
        public static final int RECRUITING_OPTIONS = 1;
    }

    public static class PreferencesSwitchesTags {
        public static String averageDamageRounding = "average_damage_rounding";

        public static String logDisplay = "log_display";

        public static String haptics = "haptics";

    }

    public static final Map<String, String> baseUrl = new HashMap<>();
    static {
        baseUrl.put("ru", "https://api.wotblitz.ru/");
        baseUrl.put("eu", "https://api.wotblitz.eu/");
        baseUrl.put("na", "https://api.wotblitz.com/");
        baseUrl.put("asia", "https://api.wotblitz.asia/");
    }

    public static final Map<String, String> url = new HashMap<>();
    static {
        url.put("getAccountId", "wotb/account/list/?application_id=APP_ID");
        url.put("getUsers", "wotb/account/info/?application_id=APP_ID&extra=statistics.rating");
        url.put("getClanInfo", "wotb/clans/accountinfo/?application_id=APP_ID&extra=clan");
        url.put("getFullClanInfo", "wotb/clans/info/?application_id=APP_ID&extra=members");
        url.put("getAchievements", "wotb/account/achievements/?application_id=APP_ID&fields=-max_series");
        url.put("getAllInformationAboutVehicles", "wotb/encyclopedia/vehicles/?application_id=APP_ID&fields=name,nation,tier,type");
        url.put("getTankStatistics", "wotb/tanks/stats/?application_id=APP_ID");
    }

    public static class Steps {
        public static final double[] battles = new double[] { 2000, 10000, 25000 };
        public static final double[] winRate = new double[] { 50, 60, 70 };
        public static final double[] averageDamage = new double[] { 1000, 1500, 2000 };
        public static final double[] efficiency = new double[] { 1, 1.25, 1.5 };
    }

    public static Map<String, Integer> localeCodes = new HashMap<>();
    static {
        localeCodes.put("ru", R.string.locale_ru);
        localeCodes.put("en", R.string.locale_en);
        localeCodes.put("uk", R.string.locale_uk);
    }

    public static Map<String, Integer> localeDescriptions = new HashMap<>();
    static {
        localeDescriptions.put("ru", R.string.locale_ru_desc);
        localeDescriptions.put("en", R.string.locale_en_desc);
        localeDescriptions.put("uk", R.string.locale_uk_desc);
    }

    public static ArrayList<String> preferencesTags = new ArrayList<>();
    static {
        preferencesTags.add(PreferencesSwitchesTags.averageDamageRounding);
        preferencesTags.add(PreferencesSwitchesTags.logDisplay);
        preferencesTags.add(PreferencesSwitchesTags.haptics);
    }

}
