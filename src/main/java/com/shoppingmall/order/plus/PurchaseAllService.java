package com.shoppingmall.order.plus;

import com.shoppingmall.order.domain.PurchaseDelivery;
import com.shoppingmall.order.domain.PurchaseItem;
import com.shoppingmall.order.domain.PurchaseList;
import com.shoppingmall.order.dto.PurchaseAllDto;
import com.shoppingmall.order.dto.PurchaseItemDto;
import com.shoppingmall.order.repository.PurchaseDeliveryRepository;
import com.shoppingmall.order.repository.PurchaseItemRepository;
import com.shoppingmall.order.repository.PurchaseListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class PurchaseAllService {

	@Autowired
	PurchaseDeliveryRepository delRepo;
	@Autowired
	PurchaseListRepository purRepo;
	@Autowired
	PurchaseItemRepository itemRepo;

	public String order(PurchaseList purchase, PurchaseDelivery delivery, PurchaseItem item, String receiveDetailAddr) {

		purchase.setCreateAt(LocalDateTime.now());
		purRepo.save(purchase);
		delivery.setReceiverAddr(delivery.getReceiverAddr() + " " + receiveDetailAddr);
		delivery.setPurchaseId(purchase);
		delivery.setDeliveryStatus("배송준비중");
		delRepo.save(delivery);

		item.setPurchaseId(purchase);
		itemRepo.save(item);

		return "주문해주셔서 감사합니다";
	}
	
	public PurchaseAllDto getOrderDetails(Long purchaseId) {
		// Purchase 정보 가져오기
		List<PurchaseList> purchases = new ArrayList<>();
		List<PurchaseDelivery> deliveries = new ArrayList<>();
		List<PurchaseItem> items = new ArrayList<>();


//		if(purchaseId!=null){
		PurchaseList purchase = purRepo.findById(purchaseId)
				.orElseThrow(() -> new RuntimeException("Purchase not found for id: " + purchaseId));
		purchases = purRepo.findByPurchaseId(purchaseId);
		if (purchases.isEmpty()) {
			throw new RuntimeException("Purchases not found for id: " + purchaseId);
		}
		deliveries = delRepo.findByPurchaseId(purchaseId);  // 반환 타입은 List<PurchaseDelivery>
		if (deliveries.isEmpty()) {
			throw new RuntimeException("Delivery not found for id: " + purchaseId);
		}
		items = itemRepo.findByPurchaseId(purchaseId);
		if (items.isEmpty()) {
			throw new RuntimeException("Item not found for id: " + purchaseId);
		}
// 날짜 포맷팅
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDate = purchase.getCreateAt().format(formatter);


				return PurchaseAllDto.builder()
				.purchaseList(purchases)  // 전체 PurchaseList 리스트
				.purchaseDelivery(deliveries)  // 전체 PurchaseDelivery 리스트
				.purchaseItem(items)  // 전체 PurchaseItem 리스트
				.formattedCreateAt(formattedDate)  // 포맷팅된 날짜
				.build();

	}

//		public PurchaseItemDto orderDetail(Long productId){
//			Optional<PurchaseItem> orderProductOne = itemRepo.findById(productId);
//
//
//
//
//	}

}