import com.codeborne.selenide.Condition;
import com.codeborne.selenide.*;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.google.gson.stream.JsonWriter;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;


import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.Assert.assertEquals;

public class Test {

    public static void setUp() {
        Configuration.holdBrowserOpen = true;
    }


    public static void tearDown() {
        closeWebDriver();
    }


    @org.junit.Test
    public void pethouseTest() {
        open("https://pethouse.ua/ua/shop/sobakam/suhoi-korm/");
        $("#tpl-logo").click();
        assert WebDriverRunner.url().equals("https://pethouse.ua/ua/");
        screenshot("Logo is working");
    }

    @org.junit.Test
    public void searchIsWorking() {
        open("https://pethouse.ua/ua/");
        $("#search").click();
        $("#search").setValue("їжа для собак").pressEnter();
        $("h1").shouldHave(text("Результати пошуку"));
        $("h1").shouldBe(Condition.visible);

    }

    @org.junit.Test// цей тест перевіряє розділи футера на клікабельність і на відсутність 404 поминки на сторінці
    public void ClickPaymentAnddelivery() {
        open("https://pethouse.ua/ua/");
        $(byText("Оплата та доставка")).click();
        $(byText("404")).shouldNot(exist);
        sleep(3000);
        back();
        $(byText("Компанія")).click();
        $(byText("404")).shouldNot(exist);
        sleep(3000);
        back();
        $(byText("Кар'єра")).click();
        $(byText("404")).shouldNot(exist);
        sleep(3000);
        back();
        $(byText("Постачальникам")).click();
        $(byText("404")).shouldNot(exist);
        sleep(3000);
        back();
        $(byText("Благодійність")).click();
        $(byText("404")).shouldNot(exist);
        sleep(3000);
        back();
        $(byText("Написати директору")).click();
        $(byText("404")).shouldNot(exist);
        sleep(3000);
        back();
        $(byText("Повернення товарів")).click();
        $(byText("404")).shouldNot(exist);
        sleep(3000);
        back();
        $(byText("Публічна оферта")).click();
        $(byText("404")).shouldNot(exist);
        sleep(3000);
        back();
        $(byText("Політика конфіденційності")).click();
        $(byText("404")).shouldNot(exist);
        sleep(3000);
        back();


    }

    @org.junit.Test //цей тест по черзі відкриває посилання на соц.мережі у футері
    public void SocialMedia() {
        open("https://pethouse.ua/ua/");
        JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        sleep(3000);
        $(new By.ByXPath("//img[@alt='facebook']")).click();
        sleep(5000);
        switchTo().window(1); // Перемикаємося на нову вкладку
        closeWindow(); // Закриваємо вкладку
        switchTo().window(0); // Повертаємося на головну вкладку
        $(new By.ByXPath("//img[@alt='youtube']")).click();
        sleep(5000);
        switchTo().window(1);
        closeWindow();
        switchTo().window(0);
        $(new By.ByXPath("//img[@alt='instagram']")).click();
        sleep(5000);
        switchTo().window(1);
        closeWindow();
        switchTo().window(0);
        sleep(5000);
        Configuration.holdBrowserOpen = true;


    }

    @org.junit.Test//цей тест перевіряє наявність необхідних розділів головного меню
    public void CheckMenu() {
        open("https://pethouse.ua/ua/");
        // Знаходимо блок з id "block-id" і перевіряємо наявність тексту "відповідний текст" в цьому блоку
        $("#menu").shouldHave(text("Собакам")).click();
        $("#menu").shouldHave(text("Котам"));
        $("#menu").shouldHave(text("Тхорам"));
        $("#menu").shouldHave(text("Гризунам"));
        $("#menu").shouldHave(text("Рибам"));
        $("#menu").shouldHave(text("Птахам"));
        $("#menu").shouldHave(text("Рептиліям"));

    }

    @org.junit.Test
    //цей тест обирає в головному меню "Собакам"->Сухий корм->3 фільтра за брендом і сортування товару за популярністю
    public void SuhiyKorm() {
        open("https://pethouse.ua/ua/");
        $("#tpl-top-menu-first-li-1 > a").shouldHave(text("Собакам")).click();
        sleep(3000);
        $(byText("Сухий корм")).click();
        $(".ph-filters-table").shouldHave(text("Optimeal")).$(byText("Optimeal")).click();
        $(".ph-filters-table").shouldHave(text("Savory")).$(byText("Savory")).click();
        $(".ph-filters-table").shouldHave(text("Oven-Baked")).$(byText("Oven-Baked")).click();
        $("#z2-cat-sort").click();
        $("#z2-cat-sort").selectOptionByValue("popular");
        Configuration.holdBrowserOpen = true;


    }

    @org.junit.Test //в цьому тесті відкриваю сторінку Собакам-Шампуні,
    // додаю товар до корзини, потім збільшую товар на 1 більше і перевіряю чи змінилася кількість біля іконки корзини.
            // так товар збільшеється до 5
    public void AddItem() {
        open("https://pethouse.ua/ua/");
        $("#tpl-top-menu-first-li-1 > a").shouldHave(text("Собакам")).click();
        sleep(3000);
        $(byText("Шампуні")).click();
        $(".tpl-unit-buy").click();
        sleep(5000);
        $(".goods-counter-wrapper").shouldBe(Condition.visible).shouldHave(text("1"));;
        int numberOfIncrements = 4; // На яку кількість буде збільшено товар
        SelenideElement quantityElement = $x("//*[@id=\"header-main\"]/section[2]/div/div[1]/div[5]/div[1]/a/span[2]");
        for (int i = 0; i < numberOfIncrements; i++) {
            String initialValue = quantityElement.getText();
            $(By.cssSelector("#increment-good")).click(); // Збільшення значення на 1
            int expectedValue = Integer.parseInt(initialValue) + 1;
            quantityElement.shouldHave(text(String.valueOf(expectedValue)));
            Configuration.holdBrowserOpen = true;
        }
    }
}
