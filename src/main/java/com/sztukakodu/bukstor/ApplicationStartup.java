package com.sztukakodu.bukstor;

import com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase;
import com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase.CreateBookCommand;
import com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase.UpdateBookCommand;
import com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase.UpdateBookResponse;
import com.sztukakodu.bukstor.catalog.domain.Book;
import com.sztukakodu.bukstor.order.application.port.OrderUseCase;
import com.sztukakodu.bukstor.order.application.port.QueryOrderUseCase;
import com.sztukakodu.bukstor.order.domain.Recipient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final OrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;
    private final String title;

    public ApplicationStartup(
            CatalogUseCase catalog,
            OrderUseCase placeOrder,
            QueryOrderUseCase queryOrder,
            @Value("${title}") String title) {
        this.catalog = catalog;
        this.placeOrder = placeOrder;
        this.queryOrder = queryOrder;
        this.title = title;
    }

    @Override
    public void run(String... args) {
        initData();
        findByTitle();
//        findByAuthor();
        findAndUpdate();
        findByTitle();

//        placeFirstOrder();
//        placeSecondOrder();
    }

    private void initData() {
        catalog.addBook(new CreateBookCommand("Henryk Potter cz.1", "J.K.Rawling", 2001, new BigDecimal(100)));
        catalog.addBook(new CreateBookCommand("Harry i komnata tajemnic", "J.K.Rawling", 2002, new BigDecimal(20)));
        catalog.addBook(new CreateBookCommand("On i Harry Potter cz.3", "Piekara", 2003, new BigDecimal(75)));
        catalog.addBook(new CreateBookCommand("Wiedzmin", "Andrzej Sapkowski", 2004, new BigDecimal(83)));
        catalog.addBook(new CreateBookCommand("Pan Tadeusz", "Mickiewicz", 1887, new BigDecimal(105)));
        catalog.addBook(new CreateBookCommand("Pan Wolodyjowski", "Henryk Sienkiewicz", 1863, new BigDecimal(99)));
        catalog.addBook(new CreateBookCommand("Dziady", "Mickiewicz", 1901, new BigDecimal(35)));


    }

    private void findByAuthor() {
        System.out.println("----by author----");
        List<Book> books = catalog.findByAuthor("Sapk");
        books.forEach(System.out::println);
    }

    private void findByTitle() {
        System.out.println("----by title----");
        List<Book> books = catalog.findByTitle(title);
        books.forEach(System.out::println);
    }

    private void findAndUpdate() {
        catalog.findByTitleAndAuthor("Wiedzmin", "Sapkowski")
                .stream()
                .findFirst()
                .ifPresent(book -> {
                    UpdateBookCommand update = UpdateBookCommand
                            .builder()
                            .id(book.getId())
                            .title("Wiedzmin opowiadania")
                            .build();
                    final UpdateBookResponse response = catalog.updateBook(update);
                    if (response == UpdateBookResponse.SUCCESS) {
                        System.out.println("Sukces");
                    } else {
                        System.out.println(response.getErrors().toString());
                    }
                });
    }

    private void placeFirstOrder() {
        Book bookWiedzmin = catalog.findOneByTitle("Wiedzmin")
                .orElseThrow(() -> new IllegalStateException("Can't find this book"));
        Book bookPanTadeusz = catalog.findOneByTitle("Pan Tadeusz")
                .orElseThrow(() -> new IllegalStateException("Can't find this book"));

        Recipient recipient = Recipient.builder()
                .name("Jan Kowalski")
                .street("Lazurowa 15")
                .city("Radom")
                .zipCode("04-521")
                .phone("555-122-74")
                .email("jan@kowalski.pl")
                .build();

//        PlaceOrderCommand order = PlaceOrderCommand.builder()
//                .recipient(recipient)
//                .item(new OrderItem(bookWiedzmin, 1))
//                .item(new OrderItem(bookPanTadeusz, 3))
//                .build();

//        PlaceOrderResponse response = placeOrder.placeOrder(order);
//        System.out.println("Created order with id: " + response.getOrderId());

//        queryOrder.findAll()
//                .forEach(o -> {
//                    System.out.println("GOT ORDER WITH TOTAL PRICE: " + o.totalPrice() + " DETAILS: " + o);
//                });
    }

    private void placeSecondOrder() {
        Book book_1 = catalog.findOneByTitle("Pan Wolodyjowski")
                .orElseThrow(() -> new IllegalStateException("Can't find this book"));

        Recipient recipient = Recipient.builder()
                .name("John Doe")
                .street("Klawiszowa 3")
                .city("Warszawa")
                .zipCode("02-223")
                .phone("512-221-674")
                .email("john.doe@adres.pl")
                .build();

//        PlaceOrderCommand order = PlaceOrderCommand.builder()
//                .recipient(recipient)
//                .item(new OrderItem(book_1, 4))
//                .build();
//
//        PlaceOrderResponse response = placeOrder.placeOrder(order);
//        System.out.println("Created order with id: " + response.getOrderId());
//
//        queryOrder.findAll()
//                .forEach(o -> {
//                    System.out.println("GOT ORDER WITH TOTAL PRICE: " + o.totalPrice() + " DETAILS: " + o);
//                });
    }
}

