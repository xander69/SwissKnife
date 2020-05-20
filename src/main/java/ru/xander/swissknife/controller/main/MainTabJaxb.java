package ru.xander.swissknife.controller.main;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.springframework.util.StringUtils;
import ru.xander.swissknife.controller.MainController;
import ru.xander.swissknife.util.Dialog;

import java.io.File;

/**
 * @author Alexander Shakhov
 */
@SuppressWarnings("WeakerAccess")
public class MainTabJaxb {

    private static final Font FONT_CONSOLAS = Font.font("Consolas");

    private final MainController main;
    private final FileChooser wsdlChooser = new FileChooser();
    private final DirectoryChooser sourceChooser = new DirectoryChooser();

    private MainTabJaxb(MainController main) {
        this.main = main;
    }

    private void initializeTab() {
        main.textJaxbWsdlFile.textProperty().bindBidirectional(main.state.jaxbWsdlFileProperty());
        main.textJaxbSourcePath.textProperty().bindBidirectional(main.state.jaxbSourcePathProperty());
        main.textJaxbTargetPackage.textProperty().bindBidirectional(main.state.jaxbTargetPackageProperty());
        main.buttonChooseWsdlFile.setOnAction(event -> chooseWsdlPath());
        main.buttonChooseSourcePath.setOnAction(event -> chooseSourcePath());
        main.buttonWsImportHelp.setOnAction(event -> showWsImportHelp());
        main.buttonJaxbGenerate.setOnAction(event -> generateJaxbCode());

        main.textJaxbLog.setFont(FONT_CONSOLAS);
        main.textJaxbLog.setEditable(false);

        wsdlChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("WSDL and XSD", "*.wsdl;*.xsd"),
                new FileChooser.ExtensionFilter("WSDL", "*.wsdl"),
                new FileChooser.ExtensionFilter("XSD", "*.xsd")
        );
    }

    public static void initialize(MainController main) {
        new MainTabJaxb(main).initializeTab();
    }

    public void chooseWsdlPath() {
        if (!StringUtils.isEmpty(main.textJaxbWsdlFile.getText())) {
            File initialFile = new File(main.textJaxbWsdlFile.getText());
            if (initialFile.exists()) {
                wsdlChooser.setInitialDirectory(initialFile.getParentFile());
            }
        } else {
            wsdlChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        }
        File wsdlFile = wsdlChooser.showOpenDialog(main.scene.getWindow());
        if (wsdlFile != null) {
            main.textJaxbWsdlFile.setText(wsdlFile.getAbsolutePath());
        }
    }

    public void chooseSourcePath() {
        if (!StringUtils.isEmpty(main.textJaxbSourcePath.getText())) {
            File initialDirectory = new File(main.textJaxbSourcePath.getText());
            if (initialDirectory.exists()) {
                sourceChooser.setInitialDirectory(initialDirectory);
            }
        } else {
            sourceChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        }
        File sourcePath = sourceChooser.showDialog(main.scene.getWindow());
        if (sourcePath != null) {
            main.textJaxbSourcePath.setText(sourcePath.getAbsolutePath());
        }
    }

    private void showWsImportHelp() {
        Dialog.message()
                .title("WSImport Help")
                .content(WS_IMPORT_HELP)
                .width(655.0)
                .height(800.0)
                .font(FONT_CONSOLAS)
                .show();
    }

    public void generateJaxbCode() {
        TextArea textJaxbLog = main.textJaxbLog;
        textJaxbLog.clear();
        textJaxbLog.appendText(main.textJaxbWsdlFile.getText() + "\n");
        textJaxbLog.appendText(main.textJaxbSourcePath.getText() + "\n");
        textJaxbLog.appendText(main.textJaxbTargetPackage.getText() + "\n");
    }

    private static final String WS_IMPORT_HELP = "Usage: wsimport [options] <WSDL_URI>\n" +
            "\n" +
            "where [options] include:\n" +
            "  -b <path>                 specify jaxws/jaxb binding files or additional schemas\n" +
            "                            (Each <path> must have its own -b)\n" +
            "  -B<jaxbOption>            Pass this option to JAXB schema compiler\n" +
            "  -catalog <file>           specify catalog file to resolve external entity references\n" +
            "                            supports TR9401, XCatalog, and OASIS XML Catalog format.\n" +
            "  -classpath <path>         specify where to find user class files and wsimport extensions\n" +
            "  -cp <path>                specify where to find user class files and wsimport extensions\n" +
            "  -d <directory>            specify where to place generated output files\n" +
            "  -encoding <encoding>      specify character encoding used by source files\n" +
            "  -extension                allow vendor extensions - functionality not specified\n" +
            "                            by the specification.  Use of extensions may\n" +
            "                            result in applications that are not portable or\n" +
            "                            may not interoperate with other implementations\n" +
            "  -help                     display help\n" +
            "  -httpproxy:<proxy>        set a HTTP proxy. Format is [user[:password]@]proxyHost:proxyPort\n" +
            "                            (port defaults to 8080)\n" +
            "  -J<javacOption>           pass this option to javac\n" +
            "  -keep                     keep generated files\n" +
            "  -p <pkg>                  specifies the target package\n" +
            "  -quiet                    suppress wsimport output\n" +
            "  -s <directory>            specify where to place generated source files\n" +
            "  -target <version>         generate code as per the given JAXWS spec version\n" +
            "                            Defaults to 2.2, Accepted values are 2.0, 2.1 and 2.2\n" +
            "                            e.g. 2.0 will generate compliant code for JAXWS 2.0 spec\n" +
            "  -verbose                  output messages about what the compiler is doing\n" +
            "  -version                  print version information\n" +
            "  -fullversion              print full version information\n" +
            "  -wsdllocation <location>  @WebServiceClient.wsdlLocation value\n" +
            "  -clientjar <jarfile>      creates the jar file of the generated artifacts along with the\n" +
            "                            WSDL metadata required for invoking the web service.\n" +
            "  -generateJWS              generate stubbed JWS implementation file\n" +
            "  -implDestDir <directory>  specify where to generate JWS implementation file\n" +
            "  -implServiceName <name>   local portion of service name for generated JWS implementation\n" +
            "  -implPortName <name>      local portion of port name for generated JWS implementation\n" +
            "\n" +
            "Extensions:\n" +
            "  -XadditionalHeaders              map headers not bound to request or response message to\n" +
            "                                   Java method parameters\n" +
            "  -Xauthfile                       file to carry authorization information in the format\n" +
            "                                   http://username:password@example.org/stock?wsdl\n" +
            "  -Xdebug                          print debug information\n" +
            "  -Xno-addressing-databinding      enable binding of W3C EndpointReferenceType to Java\n" +
            "  -Xnocompile                      do not compile generated Java files\n" +
            "  -XdisableAuthenticator           disable Authenticator used by JAX-WS RI,\n" +
            "                                   -Xauthfile option will be ignored if set\n" +
            "  -XdisableSSLHostnameVerification disable the SSL Hostname verification while fetching\n" +
            "                                   wsdls\n" +
            "\n" +
            "Examples:\n" +
            "  wsimport stock.wsdl -b stock.xml -b stock.xjb\n" +
            "  wsimport -d generated http://example.org/stock?wsdl";
}