package travel;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

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
    public static void main( String[] args )
    {
        Configuration.browser = "marionette";
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Tau\\IdeaProjects\\searching\\src\\main\\resources\\chromedriver.exe");

//        open("https://twitter.com/");
        open("https://level.travel/search/St.Petersburg-RU-to-Phuket-TH-departure-29.12.2017-for-4..8-nights-2-adults-0-kids-1..5-stars");
        SelenideElement resultsList = $("#search_results");
        ElementsCollection prices = resultsList.$$(".price");
        System.out.println(prices.texts());
    }
}
