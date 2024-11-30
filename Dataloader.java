package lab7;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.swing.table.DefaultTableModel;
import org.w3c.dom.*;

public class DataLoader implements Runnable {
    private CountDownLatch latch;

    public DataLoader(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            System.out.println("Загрузка данных из XML...");
            File xmlFile = new File("books.xml");
            if (!xmlFile.exists()) {
                System.out.println("Файл XML не найден. Создайте файл books.xml.");
                latch.countDown();
                return;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("Book");

            DefaultTableModel model = new DefaultTableModel(new String[]{"Название", "Автор", "Шрифт", "Закреплена?"}, 0);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String title = element.getElementsByTagName("Title").item(0).getTextContent();
                    String author = element.getElementsByTagName("Author").item(0).getTextContent();
                    String font = element.getElementsByTagName("Font").item(0).getTextContent();
                    String pinned = element.getElementsByTagName("Pinned").item(0).getTextContent();

                    model.addRow(new Object[]{title, author, font, pinned});
                }
            }

            System.out.println("Данные загружены: " + model.getRowCount() + " книг.");
            latch.countDown(); // Сообщаем, что поток завершен
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}