package com.filefixer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {
  @Test
  void ShouldReturnCorrectNamingConvention() {
    File c0 = new File("src/test/Test Files/Naming Convention Test/7777-7777_Alex_Russo_Projekt_7777_7777.pdf");
    File c1 = new File("src/test/Test Files/Naming Convention Test/777-777_Alex_Russo_777_Projekt.pdf");
    File c2 = new File("src/test/Test Files/Naming Convention Test/Alex Russo_1313_assignsubmission_file_Projekt.pdf");
    File c3 = new File("src/test/Test Files/Naming Convention Test/81333104.pdf");

    Assertions.assertAll(
        () -> assertEquals(0, Utils.naming_convention(c0)),
        () -> assertEquals(1, Utils.naming_convention(c1)),
        () -> assertEquals(2, Utils.naming_convention(c2)),
        () -> assertEquals(3, Utils.naming_convention(c3)));
    }
}