package com.me.canteen;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DesignDocumentsTest {

    private static final Path API_DOC = Path.of("docs", "api", "backend-modules.md");
    private static final Path DB_SCHEMA = Path.of("docs", "db", "schema.sql");

    @Test
    void shouldContainCoreUserAndAdminApiSections() throws IOException {
        String content = Files.readString(API_DOC);

        assertAll(
                () -> assertTrue(content.contains("/api/v1/user/canteens")),
                () -> assertTrue(content.contains("/api/v1/user/orders")),
                () -> assertTrue(content.contains("/api/v1/user/reservations")),
                () -> assertTrue(content.contains("/api/v1/user/pickup-qrcodes")),
                () -> assertTrue(content.contains("/api/v1/admin/canteens/realtime-occupancy")),
                () -> assertTrue(content.contains("/api/v1/admin/statistics/dining")),
                () -> assertTrue(content.contains("/api/v1/admin/statistics/food-sales")),
                () -> assertTrue(content.contains("/api/v1/admin/pickup-qrcodes/scan"))
        );
    }

    @Test
    void shouldContainCoreMysqlTables() throws IOException {
        String content = Files.readString(DB_SCHEMA);

        assertAll(
                () -> assertTrue(content.contains("CREATE TABLE users")),
                () -> assertTrue(content.contains("CREATE TABLE canteens")),
                () -> assertTrue(content.contains("CREATE TABLE food_items")),
                () -> assertTrue(content.contains("CREATE TABLE orders")),
                () -> assertTrue(content.contains("CREATE TABLE reservations")),
                () -> assertTrue(content.contains("CREATE TABLE pickup_qr_codes"))
        );
    }
}
