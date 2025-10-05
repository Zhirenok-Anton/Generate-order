package ru.sportmasterlab.Generate_order.repository;

import org.springframework.stereotype.Repository;
import ru.sportmasterlab.Generate_order.model.order.OrderResponseDto;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    ArrayList<OrderResponseDto> orderResponseDtoList;

    public OrderRepositoryImpl(){
        orderResponseDtoList = new ArrayList<>();
    }

    @Override
    public Optional<OrderResponseDto> getOrderByCode(Long orderCode) {

        for (OrderResponseDto responseDto : orderResponseDtoList) {
            if (responseDto.orderCode() == orderCode) {
                return Optional.of(responseDto);
            }
        }
        return null;
    }

    @Override
    public void insertOrder(Long orderCode, String orderNum, int authCode, String order) {
        orderResponseDtoList.add(new OrderResponseDto(orderCode,orderNum,authCode));
    }
}
