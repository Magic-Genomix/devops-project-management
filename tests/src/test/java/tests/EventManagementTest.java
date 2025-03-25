
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.*;

import java.net.MalformedURLException;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.By;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.net.URL;  // Ajoutez cet import pour la classe URL
import java.time.Duration;

public class EventManagementTest {

    private WebDriver driver;
    private WebDriverWait wait;

    // Initialisation du WebDriver et WebDriverWait
   /* @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\kathl\\Downloads\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless"); // Exécuter en mode headless (optionnel)
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    } */

          // Initialisation du WebDriver et WebDriverWait
          @BeforeEach
          public void setUp() throws MalformedURLException {
              ChromeOptions options = new ChromeOptions();
              // Temporarily remove options
              // options.addArguments("--disable-gpu");
              // options.addArguments("--no-sandbox");
              // options.addArguments("--window-size=1920x1080");
              //options.addArguments("--headless");
              URL seleniumHubUrl = new URL("http://localhost:4444/wd/hub"); // or http://selenium-hub:4444/wd/hub

              DesiredCapabilities capabilities = new DesiredCapabilities();
              capabilities.setCapability(ChromeOptions.CAPABILITY, options);

              driver = new RemoteWebDriver(seleniumHubUrl, capabilities);

              // Increase timeouts
              driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
              driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
              driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(60));

              wait = new WebDriverWait(driver, Duration.ofSeconds(30));
          }

    // Fermer le navigateur après le test
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Étape 1: Ouvrir la page principale
    public void openHomePage() {
        //driver.get("http://localhost:4200");
        driver.get("localhost:4444/event-frontend/");
        System.out.println("Page d'accueil ouverte...");
        try {
            Thread.sleep(1500); // Attendre un peu après le scroll
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Étape 2: Se connecter à l'application
    public void login(String username, String password) {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[routerLink='/login']"))).click();
        System.out.println("Clique sur le lien Login...");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".auth-form")));

        driver.findElement(By.id("username")).sendKeys(username);
        System.out.println("Nom d'utilisateur saisi...");
        driver.findElement(By.id("password")).sendKeys(password);
        System.out.println("Mot de passe saisi...");

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        System.out.println("Formulaire soumis...");
        handleAlert();
    }

    // Étape 2: Se connecter à l'application
    public void OpenLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[routerLink='/login']"))).click();
        System.out.println("Clique sur le lien Login...");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".auth-form")));
        try {
            Thread.sleep(1500); // Attendre un peu après le scroll
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Étape 2: Se connecter à l'application
    public void OpenRegister() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[routerLink='/registration']"))).click();
        System.out.println("Clique sur le lien Login...");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".auth-form")));
        try {
            Thread.sleep(1500); // Attendre un peu après le scroll
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Gérer les alertes
    private void handleAlert() {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            System.out.println("Alerte détectée...");
            alert.accept();
            System.out.println("Alerte fermée avec OK...");
        } catch (NoAlertPresentException e) {
            System.out.println("Aucune alerte à gérer.");
        }
    }

    // Étape 3: Créer un événement
    public void createEvent(String title, String description, String date, String location) {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[routerLink='/create-event']"))).click();
        System.out.println("Clique sur le lien 'Create Event'...");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".event-form")));

        driver.findElement(By.id("title")).sendKeys(title);
        driver.findElement(By.id("description")).sendKeys(description);
        driver.findElement(By.id("date")).sendKeys(date);
        driver.findElement(By.id("location")).sendKeys(location);
        System.out.println("Détails de l'événement saisis...");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.cssSelector("button[type='submit']")));
        try {
            Thread.sleep(1500); // Attendre un peu après le scroll
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        scrollToElement(By.cssSelector("button[type='submit']"));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();
        System.out.println("Événement créé...");
        handleAlert();
    }

    // Faire défiler la page jusqu'à un élément
    private void scrollToElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    // Étape 4: Modifier un événement
    public void editEvent(String updatedTitle) {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[routerLink='/my-event']"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("table tbody tr:first-child td button"))).click();
        System.out.println("Clique sur le bouton 'Edit' du premier événement...");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".edit-event")));

        driver.findElement(By.id("title")).sendKeys(updatedTitle);
        System.out.println("Titre de l'événement modifié...");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.cssSelector("button[type='submit']")));
        try {
            Thread.sleep(1500); // Attendre un peu après le scroll
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        scrollToElement(By.cssSelector("button[type='submit']"));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();
        System.out.println("Événement modifié...");
        handleAlert();
    }

    // Étape 5: Supprimer un événement
    public void deleteEvent() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("table tbody tr:first-child td button:nth-child(2)"))).click();
        System.out.println("Clique sur le bouton 'Delete' du premier événement...");
        handleAlert();
    }

    // Étape 6: Retourner à la page Dashboard
    public void goToDashboard() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[routerLink='/dashboard']"))).click();
        System.out.println("Clique sur le lien 'Dash Event'...");
    }

    // Test de base pour ouvrir une page et interagir avec un bouton
    @Test
    public void testOpenPageAndClickButton() {
        // Ouvrir une page de test
        driver.get("localhost:4444/event-frontend/");
        System.out.println("Page ouverte...");
        openHomePage();
        OpenRegister();

        // Attendre que le bouton soit cliquable
        //WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.id("someButton")));
        //button.click();
        //System.out.println("Bouton cliqué...");

        // Vérification d'un message de confirmation après l'action
        //WebElement confirmationMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("confirmationMessage")));
        //System.out.println("Message de confirmation trouvé: " + confirmationMessage.getText());
    }

    // Test de gestion d'événements
    @Test
    public void testEventManagement() throws MalformedURLException {
        setUp();
        try {
            openHomePage();
            OpenRegister();
            OpenLogin();
            login("testuser@example.com", "newpassword");
            createEvent("Mon événement test", "Ceci est un test pour créer un événement.", "21/03/2026", "Paris, France");
            editEvent("Update");
            deleteEvent();
            goToDashboard();
        } finally {
            tearDown();
        }
    }

}
