package strukturdata;

import java.util.LinkedList;

public class StrukturData {

    public void tugas1() {
        System.out.println("stuktur baris tipe data integer");
        int StrukturBaris;
        StrukturBaris = 20;

        System.out.println(StrukturBaris);

        System.out.println("kata baru tipe data string");

        String KataBaru = "Deklarasi tipe data string";

        System.out.println(KataBaru);

        System.out.println("empatAngka tipe data array");

        int[] empatAngka = {7, 10, 20, 23};

        for (int item : empatAngka) {
            System.out.println(item);
        }

        System.out.println("angka tipe data array 2 dimensi");

        String[][] angka = {
            {"1", "3", "5"},
            {"14", "19", "20"},
            {"22", "27", "29"}
        };

        for (String[] item : angka) {
            for (String item2 : item) {
                System.out.println(item2);
            }
        }

        System.out.println("linkedlis");

        LinkedList<Integer> linkedList = new LinkedList<>();

        linkedList.add(22);
        linkedList.add(19);
        linkedList.add(44);
        linkedList.add(60);
        linkedList.add(72);

        linkedList.forEach(i -> System.out.println(i));
    }

    public void tugas2() {
        MergeSortDesc mergeSort = new MergeSortDesc();
        mergeSort.run();

        CountingSortDesc countingSort = new CountingSortDesc();
        countingSort.run();
    }
}
