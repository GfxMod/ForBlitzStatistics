package ru.forblitz.statistics.data;

public class Session {

    private String path;
    private Runnable set;
    private Runnable delete;
    private boolean isSelected;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Runnable getSet() {
        return set;
    }

    public void setSet(Runnable set) {
        this.set = set;
    }

    public Runnable getDelete() {
        return delete;
    }

    public void setDelete(Runnable delete) {
        this.delete = delete;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
