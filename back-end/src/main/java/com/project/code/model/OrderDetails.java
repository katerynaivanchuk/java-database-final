package com.project.code.Model;

import java.time.LocalDateTime;

@Entity
public class OrderDetails {

// 1. Add 'id' field:
//    - Type: private Long 
//    - This field will be auto-incremented.
//    - Use @Id to mark it as the primary key.
//    - Use @GeneratedValue(strategy = GenerationType.IDENTITY) to auto-increment it.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

// 2. Add 'customer' field:
//    - Type: private Customer
//    - This field refers to the customer who placed the order.
//    - Use @ManyToOne with @JoinColumn(name = "customer_id") to define the foreign key relationship.
//    - Apply @JsonManagedReference to handle bidirectional relationships and JSON serialization.

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonManagedReference
    private Customer customer;

// 3. Add 'store' field:
//    - Type: private Store
//    - This field refers to the store from where the order was placed.
//    - Use @ManyToOne with @JoinColumn(name = "store_id") to define the foreign key relationship.
//    - Apply @JsonManagedReference to handle bidirectional relationships and JSON serialization.

    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonManagedReference
    private Store store;

// 4. Add 'totalPrice' field:
//    - Type: private Double
//    - This field represents the total price of the order.

    private Double totalPrice;

// 5. Add 'date' field:
//    - Type: private LocalDateTime
//    - This field represents the date and time when the order was placed.

    private LocalDateTime date;

// 6. Add 'orderItems' field:
//    - Type: private List<OrderItem>
//    - This field represents the list of items in the order.
//    - Use @OneToMany(mappedBy = "order", fetch = FetchType.EAGER) to establish the one-to-many relationship with OrderItem.
//    - Apply @JsonManagedReference to prevent circular references during JSON serialization.

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<OrderItem> orderItems;

// 7. Add constructors:
//    - A no-argument constructor.
//    - A parameterized constructor that accepts Customer, Store, totalPrice, and date as parameters.
    OrderDetails() {}
    OrderDetails(Customer customer, Store store, Double totalPrice, LocalDateTime date) {
        this.customer = customer;
        this.store = store;
        this.totalPrice = totalPrice;
        this.date = date;
    }

// 8. Add @Entity annotation:
//    - Use @Entity above the class name to mark it as a JPA entity.

// 9. Add Getters and Setters:
//    - Add getter and setter methods for all fields (id, customer, store, totalPrice, date, orderItems).

    public Long getId() {
        return this.id;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public Store getStore() {
        return this.store;
    }


    
}
