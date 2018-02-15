package travel;

public enum To {
    PHUKET("Phuket-TH"), SAMUI("Koh.Samui-TH"), THAI("Any-TH"), EILAT("Eilat-IL"), PRAGUE("Prague-CZ"), SRILANKA("Any-LK");

    To(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public String toString() {
        return this.name;
    }
}
