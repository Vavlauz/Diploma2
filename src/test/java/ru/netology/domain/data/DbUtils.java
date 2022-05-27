package ru.netology.domain.data;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtils {
    private static String getStatus(String query) throws SQLException {
        String result = "";
        var runner = new QueryRunner();
        try
                (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass")) {

            result = runner.query(conn, query, new ScalarHandler<String>());
            System.out.println(result);
            return result;
        }

    }

    public static String getPaymentStatus() throws SQLException {
        String statusSQL = "SELECT status FROM payment_entity";
        return getStatus(statusSQL);
    }

    public static String getCreditStatus() throws SQLException {
        String statusSQL = "SELECT status FROM credit_request_entity";
        return getStatus(statusSQL);
    }

    public static void clearTables() {
        var deletePaymentEntity = "DELETE FROM payment_entity";
        var deleteCreditEntity = "DELETE FROM credit_request_entity";
        var deleteOrderEntity = "DELETE FROM order_entity";
        var runner = new QueryRunner();
        try
                (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass")
        ) {
            runner.update(conn, deletePaymentEntity);
            runner.update(conn, deleteCreditEntity);
            runner.update(conn, deleteOrderEntity);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }
}
