package dev.shoxruhjon.models;

import dev.shoxruhjon.utils.IdGenerator;

import java.time.LocalDateTime;

public abstract class BaseEntity {
    private final String id;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BaseEntity() {
        this.id = IdGenerator.generateId();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void touch() { // updatedAt ni yangilash uchun
        this.updatedAt = LocalDateTime.now();
    }
}
