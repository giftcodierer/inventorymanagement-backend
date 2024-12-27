package org.example.service;

import org.example.model.Item;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ItemService {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public void addItem(Item item) {
        try {
            if (item.getId() != null) {
                entityManager.merge(item);
            } else {
                entityManager.persist(item);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error adding item", e);
        }
    }

    public List<Item> listItems() {
        try {
            return entityManager.createQuery("SELECT i FROM Item i", Item.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error listing items", e);
        }
    }

    public Item findById(Long id) {
        try {
            return entityManager.find(Item.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error finding item by id", e);
        }
    }

    @Transactional
    public void deleteItem(Long id) {
        try {
            Item item = entityManager.find(Item.class, id);
            if (item != null) {
                entityManager.remove(item);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting item", e);
        }
    }

    @Transactional
    public void updateItem(Item item) {
        try {
            entityManager.merge(item);
        } catch (Exception e) {
            throw new RuntimeException("Error updating item", e);
        }
    }
}