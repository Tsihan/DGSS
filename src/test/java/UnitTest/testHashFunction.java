package UnitTest;

import GraphStreamSketch.HashFunction;

import java.nio.charset.StandardCharsets;

public class testHashFunction {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!\n");

        HashFunction.Hsieh("vdfsvbfhsgdbfdsgsd".getBytes(StandardCharsets.UTF_8), 4);
        HashFunction.CalculateHashValue("qweY".getBytes(StandardCharsets.UTF_8), 4, HashFunction.hashfunctions.Hsieh);


    }
}
