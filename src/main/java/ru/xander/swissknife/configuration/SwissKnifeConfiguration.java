package ru.xander.swissknife.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * @author Alexander Shakhov
 */
@Configuration
@Getter
public class SwissKnifeConfiguration {
    @Value("${swiss.java.home}")
    private String javaHome;

    public String getJavaBinPath() {
        return javaHome + File.separator + "bin";
    }
}
