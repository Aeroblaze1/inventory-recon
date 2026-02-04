package util;

import java.sql.Connection;

public class TransactionManager {

    public static void commit(Connection conn) {
        try {
            if (conn != null) conn.commit();
        } catch (Exception e) {
            throw new RuntimeException("Commit failed", e);
        }
    }

    public static void rollback(Connection conn) {
        try {
            if (conn != null) conn.rollback();
        } catch (Exception ignored) {}
    }

    public static void close(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch (Exception ignored) {}
    }
}
