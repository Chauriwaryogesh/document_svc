package com.SecureAccessPortal.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee_photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String contentType;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;

    // Constructors
    public Photo() {}

    public Photo(String name, String contentType, byte[] data) {
        this.name = name;
        this.contentType = contentType;
        this.data = data;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }
}

