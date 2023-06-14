package modulos.produtos;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import paginas.LoginPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

@DisplayName("Testes Web do Modulo de Produtos")
public class ProdutosTest {

    private WebDriver navegador;
    String username;
    String password;
    String baseUrl;

    {
        try (InputStream input = new FileInputStream("C:\\Users\\Chris\\IdeaProjects\\lojinhaWebAutomacao\\src\\test\\resources\\config.properties")) {

            Properties prop = new Properties();
            prop.load(input);
            baseUrl = prop.getProperty("db.url");
            username = prop.getProperty("db.user");
            password = prop.getProperty("db.password");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void beforeEach(){
        // Abrir o navegador
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        System.setProperty("webdriver.chrome.driver", "C:\\drivers\\chromedriver114\\chromedriver.exe");

        // Configurações
        this.navegador = new ChromeDriver(options);
        this.navegador.manage().window().maximize();
        this.navegador.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        this.navegador.get(baseUrl);

    }

    @Test
    @DisplayName("A Não e permitido registrar um produto com valor igual a zero")
    public void testNaoEPermitidoRegistrarProdutoComValorIgualAZero(){
        // Vou fazer login
        String mensagemApresentada = new LoginPage(navegador)
                .informarOUsuario(username)
                .informarASenha(password)
                .submeterFormularioDeLogin()
                .acessarFormularioDeAdicaoNovoProduto()
                .informarNomeDoProduto("Macbook Pro")
                .informarValorDoProduto("0")
                .informarCoresDoProduto("preto, branco")
                .submeterFormularioDeAdicaoComErro()
                .capturarMensagemApresentada();

        Assertions.assertEquals("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00", mensagemApresentada);
    }

    @Test
    @DisplayName("B Não e permitido registrar um produto com valor maior que sete mil")
    public void testNaoEPermitidoRegistrarProdutoComValorMaiorQueSeteMil(){
        String mensagemApresentada = new LoginPage(navegador)
                .informarOUsuario(username)
                .informarASenha(password)
                .submeterFormularioDeLogin()
                .acessarFormularioDeAdicaoNovoProduto()
                .informarNomeDoProduto("Macbook Pro")
                .informarValorDoProduto("8000.00")
                .informarCoresDoProduto("preto, branco")
                .submeterFormularioDeAdicaoComErro()
                .capturarMensagemApresentada();

        Assertions.assertEquals("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00", mensagemApresentada);
    }

    @Test
    @DisplayName("C E permitido registrar um produto que estejam no limite de 0,01")
    public void testPossoAdicionarProdutosComValorDeUmCentavo(){
        String mensagemApresentada = new LoginPage(navegador)
                .informarOUsuario(username)
                .informarASenha(password)
                .submeterFormularioDeLogin()
                .acessarFormularioDeAdicaoNovoProduto()
                .informarNomeDoProduto("Macbook Pro")
                .informarValorDoProduto("001")
                .informarCoresDoProduto("preto, branco")
                .submeterFormularioDeAdicaoComSucesso()
                .capturarMensagemApresentada();

        Assertions.assertEquals("Produto adicionado com sucesso", mensagemApresentada);
    }

    @Test
    @DisplayName("D E permitido registrar um produto que estejam no limite de 7.000,00")
    public void testPossoAdicionarProdutosComValorDeSeteMilReais(){
        String mensagemApresentada = new LoginPage(navegador)
                .informarOUsuario(username)
                .informarASenha(password)
                .submeterFormularioDeLogin()
                .acessarFormularioDeAdicaoNovoProduto()
                .informarNomeDoProduto("Macbook Pro")
                .informarValorDoProduto("700001")
                .informarCoresDoProduto("preto, branco")
                .submeterFormularioDeAdicaoComSucesso()
                .capturarMensagemApresentada();

        Assertions.assertEquals("Produto adicionado com sucesso", mensagemApresentada);
    }

    @AfterEach
    public void afterEach(){
        //Fechar o navegador
        this.navegador.quit();
    }
}
