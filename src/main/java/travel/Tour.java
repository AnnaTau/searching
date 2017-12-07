package travel;

import java.time.LocalDate;

public class Tour {
    private Integer price;
    private LocalDate date;
    private String url;

    public Tour(Integer price, LocalDate date, String url) {
        this.price = price;
        this.date = date;
        this.url = url;
    }

    public Integer getPrice() {
        return price;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
