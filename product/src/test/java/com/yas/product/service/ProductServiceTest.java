package com.yas.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.yas.commonlibrary.exception.BadRequestException;
import com.yas.commonlibrary.exception.DuplicatedException;
import com.yas.commonlibrary.exception.NotFoundException;
import com.yas.product.model.Brand;
import com.yas.product.model.Category;
import com.yas.product.model.Product;
import com.yas.product.model.ProductCategory;
import com.yas.product.model.ProductImage;
import com.yas.product.model.ProductOption;
import com.yas.product.model.ProductOptionCombination;
import com.yas.product.model.ProductOptionValue;
import com.yas.product.model.ProductRelated;
import com.yas.product.model.attribute.ProductAttribute;
import com.yas.product.model.attribute.ProductAttributeGroup;
import com.yas.product.model.attribute.ProductAttributeValue;
import com.yas.product.model.enumeration.DimensionUnit;
import com.yas.product.model.enumeration.FilterExistInWhSelection;
import com.yas.product.repository.BrandRepository;
import com.yas.product.repository.CategoryRepository;
import com.yas.product.repository.ProductCategoryRepository;
import com.yas.product.repository.ProductImageRepository;
import com.yas.product.repository.ProductOptionCombinationRepository;
import com.yas.product.repository.ProductOptionRepository;
import com.yas.product.repository.ProductOptionValueRepository;
import com.yas.product.repository.ProductRelatedRepository;
import com.yas.product.repository.ProductRepository;
import com.yas.product.viewmodel.ImageVm;
import com.yas.product.viewmodel.NoFileMediaVm;
import com.yas.product.viewmodel.product.*;
import com.yas.product.viewmodel.productoption.ProductOptionValuePostVm;
import com.yas.product.viewmodel.productoption.ProductOptionValuePutVm;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private MediaService mediaService;
    @Mock
    private BrandRepository brandRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductCategoryRepository productCategoryRepository;
    @Mock
    private ProductImageRepository productImageRepository;
    @Mock
    private ProductOptionRepository productOptionRepository;
    @Mock
    private ProductOptionValueRepository productOptionValueRepository;
    @Mock
    private ProductOptionCombinationRepository productOptionCombinationRepository;
    @Mock
    private ProductRelatedRepository productRelatedRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Brand brand;
    private Category category;

    @BeforeEach
    void setUp() {
        brand = new Brand();
        brand.setId(1L);
        brand.setName("TestBrand");
        brand.setSlug("test-brand");

        category = new Category();
        category.setId(1L);
        category.setName("TestCategory");
        category.setSlug("test-category");

        product = Product.builder()
            .id(1L)
            .name("Test Product")
            .slug("test-product")
            .sku("SKU001")
            .gtin("GTIN001")
            .price(100.0)
            .isPublished(true)
            .isFeatured(false)
            .isAllowedToOrder(true)
            .isVisibleIndividually(true)
            .stockTrackingEnabled(false)
            .thumbnailMediaId(1L)
            .brand(brand)
            .productCategories(new ArrayList<>())
            .productImages(new ArrayList<>())
            .relatedProducts(new ArrayList<>())
            .attributeValues(new ArrayList<>())
            .products(new ArrayList<>())
            .weight(10.0)
            .dimensionUnit(DimensionUnit.CM)
            .length(20.0)
            .width(10.0)
            .height(5.0)
            .taxClassId(1L)
            .metaTitle("meta title")
            .metaKeyword("meta keyword")
            .metaDescription("meta desc")
            .build();
    }

    // ===== createProduct tests =====
    @Test
    void createProduct_simple_noVariations_success() {
        ProductPostVm postVm = new ProductPostVm(
            "New Product", "new-product", 1L, List.of(1L),
            "short desc", "desc", "spec", "SKU002", "GTIN002",
            10.0, DimensionUnit.CM, 20.0, 10.0, 5.0, 99.0,
            true, true, false, true, false,
            "meta", "keyword", "metadesc", 1L, List.of(),
            List.of(), List.of(), List.of(), List.of(), 1L
        );

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(10L);
            return p;
        });
        when(categoryRepository.findAllById(List.of(1L))).thenReturn(List.of(category));
        when(productCategoryRepository.saveAll(anyList())).thenReturn(List.of());
        when(productImageRepository.saveAll(anyList())).thenReturn(List.of());

        ProductGetDetailVm result = productService.createProduct(postVm);
        assertNotNull(result);
        assertEquals("new-product", result.slug());
    }

    @Test
    void createProduct_withVariationsAndOptions_success() {
        ProductVariationPostVm variationVm = new ProductVariationPostVm(
            "Var1", "var1-slug", "VAR-SKU", "VAR-GTIN", 50.0, 2L, List.of(),
            Map.of(1L, "Red")
        );
        ProductOptionValuePostVm optionValueVm = new ProductOptionValuePostVm(
            1L, "color", 1, List.of("Red")
        );
        ProductOptionValueDisplay displayVm = ProductOptionValueDisplay.builder()
            .productOptionId(1L).displayType("color").displayOrder(1).value("Red").build();
        ProductPostVm postVm = new ProductPostVm(
            "New Product", "new-product", 1L, List.of(1L),
            "short desc", "desc", "spec", "SKU002", "GTIN002",
            10.0, DimensionUnit.CM, 20.0, 10.0, 5.0, 99.0,
            true, true, false, true, false,
            "meta", "keyword", "metadesc", 1L, List.of(),
            List.of(variationVm), List.of(optionValueVm), List.of(displayVm), List.of(), 1L
        );

        ProductOption productOption = new ProductOption();
        productOption.setId(1L);
        productOption.setName("Color");

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            if (p.getId() == null) p.setId(10L);
            return p;
        });
        when(categoryRepository.findAllById(anyList())).thenReturn(List.of(category));
        when(productCategoryRepository.saveAll(anyList())).thenReturn(List.of());
        when(productImageRepository.saveAll(anyList())).thenReturn(List.of());
        when(productOptionRepository.findAllByIdIn(anyList())).thenReturn(List.of(productOption));

        Product savedVariation = Product.builder().id(20L).name("Var1").slug("var1-slug").build();
        when(productRepository.saveAll(anyList())).thenReturn(List.of(savedVariation));

        ProductOptionValue savedOptionValue = ProductOptionValue.builder()
            .id(1L).product(product).productOption(productOption).displayOrder(1).value("Red").build();
        when(productOptionValueRepository.saveAll(anyList())).thenReturn(List.of(savedOptionValue));
        when(productOptionCombinationRepository.saveAll(anyList())).thenReturn(List.of());

        ProductGetDetailVm result = productService.createProduct(postVm);
        assertNotNull(result);
    }

    @Test
    void createProduct_withRelatedProducts_success() {
        Product relatedProduct = Product.builder().id(2L).name("Related").slug("related").build();
        ProductPostVm postVm = new ProductPostVm(
            "New Product", "new-product", null, List.of(),
            "short", "desc", "spec", "SKU002", "GTIN002",
            10.0, DimensionUnit.CM, 20.0, 10.0, 5.0, 99.0,
            true, true, false, true, false,
            null, null, null, null, List.of(),
            List.of(), List.of(), List.of(), List.of(2L), null
        );

        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(10L);
            return p;
        });
        when(productRepository.findAllById(List.of(2L))).thenReturn(List.of(relatedProduct));
        when(productRelatedRepository.saveAll(anyList())).thenReturn(List.of());
        when(productImageRepository.saveAll(anyList())).thenReturn(List.of());

        ProductGetDetailVm result = productService.createProduct(postVm);
        assertNotNull(result);
        verify(productRelatedRepository).saveAll(anyList());
    }

    @Test
    void createProduct_lengthLessThanWidth_throwsBadRequest() {
        ProductPostVm postVm = new ProductPostVm(
            "Product", "slug", null, List.of(),
            "short", "desc", "spec", "SKU", "GTIN",
            10.0, DimensionUnit.CM, 5.0, 20.0, 5.0, 99.0,
            true, true, false, true, false,
            null, null, null, null, List.of(),
            List.of(), List.of(), List.of(), List.of(), null
        );

        assertThrows(BadRequestException.class, () -> productService.createProduct(postVm));
    }

    @Test
    void createProduct_duplicateSlug_throwsDuplicated() {
        Product existingProduct = Product.builder().id(99L).slug("dup-slug").build();
        ProductPostVm postVm = new ProductPostVm(
            "Product", "dup-slug", null, List.of(),
            "short", "desc", "spec", "SKU", "",
            10.0, DimensionUnit.CM, 20.0, 10.0, 5.0, 99.0,
            true, true, false, true, false,
            null, null, null, null, List.of(),
            List.of(), List.of(), List.of(), List.of(), null
        );

        when(productRepository.findBySlugAndIsPublishedTrue("dup-slug")).thenReturn(Optional.of(existingProduct));

        assertThrows(DuplicatedException.class, () -> productService.createProduct(postVm));
    }

    @Test
    void createProduct_duplicateSku_throwsDuplicated() {
        Product existingProduct = Product.builder().id(99L).slug("other").sku("DUP-SKU").build();
        ProductPostVm postVm = new ProductPostVm(
            "Product", "unique-slug", null, List.of(),
            "short", "desc", "spec", "DUP-SKU", "",
            10.0, DimensionUnit.CM, 20.0, 10.0, 5.0, 99.0,
            true, true, false, true, false,
            null, null, null, null, List.of(),
            List.of(), List.of(), List.of(), List.of(), null
        );

        when(productRepository.findBySkuAndIsPublishedTrue("DUP-SKU")).thenReturn(Optional.of(existingProduct));

        assertThrows(DuplicatedException.class, () -> productService.createProduct(postVm));
    }

    @Test
    void createProduct_duplicateGtin_throwsDuplicated() {
        Product existingProduct = Product.builder().id(99L).slug("other").sku("other").gtin("DUP-GTIN").build();
        ProductPostVm postVm = new ProductPostVm(
            "Product", "unique-slug", null, List.of(),
            "short", "desc", "spec", "unique-sku", "DUP-GTIN",
            10.0, DimensionUnit.CM, 20.0, 10.0, 5.0, 99.0,
            true, true, false, true, false,
            null, null, null, null, List.of(),
            List.of(), List.of(), List.of(), List.of(), null
        );

        when(productRepository.findBySlugAndIsPublishedTrue(anyString())).thenReturn(Optional.empty());
        when(productRepository.findByGtinAndIsPublishedTrue("DUP-GTIN")).thenReturn(Optional.of(existingProduct));

        assertThrows(DuplicatedException.class, () -> productService.createProduct(postVm));
    }

    @Test
    void createProduct_variationDuplicateSlug_throwsDuplicated() {
        ProductVariationPostVm var1 = new ProductVariationPostVm(
            "V1", "same-slug", "S1", "G1", 10.0, null, List.of(), Map.of());
        ProductVariationPostVm var2 = new ProductVariationPostVm(
            "V2", "same-slug", "S2", "G2", 20.0, null, List.of(), Map.of());

        ProductPostVm postVm = new ProductPostVm(
            "Product", "main-slug", null, List.of(),
            "short", "desc", "spec", "mainsku", "",
            10.0, DimensionUnit.CM, 20.0, 10.0, 5.0, 99.0,
            true, true, false, true, false,
            null, null, null, null, List.of(),
            List.of(var1, var2), List.of(), List.of(), List.of(), null
        );

        assertThrows(DuplicatedException.class, () -> productService.createProduct(postVm));
    }

    @Test
    void createProduct_variationDuplicateSku_throwsDuplicated() {
        ProductVariationPostVm var1 = new ProductVariationPostVm(
            "V1", "slug1", "SAME-SKU", "", 10.0, null, List.of(), Map.of());
        ProductVariationPostVm var2 = new ProductVariationPostVm(
            "V2", "slug2", "SAME-SKU", "", 20.0, null, List.of(), Map.of());

        ProductPostVm postVm = new ProductPostVm(
            "Product", "main-slug", null, List.of(),
            "short", "desc", "spec", "mainsku", "",
            10.0, DimensionUnit.CM, 20.0, 10.0, 5.0, 99.0,
            true, true, false, true, false,
            null, null, null, null, List.of(),
            List.of(var1, var2), List.of(), List.of(), List.of(), null
        );

        assertThrows(DuplicatedException.class, () -> productService.createProduct(postVm));
    }

    @Test
    void createProduct_variationDuplicateGtin_throwsDuplicated() {
        ProductVariationPostVm var1 = new ProductVariationPostVm(
            "V1", "slug1", "sku1", "SAME-GTIN", 10.0, null, List.of(), Map.of());
        ProductVariationPostVm var2 = new ProductVariationPostVm(
            "V2", "slug2", "sku2", "SAME-GTIN", 20.0, null, List.of(), Map.of());

        ProductPostVm postVm = new ProductPostVm(
            "Product", "main-slug", null, List.of(),
            "short", "desc", "spec", "mainsku", "",
            10.0, DimensionUnit.CM, 20.0, 10.0, 5.0, 99.0,
            true, true, false, true, false,
            null, null, null, null, List.of(),
            List.of(var1, var2), List.of(), List.of(), List.of(), null
        );

        assertThrows(DuplicatedException.class, () -> productService.createProduct(postVm));
    }

    // ===== updateProduct tests =====
    @Test
    void updateProduct_simple_success() {
        ProductOptionValuePutVm optValVm = new ProductOptionValuePutVm(1L, "color", 1, List.of("Red"));
        ProductOptionValueDisplay displayVm = ProductOptionValueDisplay.builder()
            .productOptionId(1L).displayType("color").displayOrder(1).value("Red").build();

        ProductPutVm putVm = new ProductPutVm(
            "Updated", "updated-slug", 99.0, true, true, false, true, false,
            1L, List.of(), "short", "desc", "spec", "SKU001", "GTIN001",
            10.0, DimensionUnit.CM, 20.0, 10.0, 5.0,
            "meta", "keyword", "metadesc", 1L, List.of(),
            List.of(), List.of(optValVm), List.of(displayVm),
            List.of(2L), 1L
        );

        Product relatedProduct = Product.builder().id(2L).name("Related").slug("related").build();
        ProductRelated existingRelation = ProductRelated.builder()
            .id(1L).product(product).relatedProduct(Product.builder().id(3L).build()).build();
        product.setRelatedProducts(List.of(existingRelation));

        ProductOption productOption = new ProductOption();
        productOption.setId(1L);
        productOption.setName("Color");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(productRepository.findBySlugAndIsPublishedTrue("updated-slug")).thenReturn(Optional.of(product));
        when(productRepository.findBySkuAndIsPublishedTrue("SKU001")).thenReturn(Optional.of(product));
        when(productRepository.findByGtinAndIsPublishedTrue("GTIN001")).thenReturn(Optional.of(product));
        when(productCategoryRepository.findAllByProductId(1L)).thenReturn(List.of());
        when(productImageRepository.saveAll(anyList())).thenReturn(List.of());
        when(productRepository.findAllById(anySet())).thenReturn(List.of(relatedProduct));
        when(productRepository.saveAll(anyList())).thenReturn(List.of());
        when(productOptionRepository.findAllByIdIn(anyList())).thenReturn(List.of(productOption));
        when(productOptionValueRepository.saveAll(anyList())).thenReturn(List.of());

        assertDoesNotThrow(() -> productService.updateProduct(1L, putVm));
    }

    @Test
    void updateProduct_productNotFound_throwsNotFound() {
        ProductPutVm putVm = new ProductPutVm(
            "Updated", "updated-slug", 99.0, true, true, false, true, false,
            null, List.of(), "short", "desc", "spec", "SKU", "",
            10.0, DimensionUnit.CM, 20.0, 10.0, 5.0,
            null, null, null, null, List.of(),
            List.of(), List.of(), List.of(), List.of(), null
        );

        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.updateProduct(999L, putVm));
    }

    @Test
    void updateProduct_withExistingVariants_success() {
        Product existingVariant = Product.builder().id(20L).name("OldVar").slug("old-var").build();
        product.setProducts(List.of(existingVariant));

        ProductVariationPutVm existingVarVm = new ProductVariationPutVm(
            20L, "UpdatedVar", "updated-var", "VARSKU", "VARGTIN", 50.0, null, List.of(), Map.of()
        );
        ProductOptionValuePutVm optionValueVm = new ProductOptionValuePutVm(1L, "color", 1, List.of("Red"));
        ProductOptionValueDisplay displayVm = ProductOptionValueDisplay.builder()
            .productOptionId(1L).displayType("color").displayOrder(1).value("Red").build();

        ProductPutVm putVm = new ProductPutVm(
            "Updated", "updated-slug", 99.0, true, true, false, true, false,
            1L, List.of(), "short", "desc", "spec", "SKU001", "GTIN001",
            10.0, DimensionUnit.CM, 20.0, 10.0, 5.0,
            "meta", "keyword", "metadesc", 1L, List.of(),
            List.of(existingVarVm), List.of(optionValueVm), List.of(displayVm), List.of(), 1L
        );

        ProductOption productOption = new ProductOption();
        productOption.setId(1L);
        productOption.setName("Color");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(productRepository.findBySlugAndIsPublishedTrue("updated-slug")).thenReturn(Optional.of(product));
        when(productRepository.findBySkuAndIsPublishedTrue("SKU001")).thenReturn(Optional.of(product));
        when(productRepository.findByGtinAndIsPublishedTrue("GTIN001")).thenReturn(Optional.of(product));
        when(productRepository.findAllById(anyList())).thenReturn(List.of(existingVariant));
        when(productCategoryRepository.findAllByProductId(1L)).thenReturn(List.of());
        when(productImageRepository.saveAll(anyList())).thenReturn(List.of());
        when(productRepository.saveAll(anyList())).thenReturn(List.of());
        when(productOptionRepository.findAllByIdIn(anyList())).thenReturn(List.of(productOption));
        when(productOptionValueRepository.saveAll(anyList())).thenReturn(List.of());

        assertDoesNotThrow(() -> productService.updateProduct(1L, putVm));
    }

    @Test
    void updateProduct_withNewVariations_success() {
        product.setProducts(new ArrayList<>());

        ProductVariationPutVm newVarVm = new ProductVariationPutVm(
            null, "NewVar", "new-var", "NEWSKU", "NEWGTIN", 50.0, null, List.of(), Map.of(1L, "Blue")
        );
        ProductOptionValuePutVm optionValueVm = new ProductOptionValuePutVm(1L, "color", 1, List.of("Blue"));
        ProductOptionValueDisplay displayVm = ProductOptionValueDisplay.builder()
            .productOptionId(1L).displayType("color").displayOrder(1).value("Blue").build();

        ProductPutVm putVm = new ProductPutVm(
            "Updated", "updated-slug", 99.0, true, true, false, true, false,
            1L, List.of(), "short", "desc", "spec", "SKU001", "GTIN001",
            10.0, DimensionUnit.CM, 20.0, 10.0, 5.0,
            "meta", "keyword", "metadesc", 1L, List.of(),
            List.of(newVarVm), List.of(optionValueVm), List.of(displayVm), List.of(), 1L
        );

        ProductOption productOption = new ProductOption();
        productOption.setId(1L);
        productOption.setName("Color");

        Product savedVar = Product.builder().id(30L).name("NewVar").slug("new-var").build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(productRepository.findBySlugAndIsPublishedTrue("updated-slug")).thenReturn(Optional.of(product));
        when(productRepository.findBySkuAndIsPublishedTrue("SKU001")).thenReturn(Optional.of(product));
        when(productRepository.findByGtinAndIsPublishedTrue("GTIN001")).thenReturn(Optional.of(product));
        when(productCategoryRepository.findAllByProductId(1L)).thenReturn(List.of());
        when(productImageRepository.saveAll(anyList())).thenReturn(List.of());
        when(productRepository.saveAll(anyList())).thenReturn(List.of(savedVar));
        when(productOptionRepository.findAllByIdIn(anyList())).thenReturn(List.of(productOption));

        ProductOptionValue savedOptionValue = ProductOptionValue.builder()
            .id(1L).product(product).productOption(productOption).displayOrder(1).value("Blue").build();
        when(productOptionValueRepository.saveAll(anyList())).thenReturn(List.of(savedOptionValue));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productOptionCombinationRepository.saveAll(anyList())).thenReturn(List.of());

        assertDoesNotThrow(() -> productService.updateProduct(1L, putVm));
    }

    // ===== deleteProduct tests =====
    @Test
    void deleteProduct_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        assertDoesNotThrow(() -> productService.deleteProduct(1L));
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void deleteProduct_withParentAndCombinations() {
        Product parentProduct = Product.builder().id(99L).name("Parent").build();
        product.setParent(parentProduct);

        ProductOptionCombination combo = ProductOptionCombination.builder().id(1L).product(product).build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productOptionCombinationRepository.findAllByProduct(product)).thenReturn(List.of(combo));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        assertDoesNotThrow(() -> productService.deleteProduct(1L));
        verify(productOptionCombinationRepository).deleteAll(anyList());
    }

    @Test
    void deleteProduct_notFound_throwsNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.deleteProduct(999L));
    }

    // ===== getProductsWithFilter tests =====
    @Test
    void getProductsWithFilter_success() {
        Page<Product> productPage = new PageImpl<>(List.of(product));
        when(productRepository.getProductsWithFilter(anyString(), anyString(), any(Pageable.class)))
            .thenReturn(productPage);

        ProductListGetVm result = productService.getProductsWithFilter(0, 10, "test", "brand");
        assertNotNull(result);
        assertEquals(1, result.productContent().size());
    }

    // ===== getProductById tests =====
    @Test
    void getProductById_success() {
        ProductImage img = ProductImage.builder().id(1L).imageId(10L).product(product).build();
        product.setProductImages(List.of(img));

        ProductCategory pc = ProductCategory.builder().product(product).category(category).build();
        product.setProductCategories(List.of(pc));

        NoFileMediaVm mediaVm = new NoFileMediaVm(1L, "cap", "file", "type", "http://url");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(mediaService.getMedia(anyLong())).thenReturn(mediaVm);

        ProductDetailVm result = productService.getProductById(1L);
        assertNotNull(result);
        assertEquals("Test Product", result.name());
        assertEquals(1L, result.brandId());
    }

    @Test
    void getProductById_noThumbnail_noBrand() {
        product.setThumbnailMediaId(null);
        product.setBrand(null);
        product.setProductImages(null);
        product.setProductCategories(null);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDetailVm result = productService.getProductById(1L);
        assertNotNull(result);
        assertNull(result.thumbnailMedia());
        assertNull(result.brandId());
    }

    @Test
    void getProductById_notFound_throwsNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.getProductById(999L));
    }

    // ===== getLatestProducts tests =====
    @Test
    void getLatestProducts_success() {
        when(productRepository.getLatestProducts(any(Pageable.class))).thenReturn(List.of(product));
        List<ProductListVm> result = productService.getLatestProducts(5);
        assertEquals(1, result.size());
    }

    @Test
    void getLatestProducts_countZero_returnsEmpty() {
        List<ProductListVm> result = productService.getLatestProducts(0);
        assertTrue(result.isEmpty());
    }

    @Test
    void getLatestProducts_negativeCount_returnsEmpty() {
        List<ProductListVm> result = productService.getLatestProducts(-1);
        assertTrue(result.isEmpty());
    }

    @Test
    void getLatestProducts_emptyResult_returnsEmpty() {
        when(productRepository.getLatestProducts(any(Pageable.class))).thenReturn(List.of());
        List<ProductListVm> result = productService.getLatestProducts(5);
        assertTrue(result.isEmpty());
    }

    // ===== getProductsByBrand tests =====
    @Test
    void getProductsByBrand_success() {
        NoFileMediaVm mediaVm = new NoFileMediaVm(1L, "cap", "file", "type", "http://url");
        when(brandRepository.findBySlug("test-brand")).thenReturn(Optional.of(brand));
        when(productRepository.findAllByBrandAndIsPublishedTrueOrderByIdAsc(brand)).thenReturn(List.of(product));
        when(mediaService.getMedia(anyLong())).thenReturn(mediaVm);

        List<ProductThumbnailVm> result = productService.getProductsByBrand("test-brand");
        assertEquals(1, result.size());
    }

    @Test
    void getProductsByBrand_notFound_throwsNotFound() {
        when(brandRepository.findBySlug("unknown")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.getProductsByBrand("unknown"));
    }

    // ===== getProductsFromCategory tests =====
    @Test
    void getProductsFromCategory_success() {
        ProductCategory pc = ProductCategory.builder().product(product).category(category).build();
        Page<ProductCategory> page = new PageImpl<>(List.of(pc));
        NoFileMediaVm mediaVm = new NoFileMediaVm(1L, "cap", "file", "type", "http://url");

        when(categoryRepository.findBySlug("test-category")).thenReturn(Optional.of(category));
        when(productCategoryRepository.findAllByCategory(any(Pageable.class), eq(category))).thenReturn(page);
        when(mediaService.getMedia(anyLong())).thenReturn(mediaVm);

        ProductListGetFromCategoryVm result = productService.getProductsFromCategory(0, 10, "test-category");
        assertNotNull(result);
        assertEquals(1, result.productContent().size());
    }

    @Test
    void getProductsFromCategory_categoryNotFound_throwsNotFound() {
        when(categoryRepository.findBySlug("unknown")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.getProductsFromCategory(0, 10, "unknown"));
    }

    // ===== getFeaturedProductsById tests =====
    @Test
    void getFeaturedProductsById_withThumbnailUrl() {
        NoFileMediaVm mediaVm = new NoFileMediaVm(1L, "cap", "file", "type", "http://url");
        when(productRepository.findAllByIdIn(anyList())).thenReturn(List.of(product));
        when(mediaService.getMedia(anyLong())).thenReturn(mediaVm);

        List<ProductThumbnailGetVm> result = productService.getFeaturedProductsById(List.of(1L));
        assertEquals(1, result.size());
    }

    @Test
    void getFeaturedProductsById_withParentProduct_noThumbnail() {
        Product parent = Product.builder().id(2L).name("Parent").thumbnailMediaId(5L).build();
        Product childProduct = Product.builder()
            .id(3L).name("Child").slug("child").thumbnailMediaId(null)
            .parent(parent).price(50.0).build();

        NoFileMediaVm emptyMedia = new NoFileMediaVm(null, "", "", "", "");
        NoFileMediaVm parentMedia = new NoFileMediaVm(5L, "cap", "file", "type", "http://parent-url");

        when(productRepository.findAllByIdIn(anyList())).thenReturn(List.of(childProduct));
        when(mediaService.getMedia(null)).thenReturn(emptyMedia);
        when(productRepository.findById(2L)).thenReturn(Optional.of(parent));
        when(mediaService.getMedia(5L)).thenReturn(parentMedia);

        List<ProductThumbnailGetVm> result = productService.getFeaturedProductsById(List.of(3L));
        assertEquals(1, result.size());
    }

    // ===== getListFeaturedProducts tests =====
    @Test
    void getListFeaturedProducts_success() {
        Page<Product> page = new PageImpl<>(List.of(product));
        NoFileMediaVm mediaVm = new NoFileMediaVm(1L, "cap", "file", "type", "http://url");
        when(productRepository.getFeaturedProduct(any(Pageable.class))).thenReturn(page);
        when(mediaService.getMedia(anyLong())).thenReturn(mediaVm);

        ProductFeatureGetVm result = productService.getListFeaturedProducts(0, 10);
        assertNotNull(result);
        assertEquals(1, result.productList().size());
    }

    // ===== getProductDetail tests =====
    @Test
    void getProductDetail_success_withAttributes() {
        ProductAttribute attr = ProductAttribute.builder().id(1L).name("Material").build();
        ProductAttributeGroup group = new ProductAttributeGroup();
        group.setId(1L);
        group.setName("General");
        attr.setProductAttributeGroup(group);

        ProductAttributeValue attrValue = new ProductAttributeValue();
        attrValue.setProductAttribute(attr);
        attrValue.setValue("Cotton");
        product.setAttributeValues(List.of(attrValue));

        ProductImage img = ProductImage.builder().imageId(10L).product(product).build();
        product.setProductImages(List.of(img));

        ProductCategory pc = ProductCategory.builder().product(product).category(category).build();
        product.setProductCategories(List.of(pc));

        NoFileMediaVm mediaVm = new NoFileMediaVm(1L, "cap", "file", "type", "http://url");

        when(productRepository.findBySlugAndIsPublishedTrue("test-product")).thenReturn(Optional.of(product));
        when(mediaService.getMedia(anyLong())).thenReturn(mediaVm);

        ProductDetailGetVm result = productService.getProductDetail("test-product");
        assertNotNull(result);
        assertEquals(1, result.productAttributeGroups().size());
        assertEquals("General", result.productAttributeGroups().get(0).name());
    }

    @Test
    void getProductDetail_withNullAttributeGroup() {
        ProductAttribute attr = ProductAttribute.builder().id(1L).name("Material").build();
        attr.setProductAttributeGroup(null);

        ProductAttributeValue attrValue = new ProductAttributeValue();
        attrValue.setProductAttribute(attr);
        attrValue.setValue("Cotton");
        product.setAttributeValues(List.of(attrValue));

        ProductCategory pc = ProductCategory.builder().product(product).category(category).build();
        product.setProductCategories(List.of(pc));
        product.setProductImages(List.of());

        NoFileMediaVm mediaVm = new NoFileMediaVm(1L, "cap", "file", "type", "http://url");

        when(productRepository.findBySlugAndIsPublishedTrue("test-product")).thenReturn(Optional.of(product));
        when(mediaService.getMedia(anyLong())).thenReturn(mediaVm);

        ProductDetailGetVm result = productService.getProductDetail("test-product");
        assertNotNull(result);
        assertEquals("None group", result.productAttributeGroups().get(0).name());
    }

    @Test
    void getProductDetail_notFound_throwsNotFound() {
        when(productRepository.findBySlugAndIsPublishedTrue("unknown")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.getProductDetail("unknown"));
    }

    @Test
    void getProductDetail_noImages() {
        product.setProductImages(null);
        product.setAttributeValues(List.of());
        product.setProductCategories(List.of(ProductCategory.builder().product(product).category(category).build()));

        NoFileMediaVm mediaVm = new NoFileMediaVm(1L, "cap", "file", "type", "http://url");
        when(productRepository.findBySlugAndIsPublishedTrue("test-product")).thenReturn(Optional.of(product));
        when(mediaService.getMedia(anyLong())).thenReturn(mediaVm);

        ProductDetailGetVm result = productService.getProductDetail("test-product");
        assertNotNull(result);
        assertTrue(result.productImageMediaUrls().isEmpty());
    }

    // ===== getProductsByMultiQuery tests =====
    @Test
    void getProductsByMultiQuery_success() {
        Page<Product> page = new PageImpl<>(List.of(product));
        NoFileMediaVm mediaVm = new NoFileMediaVm(1L, "cap", "file", "type", "http://url");

        when(productRepository.findByProductNameAndCategorySlugAndPriceBetween(
            anyString(), anyString(), anyDouble(), anyDouble(), any(Pageable.class))).thenReturn(page);
        when(mediaService.getMedia(anyLong())).thenReturn(mediaVm);

        ProductsGetVm result = productService.getProductsByMultiQuery(0, 10, "test", "cat", 0.0, 100.0);
        assertNotNull(result);
        assertEquals(1, result.productContent().size());
    }

    // ===== getProductVariationsByParentId tests =====
    @Test
    void getProductVariationsByParentId_hasOptions_success() {
        Product variation = Product.builder()
            .id(20L).name("Var").slug("var").sku("VSKU").gtin("VGTIN")
            .price(50.0).isPublished(true).thumbnailMediaId(5L)
            .productImages(List.of()).parent(product).build();
        product.setHasOptions(true);
        product.setProducts(List.of(variation));

        ProductOption option = new ProductOption();
        option.setId(1L);
        option.setName("Color");
        ProductOptionCombination combo = ProductOptionCombination.builder()
            .product(variation).productOption(option).value("Red").build();

        NoFileMediaVm mediaVm = new NoFileMediaVm(5L, "cap", "file", "type", "http://url");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productOptionCombinationRepository.findAllByProduct(variation)).thenReturn(List.of(combo));
        when(mediaService.getMedia(5L)).thenReturn(mediaVm);

        List<ProductVariationGetVm> result = productService.getProductVariationsByParentId(1L);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).thumbnail());
    }

    @Test
    void getProductVariationsByParentId_noOptions_returnsEmpty() {
        product.setHasOptions(false);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        List<ProductVariationGetVm> result = productService.getProductVariationsByParentId(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void getProductVariationsByParentId_variationNoThumbnail() {
        Product variation = Product.builder()
            .id(20L).name("Var").slug("var").sku("VSKU").gtin("VGTIN")
            .price(50.0).isPublished(true).thumbnailMediaId(null)
            .productImages(List.of()).parent(product).build();
        product.setHasOptions(true);
        product.setProducts(List.of(variation));

        ProductOption option = new ProductOption();
        option.setId(1L);
        ProductOptionCombination combo = ProductOptionCombination.builder()
            .product(variation).productOption(option).value("Red").build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productOptionCombinationRepository.findAllByProduct(variation)).thenReturn(List.of(combo));

        List<ProductVariationGetVm> result = productService.getProductVariationsByParentId(1L);
        assertEquals(1, result.size());
        assertNull(result.get(0).thumbnail());
    }

    @Test
    void getProductVariationsByParentId_notFound_throwsNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.getProductVariationsByParentId(999L));
    }

    // ===== exportProducts tests =====
    @Test
    void exportProducts_success() {
        when(productRepository.getExportingProducts(anyString(), anyString())).thenReturn(List.of(product));
        List<ProductExportingDetailVm> result = productService.exportProducts("test", "brand");
        assertEquals(1, result.size());
    }

    // ===== getProductSlug tests =====
    @Test
    void getProductSlug_withParent() {
        Product parent = Product.builder().id(2L).slug("parent-slug").build();
        product.setParent(parent);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductSlugGetVm result = productService.getProductSlug(1L);
        assertEquals("parent-slug", result.slug());
        assertEquals(1L, result.productVariantId());
    }

    @Test
    void getProductSlug_withoutParent() {
        product.setParent(null);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductSlugGetVm result = productService.getProductSlug(1L);
        assertEquals("test-product", result.slug());
        assertNull(result.productVariantId());
    }

    @Test
    void getProductSlug_notFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.getProductSlug(999L));
    }

    // ===== getProductEsDetailById tests =====
    @Test
    void getProductEsDetailById_success() {
        ProductCategory pc = ProductCategory.builder().product(product).category(category).build();
        product.setProductCategories(List.of(pc));

        ProductAttribute attr = ProductAttribute.builder().id(1L).name("Attr1").build();
        ProductAttributeValue attrVal = new ProductAttributeValue();
        attrVal.setProductAttribute(attr);
        product.setAttributeValues(List.of(attrVal));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductEsDetailVm result = productService.getProductEsDetailById(1L);
        assertNotNull(result);
        assertEquals("TestBrand", result.brand());
        assertEquals(1, result.categories().size());
    }

    @Test
    void getProductEsDetailById_noThumbnailNoBrand() {
        product.setThumbnailMediaId(null);
        product.setBrand(null);
        product.setProductCategories(List.of());
        product.setAttributeValues(List.of());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductEsDetailVm result = productService.getProductEsDetailById(1L);
        assertNull(result.thumbnailMediaId());
        assertNull(result.brand());
    }

    @Test
    void getProductEsDetailById_notFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.getProductEsDetailById(999L));
    }

    // ===== getRelatedProductsBackoffice tests =====
    @Test
    void getRelatedProductsBackoffice_success() {
        Product relatedProd = Product.builder()
            .id(2L).name("Related").slug("related").price(50.0)
            .isAllowedToOrder(true).isPublished(true).isFeatured(false)
            .isVisibleIndividually(true).taxClassId(1L).parent(null).build();
        ProductRelated relation = ProductRelated.builder()
            .id(1L).product(product).relatedProduct(relatedProd).build();
        product.setRelatedProducts(List.of(relation));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        List<ProductListVm> result = productService.getRelatedProductsBackoffice(1L);
        assertEquals(1, result.size());
    }

    @Test
    void getRelatedProductsBackoffice_notFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.getRelatedProductsBackoffice(999L));
    }

    // ===== getRelatedProductsStorefront tests =====
    @Test
    void getRelatedProductsStorefront_success() {
        Product relatedProd = Product.builder()
            .id(2L).name("Related").slug("related").price(50.0)
            .isPublished(true).thumbnailMediaId(5L).build();
        ProductRelated relation = ProductRelated.builder()
            .product(product).relatedProduct(relatedProd).build();
        Page<ProductRelated> page = new PageImpl<>(List.of(relation));

        NoFileMediaVm mediaVm = new NoFileMediaVm(5L, "cap", "file", "type", "http://url");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRelatedRepository.findAllByProduct(eq(product), any(Pageable.class))).thenReturn(page);
        when(mediaService.getMedia(5L)).thenReturn(mediaVm);

        ProductsGetVm result = productService.getRelatedProductsStorefront(1L, 0, 10);
        assertNotNull(result);
    }

    @Test
    void getRelatedProductsStorefront_filterUnpublished() {
        Product unpublishedRelated = Product.builder()
            .id(3L).name("Unpublished").slug("unp").price(30.0)
            .isPublished(false).thumbnailMediaId(5L).build();
        ProductRelated relation = ProductRelated.builder()
            .product(product).relatedProduct(unpublishedRelated).build();
        Page<ProductRelated> page = new PageImpl<>(List.of(relation));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRelatedRepository.findAllByProduct(eq(product), any(Pageable.class))).thenReturn(page);

        ProductsGetVm result = productService.getRelatedProductsStorefront(1L, 0, 10);
        assertTrue(result.productContent().isEmpty());
    }

    // ===== getProductsForWarehouse tests =====
    @Test
    void getProductsForWarehouse_success() {
        when(productRepository.findProductForWarehouse(anyString(), anyString(), anyList(), anyString()))
            .thenReturn(List.of(product));

        List<ProductInfoVm> result = productService.getProductsForWarehouse(
            "test", "SKU", List.of(1L), FilterExistInWhSelection.YES);
        assertEquals(1, result.size());
    }

    // ===== updateProductQuantity tests =====
    @Test
    void updateProductQuantity_success() {
        ProductQuantityPostVm qvm = new ProductQuantityPostVm(1L, 100L);
        when(productRepository.findAllByIdIn(anyList())).thenReturn(List.of(product));
        when(productRepository.saveAll(anyList())).thenReturn(List.of(product));

        assertDoesNotThrow(() -> productService.updateProductQuantity(List.of(qvm)));
    }

    // ===== subtractStockQuantity tests =====
    @Test
    void subtractStockQuantity_success() {
        product.setStockTrackingEnabled(true);
        product.setStockQuantity(100L);
        ProductQuantityPutVm qvm = new ProductQuantityPutVm(1L, 30L);

        when(productRepository.findAllByIdIn(anyList())).thenReturn(List.of(product));
        when(productRepository.saveAll(anyList())).thenReturn(List.of(product));

        assertDoesNotThrow(() -> productService.subtractStockQuantity(List.of(qvm)));
        assertEquals(70L, product.getStockQuantity());
    }

    @Test
    void subtractStockQuantity_goesNegative_setsZero() {
        product.setStockTrackingEnabled(true);
        product.setStockQuantity(10L);
        ProductQuantityPutVm qvm = new ProductQuantityPutVm(1L, 20L);

        when(productRepository.findAllByIdIn(anyList())).thenReturn(List.of(product));
        when(productRepository.saveAll(anyList())).thenReturn(List.of(product));

        assertDoesNotThrow(() -> productService.subtractStockQuantity(List.of(qvm)));
        assertEquals(0L, product.getStockQuantity());
    }

    // ===== restoreStockQuantity tests =====
    @Test
    void restoreStockQuantity_success() {
        product.setStockTrackingEnabled(true);
        product.setStockQuantity(50L);
        ProductQuantityPutVm qvm = new ProductQuantityPutVm(1L, 30L);

        when(productRepository.findAllByIdIn(anyList())).thenReturn(List.of(product));
        when(productRepository.saveAll(anyList())).thenReturn(List.of(product));

        assertDoesNotThrow(() -> productService.restoreStockQuantity(List.of(qvm)));
        assertEquals(80L, product.getStockQuantity());
    }

    // ===== getProductByIds tests =====
    @Test
    void getProductByIds_success() {
        when(productRepository.findAllByIdIn(anyList())).thenReturn(List.of(product));
        List<ProductListVm> result = productService.getProductByIds(List.of(1L));
        assertEquals(1, result.size());
    }

    // ===== getProductByCategoryIds tests =====
    @Test
    void getProductByCategoryIds_success() {
        when(productRepository.findByCategoryIdsIn(anyList())).thenReturn(List.of(product));
        List<ProductListVm> result = productService.getProductByCategoryIds(List.of(1L));
        assertEquals(1, result.size());
    }

    // ===== getProductByBrandIds tests =====
    @Test
    void getProductByBrandIds_success() {
        when(productRepository.findByBrandIdsIn(anyList())).thenReturn(List.of(product));
        List<ProductListVm> result = productService.getProductByBrandIds(List.of(1L));
        assertEquals(1, result.size());
    }

    // ===== getProductCheckoutList tests =====
    @Test
    void getProductCheckoutList_withThumbnail() {
        Page<Product> page = new PageImpl<>(List.of(product));
        NoFileMediaVm mediaVm = new NoFileMediaVm(1L, "cap", "file", "type", "http://url");

        when(productRepository.findAllPublishedProductsByIds(anyList(), any(Pageable.class))).thenReturn(page);
        when(mediaService.getMedia(anyLong())).thenReturn(mediaVm);

        ProductGetCheckoutListVm result = productService.getProductCheckoutList(0, 10, List.of(1L));
        assertNotNull(result);
        assertEquals(1, result.productCheckoutListVms().size());
    }

    @Test
    void getProductCheckoutList_emptyThumbnail() {
        Page<Product> page = new PageImpl<>(List.of(product));
        NoFileMediaVm mediaVm = new NoFileMediaVm(null, "", "", "", "");

        when(productRepository.findAllPublishedProductsByIds(anyList(), any(Pageable.class))).thenReturn(page);
        when(mediaService.getMedia(anyLong())).thenReturn(mediaVm);

        ProductGetCheckoutListVm result = productService.getProductCheckoutList(0, 10, List.of(1L));
        assertNotNull(result);
    }

    // ===== setProductImages tests =====
    @Test
    void setProductImages_emptyList_deletesExisting() {
        List<ProductImage> result = productService.setProductImages(List.of(), product);
        assertTrue(result.isEmpty());
        verify(productImageRepository).deleteByProductId(product.getId());
    }

    @Test
    void setProductImages_nullProductImages_createsNew() {
        product.setProductImages(null);
        List<ProductImage> result = productService.setProductImages(List.of(10L, 20L), product);
        assertEquals(2, result.size());
    }

    @Test
    void setProductImages_existingImages_addsNewRemovesOld() {
        ProductImage existingImg = ProductImage.builder().id(1L).imageId(10L).product(product).build();
        product.setProductImages(List.of(existingImg));

        List<ProductImage> result = productService.setProductImages(List.of(10L, 20L), product);
        assertEquals(1, result.size()); // only new image 20L
        verify(productImageRepository, never()).deleteByImageIdInAndProductId(anyList(), anyLong());
    }

    @Test
    void setProductImages_existingImages_removeSome() {
        ProductImage existingImg = ProductImage.builder().id(1L).imageId(10L).product(product).build();
        product.setProductImages(List.of(existingImg));

        List<ProductImage> result = productService.setProductImages(List.of(20L), product);
        assertEquals(1, result.size());
        verify(productImageRepository).deleteByImageIdInAndProductId(List.of(10L), product.getId());
    }

    // ===== setProductBrand related (tested through createProduct/updateProduct) =====
    @Test
    void createProduct_withBrandNotFound_throwsNotFound() {
        ProductPostVm postVm = new ProductPostVm(
            "Prod", "slug", 999L, List.of(),
            "short", "desc", "spec", "SKU", "",
            10.0, DimensionUnit.CM, 20.0, 10.0, 5.0, 99.0,
            true, true, false, true, false,
            null, null, null, null, List.of(),
            List.of(), List.of(), List.of(), List.of(), null
        );

        when(brandRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.createProduct(postVm));
    }

    // ===== setProductCategories related =====
    @Test
    void createProduct_categoriesNotFound_throwsBadRequest() {
        ProductPostVm postVm = new ProductPostVm(
            "Prod", "slug", null, List.of(999L),
            "short", "desc", "spec", "SKU", "",
            10.0, DimensionUnit.CM, 20.0, 10.0, 5.0, 99.0,
            true, true, false, true, false,
            null, null, null, null, List.of(),
            List.of(), List.of(), List.of(), List.of(), null
        );

        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(10L);
            p.setProductCategories(new ArrayList<>());
            return p;
        });
        when(categoryRepository.findAllById(List.of(999L))).thenReturn(List.of());
        when(productImageRepository.saveAll(anyList())).thenReturn(List.of());

        assertThrows(BadRequestException.class, () -> productService.createProduct(postVm));
    }

    @Test
    void createProduct_partialCategoriesNotFound_throwsBadRequest() {
        ProductPostVm postVm = new ProductPostVm(
            "Prod", "slug", null, new ArrayList<>(List.of(1L, 999L)),
            "short", "desc", "spec", "SKU", "",
            10.0, DimensionUnit.CM, 20.0, 10.0, 5.0, 99.0,
            true, true, false, true, false,
            null, null, null, null, List.of(),
            List.of(), List.of(), List.of(), List.of(), null
        );

        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(10L);
            p.setProductCategories(new ArrayList<>());
            return p;
        });
        when(categoryRepository.findAllById(anyList())).thenReturn(List.of(category));
        when(productImageRepository.saveAll(anyList())).thenReturn(List.of());

        assertThrows(BadRequestException.class, () -> productService.createProduct(postVm));
    }

    // ===== updateMainProductFromVm test =====
    @Test
    void updateMainProductFromVm_setsAllFields() {
        ProductPutVm putVm = new ProductPutVm(
            "Updated", "updated-slug", 200.0, false, false, true, false, true,
            null, List.of(), "short2", "desc2", "spec2", "SKU2", "GTIN2",
            15.0, DimensionUnit.INCH, 30.0, 15.0, 10.0,
            "meta2", "keyword2", "metadesc2", 2L, List.of(),
            List.of(), List.of(), List.of(), List.of(), 2L
        );

        productService.updateMainProductFromVm(putVm, product);

        assertEquals("Updated", product.getName());
        assertEquals("updated-slug", product.getSlug());
        assertEquals(200.0, product.getPrice());
        assertEquals("SKU2", product.getSku());
        assertEquals("GTIN2", product.getGtin());
        assertEquals(DimensionUnit.INCH, product.getDimensionUnit());
        assertEquals(2L, product.getTaxClassId());
    }

    // ===== stockTracking disabled =====
    @Test
    void subtractStockQuantity_trackingDisabled_noChange() {
        product.setStockTrackingEnabled(false);
        product.setStockQuantity(100L);
        ProductQuantityPutVm qvm = new ProductQuantityPutVm(1L, 30L);

        when(productRepository.findAllByIdIn(anyList())).thenReturn(List.of(product));
        when(productRepository.saveAll(anyList())).thenReturn(List.of(product));

        productService.subtractStockQuantity(List.of(qvm));
        assertEquals(100L, product.getStockQuantity());
    }

    // ===== mergeProductQuantityItem (duplicate productIds) =====
    @Test
    void subtractStockQuantity_duplicateProductIds_mergesQuantity() {
        product.setStockTrackingEnabled(true);
        product.setStockQuantity(100L);
        ProductQuantityPutVm qvm1 = new ProductQuantityPutVm(1L, 10L);
        ProductQuantityPutVm qvm2 = new ProductQuantityPutVm(1L, 20L);

        when(productRepository.findAllByIdIn(anyList())).thenReturn(List.of(product));
        when(productRepository.saveAll(anyList())).thenReturn(List.of(product));

        productService.subtractStockQuantity(List.of(qvm1, qvm2));
        assertEquals(70L, product.getStockQuantity());
    }
}
