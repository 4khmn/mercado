package com.example.market.service;

import com.example.market.dto.create.CartItemCreateDto;
import com.example.market.dto.response.CartItemResponseDto;
import com.example.market.dto.response.CartResponseDto;
import com.example.market.dto.response.ProductResponseDto;
import com.example.market.dto.update.UpdateCartItemQuantity;
import com.example.market.enums.QuantityDirection;
import com.example.market.exception.IllegalQuantityException;
import com.example.market.exception.NotFoundException;
import com.example.market.mapper.CartMapper;
import com.example.market.model.CartItem;
import com.example.market.model.Product;
import com.example.market.model.Shop;
import com.example.market.model.User;
import com.example.market.repository.CartItemRepository;
import com.example.market.repository.ProductRepository;
import com.example.market.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartService cartService;

    /// GET CART BY USER ///
    @Test
    void getCartByUser_returnCartItemsResponseDto(){
        User user = new User();

        CartItem item1 = new CartItem();
        CartItem item2 = new CartItem();

        Product product1 = new Product();
        Product product2 = new Product();

        Shop shop1 = new Shop();
        Shop shop2 = new Shop();
        shop1.setId(1);
        shop2.setId(2);
        product1.setShop(shop1);
        product1.setPrice(new BigDecimal(1));
        product2.setShop(shop2);
        product2.setPrice(new BigDecimal(2));
        item1.setProduct(product1);
        item2.setProduct(product2);

        user.setCart(List.of(item1, item2));

        CartItemResponseDto dto1 = new CartItemResponseDto();
        dto1.setQuantity(1);
        CartItemResponseDto dto2 = new CartItemResponseDto();
        dto2.setQuantity(1);
        ProductResponseDto productResponseDto1 = new ProductResponseDto();
        productResponseDto1.setPrice(new BigDecimal(1));
        ProductResponseDto productResponseDto2 = new ProductResponseDto();
        productResponseDto2.setPrice(new BigDecimal(2));

        dto1.setProduct(productResponseDto1);
        dto2.setProduct(productResponseDto2);
        given(userRepository.findById(1L))
            .willReturn(Optional.of(user));

        given(cartMapper.toDto(item1)).willReturn(dto1);
        given(cartMapper.toDto(item2)).willReturn(dto2);

        CartResponseDto result = cartService.getCartByUser(1L);

        assertEquals(2, result.getCart().size());
        assertEquals(new BigDecimal(3), result.getTotalPrice());
        assertTrue(result.getCart().contains(dto1));
        assertTrue(result.getCart().contains(dto2));

        verify(userRepository).findById(1L);
        verify(cartMapper, times(2)).toDto(any());
    }

    @Test
    void getCartByUser_returnNotFoundException(){

        given(userRepository.findById(1L))
                .willReturn(Optional.empty());

        assertThrows(NotFoundException.class,
               () -> cartService.getCartByUser(1L));

        verify(userRepository).findById(1L);
        verify(cartMapper, never()).toDto(any());
    }
    //////////////////////////

    /// ADD CART ITEM ///
    @Test
    void addCartItem_existingCartItemAndEnoughStock_increaseQuantity(){

        Long userId = 1L;
        Long shopId = 10L;
        Long productId = 1L;

        User authUser = new User();
        authUser.setId(userId);
        User user = new User();
        user.setId(userId);

        Shop shop = new Shop();
        shop.setId(shopId);

        Product product = new Product();
        product.setId(productId);
        product.setShop(shop);
        product.setStock(10);

        CartItemCreateDto cartItemCreateDto = new CartItemCreateDto();
        cartItemCreateDto.setProductId(productId);

        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(3);

        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();

        ProductResponseDto productDto = new ProductResponseDto();
        productDto.setStock(10);
        cartItemResponseDto.setProduct(productDto);

        given(userRepository.findById(authUser.getId()))
            .willReturn(Optional.of(user));

        given(productRepository.findById(cartItemCreateDto.getProductId()))
            .willReturn(Optional.of(product));

        given(cartItemRepository.getCartItemByProductIdAndUserId(cartItemCreateDto.getProductId(), user.getId()))
            .willReturn(Optional.of(cartItem));

        given(cartMapper.toDto(cartItem))
                .willReturn(cartItemResponseDto);

        CartItemResponseDto result = cartService.addCartItem(cartItemCreateDto, authUser);

        assertNotNull(result);
        assertEquals(shopId, result.getProduct().getShopId());
        assertEquals(4, cartItem.getQuantity());
        assertEquals(10, result.getProduct().getStock());
        assertEquals(result,  cartItemResponseDto);

        verify(cartMapper).toDto(cartItem);
    }

    @Test
    void addCartItem_newCartItemAndInStock_createCartItem(){
        Long userId = 1L;
        Long shopId = 10L;
        Long productId = 1L;

        User authUser = new User();
        authUser.setId(userId);
        User user = new User();
        user.setId(userId);

        Shop shop = new Shop();
        shop.setId(shopId);

        Product product = new Product();
        product.setId(productId);
        product.setShop(shop);
        product.setStock(10);

        CartItemCreateDto cartItemCreateDto = new CartItemCreateDto();
        cartItemCreateDto.setProductId(productId);


        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();

        ProductResponseDto productDto = new ProductResponseDto();
        productDto.setStock(10);
        cartItemResponseDto.setProduct(productDto);

        given(userRepository.findById(authUser.getId()))
                .willReturn(Optional.of(user));

        given(productRepository.findById(cartItemCreateDto.getProductId()))
                .willReturn(Optional.of(product));

        given(cartItemRepository.getCartItemByProductIdAndUserId(cartItemCreateDto.getProductId(), user.getId()))
                .willReturn(Optional.empty());

        given(cartMapper.toDto(any(CartItem.class)))
                .willReturn(cartItemResponseDto);


        CartItemResponseDto result = cartService.addCartItem(cartItemCreateDto, authUser);

        assertNotNull(result);
        assertEquals(result, cartItemResponseDto);
        assertEquals(shopId, result.getProduct().getShopId());
        assertEquals(10, result.getProduct().getStock());

        ArgumentCaptor<CartItem> cartItemArgumentCaptor = ArgumentCaptor.forClass(CartItem.class);
        verify(cartItemRepository).save(cartItemArgumentCaptor.capture());
        CartItem savedItem = cartItemArgumentCaptor.getValue();

        assertEquals(1, savedItem.getQuantity());
        assertEquals(user, savedItem.getUser());
        assertEquals(product, savedItem.getProduct());

        verify(cartMapper).toDto(savedItem);
    }

    @Test
    void addCartItem_existingCartItemAndNotEnoughStock_throwIllegalQuantityException(){
        Long userId = 1L;
        Long shopId = 10L;
        Long productId = 1L;

        User authUser = new User();
        authUser.setId(userId);
        User user = new User();
        user.setId(userId);

        Shop shop = new Shop();
        shop.setId(shopId);

        Product product = new Product();
        product.setId(productId);
        product.setShop(shop);
        product.setStock(0);

        CartItemCreateDto cartItemCreateDto = new CartItemCreateDto();
        cartItemCreateDto.setProductId(productId);

        given(userRepository.findById(authUser.getId()))
                .willReturn(Optional.of(user));

        given(productRepository.findById(cartItemCreateDto.getProductId()))
                .willReturn(Optional.of(product));

        given(cartItemRepository.getCartItemByProductIdAndUserId(cartItemCreateDto.getProductId(), user.getId()))
                .willReturn(Optional.empty());

        assertThrows(IllegalQuantityException.class,
                () -> cartService.addCartItem(cartItemCreateDto, authUser));

        verify(cartItemRepository, never()).save(any());
        verify(cartMapper, never()).toDto(any());
    }

    @Test
    void addCartItem_newCartItemAndOutOfStock_throwIllegalQuantityException(){
        Long userId = 1L;
        Long shopId = 10L;
        Long productId = 1L;

        User authUser = new User();
        authUser.setId(userId);
        User user = new User();
        user.setId(userId);

        Shop shop = new Shop();
        shop.setId(shopId);

        Product product = new Product();
        product.setId(productId);
        product.setShop(shop);
        product.setStock(2);

        CartItemCreateDto cartItemCreateDto = new CartItemCreateDto();
        cartItemCreateDto.setProductId(productId);

        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        given(userRepository.findById(authUser.getId()))
                .willReturn(Optional.of(user));

        given(productRepository.findById(cartItemCreateDto.getProductId()))
                .willReturn(Optional.of(product));

        given(cartItemRepository.getCartItemByProductIdAndUserId(cartItemCreateDto.getProductId(), user.getId()))
                .willReturn(Optional.of(cartItem));


        assertThrows(IllegalQuantityException.class,
                () -> cartService.addCartItem(cartItemCreateDto, authUser));

        verify(cartItemRepository, never()).save(any());
        verify(cartMapper, never()).toDto(any());
    }
    //////////////////////

    /// UPDATE CART ITEM QUANTITY ///
    @Test
    void updateQuantity_currentQuantity2_down(){
        Long userId = 1L;
        Long shopId = 10L;
        Long productId = 1L;

        User authUser = new User();
        authUser.setId(userId);
        User user = new User();
        user.setId(userId);

        Shop shop = new Shop();
        shop.setId(shopId);

        Product product = new Product();
        product.setId(productId);
        product.setShop(shop);
        product.setStock(10);

        CartItem cartItem = new CartItem();
        cartItem.setId(1);
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        user.setCart(List.of(cartItem));

        UpdateCartItemQuantity updateCartItemQuantity = new UpdateCartItemQuantity();
        updateCartItemQuantity.setCartItemId(1L);
        updateCartItemQuantity.setDirection(QuantityDirection.DOWN);



        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();

        ProductResponseDto productDto = new ProductResponseDto();
        productDto.setStock(10);
        cartItemResponseDto.setProduct(productDto);
        cartItemResponseDto.setQuantity(1);

        given(userRepository.findById(authUser.getId()))
                .willReturn(Optional.of(user));



        given(cartMapper.toDto(cartItem))
                .willReturn(cartItemResponseDto);


        CartItemResponseDto result = cartService.updateQuantity(authUser, updateCartItemQuantity);

        assertNotNull(result);
        assertEquals(cartItemResponseDto, result);
        assertEquals(1, cartItem.getQuantity());
        assertEquals(1, result.getQuantity());
        assertEquals(10, result.getProduct().getStock());

        verify(cartMapper).toDto(cartItem);
        verify(cartItemRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateQuantity_currentQuantity1_down(){
        Long userId = 1L;
        Long shopId = 10L;
        Long productId = 1L;

        User authUser = new User();
        authUser.setId(userId);
        User user = new User();
        user.setId(userId);

        Shop shop = new Shop();
        shop.setId(shopId);

        Product product = new Product();
        product.setId(productId);
        product.setShop(shop);
        product.setStock(10);

        CartItem cartItem = new CartItem();
        cartItem.setId(1);
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        user.setCart(List.of(cartItem));

        UpdateCartItemQuantity updateCartItemQuantity = new UpdateCartItemQuantity();
        updateCartItemQuantity.setCartItemId(1L);
        updateCartItemQuantity.setDirection(QuantityDirection.DOWN);



        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();

        ProductResponseDto productDto = new ProductResponseDto();
        productDto.setStock(10);
        cartItemResponseDto.setProduct(productDto);
        cartItemResponseDto.setQuantity(0);

        given(userRepository.findById(authUser.getId()))
                .willReturn(Optional.of(user));



        given(cartMapper.toDto(cartItem))
                .willReturn(cartItemResponseDto);


        CartItemResponseDto result = cartService.updateQuantity(authUser, updateCartItemQuantity);

        assertNotNull(result);
        assertEquals(cartItemResponseDto, result);
        assertEquals(0, cartItem.getQuantity());
        assertEquals(0, result.getQuantity());
        assertEquals(10, result.getProduct().getStock());

        verify(cartMapper).toDto(cartItem);
        verify(cartItemRepository).deleteById(1L);
    }
}
