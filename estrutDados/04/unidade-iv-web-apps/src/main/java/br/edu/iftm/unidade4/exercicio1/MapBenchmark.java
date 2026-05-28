package br.edu.iftm.unidade4.exercicio1;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Exercício 1: Análise de Benchmark JMH
 * Compara o desempenho de HashMap, TreeMap e LinkedHashMap
 * para operações de get, put e range queries.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(2)
public class MapBenchmark {

    private static final int TAMANHO = 100_000;
    private HashMap<String, Integer> hashMap;
    private TreeMap<String, Integer> treeMap;
    private LinkedHashMap<String, Integer> linkedHashMap;
    private ArrayList<String> arrayList;
    private LinkedList<String> linkedList;
    private List<String> chaves;

    @Setup(Level.Trial)
    public void setup() {
        hashMap = new HashMap<>(TAMANHO);
        treeMap = new TreeMap<>();
        linkedHashMap = new LinkedHashMap<>(TAMANHO);
        chaves = new ArrayList<>(TAMANHO);
        arrayList = new ArrayList<>(TAMANHO);
        linkedList = new LinkedList<>();

        Random rand = new Random(42);
        for (int i = 0; i < TAMANHO; i++) {
            String chave = "produto-" + rand.nextInt(TAMANHO * 10);
            chaves.add(chave);
            hashMap.put(chave, i);
            treeMap.put(chave, i);
            linkedHashMap.put(chave, i);
            arrayList.add(chave);
            linkedList.add(chave);
        }
    }

    @Benchmark
    public Integer hashMapGet(Blackhole bh) {
        String chave = chaves.get(new Random().nextInt(chaves.size()));
        Integer val = hashMap.get(chave);
        bh.consume(val);
        return val;
    }

    @Benchmark
    public Integer treeMapGet(Blackhole bh) {
        String chave = chaves.get(new Random().nextInt(chaves.size()));
        Integer val = treeMap.get(chave);
        bh.consume(val);
        return val;
    }

    @Benchmark
    public Integer linkedHashMapGet(Blackhole bh) {
        String chave = chaves.get(new Random().nextInt(chaves.size()));
        Integer val = linkedHashMap.get(chave);
        bh.consume(val);
        return val;
    }

    @Benchmark
    public NavigableMap<String, Integer> treeMapRange(Blackhole bh) {
        NavigableMap<String, Integer> sub =
                treeMap.subMap("produto-1000", true, "produto-2000", true);
        bh.consume(sub);
        return sub;
    }

    @Benchmark
    public String arrayListGet(Blackhole bh) {
        String val = arrayList.get(5000); // acesso ao meio
        bh.consume(val);
        return val;
    }

    @Benchmark
    public String linkedListGet(Blackhole bh) {
        String val = linkedList.get(5000); // acesso ao meio - O(n)
        bh.consume(val);
        return val;
    }
}
