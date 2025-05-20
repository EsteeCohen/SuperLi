package DataAccessLayer.DTO;

import java.time.LocalDate;

public record SupplyDTO(
        int supplyId,
        int shelfQuantity,
        int storageQuantity,
        LocalDate expirationDate,
        double costPrice
) {
}
