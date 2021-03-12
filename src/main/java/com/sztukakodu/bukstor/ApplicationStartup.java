package com.sztukakodu.bukstor;

import com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase;
import com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase.CreateBookCommand;
import com.sztukakodu.bukstor.catalog.db.AuthorJpaRepository;
import com.sztukakodu.bukstor.catalog.domain.Author;
import com.sztukakodu.bukstor.catalog.domain.Book;
import com.sztukakodu.bukstor.order.application.port.ManipulateOrderUseCase;
import com.sztukakodu.bukstor.order.application.port.QueryOrderUseCase;
import com.sztukakodu.bukstor.order.domain.OrderItem;
import com.sztukakodu.bukstor.order.domain.Recipient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final ManipulateOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;
    private final String title;
    private final AuthorJpaRepository authorRepository;

    public ApplicationStartup(
            CatalogUseCase catalog,
            ManipulateOrderUseCase placeOrder,
            QueryOrderUseCase queryOrder,
            @Value("${title}") String title,
            AuthorJpaRepository authorRepository) {
        this.catalog = catalog;
        this.placeOrder = placeOrder;
        this.queryOrder = queryOrder;
        this.title = title;
        this.authorRepository = authorRepository;
    }

    @Override
    public void run(String... args) {
        initData();
        placeFirstOrder();
//        placeSecondOrder();

        System.out.println(catalog.findByAuthor("jrr"));
    }

    private void initData() {
        final Author author_1 = new Author("Adam", "Mickiewicz");
        final Author author_2 = new Author("JRR", "Tolkien");

        authorRepository.save(author_1);
        authorRepository.save(author_2);


        final CreateBookCommand book_with_1_author = new CreateBookCommand(
                "Book that has 1 author",
                Set.of(author_1.getId()),
                1999,
                new BigDecimal("79.00")
        );
        final CreateBookCommand book_with_2_authors = new CreateBookCommand(
                "Another Book that has two authors",
                Set.of(author_1.getId(), author_2.getId()),
                2011,
                new BigDecimal("122.00")
        );
        catalog.addBook(book_with_1_author);
        catalog.addBook(book_with_2_authors);


    }

    private void placeFirstOrder() {
        Book book_1 = catalog.findOneByTitle("Book")
                .orElseThrow(() -> new IllegalStateException("Can't find this book"));
        Book book_2 = catalog.findOneByTitle("Another ")
                .orElseThrow(() -> new IllegalStateException("Can't find this book"));

        Recipient recipient = Recipient.builder()
                .name("Jan Kowalski")
                .street("Lazurowa 15")
                .city("Radom")
                .zipCode("04-521")
                .phone("555-122-74")
                .email("jan@kowalski.pl")
                .build();

        ManipulateOrderUseCase.PlaceOrderCommand order = ManipulateOrderUseCase.PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new OrderItem(book_1.getId(), 1))
                .item(new OrderItem(book_2.getId(), 3))
                .build();

        ManipulateOrderUseCase.PlaceOrderResponse response = placeOrder.placeOrder(order);
        String result = response.handle(
                orderId -> "Created order with id: " + orderId,
                error -> "Failed to create order: " + error
        );
        System.out.println(result);

        queryOrder.findAll()
                .forEach(o -> {
                    System.out.println("GOT ORDER WITH TOTAL PRICE: " + o.totalPrice() + " DETAILS: " + o);
                });
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

