package org.coupon.couponsunbbang.domain.couponmaster.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.coupon.couponsunbbang.domain.couponmaster.dto.request.CouponMasterCreateRequest;
import org.coupon.couponsunbbang.domain.couponmaster.dto.request.CouponMasterUpdateRequest;
import org.coupon.couponsunbbang.domain.couponmaster.dto.response.CouponMasterCreateResponse;
import org.coupon.couponsunbbang.domain.couponmaster.dto.response.CouponMasterDeleteResponse;
import org.coupon.couponsunbbang.domain.couponmaster.dto.response.CouponMasterListResponse;
import org.coupon.couponsunbbang.domain.couponmaster.dto.response.CouponMasterResponse;
import org.coupon.couponsunbbang.domain.couponmaster.service.CouponMasterService;
import org.coupon.couponsunbbang.global.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CouponMasterController {

    private final CouponMasterService couponMasterService;

    // 쿠폰마스터 등록: POST /api/v1/events/{eventId}/coupons
    @PostMapping("/events/{eventId}/coupons")
    public ResponseEntity<ApiResponse<CouponMasterCreateResponse>> createCouponMaster(
            @PathVariable Long eventId,
            @Valid @RequestBody CouponMasterCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(couponMasterService.createCouponMaster(request)));
    }

    // 쿠폰마스터 목록 조회: GET /api/v1/events/{eventId}/coupons
    @GetMapping("/events/{eventId}/coupons")
    public ResponseEntity<ApiResponse<CouponMasterListResponse>> getCouponMasters(
            @PathVariable Long eventId) {
        return ResponseEntity.ok(ApiResponse.success(couponMasterService.getCouponMasters(eventId)));
    }

    // 쿠폰마스터 단건 조회: GET /api/v1/coupons/{couponId}
    @GetMapping("/coupons/{couponId}")
    public ResponseEntity<ApiResponse<CouponMasterResponse>> getCouponMaster(
            @PathVariable Long couponId,
            @RequestParam Long eventId) {
        return ResponseEntity.ok(ApiResponse.success(couponMasterService.getCouponMaster(eventId, couponId)));
    }

    // 쿠폰마스터 수정: PUT /api/v1/coupons/{couponId}
    @PutMapping("/coupons/{couponId}")
    public ResponseEntity<ApiResponse<CouponMasterResponse>> updateCouponMaster(
            @PathVariable Long couponId,
            @RequestParam Long eventId,
            @Valid @RequestBody CouponMasterUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(couponMasterService.updateCouponMaster(eventId, couponId, request)));
    }

    // 쿠폰마스터 삭제: DELETE /api/v1/coupons/{couponId}
    @DeleteMapping("/coupons/{couponId}")
    public ResponseEntity<ApiResponse<CouponMasterDeleteResponse>> deleteCouponMaster(
            @PathVariable Long couponId,
            @RequestParam Long eventId) {
        return ResponseEntity.ok(ApiResponse.success(couponMasterService.deleteCouponMaster(eventId, couponId)));
    }
}