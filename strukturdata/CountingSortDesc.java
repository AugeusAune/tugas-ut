package strukturdata;

public class CountingSortDesc {

    public void start() {
        this.main();
    }

    // Method untuk melakukan counting sort descending
    public void countingSort(int[] arr) {
        int n = arr.length;

        // Temukan nilai maksimum dan minimum
        int max = arr[0];
        int min = arr[0];
        for (int i = 1; i < n; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
            if (arr[i] < min) {
                min = arr[i];
            }
        }

        // Buat array count dengan range dari min ke max
        int range = max - min + 1;
        int[] count = new int[range];
        int[] output = new int[n];

        // Hitung frekuensi setiap elemen
        for (int i = 0; i < n; i++) {
            count[arr[i] - min]++;
        }

        // Output hasil secara descending (dari terbesar ke terkecil)
        int index = 0;
        for (int i = range - 1; i >= 0; i--) {
            while (count[i] > 0) {
                output[index] = i + min;
                index++;
                count[i]--;
            }
        }

        // Copy output array ke arr
        for (int i = 0; i < n; i++) {
            arr[i] = output[i];
        }
    }

    // Method untuk print array
    public void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public void main() {
        // Inisialisasi data (10 elemen)
        // Menggunakan bilangan positif untuk Counting Sort
        int[] data = {45, 23, 78, 12, 67, 34, 89, 56, 91, 28};

        System.out.println("=== COUNTING SORT (DESCENDING) ===");
        System.out.println("\nData sebelum sorting:");
        printArray(data);

        // Catat waktu mulai
        long startTime = System.nanoTime();

        // Lakukan sorting
        countingSort(data);

        // Catat waktu selesai
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        System.out.println("\nData setelah sorting (terbesar ke terkecil):");
        printArray(data);

        System.out.println("\nWaktu eksekusi: " + duration + " nanoseconds");
        System.out.println("Waktu eksekusi: " + (duration / 1000000.0) + " milliseconds");

        // Demonstrasi dengan data yang memiliki range lebih kecil
        System.out.println("\n=== DEMO: Data dengan Range Kecil ===");
        int[] data2 = {5, 2, 9, 1, 5, 6, 3, 8, 4, 7};
        System.out.println("Data sebelum sorting:");
        printArray(data2);

        countingSort(data2);

        System.out.println("Data setelah sorting:");
        printArray(data2);
    }
}

/*
ANALISA KINERJA COUNTING SORT:

1. KOMPLEKSITAS WAKTU:
   - Best Case: O(n + k)
   - Average Case: O(n + k)
   - Worst Case: O(n + k)

   Dimana:
   n = jumlah elemen
   k = range nilai (max - min + 1)

2. KOMPLEKSITAS RUANG:
   - O(n + k) - Membutuhkan array count dan output
   - Sangat bergantung pada range nilai data

3. KARAKTERISTIK:
   - STABLE: Mempertahankan urutan relatif (jika diimplementasikan dengan benar)
   - NOT COMPARISON-BASED: Tidak membandingkan elemen
   - NOT IN-PLACE: Membutuhkan array tambahan

4. KELEBIHAN:
   - Sangat cepat untuk data dengan range kecil (O(n))
   - Linear time complexity untuk kondisi ideal
   - Lebih cepat dari O(n log n) algorithms jika k ≈ n
   - Cocok untuk sorting integer dalam range terbatas
   - Simple dan mudah diimplementasikan

5. KEKURANGAN:
   - Tidak efisien jika range (k) sangat besar
     Contoh: data [1, 1000000] → butuh array size 1000000
   - Hanya untuk integer atau data yang bisa dimapping ke integer
   - Membutuhkan memory ekstra O(k)
   - Tidak cocok untuk bilangan negatif besar atau floating point

6. KAPAN MENGGUNAKAN:
   - Data integer dengan range kecil (k ≈ n)
   - Ketika kecepatan lebih prioritas dari memory
   - Data memiliki banyak nilai duplikat
   - Sorting nilai ASCII, ranking, atau counting frequencies

7. KAPAN TIDAK MENGGUNAKAN:
   - Range nilai sangat besar (k >> n)
   - Data berupa floating point atau string
   - Memory terbatas
   - Data tidak diketahui range-nya

8. PERBANDINGAN PRAKTIS:
   Untuk n=10, k=100: ~20,000-40,000 nanoseconds (SANGAT CEPAT)
   Untuk n=1000, k=1000: ~0.5-1 milliseconds (LEBIH CEPAT dari Merge Sort)
   Untuk n=1000, k=1000000: ~5-10 milliseconds (LAMBAT karena k besar)

9. CONTOH KASUS NYATA:
   - Mengurutkan nilai ujian (0-100): IDEAL
   - Mengurutkan umur (0-150): IDEAL
   - Mengurutkan ID karyawan (1-9999999): TIDAK IDEAL
   - Mengurutkan harga (bisa decimal): TIDAK COCOK

KESIMPULAN:
Counting Sort adalah algoritma tercepat untuk integer sorting dengan
range kecil, tetapi Merge Sort lebih versatile dan konsisten untuk
berbagai jenis dan ukuran data.
 */
