package ru.forblitz.statistics.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Map;

public class Vehicles {

    private final ArrayList<Vehicle> list = new ArrayList<>();

    // TODO: Заменить ArrayList на Map
    // Мы, кажется, не проходили, что это такое, но
    // позволит хранить пары значений номер танка -> танк
    // private final Map<String, Vehicle> = new HashMap<>();

    private ApiResponse apiResponse;

    public ArrayList<Vehicle> getList() {
        return list;
    }

    public int getCount() { return apiResponse.meta.count; }

    public void fillVehiclesSpecifications(String json) {

        Gson gson = new Gson();
        apiResponse = gson.fromJson(json, ApiResponse.class);

        for (Map.Entry<String, Vehicle> entry : apiResponse.data.entrySet()) {
            Vehicle vehicle = entry.getValue();
            vehicle.setTankId(entry.getKey());
            list.add(vehicle);
        }

    }

    public void fillVehiclesStatistics(String json) {

        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonObject data = jsonObject.getAsJsonObject("data");
        // TODO: а что если пустой объект? next() может бросить исключение
        JsonArray dataArray = data.entrySet().iterator().next().getValue().getAsJsonArray();

        for (JsonElement element : dataArray) {
            JsonObject vehicleObject = element.getAsJsonObject();
            Vehicle vehicle = new Gson().fromJson(vehicleObject, Vehicle.class);
            // TODO: отказаться от очень долгих indexOf -> получить у map значение по ключу можно так: list.get(КЛЮЧ)
            list.get(indexOf(vehicle.getTankId())).setData(vehicle.getData());
            list.get(indexOf(vehicle.getTankId())).calculate();
        }

    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Vehicle vehicle : list) {
            result.append(vehicle).append("\n");
        }
        return result.toString();
    }

    public int indexOf(String tankId) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTankId().equals(tankId)) {
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        for (Vehicle vehicle : list) {
            vehicle.clear();
        }
    }

}

class ApiResponse {
    String status;
    Meta meta;
    Map<String, Vehicle> data;
}

class Meta {
    int count;
}