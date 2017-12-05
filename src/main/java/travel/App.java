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
    static LocalDate finishDate = LocalDate.parse("23.02.2018", formatter);

    public static void main( String[] args )
    {
        ArrayList<Double> cheapestPrices = new ArrayList<>();
        NumberFormat numFormat = new DecimalFormat("#0");
        Configuration.browser = "marionette";
        Configuration.headless = true;
        System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Tau\\IdeaProjects\\searching\\src\\main\\resources\\chromedriver.exe");
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");

        Period p = Period.between(startDate, finishDate);
        LocalDate date = LocalDate.parse(startDate.format(formatter), formatter);
        for (int i=0; i<p.getDays()+1; i++){
            log.info("Starting search for "+date.format(formatter));
            log.info("https://level.travel/search/St.Petersburg-RU-to-Phuket-TH-departure-"+date.format(formatter)+"-for-4..8-nights-2-adults-0-kids-1..5-stars");
            open("https://level.travel/search/St.Petersburg-RU-to-Phuket-TH-departure-"+date.format(formatter)+"-for-4..8-nights-2-adults-0-kids-1..5-stars");
            $(".loading-block").waitWhile(visible, 40000);
            SelenideElement resultsList = $("#search_results").should(not(empty));
            ElementsCollection prices = resultsList.$$(".price");
            //форматируем список
            ArrayList<Double> formattedList = new ArrayList<>();
            for (SelenideElement e : prices) {
                String format = e.getText();
                int index = format.indexOf("\u20BD");
                format = format.substring(0, index).replaceAll(" ", "");
                formattedList.add(Double.valueOf(format));
            }
            System.out.println(prices.texts());
            Double min = Collections.min(formattedList);
            log.info("Minimum price of the day "+numFormat.format(min));
            cheapestPrices.add(Collections.min(formattedList));
            date = date.plusDays(1);
        }
        log.info("Cheapest price of period "+numFormat.format(Collections.min(cheapestPrices)));

    }
}
