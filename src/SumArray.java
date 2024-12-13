import java.util.concurrent.*;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class SumArray {

    public static int calculateSum(int[] array) {
        int sum = 0;
        for (int num : array) {
            sum += num;
        }
        return sum;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int[] array = {5, 10, 15, 20, 25, 30, 200, 400, 250};
        int numOfThreads = 3;
        int partSize = (int) Math.ceil((double) array.length / numOfThreads); //ceil- округляет до целого числа
        ExecutorService executor = newFixedThreadPool(numOfThreads);
//список потоков
        Future<Integer>[] futures = new Future[numOfThreads];
        for (int i = 0; i < numOfThreads; i++) {
            int start = i * partSize;
            int end = Math.min(start + partSize, array.length);
            int[] subArray = new int[end - start];
//копирование элементов из оригинального массива в новый подмассив
            System.arraycopy(array, start, subArray, 0, end - start);
            futures[i] = executor.submit(() -> calculateSum(subArray));
        }
        int totalSum = 0;
        for (Future<Integer> future : futures) {
            totalSum += future.get();
        }
        executor.shutdown();
        System.out.println("Общая сумма элементов массива: " + totalSum);
    }
}