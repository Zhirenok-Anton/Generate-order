package ru.sportmasterlab.Generate_order.model.order.created;

import java.util.ArrayList;

public record OrderRequest(
        String shopNum,
        Money money,
        ArrayList<CuponsList> cupons,
        ArrayList<ItemList> itemList,
        String entryPoint
 ) {
}