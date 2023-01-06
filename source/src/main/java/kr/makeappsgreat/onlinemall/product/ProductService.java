package kr.makeappsgreat.onlinemall.product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Value("${product.list.page_request_size}")
    private int PAGE_SIZE;

    private final ManufacturerRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public Optional<Product> getProduct(Long id) {
        return productRepository.findById(id);
    }

    public Page<Product> getPagedProducts(ProductPageRequest productPageRequest) {
        Page<Product> result;
        PageRequest pageRequest = PageRequest.of(productPageRequest.getPage() - 1, PAGE_SIZE, productPageRequest.getSort());

        if (productPageRequest.getKeyword() != null && !productPageRequest.getKeyword().isBlank()) {
            result = productRepository.findByNameContainingIgnoreCaseOrSimpleDetailContainingIgnoreCase(
                    productPageRequest.getKeyword(),
                    productPageRequest.getKeyword(),
                    pageRequest);
        } else if (productPageRequest.getManufacturer() != null && productPageRequest.getCategory() != null) {
            result = productRepository.findByManufacturerAndCategory(
                    Manufacturer.of(productPageRequest.getManufacturer()),
                    Category.of(productPageRequest.getCategory()),
                    pageRequest);
        } else if (productPageRequest.getManufacturer() != null) {
            result = productRepository.findByManufacturer(
                    Manufacturer.of(productPageRequest.getManufacturer()),
                    pageRequest);
        } else if (productPageRequest.getCategory() != null) {
            result = productRepository.findByCategory(
                    Category.of(productPageRequest.getCategory()),
                    pageRequest);
        } else result = productRepository.findAll(pageRequest);


        return result;
    }

    public Optional<Manufacturer> getManufacturerById(Long manufacturer) {
        return manufacturerRepository.findById(manufacturer);
    }

    public Optional<Category> getCategoryById(Long category) {
        return categoryRepository.findById(category);
    }
}
