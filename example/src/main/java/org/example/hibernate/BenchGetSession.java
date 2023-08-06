package org.example.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.LinkedList;
import java.util.List;

public class BenchGetSession {
    public static void main(String[] args) {
        long result = 0;
        for (int i = 0; i < 1000; i++) {
            result += benchmark2(50);
        }
        System.out.println(result / 1000);

    }

    public static void benchmark() {
        SessionFactory sFactory = HibernateUtil.getSessionFactory();

        int i = 0;
        long start = System.nanoTime();
        while (i < 10000) {
            getSession(sFactory);
            i++;
        }

        long end = System.nanoTime();
        System.out.println((end - start) / 10000);
    }

    public static long benchmark2(int iterator) {
        SessionFactory sFactory = HibernateUtil.getSessionFactory();
        int i = 0;
        long start = System.nanoTime();
        List<Thread> list = new LinkedList<>();
        while (i < iterator) {
            list.add(new Thread(() -> {
                try {
                    run1(sFactory);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }));
            i++;
        }
        list.forEach(Thread::start);
        list.forEach(item -> {
            try {
                item.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        long end = System.nanoTime();
        long l = (end - start) / iterator;
        System.out.println(l);
        return l;
    }

    private static void run1(SessionFactory sFactory) throws InterruptedException {
        Thread thread = getThread(sFactory);
        Thread thread1 = getThread(sFactory);
        Thread thread2 = getThread(sFactory);
        thread2.start();
        thread1.start();
        thread.start();


        thread2.join();
        thread.join();
        thread1.join();
    }

    private static Thread getThread(SessionFactory sFactory) {
        return new Thread(() -> getSession(sFactory));
    }

    private static void getSession(SessionFactory sFactory) {
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
    }
}
