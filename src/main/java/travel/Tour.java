package travel;

import java.time.LocalDate;

public class Tour {
    private Double price;
    private LocalDate date;
    private String url;

    public Tour(Double price, LocalDate date, String url) {
        this.price = price;
        this.date = date;
        this.url = url;
    }

    public Double getPrice() {
        return price;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
