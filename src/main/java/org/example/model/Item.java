package org.example.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Item extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceName;
    private String deviceCondition;
    @ManyToOne
    private User borrowedBy;

    private Integer borrowDuration;

    @Temporal(TemporalType.DATE)
    private Date borrowedUntil;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Department department;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceCondition() {
        return deviceCondition;
    }

    public void setDeviceCondition(String deviceCondition) {
        this.deviceCondition = deviceCondition;
    }

    public User getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(User borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    public Integer getBorrowDuration() {
        return borrowDuration;
    }

    public void setBorrowDuration(Integer borrowDuration) {
        this.borrowDuration = borrowDuration;
    }

    public Date getBorrowedUntil() {
        return borrowedUntil;
    }

    public void setBorrowedUntil(Date borrowedUntil) {
        this.borrowedUntil = borrowedUntil;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}