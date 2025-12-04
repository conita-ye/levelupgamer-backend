package com.levelupgamer.boletas;

import com.levelupgamer.boletas.dto.CarritoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CarritoDto> obtenerCarrito(@PathVariable Long userId) {
        CarritoDto carrito = carritoService.getCartByUserId(userId);
        return ResponseEntity.ok(carrito);
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<CarritoDto> agregarProducto(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        CarritoDto carritoActualizado = carritoService.addProductToCart(userId, productId, quantity);
        return ResponseEntity.ok(carritoActualizado);
    }

    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<CarritoDto> quitarProducto(
            @PathVariable Long userId,
            @RequestParam Long productId) {
        CarritoDto carritoActualizado = carritoService.removeProductFromCart(userId, productId);
        return ResponseEntity.ok(carritoActualizado);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<CarritoDto> vaciarCarrito(@PathVariable Long userId) {
        CarritoDto carritoVacio = carritoService.clearCart(userId);
        return ResponseEntity.ok(carritoVacio);
    }
}
