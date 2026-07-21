package com.project.code.Model;

@Entity
public class Store {

// 1. Add 'id' field:
//    - Type: private long 
//    - This field will be auto-incremented.
//    - Use @Id to mark it as the primary key.
//    - Use @GeneratedValue(strategy = GenerationType.IDENTITY) to auto-increment it.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
// 2. Add 'name' field:
//    - Type: private String
//    - This field cannot be empty, use the @NotNull annotation to enforce this rule.

    @NotNull(message = "Name cannot be null")
    private String name;

// 3. Add 'address' field:
//    - Type: private String
//    - This field cannot be empty, use the @NotNull and @NotBlank annotations to enforce this rule.

    @NotNull(message = "Address cannot be null")
    @NotBlank(message = "Address cannot be empty")
    private String address;

// 4. Add relationships:
//    - **Inventory**: A store can have multiple inventory entries.
//    - Use @OneToMany(mappedBy = "store") to reflect the one-to-many relationship with Inventory.
//    - Use @JsonManagedReference("inventory-store") to manage bidirectional relationships and avoid circular references.

    @OneToMany(mappedBy = "store")
    @JsonManagedReference("inventory-store")
    private List<Inventory> inventories;

// 5. Add constructor:
//    - Create a constructor that accepts name and address as parameters to initialize the Store object.
    Store(String name, String address) {
        this.name = name;
        this.address = address;
    }

// 6. Add @Entity annotation:
//    - Use @Entity above the class name to mark it as a JPA entity.

// 7. Add Getters and Setters:
//    - Add getter and setter methods for all fields (id, name, address).

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

