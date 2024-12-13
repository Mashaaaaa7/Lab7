import java.util.concurrent.*;

public class TheBiggestElement {

    private static int findMaxElement(int[] row) {
        int max = row[0];
        for (int value : row) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        int[][] arr = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {20, 510, 932, 392},
                {100, 200, 150, 35}
        };
        int numOfRows = arr.length;

        ExecutorService executor = Executors.newFixedThreadPool(numOfRows);

        Future<Integer>[] futures = new Future[numOfRows];
        for (int i = 0; i < numOfRows; i++) {
            final int row = i; //финальная копия счётчика
            //запуск задачи
            futures[i] = executor.submit(() -> findMaxElement(arr[row]));
        }
        int ArrayMax = Integer.MIN_VALUE; //Инициализируется переменная ArrayMax наименьшим возможным значением для типа int.
        for (Future<Integer> future : futures) {
            int rowMax = future.get(); //блокирует выполнение, пока поток не завершит работу
            System.out.println("Максимум строки: " + rowMax);
            if (rowMax > ArrayMax) {
                ArrayMax = rowMax;
            }
        }
        executor.shutdown();

        System.out.println("Наибольший элемент в матрице: " + ArrayMax);
    }
}