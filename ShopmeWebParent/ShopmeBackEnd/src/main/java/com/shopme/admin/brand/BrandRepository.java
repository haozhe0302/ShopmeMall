package com.shopme.admin.brand;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Brand;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer>{

    public Long countById(Integer id);

    public Brand findByName(String name);
}