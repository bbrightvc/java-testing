public class IronMan {

    public IronMan() {
    }

    public IronMan(int version, String mainColor) {
        this.version = version;
        this.mainColor = mainColor;
    }

    public IronMan(int version, String mainColor, String secondaryColor) {
        this(version, mainColor);
        this.secondaryColor = secondaryColor;
    }


    private int version;
    private String mainColor;
    private String secondaryColor;
    private boolean isBeta;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getMainColor() {
        return mainColor;
    }

    public void setMainColor(String mainColor) {
        this.mainColor = mainColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public boolean isBeta() {
        return isBeta;
    }

    public void setBeta(boolean beta) {
        isBeta = beta;
    }
}
