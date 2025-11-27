package strukturdata;

public class CountingSortDesc {

    public void run() {
        this.main();
    }

    public void countingSort(int[] arr) {
        int n = arr.length;

        // Cari nilai minimum & maksimum di array
        // Tujuannya: biar tau range angka untuk bikin array count[]
        int max = arr[0];
        int min = arr[0];
        for (int i = 1; i < n; i++) {
            if (arr[i] > max) {
                max = arr[i];   // update max
            }
            if (arr[i] < min) {
                min = arr[i];   // update min
            }
        }

        // Hitung range nilai (digunakan sebagai ukuran array count)
        int range = max - min + 1;
        int[] count = new int[range];   // buat nampung frekuensi angka
        int[] output = new int[n];      // hasil sorting

        // Hitung frekuensi tiap elemen
        // Mapping: angka asli → index = angka - min
        for (int i = 0; i < n; i++) {
            count[arr[i] - min]++;
        }

        // Build output array dalam mode DESCENDING
        // Caranya: loop dari index count terbesar → terkecil
        int index = 0;
        for (int i = range - 1; i >= 0; i--) {
            while (count[i] > 0) {
                output[index] = i + min; // convert balik index ke nilai asli
                index++;
                count[i]--;
            }
        }

        // Copy hasil sorting balik ke array original
        for (int i = 0; i < n; i++) {
            arr[i] = output[i];
        }
    }

    public void printArray(int[] arr) {
        // Utility sederhana buat print array
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public void main() {

        // Sample data
        int[] data = {45, 23, 78, 12, 67, 34, 89, 56, 91, 28};

        System.out.println("=== COUNTING SORT (DESCENDING) ===");

        System.out.println("\nData sebelum sorting:");
        printArray(data);

        // Eksekusi counting sort
        countingSort(data);

        System.out.println("\nData setelah sorting (terbesar ke terkecil):");
        printArray(data);
    }
}
