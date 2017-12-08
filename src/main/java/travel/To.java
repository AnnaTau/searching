package travel;

public enum To {
    PHUKET("Phuket-TH"), SAMUI("Koh.Samui-TH");

    To(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public String toString() {
        return this.name;
    }
}
