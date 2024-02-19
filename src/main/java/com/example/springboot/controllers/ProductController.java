package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductsRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositorys.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }
    @GetMapping("/products/{id}")
    public ResponseEntity<Object> findOne(@PathVariable(value = "id") UUID id){
        Optional<ProductModel> product = productRepository.findById(id);

        if(product.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");

        return ResponseEntity.status(HttpStatus.OK).body(product.get());
    }
    @PostMapping("/products")
    public ResponseEntity<ProductModel> save(@RequestBody @Valid ProductsRecordDto productsRecordDto){
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productsRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    @PutMapping("/products/{id}")
    public  ResponseEntity<Object> update(@PathVariable(value = "id") UUID id, @RequestBody @Valid ProductsRecordDto values){
        Optional<ProductModel> product = productRepository.findById(id);

        if(product.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");

        var productFind = product.get();

        BeanUtils.copyProperties(values, productFind);

        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productFind));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteById (@PathVariable(value = "id") UUID id){
        Optional<ProductModel> product = productRepository.findById(id);

        if(product.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");

        product.get().setActive(0);

        return ResponseEntity.status(HttpStatus.OK).body("Product inactive!");
    }
}
