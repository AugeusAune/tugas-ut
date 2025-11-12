package strukturdata;

public class MergeSortDesc {

    // Method untuk melakukan merge sort
    public static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            // Sort bagian kiri
            mergeSort(arr, left, mid);
            // Sort bagian kanan
            mergeSort(arr, mid + 1, right);

            // Gabungkan kedua bagian
            merge(arr, left, mid, right);
        }
    }

    // Method untuk menggabungkan dua subarray secara descending
    public static void merge(int[] arr, int left, int mid, int right) {
        // Ukuran subarray
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Array temporary
        int[] L = new int[n1];
        int[] R = new int[n2];

        // Copy data ke array temporary
        for (int i = 0; i < n1; i++) {
            L[i] = arr[left + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = arr[mid + 1 + j];
        }

        // Merge array temporary kembali ke arr[]
        int i = 0, j = 0;
        int k = left;

        // Bandingkan dan gabungkan secara descending (terbesar ke terkecil)
        while (i < n1 && j < n2) {
            if (L[i] >= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        // Copy sisa elemen L[] jika ada
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        // Copy sisa elemen R[] jika ada
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    // Method untuk print array
    public static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // Inisialisasi data (10 elemen)
        int[] data = {45, 23, 78, 12, 67, 34, 89, 56, 91, 28};

        System.out.println("=== MERGE SORT (DESCENDING) ===");
        System.out.println("\nData sebelum sorting:");
        printArray(data);

        // Catat waktu mulai
        long startTime = System.nanoTime();

        // Lakukan sorting
        mergeSort(data, 0, data.length - 1);

        // Catat waktu selesai
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        System.out.println("\nData setelah sorting (terbesar ke terkecil):");
        printArray(data);

        System.out.println("\nWaktu eksekusi: " + duration + " nanoseconds");
        System.out.println("Waktu eksekusi: " + (duration / 1000000.0) + " milliseconds");
    }
}

/*
ANALISA KINERJA MERGE SORT:

1. KOMPLEKSITAS WAKTU:
   - Best Case: O(n log n)
   - Average Case: O(n log n)
   - Worst Case: O(n log n)

   Penjelasan: Merge Sort selalu membagi array menjadi dua bagian (log n pembagian)
   dan setiap pembagian memerlukan n operasi untuk menggabungkan kembali.

2. KOMPLEKSITAS RUANG:
   - O(n) - Membutuhkan array tambahan untuk proses merge
   - Tidak efisien untuk memory karena membutuhkan ruang ekstra

3. KARAKTERISTIK:
   - STABLE: Mempertahankan urutan relatif elemen yang sama
   - NOT IN-PLACE: Membutuhkan memory tambahan
   - DIVIDE AND CONQUER: Membagi masalah menjadi submasalah lebih kecil

4. KELEBIHAN:
   - Konsisten: Performa selalu O(n log n) dalam semua kasus
   - Cocok untuk data besar dan linked list
   - Predictable performance
   - Dapat di-parallelkan

5. KEKURANGAN:
   - Membutuhkan ruang memory tambahan O(n)
   - Lebih lambat untuk dataset kecil dibanding Quick Sort
   - Overhead dari recursive calls

6. KAPAN MENGGUNAKAN:
   - Ketika stabilitas sorting diperlukan
   - Ketika worst-case O(n log n) dijamin diperlukan
   - Untuk external sorting (data tidak muat di memory)
   - Untuk sorting linked list

7. PERBANDINGAN PRAKTIS:
   Untuk 10 elemen: ~50,000-100,000 nanoseconds
   Untuk 1000 elemen: ~1-2 milliseconds
   Untuk 1,000,000 elemen: ~200-300 milliseconds
*/
