package strukturdata;

public class MergeSortDesc {

    public void run() {
        this.main();
    }

    public void mergeSort(int[] arr, int left, int right) {

        // Base case: kalau left == right → elemen cuma 1 → otomatis udah sorted
        if (left < right) {

            // Cari middle biar array bisa dibagi dua
            int mid = left + (right - left) / 2;

            // Rekursif: sorting bagian kiri
            mergeSort(arr, left, mid);

            // Rekursif: sorting bagian kanan
            mergeSort(arr, mid + 1, right);

            // Merge dua bagian yg sudah terurut
            merge(arr, left, mid, right);
        }
    }

    public void merge(int[] arr, int left, int mid, int right) {

        // Hitung size subarray kiri & kanan
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Subarray temp (copy dulu sebelum digabung)
        int[] L = new int[n1];
        int[] R = new int[n2];

        // Copy elemen ke array sementara
        for (int i = 0; i < n1; i++) {
            L[i] = arr[left + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = arr[mid + 1 + j];
        }

        // Pointer:
        // i → subarray kiri (L)
        // j → subarray kanan (R)
        // k → posisi tulis di array asli
        int i = 0, j = 0;
        int k = left;

        // Merge bagian kiri & kanan
        // Karena descending → ambil angka terbesar dulu
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

        // Kalau elemen kiri masih tersisa → masukin semua
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        // Kalau elemen kanan masih tersisa → masukin semua
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    public void printArray(int[] arr) {
        // Utility print array
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public void main() {

        // Sample data
        int[] data = {45, 23, 78, 12, 67, 34, 89, 56, 91, 28};

        System.out.println("=== MERGE SORT (DESCENDING) ===");

        System.out.println("\nData sebelum sorting:");
        printArray(data);

        // Eksekusi merge sort
        mergeSort(data, 0, data.length - 1);

        System.out.println("\nData setelah sorting (terbesar ke terkecil):");
        printArray(data);
    }
}
