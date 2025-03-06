package com.shoppingmall.order.controller;

import com.shoppingmall.order.domain.Purchase;
import com.shoppingmall.order.domain.PurchaseProduct;
import com.shoppingmall.order.dto.PurchaseDto;
import com.shoppingmall.order.dto.PurchaseProductDto;
import com.shoppingmall.order.repository.PurchaseProductRepository;
import com.shoppingmall.order.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;

@RequestMapping("/order/rest")
@RestController
public class PurchaseRestController {

  @PostMapping("/review")
  public ResponseEntity<?> uploadReview(
          @RequestParam("reviewText") String reviewText,
          @RequestParam("reviewImages") List<MultipartFile> reviewImages) {

    // 파일 저장 로직 (예제)
    for (MultipartFile file : reviewImages) {
      System.out.println("파일 이름: " + file.getOriginalFilename());
    }

    System.out.println("리뷰 내용: " + reviewText);

    return ResponseEntity.ok("리뷰 업로드 성공"); // 문자열 반환 (타입이 String)
  }

  @Autowired
  PurchaseProductRepository productRepo;

  @Autowired
  PurchaseRepository purchRepo;

    @PostMapping("/order")
    public ResponseEntity<?> order(@RequestBody PurchaseDto purchaseDto) {
      // 받은 purchaseDto를 확인
      System.out.println(purchaseDto);

      // Purchase 객체 생성 및 설정
      Purchase purchase = new Purchase();
      purchase.setUserId(purchaseDto.getUserId());
      purchase.setCreateAt(now());

      // 상품 목록 처리
      List<PurchaseProduct> purchaseProducts = new ArrayList<>();
      for (PurchaseProductDto productDto : purchaseDto.getProducts()) {
        PurchaseProduct purchaseProduct = new PurchaseProduct();
        purchaseProduct.setProductId(productDto.getProductId());
        purchaseProduct.setProductName(productDto.getProductName());
        purchaseProduct.setQuantity(productDto.getQuantity());
        purchaseProduct.setPurchase(purchase);

        purchaseProducts.add(purchaseProduct);
        productRepo.save(purchaseProduct); // 상품 저장
      }

      // 주문 정보 저장
      purchase.setPurchaseProduct(purchaseProducts);
      purchRepo.save(purchase);

      return ResponseEntity.ok("주문 성공"); // 주문 완료 응답
    }
}