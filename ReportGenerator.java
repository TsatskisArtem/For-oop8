package lab8;

import java.io.*;
import java.util.concurrent.CountDownLatch;

public class ReportGenerator implements Runnable {
    private CountDownLatch latch;

    public ReportGenerator(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            latch.await(); // Ждем завершения формирования XML
            System.out.println("Генерация HTML-отчета...");

            File htmlFile = new File("report.html");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(htmlFile))) {
                writer.write("<!DOCTYPE html>");
                writer.write("<html><head><title>Отчет о книгах</title></head><body>");
                writer.write("<h1>Отчет о книгах</h1>");
                writer.write("<table border='1'><tr><th>Название</th><th>Автор</th><th>Шрифт</th><th>Закреплена?</th></tr>");
                writer.write("<tr><td>Война и мир</td><td>Лев Толстой</td><td>Arial</td><td>Нет</td></tr>");
                writer.write("<tr><td>1984</td><td>Джордж Оруэлл</td><td>Calibri</td><td>Да</td></tr>");
                writer.write("</table>");
                writer.write("</body></html>");
            }

            System.out.println("HTML-отчет создан: report.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}