package ru.xander.swissknife.controller.main;

import javafx.animation.AnimationTimer;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.springframework.util.StringUtils;
import ru.xander.swissknife.controller.MainController;
import ru.xander.swissknife.util.Background;
import ru.xander.swissknife.util.Dialog;
import ru.xander.swissknife.util.FxUtil;
import ru.xander.swissknife.util.Util;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Alexander Shakhov
 */
public class MainTabJaxb {

    private static final Font FONT_CONSOLAS = Font.font("Consolas");

    private final MainController main;
    private final FileChooser wsdlChooser = new FileChooser();
    private final DirectoryChooser sourceChooser = new DirectoryChooser();

    private BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private AnimationTimer timer;

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
        main.textJaxbLog.setContextMenu(createLogContextMenu());

        wsdlChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("WSDL and XSD", "*.wsdl;*.xsd"),
                new FileChooser.ExtensionFilter("WSDL", "*.wsdl"),
                new FileChooser.ExtensionFilter("XSD", "*.xsd")
        );

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                List<String> newStrings = new ArrayList<>();
                logQueue.drainTo(newStrings);
                StringBuilder sb = new StringBuilder();
                newStrings.forEach(sb::append);
                main.textJaxbLog.appendText(sb.toString());
            }
        };
    }

    private ContextMenu createLogContextMenu() {
        ContextMenu menu = new ContextMenu();
        MenuItem clearItem = new MenuItem("Clear log");
        clearItem.setOnAction(event -> main.textJaxbLog.clear());
        menu.getItems().add(clearItem);
        return menu;
    }

    public static void initialize(MainController main) {
        new MainTabJaxb(main).initializeTab();
    }

    private void chooseWsdlPath() {
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

    private void chooseSourcePath() {
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

    private void generateJaxbCode() {
        if (StringUtils.isEmpty(main.textJaxbWsdlFile.getText())) {
            Dialog.warning("Warning", "WSDL file cannot be null.");
            return;
        }
        if (StringUtils.isEmpty(main.textJaxbSourcePath.getText())) {
            Dialog.warning("Warning", "Source path cannot be null.");
            return;
        }

        Background.runTask(
                () -> {
                    timer.start();
                    main.showBackgroundIndicator();
                    main.buttonJaxbGenerate.setDisable(true);
                },
                () -> {
                    String wsImportCommand = buildWsImportCommand();

                    logQueue.put("--------------------------------------------------------------------------\n");
                    logQueue.put("Starting at " + LocalDateTime.now() + "...\n\n");
                    logQueue.put(wsImportCommand + "\n");
                    Process process = Runtime.getRuntime().exec(wsImportCommand);
                    FxUtil.writeInputStream(logQueue, process.getInputStream());
                    FxUtil.writeInputStream(logQueue, process.getErrorStream());
                    logQueue.put("\n");

                    return null;
                },
                () -> {
                    timer.stop();
                    main.hideBackgroundIndicator();
                    main.buttonJaxbGenerate.setDisable(false);
                });
    }

    private String buildWsImportCommand() {
        StringBuilder command = new StringBuilder();
        command.append(Util.getJdkBin().getAbsolutePath()).append(File.separator).append("wsimport");
        command.append(" \"").append(main.textJaxbWsdlFile.getText()).append('"');
        command.append(" -d \"").append(main.textJaxbSourcePath.getText()).append('"');
        if (!StringUtils.isEmpty(main.textJaxbTargetPackage.getText())) {
            command.append(" -p ").append(main.textJaxbTargetPackage.getText());
        }
        command.append(" -encoding UTF-8");
        command.append(" -extension");
        command.append(" -verbose");
        command.append(" -extension");
        command.append(" -keep");
        command.append(" -XadditionalHeaders");
        command.append(" -B-XautoNameResolution");
        command.append(" -Xnocompile");
        return command.toString();
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