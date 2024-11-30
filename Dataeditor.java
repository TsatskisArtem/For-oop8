package lab8;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

public class DataEditor implements Runnable {
    private CountDownLatch latch1;
    private CountDownLatch latch2;

    public DataEditor(CountDownLatch latch1, CountDownLatch latch2) {
        this.latch1 = latch1;
        this.latch2 = latch2;
    }

    @Override
    public void run() {
        try {
            latch1.await(); // Ждем завершения загрузки данных
            System.out.println("Редактирование данных...");

            File xmlFile = new File("books.xml");
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Пример: Добавляем новую книгу
            Element root = doc.getDocumentElement();
            Element newBook = doc.createElement("Book");

            Element title = doc.createElement("Title");
            title.appendChild(doc.createTextNode("Новая книга"));
            newBook.appendChild(title);

            Element author = doc.createElement("Author");
            author.appendChild(doc.createTextNode("Новый автор"));
            newBook.appendChild(author);

            Element font = doc.createElement("Font");
            font.appendChild(doc.createTextNode("Courier"));
            newBook.appendChild(font);

            Element pinned = doc.createElement("Pinned");
            pinned.appendChild(doc.createTextNode("Нет"));
            newBook.appendChild(pinned);

            root.appendChild(newBook);

            // Сохраняем изменения в XML-файл
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("books.xml"));
            transformer.transform(source, result);

            System.out.println("XML-файл обновлен.");
            latch2.countDown(); // Сообщаем, что поток завершен
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}