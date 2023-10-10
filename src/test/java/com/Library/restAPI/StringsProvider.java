package com.Library.restAPI;

import java.util.stream.Stream;

public class StringsProvider {

    private static Stream<String> okNames(){
        return Stream.of("Test");
    }
    private static Stream<String> wrongNames(){
        return Stream.of("", "test", "test123", "Test123", "T12ts", "TeSt", "Test test test",
                "Test Test Test", "123", "12as", "12Test", "Te_t", "Te.t");
    }

    private static Stream<String> okSurnames(){
        return Stream.of("Test", "Test-Test");
    }

    private static Stream<String> wrongSurnames(){
        return Stream.of("", "test", "test123", "Test123", "T12ts", "TeSt", "Test test test",
                "Test Test Test", "123", "12as", "12Test", "Te_t", "Te.t",
                "Test-test", "test-Test", "Test--Test", "Test-12", "Test123-Test", "test-test", "test.Test");
    }

}
