package travel;

public enum From {
    PETERBURG("St.Petersburg"), MOSCOW("Moscow");

    private String name;

    From(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
