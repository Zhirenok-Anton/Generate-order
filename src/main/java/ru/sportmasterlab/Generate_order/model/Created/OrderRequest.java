package ru.sportmasterlab.Generate_order.model.Created;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;


public record OrderRequest(
        @NotNull
        String orderCode,
        String shopNum,
        Money money,
        ArrayList<ItemList> itemList,
        String entryPoint
 ) {
}
