package DataAccessLayer.DTO;

import java.time.LocalDate;
import java.util.List;

public record InventoryProductDTO(String productName,
                                  String category,
                                  List<String> subCategories,
                                  String manufacturer,
                                  String shelfLocation,
                                  String storageLocation,
                                  int minQuantity,
                                  double sellPrice,
                                  LocalDate discountStart,
                                  LocalDate discountEnd,
                                  double discountPercentage)
{
}
