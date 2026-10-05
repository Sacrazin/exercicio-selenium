package br.com.aulaSelenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AutomationExerciseTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "http://automationexercise.com";

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Test Case 3: Login User with incorrect email and password
     */
    @Test
    @DisplayName("Test Case 3: Login com e-mail e senha incorretos")
    public void testLoginComCredenciaisIncorretas() {
        driver.get(BASE_URL);

        assertTrue(driver.getTitle().contains("Automation Exercise"), "Home page não carregou!");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '/login')]"))).click();

        WebElement loginTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Login to your account']")));
        assertTrue(loginTitle.isDisplayed());

        driver.findElement(By.cssSelector("input[data-qa='login-email']")).sendKeys("usuario_errado@teste.com");
        driver.findElement(By.cssSelector("input[data-qa='login-password']")).sendKeys("SenhaIncorreta123");

        driver.findElement(By.cssSelector("button[data-qa='login-button']")).click();

        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(), 'Your email or password is incorrect!')]")
        ));
        assertTrue(errorMessage.isDisplayed());
        assertEquals("Your email or password is incorrect!", errorMessage.getText().trim());
    }

    /**
     * Test Case 3 (Parametrizado): 5 entradas de Particionamento de Equivalência e Análise do Valor Limite
     */
    @ParameterizedTest(name = "Entrada {index}: email=''{0}'', senha=''{1}''")
    @DisplayName("Test Case 3 Parametrizado: 5 entradas (PCE e AVL)")
    @CsvSource({
        "usuario_inexistente@dominio.com, Senha123",
        "cadastrado@teste.com, SenhaErrada",
        "outro_usuario@empresa.com.br, 12345678",
        "usuario_valido@dominio.com, a",
        "usuario_generico@teste.com, SenhaMuitoLongaComMaisDe30Caracteres"
    })
    public void testLoginMultiplasEntradasPCEeAVL(String email, String senha) {
        driver.get(BASE_URL);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '/login')]"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Login to your account']")));

        driver.findElement(By.cssSelector("input[data-qa='login-email']")).sendKeys(email);
        driver.findElement(By.cssSelector("input[data-qa='login-password']")).sendKeys(senha);
        driver.findElement(By.cssSelector("button[data-qa='login-button']")).click();

        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(), 'Your email or password is incorrect!')]")
        ));
        assertTrue(errorMessage.isDisplayed());
        assertEquals("Your email or password is incorrect!", errorMessage.getText().trim());
    }

    /**
     * Test Case 1: Register User
     */
    @Test
    @DisplayName("Test Case 1: Registrar Usuário e excluir conta")
    public void testRegistrarUsuarioEExcluirConta() {
        long timestamp = System.currentTimeMillis();
        String nomeUsuario = "Aluno UFF " + (timestamp % 10000);
        String emailUsuario = "alunouff_" + timestamp + "@teste.com";

        driver.get(BASE_URL);
        assertTrue(driver.getTitle().contains("Automation Exercise"), "Home page não carregou.");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '/login')]"))).click();

        WebElement signupTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='New User Signup!']")));
        assertTrue(signupTitle.isDisplayed());

        driver.findElement(By.cssSelector("input[data-qa='signup-name']")).sendKeys(nomeUsuario);
        driver.findElement(By.cssSelector("input[data-qa='signup-email']")).sendKeys(emailUsuario);

        driver.findElement(By.cssSelector("button[data-qa='signup-button']")).click();

        WebElement enterAccountInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//b[contains(text(), 'Enter Account Information')]")
        ));
        assertTrue(enterAccountInfo.isDisplayed());

        driver.findElement(By.id("id_gender1")).click();
        driver.findElement(By.id("password")).sendKeys("SenhaForte@2026");

        new Select(driver.findElement(By.id("days"))).selectByValue("15");
        new Select(driver.findElement(By.id("months"))).selectByValue("5");
        new Select(driver.findElement(By.id("years"))).selectByValue("1998");

        WebElement chkNewsletter = driver.findElement(By.id("newsletter"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", chkNewsletter);
        chkNewsletter.click();

        WebElement chkOptin = driver.findElement(By.id("optin"));
        chkOptin.click();

        driver.findElement(By.id("first_name")).sendKeys("Aluno");
        driver.findElement(By.id("last_name")).sendKeys("Teste");
        driver.findElement(By.id("company")).sendKeys("Universidade Federal Fluminense");
        driver.findElement(By.id("address1")).sendKeys("Rua Passo da Patria, 156");
        driver.findElement(By.id("address2")).sendKeys("Bloco E");

        new Select(driver.findElement(By.id("country"))).selectByVisibleText("Canada");

        driver.findElement(By.id("state")).sendKeys("Ontario");
        driver.findElement(By.id("city")).sendKeys("Toronto");
        driver.findElement(By.id("zipcode")).sendKeys("M5V 2T6");
        driver.findElement(By.id("mobile_number")).sendKeys("+5521999998888");

        WebElement createAccountBtn = driver.findElement(By.cssSelector("button[data-qa='create-account']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", createAccountBtn);
        createAccountBtn.click();

        WebElement accountCreated = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("h2[data-qa='account-created']")
        ));
        assertTrue(accountCreated.isDisplayed());
        assertEquals("ACCOUNT CREATED!", accountCreated.getText().trim().toUpperCase());

        driver.findElement(By.cssSelector("a[data-qa='continue-button']")).click();

        WebElement loggedInText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//li[contains(., 'Logged in as')]")
        ));
        assertTrue(loggedInText.isDisplayed());
        assertTrue(loggedInText.getText().contains(nomeUsuario));

        driver.findElement(By.xpath("//a[contains(@href, '/delete_account')]")).click();

        WebElement accountDeleted = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("h2[data-qa='account-deleted']")
        ));
        assertTrue(accountDeleted.isDisplayed());
        assertEquals("ACCOUNT DELETED!", accountDeleted.getText().trim().toUpperCase());

        driver.findElement(By.cssSelector("a[data-qa='continue-button']")).click();
    }
}