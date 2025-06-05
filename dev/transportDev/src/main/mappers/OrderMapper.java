package transportDev.src.main.mappers;

import transportDev.src.main.dtos.OrderDTO;
import transportDev.src.main.dtos.ItemDTO;
import transportDev.src.main.entities.Order;
import transportDev.src.main.entities.Item;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.OrderStatus;
import transportDev.src.main.enums.OrderType;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }

        List<ItemDTO> itemDTOs = new ArrayList<>();
        for (Item item : order.getItems()) {
            itemDTOs.add(ItemMapper.toDTO(item));
        }

        return new OrderDTO(
                order.getId(),
                order.getDate(),
                order.getStatus().toString(),
                SiteMapper.toDTO(order.getSite()), // source site
                SiteMapper.toDTO(order.getDestinationSite()), // destination site
                itemDTOs,
                order.OrderWeight()
        );
    }

    public static Order fromDTO(OrderDTO orderDTO, Site sourceSite, List<Item> items) {
        if (orderDTO == null) {
            return null;
        }

        Order order = new Order(
                orderDTO.getId(),
                orderDTO.getOrderDate(),
                sourceSite,
                items
        );

        // Set status if provided
        if (orderDTO.getStatus() != null) {
            try {
                OrderStatus status = OrderStatus.valueOf(orderDTO.getStatus().toUpperCase());
                order.setStatus(status);
            } catch (IllegalArgumentException e) {
                // If status is invalid, keep default CREATED status
            }
        }

        return order;
    }

    public static Order fromDTO(OrderDTO orderDTO, Site sourceSite, List<Item> items, OrderType orderType) {
        if (orderDTO == null) {
            return null;
        }

        Order order = new Order(
                orderDTO.getOrderDate(),
                sourceSite,
                items,
                orderType
        );

        // Set status if provided
        if (orderDTO.getStatus() != null) {
            try {
                OrderStatus status = OrderStatus.valueOf(orderDTO.getStatus().toUpperCase());
                order.setStatus(status);
            } catch (IllegalArgumentException e) {
                // If status is invalid, keep default CREATED status
            }
        }

        return order;
    }
} 