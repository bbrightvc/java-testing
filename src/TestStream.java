import sun.plugin.dom.DOMObject;

import java.util.*;
import java.util.stream.Collectors;

public class TestStream {

    public static void main(String args[]) {
//
//        List<String> cmp = new ArrayList<>();
//        cmp.add("CPR-");
//        cmp.add("PSP-");


//        cmp.stream()
//                .find(s -> s.startsWith("PSP"));
               // .forEach(System.out::println);

        //System.out.println(  "P-JETT".matches("(CPR-|PSP-).*"));

        String[] mngPrc = {"8"};
        String prc = "5";

       // System.out.println(Arrays.stream(mngPrc).anyMatch(prc::equals));



     //   System.out.println(map.toString());

        //map();

        Object[] taxInfo = new Object[2];
        taxInfo[0] = Double.valueOf("2.5");
        taxInfo[1] = "Tax Adjustment";

        Object[] taxInfo2 = new Object[2];
        taxInfo2[0] = Double.valueOf("1");
        taxInfo2[1] = "Tax Adjustment";

        Map <String, Double> map2 = new HashMap<>();
        map2.put("101", Double.valueOf("20"));
        map2.put("102", Double.valueOf("100"));
        map2.put("103", Double.valueOf("-50"));
        map2.put("104", Double.valueOf("150"));
        map2.put("105", Double.valueOf("30"));
        map2.put("106", Double.valueOf("-70"));

        Map <String, Object[]> map3 = new HashMap<>();
        map3.put("101",taxInfo);
        map3.put("102",taxInfo2);

        Map.Entry<String, Double> maxEntry = Collections.max(map2.entrySet(),
                Map.Entry.comparingByValue());
       // System.out.println(maxEntry.getValue());

        Map result = map2.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (o, n) -> n, LinkedHashMap::new));

        Map syncMap = Collections.synchronizedMap(result);
      //  System.out.println(syncMap);


        Map<String,Double> resultMap = map2.entrySet().stream()
                .filter(map -> map.getValue() < 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
       // System.out.println(resultMap);



        String key = map3.entrySet().stream().filter(a -> (Double) a.getValue()[0] + Double.parseDouble("-2.5") >=0).findFirst().orElse(null).getKey();
        System.out.println(key);


        Map<String, Object[]> map4 = map3.entrySet().stream().filter(a -> (Double) a.getValue()[0] < 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//        String[] stringArray = { "Steve", "Rick", "Aditya", "Negan", "Lucy", "Sansa", "Jon"};
//        Arrays.sort(stringArray, String::compareToIgnoreCase);
//        Arrays.stream(stringArray).forEach(System.out::println);
//        for(String str: stringArray){
//            System.out.println(str);
//        }

        String field = "IDE_CUO_COD";

        String[] f = field.split("_");

        String fin= "";
      //  Arrays.stream(f).forEach(df -> fin = fin + "ds(" + f + ")");

    }


    public static void map(){

        Map<String, Double> map = new HashMap();
        map.put("101", Double.valueOf("1"));
        map.put("102", Double.valueOf("0"));

        map.values().removeIf(d -> d == 0.0d);

        Object[] taxInfo = new Object[2];
        taxInfo[0] = Double.valueOf("0");
        taxInfo[1] = "Tax Adjustment";

        Object[] taxInfo2 = new Object[2];
        taxInfo2[0] = Double.valueOf("1");
        taxInfo2[1] = "Tax Adjustment";

        Map <String, Object[]> map2 = new HashMap<>();
        map2.put("102", taxInfo);
        map2.put("103", taxInfo2);

        map2.values().removeIf(d -> Double.parseDouble(d[0].toString()) == 0.0d);

        System.out.println(map2);
    }

}
