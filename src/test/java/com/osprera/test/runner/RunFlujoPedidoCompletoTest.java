package com.osprera.test.runner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;


@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {
        "com.osprera.test.stepdefinitions",
        "com.osprera.test.hooks"
    },
   plugin = {
    "pretty",
    "html:target/cucumber-report.html",
    "json:target/cucumber-report.json",
},
    monochrome = true,
    tags = "@flujoPedidoCompleto"
)
public class RunFlujoPedidoCompletoTest {
}
