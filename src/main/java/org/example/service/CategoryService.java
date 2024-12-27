package org.example.service;



import org.example.model.Category;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;


@ApplicationScoped
public class CategoryService {

    public List<Category> listCategories() {
        return Category.listAll();
    }

    @Transactional
    public void addCategory(Category category) {
        category.persist();
    }

    public Category findById(Long id) {
        return Category.findById(id);
    }

    @Transactional
    public void updateCategory(Category category) {
        category.persist();
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category.deleteById(id);
    }
}
