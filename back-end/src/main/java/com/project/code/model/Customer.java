package com.project.code.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
public class Customer {

// 1. Add 'id' field: 
//    - Type: private long 
//    - It should be auto-incremented.
//    - Use @Id to mark it as the primary key and @GeneratedValue(strategy = GenerationType.IDENTITY) to auto-increment it.
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

// 2. Add 'name' field:
//    - Type: private String
//    - This field cannot be empty, use the @NotNull annotation to enforce this rule.

    // Example: @NotNull(message = "Name cannot be null")
    @NotNull(message = "Name cannot be null")
    private String name;

// 3. Add 'email' field:
//    - Type: private String
//    - This field cannot be empty, use the @NotNull annotation to enforce this rule.

    // Example: @NotNull(message = "Email cannot be null")
    @NotNull(message = "Email cannot be null")
    private String email;

// 4. Add 'phone' field:
//    - Type: private String
//    - This field cannot be empty, use the @NotNull annotation to enforce this rule.

    // Example: @NotNull(message = "Phone cannot be null")
    @NotNull(message = "Phone cannot be null")
    private String phone;

// 5. Add one-to-many relationship with orders:
//    - A customer can have multiple orders.
//    - Use the @OneToMany annotation to establish this relationship.
//    - Specify "mappedBy = 'customer'" to indicate that the 'customer' field in the 'Order' entity owns the relationship.
//    - Use @JsonManagedReference to ensure proper JSON serialization of related orders.

    // Example: @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    // Example: @JsonManagedReference

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<OrderDetails> orders;

// 6. Getters and Setters:
//    - For each field ('id', 'name', 'email', 'phone'), add getter and setter methods.
//    - Example: public Long getId(), public void setId(Long id)
//    - Example: public String getName(), public void setName(String name)
//    - Add getters and setters for 'email' and 'phone' fields as well.

    public Customer(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Customer() {

    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
        
// 7. Ensure to use proper annotations and validate constraints:
//    - Use @NotNull for fields that cannot be empty like 'name', 'email', and 'phone'.
//    - Make sure you add the correct annotations for entity mapping and relationship management like @Entity, @Id, @GeneratedValue, @OneToMany, and @JsonManagedReference.

}

