package ru.forblitz.statistics.data;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final String lestaAppId = "1957e1f71656310342971b3b1aa2efef";
    public static final String wgAppId = "ac75820d6c10195c86370ec1bc9f21de";

    public static class MainViewFlipperItems {
        public static final int ENTER_NICKNAME = 0;
        public static final int LOADING = 1;
        public static final int STATISTICS = 2;
        public static final int SETTINGS = 3;
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

    public static final Map<String, String> apiId = new HashMap<>();
    static {
        apiId.put("ru", lestaAppId);
        apiId.put("eu", wgAppId);
        apiId.put("na", wgAppId);
        apiId.put("asia", wgAppId);
    }

    public static class Steps {
        public static final double[] battles = new double[] { 2000, 10000, 25000 };
        public static final double[] winRate = new double[] { 50, 60, 70 };
        public static final double[] averageDamage = new double[] { 1000, 1500, 2000 };
        public static final double[] efficiency = new double[] { 1, 1.25, 1.5 };
    }

}
