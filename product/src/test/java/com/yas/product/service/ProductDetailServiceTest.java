package com.yas.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.yas.commonlibrary.exception.NotFoundException;
import com.yas.product.model.Brand;
import com.yas.product.model.Category;
import com.yas.product.model.Product;
import com.yas.product.model.ProductCategory;
import com.yas.product.model.ProductImage;
import com.yas.product.model.ProductOption;
import com.yas.product.model.ProductOptionCombination;
import com.yas.product.model.attribute.ProductAttribute;
import com.yas.product.model.attribute.ProductAttributeValue;
import com.yas.product.repository.ProductOptionCombinationRepository;
import com.yas.product.repository.ProductRepository;
import com.yas.product.viewmodel.ImageVm;
import com.yas.product.viewmodel.NoFileMediaVm;
import com.yas.product.viewmodel.product.ProductDetailInfoVm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductDetailServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private MediaService mediaService;
    @Mock
    private ProductOptionCombinationRepository productOptionCombinationRepository;

    @InjectMocks
    private ProductDetailService productDetailService;

    private Product product;
    private Brand brand;
    private Category category;

    @BeforeEach
    void setUp() {
        brand = new Brand();
        brand.setId(1L);
        brand.setName("TestBrand");

        category = new Category();
        category.setId(1L);
        category.setName("TestCategory");

        product = Product.builder()
            .id(1L)
            .name("Product")
            .slug("product-slug")
            .shortDescription("short")
            .description("desc")
            .specification("spec")
            .sku("SKU")
            .gtin("GTIN")
            .price(100.0)
            .isAllowedToOrder(true)
            .isPublished(true)
            .isFeatured(false)
            .isVisibleIndividually(true)
            .stockTrackingEnabled(false)
            .thumbnailMediaId(1L)
            .brand(brand)
            .metaTitle("meta")
            .metaKeyword("keyword")
            .metaDescription("metadesc")
            .taxClassId(1L)
            .hasOptions(false)
            .productCategories(List.of())
            .productImages(List.of())
            .attributeValues(new ArrayList<>())
            .products(new ArrayList<>())
            .build();
    }

    @Test
    void getProductDetailById_simpleProduct_success() {
        ProductCategory pc = ProductCategory.builder().product(product).category(category).build();
        product.setProductCategories(List.of(pc));

        NoFileMediaVm mediaVm = new NoFileMediaVm(1L, "cap", "file", "type", "http://url");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(mediaService.getMedia(1L)).thenReturn(mediaVm);

        ProductDetailInfoVm result = productDetailService.getProductDetailById(1L);
        assertNotNull(result);
        assertEquals("Product", result.getName());
        assertEquals(1L, result.getBrandId());
        assertEquals("TestBrand", result.getBrandName());
    }

    @Test
    void getProductDetailById_notPublished_throwsNotFound() {
        product.setPublished(false);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(NotFoundException.class, () -> productDetailService.getProductDetailById(1L));
    }

    @Test
    void getProductDetailById_notFound_throwsNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productDetailService.getProductDetailById(999L));
    }

    @Test
    void getProductDetailById_noBrand_returnsNull() {
        product.setBrand(null);
        product.setProductCategories(null);

        NoFileMediaVm mediaVm = new NoFileMediaVm(1L, "cap", "file", "type", "http://url");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(mediaService.getMedia(1L)).thenReturn(mediaVm);

        ProductDetailInfoVm result = productDetailService.getProductDetailById(1L);
        assertNull(result.getBrandId());
        assertNull(result.getBrandName());
    }

    @Test
    void getProductDetailById_noThumbnail() {
        product.setThumbnailMediaId(null);
        product.setProductImages(null);
        product.setProductCategories(List.of());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDetailInfoVm result = productDetailService.getProductDetailById(1L);
        assertNull(result.getThumbnail());
    }

    @Test
    void getProductDetailById_withImages() {
        ProductImage img = ProductImage.builder().id(1L).imageId(10L).product(product).build();
        product.setProductImages(List.of(img));
        product.setProductCategories(List.of());

        NoFileMediaVm mediaVm = new NoFileMediaVm(1L, "cap", "file", "type", "http://url");
        NoFileMediaVm imgMediaVm = new NoFileMediaVm(10L, "cap", "file", "type", "http://img-url");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(mediaService.getMedia(1L)).thenReturn(mediaVm);
        when(mediaService.getMedia(10L)).thenReturn(imgMediaVm);

        ProductDetailInfoVm result = productDetailService.getProductDetailById(1L);
        assertEquals(1, result.getProductImages().size());
    }

    @Test
    void getProductDetailById_withVariations() {
        product.setHasOptions(true);

        Product variation = Product.builder()
            .id(20L).name("Var").slug("var").sku("VSKU").gtin("VGTIN")
            .price(50.0).isPublished(true).thumbnailMediaId(5L)
            .productImages(List.of()).build();
        product.setProducts(List.of(variation));
        product.setProductCategories(List.of());

        ProductOption option = new ProductOption();
        option.setId(1L);
        option.setName("Color");
        ProductOptionCombination combo = ProductOptionCombination.builder()
            .product(variation).productOption(option).value("Red").build();

        NoFileMediaVm mediaVm = new NoFileMediaVm(1L, "cap", "file", "type", "http://url");
        NoFileMediaVm varMediaVm = new NoFileMediaVm(5L, "cap", "file", "type", "http://var-url");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(mediaService.getMedia(1L)).thenReturn(mediaVm);
        when(mediaService.getMedia(5L)).thenReturn(varMediaVm);
        when(productOptionCombinationRepository.findAllByProduct(variation)).thenReturn(List.of(combo));

        ProductDetailInfoVm result = productDetailService.getProductDetailById(1L);
        assertEquals(1, result.getVariations().size());
    }

    @Test
    void getProductDetailById_withUnpublishedVariation() {
        product.setHasOptions(true);

        Product unpublishedVar = Product.builder()
            .id(20L).name("Var").slug("var").isPublished(false)
            .productImages(List.of()).build();
        product.setProducts(List.of(unpublishedVar));
        product.setProductCategories(List.of());

        NoFileMediaVm mediaVm = new NoFileMediaVm(1L, "cap", "file", "type", "http://url");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(mediaService.getMedia(1L)).thenReturn(mediaVm);

        ProductDetailInfoVm result = productDetailService.getProductDetailById(1L);
        assertTrue(result.getVariations().isEmpty());
    }

    @Test
    void getProductDetailById_withAttributes() {
        ProductAttribute attr = ProductAttribute.builder().id(1L).name("Material").build();
        ProductAttributeValue attrVal = new ProductAttributeValue();
        attrVal.setId(1L);
        attrVal.setProductAttribute(attr);
        attrVal.setValue("Cotton");
        product.setAttributeValues(List.of(attrVal));
        product.setProductCategories(List.of());

        NoFileMediaVm mediaVm = new NoFileMediaVm(1L, "cap", "file", "type", "http://url");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(mediaService.getMedia(1L)).thenReturn(mediaVm);

        ProductDetailInfoVm result = productDetailService.getProductDetailById(1L);
        assertEquals(1, result.getAttributeValues().size());
    }
}
