package com.drugdesign;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vaadin.flow.server.PWA;

@SpringBootApplication
@PWA(name = "DrugCompass", shortName = "DrugCompass", theme = Lumo.DARK)
public class DrugCompassApplication implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(DrugCompassApplication.class, args);
    }
}
