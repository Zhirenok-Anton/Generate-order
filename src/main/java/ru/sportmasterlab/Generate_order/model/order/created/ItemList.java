package ru.sportmasterlab.Generate_order.model.order.created;

import java.util.ArrayList;

public record ItemList(
        String idWare,
        String qtyOrdered,
        String price,
        ArrayList<DiscountList> discountList
) {

}
