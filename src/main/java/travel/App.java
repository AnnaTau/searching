package travel;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selenide.*;

/**
 * Hello world!
 *
 */
public class App
{
    static Logger log = LoggerFactory.getLogger(App.class);
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    static LocalDate startDate = LocalDate.parse("13.02.2018", formatter);
    static LocalDate finishDate = LocalDate.parse("08.03.2018", formatter);

    public static void main( String[] args )
    {
        Configuration.browser = "marionette";
        Configuration.headless = true;
        Configuration.timeout = 10000;
        System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Tau\\IdeaProjects\\searching\\src\\main\\resources\\chromedriver.exe");
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");

        NumberFormat numFormat = new DecimalFormat("#0");
        ArrayList<Tour> cheapestPrices = new ArrayList<>();
        Period p = Period.between(startDate, finishDate);
        LocalDate date = LocalDate.parse(startDate.format(formatter), formatter);
        for (int i=0; i<p.getDays()+1; i++){
            String url = "https://level.travel/search/St.Petersburg-RU-to-Phuket-TH-departure-"+date.format(formatter)+"-for-9..11-nights-2-adults-0-kids-1..5-stars";
            log.info("Starting search for "+date.format(formatter));
            log.info(url);
            open(url);
            $(".loading-block").waitWhile(visible, 50000);
            SelenideElement resultsList = $("#search_results").should(not(empty));
            ElementsCollection hotels = resultsList.$$(".hotel_select");
            //форматируем список
            ArrayList<Tour> dayList = new ArrayList<>();
            for (SelenideElement e : hotels) {
                String price = e.$(".price").getText();
                int index = price.indexOf("\u20BD");
                price = price.substring(0, index).replaceAll(" ", "");
                String urlTour = e.$(".select_button").attr("href");
                dayList.add(new Tour(Double.valueOf(price), date, urlTour));
            }
            System.out.println(dayList.stream().map(Tour::getPrice).collect(Collectors.toList()));
            Tour min = dayList.stream().min(Comparator.comparing(Tour::getPrice)).get();
            log.info("Minimum price of the day "+numFormat.format(min.getPrice())+" with url -> "+min.getUrl());
            log.info("------------------------");
            cheapestPrices.add(min);
            date = date.plusDays(1);
        }
        Tour cheapest = cheapestPrices.stream().min(Comparator.comparing(Tour::getPrice)).get();
        log.info("Cheapest price of period "+numFormat.format(cheapest.getPrice())+" with url -> "+cheapest.getUrl());
    }
}
