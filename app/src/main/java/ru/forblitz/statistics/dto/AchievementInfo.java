package ru.forblitz.statistics.dto;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Keep
public class AchievementInfo {

    @NonNull
    @SerializedName("achievement_id")
    public String achievementId = "";

    @SerializedName("section")
    public String section;

    @SerializedName("description")
    public String description;

    @SerializedName("name")
    public String name;

    @SerializedName("options")
    public AchievementInfo[] options;

    @NonNull
    @Override
    public String toString() {
        String result = "achievementId: " + achievementId;
        result += "\nname: " + name;
        result += "\nsection: " + section;
        result += "\ndescription: " + description;

        if (options != null) {
            result += "\noptions:";
            StringBuilder optionsBuilder = new StringBuilder();
            for (AchievementInfo option : options) {
                optionsBuilder.append("\n").append(option.toString());
            }
            result += optionsBuilder.toString();
        }

        return result;
    }
}
