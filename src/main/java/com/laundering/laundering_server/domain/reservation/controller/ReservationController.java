//package com.laundering.laundering_server.domain.reservation.controller;
//
//import com.laundering.laundering_server.domain.facade.ReservationFacade;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/reservation")
//public class ReservationController {
//    private final ReservationFacade reservationFacade;
//    @Operation(summary = "예약")
//    @PostMapping
//    public ResponseEntity<Void> Reservaiton()
//    {
//        return ResponseEntity.ok(reservationFacade.reservation(userId()));
//    }
//}
