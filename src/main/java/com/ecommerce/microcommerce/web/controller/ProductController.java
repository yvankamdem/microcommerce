package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.dao.ProductDao;
import com.ecommerce.microcommerce.web.exceptions.ProductIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Objects;

@Api("API pour les opérations CRUD sur les produits.")

@RestController
public class ProductController {
    @Autowired
    private ProductDao productDao;

   /* public ProductController(ProductDao productDao){

        this.productDao = productDao;
    }*/

    //@GetMapping("/products")
    @RequestMapping(value = "/products", method = RequestMethod.GET)

    public MappingJacksonValue listeProducts() {
        Iterable<Product> products = productDao.findAll();
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue produitsFiltres = new MappingJacksonValue(products);
        produitsFiltres.setFilters(listDeNosFiltres);
        return produitsFiltres;
    }

    //Récupérer un produit par son id
    @ApiOperation(value = "Récupère un produit grâce à son ID à condition que celui-ci soit en stock!")

    @GetMapping("/products/{id}")
    public Product afficherUnProduit(@PathVariable int id){
       Product product = productDao.findById(id);
       if (product==null) throw new ProductIntrouvableException("Le produit avec l'id" + id + " est INTROUVABLE. écran Bleu si je pouvais.");
       return product;
    }

    @GetMapping(value = "test/products/{prixLimit}")
    public List<Product> testeDeRequetes(@PathVariable int prixLimit)
    {
        return productDao.findByPrixGreaterThan(400);
    }

    @PostMapping(value = "/products")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product){
        Product productAdded = productDao.save(product);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping (value = "/products/{id}")
    public void supprimerProduit(@PathVariable int id) {
        productDao.deleteById(id);
    }

    @PutMapping (value = "/products")
    public void updateProduit(@RequestBody Product product)
    {
        productDao.save(product);
    }
}
