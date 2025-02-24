package service.processor;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import dto.ItemDTO;
import service.item.ItemService;

public class ItemProcessor {

    private static final ExecutorService executor = Executors.newFixedThreadPool(10); // Thread pool for parallel execution

    public static CompletableFuture<String> getItemNameAsync(String vendorId, String itemId, int quantity) {
        return CompletableFuture.supplyAsync(() -> {
            ItemDTO item = ItemService.readItem(vendorId, itemId);
            if (item != null) {
                return item.getName() + " x" + quantity;
            } else {
                return "Unknown Item (" + itemId + ") x" + quantity;
            }
        }, executor);
    }

    public static CompletableFuture<String> processItemListAsync(String vendorId, HashMap<String, Integer> items) {
        List<CompletableFuture<String>> futures = items.entrySet().stream()
            .map(entry -> getItemNameAsync(vendorId, entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.joining("\n")));
    }
}

