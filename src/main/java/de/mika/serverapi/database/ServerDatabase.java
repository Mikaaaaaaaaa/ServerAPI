package de.mika.serverapi.database;

import de.goldendeveloper.mysql.MYSQL;
import de.goldendeveloper.mysql.entities.*;
import de.mika.serverapi.user.ban.Ban;
import de.mika.serverapi.user.rank.Rank;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ServerDatabase {

    private static ServerDatabase instance;

    private final MYSQL mysql;
    private final Database database;
    private final Map<Class<?>, Table> tables = new HashMap<>();

    private ServerDatabase() {
        mysql = new MYSQL("localhost", "root", "", 3306);
        database = mysql.getOrCreateDatabase("minecraft");
        secureTable("ban", Ban.class);
        secureTable("rank", Rank.class);
    }

    private void secureTable(String name, Class<?> clazz) {
        if (!(database.existsTable(name))) {
            database.createTable(name, Arrays.asList("uuid", "value"));
        }
        tables.put(clazz, database.getTable(name));
    }

    public <T> T loadPlayer(Class<T> clazz, String uuid) {
        Table table = tables.get(clazz);
        if (table == null) {
            throw new IllegalArgumentException("No table for class: " + clazz);
        }
        return loadPlayer(table, uuid, clazz);
    }

    private <T> T loadPlayer(Table table, String uuid, Class<T> clazz) {
        boolean playerExists = table.existsRow(table.getColumn("uuid"), uuid);

        SearchResult result = table.getRow(table.getColumn("id"), 1 ).getData().get("uuid");
        System.out.println(result == null);

        T value = null;
        if (playerExists) {
            value = getPlayer(table, uuid, clazz);
        } else {
            value = createPlayer(clazz);
            setPlayer(table, uuid, value.toString());
        }

        return value;
    }

    private <T> T getPlayer(Table table, String uuid, Class<T> clazz) {
        Row row = table.getRow(table.getColumn("uuid"), uuid);
        HashMap<String, SearchResult> rowData = row.getData();
        SearchResult result = rowData.get("value");

        return convertValueToRequiredType(clazz, result.getAsString());
    }

    private <T> T createPlayer(Class<T> clazz) {

        System.out.println("CREATE PLAYER!!!!!!!!!!");
        T value = null;
        try {
            if (clazz.isEnum()) {
                value = (T) Enum.valueOf((Class<Enum>) clazz, Rank.USER.name());
            } else {
                value = clazz.getDeclaredConstructor().newInstance();
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            handleException(e);
        }
        return value;
    }

    private <T> T convertValueToRequiredType(Class<T> clazz, String value) {
        T convertedValue = null;
        try {
            if (clazz.isEnum()) {
                convertedValue = (T) Enum.valueOf((Class<Enum>) clazz, value);
            } else {
                convertedValue = clazz.getConstructor(String.class).newInstance(value);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            handleException(e);
        }
        return convertedValue;
    }

    private void handleException(Exception e) {
        System.err.println("An error occurred: " + e.getMessage());
        e.printStackTrace();
    }


    private void setPlayer(Table table, String uuid, String value) {
        table.insert(new RowBuilder()
                .with(table.getColumn("uuid"), uuid)
                .with(table.getColumn("value"), value)
                .build());
    }

    public void close() {
        mysql.close();
    }

    public void drop() {
        tables.values().forEach(Table::drop);
    }

    public static ServerDatabase getInstance() {
        if (instance == null) {
            instance = new ServerDatabase();
        }
        return instance;
    }

}
