package travel;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import travel.telegram.Bot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.sleep;
import static travel.From.*;
import static travel.To.*;

/**
 * Hello world!
 *
 */
public class App
{
    static Logger log = LoggerFactory.getLogger(App.class);
    static String csvFile = "";
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    static LocalDate startDate = LocalDate.parse("01.03.2018", formatter);
    static LocalDate finishDate = LocalDate.parse("05.03.2018", formatter);
    static String days = "5..9";
    static From from = MOSCOW;
    static To to = EILAT;

    public static void main( String[] args )
    {
        ApiContextInitializer.init(); // Инициализируем апи
        TelegramBotsApi botapi = new TelegramBotsApi();
        try {
            botapi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static File startSearch(String flyTo){
        Configuration.browser = "marionette";
        Configuration.headless = true;
        Configuration.timeout = 10000;
        System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Tau\\IdeaProjects\\searching\\src\\main\resources\\chromedriver.exe");
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
        try {
            return SearchTours(flyTo);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File SearchTours(String flyTo) throws IOException {
        to = To.valueOf(flyTo);
        log.debug(to.toString());
        ArrayList<Tour> cheapestPrices = new ArrayList<>();
        csvFile = "/home/user/Documents/"+from.name()+"-"+to.name()+" for "+days+" days "+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm-ss"))+".csv";
        FileWriter writer = new FileWriter(csvFile);
        long period = ChronoUnit.DAYS.between(startDate, finishDate);
        LocalDate date = LocalDate.parse(startDate.format(formatter), formatter);
        for (int i=0; i<period+1; i++){
            String url = "https://level.travel/search/"+from+"-RU-to-"+to+"-departure-"+date.format(formatter)+"-for-"+days+"-nights-2-adults-0-kids-1..5-stars";
            log.info("Starting search for "+date.format(formatter));
            open(url);
            SelenideElement loadingBlock = $(".loading-block").waitWhile(visible, 200000);
            log.debug("loading-block is visible? "+loadingBlock.is(visible));

            //Если туров нет
            SelenideElement emptyBlock = $(".no-results");
            SelenideElement resultsList = $("#search_results").waitUntil(not(empty), 20000);
            if (loadingBlock.is(not(visible)) && emptyBlock.is(not(visible))&&resultsList.is(empty)){
                log.debug("Невидимы оба блока");
                emptyBlock.waitUntil(visible, 5000);
            }
            log.debug("Emptyblock is visible? "+ emptyBlock.is(visible));
            if(emptyBlock.is(visible)){
                log.info("Нет туров на эту дату");
                date = date.plusDays(1);
                continue;
            }

            //Если туры есть
            ElementsCollection hotels = resultsList.$$(".hotel_select");
            //форматируем список
            ArrayList<Tour> dayList = new ArrayList<>();
            for (SelenideElement e : hotels) {
                String price = e.$(".price").getText();
                int index = price.indexOf("\u20BD");
                price = price.substring(0, index).replaceAll(" ", "");
                String urlTour = e.$(".select_button").attr("href");
                dayList.add(new Tour(Integer.valueOf(price), date, urlTour));
            }
            Tour min = dayList.stream().min(Comparator.comparing(Tour::getPrice)).get();
            log.info("Minimum price of the day "+min.getPrice());
            log.debug(url);
            log.info("------------------------");
            cheapestPrices.add(min);
            date = date.plusDays(1);
        }
        Tour cheapest = cheapestPrices.stream().min(Comparator.comparing(Tour::getPrice)).get();
        cheapestPrices.stream().sorted(Comparator.comparing(Tour::getPrice)).forEach(i-> System.out.println(i.getPrice()+" at "+i.getDate().format(formatter)+" url -> "+i.getUrl()));
        cheapestPrices.stream().sorted(Comparator.comparing(Tour::getPrice)).forEach(i-> {
            try {
                CSVUtils.writeLine(writer, Arrays.asList(i.getPrice().toString(), i.getDate().format(formatter), i.getUrl()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        log.info("Cheapest price of period "+cheapest.getPrice()+" with url -> "+cheapest.getUrl());
        writer.flush();
        writer.close();
        log.info("File written "+csvFile);
        return new File(csvFile);
    }

    public static void Test(){}
}