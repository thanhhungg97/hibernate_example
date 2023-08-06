package org.example.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class BenchGetSession {
    public static void main(String[] args) {
        benchmark();
    }

    public static void benchmark() {
        SessionFactory sFactory = HibernateUtil.getSessionFactory();

        int i = 0;
        long start = System.nanoTime();
        while (i < 10000) {
            Transaction transaction = null;
            try (Session session = sFactory.openSession()) {
                // start a transaction
                transaction = session.beginTransaction();
                // save the student object
                // commit transaction
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
            i++;
        }

        long end = System.nanoTime();
        System.out.println((end - start) / 10000);
    }
}
