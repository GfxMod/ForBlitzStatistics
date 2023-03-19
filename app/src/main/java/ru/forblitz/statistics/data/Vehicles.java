package ru.forblitz.statistics.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Vehicles {

    private final HashMap<String, Vehicle> list = new HashMap<>();

    private ApiResponse apiResponse;

    public HashMap<String, Vehicle> getList() {
        return list;
    }

    public int getCount() { return apiResponse.meta.count; }

    public void fillVehiclesSpecifications(String json) {

        Gson gson = new Gson();
        apiResponse = gson.fromJson(json, ApiResponse.class);

        for (Map.Entry<String, Vehicle> entry : apiResponse.data.entrySet()) {
            Vehicle vehicle = entry.getValue();
            vehicle.setTankId(entry.getKey());
            list.put(entry.getKey(), vehicle);
        }

    }

    public void fillVehiclesStatistics(String json) {

        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonObject data = jsonObject.getAsJsonObject("data");
        try {
            JsonArray dataArray = data.entrySet().iterator().next().getValue().getAsJsonArray();

            for (JsonElement element : dataArray) {
                JsonObject vehicleObject = element.getAsJsonObject();
                Vehicle vehicle = new Gson().fromJson(vehicleObject, Vehicle.class);
                Objects.requireNonNull(list.get(vehicle.getTankId())).setData(vehicle.getData());
                Objects.requireNonNull(list.get(vehicle.getTankId())).calculate();
            }
        } catch (java.util.NoSuchElementException e) {
            Log.e(e.toString(), "no element in current vehicles statistics JSON");
        }

    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Vehicle vehicle : list.values().toArray(new Vehicle[0])) {
            result.append(vehicle).append("\n");
        }
        return result.toString();
    }

    public void clear() {
        for (Vehicle vehicle : list.values().toArray(new Vehicle[0])) {
            vehicle.clear();
        }
    }

}

class ApiResponse {
    Meta meta;
    Map<String, Vehicle> data;
}

class Meta {
    int count;
}