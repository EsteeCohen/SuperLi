package transportDev.src.main.mappers;

import transportDev.src.main.dtos.ItemDTO;
import transportDev.src.main.entities.Item;

public class ItemMapper {

    public static ItemDTO toDTO(Item item) {
        if (item == null) {
            return null;
        }

        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getWeight(),
                item.getQuantity(),
                "" // description - Item entity doesn't have description field
        );
    }

    public static Item fromDTO(ItemDTO itemDTO) {
        if (itemDTO == null) {
            return null;
        }

        return new Item(
                itemDTO.getId(),
                itemDTO.getName(),
                itemDTO.getQuantity(),
                itemDTO.getWeight()
        );
    }
} 