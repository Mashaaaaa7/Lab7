import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class WarehouseTransfer {

    private static final int MAX_WEIGHT = 150;
    private static void moveGoods(String worker, int[] weights, int start, int end, AtomicInteger totalWeight) {
        int currentWeight = 0;

        List<Integer> goods = new ArrayList<>(); //список для хранения результатов асинхронных задач, выполняемых в пуле потоков

        for (int i = start; i < end; i++) {
            if (currentWeight + weights[i] <= MAX_WEIGHT) {
                currentWeight += weights[i];
                goods.add(weights[i]);
                weights[i] = 0;
            }
            if (currentWeight == MAX_WEIGHT || i == end - 1) {
                System.out.println(worker + " перенёс товары весом: " + goods + " (Общий вес: " + currentWeight + " кг)");
                totalWeight.addAndGet(currentWeight);
                currentWeight = 0;
                goods.clear();
            }
        }
    }

    public static void main(String[] args) {
        int[] weights = {50, 20, 30, 70, 40, 60, 80, 10, 20, 90, 50, 40, 60, 30};
        List<CompletableFuture<Void>> tasks = new ArrayList<>();

        AtomicInteger totalWeight = new AtomicInteger(0);
        int numOfWorkers = 3;
        int partSize = (int) Math.ceil((double) weights.length / numOfWorkers);

        for (int i = 0; i < numOfWorkers; i++) {
            final int workerId = i + 1;
            final int start = i * partSize;
            final int end = Math.min(start + partSize, weights.length);
            //запуск
            tasks.add(CompletableFuture.runAsync(() -> moveGoods("Грузчик " + workerId, weights, start, end, totalWeight)));
        }
        //объединение задач •	join():
        //Блокирует выполнение программы, пока все задачи не будут завершены.
        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();
        System.out.println("Все товары перенесены на новый склад!");
    }
}