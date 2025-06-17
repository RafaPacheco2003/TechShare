package com.techshare.service.Category.impl;

import com.techshare.DTO.CategoryDTO;
import com.techshare.convert.Category.ConvertCategory;
import com.techshare.entities.Category;
import com.techshare.exception.CategoryNotFoundException;
import com.techshare.http.request.CategoryRequest;
import com.techshare.repositories.CategoryRepository;
import com.techshare.service.ImageStorage.ImageStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ImageStorage imageStorage;

    @Mock
    private ConvertCategory convertCategory;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDTO categoryDTO;
    private CategoryRequest categoryRequest;
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        category = new Category();
        category.setCategory_id(1L);
        category.setName("Test Category");
        category.setImage("test-image.jpg");

        categoryDTO = new CategoryDTO();
        categoryDTO.setCategory_id(1L);
        categoryDTO.setName("Test Category");
        categoryDTO.setImage("http://localhost:8080/images/test-image.jpg");

        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Test Category");

        multipartFile = new MockMultipartFile(
            "image",
            "test-image.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );
    }

    @Test
    void createCategory_Success() {
        // Arrange
        when(imageStorage.saveImage(any())).thenReturn("test-image.jpg");
        when(convertCategory.convertCategoryRequestToCategory(any())).thenReturn(category);
        when(categoryRepository.save(any())).thenReturn(category);
        when(convertCategory.convertCategoryToCategoryDTO(any())).thenReturn(categoryDTO);

        // Act
        CategoryDTO result = categoryService.create(categoryRequest, multipartFile);

        // Assert
        assertNotNull(result);
        assertEquals("Test Category", result.getName());
        verify(categoryRepository).save(any());
    }

    @Test
    void getCategoryById_Success() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(convertCategory.convertCategoryToCategoryDTO(any())).thenReturn(categoryDTO);

        // Act
        Optional<CategoryDTO> result = categoryService.getCategoryById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Category", result.get().getName());
    }

    @Test
    void getCategoryById_NotFound() {
        // Arrange
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.getCategoryById(99L);
        });
    }

    @Test
    void getAllCategories_Success() {
        // Arrange
        Category category2 = new Category();
        category2.setCategory_id(2L);
        category2.setName("Test Category 2");
        category2.setImage("test-image-2.jpg");

        CategoryDTO categoryDTO2 = new CategoryDTO();
        categoryDTO2.setCategory_id(2L);
        categoryDTO2.setName("Test Category 2");
        categoryDTO2.setImage("http://localhost:8080/images/test-image-2.jpg");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category, category2));
        when(convertCategory.convertCategoryToCategoryDTO(category)).thenReturn(categoryDTO);
        when(convertCategory.convertCategoryToCategoryDTO(category2)).thenReturn(categoryDTO2);

        // Act
        List<CategoryDTO> results = categoryService.getAllCategories();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void deleteCategory_Success() {
        // Arrange
        when(categoryRepository.existsById(1L)).thenReturn(true);

        // Act
        categoryService.deleteCategory(1L);

        // Assert
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void deleteCategory_NotFound() {
        // Arrange
        when(categoryRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.deleteCategory(99L);
        });
    }
}
